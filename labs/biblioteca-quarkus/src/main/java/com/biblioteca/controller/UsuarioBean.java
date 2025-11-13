package com.biblioteca.controller;

import com.biblioteca.entity.Usuario;
import com.biblioteca.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.RowEditEvent; // Importante: adicione a dependência do PrimeFaces se não tiver

import java.io.Serializable;
import java.util.List;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    private List<Usuario> usuarios;
    private List<String> roles;

    @PostConstruct
    public void init() {
        usuarios = usuarioService.listarTodosUsuarios();
        roles = List.of("ADMIN", "USER");
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void onRowEdit(RowEditEvent<Usuario> event) {
        try {
            Usuario usuarioEditado = event.getObject();
            usuarioService.atualizarUsuario(usuarioEditado);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário '" + usuarioEditado.getUsername() + "' atualizado."));
        } catch (Exception e) {
            usuarios = usuarioService.listarTodosUsuarios();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    public void onRowEditCancel(RowEditEvent<Usuario> event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Cancelado", "Edição do usuário '" + event.getObject().getUsername() + "' cancelada."));
    }
}