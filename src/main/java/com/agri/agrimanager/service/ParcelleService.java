package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.repository.ParcelleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelleService {
    private final ParcelleRepository parcelleRepository;

    public Parcelle createParcelle(Parcelle parcelle) {
        return parcelleRepository.save(parcelle);
    }

    public Parcelle getParcelleById(Long id) {
        return parcelleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcelle not found with id: " + id));
    }

    public List<Parcelle> getAllParcelle() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return parcelleRepository.findByFarmerAgentUsername(username);
    }

    public Parcelle updateParcelle(Long id, Parcelle updatedParcelle){
        Parcelle existParcelle = getParcelleById(id);
        existParcelle.setName(updatedParcelle.getName());
        existParcelle.setGeometryJson(updatedParcelle.getGeometryJson());
        existParcelle.setSyncStatus(updatedParcelle.getSyncStatus());
        existParcelle.setCulture(updatedParcelle.getCulture());
        existParcelle.setVariete(updatedParcelle.getVariete());
        existParcelle.setSuperficie(updatedParcelle.getSuperficie());
        return parcelleRepository.save(existParcelle);
    }
    public List<Parcelle> getParcellesByFarmerId(Long farmerId) {
        return parcelleRepository.findByFarmerId(farmerId);
    }

    public void deleteParcelle(Long id) {
        parcelleRepository.deleteById(id);
    }
    public List<Parcelle> getParcellesByFermeId(Long fermeId) {
        return parcelleRepository.findByFermeId(fermeId);
    }
}