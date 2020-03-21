package br.com.ratacheski.bluefood.domain.pedido;

import br.com.ratacheski.bluefood.domain.restaurante.ItemCardapio;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class Carrinho implements Serializable {

    private List<ItemPedido> itens = new ArrayList<>();
    private Restaurante restaurante;

    public void adicionarItem(ItemCardapio itemCardapio, Integer quantidade, String obervacoes) throws RestauranteDiferenteException {
        if (itens.size() == 0) {
            restaurante = itemCardapio.getRestaurante();
        } else if (!itemCardapio.getRestaurante().getId().equals(restaurante.getId())) {
            throw new RestauranteDiferenteException();
        }
        if (!exists(itemCardapio)) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setItemCardapio(itemCardapio);
            itemPedido.setQuantidade(quantidade);
            itemPedido.setObservacoes(obervacoes);
            itemPedido.setPreco(itemCardapio.getPreco());
            itens.add(itemPedido);
        }
    }

    public void adicionarItem(ItemPedido itemPedido) {
        try {
            adicionarItem(itemPedido.getItemCardapio(), itemPedido.getQuantidade(), itemPedido.getObservacoes());
        } catch (RestauranteDiferenteException e) {
        }
    }

    public void removerItem(ItemCardapio itemCardapio) {
        for (Iterator<ItemPedido> iterator = itens.iterator(); iterator.hasNext(); ) {
            ItemPedido itemPedido = iterator.next();
            if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
                iterator.remove();
                break;
            }
        }
        if (itens.size() == 0) {
            restaurante = null;
        }
    }

    private boolean exists(ItemCardapio itemCardapio) {
        for (ItemPedido itemPedido : itens) {
            if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal getPrecoTotal(boolean adicionarTaxaEntrega) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido itemPedido : itens) {
            total = total.add(itemPedido.getPrecoCalculado());
        }
        if (adicionarTaxaEntrega) {
            total = total.add(restaurante.getTaxaEntrega());
        }
        return total;
    }

    public void limpar() {
        itens.clear();
        restaurante = null;
    }

    public boolean vazio() {
        return itens.size() == 0;
    }
}
