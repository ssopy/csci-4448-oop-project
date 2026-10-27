package oop.pokemon.model;

import oop.pokemon.observer.HealthChangedEvent;
import oop.pokemon.observer.PokemonObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pokemon {
    private final PokemonSpecies species;
    private final List<PokemonObserver> observers = new ArrayList<>();
    private int currentHp;

    public Pokemon(PokemonSpecies species) {
        this.species = Objects.requireNonNull(species);
        this.currentHp = species.stats().maxHp();
    }

    public String getName() {
        return species.name();
    }

    public Type getType() {
        return species.type();
    }

    public Stats getStats() {
        return species.stats();
    }

    public List<Move> getMoves() {
        return species.moves();
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public boolean isFainted() {
        return currentHp == 0;
    }

    public void addObserver(PokemonObserver observer) {
        Objects.requireNonNull(observer);
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PokemonObserver observer) {
        observers.remove(observer);
    }

    // Damage is capped at the remaining HP.
    public int takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        int previousHp = currentHp;
        currentHp -= Math.min(damage, currentHp);
        publishHealthChange(previousHp);
        return previousHp - currentHp;
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Healing cannot be negative.");
        }
        int previousHp = currentHp;
        currentHp += Math.min(amount, getStats().maxHp() - currentHp);
        publishHealthChange(previousHp);
    }

    private void publishHealthChange(int previousHp) {
        if (previousHp != currentHp) {
            HealthChangedEvent event = new HealthChangedEvent(getName(), previousHp, currentHp, getStats().maxHp());
            for (PokemonObserver observer : List.copyOf(observers)) {
                observer.onHealthChanged(event);
            }
        }
    }
}
