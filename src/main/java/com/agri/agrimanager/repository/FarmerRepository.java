package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    List<Farmer> findByAgentUsername(String username);
}
