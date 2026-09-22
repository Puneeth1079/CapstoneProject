package com.cloudcart.service.implementation;

import com.cloudcart.dto.*;
import com.cloudcart.entity.*;
import com.cloudcart.exception.InsufficientInventoryException;
import com.cloudcart.exception.InvalidOrderException;
import com.cloudcart.exception.ResourceNotFoundException;
import com.cloudcart.repository.*;
import com.cloudcart.service.interfaces.FaultSimulationService;
import com.cloudcart.service.interfaces.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final FaultSimulationService faultSimulationService;
    private final MeterRegistry meterRegistry;

    private Counter ordersCreatedCounter;
    private Counter ordersFailedCounter;
    private Timer orderProcessingTimer;

    @PostConstruct
    public void initMetrics() {
        this.ordersCreatedCounter = Counter.builder("cloudcart.orders.created")
                .description("Total number of successful order placements")
                .register(meterRegistry);

        this.ordersFailedCounter = Counter.builder("cloudcart.orders.failed")
                .description("Total number of failed order placements")
                .register(meterRegistry);

        this.orderProcessingTimer = Timer.builder("cloudcart.order.processing.time")
                .description("Time taken to process an order creation")
                .register(meterRegistry);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        return orderProcessingTimer.record(() -> {
            faultSimulationService.evaluateAndApplyFault();

            if (faultSimulationService.shouldFailTransaction()) {
                ordersFailedCounter.increment();
                log.error("Order processing aborted due to FAILED_TRANSACTIONS fault mode simulation");
                throw new InvalidOrderException("Order placement failed due to active FAILED_TRANSACTIONS fault simulation");
            }

            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> {
                        ordersFailedCounter.increment();
                        return new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId());
                    });

            if (request.getItems() == null || request.getItems().isEmpty()) {
                ordersFailedCounter.increment();
                throw new InvalidOrderException("Order must contain at least one item");
            }

            String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Order order = Order.builder()
                    .orderNumber(orderNumber)
                    .customer(customer)
                    .totalAmount(BigDecimal.ZERO)
                    .status("COMPLETED")
                    .items(new ArrayList<>())
                    .build();

            BigDecimal totalAmount = BigDecimal.ZERO;

            for (OrderItemRequest itemReq : request.getItems()) {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> {
                            ordersFailedCounter.increment();
                            return new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId());
                        });

                Inventory inventory = inventoryRepository.findByProductId(product.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory record missing for product id: " + product.getId()));

                int availableStock = inventory.getQuantity() - inventory.getReservedQuantity();
                if (availableStock < itemReq.getQuantity()) {
                    ordersFailedCounter.increment();
                    throw new InsufficientInventoryException("Insufficient inventory for product '" + product.getName()
                            + "'. Requested: " + itemReq.getQuantity() + ", Available: " + availableStock);
                }

                inventory.setQuantity(inventory.getQuantity() - itemReq.getQuantity());
                inventoryRepository.save(inventory);

                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
                totalAmount = totalAmount.add(subtotal);

                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(itemReq.getQuantity())
                        .unitPrice(product.getPrice())
                        .subtotal(subtotal)
                        .build();

                order.addItem(orderItem);
            }

            order.setTotalAmount(totalAmount);
            Order savedOrder = orderRepository.save(order);

            ordersCreatedCounter.increment();
            log.info("Order created successfully: {} total=${}", savedOrder.getOrderNumber(), savedOrder.getTotalAmount());
            return mapToResponse(savedOrder);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        faultSimulationService.evaluateAndApplyFault();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        faultSimulationService.evaluateAndApplyFault();
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        faultSimulationService.evaluateAndApplyFault();
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(Long customerId) {
        faultSimulationService.evaluateAndApplyFault();
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        faultSimulationService.evaluateAndApplyFault();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        order.setStatus(status.toUpperCase());
        Order updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardSummary() {
        faultSimulationService.evaluateAndApplyFault();
        long totalProducts = productRepository.count();
        long totalCustomers = customerRepository.count();
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus("PENDING");
        long completedOrders = orderRepository.countByStatus("COMPLETED");

        List<Order> recentOrdersList = orderRepository.findTop10ByOrderByCreatedAtDesc();
        BigDecimal totalRevenue = recentOrdersList.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long lowStockCount = inventoryRepository.findAll().stream()
                .filter(inv -> (inv.getQuantity() - inv.getReservedQuantity()) <= inv.getMinThreshold())
                .count();

        List<OrderResponse> recentResponses = recentOrdersList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalCustomers(totalCustomers)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrders)
                .totalRevenue(totalRevenue)
                .lowStockCount(lowStockCount)
                .activeFaultMode(faultSimulationService.getFaultMode().getActiveMode())
                .recentOrders(recentResponses)
                .build();
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .productSku(item.getProduct().getSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .customerEmail(order.getCustomer().getEmail())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
