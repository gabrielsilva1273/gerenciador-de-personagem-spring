package com.gerenciadordepersonagem.personagem.model;

import lombok.Data;

@Data
public class Pericia {
    private String nomePericia;
    private String atributoQueSoma;
    private Double pontosDistribuidos;
    private Double pontosTotais;

}
