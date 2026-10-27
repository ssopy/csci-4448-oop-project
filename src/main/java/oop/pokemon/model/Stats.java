package oop.pokemon.model;

public record Stats(int maxHp, int attack, int defense, int speed) {
    public Stats {
        if (maxHp <= 0 || attack <= 0 || defense <= 0 || speed <= 0) {
            throw new IllegalArgumentException("All stats must be positive.");
        }
    }
}
