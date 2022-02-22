package com.trz.controller;

import com.trz.exception.BusinessException;
import com.trz.dto.*;
import com.trz.service.SurvivorService;
import com.trz.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/survivors")
public class SurvivorController {

    private final SurvivorService survivorService;

    private final TradeService tradeService;

    public SurvivorController(SurvivorService survivorService, TradeService tradeService) {
        this.survivorService = survivorService;
        this.tradeService = tradeService;
    }

    @PostMapping
    public ResponseEntity<SurvivorDTO> createSurvivor(@RequestBody @Valid CreateSurvivorDTO createSurvivorDTO,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        SurvivorDTO survivorDTO = survivorService.create(createSurvivorDTO);
        URI uri = uriComponentsBuilder.path("/survivors/{id}").buildAndExpand(survivorDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(survivorDTO);
    }

    @PostMapping("{fromId}/trade/{toId}")
    public ResponseEntity<TradeDTO> createTransaction(@PathVariable UUID fromId,
                                                      @PathVariable UUID toId,
                                                      @RequestBody @Valid CreateTradeDTO createTradeDTO) {
        TradeDTO tradeDTO = tradeService.exchange(fromId, toId, createTradeDTO);
        return ResponseEntity.ok(tradeDTO);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<SurvivorDTO> updateLocation(@PathVariable UUID id, @RequestBody @Valid UpdateLocationDTO updateLocationDTO) {
        SurvivorDTO survivorDTO = survivorService.updateLocation(id, updateLocationDTO);
        return ResponseEntity.ok(survivorDTO);
    }
}
