package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.application.service.PagamentoException;
import br.com.ratacheski.bluefood.application.service.PedidoService;
import br.com.ratacheski.bluefood.domain.pedido.Carrinho;
import br.com.ratacheski.bluefood.domain.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/cliente/pagamento")
@SessionAttributes("carrinho")
public class PagamentoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping(path = "/pagar")
    public String pagar(@RequestParam("numCartao") String numCartao,
                        @ModelAttribute("carrinho") Carrinho carrinho,
                        SessionStatus sessionStatus,
                        Model model) {
        Pedido pedido = null;
        try {
            pedido = pedidoService.criarEPagar(carrinho, numCartao);
            sessionStatus.setComplete();
            return "redirect:/cliente/pedido/view?pedidoId=" + pedido.getId();
        } catch (PagamentoException e) {
            model.addAttribute("msg", e.getMessage());
            return "cliente-carrinho";
        }
    }
}
