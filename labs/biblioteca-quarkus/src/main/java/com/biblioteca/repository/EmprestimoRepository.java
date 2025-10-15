package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class EmprestimoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Emprestimo> FindAll() {
        return entityManager.createQuery("SELECT e FROM Emprestimo e LEFT JOIN e.livro LEFT JOIN FETCH e.livro.autor", Emprestimo.class).getResultList();
    }

    public List<Emprestimo> findAtivos() {
        return entityManager.createQuery("SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro WHERE e.dataDevolucao IS NULL", Emprestimo.class).getResultList();
    }

    public long count(){
        return entityManager.createQuery("SELECT COUNT(e) FROM Emprestimo e", Long.class).getSingleResult();
    }

    public Long countAtivos() {
        return entityManager.createQuery("SELECT COUNT(e) FROM Emprestimo e WHERE e.dataDevolucao IS NULL", Long.class).getSingleResult();
    }

}
