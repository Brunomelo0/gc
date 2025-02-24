package com.nu_pix.nu_pix.service;

import com.nu_pix.nu_pix.model.RegistroLogin;
import com.nu_pix.nu_pix.model.Usuario;
import com.nu_pix.nu_pix.repository.RegistroLoginRepository;
import com.nu_pix.nu_pix.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroLoginService {

    private final RegistroLoginRepository repository;
    private final UsuarioRepository usuarioRepository;

    public RegistroLoginService(RegistroLoginRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public void registrarAcao(Long usuarioId, String acao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        RegistroLogin registro = new RegistroLogin(usuario, acao, LocalDateTime.now());
        repository.save(registro);
    }

    public List<RegistroLogin> listarAcoesUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}