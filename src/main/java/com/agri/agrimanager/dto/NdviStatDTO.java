package com.agri.agrimanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NdviStatDTO {
    private Double currentNdvi;
    private Double avgNdvi;
    private String healthStatus; // Excellent , Good , Moderate, Poor
    private String recommendation;
    private Integer totaleImages;
}
