package com.nu_pix.nu_pix.controller;

import com.nu_pix.nu_pix.model.RegistroLogin;
import com.nu_pix.nu_pix.service.RegistroLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logins")
public class RegistroLoginController {

    private final RegistroLoginService service;

    public RegistroLoginController(RegistroLoginService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarLogin(@RequestParam Long usuarioId, @RequestParam String acao) {
        service.registrarAcao(usuarioId, acao);
        return ResponseEntity.ok("Ação registrada com sucesso.");
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Map<String, Object>>> listarLogins(@PathVariable Long usuarioId) {
        List<RegistroLogin> registros = service.listarAcoesUsuario(usuarioId);
        List<Map<String, Object>> response = registros.stream().map(registro -> {
            Map<String, Object> map = new HashMap<>();
            map.put("nome", registro.getUsuario().getNome());
            map.put("dataLogin", registro.getDataHora());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}