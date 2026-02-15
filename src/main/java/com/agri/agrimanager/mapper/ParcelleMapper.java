package com.agri.agrimanager.mapper;

import com.agri.agrimanager.dto.ParcelleDTO;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.entity.Parcelle;
import org.springframework.stereotype.Component;

@Component
public class ParcelleMapper {

    public ParcelleDTO toDTO(Parcelle parcelle) {
        if (parcelle == null) return null;

        return ParcelleDTO.builder()
                .id(parcelle.getId())
                .name(parcelle.getName())
                .geometryJson(parcelle.getGeometryJson())
                .syncStatus(parcelle.getSyncStatus())
                .farmerId(parcelle.getFarmer() != null ? parcelle.getFarmer().getId() : null)
                .build();
    }

    public Parcelle toEntity(ParcelleDTO dto, Farmer farmer) {
        if (dto == null) return null;

        return Parcelle.builder()
                .id(dto.getId())
                .name(dto.getName())
                .geometryJson(dto.getGeometryJson())
                .syncStatus(dto.getSyncStatus())
                .farmer(farmer)
                .build();
    }
}
