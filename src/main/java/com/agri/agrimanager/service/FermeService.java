package com.agri.agrimanager.service;


import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.entity.Role;
import com.agri.agrimanager.repository.AppUserRepository;
import com.agri.agrimanager.repository.FermeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FermeService {
    private final FermeRepository fermeRepository;
    private final AppUserRepository appUserRepository;

    private AppUser getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    public Ferme createFerme(Ferme ferme) {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN){
            return fermeRepository.save(ferme);
        }
        if(user.getRole() == Role.AGENT_TERRAIN) {
            if (ferme.getFarmer() != null
                    && ferme.getFarmer().getAgent() != null
                    && ferme.getFarmer().getAgent().getEmail().equals(user.getEmail())) {
                return fermeRepository.save(ferme);
            }
            throw new RuntimeException("Acess Denied");
        }
        if(user.getRole() == Role.FARMER) {
            if (ferme.getFarmer() != null
                    && ferme.getFarmer().getEmail().equals(user.getEmail())) {
                return fermeRepository.save(ferme);
            }
            throw new RuntimeException("Acess Denied");
        }
        throw new RuntimeException("Acess Denied");
    }
    public List<Ferme> getFermesByFarmerId(Long farmerId) {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN) {
            return fermeRepository.findByFarmerId(farmerId);
        }
        if(user.getRole() == Role.AGENT_TERRAIN) {
            return fermeRepository.findByFarmerAgentEmailAndFarmerId(user.getEmail(), farmerId);
        }
        if(user.getRole() == Role.FARMER) {
            return fermeRepository.findByFarmerEmailAndFarmerId(user.getEmail(), farmerId);
        }
        return List.of();
    }
    public List<Ferme> getAllFermes() {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN) {
            return fermeRepository.findAll();
        }
        if(user.getRole() == Role.AGENT_TERRAIN) {
            return fermeRepository.findByFarmerAgentEmail(user.getEmail());
        }
        if(user.getRole() == Role.FARMER) {
            return fermeRepository.findByFarmerEmail(user.getEmail());
        }
        return List.of();
    }
    public Ferme getFermeById(Long id) {
        AppUser user = getCurrentUser();
        Ferme ferme = fermeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ferme not found with id: " + id));
        if(user.getRole() == Role.ADMIN) {
            return ferme;
        }
        if(user.getRole() == Role.AGENT_TERRAIN) {
            if (ferme.getFarmer() != null
                    && ferme.getFarmer().getAgent() != null
                    && ferme.getFarmer().getAgent().getEmail().equals(user.getEmail())) {
                return ferme;
            }
            throw new RuntimeException("Acess Denied");
        }
        if(user.getRole() == Role.FARMER) {
            if (ferme.getFarmer() != null
                    && ferme.getFarmer().getEmail().equals(user.getEmail())) {
                return ferme;
            }
            throw new RuntimeException("Acess Denied");
        }
        throw new RuntimeException("Acess Denied");

    }
    public Ferme updateFerme(Long id, Ferme updatedFerme){
        Ferme existFerme = getFermeById(id);
        existFerme.setFerme_name(updatedFerme.getFerme_name());
        existFerme.setFerme_address(updatedFerme.getFerme_address());
        existFerme.setSuperficieTotale(updatedFerme.getSuperficieTotale());
        existFerme.setGeometryJson(updatedFerme.getGeometryJson());
        existFerme.setLatitude(updatedFerme.getLatitude());
        existFerme.setLongitude(updatedFerme.getLongitude());
        existFerme.setDescription(updatedFerme.getDescription());

        return fermeRepository.save(existFerme);
    }
    public void deleteFerme(Long id){
        Ferme ferme = getFermeById(id);
        fermeRepository.delete(ferme);
    }
}
