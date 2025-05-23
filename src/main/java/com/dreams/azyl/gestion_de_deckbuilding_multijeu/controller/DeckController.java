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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @GetMapping("/{deckId}")
    public String voirDeck(@PathVariable Long deckId, Model model) {
        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck introuvable"));

        DeckViewDto dto = deckViewMapper.toDeckView(deck);
        model.addAttribute("deck", dto);
        return "decks/view";
    }

    @GetMapping("/new")
    public String showDeckCreationForm(@RequestParam(name="draftDeckId", required=false) Long draftDeckId, Model model, @PageableDefault(size = 20) Pageable pageable) {
        Deck draft;
        if (draftDeckId != null) {
            // récupère l’existant
            draft = deckRepository.findById(draftDeckId)
                    .orElseThrow(() -> new IllegalArgumentException("Draft introuvable : " + draftDeckId));
        } else {
            // crée un nouveau brouillon
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Utilisateur user = utilisateurRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
            draft = new Deck();
            draft.setName("Brouillon");
            draft.setFormat("POKEMON");
            draft.setStatut(DeckStatut.BROUILLON);
            draft.setOwner(user);
            draft.setCreatedAt(LocalDateTime.now());
            deckRepository.save(draft);
        }

        // 2) transmet toujours l’ID du brouillon
        model.addAttribute("draftDeckId", draft.getId());

        // 3) pagination comme avant, cards, currentCardPage, totalCardPages…
        List<PokemonCardDto> all = pokemonApiClient.getAllCards();
        int from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), all.size());
        int to   = Math.min(from + pageable.getPageSize(), all.size());
        model.addAttribute("cards", all.subList(from, to));
        model.addAttribute("currentCardPage", pageable.getPageNumber());
        model.addAttribute("totalCardPages", (int)Math.ceil((double) all.size() / pageable.getPageSize()));

        // 4) ton DeckFormDto
        DeckFormDto form = new DeckFormDto();
        form.setFormat("POKEMON");
        form.setStatut(DeckStatut.BROUILLON);
        model.addAttribute("deckForm", form);

        return "decks/new";
    }

    @PostMapping("/{deckId}/cards")
    @ResponseBody
    public ResponseEntity<?> addCardAjax(@PathVariable Long deckId, @RequestBody Map<String,String> payload) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Deck introuvable"));

        // récupère apiId et crée le DeckCard
        String apiId = payload.get("apiCardId");
        PokemonCardDto dto = pokemonApiClient.getCardById(apiId);

        DeckCard card = new DeckCard();
        card.setApiCardId(dto.getId());
        card.setName(dto.getName());
        card.setQuantity(1);
        card.setDeck(deck);

        deck.getCards().add(card);
        deckRepository.save(deck);

        // renvoie l’objet créé (optionnel)
        return ResponseEntity.ok(Map.of(
                "cardId",      card.getApiCardId(),
                "name",        card.getName(),
                "quantity",    card.getQuantity()
        ));
    }

    @PostMapping("/new")
    public String saveNewDeck(
            @RequestParam("draftDeckId") Long draftId,
            @ModelAttribute("deckForm") DeckFormDto deckForm) {

        Deck draft = deckRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("Draft introuvable : " + draftId));

        if (deckForm.getSelectedCardIds() == null
                || deckForm.getSelectedCardIds().isEmpty()) {
            deckRepository.delete(draft);
            return "redirect:/decks/new";
        }

        // 1) Met à jour les métadonnées
        draft.setName(deckForm.getName());
        draft.setStatut(deckForm.getStatut());

        // 2) Recrée la liste de DeckCard à partir du grouping
        Map<String, Long> counts = deckForm.getSelectedCardIds().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<DeckCard> newCards = counts.entrySet().stream()
                .map(e -> {
                    PokemonCardDto dto = pokemonApiClient.getCardById(e.getKey());
                    DeckCard dc = new DeckCard();
                    dc.setApiCardId(dto.getId());
                    dc.setName(dto.getName());
                    dc.setQuantity(e.getValue().intValue());
                    dc.setDeck(draft);
                    return dc;
                })
                .toList();

        draft.getCards().clear();
        draft.getCards().addAll(newCards);

        deckRepository.save(draft);
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
        return "redirect:/utilisateurs/mes-decks";
    }
}
