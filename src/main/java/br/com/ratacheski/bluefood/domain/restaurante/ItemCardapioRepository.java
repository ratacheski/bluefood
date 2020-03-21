package br.com.ratacheski.bluefood.domain.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {

    @Query("select distinct " +
            "ic.categoria from ItemCardapio ic " +
            "where ic.restaurante.id = :idRestaurante " +
            "order by ic.categoria")
    List<String> findCategorias(@Param("idRestaurante") Long idRestaurante);

    List<ItemCardapio> findByRestaurante_IdAndDestaqueOrderByNome(Long idRestaurante, boolean destaque);

    List<ItemCardapio> findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(Long idRestaurante, boolean destaque, String categoria);

    List<ItemCardapio> findByRestaurante_IdOrderByNome(Long idRestaurante);
}
