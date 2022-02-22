package com.trz.dto;

import java.util.UUID;

public class ReportZombieDTO {
    private UUID id;
    private String message;

    public ReportZombieDTO(UUID id, String message) {
        this.id = id;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
