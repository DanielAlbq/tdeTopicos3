// src/main/java/com/biblioteca/repository/UsuarioRepository.java
package com.biblioteca.repository;

import com.biblioteca.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class UsuarioRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Usuario usuario) {
        entityManager.persist(usuario);
    }
}