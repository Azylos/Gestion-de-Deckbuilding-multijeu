package com.dreams.azyl.gestion_de_deckbuilding_multijeu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class Home {

    @GetMapping("/")
    public String Home() {
        return "index";
    }
}
