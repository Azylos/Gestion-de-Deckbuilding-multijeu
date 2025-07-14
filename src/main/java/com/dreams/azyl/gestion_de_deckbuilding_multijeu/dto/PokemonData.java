package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonData {
    private String id;
    private String name;
    private String supertype;
    private List<String> subtypes;
    private List<String> types;
    private Images images;
    private String flavorText;

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSupertype() { return supertype; }
    public void setSupertype(String supertype) { this.supertype = supertype; }
    public List<String> getSubtypes() { return subtypes; }
    public void setSubtypes(List<String> subtypes) { this.subtypes = subtypes; }
    public Images getImages() { return images; }
    public void setImages(Images images) { this.images = images; }
    public String getFlavorText() { return flavorText; }
    public void setFlavorText(String flavorText) { this.flavorText = flavorText; }
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}