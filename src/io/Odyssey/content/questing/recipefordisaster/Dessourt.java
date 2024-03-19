package io.Odyssey.content.questing.recipefordisaster;

import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class Dessourt extends NPC {

    public Dessourt(Position position) {
        super(6372, position);
        getBehaviour().setAggressive(true);
    }
}