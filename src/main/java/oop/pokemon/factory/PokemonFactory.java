package oop.pokemon.factory;

import oop.pokemon.model.Pokemon;

public interface PokemonFactory {
    Pokemon createPokemon(String speciesName);
}
