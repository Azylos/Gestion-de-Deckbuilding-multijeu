package com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.enums.DeckStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    Page<Deck> findByOwner(Utilisateur owner, Pageable pageable);
    Page<Deck> findByStatut(DeckStatut statut, Pageable pageable);
}