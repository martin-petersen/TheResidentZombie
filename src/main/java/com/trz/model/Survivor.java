package com.trz.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Survivor {

    @Id
    private UUID id;
    private String name;
    private int age;
    private String gender;
    @OneToMany(mappedBy = "survivor", cascade = CascadeType.ALL)
    private List<Inventory> inventory;
    private boolean infected;
    @Embedded
    private LastLocation lastLocation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survivor survivor = (Survivor) o;
        return id.equals(survivor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public LastLocation getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LastLocation lastLocation) {
        this.lastLocation = lastLocation;
    }
}
