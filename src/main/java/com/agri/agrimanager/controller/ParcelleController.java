package com.agri.agrimanager.controller;

import com.agri.agrimanager.dto.ParcelleDTO;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.mapper.ParcelleMapper;
import com.agri.agrimanager.service.FarmerService;
import com.agri.agrimanager.service.ParcelleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parcelles")
@RequiredArgsConstructor
@CrossOrigin
public class ParcelleController {

    private final ParcelleService parcelleService;
    private final ParcelleMapper parcelleMapper;
    private final FarmerService farmerService;

    @PostMapping
    public ParcelleDTO addParcelle(@RequestBody ParcelleDTO parcelleDTO) {

        Farmer farmer = farmerService.GetFarmerById(parcelleDTO.getFarmerId());

        Parcelle parcelle = parcelleMapper.toEntity(parcelleDTO, farmer);

        Parcelle savedParcelle = parcelleService.createParcelle(parcelle);

        return parcelleMapper.toDTO(savedParcelle);
    }

    @GetMapping("/farmers/{farmerId}")
    public List<ParcelleDTO> getAllParcellesByFarmerID(@PathVariable Long farmerId) {

        return parcelleService.getParcellesByFarmerId(farmerId)
                .stream()
                .map(parcelleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ParcelleDTO getParcelleById(@PathVariable Long id) {

        Parcelle parcelle = parcelleService.getParcelleById(id);
        return parcelleMapper.toDTO(parcelle);
    }

    @GetMapping
    public List<ParcelleDTO> getAllParcelles() {

        return parcelleService.getAllParcelle()
                .stream()
                .map(parcelleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ParcelleDTO updateParcelle(@PathVariable Long id,
                                      @RequestBody ParcelleDTO parcelleDTO) {

        Farmer farmer = farmerService.GetFarmerById(parcelleDTO.getFarmerId());

        Parcelle parcelle = parcelleMapper.toEntity(parcelleDTO, farmer);

        Parcelle updatedParcelle = parcelleService.updateParcelle(id, parcelle);

        return parcelleMapper.toDTO(updatedParcelle);
    }

    @DeleteMapping("/{id}")
    public void deleteParcelle(@PathVariable Long id) {

        parcelleService.deleteParcelle(id);
    }
}

