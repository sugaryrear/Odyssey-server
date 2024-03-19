package io.Odyssey.content.questing.recipefordisaster;


import io.Odyssey.content.instances.InstanceConfiguration;
import io.Odyssey.content.instances.impl.LegacySoloPlayerInstance;
import io.Odyssey.content.questing.MonkeyMadness.MMDemon;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import org.apache.commons.lang3.Range;

public class RFDInstance extends LegacySoloPlayerInstance {

    private static final int PLAYER_START_X = 1901, PLAYER_START_Y = 5357;
    private static final int DEMON_SPAWN_X = 1903, DEMON_SPAWN_Y = 5356;
    private static final int HEIGHT_LEVEL = 0;

    private final Player player;

    private final boolean questInstance;


    @Override
    public boolean handleDeath(Player player) {

        player.getPA().movePlayer(1862,5317, 0);
        player.nextChat = 0;
        return true;
    }
    public RFDInstance(Player player, boolean questInstance) {
        super(InstanceConfiguration.CLOSE_ON_EMPTY_RESPAWN, player, Boundary.RFD_FIGHT_AREA);
        this.player = player;
        this.questInstance = questInstance;
    }

    public void init() {
        player.getPA().closeAllWindows();
        player.getPA().movePlayer(PLAYER_START_X, PLAYER_START_Y, resolveHeight(HEIGHT_LEVEL));
        add(player);
        spawnagrithnana();
    }

    public void spawnagrithnana() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC agrithnana = new Agrithnana(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                agrithnana.getBehaviour().setRespawn(!questInstance);
                agrithnana.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(agrithnana);
                container.stop();
            }
        }, 6);
    }
public void spawnnext(int npcid){//the npcid of the npc killed
        if(npcid == 6369){
            spawnflambeed();
        }
    if(npcid == 6370){
        spawnkaramel();
    }
    if(npcid == 6371){
        spawndessourt();
    }
    if(npcid == 6372){
        spawngelatinnothmother();
    }
    if(npcid == 6373){
        spawnculinaromancer();
    }

}
    public void spawnflambeed() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC flambeed = new Flambeed(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                flambeed.getBehaviour().setRespawn(!questInstance);
                flambeed.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(flambeed);
                container.stop();
            }
        }, 6);
    }

    public void spawnkaramel() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC flambeed = new Karamel(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                flambeed.getBehaviour().setRespawn(!questInstance);
                flambeed.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(flambeed);
                container.stop();
            }
        }, 6);
    }

    public void spawndessourt() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC flambeed = new Dessourt(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                flambeed.getBehaviour().setRespawn(!questInstance);
                flambeed.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(flambeed);
                container.stop();
            }
        }, 6);
    }
    public void spawngelatinnothmother() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC mother = new GellatinothMother(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                mother.getBehaviour().setRespawn(!questInstance);
                mother.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(mother);
                container.stop();
            }
        }, 6);
    }
    public void spawnculinaromancer() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                NPC flambeed = new Culinaromancer(new Position(DEMON_SPAWN_X, DEMON_SPAWN_Y, resolveHeight(HEIGHT_LEVEL)));
                flambeed.getBehaviour().setRespawn(!questInstance);
                flambeed.getBehaviour().setRespawnWhenPlayerOwned(!questInstance);
                add(flambeed);
                container.stop();
            }
        }, 6);
    }
}
