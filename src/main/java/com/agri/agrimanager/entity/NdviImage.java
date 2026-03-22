package com.agri.agrimanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NdviImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name ="parcelle_id", nullable=false)
    @JsonIgnoreProperties({
            "ndviImages", "weatherData",
            "farmer", "ferme"
    })  // ← ajouter
    private Parcelle parcelle;
    // La parcelle agricole à laquelle cette image NDVI correspond
    @Column(nullable = false)
    private String imageUrl;
    // L'URL de l'image NDVI récupérée depuis l'API (ex: Agromonitoring)
    private double ndviMean;// La valeur moyenne du NDVI sur la parcelle (entre 0.0 et 1.0)
    private double ndviMax;
    private double ndviMin;
    private double ndviStd;
    // L'écart-type des valeurs NDVI sur la parcelle (mesure de variation)

    @Column(nullable = false)
    private LocalDate imageDate; // date de creation de l'image

    private String sattelite;
    // Nom du satellite qui a pris l'image ("Sentinel-2", "Landsat-8", etc.)

    private Integer cloudCoverage;
    // Pourcentage de nuages sur l'image (0 = pas de nuages, 100 = tout couvert)

    @Column(name = "agromonitoring_polygon_id")
    private String polygonId;
    // L'identifiant du polygone Agromonitoring correspondant à cette parcelle

    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
