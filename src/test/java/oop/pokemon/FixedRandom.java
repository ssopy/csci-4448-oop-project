package oop.pokemon;

import java.util.Random;

public class FixedRandom extends Random {
    private final double value;

    public FixedRandom(double value) {
        super(0);
        this.value = value;
    }

    @Override
    public double nextDouble() {
        return value;
    }
}
