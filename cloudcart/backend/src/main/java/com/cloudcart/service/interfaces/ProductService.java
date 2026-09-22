package com.cloudcart.service.interfaces;

import com.cloudcart.dto.ProductRequest;
import com.cloudcart.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts(String category);
    ProductResponse getProductById(Long id);
    ProductResponse getProductBySku(String sku);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
