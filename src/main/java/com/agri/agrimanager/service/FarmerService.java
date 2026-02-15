package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService  {
    private final FarmerRepository farmerRepository;
    public Farmer createFarmer(Farmer farmer){
        return farmerRepository.save(farmer);
    }

    public Farmer GetFarmerById(Long id){
      return farmerRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));
    }
    public List<Farmer> getAllFarmer(){
        return farmerRepository.findAll();
    }
    public Farmer updateFarmer(Long id, Farmer updatedFarmer){
        Farmer existFarmer = GetFarmerById(id);
        existFarmer.setName(updatedFarmer.getName());
        existFarmer.setEmail(updatedFarmer.getEmail());
        existFarmer.setPhoneNumber(updatedFarmer.getPhoneNumber());
        return farmerRepository.save(existFarmer);
    }

    public void deleteFarmer(Long id){
        farmerRepository.deleteById(id);
    }
}

