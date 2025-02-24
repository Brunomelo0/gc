package com.nu_pix.nu_pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    Optional<ContaBancaria> findByNumero(String numero);
    List<ContaBancaria> findByUsuario(Usuario usuario);
}
