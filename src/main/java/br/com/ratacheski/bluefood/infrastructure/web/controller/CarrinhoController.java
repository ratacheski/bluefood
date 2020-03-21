package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.application.service.ImageService;
import br.com.ratacheski.bluefood.domain.pedido.*;
import br.com.ratacheski.bluefood.domain.restaurante.ItemCardapio;
import br.com.ratacheski.bluefood.domain.restaurante.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @ModelAttribute("carrinho")
    public Carrinho carrinho() {
        return new Carrinho();
    }

    @GetMapping(path = "/visualizar")
    public String viewCarrinho() {
        return "cliente-carrinho";
    }

    @GetMapping(path = "/adicionar")
    public String adicionarItem(
            @RequestParam("itemId") Long idItemCardapio,
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam("observacoes") String observacoes,
            @ModelAttribute("carrinho") Carrinho carrinho,
            Model model) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(idItemCardapio).orElseThrow(NoSuchElementException::new);
        try {
            carrinho.adicionarItem(itemCardapio, quantidade, observacoes);
        } catch (RestauranteDiferenteException e) {
            model.addAttribute("msg", "Não é possivel adicionar itens de restaurantes diferentes");
        }
        return "cliente-carrinho";
    }

    @GetMapping(path = "/remover")
    public String removerItem(
            @RequestParam("itemId") Long idItemCardapio,
            @ModelAttribute("carrinho") Carrinho carrinho,
            SessionStatus sessionStatus,
            Model model) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(idItemCardapio).orElseThrow(NoSuchElementException::new);
        carrinho.removerItem(itemCardapio);
        if (carrinho.vazio()) {
            sessionStatus.setComplete();
        }
        return "cliente-carrinho";
    }

    @GetMapping("/refazer-pedido")
    public String refazerCarrinho(
            @RequestParam("pedidoId") Long pedidoId,
            @ModelAttribute("carrinho") Carrinho carrinho,
            Model model) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
        carrinho.limpar();
        for(ItemPedido itemPedido: pedido.getItensPedido()){
            carrinho.adicionarItem(itemPedido);
        }
        return "cliente-carrinho";
    }
}
