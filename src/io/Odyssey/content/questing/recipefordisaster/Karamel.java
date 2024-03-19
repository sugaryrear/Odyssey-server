package io.Odyssey.content.questing.recipefordisaster;

import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class Karamel extends NPC {

    public Karamel(Position position) {
        super(6371, position);
        getBehaviour().setAggressive(true);
    }
}
