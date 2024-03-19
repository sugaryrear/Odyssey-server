package io.Odyssey.content.bosses.AvatarOfCreation;


import io.Odyssey.Server;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.world.objects.GlobalObject;

import static io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.X;


/**
 * Currently only handles spawning 1 NPC but more can be added.
 */

public class AvatarOfCreationSpawner {

    public enum Npcs {

        AvatarOfCreation(io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.NPC_ID, "AvatarOfCreation", 65000, 2, 250, 3000);

        private final int npcId;

        private final String monsterName;

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

    private static NPC AvatarOfCreation; // ideally just leave this in as dead content and swap to a diff script, just have to replace the world even part

    public static void spawnNPC() {
        Npcs npcType = Npcs.AvatarOfCreation;
        Server.getGlobalObjects().add(new GlobalObject(-1, io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.X, io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.Y, 0, 1, 10, -1, -1)); // West - Empty Altar
        AvatarOfCreation = NPCSpawning.spawnNpcOld(io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.NPC_ID, X, io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation.Y, 0, 0, npcType.getHp(), npcType.getMaxHit(), npcType.getAttack(), npcType.getDefence());
    }

    public static void despawn() {
        if (AvatarOfCreation != null) {
            if (AvatarOfCreation.getIndex() > 0) {
                AvatarOfCreation.unregister();
            }
            AvatarOfCreation = null;
        }
    }

    public static boolean isSpawned() {
        return getAvatarOfCreation() != null;
    }

    public static NPC getAvatarOfCreation() {
        return AvatarOfCreation;
    }
}