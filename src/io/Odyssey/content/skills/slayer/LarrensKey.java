package io.Odyssey.content.skills.slayer;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.item.lootable.impl.LarransChest;
import io.Odyssey.model.Items;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class LarrensKey {

    private static final int LARGE_CHEST_OBJECT = 34_832;
    private static final int SMALL_CHEST_OBJECT = 34_831;


    public static void roll(Player player, NPC npc) {
        int rewardAmount = 1;
        if (AvatarOfCreation.activeKeldaSeed) {
            rewardAmount = 2;
        }
        if (Misc.trueRand(110) == 0) {
            player.getEventCalendar().progress(EventChallenge.OBTAIN_X_LARRENS_KEYS, 1);
            Server.itemHandler.createGroundItem(player, Items.LARRANS_KEY, npc.getX(), npc.getY(), npc.getHeight(), rewardAmount);
            //PlayerHandler.executeGlobalMessage("@cr21@ @pur@" + player.playerName + " received a drop: Larran's key from wildy slayer.");
            player.sendMessage("@red@A " + ItemDef.forId(Items.LARRANS_KEY).getName() + " has has dropped.");
        }
    }

    public static boolean clickObject(Player player, WorldObject object) {
        if (object.getId() == LARGE_CHEST_OBJECT) {
            new LarransChest().roll(player);
            return true;
        } else if (object.getId() == SMALL_CHEST_OBJECT) {
            player.sendMessage("Larran's small chest wasn't added, if you feel it should be suggest it on ::discord!");
            return true;
        }

        return false;
    }
}
