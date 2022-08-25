package com.gerenciadordepersonagem.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
