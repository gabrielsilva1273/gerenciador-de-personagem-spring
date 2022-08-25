package com.gerenciadordepersonagem.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private String jwt;
    private Date expiresAt;
}
