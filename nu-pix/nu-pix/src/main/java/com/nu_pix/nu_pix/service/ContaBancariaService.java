package com.nu_pix.nu_pix.service;

import com.nu_pix.nu_pix.model.ContaBancaria;
import com.nu_pix.nu_pix.repository.ContaBancariaRepository;
import com.nu_pix.nu_pix.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nu_pix.nu_pix.model.Usuario;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaBancariaService {

    @Autowired
    private final ContaBancariaRepository contaBancariaRepository;
    private final UsuarioRepository usuarioRepository;
    public ContaBancariaService(ContaBancariaRepository contaBancariaRepository,
                                UsuarioRepository usuarioRepository){
        this.contaBancariaRepository = contaBancariaRepository;
        this.usuarioRepository = usuarioRepository;}

    public ContaBancaria criarConta(Long usuarioId, String senha){
        if(usuarioId == null){
            throw new IllegalArgumentException("Usuario invalido!");
        }
        Usuario usuario = usuarioRepository.findById(usuarioId) .orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado, ID: " + usuarioId));
        ContaBancaria novaConta = new ContaBancaria();
        novaConta.setUsuario(usuario);
        novaConta.setNumero(gerarNumeroConta());
        novaConta.setSaldo(BigDecimal.ZERO);
        novaConta.setSenha(senha);
        return contaBancariaRepository.save(novaConta);
    }
    public void removerConta(Long id){
        if(contaBancariaRepository.existsById(id)){
            contaBancariaRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Erro ao remover a conta");
        }
    }

    public ContaBancaria atualizarConta(Long id, Usuario novoUsuario){
        Optional<ContaBancaria> contaExistente = contaBancariaRepository.findById(id);
        if(contaExistente.isPresent()){
            ContaBancaria conta = contaExistente.get();
            //conta.setSenha(novoUsuario.getSenha());
            conta.getUsuario().setEmail(novoUsuario.getEmail());
            conta.getUsuario().setTelefone(novoUsuario.getTelefone());
            return contaBancariaRepository.save(conta);
        }
        throw new RuntimeException("Erro ao atualizar a conta");
    }
    private String gerarNumeroConta() {
        Random random = new Random();
        String numeroConta;

        do {
            numeroConta = String.format("%06d", random.nextInt(1_000_000));
        } while (contaBancariaRepository.findByNumero(numeroConta).isPresent());

        return numeroConta;
    }

    public ContaBancaria fazerLogin(String numeroConta, String senha) {
        Optional<ContaBancaria> conta = contaBancariaRepository.findByNumero(numeroConta);

        if (conta.isEmpty() || !conta.get().getSenha().equals(senha)) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        return conta.get();
    }

    public ContaBancaria buscarPorNumero(String numeroConta) {
        return contaBancariaRepository. findByNumero(numeroConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta não existe"));
    }
}
