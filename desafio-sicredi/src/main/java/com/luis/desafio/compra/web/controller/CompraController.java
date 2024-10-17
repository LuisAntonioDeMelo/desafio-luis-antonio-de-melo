package com.luis.desafio.compra.web.controller;

import com.luis.desafio.compra.model.Compra;
import com.luis.desafio.compra.service.CompraService;
import com.luis.desafio.compra.web.dto.CadastrarCompraDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Cadastrar uma nova compra",
            description = "Cadastra uma nova compra a partir dos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar a compra")
    })
    public ResponseEntity<?> candidatarParaVaga(@RequestBody CadastrarCompraDTO cadastrarCompraDTO) {
        try {
            Compra compra = compraService.cadastrarCompra(cadastrarCompraDTO);
            return ResponseEntity.ok(compra);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pesquisar")
    @Operation(summary = "Pesquisar compras",
            description = "Pesquisa compras com base nos filtros fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma compra encontrada")
    })
    public ResponseEntity<List<Compra>> pesquisarCompras(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String nomeProduto,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<Compra> compras = compraService.pesquisarCompras(cpf, nomeProduto, dataInicio, dataFim);
        return ResponseEntity.ok(compras);
    }

}
