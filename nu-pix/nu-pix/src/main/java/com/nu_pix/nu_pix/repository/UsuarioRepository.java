package com.nu_pix.nu_pix.repository;

import com.nu_pix.nu_pix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
}
