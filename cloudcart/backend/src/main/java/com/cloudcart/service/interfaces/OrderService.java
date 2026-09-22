package com.cloudcart.service.interfaces;

import com.cloudcart.dto.DashboardResponse;
import com.cloudcart.dto.OrderRequest;
import com.cloudcart.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    OrderResponse getOrderByOrderNumber(String orderNumber);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getCustomerOrders(Long customerId);
    OrderResponse updateOrderStatus(Long id, String status);
    DashboardResponse getDashboardSummary();
}
