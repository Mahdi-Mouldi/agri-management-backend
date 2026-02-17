package com.agri.agrimanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FermeDTO {
    private Long id;
    private String ferme_name;
    private String ferme_address;
    private Double superficieTotale;
    private Double latitude;
    private Double longitude;
    private String description;
    private Long farmer_id;
}
