package io.Odyssey.content.questing.recipefordisaster;


import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class Agrithnana extends NPC {

    public Agrithnana(Position position) {
        super(6369, position);
        getBehaviour().setAggressive(true);
    }
}
