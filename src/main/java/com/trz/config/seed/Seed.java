package com.trz.config.seed;

import com.trz.model.Inventory;
import com.trz.model.Item;
import com.trz.model.LastLocation;
import com.trz.model.Survivor;
import com.trz.repository.InventoryRepository;
import com.trz.repository.ItemRepository;
import com.trz.repository.SurvivorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Seed {

    private final ItemRepository itemRepository;

    private final SurvivorRepository survivorRepository;

    private final InventoryRepository inventoryRepository;

    public Seed(ItemRepository itemRepository, SurvivorRepository survivorRepository, InventoryRepository inventoryRepository) {
        this.itemRepository = itemRepository;
        this.survivorRepository = survivorRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void seedItem() {
        if(itemRepository.count() > 0) {
            return;
        }
        List<Item> listItems = new ArrayList<>();
        listItems.add(new Item(itemRepository.getNextId(),"Fuji Water",14));
        listItems.add(new Item(itemRepository.getNextId(),"Campbell Soup",12));
        listItems.add(new Item(itemRepository.getNextId(),"First Aid Pouch",10));
        listItems.add(new Item(itemRepository.getNextId(),"AK47",8));

        itemRepository.saveAll(listItems);
    }

    public void seedSurvivor() {
        List<Item> items = itemRepository.findAll();
        List<Inventory> inventories1 = new ArrayList<>();
        List<Inventory> inventories2 = new ArrayList<>();
        List<Inventory> inventories3 = new ArrayList<>();
        List<Inventory> inventories4 = new ArrayList<>();
        List<Inventory> inventories5 = new ArrayList<>();
        List<Inventory> inventories6 = new ArrayList<>();

        LastLocation lastLocation = new LastLocation();
        lastLocation.setLongitude(-87.6565);
        lastLocation.setLatitude(-12.4787);

        Survivor survivor1 = new Survivor();
        survivor1.setId(UUID.fromString("ffcb1e19-c9cf-42a3-a0b9-47f105985d23"));
        survivor1.setAge(22);
        survivor1.setName("Thomas");
        survivor1.setGender("Male");
        survivor1.setLastLocation(lastLocation);
        survivorRepository.save(survivor1);
        items.forEach(item -> inventories1.add(new Inventory(inventoryRepository.getNextId(),survivor1,item,10)));
        survivor1.setInventory(inventories1);
        survivorRepository.save(survivor1);

        Survivor survivor2 = new Survivor();
        survivor2.setId(UUID.fromString("2a0197bf-2010-4cd9-ab64-195a3a8869bf"));
        survivor2.setAge(22);
        survivor2.setName("Mary");
        survivor2.setGender("Female");
        survivor2.setLastLocation(lastLocation);
        survivorRepository.save(survivor2);
        items.forEach(item -> inventories2.add(new Inventory(inventoryRepository.getNextId(),survivor2,item,10)));
        survivor2.setInventory(inventories2);
        survivorRepository.save(survivor2);

        Survivor survivor3 = new Survivor();
        survivor3.setId(UUID.fromString("eac4f52b-bf97-4e57-9671-08a50bf806e9"));
        survivor3.setAge(22);
        survivor3.setName("Billie Jean");
        survivor3.setGender("Female");
        survivor3.setLastLocation(lastLocation);
        survivorRepository.save(survivor3);
        inventories3.add(new Inventory(inventoryRepository.getNextId(),survivor3, items.get(2), 10));
        survivor3.setInventory(inventories3);
        survivorRepository.save(survivor3);

        Survivor survivor4 = new Survivor();
        survivor4.setId(UUID.fromString("002463ab-6232-44fb-b78b-c47fedaf30a4"));
        survivor4.setAge(22);
        survivor4.setName("Michael");
        survivor4.setGender("Male");
        survivor4.setLastLocation(lastLocation);
        survivorRepository.save(survivor4);
        inventories4.add(new Inventory(inventoryRepository.getNextId(),survivor4, items.get(3), 10));
        survivor4.setInventory(inventories4);
        survivorRepository.save(survivor4);

        Survivor survivor5 = new Survivor();
        survivor5.setId(UUID.fromString("263b62d2-6972-11eb-9439-0242ac130002"));
        survivor5.setAge(22);
        survivor5.setName("Domino");
        survivor5.setGender("Female");
        survivor5.setLastLocation(lastLocation);
        survivorRepository.save(survivor5);
        items.forEach(item -> inventories5.add(new Inventory(inventoryRepository.getNextId(),survivor5,item,10)));
        survivor5.setInventory(inventories5);
        survivorRepository.save(survivor5);

        Survivor survivor6 = new Survivor();
        survivor6.setId(UUID.fromString("3f2551fe-6972-11eb-9439-0242ac130002"));
        survivor6.setAge(22);
        survivor6.setName("Ray");
        survivor6.setGender("Male");
        survivor6.setLastLocation(lastLocation);
        survivorRepository.save(survivor6);
        items.forEach(item -> inventories6.add(new Inventory(inventoryRepository.getNextId(),survivor6,item,10)));
        survivor6.setInventory(inventories6);
        survivorRepository.save(survivor6);
    }
}
