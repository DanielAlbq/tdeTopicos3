// src/main/java/com/biblioteca/repository/UsuarioRepository.java
package com.biblioteca.repository;

import com.biblioteca.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Usuario usuario) {
        entityManager.persist(usuario);
    }
    public List<Usuario> findAll() {
        return entityManager.createQuery("SELECT u FROM Usuario u ORDER BY u.username", Usuario.class)
                .getResultList();
    }

    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Usuario.class, id));
    }

    public long countAdmins() {
        return entityManager.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.role = 'ADMIN'", Long.class)
                .getSingleResult();
    }
}