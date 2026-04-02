package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.Ferme;
import com.agri.agrimanager.entity.Parcelle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FermeRepository extends JpaRepository<Ferme, Long> {
    List<Ferme> findByFarmerId(Long farmerId);
    List<Ferme> findByFarmerAgentEmail(String email);


    List<Ferme> findByFarmerAgentEmailAndFarmerId(String email, Long farmerId);

    List<Ferme> findByFarmerEmailAndFarmerId(String email, Long farmerId);

    List<Ferme> findByFarmerEmail(String email);
}
