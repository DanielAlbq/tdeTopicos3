package com.critiquehub.entity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe base para todas as entidades do sistema CritiqueHub.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * Identificador único da entidade.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Controle de versão para concorrência otimista.
     */
    @Version
    protected Long versao;

    // ========================================================================
    // Getters e Setters básicos
    // ========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersao() {
        return versao;
    }

    /**
     * Verifica se a entidade é nova (não persistida).
     */
    public boolean isNew() {
        return this.id == null;
    }

    // ========================================================================
    // Implementação de equals e hashCode
    // ========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (this.id == null || that.id == null) {
            return false;
        }

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, versao=%d}",
                           getClass().getSimpleName(), id, versao);
    }
}