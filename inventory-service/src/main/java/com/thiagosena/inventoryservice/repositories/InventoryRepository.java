package com.thiagosena.inventoryservice.repositories;

import com.thiagosena.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}