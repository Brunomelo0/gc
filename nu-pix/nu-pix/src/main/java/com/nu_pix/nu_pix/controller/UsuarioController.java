package com.nu_pix.nu_pix.controller;

import com.nu_pix.nu_pix.model.Usuario;
import com.nu_pix.nu_pix.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        try {
            Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.removerUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
