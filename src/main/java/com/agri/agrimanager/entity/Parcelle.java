package com.agri.agrimanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus;

    @ManyToOne
    @JoinColumn(name = "farmer_id")

    private Farmer farmer;

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