package com.biblioteca.controller;

import com.biblioteca.entity.Emprestimo;
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

@Named("emprestimoBean")
@ViewScoped
public class EmprestimoBean implements Serializable {

    @Inject
    private BibliotecaService bibliotecaService;

    private Emprestimo emprestimo = new Emprestimo();
    private List<Livro> livrosDisponiveis; // Para o dropdown

    @PostConstruct
    public void init() {
        livrosDisponiveis = bibliotecaService.listarLivrosDisponiveis();
    }

    public String salvar() {
        try {
            bibliotecaService.realizarEmprestimo(emprestimo);
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Empréstimo realizado.");
            return "/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro!", e.getMessage());
            return null;
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Getters e Setters
    public Emprestimo getEmprestimo() { return emprestimo; }
    public void setEmprestimo(Emprestimo emprestimo) { this.emprestimo = emprestimo; }
    public List<Livro> getLivrosDisponiveis() { return livrosDisponiveis; }
}