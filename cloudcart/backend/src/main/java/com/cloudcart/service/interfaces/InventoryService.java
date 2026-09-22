package com.cloudcart.service.interfaces;

import com.cloudcart.dto.InventoryResponse;
import com.cloudcart.dto.InventoryUpdateRequest;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getAllInventory();
    InventoryResponse getInventoryByProductId(Long productId);
    InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request);
}
