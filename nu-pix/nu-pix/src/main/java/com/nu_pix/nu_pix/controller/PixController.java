package com.nu_pix.nu_pix.controller;

import com.nu_pix.nu_pix.dto.TransferenciaDTO;
import com.nu_pix.nu_pix.model.ChavePix;
import com.nu_pix.nu_pix.service.PixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pix")
public class PixController {

    private final PixService pixService;

    @Autowired
    public PixController(PixService pixService) {
        this.pixService = pixService;
    }

    @PostMapping("/gerar/{contaId}")
    public ResponseEntity<?> gerarChave(@PathVariable Long contaId, @RequestBody ChavePix chavePix) {
        try {
            Map<String, Object> resposta = new HashMap<>();
            ChavePix chaveAssociada = pixService.gerarChave(contaId, chavePix);

            resposta.put("mensagem", "Chave cadastrada com sucesso");
            resposta.put("chave", chaveAssociada.getValor());

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<Map<String, String>> transferirValor(@RequestBody Map<String, Object> dados) {
        try {
            String chavePixOrigem = (String) dados.get("chavePixOrigem");
            String chavePixDestino = (String) dados.get("chavePixDestino");
            BigDecimal valor = new BigDecimal(dados.get("valor").toString());


            String emailDestino = pixService.transferirValor(chavePixOrigem, chavePixDestino, valor);

            String conteudoEmail = "Olá, você recebeu uma transferência no valor de " + valor.toString() + ".";

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Transferência realizada com sucesso.");
            response.put("email", emailDestino);
            response.put("conteudoEmail", conteudoEmail);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno: " + e.getMessage()));
        }
    }

    @PostMapping("/receber")
    public ResponseEntity<String> receber(@RequestBody Map<String, Object> qrCode) {
        try {
            String chavePix = (String) qrCode.get("chavePix");
            BigDecimal valor = new BigDecimal(qrCode.get("valor").toString());

            pixService.receberViaPix(chavePix, valor);
            return ResponseEntity.ok("Valor recebido com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/desbloquear")
    public ResponseEntity<Map<String, String>> desbloquearChavePix(@RequestBody Map<String, String> payload) {
        try {
            String chave = payload.get("chavePix");
            pixService.desbloquearChavePix(chave);
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Chave PIX desbloqueada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno: " + e.getMessage()));
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Map<String, String>> alterarChavePix(@RequestBody Map<String, String> payload) {
        try {
            String valorAntigo = payload.get("valorAntigo");
            String novoValor = payload.get("novoValor");
            pixService.alterarChavePix(valorAntigo, novoValor);
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Chave PIX alterada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensagem", "Erro interno: " + e.getMessage()));
        }
    }
}