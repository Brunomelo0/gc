package com.nu_pix.nu_pix.controller;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.Transacao;
import com.nu_pix.nu_pix.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public List<Transacao> listarTransacoes() {
        return transacaoService.listarTransacoes();
    }

    @GetMapping("/conta/{contaId}")
    public List<Transacao> listarTransacoesPorConta(@PathVariable ContaBancaria contaId) {
        return transacaoService.listarTransacoesPorConta(contaId);
    }

    @GetMapping("/data")
    public List<Transacao> listarTransacoesPorData(@RequestParam("inicio") String inicio,
                                                   @RequestParam("fim") String fim) {
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        return transacaoService.listarTransacoesPorData(dataInicio, dataFim);
    }

    @PostMapping("/agendar")
    public ResponseEntity<Map<String, String>> agendarTransacao(@RequestBody Map<String, Object> payload) {
        try {
            Long contaOrigemId = Long.valueOf(payload.get("contaOrigemId").toString());
            Long contaDestinoId = Long.valueOf(payload.get("contaDestinoId").toString());
            BigDecimal valor = new BigDecimal(payload.get("valor").toString());
            LocalDate dataAgendada = LocalDate.parse(payload.get("data").toString());

            transacaoService.agendarTransacao(contaOrigemId, contaDestinoId, valor, dataAgendada);

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "A transação foi agendada para a data " + dataAgendada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno: " + e.getMessage()));
        }
    }

    @PutMapping("/cancelar/{transacaoId}")
    public Transacao cancelarTransacao(@PathVariable Long transacaoId) {
        return transacaoService.cancelarTransacao(transacaoId);
    }

    @PostMapping("/estornar/{transacaoId}")
    public ResponseEntity<Map<String, String>> estornarTransacao(@PathVariable Long transacaoId) {
        try {
            Transacao transacaoEstorno = transacaoService.estornarTransacao(transacaoId);
            BigDecimal valorEstornado = transacaoEstorno.getValor();
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "A transação foi estornada com sucesso, valor: " + valorEstornado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno: " + e.getMessage()));
        }
    }
}
