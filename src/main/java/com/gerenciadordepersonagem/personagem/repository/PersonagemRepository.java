package com.gerenciadordepersonagem.personagem.repository;

import com.gerenciadordepersonagem.personagem.model.Personagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonagemRepository extends MongoRepository <Personagem, String> {
    Page <Personagem> findAllByOwnerId (String ownerId, Pageable paging);
    Page <Personagem> findAllByOwnerIdAndNome(String ownerId,String nome,Pageable paging);
}
