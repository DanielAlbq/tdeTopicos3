package com.biblioteca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "acessos_usuario")
public class AcessoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String resultado; // SUCESSO, FALHA, BLOQUEADO

    @Column(name = "ip_address")
    private String ipAddress;

    // Construtor padrão
    public AcessoUsuario() {}

    // Construtor utilitário
    public AcessoUsuario(String username, String resultado, String ipAddress) {
        this.username = username;
        this.dataHora = LocalDateTime.now();
        this.resultado = resultado;
        this.ipAddress = ipAddress;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}