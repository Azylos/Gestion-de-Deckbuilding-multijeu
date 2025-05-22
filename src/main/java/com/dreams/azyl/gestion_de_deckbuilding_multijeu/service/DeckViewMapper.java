package com.dreams.azyl.gestion_de_deckbuilding_multijeu.service;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.DeckViewDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.Deck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckViewMapper {
    private final PokemonApiClient pokemonApiClient;

    public DeckViewMapper(PokemonApiClient pokemonApiClient) {
        this.pokemonApiClient = pokemonApiClient;
    }

    public DeckViewDto toDeckView(Deck deck) {
        String thumbnail = null;

        List<String> types = new ArrayList<>();

        if (!deck.getCards().isEmpty()) {
            var firstCard = deck.getCards().get(0);
            try {
                PokemonCardDto cardDto = pokemonApiClient.getCardById(firstCard.getApiCardId());
                thumbnail = cardDto.getImageUrl();
                types = cardDto.getSubtypes();
            } catch (Exception e) {
                System.err.println("Erreur API Pokémon pour la carte : " + firstCard.getApiCardId());
            }
        }

        return new DeckViewDto(
                deck.getId(),
                deck.getName(),
                deck.getDescription(),
                deck.getOwner() != null ? deck.getOwner().getUsername() : "Anonyme",
                deck.getCreatedAt(),
                deck.getViews(),
                thumbnail,
                types,
                deck.getFormat()
        );
    }
}

