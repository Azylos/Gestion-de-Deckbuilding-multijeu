package com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}