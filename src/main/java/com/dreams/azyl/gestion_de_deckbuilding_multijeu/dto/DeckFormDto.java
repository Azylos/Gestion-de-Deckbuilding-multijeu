package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.enums.DeckStatut;

import java.util.List;

public class DeckFormDto {
    private String name;
    private String format;
    private List<String> selectedCardIds;
    private DeckStatut statut;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getSelectedCardIds() {
        return selectedCardIds;
    }

    public void setSelectedCardIds(List<String> selectedCardIds) {
        this.selectedCardIds = selectedCardIds;
    }

    public DeckStatut getStatut() {
        return statut;
    }

    public void setStatut(DeckStatut statut) {
        this.statut = statut;
    }


}
