package com.trz.dto;

import javax.validation.constraints.NotNull;

public class UpdateLocationDTO {
    @NotNull(message = "tha latitude can't be null")
    private double latitude;
    @NotNull(message = "tha longitude can't be null")
    private double longitude;

    public UpdateLocationDTO() {
    }

    public UpdateLocationDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
