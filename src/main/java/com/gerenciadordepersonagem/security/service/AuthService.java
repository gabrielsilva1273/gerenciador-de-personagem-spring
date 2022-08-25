package com.gerenciadordepersonagem.security.service;

import com.gerenciadordepersonagem.security.model.AuthRequest;
import com.gerenciadordepersonagem.security.model.AuthResponse;
import com.gerenciadordepersonagem.security.model.User;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity <?> login (AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            Date expiresAt = jwtTokenUtil.getExpiration(accessToken);
            AuthResponse response = new AuthResponse(accessToken,expiresAt);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login falhou");
        }
    }
}
