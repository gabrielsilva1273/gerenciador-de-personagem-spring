package com.gerenciadordepersonagem.security.service;

import com.gerenciadordepersonagem.security.model.RegistrationToken;
import com.gerenciadordepersonagem.security.repository.RegistrationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationTokenService {

    private RegistrationTokenRepository registrationTokenRepository;

    public void saveToken (RegistrationToken token) {
        registrationTokenRepository.save(token);
    }

    public Optional<RegistrationToken> getToken (String token) {
        return registrationTokenRepository.findByToken(token);
    }


    public RegistrationToken loadRegistrationTokenByToken (String token) throws IllegalStateException {
        String TOKEN_DOES_NOT_EXIST = "Token inexistente.";
        return registrationTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException(TOKEN_DOES_NOT_EXIST));
    }

    public void setConfirmedAt (String token) {
        RegistrationToken registrationToken = loadRegistrationTokenByToken(token);
        LocalDateTime localDateTime = LocalDateTime.now();
        registrationToken.setConfirmedAt(localDateTime);
        registrationTokenRepository.save(registrationToken);
    }
}
