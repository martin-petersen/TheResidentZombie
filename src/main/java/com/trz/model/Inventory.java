package com.trz.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Inventory {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "survivor_id")
    private Survivor survivor;
    @ManyToOne
    private Item item;
    private int amount;

    public Inventory() {
    }

    public Inventory(UUID id, Survivor survivor, Item item, int amount) {
        this.id = id;
        this.survivor = survivor;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return id.equals(inventory.id);
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

    public Survivor getSurvivor() {
        return survivor;
    }

    public void setSurvivor(Survivor survivor) {
        this.survivor = survivor;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
