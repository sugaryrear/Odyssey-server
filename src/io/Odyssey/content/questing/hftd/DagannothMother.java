package io.Odyssey.content.questing.hftd;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class DagannothMother extends NPC {

    public static final int AIR_PHASE = 983;
    public static final int WATER_PHASE = 984;
    public static final int FIRE_PHASE = 985;
    public static final int EARTH_PHASE = 986;
    public static final int RANGE_PHASE = 987;
    public static final int MELEE_PHASE = 988;

    public static final int[] DAGANNOTH_MOTHER_TRANSFORMS = {AIR_PHASE, WATER_PHASE, FIRE_PHASE, EARTH_PHASE, RANGE_PHASE, MELEE_PHASE};


    private int transformIndex = 0;

    public DagannothMother(Position position) {
        super(DAGANNOTH_MOTHER_TRANSFORMS[0], position);
        getBehaviour().setAggressive(true);

        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                transformIndex = (transformIndex + 1) % DagannothMother.DAGANNOTH_MOTHER_TRANSFORMS.length;// a way to go by order.
                requestTransform(DagannothMother.DAGANNOTH_MOTHER_TRANSFORMS[transformIndex]);
            }
        }, 30);//switches every 18 seconds =   18/0.6   =30
    }
}
