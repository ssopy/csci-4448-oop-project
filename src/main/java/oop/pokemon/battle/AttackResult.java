package oop.pokemon.battle;

public record AttackResult(String attacker, String defender, String move,
                           int damage, boolean critical, double typeMultiplier) { }
