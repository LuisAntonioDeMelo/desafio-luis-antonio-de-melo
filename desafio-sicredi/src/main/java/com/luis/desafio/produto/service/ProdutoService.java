package com.luis.desafio.produto.service;

import com.luis.desafio.compra.repository.CompraRepository;
import com.luis.desafio.produto.dto.RelatorioProdutoDTO;
import com.luis.desafio.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<RelatorioProdutoDTO> obterRelatorioProdutos(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return produtoRepository.gerarRelatorioProdutos(dataInicio, dataFim);
    }
}
