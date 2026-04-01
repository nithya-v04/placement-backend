package com.placement.service;

import com.placement.dto.LoginRequest;
import com.placement.dto.LoginResponse;
import com.placement.dto.RegisterRequest;
import com.placement.entity.Company;
import com.placement.entity.Student;
import com.placement.entity.User;
import com.placement.repository.CompanyRepository;
import com.placement.repository.StudentRepository;
import com.placement.repository.UserRepository;
import com.placement.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository        userRepository;
    private final StudentRepository     studentRepository;
    private final CompanyRepository     companyRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtService            jwtService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authManager) {
        this.userRepository    = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder   = passwordEncoder;
        this.jwtService        = jwtService;
        this.authManager       = authManager;
    }

    // ── Register ──────────────────────────────────────────────────────────────

    @Transactional
    public LoginResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered: " + req.getEmail());
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        user = userRepository.save(user);

        switch (req.getRole()) {
            case STUDENT -> {
                Student s = Student.builder()
                        .user(user)
                        .firstName(req.getFirstName())
                        .lastName(req.getLastName())
                        .rollNumber(req.getRollNumber())
                        .department(req.getDepartment())
                        .cgpa(req.getCgpa() != null ? req.getCgpa() : 0.0)
                        .yearOfGraduation(req.getYearOfGraduation())
                        .phone(req.getPhone())
                        .skills(req.getSkills())
                        .isPlaced(false)
                        .build();
                studentRepository.save(s);
            }
            case COMPANY -> {
                Company c = Company.builder()
                        .user(user)
                        .name(req.getCompanyName())
                        .industry(req.getIndustry())
                        .website(req.getWebsite())
                        .contactPerson(req.getContactPerson())
                        .contactEmail(req.getEmail())
                        .location(req.getLocation())
                        .build();
                companyRepository.save(c);
            }
            default -> { /* ADMIN – no additional profile */ }
        }

        return buildResponse(user, "Registration successful");
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public LoginResponse login(LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildResponse(user, "Login successful");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LoginResponse buildResponse(User user, String message) {
        return LoginResponse.builder()
                .token(jwtService.generateToken(user))
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .message(message)
                .build();
    }
}
