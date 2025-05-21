package com.dreams.azyl.gestion_de_deckbuilding_multijeu.service;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur avec rôle USER.
     */
    public void registerUser(String username, String email, String password) {
        if (repository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of("USER"));

        repository.save(user);
    }

    /**
     * Récupère un utilisateur par son nom.
     */
    public Utilisateur findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));
    }
}
