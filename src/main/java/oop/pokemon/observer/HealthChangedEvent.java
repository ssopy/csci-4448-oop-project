package oop.pokemon.observer;

public record HealthChangedEvent(String pokemonName, int previousHp, int currentHp, int maxHp) { }
