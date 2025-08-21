package br.upf.ads175.repository;

import br.upf.ads175.dto.ProdutoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProdutoRepository - Testes Unitários")
class ProdutoRepositoryTest {

    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepository();
    }
}