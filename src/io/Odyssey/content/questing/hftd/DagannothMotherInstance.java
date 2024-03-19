package io.Odyssey.content.questing.hftd;

import io.Odyssey.content.instances.InstanceConfiguration;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.instances.impl.LegacySoloPlayerInstance;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class DagannothMotherInstance extends LegacySoloPlayerInstance {

//    private static final int PLAYER_START_X = 2515, PLAYER_START_Y = 4632;
    private static final int DAG_SPAWN_X = 2516, DAG_SPAWN_Y = 10021;

    private final Player player;

    private final boolean questInstance;

    public DagannothMotherInstance(Player player, boolean questInstance) {
        super(InstanceConfiguration.CLOSE_ON_EMPTY_RESPAWN, player, Boundary.DAGANNOTH_MOTHER_HFTD);
        this.player = player;
        this.questInstance = questInstance;
    }

    public void init() {
        add(player);
       // player.getPA().movePlayer(PLAYER_START_X, PLAYER_START_Y, getHeight());//the dag cave for after quest
        player.getPA().movePlayer(player.absX, player.absY, getHeight());
        spawnDagannothMother();
    }

    public void spawnDagannothMother() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {

                NPC dagannothMother = new DagannothMother(new Position(DAG_SPAWN_X, DAG_SPAWN_Y, getHeight()));
                dagannothMother.getBehaviour().setRespawn(!questInstance);
                dagannothMother.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);//stupid stupid stupid. just says to respawn it if you ARE NOT on the quest? wont happen. lol.
                add(dagannothMother);
                container.stop();
            }
        }, 4);
    }

}
