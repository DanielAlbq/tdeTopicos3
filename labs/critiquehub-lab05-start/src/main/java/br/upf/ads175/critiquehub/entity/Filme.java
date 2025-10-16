package br.upf.ads175.critiquehub.entity;


import br.upf.ads175.critiquehub.entity.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Filme.listarTodos",
                query = "select f FROM Filme f"
        )
})
@Table(name="filmes")

public class Filme extends BaseEntity {

    @NotBlank(message="O título é obrigatório")
    @Size(max= 150, message= "O título não pode ter mais de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    public Filme() {

    }

    public Filme(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {}

}
