package com.nu_pix.nu_pix.repository;

import com.nu_pix.nu_pix.model.RegistroLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistroLoginRepository extends JpaRepository<RegistroLogin, Long> {

    @Query("select rl from RegistroLogin rl where rl.usuario.usuario_id = ?1")
    List<RegistroLogin> findByUsuarioId(Long usuarioId);
}
