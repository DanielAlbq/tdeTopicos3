package com.biblioteca.service;

import com.biblioteca.entity.*;
import com.biblioteca.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import jakarta.transaction.Transactional;
import java.util.Optional;

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
    public Optional<Autor> buscarAutorPorId(Long id) {
        return autorRepository.findById(id);
    }

    @Transactional
    public void salvarAutor(Autor autor) {
        if (autor.getId() == null) {
            autorRepository.save(autor);
        } else {
            autorRepository.update(autor);
        }
    }

    @Transactional
    public void excluirAutor(Long id) {
        autorRepository.deleteById(id);
    }

    public Optional<Livro> buscarLivroPorId(Long id) {
        return livroRepository.findById(id);
    }

    @Transactional
    public void salvarLivro(Livro livro) {
        Autor autor = autorRepository.findById(livro.getAutor().getId())
                .orElseThrow(() -> new IllegalArgumentException("Autor inválido"));
        livro.setAutor(autor);

        if (livro.getId() == null) {
            livroRepository.save(livro);
        } else {
            livroRepository.update(livro);
        }
    }

    @Transactional
    public void excluirLivro(Long id) {
        livroRepository.deleteById(id);
    }
}