package oop.pokemon;

import oop.pokemon.battle.DamageCalculator;
import oop.pokemon.battle.DamageRoll;
import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.PokemonSpecies;
import oop.pokemon.model.Stats;
import oop.pokemon.model.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oop.pokemon.PokemonTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class DamageCalculatorTest {
    PokemonTestFactory pokemonFactory = new PokemonTestFactory();

    private final Pokemon attacker = pokemonFactory.createPokemon("Attacker", Type.FIRE, 100, 50, EMBER);
    private final DamageCalculator normalHit = new DamageCalculator(new FixedRandom(0.5));

    @Test
    void testTypeEffectiveness() {
        Pokemon grass = pokemonFactory.createPokemon("Grass", Type.GRASS, 100, 50, TACKLE);
        Pokemon water = pokemonFactory.createPokemon("Water", Type.WATER, 100, 50, TACKLE);
        Pokemon normal = pokemonFactory.createPokemon("Normal", Type.NORMAL, 100, 50, TACKLE);

        assertEquals(80, normalHit.calculate(attacker, grass, EMBER).damage());
        assertEquals(20, normalHit.calculate(attacker, water, EMBER).damage());
        assertEquals(40, normalHit.calculate(attacker, normal, EMBER).damage());
        assertEquals(100, grass.getCurrentHp());
        assertEquals(100, attacker.getCurrentHp());
    }

    @Test
    void testCriticalHits() {
        Pokemon defender = pokemonFactory.createPokemon("Defender", Type.NORMAL, 100, 50, TACKLE);
        DamageCalculator criticalHit = new DamageCalculator(new FixedRandom(0.0));
        DamageRoll critical = criticalHit.calculate(attacker, defender, EMBER);

        assertTrue(critical.critical());
        assertEquals(60, critical.damage());
        DamageCalculator boundaryHit = new DamageCalculator(new FixedRandom(1.0 / 16.0));
        DamageRoll boundary = boundaryHit.calculate(attacker, defender, EMBER);
        assertFalse(boundary.critical());
    }

    @Test
    void testDefenseAndMinimumDamage() {
        Stats stats = new Stats(100, 50, 300, 50);
        PokemonSpecies species = new PokemonSpecies("Tank", Type.NORMAL, stats, List.of(TACKLE));
        Pokemon defender = new Pokemon(species);
        Move tinyHit = new Move("Tiny hit", Type.NORMAL, 1);

        assertEquals(6, normalHit.calculate(attacker, defender, EMBER).damage());
        assertEquals(1, normalHit.calculate(attacker, defender, tinyHit).damage());
    }

    @Test
    void testTypeChart() {
        double[][] expected = {
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 0.5, 0.5, 2.0},
                {1.0, 2.0, 0.5, 0.5},
                {1.0, 0.5, 2.0, 0.5}
        };
        for (Type attack : Type.values()) {
            for (Type defense : Type.values()) {
                assertEquals(expected[attack.ordinal()][defense.ordinal()],
                        attack.multiplierAgainst(defense), attack + " vs " + defense);
            }
        }
    }
}
