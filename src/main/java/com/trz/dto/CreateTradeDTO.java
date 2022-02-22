package com.trz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateTradeDTO {

    @NotNull(message = "The list of items to send can't be null")
    @NotEmpty(message = "The list of items to send can't be empty")
    private List<ItemDTO> supply;
    @NotNull(message = "The list of items to receive can't be null")
    @NotEmpty(message = "The list of items to receive can't be empty")
    private List<ItemDTO> demand;

    public CreateTradeDTO() {
    }

    public CreateTradeDTO(List<ItemDTO> supply, List<ItemDTO> demand) {
        this.supply = supply;
        this.demand = demand;
    }

    public List<ItemDTO> getSupply() {
        return supply;
    }

    public List<ItemDTO> getDemand() {
        return demand;
    }
}
