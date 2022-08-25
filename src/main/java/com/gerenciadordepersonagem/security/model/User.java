package com.gerenciadordepersonagem.security.model;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Document
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    private String id;
    private String userName;
    private String email;
    private String password;
    private UserRole userRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    public User (String userName, String email, String password) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userRole = UserRole.USER;
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities () {
        return null;
    }

    @Override
    public String getUsername () {
        return this.userName;
    }
    @Override
    public boolean isAccountNonExpired () {
        return true;
    }
    @Override
    public boolean isAccountNonLocked () {
        return !locked;
    }
    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }
    @Override
    public boolean isEnabled () {
        return enabled;
    }
}
