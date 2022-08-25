package com.gerenciadordepersonagem.security.service;

import com.gerenciadordepersonagem.security.model.RegistrationToken;
import com.gerenciadordepersonagem.security.model.User;
import com.gerenciadordepersonagem.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private RegistrationTokenService registrationTokenService;

    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        String USER_NOT_FOUND_MESSAGE = "Usuário com e-mail %s não encontrado.";
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    public String signUpUser (User user) {
        String EMAIL_ALREADY_REGISTERED = "%s já está cadastrado.";
        boolean userExist = userRepository.findByEmail(user.getEmail()).isPresent();
        boolean isEnabled = false;
        if (userExist) {
            isEnabled = loadUserByUsername(user.getEmail()).isEnabled();
        }

        if (userExist && isEnabled) {
            throw new IllegalStateException(String.format(EMAIL_ALREADY_REGISTERED, user.getEmail()));
        }

        if (userExist && !isEnabled) {
            User disabledUser = (User) loadUserByUsername(user.getEmail());
            String id = disabledUser.getId();
            userRepository.deleteById(id);
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        RegistrationToken registrationToken = new RegistrationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(20), user);
        registrationTokenService.saveToken(registrationToken);

        return token;
    }

    public User enableUser (String email) {
        User user = (User) loadUserByUsername(email);
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
