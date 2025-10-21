package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("livroBean")
@ViewScoped
public class LivroBean implements Serializable {

    @Inject
    private BibliotecaService bibliotecaService;

    private Livro livro = new Livro();
    private Long livroId;
    private List<Autor> autoresDisponiveis; // Lista para o dropdown

    @PostConstruct
    public void init() {
        // Carrega todos os autores para popular o dropdown
        autoresDisponiveis = bibliotecaService.listarTodosAutores();
    }

    public void carregar() {
        if (livroId != null) {
            livro = bibliotecaService.buscarLivroPorId(livroId).orElse(new Livro());
        }
    }

    public String salvar() {
        System.out.println("Salvando Livro... Autores na lista: " + (autoresDisponiveis != null ? autoresDisponiveis.size() : "NULL"));
        try {
            bibliotecaService.salvarLivro(livro);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Livro salvo."));
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível salvar o livro: " + e.getMessage()));
            return null;
        }
    }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }
    public List<Autor> getAutoresDisponiveis() { return autoresDisponiveis; }
}