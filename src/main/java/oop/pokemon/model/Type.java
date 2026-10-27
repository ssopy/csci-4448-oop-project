package oop.pokemon.model;

public enum Type {
    NORMAL, FIRE, WATER, GRASS;

    public double multiplierAgainst(Type defender) {
        return switch (this) {
            case NORMAL -> 1.0;
            case FIRE -> switch (defender) {
                case GRASS -> 2.0;
                case FIRE, WATER -> 0.5;
                default -> 1.0;
            };
            case WATER -> switch (defender) {
                case FIRE -> 2.0;
                case WATER, GRASS -> 0.5;
                default -> 1.0;
            };
            case GRASS -> switch (defender) {
                case WATER -> 2.0;
                case FIRE, GRASS -> 0.5;
                default -> 1.0;
            };
        };
    }
}
