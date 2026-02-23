package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.feign.AgromonitoringClient;
import com.agri.agrimanager.repository.NdviRepository;
import com.agri.agrimanager.repository.ParcelleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NdviService {
    private final NdviRepository ndviRepository;
    private final ParcelleRepository parcelleRepository;
    private final AgromonitoringClient agromonitoringClient;
    public String createPolygon(Long parcelleId) throws  Exception {
        Parcelle parcelle=parcelleRepository.findById(parcelleId)
                .orElseThrow(()-> new Exception("Parcelle id not found"));
        // 1) Si déjà synchronisée, on retourne directement l'id externe

        if(parcelle.getAgroPolygonId()!=null && !parcelle.getAgroPolygonId().isEmpty()){
            return parcelle.getAgroPolygonId();
        }
        // 2) Construire le body attendu par /polygons : name + geo_json (GeoJSON)

        String requestBody = String.format(
                "{\"name\":\"%s\",\"geo_json\":%s}",
                parcelle.getName(),
                parcelle.getGeometryJson()
        );//preparer les donner necessaire pour l'api de la sattelite
        // 3) Appel API Agromonitoring (POST /polygons) -> réponse JSON contenant "id"
        String response = agromonitoringClient.createPolygon(requestBody);
        // 4) Extraire l'id du polygon depuis la réponse JSON ("id") [web:28]

        ObjectMapper mapper = new  ObjectMapper();
        JsonNode node = mapper.readTree(response);
        String polygonId = node.get("id").asText();

        // 5) Sauvegarder dans la parcelle pour réutiliser plus tard (polyid pour images/ndvi)

        parcelle.setAgroPolygonId(polygonId);
        parcelleRepository.save(parcelle);
        return polygonId;

    }
}
