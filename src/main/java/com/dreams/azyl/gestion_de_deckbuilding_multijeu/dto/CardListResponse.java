package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardListResponse {
    private List<PokemonData> data;

    public List<PokemonData> getData() {
        return data;
    }

    public void setData(List<PokemonData> data) {
        this.data = data;
    }
}
