package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.Parcelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {
    List<Parcelle> findByFarmerId(Long farmerId);
    List<Parcelle> findByFermeId(Long fermeId);
    List<Parcelle> findByFarmerAgentEmail(String email);


    List<Parcelle> findByFarmerEmail(String email);

    List<Parcelle> findByFarmerIdAndFarmerAgentEmail(Long farmerId, String email);

    List<Parcelle> findByFarmerEmailAndFarmerId(String email, Long farmerId);

    List<Parcelle> findByFarmerAgentEmailAndFermeId(String email, Long fermeId);

    List<Parcelle> findByFarmerEmailAndFermeId(String email, Long fermeId);
}
