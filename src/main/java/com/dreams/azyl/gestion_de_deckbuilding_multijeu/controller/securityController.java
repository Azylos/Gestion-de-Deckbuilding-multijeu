package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class securityController {
    private final UtilisateurService utilisateurService;

    public securityController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "utilisateurs/login";
    }

    // 📋 Affiche une page HTML avec un formulaire d’inscription
    @GetMapping("/inscription")
    public String inscriptionForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "utilisateurs/inscription";
    }

    // 📝 Traite le formulaire d’inscription
    @PostMapping("/inscription")
    public String enregistrerUtilisateur(@ModelAttribute Utilisateur utilisateur) {
        utilisateurService.registerUser(
                utilisateur.getUsername(),
                utilisateur.getEmail(),
                utilisateur.getPassword()
        );
        return "redirect:/login";
    }
}
