package oop.pokemon;

import oop.pokemon.battle.AttackResult;
import oop.pokemon.battle.Battle;
import oop.pokemon.battle.DamageCalculator;
import oop.pokemon.factory.JsonPokemonFactory;
import oop.pokemon.factory.PokemonFactory;
import oop.pokemon.model.Pokemon;
import oop.pokemon.observer.BattleLog;
import oop.pokemon.observer.PokemonObserver;
import oop.pokemon.strategy.MoveStrategy;
import oop.pokemon.strategy.RandomMoveStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream resource = Objects.requireNonNull(Main.class.getResourceAsStream("/pokemon.json"),
                "Missing pokemon.json resource");
        PokemonFactory factory;
        try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            factory = new JsonPokemonFactory(reader);
        }

        Pokemon player = factory.createPokemon("Charmander");
        Pokemon cpu = factory.createPokemon("Bulbasaur");
        BattleLog log = new BattleLog();
        PokemonObserver console = event -> System.out.printf("  %s: %d/%d HP%n",
                event.pokemonName(), event.currentHp(), event.maxHp());
        player.addObserver(log);
        cpu.addObserver(log);
        player.addObserver(console);
        cpu.addObserver(console);

        MoveStrategy cpuStrategy = new RandomMoveStrategy(new Random(42));
        Battle battle = new Battle(player, cpu, cpuStrategy, new DamageCalculator(new Random(7)));
        System.out.printf("%s vs. %s%n", player.getName(), cpu.getName());
        int turn = 1;
        while (!battle.isOver()) {
            System.out.println("Turn " + turn++);
            // Use the second move until player input is added.
            for (AttackResult attack : battle.playTurn(player.getMoves().get(1))) {
                System.out.printf("  %s used %s: %d damage (type x%.1f)%s%n",
                        attack.attacker(), attack.move(), attack.damage(), attack.typeMultiplier(),
                        attack.critical() ? " Critical hit!" : "");
            }
        }
        System.out.println("Winner: " + (player.isFainted() ? cpu.getName() : player.getName()));
        System.out.println("Recorded HP changes: " + log.getEntries().size());
    }
}
