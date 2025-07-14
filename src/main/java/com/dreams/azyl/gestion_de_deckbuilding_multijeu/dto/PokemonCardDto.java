package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import java.util.List;

public class PokemonCardDto {
    private String id;
    private String name;
    private String supertype;
    private List<String> subtypes;
    private List<String> types; // ✅ Ajouté pour types élémentaires (Feu, Eau, etc.)
    private String imageUrl;
    private String text;
    private Integer quantity;

    public PokemonCardDto() {
    }

    public PokemonCardDto(String id, String name, String supertype, List<String> subtypes,
                          List<String> types, String imageUrl, String text) {
        this.id = id;
        this.name = name;
        this.supertype = supertype;
        this.subtypes = subtypes;
        this.types = types;
        this.imageUrl = imageUrl;
        this.text = text;
    }

    // ==== GETTERS & SETTERS ====

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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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

    /**
     * Sert à déterminer le type principal de carte pour les filtres JS : pokemon, trainer, energy
     */
    public String getType() {
        if (types != null && !types.isEmpty()) {
            return types.get(0).toLowerCase(); // ex: "fire"
        } else {
            return switch (supertype.toLowerCase()) {
                case "pokémon" -> "pokemon";
                case "trainer" -> "trainer";
                case "energy"  -> "energy";
                default        -> "unknown";
            };
        }
    }
}
