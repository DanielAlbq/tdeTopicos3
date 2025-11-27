package com.biblioteca.repository;

import com.biblioteca.entity.AcessoUsuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AcessoUsuarioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRES_NEW) // Roda em uma transação separada para garantir o log mesmo se o login falhar
    public void registrarAcesso(String username, String resultado, String ipAddress) {
        AcessoUsuario acesso = new AcessoUsuario(username, resultado, ipAddress);
        entityManager.persist(acesso);
    }
}