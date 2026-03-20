package com.allexolic.app_loteria.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "jogo_favorito")
@Data
public class FavoriteGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer bola1;
    @Column(nullable = false)
    private Integer bola2;
    @Column(nullable = false)
    private Integer bola3;
    @Column(nullable = false)
    private Integer bola4;
    @Column(nullable = false)
    private Integer bola5;
    @Column(nullable = false)
    private Integer bola6;
    @Column(nullable = false)
    private Integer bola7;
    @Column(nullable = false)
    private Integer bola8;
    @Column(nullable = false)
    private Integer bola9;
    @Column(nullable = false)
    private Integer bola10;
    @Column(nullable = false)
    private Integer bola11;
    @Column(nullable = false)
    private Integer bola12;
    @Column(nullable = false)
    private Integer bola13;
    @Column(nullable = false)
    private Integer bola14;
    @Column(nullable = false)
    private Integer bola15;
    @Column(name = "jogo_sorteado", nullable = false)
    private boolean jogoSorteado = false;
    @Column(name = "total_numeros_sorteados", nullable = false)
    private int totalNumerosSorteados = 0;
}
