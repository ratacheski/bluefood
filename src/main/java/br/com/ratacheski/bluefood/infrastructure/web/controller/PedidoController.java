package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.domain.pedido.Pedido;
import br.com.ratacheski.bluefood.domain.pedido.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/cliente/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping(path = "/view")
    public String viewPedido(
            @RequestParam("pedidoId") Long pedidoId,
            Model model) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("pedido", pedido);
        return "cliente-pedido";
    }
}
