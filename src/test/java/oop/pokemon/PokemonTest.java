package oop.pokemon;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.PokemonSpecies;
import oop.pokemon.model.Stats;
import oop.pokemon.model.Type;
import oop.pokemon.observer.BattleLog;
import oop.pokemon.observer.HealthChangedEvent;
import oop.pokemon.observer.PokemonObserver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static oop.pokemon.PokemonTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    PokemonTestFactory pokemonFactory = new PokemonTestFactory();

    private final Pokemon pokemon = pokemonFactory.createPokemon("Testmon", Type.FIRE, 100, 50, TACKLE, EMBER);

    @Test
    void testDamageStopsAtZero() {
        assertEquals(100, pokemon.takeDamage(999));
        assertEquals(0, pokemon.getCurrentHp());
        assertTrue(pokemon.isFainted());
        assertEquals(0, pokemon.takeDamage(10));
    }

    @Test
    void testHealingAndInvalidAmounts() {
        pokemon.takeDamage(30);
        pokemon.heal(Integer.MAX_VALUE);
        assertEquals(100, pokemon.getCurrentHp());
        assertThrows(IllegalArgumentException.class, () -> pokemon.takeDamage(-1));
        assertThrows(IllegalArgumentException.class, () -> pokemon.heal(-1));
        assertEquals(100, pokemon.getCurrentHp());
    }

    @Test
    void testHealthObservers() {
        List<HealthChangedEvent> events = new ArrayList<HealthChangedEvent>();
        PokemonObserver collector = events::add;
        BattleLog log = new BattleLog();
        pokemon.addObserver(collector);
        pokemon.addObserver(collector);
        pokemon.addObserver(log);
        pokemon.takeDamage(20);
        pokemon.takeDamage(0);
        pokemon.heal(10);
        pokemon.removeObserver(collector);
        pokemon.takeDamage(5);

        assertEquals(List.of(new HealthChangedEvent("Testmon", 100, 80, 100),
                new HealthChangedEvent("Testmon", 80, 90, 100)), events);
        assertEquals(3, log.getEntries().size());
        assertThrows(UnsupportedOperationException.class, () -> log.getEntries().clear());
    }

    @Test
    void testMoveListLimitsAndCopying() {
        List<Move> moves = new ArrayList<>(List.of(TACKLE));
        PokemonSpecies species = new PokemonSpecies("Test", Type.NORMAL, new Stats(100, 50, 50, 50), moves);
        moves.add(EMBER);
        assertEquals(1, species.moves().size());
        assertThrows(UnsupportedOperationException.class, () -> species.moves().add(EMBER));
        assertThrows(IllegalArgumentException.class, () -> new PokemonSpecies(
                "Test", Type.NORMAL, species.stats(), List.of()));
        List<Move> tooMany = List.of(TACKLE, EMBER, TACKLE, EMBER, TACKLE);
        assertThrows(IllegalArgumentException.class, () -> new PokemonSpecies(
                "Test", Type.NORMAL, species.stats(), tooMany));
    }
}
