package io.Odyssey.content.bosses;


import io.Odyssey.Server;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;


/**
 * Currently only handles spawning 1 NPC but more can be added.
 */

public class AngelofdeathSpawner {

    public enum Npcs { /// nevermind thought I forgot to add it to vps

        angelOfDeath(11282
                , "Angelofdeath worlboss", 3400, 30, 250, 30);

        private final int npcId;

        private final String monsterName; // u trying to find the spawner ?

        private final int hp;

        private final int maxHit;

        private final int attack;

        private final int defence;

        Npcs(final int npcId, final String monsterName, final int hp, final int maxHit, final int attack, final int defence) {
            this.npcId = npcId;
            this.monsterName = monsterName;
            this.hp = hp;
            this.maxHit = maxHit;
            this.attack = attack;
            this.defence = defence;
        }

        public int getNpcId() {
            return npcId;
        }

        public String getMonsterName() {
            return monsterName;
        }

        public int getHp() {
            return hp;
        }

        public int getMaxHit() {
            return maxHit;
        }

        public int getAttack() {
            return attack;
        }

        public int getDefence() {
            return defence;
        }
    }

    /**
     * The spawnNPC method which handles the spawning of the NPC and the global
     * message sent.
     *
     * @param c
     */

    private static Position angleOfDeathCoords = new Position(2925, 5203, 0);

    private static NPC angel; // ideally just leave this in as dead content and swap to a diff script, just have to replace the world even part

    public static void spawnNPC() {
        Npcs npcType = Npcs.angelOfDeath; // where you want the nex to spawn,, here is fine ill make a custom map for it this si avatar of creation spot
     angel = NPCSpawning.spawnNpcOld(11670, angleOfDeathCoords.getX(), angleOfDeathCoords.getY(), angleOfDeathCoords.getHeight(), 0, npcType.getHp(), npcType.getMaxHit(), npcType.getAttack(), npcType.getDefence());

    }

    public static void despawn() {
        if (angel != null) {
            if (angel.getIndex() > 0) {
                angel.unregister();
            }
            angel = null; // should work if I.D. is diff right , like not load the script ?
        }
    }

    public static boolean isSpawned() {
        return getAngelofdeaTh() != null;
    }

    public static NPC getAngelofdeaTh() {
        return angel;
    }
}