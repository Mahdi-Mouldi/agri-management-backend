package com.agri.agrimanager.mapper;

import com.agri.agrimanager.dto.FermeDTO;
import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.entity.Ferme;

public class FermeMapper {
    // Convertir FermeDTO -> Ferme (pour enregistrer en base)
    public static Ferme toEntity(FermeDTO dto, Farmer farmer) {
        if (dto == null) return null;

        return Ferme.builder()
                .id(dto.getId()) // utile si update
                .ferme_name(dto.getFerme_name())
                .ferme_address(dto.getFerme_address())
                .superficieTotale(dto.getSuperficieTotale())
                .geometryJson(dto.getGeometryJson())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .description(dto.getDescription())
                .farmer(farmer)
                .build();
    }
    //entity to dto

    public static FermeDTO toDTO(Ferme ferme) {
        if (ferme == null) return null;

        return FermeDTO.builder()
                .id(ferme.getId())
                .ferme_name(ferme.getFerme_name())
                .ferme_address(ferme.getFerme_address())
                .superficieTotale(ferme.getSuperficieTotale())
                .geometryJson(ferme.getGeometryJson())
                .latitude(ferme.getLatitude())
                .longitude(ferme.getLongitude())
                .description(ferme.getDescription())
                .farmer_id(ferme.getFarmer() != null ? ferme.getFarmer().getId() : null)
                .build();
    }


}
