package io.Odyssey.content.bosses;


import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.model.Items;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.actions.LoadSpell;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.timers.TickTimer;
import io.Odyssey.util.Misc;

import java.util.*;

import static io.Odyssey.util.Misc.rand;

public class GiantMole extends NPC {




    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {


        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(Items.MOLE_CLAW),
                new GameItem(Items.MOLE_SKIN),
                new GameItem(Items.BABY_MOLE)



        ));
    }

    public static ArrayList<GameItem> getRareDrops() {
        ArrayList<GameItem> drops = new ArrayList<>();
        List<GameItem> found = items.get(LootRarity.RARE);
        for(GameItem f : found) {
            boolean foundItem = false;
            for(GameItem drop : drops) {
                if (drop.getId() == f.getId()) {
                    foundItem = true;
                    break;
                }
            }
            if (!foundItem) {
                drops.add(f);
            }
        }
        return drops;
    }



    public GiantMole(int npcId, Position position) {
        super(npcId, position);

    }



    @Override
    public int modifyDamage(Player player, int damage) {
        super.modifyDamage(player, damage);

        return damage;
    }

    @Override
    public void process() {

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



    public static void attack(NPC n) {
        if (n == null || n.isDead) {
            return;
        }
//System.out.println("mole atack");

    }
    public static boolean moleburying(NPC n, Player c) {
        int molerandom = Misc.random(10);
       //System.out.println("molerandom: " + molerandom);

        if (molerandom == 1) {

           c.attacking.reset();
        //  n.resetAttack();
            n.startAnimation(3314);
          c.sendMessage("The mole burrows...");
          n.attackTimer = 10;
            Position randomElement = LoadSpell.mole_burrow_points.get(rand.nextInt(LoadSpell.mole_burrow_points.size()));

            Position burrowDestination = randomElement;
            CycleEventHandler.getSingleton().addEvent(n, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    System.out.println("going invis");
                    n.setInvisible(true);
                    container.stop();
                }

            }, 3); //handles delay between ticks

            CycleEventHandler.getSingleton().addEvent(n, new CycleEvent() {
                @Override
                public void execute(CycleEventContainer container) {
                    if (n.isDead()) {

                        n.teleport(burrowDestination);
                        n.startAnimation(3315);
                        container.stop();
                        return;
                    }

                    n.teleport(burrowDestination);
                    n.setInvisible(false);
                    n.startAnimation(3315);
                    container.stop();
                }

            }, 10); //handles delay between ticks
            return true;
        }


        return false;
    }
}
