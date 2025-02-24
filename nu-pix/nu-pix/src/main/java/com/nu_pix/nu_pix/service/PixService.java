package com.nu_pix.nu_pix.service;

import com.nu_pix.nu_pix.model.ChavePix;
import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.StatusTransacao;
import com.nu_pix.nu_pix.model.TipoChavePix;
import com.nu_pix.nu_pix.model.Transacao;
import com.nu_pix.nu_pix.model.Validator;
import com.nu_pix.nu_pix.repository.ChavePixRepository;
import com.nu_pix.nu_pix.repository.ContaBancariaRepository;
import com.nu_pix.nu_pix.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class PixService {

    private final ChavePixRepository chavePixRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final TransacaoRepository transacaoRepository;
    private final EmailService emailService;

    @Autowired
    public PixService(ChavePixRepository chavePixRepository,
                      ContaBancariaRepository contaBancariaRepository,
                      TransacaoRepository transacaoRepository,
                      EmailService emailService) {
        this.chavePixRepository = chavePixRepository;
        this.contaBancariaRepository = contaBancariaRepository;
        this.transacaoRepository = transacaoRepository;
        this.emailService = emailService;
    }

    private String gerarChaveAleatoria() {
        return UUID.randomUUID().toString();
    }

    private void validarChaveUnica(String valor) {
        if (chavePixRepository.findByValor(valor).isPresent()) {
            throw new IllegalArgumentException("Chave Pix já cadastrada.");
        }
    }

    private void validarChaveTipoUnicoPorConta(TipoChavePix tipo, ContaBancaria conta) {
        if (tipo == TipoChavePix.CPF || tipo == TipoChavePix.CNPJ) {
            if (chavePixRepository.findByTipoAndConta(tipo, conta).isPresent()) {
                throw new IllegalArgumentException("Já existe uma chave desse tipo associada a essa conta.");
            }
        }
    }

    private void validarFormatoChave(TipoChavePix tipo, String valor) {
        if (tipo == TipoChavePix.CPF && !Validator.validarCpf(valor)) {
            throw new IllegalArgumentException("CPF inválido.");
        } else if (tipo == TipoChavePix.CNPJ && !Validator.validarCnpj(valor)) {
            throw new IllegalArgumentException("CNPJ inválido.");
        } else if (tipo == TipoChavePix.TELEFONE && !Validator.validarTelefone(valor)) {
            throw new IllegalArgumentException("Telefone inválido.");
        } else if (tipo == TipoChavePix.EMAIL && !Validator.validarEmail(valor)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    private void validarChave(TipoChavePix tipo, String valor, ContaBancaria conta) {
        validarFormatoChave(tipo, valor);
        validarChaveUnica(valor);
        validarChaveTipoUnicoPorConta(tipo, conta);
    }

    public ChavePix gerarChave(Long contaId, ChavePix chavePix) {
        Optional<ContaBancaria> contaExistente = contaBancariaRepository.findById(contaId);

        if (contaExistente.isEmpty()) {
            throw new IllegalArgumentException("Conta bancária não encontrada.");
        }

        ContaBancaria conta = contaExistente.get();

        if (chavePix.getTipo() == TipoChavePix.ALEATORIA) {
            chavePix.setValor(gerarChaveAleatoria());
        } else {
            validarChave(chavePix.getTipo(), chavePix.getValor(), conta);
        }

        chavePix.setConta(conta);
        return chavePixRepository.save(chavePix);
    }

    public String transferirValor(String chavePixOrigem, String chavePixDestino, BigDecimal valor) {
        Optional<ChavePix> pixOrigem = chavePixRepository.findByValor(chavePixOrigem);
        Optional<ChavePix> pixDestino = chavePixRepository.findByValor(chavePixDestino);

        if (pixOrigem.isEmpty()) {
            throw new IllegalArgumentException("Chave Pix de origem inválida.");
        }
        if (pixDestino.isEmpty()) {
            throw new IllegalArgumentException("Chave Pix de destino inválida.");
        }

        ContaBancaria contaOrigem = pixOrigem.get().getConta();
        ContaBancaria contaDestino = pixDestino.get().getConta();

        BigDecimal totalTransacoesHoje = transacaoRepository
                .findByContaOrigemAndData(contaOrigem, LocalDate.now())
                .stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (contaOrigem.getLimiteDiario() != null &&
                totalTransacoesHoje.add(valor).compareTo(contaOrigem.getLimiteDiario()) > 0) {
            throw new IllegalArgumentException("Limite diário excedido.");
        }

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);
        transacao.setData(LocalDate.now());
        transacao.setStatus(StatusTransacao.CONCLUIDA);
        transacaoRepository.save(transacao);

        String assunto = "Você recebeu uma transferência PIX";
        String corpo = String.format("Olá, você recebeu uma transferência no valor de %s.", valor.toString());
        try {
            emailService.enviarEmail(contaDestino.getUsuario().getEmail(), assunto, corpo);
        } catch (Exception e) {
            System.err.println("Falha ao enviar email: " + e.getMessage());
        }

        return contaDestino.getUsuario().getEmail();
    }

    public void receberViaPix(String chavePix, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor a ser recebido deve ser positivo.");
        }

        Optional<ChavePix> chavePixExiste = chavePixRepository.findByValor(chavePix);

        if (chavePixExiste.isEmpty()) {
            throw new IllegalArgumentException("Chave Pix não encontrada.");
        }

        ChavePix chave = chavePixExiste.get();
        ContaBancaria contaDestino = chave.getConta();

        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        contaBancariaRepository.save(contaDestino);
    }

    public void desbloquearChavePix(String valor) {
        Optional<ChavePix> chavePix = chavePixRepository.findByValorAndSuspensaTrue(valor);

        if (chavePix.isEmpty()) {
            throw new IllegalArgumentException("Chave Pix não encontrada ou não está suspensa.");
        }

        ChavePix chave = chavePix.get();
        chave.setSuspensa(false);
        chavePixRepository.save(chave);
    }

    public void alterarChavePix(String valorAntigo, String novoValor) {
        Optional<ChavePix> chavePix = chavePixRepository.findByValor(valorAntigo);

        if (chavePix.isEmpty()) {
            throw new IllegalArgumentException("Chave Pix não encontrada.");
        }

        if (chavePixRepository.findByValor(novoValor).isPresent()) {
            throw new IllegalArgumentException("O novo valor da Chave Pix já está em uso.");
        }

        ChavePix chave = chavePix.get();
        validarFormatoChave(chave.getTipo(), novoValor);
        chave.setValor(novoValor);
        chavePixRepository.save(chave);
    }
}