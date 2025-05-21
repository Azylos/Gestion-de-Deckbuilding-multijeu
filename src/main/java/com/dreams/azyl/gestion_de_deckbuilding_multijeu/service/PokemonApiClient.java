    package com.dreams.azyl.gestion_de_deckbuilding_multijeu.service;

    import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.CardListResponse;
    import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonCardDto;
    import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.CardResponse;
    import com.dreams.azyl.gestion_de_deckbuilding_multijeu.dto.PokemonData;
    import jakarta.persistence.EntityNotFoundException;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.web.client.RestTemplateBuilder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class PokemonApiClient {

        private final RestTemplate restTemplate;
        private static final String BASE_URL = "https://api.pokemontcg.io/v2/cards";

        /**
         * Construit le client HTTP avec l'en-tête X-Api-Key nécessaire pour l'API Pokémon TCG.
         */
        public PokemonApiClient(RestTemplateBuilder builder, @Value("${pokemon.api.key}") String apiKey) {
            this.restTemplate = builder.defaultHeader("X-Api-Key", apiKey).build();
        }

        /**
         * Appelle l'API Pokémon TCG pour récupérer une carte via son ID.
         * @param id identifiant de la carte (ex : "base1-1")
         * @return DTO contenant les infos importantes de la carte
         * @throws EntityNotFoundException si la carte est absente ou malformée
         */
        public PokemonCardDto getCardById(String id) {
            String url = String.format("%s/%s", BASE_URL, id);
            CardResponse response = restTemplate.getForObject(url, CardResponse.class);

            if (response == null || response.getData() == null) {
                throw new EntityNotFoundException("Carte non trouvée pour l’id : " + id);
            }

            PokemonData d = response.getData();

            return new PokemonCardDto(
                    d.getId(),
                    d.getName(),
                    d.getSupertype(),
                    d.getSubtypes(),
                    d.getImages().getSmall(),
                    Optional.ofNullable(d.getFlavorText()).orElse("")
            );
        }

        public List<PokemonCardDto> getAllCards() {
            String url = BASE_URL + "?pageSize=250"; // maximum autorisé par l’API
            CardListResponse response = restTemplate.getForObject(url, CardListResponse.class);

            if (response == null || response.getData() == null) {
                throw new EntityNotFoundException("Aucune carte trouvée");
            }

            return response.getData().stream().map(d -> new PokemonCardDto(
                    d.getId(),
                    d.getName(),
                    d.getSupertype(),
                    d.getSubtypes(),
                    d.getImages().getSmall(),
                    Optional.ofNullable(d.getFlavorText()).orElse("")
            )).toList();
        }

    }
