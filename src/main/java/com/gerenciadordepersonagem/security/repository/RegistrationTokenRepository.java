package com.gerenciadordepersonagem.security.repository;

import com.gerenciadordepersonagem.security.model.RegistrationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends MongoRepository <RegistrationToken, String> {

    Optional <RegistrationToken> findByToken (String token);
}
