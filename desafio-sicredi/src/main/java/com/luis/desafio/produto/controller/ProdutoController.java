package com.luis.desafio.produto.controller;

import com.luis.desafio.produto.dto.RelatorioProdutoDTO;
import com.luis.desafio.produto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioProdutoDTO>> gerarRelatorioProdutos(
            @RequestParam(required = false) LocalDateTime dataInicio,
            @RequestParam(required = false) LocalDateTime dataFim) {
        List<RelatorioProdutoDTO> relatorio = produtoService.obterRelatorioProdutos(dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}