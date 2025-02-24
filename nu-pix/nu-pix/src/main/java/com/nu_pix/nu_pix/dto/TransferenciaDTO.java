package com.nu_pix.nu_pix.dto;

import java.math.BigDecimal;

public class TransferenciaDTO {
    private String chavePixOrigem;
    private String chavePixDestino;
    private BigDecimal valor;

    // Getters e Setters
    public String getChavePixOrigem() {
        return chavePixOrigem;
    }

    public void setChavePixOrigem(String chavePixOrigem) {
        this.chavePixOrigem = chavePixOrigem;
    }

    public String getChavePixDestino() {
        return chavePixDestino;
    }

    public void setChavePixDestino(String chavePixDestino) {
        this.chavePixDestino = chavePixDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}