package br.com.ratacheski.bluefood.domain.pedido;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RelatorioPedidoFilter {
    private Long idPedido;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicial;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFinal;
}
