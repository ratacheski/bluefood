package br.com.ratacheski.bluefood.domain.pedido;

import br.com.ratacheski.bluefood.domain.restaurante.ItemCardapio;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "bf_item_pedido", schema = "bf")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ItemPedidoPK id;

    @ManyToOne
    @NotNull
    private ItemCardapio itemCardapio;

    @NotNull
    private Integer quantidade;

    @Size(max = 50)
    private String observacoes;

    @NotNull
    private BigDecimal preco;

    public BigDecimal getPrecoCalculado(){
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

}
