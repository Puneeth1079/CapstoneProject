package com.cloudcart.service.implementation;

import com.cloudcart.dto.ProductRequest;
import com.cloudcart.dto.ProductResponse;
import com.cloudcart.entity.Inventory;
import com.cloudcart.entity.Product;
import com.cloudcart.exception.ResourceNotFoundException;
import com.cloudcart.repository.InventoryRepository;
import com.cloudcart.repository.ProductRepository;
import com.cloudcart.service.interfaces.FaultSimulationService;
import com.cloudcart.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final FaultSimulationService faultSimulationService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String category) {
        faultSimulationService.evaluateAndApplyFault();
        List<Product> products = (category != null && !category.isBlank())
                ? productRepository.findByCategory(category)
                : productRepository.findAll();

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        faultSimulationService.evaluateAndApplyFault();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku) {
        faultSimulationService.evaluateAndApplyFault();
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        faultSimulationService.evaluateAndApplyFault();
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .sku(request.getSku())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl() != null && !request.getImageUrl().isBlank()
                        ? request.getImageUrl()
                        : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400")
                .status("ACTIVE")
                .build();

        Product savedProduct = productRepository.save(product);

        int initialStock = request.getInitialStock() != null ? request.getInitialStock() : 100;
        Inventory inventory = Inventory.builder()
                .product(savedProduct)
                .quantity(initialStock)
                .reservedQuantity(0)
                .minThreshold(10)
                .build();

        inventoryRepository.save(inventory);

        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        faultSimulationService.evaluateAndApplyFault();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        faultSimulationService.evaluateAndApplyFault();
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        Integer available = inventoryRepository.findByProductId(product.getId())
                .map(inv -> inv.getQuantity() - inv.getReservedQuantity())
                .orElse(0);

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .sku(product.getSku())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .availableStock(available)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
