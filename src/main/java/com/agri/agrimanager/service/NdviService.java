package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.repository.NdviRepository;
import com.agri.agrimanager.repository.ParcelleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NdviService {
    private final NdviRepository ndviRepository;
    private final ParcelleRepository parcelleRepository;
    public String createPolygon(Long parcelleId) throws  Exception {
        Parcelle parcelle=parcelleRepository.findById(parcelleId)
                .orElseThrow(()-> new Exception("Parcelle id not found"));
        String requestBody = String.format(
                "{\"name\":\"%s\",\"geo_json\":%s}",
                parcelle.getName(),
                parcelle.getGeometryJson()
        );//preparer les donner necessaire pour l'api de la sattelite
        
    }
}
