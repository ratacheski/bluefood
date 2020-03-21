package br.com.ratacheski.bluefood.domain.restaurante;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoriaRestauranteRepositoryTest {


    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository;

    @Test
    void testInsertAndDelete(){

        assertNotNull(categoriaRestauranteRepository);

        CategoriaRestaurante cr = new CategoriaRestaurante();
        cr.setNome("Chinesa");
        cr.setImagem("chinesa.png");
        categoriaRestauranteRepository.saveAndFlush(cr);

        assertNotNull(cr.getId());

        CategoriaRestaurante cr2 = categoriaRestauranteRepository.findById(cr.getId()).orElseThrow(NoSuchElementException::new);
        assertEquals(cr.getNome(), cr2.getNome());
        assertEquals(categoriaRestauranteRepository.findAll().size(),7);
        categoriaRestauranteRepository.delete(cr);
        assertEquals(categoriaRestauranteRepository.findAll().size(),6);
    }
}