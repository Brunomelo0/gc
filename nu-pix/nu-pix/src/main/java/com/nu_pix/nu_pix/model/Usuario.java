package com.nu_pix.nu_pix.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuario_id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 15)
    private String telefone;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(unique = true, length = 14)
    private String cnpj;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroLogin> logins = new ArrayList<>();
}