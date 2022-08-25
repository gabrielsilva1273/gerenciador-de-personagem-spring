package com.gerenciadordepersonagem.personagem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformacoesPersonagem {
    private Double totalPontos;
    private Double forca;
    private Double agilidade;
    private Double atletismo;
    private Double destreza;
    private Double pontaria;
    private Double furtividade;
    private Double percepcao;
    private Double inteligencia;
    private Double sabedoria;
    private Double carisma;
    private Double diplomacia;
    private Double enganacao;
    private Double intimidacao;
    private Double vontade;
    private Double sentirMotivacao;
    private Double vida;
    private Double energia;
    private Double sanidade;
    private Double bloqueio;
    private Double esquiva;
    private Double resistencia;
    private Double resiliencia;
    private Double iniciativa;

    public InformacoesPersonagem() {
        this.totalPontos = 30.0;
        this.forca = 0.0;
        this.agilidade = 0.0;
        this.atletismo = 0.0;
        this.destreza = 0.0;
        this.pontaria = 0.0;
        this.furtividade = 0.0;
        this.percepcao = 0.0;
        this.inteligencia = 0.0;
        this.sabedoria = 0.0;
        this.carisma = 0.0;
        this.diplomacia = 0.0;
        this.enganacao = 0.0;
        this.intimidacao = 0.0;
        this.vontade = 0.0;
        this.sentirMotivacao = 0.0;
        this.vida = 0.0;
        this.sanidade = 0.0;
        this.bloqueio = 0.0;
        this.resistencia = 0.0;
        this.resiliencia = 0.0;
        this.iniciativa = 0.0;
        this.esquiva = 0.0;
        this.energia = 0.0;
    }

    public void atualizarInformacoesDerivadas() {
        this.vida = Math.floor((this.forca + this.vontade) * 3);
        this.sanidade = Math.floor(((this.diplomacia + this.intimidacao + this.sentirMotivacao) * 2) + 100);
        this.resistencia = Math.floor(((this.atletismo + this.sentirMotivacao) / 2) - 2);
        this.resiliencia = Math.floor(((this.sabedoria + this.sentirMotivacao) / 2) - 2);
        this.iniciativa = Math.floor(this.vontade + this.agilidade);
        this.bloqueio = Math.floor((this.resistencia + this.forca) * 2);
        this.esquiva = Math.floor(this.agilidade + this.percepcao);
    }
}
