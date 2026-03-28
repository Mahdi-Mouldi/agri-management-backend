package com.agri.agrimanager.service;


import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.repository.FermeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FermeService {
    private final FermeRepository fermeRepository;
    public Ferme createFerme(Ferme ferme) {

        return fermeRepository.save(ferme);
    }
    public List<Ferme> getFermesByFarmerId(Long farmerId) {
        return fermeRepository.findByFarmerId(farmerId);
    }
    public List<Ferme> getAllFermes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return fermeRepository.findByFarmerAgentUsername(username);
    }
    public Ferme getFermeById(Long id) {
        return fermeRepository.findById(id)s
                .orElseThrow(() -> new RuntimeException("Ferme not found with id: " + id));
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
        fermeRepository.deleteById(id);
    }
}
