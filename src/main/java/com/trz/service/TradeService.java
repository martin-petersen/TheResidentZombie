package com.trz.service;

import com.trz.config.Constants;
import com.trz.dto.CreateTradeDTO;
import com.trz.dto.ItemDTO;
import com.trz.dto.TradeDTO;
import com.trz.exception.BusinessException;
import com.trz.exception.EntityNotFoundException;
import com.trz.model.Inventory;
import com.trz.model.Item;
import com.trz.model.Survivor;
import com.trz.repository.InventoryRepository;
import com.trz.repository.ItemRepository;
import com.trz.repository.SurvivorRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TradeService {

    private final SurvivorRepository survivorRepository;

    private final ItemRepository itemRepository;

    private final InventoryRepository inventoryRepository;

    public TradeService(SurvivorRepository survivorRepository, ItemRepository itemRepository, InventoryRepository inventoryRepository) {
        this.survivorRepository = survivorRepository;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public TradeDTO exchange(UUID exchangeFromId, UUID exchangeToId, CreateTradeDTO createTradeDTO) {
        List<Survivor> survivors = getSurvivors(exchangeFromId, exchangeToId);
        Map<UUID, Map<String, Inventory>> survivorInventoryMap = mapInventories(survivors);
        Map<UUID, List<ItemDTO>> tradeRequestMap = mapTradeRequest(exchangeFromId, exchangeToId, createTradeDTO);
        validateExchange(survivors, survivorInventoryMap, tradeRequestMap, createTradeDTO);
        return trade(survivors, survivorInventoryMap, tradeRequestMap, exchangeFromId, exchangeToId, createTradeDTO);
    }

    private TradeDTO trade(List<Survivor> survivors, Map<UUID, Map<String, Inventory>> survivorInventoryMap, Map<UUID, List<ItemDTO>> tradeRequestMap, UUID exchangeFromId, UUID exchangeToId, CreateTradeDTO createTradeDTO) {
        List<Survivor> survivorList = addItemsIntoInventory(
                removeItemsFromInventory(survivors, survivorInventoryMap, tradeRequestMap),
                exchangeFromId,
                exchangeToId,
                createTradeDTO
        );

        TradeDTO tradeDTO = new TradeDTO();
        survivorList.forEach(survivor -> {
            if (survivor.getId().equals(exchangeFromId)) {
                tradeDTO.setSender(survivor.getName());
                survivor.getInventory().forEach(inventory -> tradeDTO.setPostTradeSenderItems(new ItemDTO(inventory.getItem().getItemName(), inventory.getAmount())));
            }
            if (survivor.getId().equals(exchangeToId)) {
                tradeDTO.setReceiver(survivor.getName());
                survivor.getInventory().forEach(inventory -> tradeDTO.setPostTradeReceiverItems(new ItemDTO(inventory.getItem().getItemName(), inventory.getAmount())));
            }
        });

        return tradeDTO;
    }

    private List<Survivor> addItemsIntoInventory(List<Survivor> survivors, UUID exchangeFromId, UUID exchangeToId, CreateTradeDTO createTradeDTO) {
        List<Item> itemList = itemRepository.findAll();
        Map<String, Item> itemMap = new HashMap<>();

        itemList.forEach(item -> itemMap.put(item.getItemName(), item));
        survivors.forEach(survivor -> {
            Map<String, Inventory> inventoryMap = new HashMap<>();
            List<Inventory> inventories = new ArrayList<>();
            survivor.getInventory().forEach(inventory -> inventoryMap.put(inventory.getItem().getItemName(), inventory));
            if (survivor.getId().equals(exchangeFromId)) {
                createTradeDTO.getDemand().forEach(itemDTO -> inventories.add(incrementInventory(itemMap, inventoryMap, survivor, itemDTO)));
            }
            if (survivor.getId().equals(exchangeToId)) {
                createTradeDTO.getSupply().forEach(itemDTO -> inventories.add(incrementInventory(itemMap, inventoryMap, survivor, itemDTO)));
            }
            survivor.setInventory(inventories);
        });

        survivorRepository.saveAll(survivors);
        return survivors;
    }

    private Inventory incrementInventory(Map<String, Item> itemMap, Map<String, Inventory> inventoryMap, Survivor s, ItemDTO i) {
        Item item = itemMap.get(i.getItem());
        Inventory inventory;

        if (inventoryMap.containsKey(i.getItem())) {
            inventory = inventoryMap.get(i.getItem());
        } else {
            inventory = createNewInventory(s, item, 0);
        }

        inventory.setAmount(inventory.getAmount() + i.getAmount());

        return inventory;
    }


    private Inventory createNewInventory(Survivor survivor, Item item, int i) {
        return new Inventory(inventoryRepository.getNextId(), survivor, item, i);
    }

    private List<Survivor> removeItemsFromInventory(List<Survivor> survivors, Map<UUID, Map<String, Inventory>> survivorInventoryMap, Map<UUID, List<ItemDTO>> tradeRequestMap) {
        survivors.forEach(survivor -> {
            Map<String, Inventory> inventoryMap = survivorInventoryMap.get(survivor.getId());
            List<ItemDTO> listItems = tradeRequestMap.get(survivor.getId());
            listItems.forEach(itemDTO -> {
                Inventory inventory = inventoryMap.get(itemDTO.getItem());
                inventory.setAmount(inventory.getAmount() - itemDTO.getAmount());
                inventoryMap.put(itemDTO.getItem(), inventory);
            });
            List<Inventory> inventories = new ArrayList<>(inventoryMap.values());
            survivor.setInventory(inventories);
        });
        return survivors;
    }

    private void validateExchange(List<Survivor> survivors, Map<UUID, Map<String, Inventory>> survivorInventoryMap, Map<UUID, List<ItemDTO>> tradeRequestMap, CreateTradeDTO createTradeDTO) throws BusinessException {
        areTheSurvivorsInfected(survivors);
        validateFairTrade(createTradeDTO);
        validateAmountOfEachSurvivorItems(survivors, survivorInventoryMap, tradeRequestMap);
    }

    private void validateFairTrade(CreateTradeDTO createTradeDTO) {
        int supply = 0;
        int demand = 0;
        List<Item> itemList = itemRepository.findAll();
        Map<String, Item> itemMap = new HashMap<>();
        itemList.forEach(item -> itemMap.put(item.getItemName(), item));
        for (ItemDTO i :
                createTradeDTO.getSupply()) {
            Item item = itemMap.get(i.getItem());
            supply += item.getPoints() * i.getAmount();
        }
        for (ItemDTO i :
                createTradeDTO.getDemand()) {
            Item item = itemMap.get(i.getItem());
            demand += item.getPoints() * i.getAmount();
        }
        if (supply != demand) {
            throw new BusinessException(Constants.POINTS_DOES_NOT_MATCH);
        }
    }

    private void validateAmountOfEachSurvivorItems(List<Survivor> survivors, Map<UUID, Map<String, Inventory>> survivorInventoryMap, Map<UUID, List<ItemDTO>> tradeRequestMap) {
        survivors.forEach(survivor -> {
            Map<String, Inventory> inventoryMap = survivorInventoryMap.get(survivor.getId());
            List<ItemDTO> listItems = tradeRequestMap.get(survivor.getId());
            listItems.forEach(itemDTO -> {
                if (!inventoryMap.containsKey(itemDTO.getItem())) {
                    throw new BusinessException(String.format(Constants.DO_NOT_HAVE_ITEM, survivor.getName(), itemDTO.getItem()));
                } else {
                    if (inventoryMap.get(itemDTO.getItem()).getAmount() < itemDTO.getAmount()) {
                        throw new BusinessException(String.format(Constants.DO_NOT_HAVE_ENOUGH_ITEM, survivor.getName(), itemDTO.getItem()));
                    }
                }
            });
        });
    }

    private void areTheSurvivorsInfected(List<Survivor> survivors) {
        survivors.forEach(survivor -> {
            if (survivor.isInfected()) {
                throw new BusinessException(String.format(Constants.SURVIVOR_TRADING_IS_A_ZOMBIE, survivor.getName()));
            }
        });
    }

    private Map<UUID, List<ItemDTO>> mapTradeRequest(UUID exchangeFromId, UUID exchangeToId, CreateTradeDTO createTradeDTO) {
        Map<UUID, List<ItemDTO>> tradeRequestMap = new HashMap<>();
        tradeRequestMap.put(exchangeFromId, createTradeDTO.getSupply());
        tradeRequestMap.put(exchangeToId, createTradeDTO.getDemand());
        return tradeRequestMap;
    }

    private List<Survivor> getSurvivors(UUID exchangeFromId, UUID exchangeToId) {
        List<Survivor> survivors = new ArrayList<>();
        survivors.add(survivorRepository.findById(exchangeFromId).orElseThrow(() -> new EntityNotFoundException(Constants.SURVIVOR_TRADING_NOT_FOUND)));
        survivors.add(survivorRepository.findById(exchangeToId).orElseThrow(() -> new EntityNotFoundException(Constants.SURVIVOR_TRADING_NOT_FOUND)));
        return survivors;
    }

    private Map<UUID, Map<String, Inventory>> mapInventories(List<Survivor> survivors) {
        Map<UUID, Map<String, Inventory>> survivorInventoryMap = new HashMap<>();
        survivors.forEach(survivor -> {
            Map<String, Inventory> inventoryMap = new HashMap<>();
            survivor.getInventory().forEach(inventory -> inventoryMap.put(inventory.getItem().getItemName(), inventory));
            survivorInventoryMap.put(survivor.getId(), inventoryMap);
        });
        return survivorInventoryMap;
    }
}