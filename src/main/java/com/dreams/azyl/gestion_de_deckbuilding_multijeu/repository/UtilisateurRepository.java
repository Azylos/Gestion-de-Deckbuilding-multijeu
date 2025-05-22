package com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
  Optional<Utilisateur>findByUsername(String username);
}