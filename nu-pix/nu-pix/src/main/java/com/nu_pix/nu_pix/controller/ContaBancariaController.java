package com.nu_pix.nu_pix.controller;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.model.Usuario;
import com.nu_pix.nu_pix.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/conta-bancaria")
public class ContaBancariaController {
    @Autowired
    private final ContaBancariaService contaBancariaService;
    public ContaBancariaController(ContaBancariaService contaBancariaService) {
        this.contaBancariaService = contaBancariaService;
    }

    @PostMapping("/usuario/{usuarioId}/nova-conta")
    public ResponseEntity<?> criarConta(@PathVariable Long usuarioId, @RequestBody String senha) {
        try {

            Map<String, Object> resposta = new HashMap<>();
            ContaBancaria contaCriada = contaBancariaService.criarConta(usuarioId, senha);

            resposta.put("mensagem", "Conta criada com sucesso.");
            resposta.put("numeroConta", contaCriada.getNumero());
            resposta.put("usuario", contaCriada.getUsuario());
            resposta.put("saldo", contaCriada.getSaldo().doubleValue());

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerConta(@PathVariable Long id){
        try{
            contaBancariaService.removerConta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ContaBancaria> atualizarConta(@PathVariable Long id, @RequestBody Usuario novoUsuario){
        try {
            ContaBancaria contaAtualizada = contaBancariaService.atualizarConta(id, novoUsuario);
            return ResponseEntity.ok(contaAtualizada);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> login) {
        try {
            String numeroConta = login.get("numero");
            String senha = login.get("senha");

            ContaBancaria conta = contaBancariaService.fazerLogin(numeroConta, senha);

            return ResponseEntity.ok(conta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{numeroConta}")
    public ResponseEntity<?> buscarPorNumero(@PathVariable String numeroConta) {
        try {
            ContaBancaria conta = contaBancariaService.buscarPorNumero(numeroConta);
            return ResponseEntity.ok(conta);
        } catch (IllegalArgumentException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }

}
