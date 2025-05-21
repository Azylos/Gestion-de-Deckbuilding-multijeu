package com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.CardCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCacheRepository extends JpaRepository<CardCache, String> {
}