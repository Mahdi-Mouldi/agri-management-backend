package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.NdviImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NdviRepository extends JpaRepository<NdviImage, Long> {
}
