package com.luis.desafio.compra.web.dto;

import com.luis.desafio.produto.dto.ProdutoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record CadastrarCompraDTO(
        String cpf,
        List<ProdutoDTO> produtos,
        String dataCompra
){}
