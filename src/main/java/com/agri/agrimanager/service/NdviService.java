package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.NdviImage;
import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.feign.AgromonitoringClient;
import com.agri.agrimanager.repository.NdviRepository;
import com.agri.agrimanager.repository.ParcelleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NdviService {
    private final NdviRepository ndviRepository;
    private final ParcelleRepository parcelleRepository;
    private final AgromonitoringClient agromonitoringClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String createPolygon(Long parcelleId) throws  Exception {
        Parcelle parcelle=parcelleRepository.findById(parcelleId)
                .orElseThrow(()-> new Exception("Parcelle id not found"));
        // 1) Si déjà synchronisée, on retourne directement l'id externe

        if(parcelle.getAgroPolygonId()!=null && !parcelle.getAgroPolygonId().isEmpty()){
            return parcelle.getAgroPolygonId();
        }

        // 2) Construire le body attendu par /polygons : name + geo_
        String requestBody = String.format(
                "{\"geo_json\":%s}",
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
    public String searchNdviImages(Long parcelleId, LocalDate startDate, LocalDate endDate) throws Exception {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate/endDate ne doivent pas être null");
        }
        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("startDate doit être < endDate");
        }

        // 1) Récupérer (ou créer) le polyid côté Agromonitoring
        String polyId = createPolygon(parcelleId);

        // 2) Convertir en timestamps UNIX (secondes, UTC) comme demandé par l’API
        long start = startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long end = endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);

        // 3) Appeler /image/search (l'appid est ajouté par ton interceptor)
        return agromonitoringClient.serchImage(start, end, polyId);
    }
    public String searchNdviHistory(Long parcelleId, LocalDate startDate, LocalDate endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate/endDate ne doivent pas être null");
        }
        if(!startDate.isBefore(endDate)){
            throw new IllegalArgumentException("startDate doit être < endDate");
        }
        String polyId = createPolygon(parcelleId);
        long start = startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long end = endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);

        return agromonitoringClient.getNdviHistory(start, end, polyId);
    }

    public List<NdviImage> syncNdviImages(Long parcelleId, LocalDate startDate, LocalDate endDate) throws Exception {

        Parcelle parcelle = parcelleRepository.findById(parcelleId)
                .orElseThrow(() -> new Exception("Parcelle id not found"));

        // 1) Appel step1: image/search -> tableau JSON [page:1]
        String json = searchNdviImages(parcelleId, startDate, endDate);

        JsonNode root = objectMapper.readTree(json);
        if (!root.isArray()) return List.of();

        String polyId = parcelle.getAgroPolygonId(); // normalement déjà rempli par createPolygon()

        List<NdviImage> toSave = new ArrayList<>();

        for (JsonNode item : root) {
            // Champs de /image/search [page:1]
            long dt = item.path("dt").asLong();
            String satelliteType = item.path("type").asText(null);
            double cl = item.path("cl").asDouble(0.0);

            String ndviPngUrl = item.path("image").path("ndvi").asText(null);
            String ndviStatsUrl = item.path("stats").path("ndvi").asText(null);

            if (ndviPngUrl == null || ndviPngUrl.isBlank()) continue;

            // Eviter doublons (recommandé)
            if (ndviRepository.existsByParcelleIdAndImageUrl(parcelleId, ndviPngUrl)) {
                continue;
            }

            LocalDate imageDate = Instant.ofEpochSecond(dt).atZone(ZoneOffset.UTC).toLocalDate();

            NdviImage entity = NdviImage.builder()
                    .parcelle(parcelle)
                    .imageUrl(ndviPngUrl)
                    .imageDate(imageDate)
                    .sattelite(satelliteType)
                    .cloudCoverage((int) Math.round(cl))   // cl est un % approximatif [page:1]
                    .polygonId(polyId)
                    .build();

            // 2) Step2: appeler stats.ndvi (retourne mean/min/max/std...) [page:1]
            if (ndviStatsUrl != null && !ndviStatsUrl.isBlank()) {
                fillStats(entity, ndviStatsUrl);
            }

            toSave.add(entity);
        }

        return ndviRepository.saveAll(toSave);
    }

    private void fillStats(NdviImage entity, String statsUrl) throws Exception {
        String statsJson = restTemplate.getForObject(statsUrl, String.class);
        if (statsJson == null) return;

        JsonNode stats = objectMapper.readTree(statsJson);

        entity.setNdviMean(stats.path("mean").asDouble());
        entity.setNdviMin(stats.path("min").asDouble());
        entity.setNdviMax(stats.path("max").asDouble());
        entity.setNdviStd(stats.path("std").asDouble());
    }

    // ... tes méthodes existantes: createPolygon, searchNdviImages, searchNdviHistory
}

