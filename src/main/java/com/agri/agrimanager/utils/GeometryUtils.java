package com.agri.agrimanager.utils;

import com.agri.agrimanager.entity.Parcelle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

//transformer le GeoJSON en polygone
//vérifier si la parcelle est dans la ferme
public class GeometryUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static Polygon geoJsonToPolygon(String geometryJson) {
        try {
            JsonNode root = objectMapper.readTree(geometryJson);
            JsonNode coords = root.get("coordinates").get(0);

            Coordinate[] coordinates = new Coordinate[coords.size()];
            for (int i = 0; i < coords.size(); i++) {
                double lon = coords.get(i).get(0).asDouble();
                double lat = coords.get(i).get(1).asDouble();
                coordinates[i] = new Coordinate(lon, lat);
            }
            // nsna3 contour msakkar (polygon)
            LinearRing shell = new LinearRing(
                    new CoordinateArraySequence(coordinates),
                    geometryFactory
            );

            return new Polygon(shell, null, geometryFactory);

        } catch (Exception e) {
            throw new RuntimeException("GeoJSON invalide", e);
        }
    }
    public static boolean isParcelleInsideFerme(String parcelleGeoJson, String fermeGeoJson){
        //convertir parcelle men geoJson ll polygon
        Polygon parcelle = geoJsonToPolygon(parcelleGeoJson);
        Polygon ferme = geoJsonToPolygon(fermeGeoJson);
        // nchouf est-ce que parcelle kolha dakhla fil ferme
        return ferme.covers(parcelle);
    }

}
