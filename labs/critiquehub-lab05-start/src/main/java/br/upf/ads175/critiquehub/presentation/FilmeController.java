package br.upf.ads175.critiquehub.presentation;


import br.upf.ads175.critiquehub.entity.Filme;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class FilmeController implements Serializable {

    private Filme filme;

    @PostConstruct
    public void init() {

    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

}
