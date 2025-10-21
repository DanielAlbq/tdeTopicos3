package com.biblioteca.util;

import com.biblioteca.entity.Autor;
import com.biblioteca.repository.AutorRepository; // Importe o repositório
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("autorConverter") // Transforma em um bean CDI
@ApplicationScoped      // Define o escopo
public class AutorConverter implements Converter<Autor> {

    @Inject
    AutorRepository autorRepository; // Injeta o repositório para acesso ao banco

    @Override
    public Autor getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Converte o ID (String) de volta para um objeto Autor buscando no banco
        Long autorId = Long.valueOf(value);
        return autorRepository.findById(autorId).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Autor value) {
        if (value == null) {
            return "";
        }
        // Converte o objeto Autor para seu ID (String)
        return String.valueOf(value.getId());
    }
}