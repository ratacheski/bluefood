package br.com.ratacheski.bluefood.domain.pedido;

import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.pagamento.Pagamento;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bf_pedido", schema = "bf")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime data;

    @ManyToOne
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @NotNull
    private Restaurante restaurante;

    @NotNull
    private BigDecimal subTotal;

    @NotNull
    private BigDecimal taxaEntrega;

    @NotNull
    private BigDecimal total;

    @NotNull
    private StatusPedido statusPedido;

    @OneToMany(mappedBy = "id.pedido", fetch = FetchType.EAGER)
    private Set<ItemPedido> itensPedido;

    public String getFormattedId(){
        return String.format("#%04d", id);
    }

    public void progredirStatus(){
        StatusPedido novoStatus = StatusPedido.fromOrdem(this.statusPedido.getOrdem() + 1);
        if (novoStatus != null){
            this.statusPedido = novoStatus;
        }
    }
}
