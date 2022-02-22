package com.trz.service;

import com.trz.dto.CreateSurvivorDTO;
import com.trz.dto.ItemDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SurvivorServiceTest {

    private SurvivorService survivorService;

    @Mock
    private SurvivorRepository survivorRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        survivorService = new SurvivorService(survivorRepository, inventoryRepository, itemRepository);
    }

    @Test
    void should_Emit_Invalid_Latitude_Message() {
        Exception exception = assertThrows(BusinessException.class, () -> {
            survivorService.create(new CreateSurvivorDTO(-91, 0));
        });

        assertEquals("there's no such a place with this latitude", exception.getMessage());
    }

    @Test
    void should_Emit_Invalid_Longitude_Message() {
        Exception exception = assertThrows(BusinessException.class, () -> {
            survivorService.create(new CreateSurvivorDTO(0, 181));
        });

        assertEquals("there's no such a place with this longitude", exception.getMessage());
    }

    @Test
    void should_Emit_Survivor_Is_Underage() {
        Exception exception = assertThrows(BusinessException.class, () -> {
            survivorService.create(new CreateSurvivorDTO(10));
        });

        assertEquals("unfortunately minors can't be considered survivors in this apocalypse", exception.getMessage());
    }

    @Test
    void should_Emit_Item_Not_Found_Message() {
        UUID uuid = UUID.randomUUID();

        ItemDTO itemDTO = new ItemDTO("Item", 1);

        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

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

        CreateSurvivorDTO createSurvivorDTO = new CreateSurvivorDTO("survivor", 44, "undefined", itemDTOList, 0, 0);

        when(survivorRepository.getNextId()).thenReturn(uuid);
        when(itemRepository.findAll()).thenReturn(items);
        when(survivorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            survivorService.create(createSurvivorDTO);
        });

        assertEquals(itemDTO.getItem() + " not found in database, make sure you're writing everything correct", exception.getMessage());
    }

    @Test
    void create_Should_Emit_Survivor_Not_Found_Message() {
        UUID uuid = UUID.randomUUID();

        ItemDTO itemDTO = new ItemDTO("AK47", 1);

        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("AK47");
        item.setPoints(8);
        items.add(item);

        CreateSurvivorDTO createSurvivorDTO = new CreateSurvivorDTO("survivor", 44, "undefined", itemDTOList, 0, 0);

        when(survivorRepository.getNextId()).thenReturn(uuid);
        when(itemRepository.findAll()).thenReturn(items);
        when(survivorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            survivorService.create(createSurvivorDTO);
        });

        assertEquals("survivor not found in database", exception.getMessage());
    }

    @Test
    void should_Create_Return_Correct_SurvivorDTO() {
        UUID uuid = UUID.randomUUID();

        ItemDTO itemDTO = new ItemDTO("AK47", 1);
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setItem("AK47");
        item.setPoints(8);
        items.add(item);

        CreateSurvivorDTO createSurvivorDTO = new CreateSurvivorDTO("survivor", 44, "undefined", itemDTOList, 0, 0);

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(0);
        lastLocation.setLatitude(0);

        Survivor testSurvivor = new Survivor();
        testSurvivor.setId(uuid);
        testSurvivor.setLastLocation(lastLocation);
        testSurvivor.setName("survivor");
        testSurvivor.setAge(44);
        testSurvivor.setGender("undefined");

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(new Inventory(UUID.randomUUID(), testSurvivor, item, 4));
        testSurvivor.setInventory(inventories);
        SurvivorDTO survivor = new SurvivorDTO(uuid, "survivor", 44, "undefined", lastLocation);

        when(survivorRepository.getNextId()).thenReturn(uuid);
        when(itemRepository.findAll()).thenReturn(items);
        when(survivorRepository.findById(any())).thenReturn(Optional.of(testSurvivor));

        SurvivorDTO survivorDTO = survivorService.create(createSurvivorDTO);

        assertEquals(survivor.getId(), survivorDTO.getId());
        assertEquals(survivor.getName(), survivorDTO.getName());
        assertEquals(survivor.getAge(), survivorDTO.getAge());
        assertEquals(survivor.getGender(), survivorDTO.getGender());
        assertEquals("[Item: AK47, Amount: 1]", survivorDTO.getItems().toString());
        assertEquals(survivor.getLastLocation().getLatitude(), survivorDTO.getLastLocation().getLatitude());
        assertEquals(survivor.getLastLocation().getLongitude(), survivorDTO.getLastLocation().getLongitude());
    }

    @Test
    void update_Should_Emit_Survivor_Not_Found_Message() {
        UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO(0, 0);

        when(survivorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            survivorService.updateLocation(UUID.randomUUID(), updateLocationDTO);
        });

        assertEquals("survivor not found in database", exception.getMessage());
    }

    @Test
    void update_Must_Be_Successfully_Done() {
        UUID uuid = UUID.randomUUID();

        UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO(0, 0);

        List<Inventory> inventories = new ArrayList<>();

        Survivor survivor = new Survivor();
        survivor.setId(uuid);
        survivor.setInventory(inventories);

        when(survivorRepository.findById(any())).thenReturn(Optional.of(survivor));

        SurvivorDTO survivorDTO = survivorService.updateLocation(uuid, updateLocationDTO);


        assertEquals(updateLocationDTO.getLongitude(), survivorDTO.getLastLocation().getLongitude());
        assertEquals(updateLocationDTO.getLatitude(), survivorDTO.getLastLocation().getLatitude());
        assertEquals(uuid, survivorDTO.getId());
    }
}