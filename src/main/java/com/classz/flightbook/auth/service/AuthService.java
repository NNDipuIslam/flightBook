package com.classz.flightbook.auth.service;

import com.classz.flightbook.auth.dto.AuthResponse;
import com.classz.flightbook.auth.dto.LoginRequest;
import com.classz.flightbook.auth.dto.LogoutRequest;
import com.classz.flightbook.auth.dto.RegisterRequest;
import com.classz.flightbook.parent.entity.Parent;
import com.classz.flightbook.security.jwt.JwtService;
import com.classz.flightbook.user.entity.Role;
import com.classz.flightbook.user.entity.RoleName;
import com.classz.flightbook.user.repository.RoleRepository;
import com.classz.flightbook.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // LOGOUT
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }
        tokenBlacklistService.blacklistToken(token);
    }

    public void logout(LogoutRequest logoutRequest) {
        if (logoutRequest == null || logoutRequest.getToken() == null || logoutRequest.getToken().isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }
        logout(logoutRequest.getToken());
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Login successful");
    }

    // REGISTER
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered");
        }

        Role parentRole = roleRepository
                .findByRoleName(RoleName.PARENT)
                .orElseThrow(() -> new IllegalStateException("PARENT role not initialized"));

        Parent parent = new Parent();
        parent.setName(request.getName());
        parent.setEmail(request.getEmail());
        parent.setPassword(passwordEncoder.encode(request.getPassword()));
        parent.setEnabled(true);
        parent.getRoles().add(parentRole);

        userRepository.save(parent);

        UserDetails userDetails = userDetailsService.loadUserByUsername(parent.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, "Registration successful");
    }
}
