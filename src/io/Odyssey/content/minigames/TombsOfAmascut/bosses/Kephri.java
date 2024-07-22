package io.Odyssey.content.minigames.TombsOfAmascut.bosses;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutBoss;
import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.model.Animation;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.ProjectileBaseBuilder;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.util.Misc;

public class Kephri extends TombsOfAmascutBoss {

    private static final int[] GREEN_SPLAT_STILL_GFX = {2112};

    public Kephri(InstancedArea instancedArea) {
        super(Npcs.KEPHRI, new Position(3549, 5406, instancedArea.getHeight()), instancedArea);
        setNpcAutoAttacks(Lists.newArrayList(
            new NPCAutoAttackBuilder()
                    .setAnimation(new Animation(8059))
                    .setMaxHit(0)
                    .setAttackDamagesPlayer(false)
                    .setDistanceRequiredForAttack(18)
                    .setOnAttack(this::sendSplatProjectile)
                    .createNPCAutoAttack()
        ));
    }

    private void sendSplatProjectile(NPCCombatAttack npcCombatAttack) {
        Position position = npcCombatAttack.getVictim().getPosition();
        new ProjectileBaseBuilder().setProjectileId(2167).setSendDelay(1).createProjectileBase()
                .createTargetedProjectile(npcCombatAttack.getNpc(), position).send(getInstance());

        // Cycle event to handle pool damage
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (isDead() || !isRegistered()) {
                    container.stop();
                    return;
                }

                if (container.getTotalExecutions() == 2) {
                    Server.playerHandler.sendStillGfx(new StillGraphic(GREEN_SPLAT_STILL_GFX[Misc.trueRand(GREEN_SPLAT_STILL_GFX.length)], 0, position), getInstance());
               //player.getPA().stillGfx(1177, itemX, itemY, height, 5);
                }

                if (container.getTotalExecutions() == 18) {
                    container.stop();
                } else if (container.getTotalExecutions() >= 2) {
                    getInstance().getPlayers().stream().filter(plr -> plr.getPosition().equals(position)).forEach(plr ->
                                    plr.appendDamage(6 + Misc.random(10), Hitmark.POISON));
                }
            }
        }, 1);
    }

    @Override
    public int getDeathAnimation() {
        return 8063;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        TombsOfAmascutInstance instance = (TombsOfAmascutInstance) super.getInstance();
        instance.getNpcs().clear();
        instance.getPlayers().forEach(plr2 -> {

            CycleEventHandler.getSingleton().addEvent(plr2, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    instance.moveToNextRoom(plr2);
                    container.stop();

                }
                @Override
                public void onStopped() {

                }
            }, 3);
        });


    }

}
