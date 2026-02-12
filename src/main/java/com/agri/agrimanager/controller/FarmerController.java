package com.agri.agrimanager.controller;

import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.service.FarmerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
@CrossOrigin
public class FarmerController {
    private final FarmerService farmerService;

    @PostMapping
    public Farmer addFarmer(@RequestBody Farmer farmer) {
        return farmerService.createFarmer(farmer);
    }
    @GetMapping
    public List<Farmer> getAllFarmers() {
        return farmerService.getAllFarmer();
    }
    @GetMapping("/{id}")
    public Farmer getFarmerById(@PathVariable Long id) {
            return farmerService.GetFarmerById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
    }
}
