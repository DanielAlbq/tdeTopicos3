package br.upf.ads175.resource;

import br.upf.ads175.dto.CategoriaDTO;
import br.upf.ads175.dto.ProdutoDTO;
import br.upf.ads175.service.ProdutoService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("ProdutoResource - Testes de Integração REST")
class ProdutoResourceTest {

    @InjectMock
    ProdutoService produtoService;

    private List<ProdutoDTO> produtosMock = List.of(
        new ProdutoDTO(1L, "Notebook Gamer", new BigDecimal("8500.00"), true, new CategoriaDTO("Eletrônicos")),
        new ProdutoDTO(2L, "Cadeira de Escritório", new BigDecimal("1200.50"), true, new CategoriaDTO("Móveis"))
    );

    @Test
@DisplayName("GET /produtos deve retornar lista de produtos ativos")
void deveRetornarListaProdutosAtivos() {
    // Given - Mock configurado para retornar produtos
    when(produtoService.buscarProdutosAtivosOrdenadosPorNome())
        .thenReturn(produtosMock);

    // When & Then - Fazemos requisição HTTP e verificamos resposta
    given()
        .when()
            .get("/produtos")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .body("$", hasSize(2))
            .body("[0].id", equalTo(1))
            .body("[0].nome", equalTo("Notebook Gamer"))
            .body("[1].id", equalTo(2));

    // Verifica que o serviço foi chamado
    verify(produtoService, times(1)).buscarProdutosAtivosOrdenadosPorNome();
}
}

