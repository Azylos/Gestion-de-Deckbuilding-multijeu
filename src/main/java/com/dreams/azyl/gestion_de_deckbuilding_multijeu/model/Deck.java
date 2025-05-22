package com.dreams.azyl.gestion_de_deckbuilding_multijeu.model;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.enums.DeckStatut;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Deck implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    private int views; // simple compteur
    private String format;

    @Enumerated(EnumType.STRING)
    private DeckStatut statut;

    @ManyToOne
    private Utilisateur owner;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "deck")
    private List<DeckCard> cards = new ArrayList<>();
    private LocalDateTime createdAt;

    public Deck() {
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

    public Utilisateur getOwner() {
        return owner;
    }

    public void setOwner(Utilisateur owner) {
        this.owner = owner;
    }

    public List<DeckCard> getCards() {
        return cards;
    }

    public void setCards(List<DeckCard> cards) {
        this.cards = cards;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public DeckStatut getStatut() {
        return statut;
    }

    public void setStatut(DeckStatut statut) {
        this.statut = statut;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
