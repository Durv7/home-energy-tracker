package com.dsk.ingestion_service.controller;

import com.dsk.ingestion_service.DTO.EnergyUsageDTO;
import com.dsk.ingestion_service.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingestion")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService){
        this.ingestionService = ingestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> ingectData(@RequestBody EnergyUsageDTO energyUsageDTO){

        ingestionService.ingestEnergyUsage(energyUsageDTO);

        return ResponseEntity.noContent().build();
    }
}
