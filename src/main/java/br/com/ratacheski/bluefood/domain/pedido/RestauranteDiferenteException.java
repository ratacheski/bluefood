package br.com.ratacheski.bluefood.domain.pedido;

public class RestauranteDiferenteException extends Exception {
    public RestauranteDiferenteException() {
    }
    public RestauranteDiferenteException(String message) {
        super(message);
    }
}
