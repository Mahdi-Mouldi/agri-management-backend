package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.repository.AppUserRepository;
import com.agri.agrimanager.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService  {
    private final FarmerRepository farmerRepository;
    private final AppUserRepository appUserRepository;
    // Récupère le nom d'utilisateur de l'agent actuellement connecté
// puis cherche cet agent dans la base de données.
// Si aucun agent n'est trouvé avec ce username, lance une exception.
    public Farmer createFarmer(Farmer farmer){
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //njib lusername mte3 luser li aaml login
        AppUser agent = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        farmer.setAgent(agent);
        return farmerRepository.save(farmer);
    }

    public Farmer getFarmerById(Long id){
      return farmerRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));
    }
    public List<Farmer> getAllFarmer(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return farmerRepository.findByAgentEmail(email);
    }
    public Farmer updateFarmer(Long id, Farmer updatedFarmer){
        Farmer existFarmer = getFarmerById(id);
        existFarmer.setName(updatedFarmer.getName());
        existFarmer.setEmail(updatedFarmer.getEmail());
        existFarmer.setPhoneNumber(updatedFarmer.getPhoneNumber());
        return farmerRepository.save(existFarmer);
    }

    public void deleteFarmer(Long id){
        farmerRepository.deleteById(id);
    }
}

