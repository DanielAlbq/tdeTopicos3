package br.upf.ads175.critiquehub.service;

import br.upf.ads175.critiquehub.entity.model.ItemCultural;
import br.upf.ads175.critiquehub.entity.model.Tag;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TagServiceTest {

    @Inject
    TagService tagService;

    @Inject
    EntityManager em;

    @BeforeEach
    @Transactional
    void setup() {
        // Limpa as tabelas na ordem correta para evitar erros de chave estrangeira
        em.createQuery("DELETE FROM ItemCultural i WHERE i.id IN (SELECT it.id FROM ItemCultural it JOIN it.tags t)").executeUpdate();
        em.createQuery("DELETE FROM Tag").executeUpdate();
        em.createQuery("DELETE FROM ItemCultural").executeUpdate();
    }

    @Test
    @Transactional
    @DisplayName("Deve adicionar uma tag nova a um item cultural")
    void deveAdicionarTagNovaAoItem() {
        ItemCultural item = new ItemCultural();
        item.setTitulo("A Sociedade do Anel");
        em.persist(item);

        String nomeTag = "Fantasia";

        tagService.adicionarTagAoItem(item.getId(), nomeTag);

        em.flush();
        em.clear();

        // Busca o item novamente para verificar o estado atualizado
        ItemCultural itemAtualizado = em.find(ItemCultural.class, item.getId());
        assertNotNull(itemAtualizado.getTags(), "A lista de tags não deveria ser nula.");
        assertEquals(1, itemAtualizado.getTags().size(), "O item deveria ter 1 tag.");
        assertTrue(itemAtualizado.getTags().stream().anyMatch(t -> t.getNome().equals("fantasia")), "A tag 'fantasia' deveria estar associada ao item.");

        // Verifica se a tag foi criada no banco
        long contagemTags = (long) em.createQuery("SELECT count(t) FROM Tag t WHERE t.nome = 'fantasia'").getSingleResult();
        assertEquals(1, contagemTags, "Uma nova tag 'fantasia' deveria ter sido criada.");
    }

    @Test
    @Transactional
    @DisplayName("Deve associar uma tag já existente a um item cultural")
    void deveAdicionarTagExistenteAoItem() {
        // 1. Arrange (Preparação)
        // Cria um item e uma tag previamente
        ItemCultural item = new ItemCultural();
        item.setTitulo("Duna");
        em.persist(item);

        Tag tagExistente = new Tag("Ficção Científica");
        em.persist(tagExistente);

        // Garante que só existe 1 tag no banco antes da operação
        assertEquals(1, (long) em.createQuery("SELECT count(t) FROM Tag t").getSingleResult());

        // 2. Act (Ação)
        tagService.adicionarTagAoItem(item.getId(), "Ficção Científica");

        // 3. Assert (Verificação)
        em.flush();
        em.clear();

        ItemCultural itemAtualizado = em.find(ItemCultural.class, item.getId());
        assertEquals(1, itemAtualizado.getTags().size(), "O item deveria ter 1 tag associada.");

        // A verificação mais importante: NENHUMA tag nova deve ser criada.
        assertEquals(1, (long) em.createQuery("SELECT count(t) FROM Tag t").getSingleResult(), "O número de tags no banco não deveria mudar.");
    }

    @Test
    @Transactional
    @DisplayName("Deve buscar itens culturais por nome de tag")
    void deveBuscarItensPorTag() {
        // 1. Arrange (Preparação)
        // Cria dois itens com a mesma tag e um com uma tag diferente
        ItemCultural item1 = new ItemCultural();
        item1.setTitulo("Blade Runner");
        em.persist(item1);

        ItemCultural item2 = new ItemCultural();
        item2.setTitulo("Neuromancer");
        em.persist(item2);

        ItemCultural item3 = new ItemCultural();
        item3.setTitulo("O Senhor dos Anéis");
        em.persist(item3);

        // Associa as tags
        tagService.adicionarTagAoItem(item1.getId(), "Cyberpunk");
        tagService.adicionarTagAoItem(item2.getId(), "Cyberpunk");
        tagService.adicionarTagAoItem(item3.getId(), "Fantasia");

        // 2. Act (Ação)
        List<ItemCultural> itensEncontrados = tagService.buscarItensPorTag("Cyberpunk");

        // 3. Assert (Verificação)
        assertNotNull(itensEncontrados, "A lista de itens não deveria ser nula.");
        assertEquals(2, itensEncontrados.size(), "Deveria encontrar 2 itens com a tag 'Cyberpunk'.");
        assertTrue(itensEncontrados.stream().anyMatch(i -> i.getTitulo().equals("Blade Runner")), "Blade Runner deveria estar na lista.");
        assertTrue(itensEncontrados.stream().anyMatch(i -> i.getTitulo().equals("Neuromancer")), "Neuromancer deveria estar na lista.");
        assertFalse(itensEncontrados.stream().anyMatch(i -> i.getTitulo().equals("O Senhor dos Anéis")), "O Senhor dos Anéis não deveria estar na lista.");
    }
}