package br.com.ratacheski.bluefood.infrastructure.web.controller;

import br.com.ratacheski.bluefood.domain.restaurante.CategoriaRestaurante;
import br.com.ratacheski.bluefood.domain.restaurante.CategoriaRestauranteRepository;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import br.com.ratacheski.bluefood.domain.restaurante.RestauranteRepository;
import br.com.ratacheski.bluefood.util.SecurityUtils;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.List;
import java.util.NoSuchElementException;

public class ControllerHelper {

    public static void setEditMode(Model model, boolean isEdit) {
        model.addAttribute("editMode", isEdit);
    }

    public static void addCategoriasToRequest(CategoriaRestauranteRepository repository, Model model) {
        List<CategoriaRestaurante> categorias = repository.findAll(Sort.by("nome"));
        model.addAttribute("categorias", categorias);
    }

    public static void addRestauranteToRequest(RestauranteRepository repository, Model model) {
        Long idRestaurante = SecurityUtils.sessionRestaurante().getId();
        Restaurante restaurante = repository.findById(idRestaurante).orElseThrow(NoSuchElementException::new);
        model.addAttribute("restaurante", restaurante);
    }
}
