package br.upf.ads175.service;

import br.upf.ads175.dto.CategoriaDTO;
import br.upf.ads175.dto.ProdutoDTO;
import br.upf.ads175.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProdutoService - Testes Unitários com Mocks")
class ProdutoServiceTest {

    @Mock
    ProdutoRepository repository;

    @InjectMocks
    ProdutoService service;

    private List<ProdutoDTO> produtosMock;

    @BeforeEach
    void setUp() {
        // Dados de teste que usaremos nos mocks
        produtosMock = List.of(
            new ProdutoDTO(1L, "Produto A", new BigDecimal("500.00"), true, new CategoriaDTO("Cat1")),
            new ProdutoDTO(2L, "Produto B", new BigDecimal("1500.00"), true, new CategoriaDTO("Cat2")),
            new ProdutoDTO(3L, "Produto C", new BigDecimal("300.00"), false, new CategoriaDTO("Cat1"))
        );
    }

    @Test
@DisplayName("Deve retornar produtos ativos ordenados por nome")
void deveRetornarProdutosAtivosOrdenadosPorNome() {
    // Given - Mock configurado para retornar produtos ativos
    when(repository.findByAtivo(true)).thenReturn(
        produtosMock.stream()
            .filter(ProdutoDTO::ativo)
            .toList()
    );

    // When - Executamos o método do serviço
    List<ProdutoDTO> resultado = service.buscarProdutosAtivosOrdenadosPorNome();

    // Then - Verificamos o resultado e as interações
    assertFalse(resultado.isEmpty(), "Lista não deve estar vazia");
    assertTrue(resultado.stream().allMatch(ProdutoDTO::ativo),
               "Todos devem estar ativos");

    // Verifica se está ordenado por nome
    assertEquals("Produto A", resultado.get(0).nome());
    assertEquals("Produto B", resultado.get(1).nome());

    // Verifica se o repositório foi chamado corretamente
    verify(repository, times(1)).findByAtivo(true);
}
}