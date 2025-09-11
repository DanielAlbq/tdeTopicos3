package com.critiquehub.entity.model;

import com.critiquehub.entity.enums.StatusUsuario;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

/**
 * Entidade principal representando um usuário da plataforma CritiqueHub.
 *
 * Demonstra conceitos fundamentais de JPA:
 * - Mapeamento básico de entidade
 * - Uso de enumerações
 * - Named Queries essenciais
 * - Métodos de negócio simples
 */
@Entity
@Table(name = "usuarios")
@NamedQueries({
    @NamedQuery(
        name = "Usuario.buscarPorEmail",
        query = "SELECT u FROM Usuario u WHERE u.email = :email"
    ),
    @NamedQuery(
        name = "Usuario.buscarPorNomeUsuario",
        query = "SELECT u FROM Usuario u WHERE u.nomeUsuario = :nomeUsuario"
    ),
    @NamedQuery(
        name = "Usuario.listarAtivos",
        query = "SELECT u FROM Usuario u WHERE u.status = 'ATIVO' ORDER BY u.dataRegistro DESC"
    )
})
public class Usuario extends BaseEntity {

    // ========================================================================
    // Atributos Básicos
    // ========================================================================

    /**
     * Email único do usuário.
     */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /**
     * Nome de usuário público - identificador único na plataforma.
     */
    @Column(name = "nome_usuario", nullable = false, unique = true, length = 30)
    private String nomeUsuario;

    /**
     * Nome completo do usuário.
     */
    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    /**
     * Biografia opcional do usuário.
     */
    @Column(name = "biografia", length = 500)
    private String biografia;

    /**
     * Data de nascimento para cálculo de idade.
     */
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    /**
     * Data de registro na plataforma.
     */
    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDate dataRegistro;

    /**
     * Status atual do usuário.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusUsuario status;

    /**
     * Indica se o perfil é público.
     */
    @Column(name = "perfil_publico", nullable = false)
    private Boolean perfilPublico = true;

    @Entity
    @Table(name = "usuarios")
    public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false, length = 255)
        private String email;

        @Column(nullable = false, length = 150)
        private String nome;

        @Column(name = "data_cadastro", nullable = false)
        private LocalDateTime dataCadastro;

        // Lado "One" do relacionamento bidirecional
        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,
                fetch = FetchType.LAZY, orphanRemoval = true)
        private List<Avaliacao> avaliacoes = new ArrayList<>();

        // Métodos para manutenção da consistência bidirecional
        public void adicionarAvaliacao(Avaliacao avaliacao) {
            if (avaliacao != null) {
                avaliacoes.add(avaliacao);
                avaliacao.setUsuario(this); // Sincronização bidirecional
            }
        }

        public void removerAvaliacao(Avaliacao avaliacao) {
            if (avaliacao != null) {
                avaliacoes.remove(avaliacao);
                avaliacao.setUsuario(null); // Quebra da associação
            }
        }

        protected Usuario() {}

        public Usuario(String email, String nome) {
            this.email = email;
            this.nome = nome;
            this.dataCadastro = LocalDateTime.now();
        }

        // getters e setters...
    }

    @Entity
    @Table(name = "avaliacoes")
    public class Avaliacao {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        @Min(1)
        @Max(10)
        private Integer nota;

        @Column(length = 2000)
        private String resenha;

        @Column(name = "data_avaliacao", nullable = false)
        private LocalDateTime dataAvaliacao;

        // Lado "Many" do relacionamento bidirecional
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        protected Avaliacao() {}

        public Avaliacao(Integer nota, String resenha, Usuario usuario) {
            this.nota = nota;
            this.resenha = resenha;
            this.usuario = usuario;
            this.dataAvaliacao = LocalDateTime.now();

            // Adiciona automaticamente à lista do usuário
            if (usuario != null) {
                usuario.getAvaliacoes().add(this);
            }
        }

        // Setter especial para manter consistência
        public void setUsuario(Usuario usuario) {
            // Remove da lista do usuário anterior, se existir
            if (this.usuario != null) {
                this.usuario.getAvaliacoes().remove(this);
            }

            this.usuario = usuario;

            // Adiciona à lista do novo usuário, se não nulo
            if (usuario != null && !usuario.getAvaliacoes().contains(this)) {
                usuario.getAvaliacoes().add(this);
            }
        }

        // getters e outros setters...
    }
    // ========================================================================
    // Construtores
    // ========================================================================

    /**
     * Construtor padrão JPA.
     */
    protected Usuario() {
        this.status = StatusUsuario.ATIVO;
        this.dataRegistro = LocalDate.now();
    }

    /**
     * Construtor para criação de novos usuários.
     */
    public Usuario(String email, String nomeUsuario, String nomeCompleto) {
        this();
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.nomeCompleto = nomeCompleto;
    }

    // ========================================================================
    // Métodos de Negócio
    // ========================================================================

    /**
     * Verifica se o usuário está ativo.
     */
    public boolean isAtivo() {
        return StatusUsuario.ATIVO.equals(this.status);
    }

    /**
     * Verifica se o perfil é público.
     */
    public boolean isPerfilPublico() {
        return Boolean.TRUE.equals(perfilPublico);
    }

    /**
     * Calcula a idade do usuário.
     */
    public Integer getIdade() {
        if (dataNascimento == null) {
            return null;
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    // ========================================================================
    // Getters e Setters
    // ========================================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public Boolean getPerfilPublico() {
        return perfilPublico;
    }

    public void setPerfilPublico(Boolean perfilPublico) {
        this.perfilPublico = perfilPublico;
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nomeUsuario='%s', status=%s}",
                           id, nomeUsuario, status);
    }
}