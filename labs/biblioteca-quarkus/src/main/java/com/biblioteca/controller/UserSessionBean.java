// src/main/java/com/biblioteca/controller/UserSessionBean.java
package com.biblioteca.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.Principal;

@Named("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {

    public String getUsername() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        return principal != null ? principal.getName() : null;
    }

    public boolean isLoggedIn() {
        return getUsername() != null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean hasRole(String role) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().isUserInRole(role);
    }
}