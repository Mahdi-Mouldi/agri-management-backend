package com.agri.agrimanager.service;

import com.agri.agrimanager.dto.LoginRequest;
import com.agri.agrimanager.dto.LoginResponse;
import com.agri.agrimanager.dto.RegisterRequest;
import com.agri.agrimanager.entity.AppUser;
import com.agri.agrimanager.entity.Role;
import com.agri.agrimanager.entity.VerificationToken;
import com.agri.agrimanager.repository.AppUserRepository;
import com.agri.agrimanager.repository.VerificationTokenRepository;
import com.agri.agrimanager.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, VerificationTokenRepository verificationTokenRepository
    , EmailService emailService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }
    public LoginResponse authenticate(LoginRequest loginRequest) {
        AppUser user =appUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        if(!user.isEnabled()){
            throw new RuntimeException("Veuillez vérifier votre email avant de vous connecter");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(user.getEmail(),token, user.getRole());
    }
    public void register(RegisterRequest request) {
        Optional<AppUser> existUser = appUserRepository.findByEmail(request.getEmail());
        if(existUser.isPresent()){
            throw new RuntimeException("Email already exists");
        }
        AppUser user = new AppUser();

        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.FARMER);
        user.setEnabled(false);

        AppUser savedUser =appUserRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiredDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationMail(savedUser.getEmail(), token);

    }
    public String verifyEmail(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        if(verificationToken.getExpiredDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token expired");
        }
        AppUser user = verificationToken.getUser();
        user.setEnabled(true);
        appUserRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "Email vérifié avec succès, vous pouvez maintenant vous connecter";
    }



}
