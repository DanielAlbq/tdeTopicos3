// src/main/java/com/biblioteca/controller/RegistroBean.java
package com.biblioteca.controller;

import com.biblioteca.service.UsuarioService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("registroBean")
@ViewScoped
public class RegistroBean implements Serializable {

    @Inject
    UsuarioService usuarioService;

    private String username;
    private String password;

    public String registrar() {
        try {
            usuarioService.registrar(username, password);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Conta criada. Faça o login."));
            // Redireciona para o login após o sucesso
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Usuário já existe."));
            return null;
        }
    }

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}