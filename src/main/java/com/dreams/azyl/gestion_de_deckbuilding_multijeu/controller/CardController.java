package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.service.CardCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardController {

    private final CardCacheService cardService;

    public CardController(CardCacheService cardService) {
        this.cardService = cardService;
    }

    /**
     * Page d'affichage d'une carte par son ID (API)
     */
    @GetMapping("/{id}")
    public String showCard(@PathVariable String id, Model model) {
        PokemonCardDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        return "cards/view";
    }

    @GetMapping
    public String showAllCards(Model model) {
        List<PokemonCardDto> cards = cardService.getAllCards();
        model.addAttribute("cards", cards);
        return "cards/list";
    }


}

