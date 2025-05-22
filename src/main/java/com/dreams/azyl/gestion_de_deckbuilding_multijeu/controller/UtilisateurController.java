package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckViewDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.DeckRepository;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.UtilisateurRepository;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.DeckViewMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/utilisateurs")
class UtilisateurController {
    private final DeckRepository deckRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DeckViewMapper deckViewMapper;

    public UtilisateurController(DeckRepository deckRepository, UtilisateurRepository utilisateurRepository, DeckViewMapper deckViewMapper) {
        this.deckRepository = deckRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.deckViewMapper = deckViewMapper;
    }

    @GetMapping("/mes-decks")
    public String mesDecks(Model model, @PageableDefault(size = 9) Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Utilisateur user = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));

        Page<Deck> page = deckRepository.findByOwner(user, pageable);

        Map<String, List<DeckViewDto>> decksParJeu = page.getContent().stream()
                .map(deckViewMapper::toDeckView)
                .collect(Collectors.groupingBy(DeckViewDto::getFormat)); // Assure-toi que DeckViewDto a bien `getFormat()`

        model.addAttribute("decksParJeu", decksParJeu);
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());

        return "utilisateurs/mes-decks";
    }


}
