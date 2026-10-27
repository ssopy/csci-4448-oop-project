package oop.pokemon.observer;

import java.util.ArrayList;
import java.util.List;

public class BattleLog implements PokemonObserver {
    private final List<String> entries = new ArrayList<>();

    @Override
    public void onHealthChanged(HealthChangedEvent event) {
        entries.add(String.format("%s: HP %d -> %d/%d",
                event.pokemonName(), event.previousHp(), event.currentHp(), event.maxHp()));
    }

    public List<String> getEntries() {
        return List.copyOf(entries);
    }
}
