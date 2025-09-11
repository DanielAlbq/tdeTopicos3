package br.upf.ads175.critiquehub.entity.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set; // Importado caso opte por usar Set

@Entity
@Table(name = "generos")
public class Genero extends BaseEntity { // Supondo que BaseEntity tenha o 'id'

    @Column(unique = true, nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @ManyToMany(mappedBy = "generos")
    private List<ItemCultural> ItensCulturais = new ArrayList<>();
    // 💡 Dica: Usar Set<ItemCultural> em vez de List é geralmente melhor
    // para evitar duplicatas. Se optar por usar, lembre-se de mudar
    // a inicialização para "new HashSet<>()".

    // Construtor padrão para o JPA
    public Genero() {}

    // Construtor para facilitar a criação
    public Genero(String nome) {
        this.nome = nome;
    }

    // --- GETTERS E SETTERS ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ItemCultural> getItensCulturais() {
        return ItensCulturais;
    }

    public void setItems(List<ItemCultural> items) {
        this.ItensCulturais = items;
    }


    // --- equals e hashCode (Boa prática) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Genero genero = (Genero) o;
        return Objects.equals(nome, genero.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nome);
    }
}