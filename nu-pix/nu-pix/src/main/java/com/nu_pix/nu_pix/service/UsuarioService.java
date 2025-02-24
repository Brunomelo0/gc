package com.nu_pix.nu_pix.service;

import com.nu_pix.nu_pix.model.*;
import com.nu_pix.nu_pix.repository.ChavePixRepository;
import com.nu_pix.nu_pix.repository.ContaBancariaRepository;
import com.nu_pix.nu_pix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final ChavePixRepository chavePixRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          ContaBancariaRepository contaBancariaRepository,
                          ChavePixRepository chavePixRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaBancariaRepository = contaBancariaRepository;
        this.chavePixRepository = chavePixRepository;
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        validarUsuarioParaCadastro(usuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        validarUsuarioParaAtualizacao(usuario, usuarioAtualizado);

        boolean emailAlterado = !usuario.getEmail().equals(usuarioAtualizado.getEmail());
        boolean telefoneAlterado = !usuario.getTelefone().equals(usuarioAtualizado.getTelefone());

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setTelefone(usuarioAtualizado.getTelefone());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        if (emailAlterado) {
            atualizarChavesPix(usuarioSalvo, TipoChavePix.EMAIL, usuarioSalvo.getEmail());
        }
        if (telefoneAlterado) {
            atualizarChavesPix(usuarioSalvo, TipoChavePix.TELEFONE, usuarioSalvo.getTelefone());
        }

        return usuarioSalvo;
    }

    public void removerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        List<ContaBancaria> contas = contaBancariaRepository.findByUsuario(usuario);
        contas.forEach(conta -> {
            contaBancariaRepository.delete(conta);
        });

        usuarioRepository.delete(usuario);
    }

    private void validarUsuarioParaCadastro(Usuario usuario) {
        if (usuario.getCpf() == null || usuario.getCpf().isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("Já existe um usuário com este CPF.");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }
    }

    private void validarUsuarioParaAtualizacao(Usuario usuarioExistente, Usuario usuarioAtualizado) {
        if (usuarioRepository.existsByEmail(usuarioAtualizado.getEmail()) &&
                !usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }
    }

    private void atualizarChavesPix(Usuario usuario, TipoChavePix tipo, String novoValor) {
        List<ContaBancaria> contas = contaBancariaRepository.findByUsuario(usuario);
        contas.forEach(conta -> {
            Optional<ChavePix> chaveOpt = chavePixRepository.findByTipoAndConta(tipo, conta);
            chaveOpt.ifPresent(chave -> {
                chave.setValor(novoValor);
                chavePixRepository.save(chave);
            });
        });
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}