package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckFormDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckViewDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.DeckCard;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.enums.DeckStatut;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.DeckRepository;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.UtilisateurRepository;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.DeckViewMapper;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.PokemonApiClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckRepository deckRepository;
    private final PokemonApiClient pokemonApiClient;
    private final UtilisateurRepository utilisateurRepository;
    private final DeckViewMapper deckViewMapper;

    public DeckController(DeckRepository deckRepository, PokemonApiClient pokemonApiClient, UtilisateurRepository utilisateurRepository, DeckViewMapper deckViewMapper) {
        this.deckRepository = deckRepository;
        this.pokemonApiClient = pokemonApiClient;
        this.utilisateurRepository = utilisateurRepository;
        this.deckViewMapper = deckViewMapper;
    }

    @GetMapping
    public String deckList(Model model, @PageableDefault(size = 9) Pageable pageable) {
        Page<Deck> page = deckRepository.findByStatut(DeckStatut.PUBLIE, pageable);

        List<DeckViewDto> deckDtos = page.getContent().stream()
                .map(deckViewMapper::toDeckView)
                .toList();

        model.addAttribute("decks", deckDtos);
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());

        return "decks/list";
    }

    @GetMapping("/new")
    public String showDeckCreationForm(Model model, @PageableDefault(size = 20) Pageable pageable) {
        List<PokemonCardDto> allCards = pokemonApiClient.getAllCards();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCards = allCards.size();

        int fromIndex = Math.min(pageNumber * pageSize, totalCards);
        int toIndex = Math.min(fromIndex + pageSize, totalCards);

        List<PokemonCardDto> cardsPage = allCards.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) totalCards / pageSize);

        model.addAttribute("deckForm", new DeckFormDto());
        model.addAttribute("cards", cardsPage);
        model.addAttribute("currentCardPage", pageNumber);
        model.addAttribute("totalCardPages", totalPages);

        return "decks/new";
    }

    @PostMapping("/new")
    public String saveNewDeck(@ModelAttribute("deckForm") DeckFormDto deckForm) {
        // 1. Récupérer l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable : " + username));

        // 2. Créer et remplir le deck
        Deck deck = new Deck();
        deck.setName(deckForm.getName());
        deck.setDescription(deckForm.getFormat()); // ← à adapter si ce champ est bien "format"
        deck.setCreatedAt(LocalDateTime.now());
        deck.setStatut(deckForm.getStatut());
        deck.setOwner(utilisateur);
        deck.setFormat("POKEMON");

        List<DeckCard> cards = deckForm.getSelectedCardIds().stream().map(cardId -> {
            PokemonCardDto cardDto = pokemonApiClient.getCardById(cardId);
            DeckCard deckCard = new DeckCard();
            deckCard.setApiCardId(cardDto.getId());
            deckCard.setName(cardDto.getName());
            deckCard.setQuantity(1); // à ajuster si tu gères les quantités
            return deckCard;
        }).toList();

        deck.setCards(cards);

        // 3. Sauvegarder
        deckRepository.save(deck);

        return "redirect:/decks";
    }

    @GetMapping("/{id}/edit")
    public String editDeck(@PathVariable Long id, Model model) {
        Deck deck = deckRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Deck introuvable: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!deck.getOwner().getUsername().equals(username)) {
            return "redirect:/access-denied"; // ou 403
        }

        DeckFormDto dto = new DeckFormDto();
        dto.setName(deck.getName());
        dto.setFormat(deck.getDescription()); // adapter si besoin
        dto.setStatut(deck.getStatut());

        model.addAttribute("deckForm", dto);
        model.addAttribute("deckId", id);

        return "decks/edit"; // ton formulaire de modification
    }

    @PostMapping("/{id}/edit")
    public String updateDeck(@PathVariable Long id, @ModelAttribute DeckFormDto form) {
        Deck deck = deckRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Deck introuvable"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!deck.getOwner().getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        deck.setName(form.getName());
        deck.setDescription(form.getFormat());
        deck.setStatut(form.getStatut());
        // TODO: recharger les cartes si modifiables

        deckRepository.save(deck);
        return "redirect:/decks";
    }

    @PostMapping("/{id}/delete")
    public String deleteDeck(@PathVariable Long id) {
        Deck deck = deckRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Deck introuvable"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!deck.getOwner().getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        deckRepository.delete(deck);
        return "redirect:/decks";
    }
}
