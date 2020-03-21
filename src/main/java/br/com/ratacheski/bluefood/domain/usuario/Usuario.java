package br.com.ratacheski.bluefood.domain.usuario;

import br.com.ratacheski.bluefood.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Nome não pode estar vazio.")
    @Size(max=80, message = "O Nome deve possuir no máximo 80 letras.")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O email não pode estar vazio.")
    @Size(max=60, message = "O email deve possuir no máximo 60 caracteres.")
    @Email(message = "O email não é um email válido.")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "A senha não pode estar vazia.")
    @Size(max = 80)
    @Column(nullable = false)
    private String senha;

    @NotBlank(message = "O telefone não pode estar vazio.")
    @Pattern(regexp = "[0-9]{10,11}", message = "O telefone possui formato inválido")
    @Column(nullable = false, length = 11)
    private String telefone;

    public void encryptPassword(){
        this.senha = StringUtils.encrypt(this.senha);
    }
}
