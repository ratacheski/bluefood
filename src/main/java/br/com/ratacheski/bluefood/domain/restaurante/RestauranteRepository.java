package br.com.ratacheski.bluefood.domain.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Restaurante findByEmail(String email);

    List<Restaurante> findByNomeIgnoreCaseContaining(String nome);

    List<Restaurante> findByCategorias_Id(Long idCategoria);
}
