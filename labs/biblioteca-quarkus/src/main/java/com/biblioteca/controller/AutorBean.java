package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.service.BibliotecaService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("autorBean")
@ViewScoped
public class AutorBean implements Serializable {

    @Inject
    private BibliotecaService bibliotecaService;

    private Autor autor = new Autor();
    private Long autorId; // Usado para carregar o autor para edição

    public void carregar() {
        if (autorId != null) {
            autor = bibliotecaService.buscarAutorPorId(autorId).orElse(new Autor());
        }
    }

    public String salvar() {
        try {
            bibliotecaService.salvarAutor(autor);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Autor salvo."));
            // Redireciona para a página principal após salvar
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível salvar o autor."));
            return null;
        }
    }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }
    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
}