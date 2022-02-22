package com.trz.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Item {

    @Id
    private UUID id;
    private String item;
    private int points;

    public Item() {
    }

    public Item(UUID id, String item, int points) {
        this.id = id;
        this.item = item;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
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

    public String getItemName() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
