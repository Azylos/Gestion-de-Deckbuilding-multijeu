package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckFormDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckViewDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.DeckCard;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.DeckRepository;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.PokemonApiClient;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckRepository deckRepository;
    private final PokemonApiClient pokemonApiClient;

    public DeckController(DeckRepository deckRepository, PokemonApiClient pokemonApiClient) {
        this.deckRepository = deckRepository;
        this.pokemonApiClient = pokemonApiClient;
    }

    @GetMapping
    public String DeckList(Model model, @PageableDefault(size = 9) Pageable pageable) {
        Page<Deck> page = deckRepository.findAll(pageable);

        List<DeckViewDto> deckDtos = page.getContent().stream()
                .map(deck -> toDeckView(deck)) // méthode définie plus bas
                .toList();

        model.addAttribute("decks", deckDtos);
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());
        return "decks/list";
    }


    @GetMapping("/new")
    public String showDeckCreationForm(Model model) {
        model.addAttribute("deckForm", new DeckFormDto());

        List<PokemonCardDto> cards = pokemonApiClient.getAllCards(); // appelle l’API
        model.addAttribute("cards", cards); // injecte dans le modèle

        return "decks/new";
    }

    @PostMapping("/new")
    public String saveNewDeck(@ModelAttribute("deckForm") DeckFormDto deckForm) {
        Deck deck = new Deck();
        deck.setName(deckForm.getName());
        deck.setCreatedAt(LocalDateTime.now());
        deck.setCards(
                deckForm.getSelectedCardIds().stream().map(cardId -> {
                    PokemonCardDto cardDto = pokemonApiClient.getCardById(cardId);
                    DeckCard deckCard = new DeckCard();
                    deckCard.setApiCardId(cardDto.getId());
                    deckCard.setName(cardDto.getName());
                    deckCard.setQuantity(1); // ajustable si tu veux supporter quantité
                    return deckCard;
                }).toList()
        );

        deckRepository.save(deck);

        return "redirect:/decks";
    }


    private DeckViewDto toDeckView(Deck deck) {
        String thumbnail = null;
        List<String> types = new ArrayList<>();

        if (!deck.getCards().isEmpty()) {
            var firstCard = deck.getCards().get(0);
            try {
                PokemonCardDto cardDto = pokemonApiClient.getCardById(firstCard.getApiCardId());
                thumbnail = cardDto.getImageUrl();
                types = cardDto.getSubtypes();
            } catch (Exception e) {
                // En cas d’erreur API, on continue sans image/types
                System.err.println("Erreur API Pokémon pour la carte : " + firstCard.getApiCardId());
            }
        }

        return new DeckViewDto(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                deck.getOwner() != null ? deck.getOwner().getUsername() : "Anonyme",
                deck.getCreatedAt(),
                deck.getViews(),
                thumbnail,
                types
        );
    }
}
