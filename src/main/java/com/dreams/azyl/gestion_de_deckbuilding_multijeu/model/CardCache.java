package com.dreams.azyl.gestion_de_deckbuilding_multijeu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class CardCache implements Serializable {

    @Id
    private String apiCardId; // L'identifiant unique fourni par l’API externe

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    private String type; // (optionnel) créature, sort, etc.

    private String description; // texte de la carte si tu veux l’afficher directement

    private LocalDateTime lastUpdated; // Pour gérer l’expiration ou le rafraîchissement du cache

    public CardCache() {
    }

    public String getApiCardId() {
        return apiCardId;
    }

    public void setApiCardId(String apiCardId) {
        this.apiCardId = apiCardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
