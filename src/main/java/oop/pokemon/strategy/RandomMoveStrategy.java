package oop.pokemon.strategy;

import oop.pokemon.model.Move;
import oop.pokemon.model.Pokemon;

import java.util.Objects;
import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {
    private final Random random;

    public RandomMoveStrategy(Random random) {
        this.random = Objects.requireNonNull(random);
    }

    @Override
    public Move chooseMove(Pokemon self, Pokemon opponent) {
        if (self.isFainted()) {
            throw new IllegalStateException("A fainted Pokemon cannot choose a move.");
        }
        return self.getMoves().get(random.nextInt(self.getMoves().size()));
    }
}
