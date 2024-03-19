package io.Odyssey.content.questing.recipefordisaster;


import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class Flambeed extends NPC {

    public Flambeed(Position position) {
        super(6370, position);
        getBehaviour().setAggressive(true);
    }
}