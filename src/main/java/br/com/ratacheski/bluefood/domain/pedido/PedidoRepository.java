package br.com.ratacheski.bluefood.domain.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente_IdOrderByDataDesc(Long clienteId);

    List<Pedido> findByRestaurante_IdOrderByDataDesc(Long restauranteId);

    Pedido findByIdAndRestaurante_Id(Long id, Long restauranteId);

    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.data BETWEEN :dataInicial AND :dataFinal")
    List<Pedido> findByDateInterval(@Param("restauranteId") Long restauranteId,
                                    @Param("dataInicial") LocalDateTime dataInicial,
                                    @Param("dataFinal") LocalDateTime dataFinal);

    @Query("SELECT i.itemCardapio.nome, SUM(i.quantidade), SUM(i.preco * i.quantidade) FROM " +
            "Pedido p INNER JOIN p.itensPedido i " +
            "WHERE p.restaurante.id = :restauranteId AND i.itemCardapio.id = :itemId " +
            "AND p.data BETWEEN :dataInicial AND :dataFinal " +
            "GROUP BY i.itemCardapio.nome")
    List<Object[]> findItensForFaturamento(@Param("restauranteId") Long restauranteId,
                                           @Param("itemId") Long itemId,
                                           @Param("dataInicial") LocalDateTime dataInicial,
                                           @Param("dataFinal") LocalDateTime dataFinal);

    @Query("SELECT i.itemCardapio.nome, SUM(i.quantidade), SUM(i.preco * i.quantidade) FROM " +
            "Pedido p INNER JOIN p.itensPedido i " +
            "WHERE p.restaurante.id = :restauranteId AND " +
            "p.data BETWEEN :dataInicial AND :dataFinal " +
            "GROUP BY i.itemCardapio.nome")
    List<Object[]> findItensForFaturamento(@Param("restauranteId") Long restauranteId,
                                           @Param("dataInicial") LocalDateTime dataInicial,
                                           @Param("dataFinal") LocalDateTime dataFinal);
}
