package com.agri.agrimanager.mapper;

import com.agri.agrimanager.dto.NdviImageDTO;
import com.agri.agrimanager.entity.NdviImage;

public class NdviImageMapper {
    public NdviImageDTO toDto(NdviImage ndviImage) {
        return NdviImageDTO.builder()
                .id(ndviImage.getId())
                .parcelleId(ndviImage.getParcelle().getId())
                .imageUrl(ndviImage.getImageUrl())
                .ndviMean(ndviImage.getNdviMean())
                .ndviMin(ndviImage.getNdviMin())
                .ndviMax(ndviImage.getNdviMax())
                .sattelite(ndviImage.getSattelite())
                .cloudCoverage(ndviImage.getCloudCoverage())
                .build();
    }
}
