package br.com.ratacheski.bluefood.domain.pedido;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ItemPedidoPK implements Serializable {
    @NotNull
    @ManyToOne
    private Pedido pedido;
    @NotNull
    private Integer ordem;
}
