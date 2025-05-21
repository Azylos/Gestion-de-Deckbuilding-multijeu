package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Images {
    private String small;

    public String getSmall() { return small; }
    public void setSmall(String small) { this.small = small; }
}