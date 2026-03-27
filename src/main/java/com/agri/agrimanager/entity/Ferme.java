package com.agri.agrimanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Ferme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ferme_name;
    private String ferme_address;
    private Double superficieTotale;
    @Column(columnDefinition = "TEXT")
    private String geometryJson;
    private Double latitude;
    private Double longitude;
    private String description;
    private LocalDate created_at;
    @ManyToOne
    @JoinColumn(name ="farmer_id")
    private Farmer farmer;



}
