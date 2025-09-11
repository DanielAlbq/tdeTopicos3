package br.upf.ads175.critiquehub.entity.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "itens_culturais")
public class ItemCultural extends BaseEntity { // Supondo que BaseEntity tenha o 'id'

    @Column(nullable = false, length = 255)
    private String titulo;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "item_cultural_genero",
            joinColumns = @JoinColumn(name = "item_cultural_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "item_cultural_tag", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "item_cultural_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // --- GETTER E SETTER PARA TAGS ---
// Este método resolve o erro "Cannot resolve method 'getTags'"
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // Construtor padrão para o JPA
    public ItemCultural() {}

    // --- Getters e Setters ---

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }

    // --- Métodos de Ajuda (Helper Methods) ---

    public void addGenero(Genero genero) {
        this.generos.add(genero);
        genero.getItensCulturais().add(this);
    }



    public void removeGenero(Genero genero) {
        this.generos.remove(genero);
        genero.getItensCulturais().remove(this);
    }

    // --- equals e hashCode (Boa prática) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Usa o equals da BaseEntity (se houver)
        ItemCultural that = (ItemCultural) o;
        return Objects.equals(titulo, that.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), titulo);
    }
}