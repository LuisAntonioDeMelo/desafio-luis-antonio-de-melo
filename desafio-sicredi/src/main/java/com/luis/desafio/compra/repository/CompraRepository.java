package com.luis.desafio.compra.repository;

import com.luis.desafio.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query(
            """
            select c from Compra c
            inner join fetch c.produtos prod
            where c.cpf = :cpf
            """
    )
   public List<Compra> compraPorCPF(@Param("cpf") String cpf);

    @Query(
        """
        SELECT c FROM Compra c JOIN c.produtos p
        WHERE (:cpf IS NULL OR c.cpf = :cpf)
        AND (:nomeProduto IS NULL OR p.nomeProduto LIKE %:nomeProduto%)
        AND ( CAST(:dataInicio AS date) IS NULL OR c.dataCompra >= :dataInicio)
        AND ( CAST(:dataFim AS date) IS NULL OR c.dataCompra <= :dataFim)
    """)
    List<Compra> pesquisarComprasFiltros(
            @Param("cpf") String cpf,
            @Param("nomeProduto") String nomeProduto,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
