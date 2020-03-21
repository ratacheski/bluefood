package br.com.ratacheski.bluefood.application.service;

import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.cliente.ClienteRepository;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import br.com.ratacheski.bluefood.domain.restaurante.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Transactional
    public void saveCliente(Cliente cliente) throws ValidationException {
        if (!validateEmail(cliente.getEmail(), cliente.getId())) {
            throw new ValidationException("Email já está cadastrado!");
        }
        if(cliente.getId() != null){
            Cliente clienteDB = clienteRepository.findById(cliente.getId()).orElseThrow(NoSuchElementException::new);
            cliente.setSenha(clienteDB.getSenha());
        }else {
            cliente.encryptPassword();
        }
        clienteRepository.save(cliente);
    }


    private boolean validateEmail(String email, Long idCliente) {
        Cliente cliente = clienteRepository.findByEmail(email);
        Restaurante restaurante = restauranteRepository.findByEmail(email);
        if (restaurante != null){
            return false;
        }
        if (cliente != null) {
            if (cliente.getId() == null) {
                return false;
            } else if (!cliente.getId().equals(idCliente)) {
                return false;
            }
        }
        return true;
    }
}
