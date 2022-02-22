package com.trz.service;

import com.trz.config.Constants;
import com.trz.dto.CreateSurvivorDTO;
import com.trz.dto.SurvivorDTO;
import com.trz.dto.UpdateLocationDTO;
import com.trz.exception.BusinessException;
import com.trz.exception.EntityNotFoundException;
import com.trz.model.Inventory;
import com.trz.model.Item;
import com.trz.model.LastLocation;
import com.trz.model.Survivor;
import com.trz.repository.InventoryRepository;
import com.trz.repository.ItemRepository;
import com.trz.repository.SurvivorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SurvivorService {

    private final SurvivorRepository survivorRepository;

    private final InventoryRepository inventoryRepository;

    private final ItemRepository itemRepository;

    public SurvivorService(SurvivorRepository survivorRepository, InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.survivorRepository = survivorRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public SurvivorDTO create(CreateSurvivorDTO createSurvivorDTO) {
        Survivor survivor = createBasicSurvivor(createSurvivorDTO);
        List<Inventory> items = createSurvivorListOfItems(survivor, createSurvivorDTO);
        return createSurvivor(survivor, items);
    }

    private SurvivorDTO createSurvivor(Survivor survivor1, List<Inventory> items) {
        Survivor survivor = survivorRepository.findById(survivor1.getId()).orElseThrow(() -> new EntityNotFoundException("survivor not found in database"));
        survivor.setInventory(items);
        survivorRepository.save(survivor);
        return new SurvivorDTO(survivor);
    }

    private Survivor createBasicSurvivor(CreateSurvivorDTO createSurvivorDTO) {
        if (createSurvivorDTO.getLatitude() > 90 || createSurvivorDTO.getLatitude() < -90) {
            throw new BusinessException(Constants.LATITUDE_OUT_OF_BOUND);
        }
        if (createSurvivorDTO.getLongitude() > 180 || createSurvivorDTO.getLongitude() < -180) {
            throw new BusinessException(Constants.LONGITUDE_OUT_OF_BOUND);
        }
        if (createSurvivorDTO.getAge() < 18) {
            throw new BusinessException(Constants.SURVIVOR_IS_UNDERAGE);
        }

        Survivor survivor = new Survivor();
        survivor.setId(survivorRepository.getNextId());
        survivor.setName(createSurvivorDTO.getName());
        survivor.setAge(createSurvivorDTO.getAge());
        survivor.setGender(createSurvivorDTO.getGender());

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLatitude(createSurvivorDTO.getLatitude());
        lastLocation.setLongitude(createSurvivorDTO.getLongitude());
        survivor.setLastLocation(lastLocation);

        survivorRepository.save(survivor);
        return survivor;
    }

    private List<Inventory> createSurvivorListOfItems(Survivor survivor, CreateSurvivorDTO createSurvivorDTO) {
        List<Inventory> inventories = new ArrayList<>();
        List<Item> items = itemRepository.findAll();
        Map<String, Item> mapItems = new HashMap<>();
        items.forEach(item -> mapItems.put(item.getItemName().toLowerCase(), item));
        createSurvivorDTO.getItems().forEach(itemDTO -> {
            if (!mapItems.containsKey(itemDTO.getItem().toLowerCase())) {
                throw new BusinessException(String.format(Constants.ITEM_NOT_FOUND, itemDTO.getItem()));
            }
            inventories.add(new Inventory(
                    inventoryRepository.getNextId(),
                    survivor,
                    mapItems.get(itemDTO.getItem().toLowerCase()),
                    itemDTO.getAmount()
            ));
        });
        return inventories;
    }

    @Transactional
    public SurvivorDTO updateLocation(UUID id, UpdateLocationDTO updateLocationDTO) {
        LastLocation updateLocation = new LastLocation();
        updateLocation.setLatitude(updateLocationDTO.getLatitude());
        updateLocation.setLongitude(updateLocationDTO.getLongitude());
        Survivor survivor = survivorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constants.SURVIVOR_NOT_FOUND));
        survivor.setLastLocation(updateLocation);
        survivorRepository.save(survivor);
        return new SurvivorDTO(survivor);
    }
}
