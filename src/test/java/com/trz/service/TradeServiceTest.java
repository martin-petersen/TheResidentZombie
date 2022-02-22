package com.trz.service;

import com.trz.dto.*;
import com.trz.exception.BusinessException;
import com.trz.exception.EntityNotFoundException;
import com.trz.model.Inventory;
import com.trz.model.Item;
import com.trz.model.LastLocation;
import com.trz.model.Survivor;
import com.trz.repository.InventoryRepository;
import com.trz.repository.ItemRepository;
import com.trz.repository.SurvivorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TradeServiceTest {

    private TradeService tradeService;

    @Mock
    private SurvivorRepository survivorRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tradeService = new TradeService(survivorRepository, itemRepository, inventoryRepository);
    }

    @Test
    void should_Emit_Survivor_Not_Found_Message() {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        CreateTradeDTO createTradeDTO = new CreateTradeDTO(itemDTOList, itemDTOList);


        when(survivorRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            tradeService.exchange(UUID.randomUUID(), UUID.randomUUID(), createTradeDTO);
        });

        assertEquals("one of the parts in this trade does not exist in database", exception.getMessage());
    }

    @Test
    void should_Emit_Survivor_Is_Infected_Message() {
        UUID survivorOne = UUID.randomUUID();
        UUID survivorTwo = UUID.randomUUID();

        ItemDTO itemDTO = new ItemDTO("AK47", 1);

        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("AK47");
        item.setPoints(8);

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(0);
        lastLocation.setLatitude(0);

        Survivor survivor1 = new Survivor();
        Survivor survivor2 = new Survivor();
        survivor1.setId(survivorOne);
        survivor1.setLastLocation(lastLocation);
        survivor1.setName("survivor");
        survivor1.setAge(44);
        survivor1.setGender("undefined");

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(UUID.randomUUID(), survivor1, item, 4));
        survivor1.setInventory(inventories);
        survivor2 = survivor1;
        survivor2.setId(survivorTwo);
        survivor2.setInfected(true);

        CreateTradeDTO createTradeDTO = new CreateTradeDTO(itemDTOList, itemDTOList);


        when(survivorRepository.findById(eq(survivorOne))).thenReturn(Optional.of(survivor1));
        when(survivorRepository.findById(eq(survivorTwo))).thenReturn(Optional.of(survivor2));

        Exception exception = assertThrows(BusinessException.class, () -> {
            tradeService.exchange(survivorOne, survivorTwo, createTradeDTO);
        });

        assertEquals("can't complete the trade, " + survivor2.getName() + " is now on the zombie team", exception.getMessage());
    }

    @Test
    void should_Emit_Survivor_Do_Not_Have_Item() {
        UUID survivorOne = UUID.randomUUID();
        UUID survivorTwo = UUID.randomUUID();

        ItemDTO itemDTO1 = new ItemDTO("First Aid Pouch", 8);
        ItemDTO itemDTO2 = new ItemDTO("AK47", 10);

        List<ItemDTO> itemDTOList1 = new ArrayList<>();
        itemDTOList1.add(itemDTO1);
        List<ItemDTO> itemDTOList2 = new ArrayList<>();
        itemDTOList2.add(itemDTO2);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setItem("Fuji Water");
        item1.setPoints(14);
        Item item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setItem("Campbell Soup");
        item2.setPoints(12);
        Item item3 = new Item();
        item3.setId(UUID.randomUUID());
        item3.setItem("First Aid Pouch");
        item3.setPoints(10);
        Item item4 = new Item();
        item4.setId(UUID.randomUUID());
        item4.setItem("AK47");
        item4.setPoints(8);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(0);
        lastLocation.setLatitude(0);

        Survivor survivor1 = new Survivor();
        Survivor survivor2 = new Survivor();
        survivor1.setId(survivorOne);
        survivor1.setLastLocation(lastLocation);
        survivor1.setName("survivor");
        survivor1.setAge(44);
        survivor1.setGender("undefined");

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(UUID.randomUUID(), null, item1, 4));
        inventories.add(new Inventory(UUID.randomUUID(), null, item2, 4));
        inventories.add(new Inventory(UUID.randomUUID(), null, item4, 10));
        survivor1.setInventory(inventories);
        survivor2.setInventory(inventories);
        survivor2.setId(survivorTwo);
        survivor2.setLastLocation(lastLocation);
        survivor2.setName("survivor");
        survivor2.setAge(44);
        survivor2.setGender("undefined");

        CreateTradeDTO createTradeDTO = new CreateTradeDTO(itemDTOList1, itemDTOList2);

        when(survivorRepository.findById(eq(survivorOne))).thenReturn(Optional.of(survivor1));
        when(survivorRepository.findById(eq(survivorTwo))).thenReturn(Optional.of(survivor2));
        when(itemRepository.findAll()).thenReturn(items);

        Exception exception = assertThrows(BusinessException.class, () -> {
            tradeService.exchange(survivorOne, survivorTwo, createTradeDTO);
        });

        assertEquals(survivor1.getName() + " doesn't have " + itemDTO1.getItem() + ", the trade can't be completed", exception.getMessage());
    }

    @Test
    void should_Emit_Survivor_Do_Not_Have_Item_Enough_Amount() {
        UUID survivorOne = UUID.randomUUID();
        UUID survivorTwo = UUID.randomUUID();

        ItemDTO itemDTO1 = new ItemDTO("First Aid Pouch", 8);
        ItemDTO itemDTO2 = new ItemDTO("AK47", 10);

        List<ItemDTO> itemDTOList1 = new ArrayList<>();
        itemDTOList1.add(itemDTO1);
        List<ItemDTO> itemDTOList2 = new ArrayList<>();
        itemDTOList2.add(itemDTO2);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setItem("Fuji Water");
        item1.setPoints(14);
        Item item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setItem("Campbell Soup");
        item2.setPoints(12);
        Item item3 = new Item();
        item3.setId(UUID.randomUUID());
        item3.setItem("First Aid Pouch");
        item3.setPoints(10);
        Item item4 = new Item();
        item4.setId(UUID.randomUUID());
        item4.setItem("AK47");
        item4.setPoints(8);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(0);
        lastLocation.setLatitude(0);

        Survivor survivor1 = new Survivor();
        Survivor survivor2 = new Survivor();
        survivor1.setId(survivorOne);
        survivor1.setLastLocation(lastLocation);
        survivor1.setName("survivor");
        survivor1.setAge(44);
        survivor1.setGender("undefined");

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(UUID.randomUUID(), null, item1, 4));
        inventories.add(new Inventory(UUID.randomUUID(), null, item2, 4));
        inventories.add(new Inventory(UUID.randomUUID(), null, item3, 4));
        inventories.add(new Inventory(UUID.randomUUID(), null, item4, 10));
        survivor1.setInventory(inventories);
        survivor2.setInventory(inventories);
        survivor2.setId(survivorTwo);
        survivor2.setLastLocation(lastLocation);
        survivor2.setName("survivor");
        survivor2.setAge(44);
        survivor2.setGender("undefined");

        CreateTradeDTO createTradeDTO = new CreateTradeDTO(itemDTOList1, itemDTOList2);

        when(survivorRepository.findById(eq(survivorOne))).thenReturn(Optional.of(survivor1));
        when(survivorRepository.findById(eq(survivorTwo))).thenReturn(Optional.of(survivor2));
        when(itemRepository.findAll()).thenReturn(items);

        Exception exception = assertThrows(BusinessException.class, () -> {
            tradeService.exchange(survivorOne, survivorTwo, createTradeDTO);
        });

        assertEquals(survivor1.getName() + " doesn't have enough " + itemDTO1.getItem() + ", the trade can't be completed", exception.getMessage());
    }

    @Test
    void should_Complete_The_Trade() {
        UUID survivorOne = UUID.randomUUID();
        UUID survivorTwo = UUID.randomUUID();

        ItemDTO itemDTO1 = new ItemDTO("First Aid Pouch", 4);
        ItemDTO itemDTO2 = new ItemDTO("AK47", 5);

        List<ItemDTO> itemDTOList1 = new ArrayList<>();
        itemDTOList1.add(itemDTO1);
        List<ItemDTO> itemDTOList2 = new ArrayList<>();
        itemDTOList2.add(itemDTO2);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setItem("Fuji Water");
        item1.setPoints(14);
        Item item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setItem("Campbell Soup");
        item2.setPoints(12);
        Item item3 = new Item();
        item3.setId(UUID.randomUUID());
        item3.setItem("First Aid Pouch");
        item3.setPoints(10);
        Item item4 = new Item();
        item4.setId(UUID.randomUUID());
        item4.setItem("AK47");
        item4.setPoints(8);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(0);
        lastLocation.setLatitude(0);

        Survivor survivor1 = new Survivor();
        Survivor survivor2 = new Survivor();
        survivor1.setId(survivorOne);
        survivor1.setLastLocation(lastLocation);
        survivor1.setName("survivorOne");
        survivor1.setAge(44);
        survivor1.setGender("undefined");

        List<Inventory> inventories1 = new ArrayList<>();
        List<Inventory> inventories2 = new ArrayList<>();
        inventories1.add(new Inventory(UUID.randomUUID(), null, item1, 10));
        inventories1.add(new Inventory(UUID.randomUUID(), null, item2, 10));
        inventories1.add(new Inventory(UUID.randomUUID(), null, item3, 10));
        inventories1.add(new Inventory(UUID.randomUUID(), null, item4, 10));
        inventories2.add(new Inventory(UUID.randomUUID(), null, item1, 10));
        inventories2.add(new Inventory(UUID.randomUUID(), null, item2, 10));
        inventories2.add(new Inventory(UUID.randomUUID(), null, item3, 10));
        inventories2.add(new Inventory(UUID.randomUUID(), null, item4, 10));
        survivor1.setInventory(inventories1);
        survivor2.setInventory(inventories2);
        survivor2.setId(survivorTwo);
        survivor2.setLastLocation(lastLocation);
        survivor2.setName("survivorTwo");
        survivor2.setAge(44);
        survivor2.setGender("undefined");

        CreateTradeDTO createTradeDTO = new CreateTradeDTO(itemDTOList1, itemDTOList2);

        when(survivorRepository.findById(eq(survivorOne))).thenReturn(Optional.of(survivor1));
        when(survivorRepository.findById(eq(survivorTwo))).thenReturn(Optional.of(survivor2));
        when(itemRepository.findAll()).thenReturn(items);

        TradeDTO tradeDTO = tradeService.exchange(survivorOne, survivorTwo, createTradeDTO);

        assertEquals("survivorOne", tradeDTO.getSender());
        assertEquals("survivorTwo", tradeDTO.getReceiver());
        assertEquals(15, tradeDTO.getPostTradeSenderItems().get(0).getAmount());
        assertEquals(14, tradeDTO.getPostTradeReceiverItems().get(0).getAmount());
    }
}