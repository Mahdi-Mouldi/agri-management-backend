package com.agri.agrimanager.dto;

import com.agri.agrimanager.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponse {
    private String email;
    private String token;
    private Role role;
}

