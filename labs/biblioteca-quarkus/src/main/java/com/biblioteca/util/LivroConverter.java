package com.biblioteca.util;

import com.biblioteca.entity.Livro;
import com.biblioteca.repository.LivroRepository; // Importe o repositório de Livro
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("livroConverter") // Damos um nome único para este conversor
@ApplicationScoped
public class LivroConverter implements Converter<Livro> {

    @Inject
    LivroRepository livroRepository; // Injeta o repositório de Livro

    @Override
    public Livro getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Converte o ID (String) de volta para um objeto Livro buscando no banco
        Long livroId = Long.valueOf(value);
        return livroRepository.findById(livroId).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Livro value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        // Converte o objeto Livro para seu ID (String)
        return String.valueOf(value.getId());
    }
}