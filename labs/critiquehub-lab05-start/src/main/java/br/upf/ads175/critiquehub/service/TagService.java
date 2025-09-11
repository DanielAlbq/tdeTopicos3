package br.upf.ads175.critiquehub.service;

import br.upf.ads175.critiquehub.entity.model.ItemCultural;
import br.upf.ads175.critiquehub.entity.model.Tag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TagService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void adicionarTagAoItem(Long itemId, String nomeTag) {
        ItemCultural item = entityManager.find(ItemCultural.class, itemId);

        // Buscar tag existente ou criar nova
        Tag tag = buscarOuCriarTag(nomeTag);

        item.getTags().add(tag);
        tag.getItens().add(item);
    }

    private Tag buscarOuCriarTag(String nome) {
        List<Tag> tags = entityManager.createQuery(
                        "SELECT t FROM Tag t WHERE t.nome = :nome", Tag.class)
                .setParameter("nome", nome.toLowerCase().trim())
                .getResultList();

        if (!tags.isEmpty()) {
            return tags.get(0);
        }

        Tag novaTag = new Tag(nome);
        entityManager.persist(novaTag);
        return novaTag;
    }

    public List<ItemCultural> buscarItensPorTag(String nomeTag) {
        return entityManager.createQuery(
                        "SELECT DISTINCT i FROM ItemCultural i " +
                                "JOIN i.tags t " +
                                "WHERE t.nome = :nome", ItemCultural.class)
                .setParameter("nome", nomeTag.toLowerCase().trim())
                .getResultList();
    }
}
