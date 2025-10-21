package com.biblioteca.service;

import com.biblioteca.entity.*;
import com.biblioteca.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BibliotecaService {

    @Inject
    private AutorRepository autorRepository;

    @Inject
    private LivroRepository livroRepository;

    @Inject
    private EmprestimoRepository emprestimoRepository;

    // Métodos de Listagem
    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findAtivos();
    }

    // Métodos de Contagem para Estatísticas
    public long contarTotalLivros() {
        return livroRepository.count();
    }

    public long contarLivrosDisponiveis() {
        return livroRepository.countByDisponivel(true);
    }

    public long contarEmprestimosAtivos() {
        return emprestimoRepository.countAtivos();
    }

    public long contarTotalAutores() {
        return autorRepository.count();
    }
}