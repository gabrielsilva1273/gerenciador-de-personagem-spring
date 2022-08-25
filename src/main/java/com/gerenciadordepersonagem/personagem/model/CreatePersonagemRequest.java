package com.gerenciadordepersonagem.personagem.model;

import lombok.Getter;

@Getter
public class CreatePersonagemRequest {
    private String nome;
    private Double idade;
    private String nacionalidade;
    private String aparencia;

    public CreatePersonagemRequest (String nome, Double idade, String nacionalidade, String aparencia) {
        this.nome = nome;
        this.idade = idade;
        this.nacionalidade = nacionalidade;
        this.aparencia = aparencia;
    }
}
