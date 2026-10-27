package oop.pokemon.factory;

import com.google.gson.Gson;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.PokemonSpecies;

import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class JsonPokemonFactory implements PokemonFactory {
    private final Map<String, PokemonSpecies> speciesByName = new HashMap<>();

    public JsonPokemonFactory(Reader reader) {
        Objects.requireNonNull(reader);
        PokemonSpecies[] roster;
        try {
            roster = new Gson().fromJson(reader, PokemonSpecies[].class);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Invalid Pokemon roster JSON.", exception);
        }
        if (roster == null || roster.length == 0) {
            throw new IllegalArgumentException("The roster must contain at least one species.");
        }
        for (PokemonSpecies species : roster) {
            if (species == null) {
                throw new IllegalArgumentException("The roster cannot contain null entries.");
            }
            if (speciesByName.putIfAbsent(key(species.name()), species) != null) {
                throw new IllegalArgumentException("Duplicate species: " + species.name());
            }
        }
    }

    @Override
    public Pokemon createPokemon(String speciesName) {
        PokemonSpecies species = speciesByName.get(key(Objects.requireNonNull(speciesName)));
        if (species == null) {
            throw new IllegalArgumentException("Unknown Pokemon: " + speciesName);
        }
        return new Pokemon(species);
    }

    private static String key(String name) {
        return name.strip().toLowerCase(Locale.ROOT);
    }
}
