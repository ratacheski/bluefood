package br.com.ratacheski.bluefood.domain.pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class RelatorioItemFaturamento {
    private String nome;
    private Long quantidade;
    private BigDecimal valor;
}
