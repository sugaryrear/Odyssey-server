package io.Odyssey.content.questing.MonkeyMadness;

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

public class MMDemonInstance extends LegacySoloPlayerInstance {

    private static final int PLAYER_START_X = 2655, PLAYER_START_Y = 4636;
    private static final int DEMON_SPAWN_X = 2655, DEMON_SPAWN_Y = 4640;
    private static final int HEIGHT_LEVEL = 0;

    private final Player player;

    private final boolean questInstance;

    public MMDemonInstance(Player player, boolean questInstance) {
        super(InstanceConfiguration.CLOSE_ON_EMPTY_RESPAWN, player, Boundary.MONKEY_MADNESS_DEMON);
        this.player = player;
        this.questInstance = questInstance;
    }
    @Override
    public boolean handleDeath(Player player) {

        player.getPA().movePlayer(2806,2761, 0);
        player.nextChat = 0;
        return true;
    }
    public void init() {
        player.getPA().closeAllWindows();
        player.getPA().movePlayer(PLAYER_START_X, PLAYER_START_Y, resolveHeight(HEIGHT_LEVEL));
        add(player);
        spawnMMDemon();
    }

    public void spawnMMDemon() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC mmDemon = new MMDemon(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                mmDemon.getBehaviour().setRespawn(!questInstance);
                mmDemon.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(mmDemon);
                container.stop();
            }
        }, 6);
    }

}
