package br.com.ratacheski.bluefood.domain.pedido;

import lombok.Getter;

@Getter
public enum StatusPedido {
    PRODUCAO(1, "Em Produção", false),
    ENTREGA(2, "Saiu para Entrega", false),
    CONCLUIDO(3, "Concluído", true);

    int ordem;
    String descricao;
    boolean ultimoStatus;

    StatusPedido(int ordem, String descricao, boolean ultimoStatus) {
        this.ordem = ordem;
        this.descricao = descricao;
        this.ultimoStatus = ultimoStatus;
    }

    public static StatusPedido fromOrdem(int ordem){
        for (StatusPedido statusPedido : StatusPedido.values()){
            if(statusPedido.getOrdem() == ordem){
                return statusPedido;
            }
        }
        return null;
    }
}
