package br.com.ratacheski.bluefood.domain.restaurante;

import br.com.ratacheski.bluefood.util.StringUtils;
import lombok.Data;

@Data
public class SearchFilter {
    public enum SearchType {
        TEXTO, CATEGORIA
    }

    public enum Order {
        TAXA, TEMPO
    }

    public enum Command {
        ENTREGA_GRATIS,
        MAIOR_TAXA,
        MENOR_TAXA,
        MAIOR_TEMPO,
        MENOR_TEMPO
    }

    private String texto;
    private SearchType searchType;
    private Long idCategoria;
    private Order order = Order.TAXA;
    private boolean asc = true;
    private boolean entregaGratis;

    public void processFilter(String cmdString) {
        if (!StringUtils.isEmpty(cmdString)) {
            Command command = Command.valueOf(cmdString);
            if (command == Command.ENTREGA_GRATIS) {
                entregaGratis = !entregaGratis;
            } else if (command == Command.MAIOR_TAXA) {
                order = Order.TAXA;
                asc = false;
            } else if (command == Command.MENOR_TAXA) {
                order = Order.TAXA;
                asc = true;
            } else if (command == Command.MAIOR_TEMPO) {
                order = Order.TEMPO;
                asc = false;
            } else if (command == Command.MENOR_TEMPO) {
                order = Order.TEMPO;
                asc = true;
            }
        }
        if (searchType == SearchType.TEXTO) {
            idCategoria = null;
        } else if (searchType == SearchType.CATEGORIA) {
            texto = null;
        }
    }
}
