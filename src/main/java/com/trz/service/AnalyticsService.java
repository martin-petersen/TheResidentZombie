package com.trz.service;

import com.trz.dto.AnalyticsDTO;
import com.trz.dto.ItemDTO;
import com.trz.model.Inventory;
import com.trz.repository.InventoryRepository;
import com.trz.repository.SurvivorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final SurvivorRepository survivorRepository;

    private final InventoryRepository inventoryRepository;

    public AnalyticsService(SurvivorRepository survivorRepository, InventoryRepository inventoryRepository) {
        this.survivorRepository = survivorRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public AnalyticsDTO analytics() {
        return new AnalyticsDTO(
                percentOfInfected(),
                percentOfSurvivors(),
                amountOfItemsPerSurvivor(),
                totalPointsLostDueInfection()
        );
    }

    private int totalPointsLostDueInfection() {
        int sum;
        List<Inventory> zombieInventories = inventoryRepository.findBySurvivor_Infected(true);
        sum = zombieInventories.stream().mapToInt(inventory -> (inventory.getAmount() * inventory.getItem().getPoints())).sum();
        return sum;
    }

    private List<ItemDTO> amountOfItemsPerSurvivor() {
        List<Inventory> survivorInventories = inventoryRepository.findBySurvivor_Infected(false);
        int totalNonInfected = (int)survivorRepository.countAllByInfected(false);
        Map<String,ItemDTO> itemDTOMap = new HashMap<>();
        survivorInventories.forEach(inventory -> {
            if(itemDTOMap.containsKey(inventory.getItem().getItemName())) {
                ItemDTO itemDTO = itemDTOMap.get(inventory.getItem().getItemName());
                itemDTO.setAmount(itemDTO.getAmount() + inventory.getAmount());
                itemDTOMap.put(inventory.getItem().getItemName(),itemDTO);
            } else {
                itemDTOMap.put(inventory.getItem().getItemName(),new ItemDTO(
                                inventory.getItem().getItemName(),
                                inventory.getAmount()
                        )
                );
            }
        });
        List<ItemDTO> itemDTOList = new ArrayList<>(itemDTOMap.values());
        itemDTOList.forEach(itemDTO -> itemDTO.setAmount(itemDTO.getAmount() / totalNonInfected));
        return itemDTOList;
    }

    private long percentOfSurvivors() {
        long totalInDatabase = survivorRepository.count();
        long totalNonInfected = survivorRepository.countAllByInfected(false);
        return totalNonInfected * 100 / totalInDatabase;
    }

    private long percentOfInfected() {
        long totalInDatabase = survivorRepository.count();
        long totalInfected = survivorRepository.countAllByInfected(true);
        return totalInfected * 100 / totalInDatabase;
    }
}
