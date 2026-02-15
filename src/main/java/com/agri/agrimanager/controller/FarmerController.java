package com.agri.agrimanager.controller;

import com.agri.agrimanager.dto.FarmerDTO;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.mapper.FarmerMapper;
import com.agri.agrimanager.service.FarmerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
@CrossOrigin
public class FarmerController {

    private final FarmerService farmerService;

    // Ajouter un agriculteur
    @PostMapping
    public FarmerDTO addFarmer(@RequestBody FarmerDTO farmerDTO) {
        // Convert DTO -> Entity
        Farmer farmer = FarmerMapper.toEntity(farmerDTO);
        // Sauvegarde
        Farmer savedFarmer = farmerService.createFarmer(farmer);
        // Convert Entity -> DTO pour la réponse
        return FarmerMapper.toDTO(savedFarmer);
    }

    // Récupérer tous les agriculteurs
    @GetMapping
    public List<FarmerDTO> getAllFarmers() {
        return farmerService.getAllFarmer().stream()
                .map(FarmerMapper::toDTO)  // Convertir chaque Entity en DTO
                .collect(Collectors.toList());
    }

    // Récupérer un agriculteur par id
    @GetMapping("/{id}")
    public FarmerDTO getFarmerById(@PathVariable Long id) {
        Farmer farmer = farmerService.GetFarmerById(id);
        return FarmerMapper.toDTO(farmer);
    }

    // Mettre à jour un agriculteur
    @PutMapping("/{id}")
    public FarmerDTO updateFarmer(@PathVariable Long id, @RequestBody FarmerDTO farmerDTO) {
        Farmer updatedFarmerEntity = FarmerMapper.toEntity(farmerDTO);
        Farmer savedFarmer = farmerService.updateFarmer(id, updatedFarmerEntity);
        return FarmerMapper.toDTO(savedFarmer);
    }

    // Supprimer un agriculteur
    @DeleteMapping("/{id}")
    public void deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
    }
}
