package com.nu_pix.nu_pix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChavePix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoChavePix tipo;

    @Column(nullable = false, unique = true)
    private String valor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_id", nullable = false)
    private ContaBancaria conta;

    @Column(nullable = false)
    private boolean suspensa;
}
