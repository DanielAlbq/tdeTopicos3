// src/main/java/com/biblioteca/service/UsuarioService.java
package com.biblioteca.service;

import com.biblioteca.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil; // Utilitário para hash de senhas
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioService {
    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(String username, String password) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsername(username);
        // Cria um hash seguro da senha antes de salvar
        novoUsuario.setPassword(BcryptUtil.bcryptHash(password));
        novoUsuario.setRole("USER"); // Define uma permissão padrão
        usuarioRepository.save(novoUsuario);
    }
}