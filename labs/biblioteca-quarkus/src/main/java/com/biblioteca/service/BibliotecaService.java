package com.biblioteca.service;

import com.biblioteca.entity.*;
import com.biblioteca.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
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

    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.findDisponiveis();
    }

    /**
     * Cria um novo empréstimo e atualiza o status do livro para indisponível.
     */
    @Transactional
    public void realizarEmprestimo(Emprestimo emprestimo) {
        // 1. Busca o livro no banco para garantir que é uma entidade gerenciada
        Livro livroParaEmprestar = livroRepository.findById(emprestimo.getLivro().getId())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        // 2. Regra de negócio: verifica se o livro está realmente disponível
        if (!livroParaEmprestar.isDisponivel()) {
            throw new IllegalStateException("Este livro já está emprestado.");
        }

        // 3. Define as datas do empréstimo
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(14)); // Ex: 14 dias de prazo
        emprestimo.setDataDevolucao(null); // Garante que a data de devolução é nula

        // 4. Atualiza o status do livro
        livroParaEmprestar.setDisponivel(false);

        // 5. Salva o novo empréstimo
        emprestimoRepository.save(emprestimo);
    }

    /**
     * Registra a devolução de um livro, atualizando o empréstimo e o status do livro.
     */
    @Transactional
    public void devolverLivro(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        // Regra de negócio: não devolver um livro que já foi devolvido
        if (emprestimo.getDataDevolucao() != null) {
            throw new IllegalStateException("Este livro já foi devolvido.");
        }

        // Atualiza o empréstimo
        emprestimo.setDataDevolucao(LocalDate.now());

        // Atualiza o status do livro para disponível
        emprestimo.getLivro().setDisponivel(true);
    }
}