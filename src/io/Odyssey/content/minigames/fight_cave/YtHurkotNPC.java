package io.Odyssey.content.minigames.fight_cave;

import io.Odyssey.model.Graphic;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class YtHurkotNPC extends NPC {

    private boolean healingevent;
    private NPC jad;

    public YtHurkotNPC(int npcId, Position position, Player player, NPC jad) {
        super(npcId, position);
        // this.spawnedBy = player.getIndex();
        this.jad = jad;
        this.faceNPC(jad.getIndex());
    }
    private void starthealingevent() {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (jad == null || jad.isDead() || YtHurkotNPC.super.isDead()) {
                    container.stop();
                    return;
                }
                if(  YtHurkotNPC.super.underAttackBy > 0){
                 //   YtHurkotNPC.super.forceChat("I'm under attack! can't heal jad!");
                    return;
                }
                YtHurkotNPC.super.faceNPC(jad.getIndex());
                YtHurkotNPC.super.forceChat("Healing!");
                YtHurkotNPC.super.startAnimation(2639);

                jad.startGraphic(new Graphic(444));
                if(jad.getHealth().getCurrentHealth() < jad.getHealth().getMaximumHealth()){
                    jad.getHealth().increase(5);
                }

            }
        }, 10);
    }

    @Override
    public void process() {

        if(!healingevent){
            healingevent = true;
            starthealingevent();

        }

        super.process();

    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return true;
    }


    @Override
    public boolean canBeDamaged(Entity entity) {

        return true;
    }

    @Override
    public boolean isFreezable() {
        return false;
    }

}


