package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import java.util.Set;

/**
 * DTO pour un utilisateur, sans mot de passe.
 * Sert à remplir tes vues Thymeleaf (profil, liste d'utilisateurs…).
 */
public class UtilisateurDto {
    private Long id;               // identifiant en base
    private String username;       // login
    private String email;          // email
    private Set<String> roles;     // rôles (USER, ADMIN…)

    public UtilisateurDto() {}

    public UtilisateurDto(Long id, String username, String email, Set<String> roles) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.roles    = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
