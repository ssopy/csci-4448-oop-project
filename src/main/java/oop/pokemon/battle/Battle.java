package oop.pokemon.battle;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;
import oop.pokemon.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Battle {
    private final Pokemon player;
    private final Pokemon cpu;
    private final MoveStrategy cpuStrategy;
    private final DamageCalculator damageCalculator;

    public Battle(Pokemon player, Pokemon cpu, MoveStrategy cpuStrategy, DamageCalculator damageCalculator) {
        this.player = Objects.requireNonNull(player);
        this.cpu = Objects.requireNonNull(cpu);
        this.cpuStrategy = Objects.requireNonNull(cpuStrategy);
        this.damageCalculator = Objects.requireNonNull(damageCalculator);
        if (player == cpu) {
            throw new IllegalArgumentException("Each side needs its own Pokemon instance.");
        }
    }

    public boolean isOver() {
        return player.isFainted() || cpu.isFainted();
    }

    public List<AttackResult> playTurn(Move playerMove) {
        if (isOver()) {
            throw new IllegalStateException("The battle is already over.");
        }
        requireKnownMove(player, playerMove);
        Move cpuMove = cpuStrategy.chooseMove(cpu, player);
        requireKnownMove(cpu, cpuMove);

        List<AttackResult> results = new ArrayList<>();
        // Player wins speed ties for this milestone.
        if (player.getStats().speed() >= cpu.getStats().speed()) {
            results.add(attack(player, cpu, playerMove));
            if (!cpu.isFainted()) {
                results.add(attack(cpu, player, cpuMove));
            }
        } else {
            results.add(attack(cpu, player, cpuMove));
            if (!player.isFainted()) {
                results.add(attack(player, cpu, playerMove));
            }
        }
        return List.copyOf(results);
    }

    private AttackResult attack(Pokemon attacker, Pokemon defender, Move move) {
        DamageRoll roll = damageCalculator.calculate(attacker, defender, move);
        int actualDamage = defender.takeDamage(roll.damage());
        return new AttackResult(attacker.getName(), defender.getName(), move.name(), actualDamage,
                roll.critical(), roll.typeMultiplier());
    }

    private static void requireKnownMove(Pokemon pokemon, Move move) {
        if (!pokemon.getMoves().contains(move)) {
            throw new IllegalArgumentException(pokemon.getName() + " does not know that move.");
        }
    }
}
