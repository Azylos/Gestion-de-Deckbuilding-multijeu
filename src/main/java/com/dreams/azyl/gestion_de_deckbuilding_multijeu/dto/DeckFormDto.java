package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import java.util.List;

public class DeckFormDto {
    private String name;
    private String format;
    private List<String> selectedCardIds;

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
}
