package br.com.ratacheski.bluefood.util;

import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import br.com.ratacheski.bluefood.infrastructure.web.security.SessionUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static SessionUser sessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        return (SessionUser) authentication.getPrincipal();
    }

    public static Cliente sessionCliente() {
        SessionUser sessionUser = sessionUser();
        if (sessionUser == null){
            throw new IllegalStateException("Não existe um Usuário Logado.");
        }
        if(!(sessionUser.getUsuario() instanceof Cliente)){
            throw new IllegalStateException("O usuário logado não é um cliente.");
        }
        return (Cliente) sessionUser.getUsuario();
    }

    public static Restaurante sessionRestaurante() {
        SessionUser sessionUser = sessionUser();
        if (sessionUser == null){
            throw new IllegalStateException("Não existe um Usuário Logado.");
        }
        if(!(sessionUser.getUsuario() instanceof Restaurante)){
            throw new IllegalStateException("O usuário logado não é um restaurante.");
        }
        return (Restaurante) sessionUser.getUsuario();
    }
}
