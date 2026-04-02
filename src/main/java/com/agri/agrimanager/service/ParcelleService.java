package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.entity.Role;
import com.agri.agrimanager.repository.AppUserRepository;
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
    private final AppUserRepository appUserRepository;

    private AppUser getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


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
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN){
            return parcelleRepository.save(parcelle);
        }
        if(user.getRole() == Role.AGENT_TERRAIN){
            if(parcelle.getFarmer() != null
                 && parcelle.getFarmer().getAgent() != null
                && parcelle.getFarmer().getAgent().getEmail().equals(user.getEmail())){
                    return parcelleRepository.save(parcelle);
                }
            throw new RuntimeException("Acess Denied");
            }
        if(user.getRole() == Role.FARMER){
            if(parcelle.getFarmer() != null && parcelle.getFarmer().getEmail().equals(user.getEmail())){
                return parcelleRepository.save(parcelle);
            }
            throw new RuntimeException("Acess Denied");
        }
        throw new RuntimeException("Acess Denied");
    }

    public Parcelle getParcelleById(Long id) {
        AppUser user = getCurrentUser();
        Parcelle parcelle = parcelleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcelle not found with id: " + id));
        if (user.getRole() == Role.ADMIN) {
            return parcelle;
        }
        if (user.getRole() == Role.AGENT_TERRAIN) {
            if (parcelle.getFarmer() != null
                    && parcelle.getFarmer().getAgent() != null
                    && parcelle.getFarmer().getAgent().getEmail().equals(user.getEmail())) {
                return parcelle;
            }
            throw new RuntimeException("Acess Denied");
        }
        if (user.getRole() == Role.FARMER) {
            if (parcelle.getFarmer() != null && parcelle.getFarmer().getEmail().equals(user.getEmail())) {
                return parcelle;
            }
            throw new RuntimeException("Acess Denied");
        }
        throw new RuntimeException("Acess Denied");

    }

    public List<Parcelle> getAllParcelle() {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN){
            return parcelleRepository.findAll();
        }
        if(user.getRole() == Role.AGENT_TERRAIN){
        return parcelleRepository.findByFarmerAgentEmail(user.getEmail());
        }
        if(user.getRole() == Role.FARMER){
            return parcelleRepository.findByFarmerEmail(user.getEmail());
        }
        return List.of();

    }

    public Parcelle updateParcelle(Long id, Parcelle updatedParcelle){
        Parcelle existParcelle = getParcelleById(id);

        existParcelle.setName(updatedParcelle.getName());
        existParcelle.setGeometryJson(updatedParcelle.getGeometryJson());
        existParcelle.setSyncStatus(updatedParcelle.getSyncStatus());
        existParcelle.setCulture(updatedParcelle.getCulture());
        existParcelle.setVariete(updatedParcelle.getVariete());
        existParcelle.setSuperficie(updatedParcelle.getSuperficie());
        if(updatedParcelle.getGeometryJson() != null && !updatedParcelle.getGeometryJson().isBlank()){
            validateParcelleLocation(updatedParcelle);
        }
        return parcelleRepository.save(existParcelle);
    }

    public List<Parcelle> getParcellesByFarmerId(Long farmerId) {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN){
            return parcelleRepository.findByFarmerId(farmerId);
        }
        if(user.getRole() == Role.AGENT_TERRAIN){
            return parcelleRepository.findByFarmerIdAndFarmerAgentEmail(farmerId, user.getEmail());
        }
        if(user.getRole() == Role.FARMER){
            return parcelleRepository.findByFarmerEmailAndFarmerId(user.getEmail(), farmerId);
        }
        return List.of();
    }

    public void deleteParcelle(Long id) {
        Parcelle parcelle = getParcelleById(id);
        parcelleRepository.delete(parcelle);
    }
    public List<Parcelle> getParcellesByFermeId(Long fermeId) {
        AppUser user = getCurrentUser();
        if(user.getRole() == Role.ADMIN){
            return parcelleRepository.findByFermeId(fermeId);
        }
        if(user.getRole() == Role.AGENT_TERRAIN){
            return parcelleRepository.findByFarmerAgentEmailAndFermeId(user.getEmail(), fermeId);
        }
        if(user.getRole() == Role.FARMER){
            return parcelleRepository.findByFarmerEmailAndFermeId(user.getEmail(), fermeId);
        }
        return List.of();
    }
}