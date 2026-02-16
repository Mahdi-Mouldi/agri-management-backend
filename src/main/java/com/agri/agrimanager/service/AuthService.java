package com.agri.agrimanager.service;

import com.agri.agrimanager.dto.LoginRequest;
import com.agri.agrimanager.dto.LoginResponse;
import com.agri.agrimanager.dto.RegisterRequest;
import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.repository.AppUserRepository;
import com.agri.agrimanager.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public LoginResponse authenticate(LoginRequest loginRequest) {
        AppUser user =appUserRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(user.getUsername(),token);
    }
    public void register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        appUserRepository.save(user);
    }

}
