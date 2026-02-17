package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.Parcelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {
    List<Parcelle> findByFarmerId(Long farmerId);
    List<Parcelle> findByFermeId(Long fermeId);
    List<Parcelle> findByFarmerAgentUsername(String username);


}
