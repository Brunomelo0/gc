package com.nu_pix.nu_pix.service;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.StatusTransacao;
import com.nu_pix.nu_pix.model.Transacao;
import com.nu_pix.nu_pix.repository.ContaBancariaRepository;
import com.nu_pix.nu_pix.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;
    private final ContaBancariaRepository contaBancariaRepository;

    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, ContaBancariaRepository contaBancariaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaBancariaRepository = contaBancariaRepository;
    }


    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }

    public List<Transacao> listarTransacoesPorConta(ContaBancaria contaId) {
        return transacaoRepository.findByContaOrigemOrContaDestino(contaId, contaId);
    }

    public List<Transacao> listarTransacoesPorData(LocalDate dataInicio, LocalDate dataFim) {
        return transacaoRepository.findByDataBetween(dataInicio, dataFim);
    }

    public Transacao agendarTransacao(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, LocalDate dataAgendada) {
        if (dataAgendada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de agendamento deve ser futura.");
        }

        ContaBancaria contaOrigem = contaBancariaRepository.findById(contaOrigemId)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));
        ContaBancaria contaDestino = contaBancariaRepository.findById(contaDestinoId)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."));

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);
        transacao.setData(dataAgendada);
        transacao.setStatus(StatusTransacao.PENDENTE);

        return transacaoRepository.save(transacao);
    }

    public Transacao cancelarTransacao(Long transacaoId) {
        Optional<Transacao> transacaoOpt = transacaoRepository.findById(transacaoId);

        if (transacaoOpt.isEmpty()) {
            throw new IllegalArgumentException("Transação não encontrada.");
        }

        Transacao transacao = transacaoOpt.get();

        if (!transacao.getStatus().equals(StatusTransacao.PENDENTE)) {
            throw new IllegalArgumentException("Apenas transações pendentes podem ser canceladas.");
        }

        transacao.setStatus(StatusTransacao.CANCELADA);
        return transacaoRepository.save(transacao);
    }

    public Transacao estornarTransacao(Long transacaoId) {

        Transacao transacaoOriginal = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));

        if (!transacaoOriginal.getStatus().equals(StatusTransacao.CONCLUIDA)) {
            throw new IllegalArgumentException("Apenas transações concluídas podem ser estornadas.");
        }

        ContaBancaria contaOrigem = transacaoOriginal.getContaOrigem();
        ContaBancaria contaDestino = transacaoOriginal.getContaDestino();
        BigDecimal valor = transacaoOriginal.getValor();

        if (contaDestino.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta de destino para estorno.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().add(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().subtract(valor));

        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);

        transacaoOriginal.setStatus(StatusTransacao.ESTORNADA);
        transacaoRepository.save(transacaoOriginal);

        Transacao transacaoEstorno = new Transacao();
        transacaoEstorno.setContaOrigem(contaDestino);
        transacaoEstorno.setContaDestino(contaOrigem);
        transacaoEstorno.setValor(valor);
        transacaoEstorno.setData(LocalDate.now());
        transacaoEstorno.setStatus(StatusTransacao.ESTORNADA);

        return transacaoRepository.save(transacaoEstorno);
    }

}