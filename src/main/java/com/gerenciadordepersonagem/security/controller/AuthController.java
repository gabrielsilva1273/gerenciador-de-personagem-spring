package com.gerenciadordepersonagem.security.controller;

import com.gerenciadordepersonagem.security.model.AuthRequest;
import com.gerenciadordepersonagem.security.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private AuthService authService;

    @PostMapping(path = "/auth/login")
    public ResponseEntity <?> login (@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
