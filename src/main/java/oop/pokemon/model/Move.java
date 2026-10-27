package oop.pokemon.model;

public record Move(String name, Type type, int power) {
    public Move {
        if (name == null || name.isBlank() || type == null || power <= 0) {
            throw new IllegalArgumentException("A move needs a name, supported type, and positive power.");
        }
    }
}
