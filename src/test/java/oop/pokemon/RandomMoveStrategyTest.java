package oop.pokemon;

import oop.pokemon.model.Pokemon;
import oop.pokemon.model.Type;
import oop.pokemon.strategy.RandomMoveStrategy;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static oop.pokemon.PokemonTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class RandomMoveStrategyTest {
    PokemonTestFactory pokemonFactory = new PokemonTestFactory();

    @Test
    void testRandomMoveSelection() {
        Pokemon self = pokemonFactory.createPokemon("Self", Type.FIRE, 100, 50, TACKLE, EMBER);
        Pokemon opponent = pokemonFactory.createPokemon("Opponent", Type.NORMAL, 100, 50, TACKLE);
        RandomMoveStrategy strategy = new RandomMoveStrategy(new Random(0) {
            @Override
            public int nextInt(int bound) {
                assertEquals(2, bound);
                return bound - 1;
            }
        });
        assertEquals(EMBER, strategy.chooseMove(self, opponent));
    }

    @Test
    void testFaintedPokemonCannotChooseMove() {
        Pokemon self = pokemonFactory.createPokemon("Self", Type.NORMAL, 100, 50, TACKLE);
        Pokemon opponent = pokemonFactory.createPokemon("Opponent", Type.NORMAL, 100, 50, TACKLE);
        self.takeDamage(100);
        RandomMoveStrategy strategy = new RandomMoveStrategy(new Random(0));
        assertThrows(IllegalStateException.class, () -> strategy.chooseMove(self, opponent));
    }
}
