package oop.pokemon;

import oop.pokemon.battle.AttackResult;
import oop.pokemon.battle.Battle;
import oop.pokemon.battle.DamageCalculator;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.Type;
import oop.pokemon.strategy.MoveStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static oop.pokemon.PokemonTestFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class BattleTest {
    PokemonTestFactory pokemonFactory = new PokemonTestFactory();

    private final DamageCalculator damage = new DamageCalculator(new FixedRandom(0.5));
    private final MoveStrategy firstMove = (self, opponent) -> self.getMoves().get(0);

    @Test
    void testAttackOrderAndSpeedTies() {
        Pokemon player = pokemonFactory.createPokemon("Player", Type.NORMAL, 100, 40, TACKLE);
        Pokemon cpu = pokemonFactory.createPokemon("CPU", Type.NORMAL, 100, 60, TACKLE);
        Battle battle = new Battle(player, cpu, firstMove, damage);
        List<AttackResult> results = battle.playTurn(TACKLE);

        assertEquals("CPU", results.get(0).attacker());
        assertEquals("Player", results.get(1).attacker());
        assertEquals(60, player.getCurrentHp());
        assertEquals(60, cpu.getCurrentHp());

        Pokemon tiedCpu = pokemonFactory.createPokemon("Tied CPU", Type.NORMAL, 100, 40, TACKLE);
        Battle tiedBattle = new Battle(player, tiedCpu, firstMove, damage);
        List<AttackResult> tiedResults = tiedBattle.playTurn(TACKLE);
        assertEquals("Player", tiedResults.get(0).attacker());
    }

    @Test
    void testPlayerKnockoutEndsBattle() {
        Pokemon player = pokemonFactory.createPokemon("Player", Type.NORMAL, 100, 60, TACKLE);
        Pokemon cpu = pokemonFactory.createPokemon("CPU", Type.NORMAL, 20, 40, TACKLE);
        Battle battle = new Battle(player, cpu, firstMove, damage);
        List<AttackResult> results = battle.playTurn(TACKLE);

        assertEquals(1, results.size());
        assertEquals(20, results.get(0).damage());
        assertEquals(100, player.getCurrentHp());
        assertTrue(battle.isOver());
        assertThrows(IllegalStateException.class, () -> battle.playTurn(TACKLE));
    }

    @Test
    void testCpuKnockoutEndsBattle() {
        Pokemon player = pokemonFactory.createPokemon("Player", Type.NORMAL, 20, 40, TACKLE);
        Pokemon cpu = pokemonFactory.createPokemon("CPU", Type.NORMAL, 100, 60, TACKLE);
        Battle battle = new Battle(player, cpu, firstMove, damage);
        List<AttackResult> results = battle.playTurn(TACKLE);

        assertEquals(1, results.size());
        assertTrue(player.isFainted());
        assertEquals(100, cpu.getCurrentHp());
        assertTrue(battle.isOver());
    }

    @Test
    void testUnknownMovesDoNotChangeHealth() {
        Pokemon player = pokemonFactory.createPokemon("Player", Type.NORMAL, 100, 50, TACKLE);
        Pokemon cpu = pokemonFactory.createPokemon("CPU", Type.NORMAL, 100, 50, TACKLE);
        Battle battle = new Battle(player, cpu, firstMove, damage);
        assertThrows(IllegalArgumentException.class, () -> battle.playTurn(EMBER));
        MoveStrategy invalidStrategy = (self, opponent) -> EMBER;
        Battle invalidBattle = new Battle(player, cpu, invalidStrategy, damage);
        assertThrows(IllegalArgumentException.class, () -> invalidBattle.playTurn(TACKLE));
        assertEquals(100, player.getCurrentHp());
        assertEquals(100, cpu.getCurrentHp());
    }

    @Test
    void testCpuUsesSelectedStrategy() {
        Pokemon player = pokemonFactory.createPokemon("Player", Type.NORMAL, 100, 50, TACKLE);
        Pokemon cpu = pokemonFactory.createPokemon("CPU", Type.FIRE, 100, 50, TACKLE, EMBER);
        MoveStrategy chooseEmber = (self, opponent) -> self.getMoves().get(1);
        Battle battle = new Battle(player, cpu, chooseEmber, damage);
        List<AttackResult> results = battle.playTurn(TACKLE);

        assertEquals("Ember", results.get(1).move());
    }
}
