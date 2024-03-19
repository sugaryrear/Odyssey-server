package io.Odyssey.content.bosses.sarachnis;

import io.Odyssey.Server;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.content.combat.npc.NPCCombatAttackHit;
import io.Odyssey.model.*;
import io.Odyssey.model.entity.EntityReference;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.ClientGameTimer;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SarachnisWeb implements Function<SarachnisNpc, NPCAutoAttack> {

    private static Position[] CORNER_AREAS = {
            new Position(1844,9895),//se corner
            new Position(1835,9895),//sw corner
            new Position(1835,9904),//nw corner
            new Position(1844,9904)//ne corner
    };

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(1)
                .setSpeed(75)
                .setCurve(0)
                .setStartHeight(75)
                .setEndHeight(50)
                .setProjectileId(1687)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(SarachnisNpc nightmare) {
        Consumer<NPCCombatAttackHit> freeze = t -> {
            if (t.getCombatHit().missed())
                return;
            if (!t.getVictim().isFreezable())
                return;
         //   t.getVictim().attackTimer += 6;
            t.getVictim().freezeTimer = 6;
            t.getVictim().resetWalkingQueue();
            t.getVictim().frozenBy = EntityReference.getReference(t.getNpc());//amazing way of referencing an npc!
            if (t.getVictim().isPlayer()) {
                Player p = (Player) t.getVictim();
                ((Player) t.getVictim()).sendMessage("Sarachnis has webbed you in place!");
                ((Player) t.getVictim()).getPA().sendGameTimer(ClientGameTimer.FREEZE, TimeUnit.MILLISECONDS, 600 * 6);
                GlobalObject ground_web = new GlobalObject(34895, p.getX(), p.getY(), p.heightLevel, 2, 10, 5, -1);
                Server.getGlobalObjects().add(ground_web);
            }
            if (nightmare.nextWalk == null) {
                nightmare.nextWalk = CORNER_AREAS[Misc.random(CORNER_AREAS.length - 1)];
            }
            nightmare.resetAttack();
            nightmare.getBehaviour().setAggressive(false);
            nightmare.randomWalk = false;
        };
        Consumer<NPCCombatAttack> hiss = t -> {
            t.getNpc().forceChat("Hsss!");
            nightmare.attackCounter = 0;
        };
        List<Player> players = NPCAutoAttack.getPlayers(nightmare);
        return new NPCAutoAttackBuilder()
                .setSelectPlayersForMultiAttack(new Function<>() {
                    @Override
                    public List<Player> apply(NPCCombatAttack npcCombatAttack) {
                        return players.stream().filter(plr -> Boundary.isIn(plr, Boundary.SARACHNIS_LAIR))
                                .collect(Collectors.toList());
                    }
                })
                .setSelectAutoAttack(new Function<NPCCombatAttack, Boolean>() {
                    @Override
                    public Boolean apply(NPCCombatAttack npcCombatAttack) {
                        return nightmare.attackCounter >= 4;
                    }
                })
                .setAnimation(new Animation(4410))
                .setCombatType(CombatType.SPECIAL)
                .setAttackDelay(5)
                .setHitDelay(3)
                .setDistanceRequiredForAttack(24)
                .setMultiAttack(true)
                .setOnAttack(
                        hiss
                )
                .setOnHit(freeze
                )
                .setProjectile(projectile())
                .createNPCAutoAttack();
    }
}