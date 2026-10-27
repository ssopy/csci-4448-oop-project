package oop.pokemon;

import oop.pokemon.factory.JsonPokemonFactory;
import oop.pokemon.factory.PokemonFactory;
import oop.pokemon.model.Pokemon;
import oop.pokemon.model.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class JsonPokemonFactoryTest {
    private static final String SPECIES = """
            {"name":"Testmon","type":"FIRE",
             "stats":{"maxHp":100,"attack":50,"defense":50,"speed":50},
             "moves":[{"name":"Ember","type":"FIRE","power":40}]}
            """;

    @Test
    void testCreatePokemonFromJson() throws Exception {
        try (InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(
                getClass().getResourceAsStream("/pokemon.json")), StandardCharsets.UTF_8)) {
            PokemonFactory factory = new JsonPokemonFactory(reader);
            Pokemon first = factory.createPokemon("Charmander");
            Pokemon second = factory.createPokemon(" charmander ");
            assertEquals(Type.FIRE, first.getType());
            assertEquals("Ember", first.getMoves().get(1).name());
            assertEquals(Type.WATER, factory.createPokemon("Squirtle").getType());
            assertEquals(Type.GRASS, factory.createPokemon("Bulbasaur").getType());
            first.takeDamage(30);
            assertNotSame(first, second);
            assertEquals(90, first.getCurrentHp());
            assertEquals(120, second.getCurrentHp());
        }
    }

    @Test
    void testUnknownSpecies() {
        PokemonFactory factory = new JsonPokemonFactory(new StringReader("[" + SPECIES + "]"));
        assertThrows(IllegalArgumentException.class, () -> factory.createPokemon("Missingno"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"[]", "null", "[null]", "{broken json", "{}"})
    void testInvalidRoster(String json) {
        assertThrows(IllegalArgumentException.class, () -> new JsonPokemonFactory(new StringReader(json)));
    }

    @Test
    void testDuplicateSpecies() {
        assertThrows(IllegalArgumentException.class, () ->
                new JsonPokemonFactory(new StringReader("[" + SPECIES + "," + SPECIES + "]")));
    }

    @Test
    void testInvalidStatsAndTypes() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new JsonPokemonFactory(
                        new StringReader("[" + SPECIES.replace("\"maxHp\":100", "\"maxHp\":0") + "]"))),
                () -> assertThrows(IllegalArgumentException.class, () -> new JsonPokemonFactory(
                        new StringReader("[" + SPECIES.replace("FIRE", "UNKNOWN") + "]")))
        );
    }
}
