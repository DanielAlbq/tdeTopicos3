// src/main/java/com/biblioteca/service/UsuarioService.java
package com.biblioteca.service;

import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil; // Utilitário para hash de senhas
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String username, String password) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(username);
        // hash com Bcrypt util
        novoUsuario.setPassword(BcryptUtil.bcryptHash(password));
        novoUsuario.setRole("USER"); // permissao padro
        usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario atualizarUsuario(Usuario usuario) {
        // Busca o usuário original no banco
        Usuario u = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if ("USER".equals(usuario.getRole()) && "ADMIN".equals(u.getRole())) {
            long adminCount = usuarioRepository.countAdmins();
            if (adminCount <= 1) {
                throw new IllegalStateException("Não é possível ficar sem administrador.");
            }
        }
        u.setUsername(usuario.getUsername());
        u.setRole(usuario.getRole());

        // O merge não é necessário aqui, pois a entidade 'u' já está gerenciada pelo JPA
        return u;
    }
}