package com.biblioteca.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class LoginAttemptService {

    // Configurações: 5 tentativas, bloqueio de 15 min, janela de 15 min
    private static final int MAX_TENTATIVAS = 5;
    private static final int MINUTOS_BLOQUEIO = 15;
    private static final int MINUTOS_JANELA = 15;

    private final Map<String, TentativaLogin> tentativas = new ConcurrentHashMap<>();

    public void registrarTentativaFalha(String username) {
        if (username == null || username.isBlank()) return;

        LocalDateTime agora = LocalDateTime.now();

        tentativas.compute(username.toLowerCase(), (key, tentativa) -> {
            if (tentativa == null) {
                return new TentativaLogin(1, agora, agora);
            }
            long minutosDesdeUltima = ChronoUnit.MINUTES.between(tentativa.ultimaTentativa, agora);
            if (minutosDesdeUltima > MINUTOS_JANELA) {
                return new TentativaLogin(1, agora, agora);
            }
            return new TentativaLogin(tentativa.contador + 1, tentativa.primeiraTentativa, agora);
        });
    }

    public boolean isBloqueado(String username) {
        if (username == null || username.isBlank()) return false;

        TentativaLogin tentativa = tentativas.get(username.toLowerCase());
        if (tentativa == null) return false;

        if (tentativa.contador < MAX_TENTATIVAS) return false;

        LocalDateTime agora = LocalDateTime.now();
        long minutosBloqueio = ChronoUnit.MINUTES.between(tentativa.ultimaTentativa, agora);

        if (minutosBloqueio >= MINUTOS_BLOQUEIO) {
            limparTentativas(username);
            return false;
        }
        return true;
    }

    public long minutosParaDesbloqueio(String username) {
        if (!isBloqueado(username)) return 0;
        TentativaLogin tentativa = tentativas.get(username.toLowerCase());
        long minutosPassados = ChronoUnit.MINUTES.between(tentativa.ultimaTentativa, LocalDateTime.now());
        return Math.max(0, MINUTOS_BLOQUEIO - minutosPassados);
    }

    public void limparTentativas(String username) {
        if (username != null) tentativas.remove(username.toLowerCase());
    }

    private static class TentativaLogin {
        final int contador;
        final LocalDateTime primeiraTentativa;
        final LocalDateTime ultimaTentativa;

        TentativaLogin(int contador, LocalDateTime primeira, LocalDateTime ultima) {
            this.contador = contador;
            this.primeiraTentativa = primeira;
            this.ultimaTentativa = ultima;
        }
    }
}