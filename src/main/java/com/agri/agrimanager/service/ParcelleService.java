package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.repository.ParcelleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcelleService {
    private final ParcelleRepository parcelleRepository;
    // Ajouter la méthode calculateDistance (formule Haversine)
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2){
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void validateParcelleLocation(Parcelle parcelle) {
        Ferme ferme = parcelle.getFerme();

        // Vérifier que ferme a des coordonnées
        if (ferme.getLatitude() == null || ferme.getLongitude() == null
                || ferme.getLatitude() == 0 || ferme.getLongitude() == 0) {
            throw new RuntimeException(
                    "La ferme n'a pas de coordonnées GPS définies"
            );
        }

        // Vérifier que parcelle a un geometryJson
        if (parcelle.getGeometryJson() == null) return;

        // Extraire le centroïde de la parcelle depuis geometryJson
        // (même méthode que dans WeatherService)
        double[] centroid = extractCentroid(parcelle.getGeometryJson());
        double parcelleLatitude = centroid[0];
        double parcelleLongitude = centroid[1];

        // Calculer la distance
        double distance = calculerDistance(
                ferme.getLatitude(), ferme.getLongitude(),
                parcelleLatitude, parcelleLongitude
        );

        // Bloquer si > 5 km
        if (distance > 5.0) {
            throw new RuntimeException(
                    String.format(
                            "La parcelle est trop éloignée de la ferme (%.2f km). " +
                                    "Distance maximale autorisée : 5 km", distance
                    )
            );
        }
    }
    private double[] extractCentroid(String geoJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode geometry = mapper.readTree(geoJson);
            JsonNode coordinates = geometry.get("coordinates").get(0);

            double totalLat = 0;
            double totalLon = 0;
            int count = coordinates.size();

            for (JsonNode point : coordinates) {
                totalLon += point.get(0).asDouble();
                totalLat += point.get(1).asDouble();
            }
            return new double[]{totalLat / count, totalLon / count};
        } catch (Exception e) {
            throw new RuntimeException("Erreur extraction centroïde", e);
        }
    }

    public Parcelle createParcelle(Parcelle parcelle) {
        if(parcelle.getGeometryJson() != null){
            validateParcelleLocation(parcelle);
        }
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
        if(updatedParcelle.getGeometryJson() != null){
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