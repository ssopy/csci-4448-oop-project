package oop.pokemon.observer;

@FunctionalInterface
public interface PokemonObserver {
    void onHealthChanged(HealthChangedEvent event);
}
