package com.luis.desafio.compra.web.controller;

import com.luis.desafio.compra.model.Compra;
import com.luis.desafio.compra.service.CompraService;
import com.luis.desafio.compra.web.dto.CadastrarCompraDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @GetMapping
    public List<Compra> getAll() {
        return compraService.getAll();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> candidatarParaVaga(@RequestBody CadastrarCompraDTO cadastrarCompraDTO) {
        try {
            Compra compra = compraService.cadastrarCompra(cadastrarCompraDTO);
            return ResponseEntity.ok(compra);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<Compra>> pesquisarCompras(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String nomeProduto,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<Compra> compras = compraService.pesquisarCompras(cpf, nomeProduto, dataInicio, dataFim);
        return ResponseEntity.ok(compras);
    }

}
