package com.gerenciadordepersonagem.personagem.controller;

import com.gerenciadordepersonagem.personagem.model.CreatePersonagemRequest;
import com.gerenciadordepersonagem.personagem.model.Personagem;
import com.gerenciadordepersonagem.personagem.service.PersonagemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PersonagemController {
    private PersonagemService personagemService;

    //create
    @PostMapping(path = "/personagens")
    public ResponseEntity <?> create (@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody CreatePersonagemRequest request) {
        try {
            Personagem createdPersonagem = personagemService.create(authorization, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersonagem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //delete
    @DeleteMapping(path = "personagens/{id}")
    public ResponseEntity <?> deleteById (@PathVariable String id) {
        try {
            personagemService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(String.format("Personagem com a ID %s excluido com sucesso", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //read by id
    @GetMapping(path = "/personagens/{id}")
    public ResponseEntity <?> read (@PathVariable String id) {
        try {
            Personagem personagem = personagemService.read(id);
            return ResponseEntity.status(HttpStatus.OK).body(personagem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //read all - pageable
    @GetMapping(path = "/personagens")
    public ResponseEntity <?> readAll (@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        try {
            Map <String, Object> personagemPages = personagemService.readAll(authorization, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(personagemPages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/personagens/procurar")
    public ResponseEntity <?> readAllByName (@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "4") int size,
                                             @RequestParam String name) {
        try {
            Map <String, Object> personagemPages = personagemService.readAllByName(authorization,page,size,name);
            return ResponseEntity.status(HttpStatus.OK).body(personagemPages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //update
    @PutMapping(path = "/personagens")
    public ResponseEntity <?> update (@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,@RequestBody Personagem personagem) {
        try {
            Personagem updatedPersonagem = personagemService.update(authorization,personagem);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPersonagem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
