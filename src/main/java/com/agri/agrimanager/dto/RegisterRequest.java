package com.agri.agrimanager.dto;

//import com.agri.agrimanager.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
