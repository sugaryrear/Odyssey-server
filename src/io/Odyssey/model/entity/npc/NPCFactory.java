package io.Odyssey.model.entity.npc;

import java.util.Arrays;

import io.Odyssey.content.questing.hftd.DagannothMother;
import io.Odyssey.content.questing.recipefordisaster.GellatinothMother;
import io.Odyssey.model.entity.player.Position;

public class NPCFactory {

    public static NPC create(NPC oldInstance, int npcIndex, int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit) {
        NPC npc = oldInstance.provideRespawnInstance();
        if (npc == null) {
            if (Arrays.stream(DagannothMother.DAGANNOTH_MOTHER_TRANSFORMS).anyMatch(dagId -> dagId == npcType)) {//because look you can get a "private" dag mother instance where she respawns so this is important otherwise fk it
                npc = new DagannothMother(new Position(x, y, heightLevel));
                NPCSpawning.finishNpcConstruction(npc, WalkingType, maxHit);
            } else  if (Arrays.stream(GellatinothMother.DAGANNOTH_MOTHER_TRANSFORMS).anyMatch(dagId -> dagId == npcType)) {//because look you can get a "private" dag mother instance where she respawns so this is important otherwise fk it
                npc = new GellatinothMother(new Position(x, y, heightLevel));
                NPCSpawning.finishNpcConstruction(npc, WalkingType, maxHit);
            } else {
                npc = NPCSpawning.newNPC(npcIndex, npcType, x, y, heightLevel, WalkingType, maxHit);
            }
        } else {
            NPCSpawning.finishNpcConstruction(npc, WalkingType, maxHit);
        }

        return npc;
    }

}
