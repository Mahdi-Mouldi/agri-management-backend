package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

}
