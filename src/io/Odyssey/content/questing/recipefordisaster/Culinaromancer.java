package io.Odyssey.content.questing.recipefordisaster;

import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class Culinaromancer extends NPC {

    public Culinaromancer(Position position) {
        super(4878, position);
        getBehaviour().setAggressive(true);
    }
}