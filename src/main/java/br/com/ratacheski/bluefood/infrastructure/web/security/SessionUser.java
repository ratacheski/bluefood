package br.com.ratacheski.bluefood.infrastructure.web.security;

import br.com.ratacheski.bluefood.domain.cliente.Cliente;
import br.com.ratacheski.bluefood.domain.restaurante.Restaurante;
import br.com.ratacheski.bluefood.domain.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SessionUser implements UserDetails {

    private Usuario usuario;
    private Role role;
    private Collection<? extends GrantedAuthority> userRoles;

    public SessionUser(Usuario usuario) {
        this.usuario = usuario;
        if (usuario instanceof Cliente){
            this.role = Role.CLIENTE;
        } else if (usuario instanceof Restaurante){
            this.role = Role.RESTAURANTE;
        } else {
            throw new IllegalStateException("O tipo de usuário não é válido");
        }
        this.userRoles = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Role getRole() {
        return role;
    }
}
