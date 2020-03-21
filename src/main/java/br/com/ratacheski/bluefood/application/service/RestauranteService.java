package br.com.ratacheski.bluefood.application.service;

import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.cliente.ClienteRepository;
import br.com.ratacheski.bluefood.domain.restaurante.*;
import br.com.ratacheski.bluefood.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ImageService imageService;
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Transactional
    public void saveRestaurante(Restaurante restaurante) throws ValidationException {
        if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
            throw new ValidationException("Email já está cadastrado!");
        }
        if (restaurante.getId() != null) {
            Restaurante restauranteDB = restauranteRepository.findById(restaurante.getId()).orElseThrow(NoSuchElementException::new);
            restaurante.setSenha(restauranteDB.getSenha());
            restaurante.setLogotipo(restauranteDB.getLogotipo());
            restauranteRepository.save(restaurante);
        } else {
            restaurante.encryptPassword();
            restaurante = restauranteRepository.save(restaurante);
            restaurante.setLogotipoFileName();
            imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
        }
    }

    private boolean validateEmail(String email, Long idRestaurante) {
        Restaurante restaurante = restauranteRepository.findByEmail(email);
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente != null) {
            return false;
        }
        if (restaurante != null) {
            if (idRestaurante == null) {
                return false;
            } else if (!restaurante.getId().equals(idRestaurante)) {
                return false;
            }
        }
        return true;
    }

    public List<Restaurante> search(SearchFilter filter) {
        List<Restaurante> restaurantes;
        if (filter.getSearchType().equals(SearchFilter.SearchType.TEXTO)) {
            restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(filter.getTexto());
        } else if (filter.getSearchType().equals(SearchFilter.SearchType.CATEGORIA)) {
            restaurantes = restauranteRepository.findByCategorias_Id(filter.getIdCategoria());
        } else {
            throw new IllegalStateException("O tipo de busca " + filter.getSearchType() + " não é suportado.");
        }
        Iterator<Restaurante> iterator = restaurantes.iterator();
        while (iterator.hasNext()) {
            Restaurante restaurante = iterator.next();
            double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();
            if (filter.isEntregaGratis() && taxaEntrega > 0) {
                iterator.remove();
            }
        }
        RestauranteComparator comparator = new RestauranteComparator(filter, SecurityUtils.sessionCliente().getCep());
        restaurantes.sort(comparator);
        return restaurantes;
    }

    @Transactional
    public void saveItemCardapio(ItemCardapio itemCardapio){
        itemCardapio = itemCardapioRepository.save(itemCardapio);
        itemCardapio.setImagemFileName();
        imageService.uploadComida(itemCardapio.getImagemFile(),itemCardapio.getImagem());
    }

}
