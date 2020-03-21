package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.application.service.ClienteService;
import br.com.ratacheski.bluefood.application.service.RestauranteService;
import br.com.ratacheski.bluefood.application.service.ValidationException;
import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.cliente.ClienteRepository;
import br.com.ratacheski.bluefood.domain.pedido.Pedido;
import br.com.ratacheski.bluefood.domain.pedido.PedidoRepository;
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
@RequestMapping(path = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private RestauranteService restauranteService;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping(path = "/home")
    public String home(Model model) {
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
        model.addAttribute("searchFilter", new SearchFilter());
        List<Pedido> pedidos = pedidoRepository.findByCliente_IdOrderByDataDesc(SecurityUtils.sessionCliente().getId());
        model.addAttribute("pedidos", pedidos);
        return "cliente-home";
    }

    @GetMapping(path = "/edit")
    public String edit(Model model) {
        Long idCliente = SecurityUtils.sessionCliente().getId();
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(NoSuchElementException::new);
        model.addAttribute("cliente", cliente);
        ControllerHelper.setEditMode(model, true);
        return "cliente-cadastro";
    }

    @PostMapping(path = "/save")
    public String save(@ModelAttribute("cliente") @Valid Cliente cliente,
                       Errors errors,
                       Model model) {
        if (!errors.hasErrors()) {
            try {
                clienteService.saveCliente(cliente);
                model.addAttribute("msg", "Cliente salvo com Sucesso");
            } catch (ValidationException e) {
                errors.rejectValue("email", null, e.getMessage());
            }
        }
        ControllerHelper.setEditMode(model, true);
        return "cliente-cadastro";
    }

    @GetMapping(path = "/search")
    public String search(@ModelAttribute("searchFilter") SearchFilter filter,
                         Model model,
                         @RequestParam(value = "cmd", required = false) String command) {
        filter.processFilter(command);
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
        List<Restaurante> restaurantes = restauranteService.search(filter);
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("cep", SecurityUtils.sessionCliente().getCep());
        model.addAttribute("searchFilter", filter);
        return "cliente-busca";
    }

    @GetMapping("/restaurante")
    public String viewRestaurante(@RequestParam("restauranteId") Long restauranteId,
                                  @RequestParam(value = "categoria", required = false) String categoria,
                                  Model model) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("cep", SecurityUtils.sessionCliente().getCep());

        List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);
        model.addAttribute("categorias", categorias);

        List<ItemCardapio> itens;
        List<ItemCardapio> itensDestaque;
        if (categoria == null) {
            itens = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);
            itensDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
        } else {
            itens = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);
            itensDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
        }
        model.addAttribute("itens", itens);
        model.addAttribute("itensDestaque", itensDestaque);
        model.addAttribute("categoriaSelecionada", categoria);

        return "cliente-restaurante";
    }

}
