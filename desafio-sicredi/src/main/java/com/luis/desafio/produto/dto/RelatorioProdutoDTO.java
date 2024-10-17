package com.luis.desafio.produto.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RelatorioProdutoDTO {

    private String nomeProduto;
    private BigDecimal valorUnitario;
    private Long quantidadeCompras;
    private BigDecimal totalVendido;

    public RelatorioProdutoDTO(String nomeProduto, BigDecimal valorUnitario, Long quantidadeCompras, BigDecimal totalVendido) {
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
        this.quantidadeCompras = quantidadeCompras;
        this.totalVendido = totalVendido;
    }
}
