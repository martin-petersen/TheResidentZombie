package com.trz.dto;

import java.util.List;

public class AnalyticsDTO {
    private final long percentOfInfected;
    private final long getPercentOfSurvivors;
    private final List<ItemDTO> amountOfItemsPerSurvivor;
    private final int totalPointsLostDueInfection;

    public AnalyticsDTO(long percentOfInfected, long getPercentOfSurvivors, List<ItemDTO> amountOfItemsPerSurvivor, int totalPointsLostDueInfection) {
        this.percentOfInfected = percentOfInfected;
        this.getPercentOfSurvivors = getPercentOfSurvivors;
        this.amountOfItemsPerSurvivor = amountOfItemsPerSurvivor;
        this.totalPointsLostDueInfection = totalPointsLostDueInfection;
    }

    public long getPercentOfInfected() {
        return percentOfInfected;
    }

    public long getGetPercentOfSurvivors() {
        return getPercentOfSurvivors;
    }

    public List<ItemDTO> getAmountOfItemsPerSurvivor() {
        return amountOfItemsPerSurvivor;
    }

    public int getTotalPointsLostDueInfection() {
        return totalPointsLostDueInfection;
    }
}
