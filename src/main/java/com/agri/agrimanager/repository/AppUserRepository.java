package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String email);

    List<AppUser> findByRole(Role role);

    boolean existsByEmail(String email);
}
