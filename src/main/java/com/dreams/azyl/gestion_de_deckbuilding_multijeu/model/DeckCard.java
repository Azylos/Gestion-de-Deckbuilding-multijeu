package com.dreams.azyl.gestion_de_deckbuilding_multijeu.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class DeckCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String apiCardId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    public DeckCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
