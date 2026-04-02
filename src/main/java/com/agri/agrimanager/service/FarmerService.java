package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.entity.Role;
import com.agri.agrimanager.repository.AppUserRepository;
import com.agri.agrimanager.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final AppUserRepository appUserRepository;

    // 🔹 récupérer user connecté
    private AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 🔹 créer farmer
    public Farmer createFarmer(Farmer farmer) {
        AppUser user = getCurrentUser();

        if (user.getRole() == Role.AGENT_TERRAIN) {
            farmer.setAgent(user);
        }

        return farmerRepository.save(farmer);
    }

    // 🔹 récupérer farmer par id (sécurisé)
    public Farmer getFarmerById(Long id) {
        AppUser user = getCurrentUser();

        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        if (user.getRole() == Role.ADMIN) {
            return farmer;
        }

        if (user.getRole() == Role.AGENT_TERRAIN &&
                farmer.getAgent() != null &&
                farmer.getAgent().getEmail().equals(user.getEmail())) {
            return farmer;
        }

        throw new RuntimeException("Access denied");
    }

    // 🔹 récupérer tous les farmers
    public List<Farmer> getAllFarmer() {
        AppUser user = getCurrentUser();

        if (user.getRole() == Role.ADMIN) {
            return farmerRepository.findAll();
        }

        if (user.getRole() == Role.AGENT_TERRAIN) {
            return farmerRepository.findByAgentEmail(user.getEmail());
        }

        return List.of(); // FARMER ou autre
    }

    // 🔹 update farmer
    public Farmer updateFarmer(Long id, Farmer updatedFarmer) {
        AppUser user = getCurrentUser();
        Farmer farmer = getFarmerById(id);

        if (user.getRole() == Role.ADMIN ||
                (user.getRole() == Role.AGENT_TERRAIN &&
                        farmer.getAgent() != null &&
                        farmer.getAgent().getEmail().equals(user.getEmail()))) {

            farmer.setName(updatedFarmer.getName());
            farmer.setEmail(updatedFarmer.getEmail());
            farmer.setPhoneNumber(updatedFarmer.getPhoneNumber());

            return farmerRepository.save(farmer);
        }

        throw new RuntimeException("Access denied");
    }

    // 🔹 delete farmer
    public void deleteFarmer(Long id) {
        AppUser user = getCurrentUser();
        Farmer farmer = getFarmerById(id);

        if (user.getRole() == Role.ADMIN ||
                (user.getRole() == Role.AGENT_TERRAIN &&
                        farmer.getAgent() != null &&
                        farmer.getAgent().getEmail().equals(user.getEmail()))) {

            farmerRepository.deleteById(id);
            return;
        }

        throw new RuntimeException("Access denied");
    }
}