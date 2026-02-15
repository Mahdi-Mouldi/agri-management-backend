package com.agri.agrimanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour transférer les données de Farmer entre le front et le back
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
}
