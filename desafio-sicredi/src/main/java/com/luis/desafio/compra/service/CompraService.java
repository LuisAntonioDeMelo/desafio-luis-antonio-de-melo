package com.luis.desafio.compra.service;

import com.luis.desafio.compra.model.Compra;
import com.luis.desafio.compra.model.exception.CompraException;
import com.luis.desafio.compra.repository.CompraRepository;
import com.luis.desafio.compra.web.dto.CadastrarCompraDTO;
import com.luis.desafio.produto.dto.ProdutoDTO;
import com.luis.desafio.produto.model.Produto;
import com.luis.desafio.produto.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final ProdutoRepository produtoRepository;
    private final CompraRepository compraRepository;

    private static final int LIMITE_QUANTIDADE = 3;
    private static final String MSG_VALIDACAO_CPF = "Produto: {0} excedeu a quantidade máxima permitida por CPF.";
    private static final String MSG_VALIDACAO_LIMITE_CPF = "Excedeu o limite para cadastro do produto: {0}. Já existem: {1} para esse CPF.";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {CompraException.class})
    public Compra cadastrarCompra(CadastrarCompraDTO cadastrarCompraDTO) throws CompraException {
        List<Produto> produtosParaCompra = cadastrarCompraDTO.produtos().stream()
                .map(this::mapearProdutoDTOParaProduto)
                .toList();

        validarQuantidadeDeProdutosPorCPF(cadastrarCompraDTO.cpf(), produtosParaCompra);

        Compra compra = new Compra();

        String cpfFormatado = cadastrarCompraDTO.cpf().replaceAll("\\D", "");
        compra.setCpf(cpfFormatado);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        compra.setDataCompra(LocalDateTime.parse(cadastrarCompraDTO.dataCompra(), formatter));

        BigDecimal valorTotal = calcularValorTotalCompra(produtosParaCompra);
        compra.setValorTotalCompra(valorTotal);

        compra.setQuantidadeTotal(
                produtosParaCompra.stream()
                        .mapToInt(Produto::getQuantidadeDisponivel)
                        .sum());

        criarProdutos(produtosParaCompra, compra);
        compra.setProdutos(produtosParaCompra);

        return compraRepository.save(compra);
    }

    private void criarProdutos(List<Produto> produtosParaCompra, Compra compra) {
        produtosParaCompra.forEach(produto -> {
            Optional<Produto> produtoDb = produtoRepository.findById(produto.getId());
            if (produtoDb.isEmpty()) {
                produto.setId(null);
                produto = produtoRepository.save(produto);
            } else {
                Produto produtoExistente = produtoDb.get();
                produtoExistente.setNomeProduto(produto.getNomeProduto());
                produtoExistente.setQuantidadeDisponivel(produto.getQuantidadeDisponivel());
                produtoExistente.setValorUnitario(produto.getValorUnitario());
                produto = produtoRepository.save(produtoExistente);
            }
            produto.addCompra(compra);
        });
    }

    private Produto mapearProdutoDTOParaProduto(ProdutoDTO produtoDTO) {
        return Produto.builder()
                .id(produtoDTO.getId())
                .nomeProduto(produtoDTO.getNome())
                .valorUnitario(produtoDTO.getValorUnitario())
                .quantidadeDisponivel(produtoDTO.getQuantidade())
                .build();
    }

    public BigDecimal calcularValorTotalCompra(List<Produto> produtosParaCompra) {
        return produtosParaCompra.stream()
                .map(produto -> produto.getValorUnitario()
                        .multiply(BigDecimal.valueOf(produto.getQuantidadeDisponivel())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void validarQuantidadeDeProdutosPorCPF(String cpf, List<Produto> produtos) throws CompraException {
        List<Compra> compraExistente = compraRepository.compraPorCPF(cpf);

        if (compraExistente.isEmpty()) {
            for (Produto produto : produtos) {
                validaLimiteMaximo(produto);
            }
        } else {
            Map<Long, Produto> produtosParaCompraMap = produtos.stream()
                    .collect(Collectors.toMap(Produto::getId, produto -> produto));
            for (Compra compra : compraExistente) {

                for (Produto produto : compra.getProdutos()) {
                    Produto produtoCompra = produtosParaCompraMap.get(produto.getId());

                    if (produtoCompra != null) {
                        validaLimiteMaximo(produtoCompra);
                        validaLimiteExcedido(produto, produtoCompra);
                    }
                }

            }
        }
    }

    private void validaLimiteMaximo(Produto produto) throws CompraException {
        if (produto.getQuantidadeDisponivel() > LIMITE_QUANTIDADE) {
            throw new CompraException(MessageFormat.format(MSG_VALIDACAO_CPF, produto.getNomeProduto()));
        }
    }

    private void validaLimiteExcedido(Produto produto, Produto produtoCompra) throws CompraException {
        int quantidadeTotal = produto.getQuantidadeDisponivel() + produtoCompra.getQuantidadeDisponivel();
        if (quantidadeTotal > LIMITE_QUANTIDADE) {
            throw new CompraException(MessageFormat.format(MSG_VALIDACAO_LIMITE_CPF,
                    produtoCompra.getNomeProduto(), produto.getQuantidadeDisponivel()));
        }
    }



    public List<Compra> getAll() {
        return compraRepository.findAll();
    }

    public List<Compra> pesquisarCompras(
            String cpf,
            String nomeProduto,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        String cpfFormatado =  cpf.replaceAll("\\D", "");

        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? dataFim.atTime(LocalTime.MAX) : null;
        return compraRepository.pesquisarComprasFiltros(cpfFormatado, nomeProduto, inicio, fim);
    }
}
