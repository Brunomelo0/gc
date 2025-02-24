package com.nu_pix.nu_pix.repository;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.TipoChavePix;
import com.nu_pix.nu_pix.model.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {
    
    Optional<ChavePix> findByValor(String valor);
    Optional<ChavePix> findByTipoAndConta(TipoChavePix tipo, ContaBancaria conta);
    
    List<ChavePix> findBySuspensaTrue();

    Optional<ChavePix> findByValorAndSuspensaTrue(String valor);
}
