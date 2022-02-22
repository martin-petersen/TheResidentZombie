package com.trz.dto;

public class ItemDTO {

    private String item;
    private int amount;

    public ItemDTO() {
    }

    public ItemDTO(String item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
