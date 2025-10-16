package br.upf.ads175.critiquehub.service;

import br.upf.ads175.critiquehub.entity.Filme;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class FilmeService {

    @Inject
    private EntityManager em;

    public List<Filme> ListarTodos(){
        return em.createNamedQuery("Filme.listarTodos").getResultList();
    }
}
