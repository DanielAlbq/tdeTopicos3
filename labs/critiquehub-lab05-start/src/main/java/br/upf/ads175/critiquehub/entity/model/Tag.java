package br.upf.ads175.critiquehub.entity.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nome;

    @Column(length = 7) // Para cores hexadecimais como #FF5733
    private String cor;

    /**
     * Construtor padrão protegido, exigido pelo JPA.
     */
    protected Tag() {}

    /**
     * Construtor principal para criar novas tags.
     * @param nome O nome da tag.
     */
    public Tag(String nome) {
        // Reutiliza a lógica do setter para evitar duplicação de código
        this.setNome(nome);
        this.cor = gerarCorAleatoria();
    }

    private String gerarCorAleatoria() {
        // Gera uma cor hexadecimal aleatória
        Random random = new Random();
        return String.format("#%06X", random.nextInt(0xFFFFFF));
    }

    @ManyToMany(mappedBy = "tags") // "tags" é o nome do campo na classe ItemCultural
    private Set<ItemCultural> itens = new HashSet<>();

    // --- GETTER E SETTER PARA ITENS ---
// Este método resolve o erro "Cannot resolve method 'getItens'"
    public Set<ItemCultural> getItens() {
        return itens;
    }

    public void setItens(Set<ItemCultural> itens) {
        this.itens = itens;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da tag, aplicando a mesma formatação do construtor
     * para garantir a consistência dos dados (minúsculas e sem espaços extras).
     * @param nome O novo nome para a tag.
     */
    public void setNome(String nome) {
        if (nome != null) {
            this.nome = nome.toLowerCase().trim();
        } else {
            this.nome = null;
        }
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    // --- MÉTODOS equals E hashCode (BOA PRÁTICA) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(nome, tag.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}