package io.Odyssey.content.skills.firemake;


import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ItemAssistant;

import java.util.HashMap;
import java.util.Map;



/**
 * @author Raw Envy
 */

public class LightSources
{
    //	public static void saveBrightness(Player player) {
    //		if (player.brightness == 1) {
    //			brightness1(player);
    //		} else if (player.brightness == 2) {
    //			brightness2(player);
    //		} else if (player.brightness == 4) {
    //			brightness4(player);
    //		} else {
    //			brightness3(player);
    //		}
    //	}
    //
    //	public static void brightness1(Player player) {
    //		player.getPacketSender().sendConfig(505, 1);
    //		player.getPacketSender().sendConfig(506, 0);
    //		player.getPacketSender().sendConfig(507, 0);
    //		player.getPacketSender().sendConfig(508, 0);
    //		player.getPacketSender().sendConfig(166, 1);
    //		player.brightness = 1;
    //	}
    //
    //	public static void brightness2(Player player) {
    //		player.getPacketSender().sendConfig(505, 0);
    //		player.getPacketSender().sendConfig(506, 1);
    //		player.getPacketSender().sendConfig(507, 0);
    //		player.getPacketSender().sendConfig(508, 0);
    //		player.getPacketSender().sendConfig(166, 2);
    //		player.brightness = 2;
    //	}
    //
    //	public static void brightness3(Player player) {
    //		player.getPacketSender().sendConfig(505, 0);
    //		player.getPacketSender().sendConfig(506, 0);
    //		player.getPacketSender().sendConfig(507, 1);
    //		player.getPacketSender().sendConfig(508, 0);
    //		player.getPacketSender().sendConfig(166, 3);
    //		player.brightness = 3;
    //	}
    //
    //	public static void brightness4(Player player) {
    //		player.getPacketSender().sendConfig(505, 0);
    //		player.getPacketSender().sendConfig(506, 0);
    //		player.getPacketSender().sendConfig(507, 0);
    //		player.getPacketSender().sendConfig(508, 1);
    //		player.getPacketSender().sendConfig(166, 4);
    //		player.brightness = 4;
    //	}
    //
    //	public static void setBrightness(Player c) {
    //		if (c.getItemAssistant().playerHasItem(594) || c.getItemAssistant().playerHasItem(32) || c.getItemAssistant().playerHasItem(33)) {
    //			brightness2(c);
    //		} else if (c.getItemAssistant().playerHasItem(4535) || c.getItemAssistant().playerHasItem(4524)) {
    //			brightness3(c);
    //		} else if (c.getItemAssistant().playerHasItem(4550)) {
    //			brightness4(c);
    //		}
    //	}
    public static int[] LITITEMS = {
        594,33,32,4531,4524,4539,4550,4702,9065,5013
    };
    public static final int TINDERBOX = 590;

    /**
     * Enum used for storing the light sources data
     */

    public enum LightSource
    {
        TORCH(1, 596, 594),
        CANDLE(1, 36, 33),
        BLACK_CANDLE(1, 38, 32),
        CANDLE_LANTERN(4, 4529, 4531),
        OIL_LAMP(12, 4522, 4524),
        OIL_LANTERN(26, 4537, 4539),
        BULLSEYE_LANTERN(49, 4548, 4550),
        SAPPHIRE_LANTERN(49, 4701, 4702),
        EMERALD_LANTERN(49, 9064, 9065),
        MINING_HELMET(65, 5014, 5013);

        private int levelReq;
        private int unlitId;
        private int litId;

        private static Map<Integer, LightSource> sources = new HashMap<>();

        static
        {
            for (LightSource s : values())
            {
                sources.put(s.unlitId, s);
                sources.put(s.litId, s);
            }
        }

        /**
         * Enum constructor
         *
         * @Param levelRequirement
         * @Param unlitItemID
         * @Param litItemID
         */
        LightSource(final int levelRequirement, final int unlitItemId, final int litItemId) // Enum constructors always private
        {
            this.levelReq = levelRequirement;
            this.unlitId = unlitItemId;
            this.litId = litItemId;
        }

        public int getlevelReq()
        {
            return levelReq; // Checks the level requirement
        }

        public int getUnlitId()
        {
            return unlitId; // Gets the unlit item Id
        }

        public int getLitId()
        {
            return litId; // Gets the lit item Id
        }

        public static LightSource getSource(int id)
        {
            return sources.get(id);
        }

    }

    /**
     * Method used to light an unlit source. Checks the item used id and
     * lights if its unlit and if player has a tinderbox
     *
     * @Param player
     * @Param itemUsed
     * @Param usedWith
     */
    public static boolean lightSource(Player p, int itemUsed, int usedWith)
    {
        final int item = (itemUsed != TINDERBOX ? itemUsed : usedWith);

        LightSource s = LightSource.getSource(item);

        if (s == null)
        {
            return false;
        }
        if (p.playerLevel[11] < s.getlevelReq()) {
            p.sendMessage("You need a Firemaking level of "+s.getlevelReq()+" to light this.");
            return false;
        }
        p.getItems().replaceItem(p,item,s.getLitId());
        p.sendMessage("You light the " + ItemAssistant.getItemName(item).toLowerCase() + ".");
        if(Boundary.isIn(p, Boundary.FALADOR_MOLE_LAIR)){

                p.getPA().sendFrame99(0);
                p.getPA().walkableInterface(-1);


        }
        return true;
    }

    public static boolean extinguish(Player p, int itemId)
    {

        LightSource s = LightSource.getSource(itemId);

        if (s == null || !p.getItems().playerHasItem(itemId))
        {
            return false;
        }
        p.getItems().replaceItem(p,itemId,s.getUnlitId());


        p.sendMessage("You extinguish the " + ItemAssistant.getItemName(itemId).toLowerCase() + ".");
        if(Boundary.isIn(p, Boundary.FALADOR_MOLE_LAIR)){

            if (! p.getItems().hasItemOnOrInventory( LightSources.LITITEMS)) {
                p.getPA().sendFrame99(2);
                p.getPA().walkableInterface(13583);
                p.sendMessage("You do not have a light source.");
            }

        }

        return true;
    }

}