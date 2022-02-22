package com.trz.service;

import com.trz.dto.AnalyticsDTO;
import com.trz.model.Inventory;
import com.trz.model.Item;
import com.trz.repository.InventoryRepository;
import com.trz.repository.SurvivorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {

    private AnalyticsService analyticsService;

    @Mock
    private SurvivorRepository survivorRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        analyticsService = new AnalyticsService(survivorRepository, inventoryRepository);
    }

    @Test
    void infected_Should_Be_Equals_To_One_Hundred_Percent() {
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(true)).thenReturn(100L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(100, analyticsDTO.getPercentOfInfected());
    }

    @Test
    void survivors_Should_Be_Equals_To_Fifty_Percent() {
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(50, analyticsDTO.getGetPercentOfSurvivors());
    }

    @Test
    void average_Amount_Of_Items_Must_Be_5_Fiji_Water() {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("Fiji Water");
        List<Inventory> survivorInventories = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setItem(item);
        inventory1.setAmount(7);
        Inventory inventory2 = new Inventory();
        inventory2.setItem(item);
        inventory2.setAmount(5);
        Inventory inventory3 = new Inventory();
        inventory3.setItem(item);
        inventory3.setAmount(3);
        survivorInventories.add(inventory1);
        survivorInventories.add(inventory2);
        survivorInventories.add(inventory3);
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        when(inventoryRepository.findBySurvivor_Infected(false)).thenReturn(survivorInventories);
        when(survivorRepository.countAllByInfected(false)).thenReturn(3L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(5, analyticsDTO.getAmountOfItemsPerSurvivor().get(0).getAmount());
    }

    @Test
    void average_Amount_Of_Items_Must_Be_8_Campbell_Soup() {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("Campbell Soup");
        List<Inventory> survivorInventories = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setItem(item);
        inventory1.setAmount(20);
        Inventory inventory2 = new Inventory();
        inventory2.setItem(item);
        inventory2.setAmount(2);
        Inventory inventory3 = new Inventory();
        inventory3.setItem(item);
        inventory3.setAmount(2);
        survivorInventories.add(inventory1);
        survivorInventories.add(inventory2);
        survivorInventories.add(inventory3);
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        when(inventoryRepository.findBySurvivor_Infected(false)).thenReturn(survivorInventories);
        when(survivorRepository.countAllByInfected(false)).thenReturn(3L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(8, analyticsDTO.getAmountOfItemsPerSurvivor().get(0).getAmount());
    }

    @Test
    void average_Amount_Of_Items_Must_Be_13_First_Aid_Pouch() {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("First Aid Pouch");
        List<Inventory> survivorInventories = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setItem(item);
        inventory1.setAmount(9);
        Inventory inventory2 = new Inventory();
        inventory2.setItem(item);
        inventory2.setAmount(16);
        Inventory inventory3 = new Inventory();
        inventory3.setItem(item);
        inventory3.setAmount(14);
        survivorInventories.add(inventory1);
        survivorInventories.add(inventory2);
        survivorInventories.add(inventory3);
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        when(inventoryRepository.findBySurvivor_Infected(false)).thenReturn(survivorInventories);
        when(survivorRepository.countAllByInfected(false)).thenReturn(3L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(13, analyticsDTO.getAmountOfItemsPerSurvivor().get(0).getAmount());
    }

    @Test
    void average_Amount_Of_Items_Must_Be_7_AK47() {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("AK47");
        List<Inventory> survivorInventories = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setItem(item);
        inventory1.setAmount(1);
        Inventory inventory2 = new Inventory();
        inventory2.setItem(item);
        inventory2.setAmount(9);
        Inventory inventory3 = new Inventory();
        inventory3.setItem(item);
        inventory3.setAmount(11);
        survivorInventories.add(inventory1);
        survivorInventories.add(inventory2);
        survivorInventories.add(inventory3);
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        when(inventoryRepository.findBySurvivor_Infected(false)).thenReturn(survivorInventories);
        when(survivorRepository.countAllByInfected(false)).thenReturn(3L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(7, analyticsDTO.getAmountOfItemsPerSurvivor().get(0).getAmount());
    }

    @Test
    void total_Points_Lost_Due_Infection_Must_Be_436() {
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setItem("Fiji Water");
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
        List<Inventory> survivorInventories = new ArrayList<>();
        Inventory inventory1 = new Inventory();
        inventory1.setItem(item1);
        inventory1.setAmount(15);
        Inventory inventory2 = new Inventory();
        inventory2.setItem(item2);
        inventory2.setAmount(9);
        Inventory inventory3 = new Inventory();
        inventory3.setItem(item3);
        inventory3.setAmount(11);
        Inventory inventory4 = new Inventory();
        inventory4.setItem(item4);
        inventory4.setAmount(1);
        survivorInventories.add(inventory1);
        survivorInventories.add(inventory2);
        survivorInventories.add(inventory3);
        survivorInventories.add(inventory4);
        when(survivorRepository.count()).thenReturn(100L);
        when(survivorRepository.countAllByInfected(false)).thenReturn(50L);
        when(inventoryRepository.findBySurvivor_Infected(true)).thenReturn(survivorInventories);
        when(survivorRepository.countAllByInfected(false)).thenReturn(3L);
        AnalyticsDTO analyticsDTO = analyticsService.analytics();
        assertEquals(436, analyticsDTO.getTotalPointsLostDueInfection());
    }
}