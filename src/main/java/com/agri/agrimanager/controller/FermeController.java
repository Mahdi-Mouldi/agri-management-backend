package com.agri.agrimanager.controller;

import com.agri.agrimanager.dto.FermeDTO;
import com.agri.agrimanager.dto.ParcelleDTO;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.mapper.FermeMapper;
import com.agri.agrimanager.service.FarmerService;
import com.agri.agrimanager.service.FermeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/fermes")
@RequiredArgsConstructor
@CrossOrigin
public class FermeController {
    private final FermeService fermeService;
    private final FarmerService farmerService;
    //ajouter une ferme
    @PostMapping
    public FermeDTO createFerme(@RequestBody FermeDTO fermeDTO){
        Farmer farmer = farmerService.getFarmerById(fermeDTO.getFarmer_id());
        Ferme fermeEntity = FermeMapper.toEntity(fermeDTO, farmer);
        Ferme savedEntity = fermeService.createFerme(fermeEntity);
        // to DTO
        return FermeMapper.toDTO(savedEntity);
    }
    @GetMapping("/farmers/{farmer_id}")
    public List<FermeDTO> getFermesByFarmerId(@PathVariable Long farmer_id){
        return fermeService.getFermesByFarmerId(farmer_id)
                .stream()
                .map(FermeMapper::toDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public FermeDTO getFermesById(@PathVariable Long id) {

        Ferme ferme = fermeService.getFermeById(id);
        return FermeMapper.toDTO(ferme);
    }

    @GetMapping
    public List<FermeDTO> getAllFermes() {

        return fermeService.getAllFermes()
                .stream()
                .map(FermeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public FermeDTO updateFerme(@PathVariable Long id,
                                      @RequestBody FermeDTO fermeDTO) {

        Farmer farmer = farmerService.getFarmerById(fermeDTO.getId());

        Ferme ferme = FermeMapper.toEntity(fermeDTO, farmer);

        Ferme updatedFerme = fermeService.updateFerme(id, ferme);

        return FermeMapper.toDTO(updatedFerme);
    }

    @DeleteMapping("/{id}")
    public void deleteFerme(@PathVariable Long id) {
        fermeService.deleteFerme(id);
    }

}
