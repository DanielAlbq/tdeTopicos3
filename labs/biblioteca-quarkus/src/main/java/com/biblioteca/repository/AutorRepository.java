package com.biblioteca.repository;

import com.biblioteca.entity.Autor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class AutorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Autor> findAll() {
        return entityManager.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(a) FROM Autor a", Long.class).getSingleResult();
    }

    public Optional<Autor> findById(Long id) {
        Autor autor = entityManager.find(Autor.class, id);
        return Optional.ofNullable(autor);
    }

    public void save(Autor autor) {
        entityManager.persist(autor);
    }

    public Autor update(Autor autor) {
        return entityManager.merge(autor);
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}

