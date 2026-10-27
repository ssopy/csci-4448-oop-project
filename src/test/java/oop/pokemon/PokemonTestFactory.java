package oop.pokemon;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.PokemonSpecies;
import oop.pokemon.model.Stats;
import oop.pokemon.model.Type;

import java.util.List;

public class PokemonTestFactory {
    static final Move TACKLE = new Move("Tackle", Type.NORMAL, 40);
    static final Move EMBER = new Move("Ember", Type.FIRE, 40);

    public Pokemon createPokemon(String name, Type type, int hp, int speed, Move... moves) {
        Stats stats = new Stats(hp, 50, 50, speed);
        PokemonSpecies species = new PokemonSpecies(name, type, stats, List.of(moves));
        return new Pokemon(species);
    }
}
