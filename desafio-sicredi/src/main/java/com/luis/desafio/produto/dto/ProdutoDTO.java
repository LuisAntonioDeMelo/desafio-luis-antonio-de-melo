package com.luis.desafio.produto.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDTO {
    private Long id;
    private String nome;
    private Integer quantidade;
    private BigDecimal valorUnitario;
}
