package io.Odyssey.content.item.lootable;

import java.util.*;

import io.Odyssey.model.Items;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.definitions.ShopDef;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;

public abstract class MysteryBoxLootable implements Lootable {

    public abstract int getItemId();

    /**
     * The player object that will be triggering this event
     */
    private final Player player;

    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public MysteryBoxLootable(Player player) {
        this.player = player;
    }

    /**
     * Can the player open the mystery box
     */
    public boolean canMysteryBox = true;

    /**
     * The prize received
     */
    private int mysteryPrize;

    private int mysteryAmount;

    private int spinNum;

    /**
     * The chance to obtain the item
     */
    private int random;
    private boolean active;
    private final int INTERFACE_ID = 47000;
    private final int ITEM_FRAME = 47101;

    public boolean isActive() {
        return active;
    }

    public void spin() {

        if (!canMysteryBox) {
            player.sendMessage("Please finish your current spin.");
            return;
        }
        if (!player.getItems().playerHasItem(getItemId())) {
            player.sendMessage("You require a mystery box to do this.");
            return;
        }

        player.getItems().deleteItem(getItemId(), 1);

        player.sendMessage(":resetBox:");
        for (int i=0; i<66; i++){
            player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
        }
        spinNum = 0;
        player.sendMessage(":spin:");
        process();
    }
    private static Map<Rarity, List<GameItem>> items = new HashMap<>();


    /**
     * The rarity of the reward
     */
    private Rarity rewardRarity;

    /**
     * Represents the rarity of a certain list of items
     */
    enum Rarity {
        UNCOMMON("<col=005eff>"),
        COMMON("<col=336600>"),
        RARE("<col=B80000>");

        private String color;

        Rarity(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public static Rarity forId(int id) {
            for (Rarity tier : Rarity.values()) {
                if (tier.ordinal() == id)
                    return tier;
            }
            return null;
        }
    }
    public void loadloot() {
        items.clear();
        if(getItemId() == 6199){
            items.put(Rarity.COMMON, //50% chance
                    Arrays.asList(
                            new GameItem(990, 15),//crystal key
                            new GameItem(268, 150),//dwarf weed
                            new GameItem(270, 150),//torstol
                            new GameItem(3001, 150),//snapdragon
                            new GameItem(12696, 150),//super combat potion(4)
                            new GameItem(3025, 200),//super restore(4)
                            new GameItem(11937, 500),//dark crab
                            new GameItem(13442, 500),//anglerfish
                            new GameItem(3145, 500),//cooked karambwan
                            new GameItem(6570),//fire cape
                            new GameItem(535, 500),//babydragon bones
                            new GameItem(11235),//dark bow
                            new GameItem(4151),//abyssal whip
                            new GameItem(12873),//guthan set
                            new GameItem(12875),//verac set
                            new GameItem(12877),//dharok set
                            new GameItem(12879),//torags set
                            new GameItem(12881),//ahrim set
                            new GameItem(12883)//karil set

                    ));

            items.put(Rarity.UNCOMMON,//40% Chance
                    Arrays.asList(

                            new GameItem(11840),//dragon boots
                            new GameItem(11836),//bandos boots
                            new GameItem(6585),//amulet of fury
                            new GameItem(6737),//berserker ring
                            new GameItem(6889),//mages book
                            new GameItem(12873),//guthan set
                            new GameItem(12875),//verac set
                            new GameItem(12877),//dharok set
                            new GameItem(12879),//torags set
                            new GameItem(12881),//ahrim set
                            new GameItem(12883),//karil set
                            new GameItem(537, 250),//dragon bones
                            new GameItem(2364, 300),//runite bar
                            new GameItem(1514, 500),// magic logs
                            new GameItem(1632, 250),//uncut dragonstone
                            new GameItem(2577),//ranger boots
                            new GameItem(12596),//rangers tunic
                            new GameItem(11920),//dragon pickaxe
                            new GameItem(6739),//dragon axe
                            new GameItem(6733),//archers ring
                            new GameItem(6731),//seers ring
                            new GameItem(6735)//warrior ring
                    )
            );

            items.put(Rarity.RARE,//8% chance
                    Arrays.asList(
                            new GameItem(12002),//occult necklace
                            new GameItem(11804),//BGS
                            new GameItem(11806),//SGS
                            new GameItem(11808),//ZGS
                            new GameItem(11832),//bandos chestplate
                            new GameItem(11834),//bandos tassets

                            new GameItem(11826),//armadyl helmet
                            new GameItem(11828),//armadyl chestplate
                            new GameItem(11830),//armadyl chainskirt
                            new GameItem(10346),//3rd age platelegs
                            new GameItem(10348),//3rd age platebody
                            new GameItem(10350),//3rd age helm
                            new GameItem(10352),//3rd age kite
                            new GameItem(10330),//3rd age range top
                            new GameItem(10332),//3rd age range bottom
                            new GameItem(10338),//3rd age robe top
                            new GameItem(10340),//3rd age robe bottom
                            new GameItem(10334),//3rd age range coif
                            new GameItem(10342),//3rd age mage hat
                            new GameItem(10344)));//3rd age amulet

        } else if (getItemId() == 787){
            items.put(Rarity.COMMON, //50% chance
                    Arrays.asList(     new GameItem(3050, 5, 7), //grimy lantadyme
                            new GameItem(212, 5, 7), //grimy dwarf weed
                            new GameItem(214, 5, 7), //grimy torstol
                            new GameItem(3052, 5, 7),  //lantadyme potion (unf)
                            new GameItem(216, 5, 7),  //dwarf weed potion (unf)
                            new GameItem(3003, 5, 7), //torstol potion (unf)
                            new GameItem(454, 7, 10), //coal
                            new GameItem(448, 7, 10), //mithril ore
                            new GameItem(450, 7, 10), //adamantite ore
                            new GameItem(452, 7, 10),  //runite ore
                            new GameItem(2360,2, 5),  //mithril bar
                            new GameItem(2362,2, 5), //adamantite bar
                            new GameItem(2364,2, 5), //runite bar
                            new GameItem(1516, 7, 10),  //yew logs
                            new GameItem(1514, 7, 10),  //magic logs
                            new GameItem(19670, 7, 10),  //redwood logs

                            new GameItem(Items.LIMPWURT_ROOT_NOTED, 5, 10),
                            new GameItem(Items.RED_SPIDERS_EGGS_NOTED, 10, 15),
                            new GameItem(Items.MORT_MYRE_FUNGUS_NOTED, 10, 15),
                            new GameItem(Items.CRUSHED_NEST_NOTED, 2, 4)));

            items.put(Rarity.UNCOMMON,//40% Chance
                    Arrays.asList(
                            new GameItem(1620, 5, 7), //uncut ruby
                            new GameItem(1618, 5, 7), //uncut diamond
                            new GameItem(1632, 5, 7), //uncut dragonstone
                            new GameItem(7945, 7, 10),  //raw monkfish
                            new GameItem(3143, 7, 10), //raw karamwban
                            new GameItem(384, 7, 10),  //raw shark
                            new GameItem(390, 7, 10)) //raw manta ray

            );

            items.put(Rarity.RARE,//8% chance
                    Arrays.asList(
                            new GameItem(1620, 5, 7), //uncut ruby
                            new GameItem(1618, 5, 7), //uncut diamond
                            new GameItem(1632, 5, 7), //uncut dragonstone
                            new GameItem(7945, 7, 10),  //raw monkfish
                            new GameItem(3143, 7, 10), //raw karamwban
                            new GameItem(384, 7, 10),  //raw shark
                            new GameItem(390, 7, 10))); //raw manta ray
        }
    }
    public void process() {

        canMysteryBox = false;
        active = true;
//System.out.println("box id: "+getItemId());
        random = Misc.random(100);
        List<GameItem> itemList = random < 50 ? items.get(Rarity.COMMON) : random >= 50 && random <= 91 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
       //System.out.println("list: "+itemList.size());
        rewardRarity = random < 50 ? Rarity.COMMON : random >= 50 && random <= 91 ? Rarity.UNCOMMON : Rarity.RARE;

        GameItem item = Misc.getRandomItem(itemList);

        mysteryPrize = item.getId();
        // System.out.println("item: "+mysteryPrize);
        mysteryAmount = item.getAmount();


        if (spinNum == 0) {
            for (int i=0; i<66; i++){
                Rarity notPrizeRarity = Rarity.values()[new Random().nextInt(Rarity.values().length)];
                GameItem NotPrize =Misc.getRandomItem(items.get(notPrizeRarity));
                final int NOT_PRIZE_ID = NotPrize.getId();
                final int NOT_PRIZE_AMOUNT = NotPrize.getAmount();
                sendItem(i, 55, mysteryPrize, NOT_PRIZE_ID,NOT_PRIZE_AMOUNT);
            }
        } else {
            for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
                Rarity notPrizeRarity = Rarity.values()[new Random().nextInt(Rarity.values().length)];
                final int NOT_PRIZE_ID = Misc.getRandomItem(items.get(notPrizeRarity)).getId();
                sendItem(i, (spinNum+1)*50 + 5, mysteryPrize, NOT_PRIZE_ID, mysteryAmount);
            }
        }
        spinNum++;

    }
    public void reward() {
        String name = ItemDef.forId(mysteryPrize).isNoted()  ? ItemDef.forId(mysteryPrize-1).getName() : ItemDef.forId(mysteryPrize).getName();
        player.sendMessage("You've won a <col=A10081>" + name + "</col> from the <col=A10081>" + ItemDef.forId(getItemId()).getName() + "</col>.");
        //player.getItems().addItem(ItemDef.forId(mysteryPrize).isNoted()  ? mysteryPrize-1 : mysteryPrize, mysteryAmount);
        player.getItems().addItem(mysteryPrize, mysteryAmount);
        mysteryPrize = -1;
        active = false;
        canMysteryBox  = true;
    }
    public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID, int amount) {
        if (i == prizeSlot) {
            player.getPA().mysteryBoxItemOnInterface(PRIZE_ID, amount, ITEM_FRAME, i);
        }
        else {
            player.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, amount, ITEM_FRAME, i);
        }
    }
    public void setMysteryPrize() {
        random = Misc.random(100);
        List<GameItem> itemList = random < 50 ? getLoot().get(MysteryBoxRarity.COMMON.getLootRarity()) : random >= 50
                && random <= 85 ? getLoot().get(MysteryBoxRarity.UNCOMMON.getLootRarity())
                : getLoot().get(MysteryBoxRarity.RARE.getLootRarity());
        GameItem item = Misc.getRandomItem(itemList);
        mysteryPrize = item.getId();
        mysteryAmount = item.getAmount();

    }

    public void sendItem(int slot, int prizeSlot, int prize, int other_prizes) {
        if (slot == prizeSlot) {

            player.getPA().mysteryBoxItemOnInterface(prize, 1, ITEM_FRAME, slot);

        } else {

            player.getPA().mysteryBoxItemOnInterface(other_prizes, 1, ITEM_FRAME, slot);
        }
    }

    public void openInterface() {
        loadloot();
        player.boxCurrentlyUsing = getItemId();
        player.sendMessage(":resetBox:");
        canMysteryBox = true;
        mysteryPrize = -1;
        mysteryAmount = -1;
        for (int i = 0; i < 66; i++) {
            player.getPA().mysteryBoxItemOnInterface(-1, 1, ITEM_FRAME, i);
        }

        spinNum = 0;


        player.getPA().showInterface(INTERFACE_ID);
    }


    public void canMysteryBox() {
        canMysteryBox = true;
    }

    public void quickOpen() {

        if (player.getUltraInterface().isActive() || player.getBlackAodInterface().isActive() || player.getSuperBoxInterface().isActive() || player.getNormalBoxInterface().isActive() || player.getFoeInterface().isActive()) {
            player.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");

            return;
        }
        if (!(player.getSuperMysteryBox().canMysteryBox) || !(player.getBlackAodLootChest().canMysteryBox) || !(player.getNormalMysteryBox().canMysteryBox) ||
                !(player.getUltraMysteryBox().canMysteryBox) || !(player.getFoeMysteryBox().canMysteryBox) ||
                !(player.getYoutubeMysteryBox().canMysteryBox) || !(player.getBlackAodLootChest().canMysteryBox)
        ) {
            player.getPA().showInterface(47000);
            player.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
            return;
        }
        if (player.getItems().playerHasItem(getItemId(), 1)) {
            player.getItems().deleteItem(getItemId(), 1);
            setMysteryPrize();
            roll(player);
        } else {
            player.sendMessage("@blu@You have used your last mystery box.");
        }
    }

    @Override
    public void roll(Player player) {

    }
}
