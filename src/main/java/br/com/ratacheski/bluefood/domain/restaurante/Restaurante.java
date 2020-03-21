package br.com.ratacheski.bluefood.domain.restaurante;

import br.com.ratacheski.bluefood.domain.usuario.Usuario;
import br.com.ratacheski.bluefood.infrastructure.web.validator.UploadConstraint;
import br.com.ratacheski.bluefood.util.FileType;
import br.com.ratacheski.bluefood.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name = "bf_restaurante", schema = "bf")
public class Restaurante extends Usuario {

    @NotBlank(message = "O CNPJ não pode estar vazio.")
    @Pattern(regexp = "[0-9]{14}", message = "O CNPJ possui formato inválido")
    @Column(nullable = false, length = 14)
    private String cnpj;

    @Size(max = 80)
    private String logotipo;

    @Transient
    @UploadConstraint(acceptedTypes = {FileType.JPG, FileType.PNG}, message = "Arquivo do logotipo com formato inválido!")
    private MultipartFile logotipoFile;

    @NotNull(message = "Taxa de Entrega não pode ser vazia.")
    @Min(0)
    @Max(99)
    @Column(nullable = false)
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de Entrega não pode ser vazio.")
    @Min(0)
    @Max(120)
    @Column(nullable = false)
    private Integer tempoEntregaBase;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "bf_restaurante_categoria_restaurante",
            schema = "bf",
            joinColumns = @JoinColumn(name = "id_restaurante"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria_restaurante"))
    @Size(min = 1, message = "Selecione pelo menos uma categoria!")
    @ToString.Exclude
    private Set<CategoriaRestaurante> categorias = new HashSet<>(0);

    @OneToMany(mappedBy = "restaurante")
    @ToString.Exclude
    private Set<ItemCardapio> itensCardapio = new HashSet<>(0);

    public void setLogotipoFileName() {
        if (getId() == null) {
            throw new IllegalStateException("É preciso primeiro gravar o registro");
        }
        this.logotipo = String.format("%08d-logo.%s", getId(), FileType.of(logotipoFile.getContentType()).getExtension());
    }

    public String getCategoriasAsText() {
        Set<String> categoriasText = new LinkedHashSet<>();
        categorias.forEach(categoriaRestaurante -> {
            categoriasText.add(categoriaRestaurante.getNome());
        });
        return StringUtils.concatenate(categoriasText, ",");
    }

    public Integer calculateTempoEntrega(String cep){
        int soma = 0;
        for(char c : cep.toCharArray()){
            int value = Character.getNumericValue(c);
            if(value > 0)
                soma+=value;
        }
        soma /= 2;
        return soma + tempoEntregaBase;
    }

}
