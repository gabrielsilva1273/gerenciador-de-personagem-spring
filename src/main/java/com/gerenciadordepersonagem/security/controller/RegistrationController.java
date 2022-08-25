package com.gerenciadordepersonagem.security.controller;

import com.gerenciadordepersonagem.security.model.RegistrationRequest;
import com.gerenciadordepersonagem.security.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping(path = "/auth/registrar")
    public ResponseEntity <?> register (@RequestBody RegistrationRequest request) {
        try {
            String registrationSucessful = registrationService.register(request);
            return ResponseEntity.status(HttpStatus.OK).body(registrationSucessful);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/auth/confirmar")
    public ResponseEntity <?> confirm (@RequestParam("token") String token) {
        try {
            String confirmationSucessful = registrationService.confirmToken(token);
            return ResponseEntity.status((HttpStatus.OK)).body(confirmationSucessful);
        } catch (Exception e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(e.getMessage());
        }
    }
}
