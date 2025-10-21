package com.biblioteca.controller;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("bibliotecaBean")
@ViewScoped
public class BibliotecaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(BibliotecaBean.class.getName());

    @Inject
    private BibliotecaService bibliotecaService;

    // Listas para exibição
    private List<Autor> autores;
    private List<Livro> livros;
    private List<Emprestimo> emprestimosAtivos;

    // Variáveis para estatísticas
    private long totalLivros;
    private long livrosDisponiveis;
    private long emprestimosAtivosCount;
    private long totalAutores;

    @PostConstruct
    public void init() {
        carregarDados();
        carregarEstatisticas();
    }

    public void carregarDados() {
        try {
            autores = bibliotecaService.listarTodosAutores();
            livros = bibliotecaService.listarTodosLivros();
            emprestimosAtivos = bibliotecaService.listarEmprestimosAtivos();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar dados da biblioteca", e);
        }
    }

    public void carregarEstatisticas() {
        try {
            totalLivros = bibliotecaService.contarTotalLivros();
            livrosDisponiveis = bibliotecaService.contarLivrosDisponiveis();
            emprestimosAtivosCount = bibliotecaService.contarEmprestimosAtivos();
            totalAutores = bibliotecaService.contarTotalAutores();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar estatísticas", e);
        }
    }

    public void recarregarDados() {
        init();
    }

    // Getters
    public List<Autor> getAutores() {
        return autores;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Emprestimo> getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public long getTotalLivros() {
        return totalLivros;
    }

    public long getLivrosDisponiveis() {
        return livrosDisponiveis;
    }

    public long getEmprestimosAtivosCount() {
        return emprestimosAtivosCount;
    }

    public long getTotalAutores() {
        return totalAutores;
    }
}