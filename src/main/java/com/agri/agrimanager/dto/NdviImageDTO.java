package com.agri.agrimanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

// hedha lbackend li chyaabtho llfrontend hedha li recupereneh mel sattelite response w hedha chnoua khddhina mnou
public class NdviImageDTO {
    private Long id;
    private Long parcelleId;
    private String imageUrl;
    private double ndviMean;
    private double ndviMax;
    private double ndviMin;
    private LocalDate imageDate;
    private String sattelite;
    private Integer cloudCoverage;

}
