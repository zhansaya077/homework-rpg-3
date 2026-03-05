
package com.narxoz.rpg.battle;

import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {

        EncounterResult result = new EncounterResult();
        int rounds = 0;

        while (hasAlive(teamA) && hasAlive(teamB)) {
            rounds++;

            attack(teamA, teamB, result);
            if (!hasAlive(teamB)) break;

            attack(teamB, teamA, result);
        }

        result.setRounds(rounds);

        if (hasAlive(teamA)) {
            result.setWinner("Heroes");
        } else {
            result.setWinner("Enemies");
        }

        return result;
    }

    private void attack(List<Combatant> attackers, List<Combatant> defenders, EncounterResult result) {
        for (Combatant attacker : attackers) {

            if (!attacker.isAlive()) continue;

            Combatant target = getRandomAlive(defenders);
            if (target == null) return;

            int damage = attacker.getAttackPower();
            target.takeDamage(damage);

            result.addLog(attacker.getName() + " attacks " + target.getName() + " for " + damage + " damage");

            if (!target.isAlive()) {
                result.addLog(target.getName() + " is defeated");
            }
        }
    }

    private boolean hasAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) return true;
        }
        return false;
    }

    private Combatant getRandomAlive(List<Combatant> team) {
        for (Combatant c : team) {
            if (c.isAlive()) return c;
        }
        return null;
    }
}
