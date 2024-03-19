package io.Odyssey.content.questing.MonkeyMadness;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class MMDemon extends NPC {

    public MMDemon(Position position) {
        super(1443, position);
        getBehaviour().setAggressive(true);
    }
}
