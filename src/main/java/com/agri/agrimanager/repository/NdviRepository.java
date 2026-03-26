package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.NdviImage;
import com.agri.agrimanager.entity.Parcelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NdviRepository extends JpaRepository<NdviImage, Long> {
    Boolean existsByParcelleIdAndImageUrl(Long parcelleId, String imageUrl);

    Optional<NdviImage> getNdviImageById(Long id);

    void deleteNdviImageByParcelleId(Long parcelleId);

    List<NdviImage> findByParcelleId(Long parcelleId);
}
