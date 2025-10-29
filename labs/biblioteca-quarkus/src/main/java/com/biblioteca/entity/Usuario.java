// src/main/java/com/biblioteca/entity/Usuario.java
package com.biblioteca.entity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@UserDefinition // Marca esta entidade como a fonte de usuários para o Quarkus Security
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Username // Marca este campo como o nome de usuário
    @Column(unique = true, nullable = false)
    private String username;

    @Password // Marca este campo como a senha
    @Column(nullable = false)
    private String password;

    @Roles // Marca este campo como a permissão/role do usuário
    @Column(nullable = false)
    private String role;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}