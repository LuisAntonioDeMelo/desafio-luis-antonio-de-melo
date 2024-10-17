package com.luis.desafio.compra.model;

import com.luis.desafio.produto.model.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id")
    private Long id;

    private String cpf;

    @ManyToMany
    @JoinTable(
            name = "compra_produto",
            joinColumns = @JoinColumn(name = "compra_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime dataCompra;

    private Integer quantidadeTotal;

    private BigDecimal valorTotalCompra;

    @PrePersist
    private void salvarData() {
        if(dataCompra == null) {
            dataCompra = LocalDateTime.now();
        }
    }


}
