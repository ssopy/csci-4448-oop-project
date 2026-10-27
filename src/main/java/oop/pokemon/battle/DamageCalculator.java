package oop.pokemon.battle;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;

import java.util.Objects;
import java.util.Random;

public class DamageCalculator {
    private final Random random;

    public DamageCalculator(Random random) {
        this.random = Objects.requireNonNull(random);
    }

    public DamageRoll calculate(Pokemon attacker, Pokemon defender, Move move) {
        double base = move.power() * (double) attacker.getStats().attack() / defender.getStats().defense();
        double effectiveness = move.type().multiplierAgainst(defender.getType());
        boolean critical = random.nextDouble() < 1.0 / 16.0;
        double criticalMultiplier = 1.0;
        if (critical) {
            criticalMultiplier = 1.5;
        }
        int damage = Math.max(1, (int) Math.floor(base * effectiveness * criticalMultiplier));
        return new DamageRoll(damage, critical, effectiveness);
    }
}
