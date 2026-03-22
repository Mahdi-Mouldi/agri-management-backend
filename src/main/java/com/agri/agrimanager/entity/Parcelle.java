package com.agri.agrimanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String geometryJson; // GeoJSON format

    @Column(name = "agro_polygon_id")      // ← AJOUTÉ
    private String agroPolygonId;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus;

    @ManyToOne
    @JsonIgnoreProperties({
            "parcelles", "fermes"
    })  // ← ajouter
    @JoinColumn(name = "farmer_id")

    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "ferme_id")
    @JsonIgnoreProperties({
            "parcelles"
    })  // ← ajouter

    private Ferme ferme;

    private String culture;
    private String variete;
    private Double superficie;

    @OneToMany(mappedBy = "parcelle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("parcelle")
    private List<NdviImage> ndviImages;

    @OneToMany(mappedBy = "parcelle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("parcelle")
    private List<WeatherData> weatherData;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        syncStatus = SyncStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();


    }
}