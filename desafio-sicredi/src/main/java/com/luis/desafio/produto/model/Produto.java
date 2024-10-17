package com.luis.desafio.produto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luis.desafio.compra.model.Compra;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    private Long id;

    private String nomeProduto;
    private BigDecimal valorUnitario;
    private Integer quantidadeDisponivel;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "produtos", fetch = FetchType.LAZY)
    private List<Compra> compras;

    public void addCompra(Compra compra) {
        if(compras == null || compras.isEmpty()) {
            compras = new ArrayList<>();
        }
        compras.add(compra);
    }

}
