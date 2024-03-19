package io.Odyssey.content.questing.recipefordisaster;

import io.Odyssey.content.questing.hftd.DagannothMother;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Position;

public class GellatinothMother extends NPC {

    private int transformIndex = 0;

    public static final int AIR_PHASE = 6373;
    public static final int WATER_PHASE = 6375;
    public static final int FIRE_PHASE = 6376;
    public static final int EARTH_PHASE = 6378;
    public static final int RANGE_PHASE = 6377;
    public static final int MELEE_PHASE = 6374;

    public static final int[] DAGANNOTH_MOTHER_TRANSFORMS = {MELEE_PHASE, AIR_PHASE,WATER_PHASE, FIRE_PHASE, EARTH_PHASE, RANGE_PHASE};

    public GellatinothMother(Position position) {
        super(6373, position);
        getBehaviour().setAggressive(true);

        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                transformIndex = (transformIndex + 1) % DAGANNOTH_MOTHER_TRANSFORMS.length;// a way to go by order.
                requestTransform(DAGANNOTH_MOTHER_TRANSFORMS[transformIndex]);
            }
        }, 30);//switches every 18 seconds =   18/0.6   =30


    }
}