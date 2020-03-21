package br.com.ratacheski.bluefood.application.service;

import br.com.ratacheski.bluefood.domain.pedido.*;
import br.com.ratacheski.bluefood.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listPedidos(Long restauranteId, RelatorioPedidoFilter filter) {
        if (filter.getIdPedido() != null) {
            Pedido pedido = pedidoRepository.findByIdAndRestaurante_Id(filter.getIdPedido(), restauranteId);
            return CollectionUtils.listOf(pedido);
        }

        if (filter.getDataInicial() == null) {
            return CollectionUtils.listOf();
        }
        if (filter.getDataFinal() == null) {
            filter.setDataFinal(LocalDate.now());
        }
        return pedidoRepository.findByDateInterval(restauranteId,
                filter.getDataInicial().atStartOfDay(),
                filter.getDataFinal().atTime(23, 59, 59));
    }

    public List<RelatorioItemFaturamento> calcularFaturamentoItens(Long restauranteId, RelatorioItemFilter filter) {
        List<RelatorioItemFaturamento> itens = new ArrayList<>();
        List<Object[]> objects;
        if (filter.getDataInicial() == null) {
            return CollectionUtils.listOf();
        }
        if (filter.getDataFinal() == null) {
            filter.setDataFinal(LocalDate.now());
        }
        if (filter.getItemId() != 0) {
            objects = pedidoRepository.findItensForFaturamento(restauranteId,
                    filter.getItemId(), filter.getDataInicial().atStartOfDay(),
                    filter.getDataFinal().atTime(23, 59, 59));

        } else {
            objects = pedidoRepository.findItensForFaturamento(restauranteId,
                    filter.getDataInicial().atStartOfDay(),
                    filter.getDataFinal().atTime(23, 59, 59));
        }
        objects.forEach(object -> {
            RelatorioItemFaturamento relatorioItemFaturamento = new RelatorioItemFaturamento();
            relatorioItemFaturamento.setNome((String) object[0]);
            relatorioItemFaturamento.setQuantidade((Long) object[1]);
            relatorioItemFaturamento.setValor((BigDecimal) object[2]);
            itens.add(relatorioItemFaturamento);
        });
        return itens;
    }
}
