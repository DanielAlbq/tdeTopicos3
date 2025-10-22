package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

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

    public Optional<Emprestimo> findById(Long id) {
        //JOIN FETCH para carregar o livro junto
        var query = entityManager.createQuery(
                "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro WHERE e.id = :id", Emprestimo.class);
        query.setParameter("id", id);
        return query.getResultStream().findFirst();
    }

    public void save(Emprestimo emprestimo) {
        entityManager.persist(emprestimo);
    }


    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
