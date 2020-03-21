package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.application.service.RelatorioService;
import br.com.ratacheski.bluefood.application.service.RestauranteService;
import br.com.ratacheski.bluefood.application.service.ValidationException;
import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.pedido.*;
import br.com.ratacheski.bluefood.domain.restaurante.*;
import br.com.ratacheski.bluefood.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository;
    @Autowired
    private RestauranteService restauranteService;
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping(path = "/home")
    public String home(Model model) {
        List<Pedido> pedidos = pedidoRepository.findByRestaurante_IdOrderByDataDesc(SecurityUtils.sessionRestaurante().getId());
        model.addAttribute("pedidos", pedidos);
        return "restaurante-home";
    }

    @GetMapping(path = "/edit")
    public String edit(Model model) {
        ControllerHelper.addRestauranteToRequest(restauranteRepository, model);
        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
        return "restaurante-cadastro";
    }

    @PostMapping(path = "/save")
    public String save(@ModelAttribute("restaurante") @Valid Restaurante restaurante,
                       Errors errors,
                       Model model) {
        if (!errors.hasErrors()) {
            try {
                restauranteService.saveRestaurante(restaurante);
                model.addAttribute("msg", "Restaurante salvo com Sucesso");
                ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
            } catch (ValidationException e) {
                errors.rejectValue("email", null, e.getMessage());
            }
        }
        ControllerHelper.setEditMode(model, true);
        return "restaurante-cadastro";
    }

    @GetMapping(path = "/comidas")
    public String viewComidas(Model model) {
        ControllerHelper.addRestauranteToRequest(restauranteRepository, model);
        model.addAttribute("itemCardapio", new ItemCardapio());
        model.addAttribute("itensCardapio",
                itemCardapioRepository.findByRestaurante_IdOrderByNome(SecurityUtils.sessionRestaurante().getId()));
        return "restaurante-comidas";
    }

    @GetMapping(path = "/comidas/remover")
    public String removerComida(@RequestParam("itemId") Long itemId, Model model) {
        itemCardapioRepository.deleteById(itemId);
        return "redirect:/restaurante/comidas";
    }

    @PostMapping(path = "/comidas/cadastrar")
    public String cadastrarItemCardapio(@Valid @ModelAttribute("itemCardapio") ItemCardapio itemCardapio,
                                        Errors errors,
                                        Model model) {
        if (errors.hasErrors()) {
            ControllerHelper.addRestauranteToRequest(restauranteRepository, model);
            model.addAttribute("itensCardapio",
                    itemCardapioRepository.findByRestaurante_IdOrderByNome(SecurityUtils.sessionRestaurante().getId()));
            return "restaurante-comidas";
        }
        restauranteService.saveItemCardapio(itemCardapio);
        return "redirect:/restaurante/comidas";
    }

    @GetMapping(path = "/pedido")
    public String viewPedido(@RequestParam("pedidoId") Long pedidoId,
                             Model model) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("pedido", pedido);
        return "restaurante-pedido";
    }

    @PostMapping(path = "/pedido/proximoStatus")
    public String proximoStatus(@RequestParam("pedidoId") Long pedidoId,
                                Model model) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(NoSuchElementException::new);
        pedido.progredirStatus();
        pedidoRepository.save(pedido);
        model.addAttribute("pedido", pedido);
        model.addAttribute("msg", "Status alterado com sucesso");
        return "restaurante-pedido";
    }

    @GetMapping(path = "/relatorio/pedidos")
    public String relatorioPedidos(@ModelAttribute("relatorioPedidoFilter") RelatorioPedidoFilter filter,
                                   Model model) {
        List<Pedido> pedidos = relatorioService.listPedidos(SecurityUtils.sessionRestaurante().getId(), filter);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("relatorioPedidoFilter", filter);
        return "restaurante-relatorio-pedidos";
    }

    @GetMapping(path = "/relatorio/itens")
    public String relatorioItensPedido(@ModelAttribute("relatorioItemFilter") RelatorioItemFilter filter,
                                       Model model) {
        Long restauranteId = SecurityUtils.sessionRestaurante().getId();
        List<ItemCardapio> itensCardapio = itemCardapioRepository.findByRestaurante_IdOrderByNome(restauranteId);
        model.addAttribute("itensCardapio", itensCardapio);
        List<RelatorioItemFaturamento> itensFaturamento = relatorioService.calcularFaturamentoItens(restauranteId, filter);
        model.addAttribute("itensFaturamento", itensFaturamento);
        model.addAttribute("relatorioItemFilter", filter);
        return "restaurante-relatorio-itens";
    }
}
