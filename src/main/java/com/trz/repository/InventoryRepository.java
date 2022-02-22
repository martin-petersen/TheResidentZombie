package com.trz.repository;

import com.trz.model.Inventory;
import com.trz.model.Survivor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    List<Inventory> findBySurvivor(Survivor survivor);

    default UUID getNextId() {
        return UUID.randomUUID();
    }

    List<Inventory> findBySurvivor_Infected(boolean survivorInfected);
}
