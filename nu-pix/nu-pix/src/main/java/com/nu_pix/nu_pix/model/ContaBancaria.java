package com.nu_pix.nu_pix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
public class ContaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conta_id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "limite_diario", precision = 15, scale = 2)
    private BigDecimal limiteDiario;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<ChavePix> chavesPix;
}