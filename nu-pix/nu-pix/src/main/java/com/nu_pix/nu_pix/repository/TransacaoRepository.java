package com.nu_pix.nu_pix.repository;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.StatusTransacao;
import com.nu_pix.nu_pix.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaOrigemOrContaDestino(ContaBancaria contaOrigem, ContaBancaria contaDestino);

    List<Transacao> findByDataBetween(LocalDate start, LocalDate end);

    List<Transacao> findByStatus(StatusTransacao status);

    List<Transacao> findByContaOrigemAndData(ContaBancaria contaOrigem, LocalDate data);
}