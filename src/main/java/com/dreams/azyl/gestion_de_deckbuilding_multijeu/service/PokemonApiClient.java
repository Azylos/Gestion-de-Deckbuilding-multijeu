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

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class PokemonApiClient {

        private final RestTemplate restTemplate;
        private static final String BASE_URL = "https://api.pokemontcg.io/v2/cards";

        public PokemonApiClient(RestTemplateBuilder builder,
                                @Value("${pokemon.api.key}") String apiKey) {
            this.restTemplate = builder
                    .defaultHeader("X-Api-Key", apiKey)
                    .build();
        }

        public PokemonCardDto getCardById(String id) {
            String url = BASE_URL + "/" + id;
            CardResponse response = restTemplate.getForObject(url, CardResponse.class);

            if (response == null || response.getData() == null) {
                throw new EntityNotFoundException("Carte non trouvée pour l’id : " + id);
            }
            PokemonData d = response.getData();

            // on passe d.getSupertype() comme 'type'
            return new PokemonCardDto(
                    d.getId(),
                    d.getName(),
                    d.getSupertype(),
                    d.getSubtypes(),
                    d.getTypes(),
                    d.getImages().getSmall(),
                    Optional.ofNullable(d.getFlavorText()).orElse("")
            );
        }

        public List<PokemonCardDto> getAllCards() {
            List<PokemonCardDto> pokemons = fetchCardsBySupertype("Pokémon");
            List<PokemonCardDto> trainers = fetchCardsBySupertype("Trainer");
            List<PokemonCardDto> energies = fetchCardsBySupertype("Energy");

            List<PokemonCardDto> all = new ArrayList<>();
            all.addAll(pokemons);
            all.addAll(trainers);
            all.addAll(energies);

            return all;
        }

        private List<PokemonCardDto> fetchCardsBySupertype(String supertype) {
            String url = BASE_URL + "?pageSize=100&q=supertype:" + supertype;
            CardListResponse response = restTemplate.getForObject(url, CardListResponse.class);

            if (response == null || response.getData() == null) {
                return List.of();
            }

            return response.getData().stream()
                    .map(d -> new PokemonCardDto(
                            d.getId(),
                            d.getName(),
                            d.getSupertype(),
                            d.getSubtypes(),
                            d.getTypes(),
                            d.getImages().getSmall(),
                            Optional.ofNullable(d.getFlavorText()).orElse("")
                    ))
                    .toList();
        }
    }