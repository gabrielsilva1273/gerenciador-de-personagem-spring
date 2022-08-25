package com.gerenciadordepersonagem.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegistrationRequest (String userName, String email, String password, String confirmPassword) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
