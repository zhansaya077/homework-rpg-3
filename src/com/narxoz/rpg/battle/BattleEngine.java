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
        random = new Random(1L);
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {

        EncounterResult result = new EncounterResult();
        int rounds = 0;

        while (!teamA.isEmpty() && !teamB.isEmpty()) {
            rounds++;
            result.addLog("=== Round " + rounds + " ===");

            attackPhase(teamA, teamB, result);
            attackPhase(teamB, teamA, result);
        }

        if (teamA.isEmpty()) {
            result.setWinner("Team B");
        } else {
            result.setWinner("Team A");
        }

        result.setRounds(rounds);
        return result;
    }

    private void attackPhase(List<Combatant> attackers,
                             List<Combatant> defenders,
                             EncounterResult result) {

        for (Combatant attacker : attackers) {

            if (defenders.isEmpty()) {
                return;
            }

            Combatant target = defenders.get(0);

            int damage = attacker.getAttackPower();

            target.takeDamage(damage);

            result.addLog(attacker.getName() +
                    " attacks " +
                    target.getName() +
                    " for " + damage + " damage");

            if (!target.isAlive()) {
                result.addLog(target.getName() + " has died!");
                defenders.remove(0);
            }
        }
    }
}
