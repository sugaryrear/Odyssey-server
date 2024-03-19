package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.core.HitDispatcher;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.AnimationLength;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

public class AnicentGodSword extends Special {
//    player anim   7640
//    gfx 1996
//
//    second /combat delay between 1st and 2nd hit             npc gfx 2003

    public AnicentGodSword() {
        super(5.0, 2.0, 1.375, new int[] { 26233 });
    }
//    public AncientGodsword() {
//        super(5.0, 1.00, 1.00, new int[] { 26233 });
//    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        player.startAnimation(9171);
        player.gfx0(1996);
        //player.gfx0(2363);
        //damage.setAmount((MeleeCombatFormula.get().getMaxHit(player, player) * Misc.random((int) 0.5, (int) 1.5)));
        //int damage2 = damage.getAmount();
        //if (damage.getAmount() > 0) {
        // player.getDamageQueue().add(new Damage(target, damage.setAmount(25), 8, player.playerEquipment, Hitmark.HIT, CombatType.MAGE));
        // player.getPA().addXpDrop(new PlayerAssistant.XpDrop(damage2, Skill.ATTACK.getId()));
        // }
    }
    @Override
    public void hit(Player player, Entity target, Damage damage) {
        player.getDamageQueue().add(new Damage(target, damage.setAmount(25), 8, player.playerEquipment, Hitmark.HIT, CombatType.MELEE));
        if (target instanceof NPC) {
            ((NPC) target).gfx0(2003);
        } else if (target instanceof Player) {
            ((Player) target).gfx0(2003);
        }
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
//                if (c.absX == 2272 && c.absY == 4061) {
//                    c.facePosition(2272, 4062);
//                    c.startAnimation(827);
//                    Vorkath.poke(c, n);
//                    container.stop();
//                }
                player.getHealth().increase(15);
                container.stop();
            }

        }, 8);
    }



}
