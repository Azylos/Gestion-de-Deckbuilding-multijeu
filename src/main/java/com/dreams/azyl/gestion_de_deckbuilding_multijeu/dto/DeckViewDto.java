package com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DeckViewDto {
    private Long id;
    private String name;
    private String description;
    private String ownerName;
    private LocalDateTime createdAt;
    private int views;
    private String thumbnailUrl;
    private List<String> types; // ex: ["Feu", "Électrik"]

    public DeckViewDto() {
    }

    public DeckViewDto(Long id, String name, String description, String ownerName, LocalDateTime createdAt, int views, String thumbnailUrl, List<String> types) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerName = ownerName;
        this.createdAt = createdAt;
        this.views = views;
        this.thumbnailUrl = thumbnailUrl;
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}

