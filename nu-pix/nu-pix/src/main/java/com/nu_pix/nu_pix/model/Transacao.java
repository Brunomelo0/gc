package com.nu_pix.nu_pix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private ContaBancaria contaOrigem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_destino_id", nullable = false)
    private ContaBancaria contaDestino;

    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao status;
}