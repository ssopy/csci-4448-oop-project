package oop.pokemon.model;

import java.util.List;

public record PokemonSpecies(String name, Type type, Stats stats, List<Move> moves) {
    public PokemonSpecies {
        if (name == null || name.isBlank() || type == null || stats == null
                || moves == null || moves.isEmpty() || moves.size() > 4) {
            throw new IllegalArgumentException("A species needs a name, type, stats, and 1–4 moves.");
        }
        for (Move move : moves) {
            if (move == null) {
                throw new IllegalArgumentException("A species cannot have a null move.");
            }
        }
        moves = List.copyOf(moves);
    }
}
