package com.trz.dto;

import com.trz.model.LastLocation;
import com.trz.model.Survivor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SurvivorDTO {

    private final UUID id;
    private final String name;
    private final int age;
    private final String gender;
    private final List<String> items = new ArrayList<>();
    private final LastLocation lastLocation;

    public SurvivorDTO(Survivor s) {
        this.id = s.getId();
        this.name = s.getName();
        this.age = s.getAge();
        this.gender = s.getGender();
        s.getInventory().forEach(inventory -> items.add("Item: " + inventory.getItem().getItemName() + ", Amount: " + inventory.getAmount()));
        this.lastLocation = s.getLastLocation();
    }

    public SurvivorDTO(UUID id, String name, int age, String gender, LastLocation lastLocation) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.lastLocation = lastLocation;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getItems() {
        return items;
    }

    public LastLocation getLastLocation() {
        return lastLocation;
    }
}
