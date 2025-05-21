package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/decks")
public class DeckController {

    @GetMapping
    public String DeckList() {
        return "decks/list";
    }

    @GetMapping("/new")
    public String DeckNew() {
        return "decks/new";
    }
}
