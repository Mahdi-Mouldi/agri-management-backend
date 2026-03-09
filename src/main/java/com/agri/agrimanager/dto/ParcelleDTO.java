package com.agri.agrimanager.dto;

import com.agri.agrimanager.entity.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelleDTO {
    private Long id;             // pour identifier la parcelle à updater
    private String name;
    private String geometryJson; // GeoJSON
    private SyncStatus syncStatus;
    private Long farmerId;       // pour associer la parcelle à un Farmer
    private Long fermeId;
    private String culture;
    private String variete;
    private Double superficie;
}
