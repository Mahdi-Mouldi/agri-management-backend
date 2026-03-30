package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.repository.ParcelleRepository;
import com.agri.agrimanager.utils.GeometryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelleService {
    private final ParcelleRepository parcelleRepository;
    private final FermeService fermeService;



    private void validateParcelleLocation(Parcelle parcelle) {
        Ferme ferme = parcelle.getFerme();
        if(ferme == null){
            throw new RuntimeException("La parcelle doit appartenir à une ferme");
        }
        if (ferme.getGeometryJson() == null || ferme.getGeometryJson().isBlank()) {
            throw new RuntimeException("La ferme n'a pas de limites géographiques définies");
        }
        if (parcelle.getGeometryJson() == null || parcelle.getGeometryJson().isBlank()) {
            throw new RuntimeException("La géométrie de la parcelle est obligatoire");
        }
        boolean inside = GeometryUtils.isParcelleInsideFerme(parcelle.getGeometryJson(), ferme.getGeometryJson());
        if(!inside){
            throw new RuntimeException("La parcelle dépasse les limites de la ferme");
        }

    }


    public Parcelle createParcelle(Parcelle parcelle) {
        if(parcelle.getGeometryJson() != null && !parcelle.getGeometryJson().isBlank()){
            validateParcelleLocation(parcelle);
        }
        return parcelleRepository.save(parcelle);
    }

    public Parcelle getParcelleById(Long id) {
        return parcelleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcelle not found with id: " + id));
    }

    public List<Parcelle> getAllParcelle() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return parcelleRepository.findByFarmerAgentEmail(email);
    }

    public Parcelle updateParcelle(Long id, Parcelle updatedParcelle){
        Parcelle existParcelle = getParcelleById(id);
        if(updatedParcelle.getGeometryJson() != null && !updatedParcelle.getGeometryJson().isBlank()){
            validateParcelleLocation(updatedParcelle);
        }
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