package com.biblioteca.controller;

import com.biblioteca.repository.AcessoUsuarioRepository;
import com.biblioteca.service.LoginAttemptService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("loginBean")
@RequestScoped
public class LoginBean {

    private static final Logger LOGGER = Logger.getLogger(LoginBean.class.getName());

    @Inject
    private LoginAttemptService loginAttemptService;

    @Inject
    private AcessoUsuarioRepository acessoUsuarioRepository;

    private String username;
    private String password;

    public void login() {  // MUDOU DE String PARA void
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String ipAddress = request.getRemoteAddr();

        LOGGER.log(Level.INFO, "Tentativa de login para usuário: {0} de IP: {1}",
                new Object[]{username, ipAddress});

        // Validação básica
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Usuário e senha são obrigatórios.");
            return;
        }

        // Verifica bloqueio
        if (loginAttemptService.isBloqueado(username)) {
            long minutos = loginAttemptService.minutosParaDesbloqueio(username);
            acessoUsuarioRepository.registrarAcesso(username, "BLOQUEADO", ipAddress);

            LOGGER.log(Level.WARNING, "Usuário bloqueado: {0}", username);
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Conta bloqueada. Tente novamente em " + minutos + " minuto(s).");
            return;
        }

        try {
            // Tenta autenticar
            LOGGER.log(Level.INFO, "Chamando request.login() para: {0}", username);
            request.login(username, password);

            // Verifica se realmente autenticou
            if (request.getUserPrincipal() == null) {
                LOGGER.log(Level.SEVERE, "Login retornou mas Principal é null!");
                throw new ServletException("Autenticação falhou silenciosamente");
            }

            // Sucesso
            loginAttemptService.limparTentativas(username);
            acessoUsuarioRepository.registrarAcesso(username, "SUCESSO", ipAddress);

            LOGGER.log(Level.INFO, "Login bem-sucedido para: {0}. Redirecionando...", username);

            // Cria sessão explicitamente
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);

            // REDIRECIONAMENTO PROGRAMÁTICO
            String contextPath = externalContext.getRequestContextPath();
            externalContext.redirect(contextPath + "/index.xhtml");
            context.responseComplete();  // CRÍTICO: Informa ao JSF para parar o processamento

        } catch (ServletException e) {
            LOGGER.log(Level.WARNING, "Falha de autenticação para: " + username, e);

            loginAttemptService.registrarTentativaFalha(username);
            acessoUsuarioRepository.registrarAcesso(username, "FALHA", ipAddress);

            addMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao redirecionar após login", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao acessar a página inicial.");
        }
    }

    public void logout() {  // MUDOU DE String PARA void
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            String currentUser = (request.getUserPrincipal() != null)
                    ? request.getUserPrincipal().getName()
                    : "desconhecido";

            LOGGER.log(Level.INFO, "Logout para usuário: {0}", currentUser);

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            request.logout();

            String contextPath = externalContext.getRequestContextPath();
            externalContext.redirect(contextPath + "/login.xhtml");
            context.responseComplete();

        } catch (ServletException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao fazer logout", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao sair do sistema.");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, null));
    }

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}