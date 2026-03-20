package com.allexolic.app_loteria.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "arquivo_loteria_detalhe")
@Data
public class LotteryFileDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arquivo_loteria_id", nullable = false)
    private LotteryFile arquivo;

    @Column(nullable = false)
    private Integer concurso;

    @Column(name = "data_sorteio", nullable = false)
    private LocalDate dataSorteio;

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
}
