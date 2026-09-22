package com.cloudcart.service.implementation;

import com.cloudcart.dto.InventoryResponse;
import com.cloudcart.dto.InventoryUpdateRequest;
import com.cloudcart.entity.Inventory;
import com.cloudcart.exception.ResourceNotFoundException;
import com.cloudcart.repository.InventoryRepository;
import com.cloudcart.service.interfaces.FaultSimulationService;
import com.cloudcart.service.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final FaultSimulationService faultSimulationService;

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        faultSimulationService.evaluateAndApplyFault();
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(Long productId) {
        faultSimulationService.evaluateAndApplyFault();
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found for product id: " + productId));
        return mapToResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Long productId, InventoryUpdateRequest request) {
        faultSimulationService.evaluateAndApplyFault();
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found for product id: " + productId));

        inventory.setQuantity(request.getQuantity());
        if (request.getMinThreshold() != null) {
            inventory.setMinThreshold(request.getMinThreshold());
        }

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToResponse(updatedInventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        int available = Math.max(0, inventory.getQuantity() - inventory.getReservedQuantity());
        boolean lowStock = available <= inventory.getMinThreshold();

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProduct().getId())
                .productName(inventory.getProduct().getName())
                .productSku(inventory.getProduct().getSku())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(available)
                .minThreshold(inventory.getMinThreshold())
                .lowStock(lowStock)
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
