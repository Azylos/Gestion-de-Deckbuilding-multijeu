package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardResponse {
    private PokemonData data;
    public PokemonData getData() { return data; }
    public void setData(PokemonData data) { this.data = data; }
}