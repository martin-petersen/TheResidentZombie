package com.trz.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateSurvivorDTO {

    @NotBlank(message = "Field name is mandatory")
    private String name;
    private int age;
    @NotBlank(message = "Field gender is mandatory")
    private String gender;
    @NotNull(message = "The list of items can't be null")
    @NotEmpty(message = "The list of items can't be empty")
    private List<ItemDTO> items;
    private double latitude;
    private double longitude;

    public CreateSurvivorDTO() {
    }

    public CreateSurvivorDTO(int age) {
        this.age = age;
    }

    public CreateSurvivorDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CreateSurvivorDTO(String name, int age, String gender, List<ItemDTO> items, double latitude, double longitude) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.items = items;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public List<ItemDTO> getItems() {
        return items;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
