package com.luis.desafio.produto.repository;

import com.luis.desafio.produto.dto.RelatorioProdutoDTO;
import com.luis.desafio.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(
            """
            SELECT new com.luis.desafio.produto.dto.RelatorioProdutoDTO(
                p.nomeProduto, 
                p.valorUnitario, 
                COUNT(c) AS quantidadeCompras, 
                SUM(c.valorTotalCompra)
            )
            FROM Compra c
            JOIN c.produtos p
            WHERE (CAST(:dataInicio AS date) IS NULL OR c.dataCompra >= :dataInicio)
            AND (CAST(:dataFim AS date) IS NULL OR c.dataCompra <= :dataFim)
            GROUP BY p.nomeProduto, p.valorUnitario
            """)
    List<RelatorioProdutoDTO> gerarRelatorioProdutos(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
