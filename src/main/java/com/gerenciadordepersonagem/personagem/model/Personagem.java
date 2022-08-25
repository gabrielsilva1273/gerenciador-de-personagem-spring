package com.gerenciadordepersonagem.personagem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Personagem {
    @Id
    private String id;
    private final String ownerId;
    private String nome;
    private Double idade;
    private String gos;
    private String nacionalidade;
    private Double experiencia;
    private Double nivel;
    private Double dinheiro;
    private Double hidratacao;
    private Double saciedade;

    private String titulo;
    private String aparencia;
    private String personalidade;
    private String habito;
    private String vicio;
    private String historia;
    private String moralidade;

    private InformacoesPersonagem informacoesPersonagem;

    private Double quantidadePericias;
    private Double pontosPericiasParaDistribuir;
    private List <Pericia> periciaList;

    private Double quantidadeTalentos;
    private List <Talento> talentoList;

    private List <String> inventario;

    public Personagem (String ownerId, String nome,Double idade,String nacionalidade,String aparencia) {
        this.ownerId = ownerId;
        this.nome = nome;
        this.idade = idade;
        this.nacionalidade = nacionalidade;
        this.aparencia = aparencia;
    }
}

