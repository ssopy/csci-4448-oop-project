package oop.pokemon.strategy;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;

@FunctionalInterface
public interface MoveStrategy {
    Move chooseMove(Pokemon self, Pokemon opponent);
}
