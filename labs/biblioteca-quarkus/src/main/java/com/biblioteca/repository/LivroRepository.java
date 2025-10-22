package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LivroRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Livro> findAll() {
        //Left join para buscar o autor na mesma consulta
        return entityManager.createQuery("SELECT l FROM Livro l LEFT JOIN FETCH l.autor", Livro.class).getResultList();
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM Livro l", Long.class).getSingleResult();
    }

    public long countByDisponivel(boolean disponivel) {
        return entityManager.createQuery("SELECT COUNT(l) FROM Livro l WHERE l.disponivel = :disponivel", Long.class)
                .setParameter("disponivel", disponivel).getSingleResult();
    }

    public Optional<Livro> findById(Long id) {
        List<Livro> result = entityManager.createQuery(
                        "SELECT l FROM Livro l LEFT JOIN FETCH l.autor WHERE l.id = :id", Livro.class)
                .setParameter("id", id)
                .getResultList();
        return result.stream().findFirst();
    }

    public void save(Livro livro) {
        entityManager.persist(livro);
    }

    public Livro update(Livro livro) {
        return entityManager.merge(livro);
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    public List<Livro> findDisponiveis() {
        return entityManager.createQuery(
                        "SELECT l FROM Livro l LEFT JOIN FETCH l.autor WHERE l.disponivel = true ORDER BY l.titulo", Livro.class)
                .getResultList();
    }
}
