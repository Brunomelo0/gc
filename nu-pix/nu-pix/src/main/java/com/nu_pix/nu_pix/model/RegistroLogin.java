package com.nu_pix.nu_pix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "registro_login")
public class RegistroLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public RegistroLogin(Usuario usuario, String acao, LocalDateTime dataHora) {
        this.usuario = usuario;
        this.acao = acao;
        this.dataHora = dataHora;
    }
}
