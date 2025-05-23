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
    private final CardCacheService cardCacheService;

    public DeckViewMapper(PokemonApiClient pokemonApiClient, CardCacheService cardCacheService) {
        this.pokemonApiClient = pokemonApiClient;
        this.cardCacheService = cardCacheService;
    }

    public DeckViewDto toDeckView(Deck deck) {
        DeckViewDto dto = new DeckViewDto();

        dto.setId(deck.getId());
        dto.setName(deck.getName());
        dto.setDescription(deck.getDescription());
        dto.setOwnerName(deck.getOwner().getUsername());
        dto.setCreatedAt(deck.getCreatedAt());
        dto.setViews(deck.getViews());
        dto.setFormat(deck.getFormat());

        // puis votre calcul des cartes / quantités :
        List<PokemonCardDto> cardDtos = deck.getCards().stream()
                .map(dc -> {
                    PokemonCardDto cd;
                    try {
                        cd = cardCacheService.getCard(dc.getApiCardId());
                    } catch (Exception e) {
                        cd = new PokemonCardDto();
                        cd.setId(dc.getApiCardId());
                        cd.setName("Carte indisponible");
                        cd.setImageUrl("/images/placeholder.png");
                    }
                    cd.setQuantity(dc.getQuantity());
                    return cd;
                })
                .toList();
        dto.setCards(cardDtos);

        int total = cardDtos.stream().mapToInt(PokemonCardDto::getQuantity).sum();
        dto.setTotalCards(total);
        dto.setPokemonCount(  cardDtos.stream()
                .filter(c->"Pokémon".equals(c.getSupertype()))
                .mapToInt(PokemonCardDto::getQuantity).sum());
        dto.setTrainerCount(  cardDtos.stream()
                .filter(c->"Trainer".equals(c.getSupertype()))
                .mapToInt(PokemonCardDto::getQuantity).sum());
        dto.setEnergyCount(   cardDtos.stream()
                .filter(c->"Energy".equals(c.getSupertype()))
                .mapToInt(PokemonCardDto::getQuantity).sum());

        return dto;
    }
}

