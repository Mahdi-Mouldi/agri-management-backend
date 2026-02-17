package com.agri.agrimanager.mapper;

import com.agri.agrimanager.entity.Farmer;
import com.agri.agrimanager.dto.FarmerDTO;


public class FarmerMapper {

    // Convertir FarmerDTO -> Farmer (pour enregistrer en base)
    public static Farmer toEntity(FarmerDTO dto) {
        if (dto == null) return null;

        return Farmer.builder()
                .id(dto.getId()) // utile si update
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    // Convertir Farmer -> FarmerDTO (pour renvoyer au client)
    public static FarmerDTO toDTO(Farmer farmer) {
        if (farmer == null) return null;

        FarmerDTO dto = new FarmerDTO();
        dto.setId(farmer.getId());
        dto.setName(farmer.getName());
        dto.setEmail(farmer.getEmail());
        dto.setPhoneNumber(farmer.getPhoneNumber());
        return dto;
    }
}
