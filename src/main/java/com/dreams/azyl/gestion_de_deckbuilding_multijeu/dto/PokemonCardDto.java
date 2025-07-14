package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import java.util.List;

public class PokemonCardDto {
    private String id;
    private String name;
    private String supertype;
    private List<String> subtypes;
    private String type;
    private String imageUrl;
    private String text;
    private Integer quantity;

    public PokemonCardDto() {

    }

    public PokemonCardDto(String id, String name, String supertype, List<String> subtypes, String type, String imageUrl, String text) {
        this.id = id;
        this.name = name;
        this.supertype = supertype;
        this.subtypes = subtypes;
        this.type = type;
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return switch (supertype.toLowerCase()) {
            case "pokémon" -> "pokemon";
            case "trainer" -> "trainer";
            case "energy"  -> "energy";
            default        -> "unknown";
        };
    }

    public void setType(String types) {
        this.type = types;
    }
}