package com.dreams.azyl.gestion_de_deckbuilding_multijeu.service;

import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.model.CardCache;
import com.dreams.azyl.gestion_de_deckbuilding_multijeu.repository.CardCacheRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardCacheService {

    private final CardCacheRepository repository;
    private final PokemonApiClient apiClient;

    // Définition d'un TTL de 6 heures pour le cache
    private static final Duration TTL = Duration.ofHours(6);

    public CardCacheService(CardCacheRepository repository, PokemonApiClient apiClient) {
        this.repository = repository;
        this.apiClient = apiClient;
    }

    /**
     * Retourne une carte Pokémon depuis le cache ou l’API.
     * Si le cache est présent et non expiré : on le retourne.
     * Sinon : on appelle l’API, on met en cache, et on retourne le résultat.
     */
    public PokemonCardDto getCard(String apiCardId) {
        LocalDateTime now = LocalDateTime.now();

        return repository.findById(apiCardId)
                .filter(cached -> cached.getLastUpdated().isAfter(now.minus(TTL)))
                .map(this::toDto) // Convertit l'entité en DTO
                .orElseGet(() -> {
                    // Cache manquant ou expiré → appel API
                    PokemonCardDto fresh = apiClient.getCardById(apiCardId);
                    repository.save(toEntity(fresh)); // Sauvegarde dans le cache
                    return fresh;
                });
    }

    /**
     * Convertit une entité CardCache (en base) vers un DTO pour affichage.
     */
    private PokemonCardDto toDto(CardCache entity) {
        return new PokemonCardDto(
                entity.getApiCardId(),   // id
                entity.getName(),        // name
                entity.getType(),        // supertype
                List.of(),               // subtypes (vide)
                entity.getType(),        // type (même valeur que supertype)
                entity.getImageUrl(),    // imageUrl
                entity.getDescription()  // text
        );
    }

    /**
     * Convertit un DTO venant de l’API en entité JPA pour cache.
     */
    private CardCache toEntity(PokemonCardDto dto) {
        CardCache entity = new CardCache();
        entity.setApiCardId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getSupertype());
        entity.setImageUrl(dto.getImageUrl());
        entity.setDescription(dto.getText());
        entity.setLastUpdated(LocalDateTime.now());
        return entity;
    }

    public List<PokemonCardDto> getAllCards() {
        return apiClient.getAllCards();
    }
}
