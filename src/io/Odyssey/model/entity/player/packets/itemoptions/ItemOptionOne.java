package io.Odyssey.model.entity.player.packets.itemoptions;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.DiceHandler;
import io.Odyssey.content.battlepass.BattlePass;
import io.Odyssey.content.bosses.Cerberus;
import io.Odyssey.content.bosses.bryophyta.Bryophyta;
import io.Odyssey.content.bosses.mimic.MimicCasket;
import io.Odyssey.content.collection_log.CollectionLog;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.magic.AccursedScepter;
import io.Odyssey.content.combat.magic.NonCombatSpellData;
import io.Odyssey.content.combat.magic.SanguinestiStaff;
import io.Odyssey.content.combat.magic.TumekensShadow;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.ClaimDonatorScrollDialogue;
import io.Odyssey.content.dwarfmulticannon.Cannon;
import io.Odyssey.content.item.lootable.impl.*;
import io.Odyssey.content.combat.magic.items.CluescrollRateIncreaseScroll;
import io.Odyssey.content.combat.magic.items.Packs;
import io.Odyssey.content.combat.magic.items.Starter;
import io.Odyssey.content.combat.magic.items.pouch.RunePouch;
import io.Odyssey.content.lootbag.LootingBag;
import io.Odyssey.content.minigames.barrows.Barrows;
import io.Odyssey.content.miniquests.magearenaii.MageArenaII;
import io.Odyssey.content.skills.DoubleDropScroll;
import io.Odyssey.content.skills.DoubleExpScroll;
import io.Odyssey.content.skills.SkillHandler;
import io.Odyssey.content.skills.SkillPetRateIncreaseScroll;
import io.Odyssey.content.skills.firemake.LightSources;
import io.Odyssey.content.skills.hunter.Hunter;
import io.Odyssey.content.skills.hunter.trap.impl.BirdSnare;
import io.Odyssey.content.skills.hunter.trap.impl.BoxTrap;
import io.Odyssey.content.skills.prayer.Bone;
import io.Odyssey.content.skills.prayer.Prayer;
import io.Odyssey.content.skills.runecrafting.Pouches;
import io.Odyssey.content.skills.slayer.SlayerUnlock;
import io.Odyssey.content.skills.woodcutting.BirdNest;
import io.Odyssey.content.teleportation.TeleportTablets;
import io.Odyssey.content.trails.TreasureTrails;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.Items;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.items.ArmorSets;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.multiplayersession.duel.DuelSessionRules.Rule;
import io.Odyssey.model.multiplayersession.flowerpoker.FlowerData;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.net.discord.DiscordMessager;

import static io.Odyssey.content.DiceHandler.DICING_AREA;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ItemOptionOne implements PacketType {

    /**
     * The position the mithril seed was planted
     */
    private static final int[] position = new int[2];

    public void plantMithrilSeed(Player c) {
        position[0] = c.absX;
        position[1] = c.absY;
        GlobalObject object1 = new GlobalObject(FlowerData.getRandomFlower().objectId, position[0], position[1], c.getHeight(), 3, 10, 120, -1);
        Server.getGlobalObjects().add(object1);
        c.getPA().walkTo(1, 0);
        c.facePosition(position[0] -1, position[1]);
        c.sendMessage("You planted a flower!");
        c.getItems().deleteItem(299, c.getItems().getInventoryItemSlot(299), 1);
    }
    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        if (c.getMovementState().isLocked())
            return;
        c.interruptActions();
        int interfaceId = c.getInStream().readUnsignedWord();
        int itemSlot = c.getInStream().readUnsignedWord();
        int itemId = c.getInStream().readUnsignedWord();
        if (c.debugMessage) {
            c.sendMessage(String.format("ItemClick[item=%d, option=%d, interface=%d, slot=%d]", itemId, 1, interfaceId, itemSlot));
        }

        if (itemSlot >= c.playerItems.length || itemSlot < 0) {
            return;
        }
        if (itemId != c.playerItems[itemSlot] - 1) {
            return;
        }
        if (c.isDead || c.getHealth().getCurrentHealth() <= 0) {
            return;
        }

        if (c.getInterfaceEvent().isActive()) {
            c.sendMessage("Please finish what you're doing.");
            return;
        }
        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        c.lastClickedItem = itemId;

        if (c.isFping() && itemId != 299) {
            return;
        }

        c.getHerblore().clean(itemId);
        if (c.getFood().isFood(itemId)) {
            c.getFood().eat(itemId, itemSlot);
            return;
        } else if (c.getPotions().isPotion(itemId)) {
            c.getPotions().handlePotion(itemId, itemSlot);
            return;
        }
        if (c.getQuesting().handleItemClick(itemId)) {
            return;
        }
        if(ArmorSets.isSet(itemId)){
            ArmorSets.handleSet(c,itemId);
            return;
        }
        Optional<Bone> bone = Prayer.isOperableBone(itemId);
        if (bone.isPresent()) {
            c.getPrayer().bury(bone.get());
            return;
        }
        TeleportTablets.operate(c, itemId);
        Packs.openPack(c, itemId);
        if (LootingBag.isLootingBag(itemId)) {
            c.getLootingBag().toggleOpen();
            return;
        }
        if (RunePouch.isRunePouch(itemId)) {
            c.getRunePouch().openRunePouch();
            return;
        }
        if (Cannon.clickItem(c, itemId)) {
            return;
        }
        if (ClaimDonatorScrollDialogue.clickScroll(c, itemId)) {
            return;
        }
        if (SanguinestiStaff.clickItem(c, itemId, 1)) {
            return;
        }
        if (AccursedScepter.clickItem(c, itemId, 1)) {
            return;
        }
        if (TumekensShadow.clickItem(c, itemId, 1)) {
            return;
        }
        if (TreasureTrails.firstClickItem(c, itemId)) {
            return;
        }
if(LightSources.extinguish(c,itemId)){
    return;
}
        switch (itemId) {
            case 7413:
                BirdNest.handleBirdNest(c, itemId, 2);
                break;

            case 31015: //inf run energy
                c.start(new DialogueBuilder(c).statement("Once you use this item the 1 hour of infinite run energy will start",
                                "the timer will save if you logout.").

                        option("Are you sure you wish to open it?",
                                new DialogueOption("Start 1 hour of infinite run energy", player -> {
                                    if (!player.getItems().playerHasItem(31015, 1)) {
                                        player.sendMessage("You don't have the correct item.");
                                        player.getPA().closeAllWindows();
                                        return;
                                    }
                                    player.getItems().deleteItem(31015,1);
//                                    player.hasinfrunenergy = true;
                                    c.infrunenergyLength +=(int) Configuration.INF_RUN_ENERGY_LENGTH;
                                    c.infrunenergyDelay = System.currentTimeMillis();
                                    c.getPA().updateRunEnergy(true);
                                  //  player.sendMessage("infrunenergy##0");
                                    player.getPA().closeAllWindows();

                                }),
                                new DialogueOption("Cancel", plr -> {
                                    plr.getPA().closeAllWindows();
                                })));
                break;
            case 7292: //unlock all teleports scroll
                c.start(new DialogueBuilder(c).statement("Upon redeeming this scroll you will unlock",
                                "all world teleports and NPCs.").

                        option("Are you sure you wish to redeem it?",
                                new DialogueOption("Redeem scroll", player -> {
                                    if (!player.getItems().playerHasItem(7292, 1)) {
                                        player.sendMessage("You don't have the correct item.");
                                        player.getPA().closeAllWindows();
                                        return;
                                    }
                                    player.getItems().deleteItem(7292,1);
player.sendMessage("@blu@You have unlocked all areas of the world map!");
player.unlockedallteles=true;
                                    player.getPA().closeAllWindows();

                                }),
                                new DialogueOption("Cancel", plr -> {
                                    plr.getPA().closeAllWindows();
                                })));
                break;
            case 6199:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
              if (c.getItems().playerHasItem(6199)) {
                    c.inDonatorBox = true;
                  c.boxusing = 6199;
                    c.getNormalMysteryBox().openInterface();
                    return;
                }
                break;
            case 952: //Spade
                c.startAnimation(830);

                CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if (!c.isOnline()) {
                            container.stop();
                            return;
                        }
                        if (Boundary.isIn(c, Barrows.GRAVEYARD)) {
                            c.getBarrows().digDown();
                        }
                        if (Region.molehills.stream().anyMatch(s -> c.getPosition().equals(s))){
                            if (!c.getItems().hasItemOnOrInventory( LightSources.LITITEMS)) {
                                c.getDH().sendStatement("You do not appear to have a valid light source on you.");
                                c.sendMessage("Check the Firemaking skill guide for valid light sources.");
                                c.nextChat = 17559;
                            } else {
                                c.getPA().movePlayer(1753, 5237, 0);
                            }

                          //
                            //check lightsource
//                            if (!c.getItems().hasItemOnOrInventory( LightSources.LITITEMS)) {
//                                c.getPA().sendFrame99(2);
//                                c.getPA().walkableInterface(13583);
//                                c.sendMessage("You do not have a light source.");
//                            }

                        }
//                        if (x == 3005 && y == 3376 || x == 2999 && y == 3375 || x == 2996 && y == 3377) {
//
//                            c.getPA().movePlayer(1760, 5163, 0);
//                            return;
//
//                        }

                        container.stop();
                    }

                    @Override
                    public void onStopped() {
                    }
                }, 2);
                break;

            case MageArenaII.SYMBOL_ID:
                MageArenaII.handleEnchantedSymbol(c);
                return;
            case Items.MIMIC:
                MimicCasket.open(c);
                break;
            case 21730:
                c.sendMessage("Fallen from the centre of a Grotesque Guardian. This could be attached");
                c.sendMessage("to a pair of Bandos boots...");
                break;
            case 23517:
                if (c.getItems().playerHasItem(946)) {
                    c.getItems().deleteItem(23517, 1);
                    c.getItems().addItem(224, 100);
                } else {
                    c.sendMessage("You need a knife to open this.");
                }
                break;
            case 27:
                if (c.boughtBP == true) {
                    c.sendMessage("You have already activated the battlepass.");
                    return;
                }
                if (c.battlePassSeason != BattlePass.currentSeason) {
                    c.sendMessage("The battlepass new battlepass not yet started.");
                    return;
                }
                c.boughtBP = true;
              //  c.getItems().deleteItem(27, 1);
                c.sendMessage("@red@You have activated the battlepass!");
                break;
            case ResourceBoxSmall.BOX_ITEM:
                new ResourceBoxSmall().roll(c);
                break;
            case ResourceBoxMedium.BOX_ITEM:
                new ResourceBoxMedium().roll(c);
                break;
//            case ResourceBoxLarge.BOX_ITEM:
//                new ResourceBoxLarge().roll(c);
//                break;
            case 21034:
                c.getDH().sendDialogues(345, 9120);
                break;
            case 21079:
                c.getDH().sendDialogues(347, 9120);
                break;
            case 22477:
                c.sendMessage("Attach it onto a dragon defender to make avernic defender.");
                break;
            case 23185:
                if (!c.getPA().morphPermissions()) {
                    return;
                }
                for (int i = 0; i <= 12; i++) {
                    c.setSidebarInterface(i, 6014);
                }
                c.npcId2 = 9415;
                c.isNpc = true;
                c.playerStandIndex = -1;
                c.setUpdateRequired(true);
                c.morphed = true;
                c.setAppearanceUpdateRequired(true);
                break;
            case 19564:
                if (c.wildLevel > 30) {
                    c.sendMessage("You can't teleport above level 30 in the wilderness.");
                    return;
                }
                c.getPA().startTeleport(Configuration.START_LOCATION_X, Configuration.START_LOCATION_Y, 0, "pod", false);
                break;
            case 13188:
                c.startAnimation(7514);
                c.gfx0(1282);
                break;
            case 2841:
                if (!c.getItems().playerHasItem(2841)) {
                    c.sendMessage("You need an Bonus XP Scroll to do this!");
                    return;
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                        || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    c.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, no need to use that now!");
                    return;
                } else if (c.xpScroll == false && c.getItems().playerHasItem(2841)) {
                    c.getItems().deleteItem(2841, 1);
                    DoubleExpScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 1 hour of bonus experience.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, 60);
                    c.getQuestTab().updateInformationTab();
                } else if (c.xpScroll == true) {
                    c.sendMessage("@red@You already used this up.");
                }
                break;
            case 7968:
                if (!c.getItems().playerHasItem(7968)) {
                    c.sendMessage("You need an Bonus Pet Scroll to do this!");
                    return;
                }
                if (c.skillingPetRateScroll == false && c.getItems().playerHasItem(7968)) {
                    c.getItems().deleteItem(7968, 1);
                    SkillPetRateIncreaseScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 30 minutes of bonus skilling pet rate increase.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_SKILLING_PET_RATE, TimeUnit.MINUTES, 30);
                } else if (c.skillingPetRateScroll == true) {
                    c.sendMessage("@red@You already have a pet bonus going.");
                }
                break;
            case 260830:
                if (!c.getItems().playerHasItem(26083)) {
                    c.sendMessage("You need an double XP sigil to do this!");
                    return;
                }
//                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
//                        || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
//                    c.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, no need to use that now!");
//                    return;
//                } else
                if (c.xpScroll == false && c.getItems().playerHasItem(26080)) {
                    c.startGraphic(new Graphic(1990));
                    DoubleExpScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 1 hour of Double XP.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, 60);
                    c.getQuestTab().updateInformationTab();
                } else if (c.xpScroll == true) {
                    c.sendMessage("@red@You already used this up.");
                }
                break;
            case 26080:
                if (!c.getItems().playerHasItem(26080)) {
                    c.sendMessage("You need an Bonus drops sigil to do this!");
                    return;
                }
//                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
//                        || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
//                    c.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, no need to use that now!");
//                    return;
//                } else
                    if (c.ddScroll == false && c.getItems().playerHasItem(26080)) {
                    c.startGraphic(new Graphic(1990));
                    DoubleDropScroll.openScroll(c);
                    c.sendMessage("@red@You have activated 1 hour of Double Drops.");
                    c.getPA().sendGameTimer(ClientGameTimer.DOUBLES_DROPS, TimeUnit.MINUTES, 60);
                    c.getQuestTab().updateInformationTab();
                } else if (c.ddScroll == true) {
                    c.sendMessage("@red@You already used this up.");
                }
                break;
            case 26071:
                if (!c.getItems().playerHasItem(26071)) {
                    c.sendMessage("You need a damage boost sigil to do this!");
                    return;
                }
                if (c.fasterCluesScroll == false && c.getItems().playerHasItem(26071)) {
                    c.getItems().deleteItem(26071, 1);
                    CluescrollRateIncreaseScroll.openScroll(c);
                    c.sendMessage("@blu@You have activated 60 minutes of bonus damage vs bosses.");
                    c.getPA().sendGameTimer(ClientGameTimer.BONUS_CLUES, TimeUnit.MINUTES, 60);
                } else if (c.fasterCluesScroll == true) {
                    c.sendMessage("@red@You already have a bonus damage boost active.");
                }
                break;
            case 26135:
                c.startGraphic(new Graphic(1993));
                if (c.get2XDamage()) {
                    c.sendMessage("You already have this activated.");
                    return;
                }
                if (c.getItems().playerHasItem(26135)) {
                    c.sendMessage("@red@You have activated 2x damage for 15 mins.");
                    c.set2XDamage(true);
                    CycleEventHandler.getSingleton().stopEvents(c, CycleEventHandler.Event.DAMAGE_SIGIL);
                    CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.DAMAGE_SIGIL, c, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer b) {
                            c.sendMessage("Your 2x damage has expired!"); // so you feel like 1 more sigil of aids or mayyybeeee help finish world boss its fk'dnah im
                            c.set2XDamage(false); // did it for u
                            b.stop();
                            if (c == null) {
                                b.stop();
                                return;
                            }
                        }

                        @Override
                        public void onStopped() {
                        }
                    }, 1500); //15mins

                }
                break;
            case 12885:
            case 13277:
            case 19701:
            case 13245:
            case 12007:
            case 22106:
            case 12936:
            case 24495:
                c.getDH().sendDialogues(361, Npcs.BOSS_POINT_SHOP);
                break;

            case 6830:
                c.getItems().deleteItem(6830, 1);
                c.gfx100(263);
                Starter.addStarterItems(c);
                break;

            case 6829://yt video giveaway box tier 1
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6829, 1);
                    c.gfx100(263);
                    c.getItems().addItem(2396, 1);//25 scroll
                    c.getItems().addItem(6828, 1);//super m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    DiscordMessager.sendLogs("[YOUTUBE VIDEO BOX T1] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a video box t1!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;

            case 6831://yt video giveaway box tier2
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6831, 1);
                    c.gfx100(263);
                    c.getItems().addItem(786, 1);//50 scroll
                    c.getItems().addItem(13346, 1);//ultra m box
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    DiscordMessager.sendLogs("[YOUTUBE VIDEO BOX T2] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a video box t2!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;

            case 6832://yt stream giveaway box tier 1
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6832, 1);
                    c.gfx100(263);
                    c.getItems().addItem(29973, 2);//refer box
                    c.getItems().addItem(6199, 1);//normal m box
                    c.getItems().addItem(6828, 1);//super m box
                    c.getItems().addItem(13346, 1);//ultra m box
                    c.getItems().addItem(6769, 5);//5 scroll
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    DiscordMessager.sendLogs("[YOUTUBE STREAM BOX T1] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a stream box t1!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;
            case 6833://yt stream giveaway box tier2
                if (c.getItems().freeSlots() > 20) {
                    c.getItems().deleteItem(6833, 1);
                    c.gfx100(263);
                    c.getItems().addItem(29973, 2);//normal m box
                    c.getItems().addItem(6828, 2);//super m box
                    c.getItems().addItem(13346, 2);//ultra m box
                    c.getItems().addItem(6769, 5);//5 scroll
                    c.getItems().addItem(2396, 2);//25 scroll
                    c.getDH().sendStatement("Box has been logged for staff, please give away responsibly!");
                    DiscordMessager.sendLogs("[YOUTUBE STREAM BOX T2] Opened by " + c.getDisplayName() + ".");
                    PlayerHandler.executeGlobalStaffMessage("[@red@Staff Message@bla@] <col=255>" + c.getDisplayName() + " has opened a stream box t2!");
                } else {
                    c.sendMessage("Please clear your inventory before opening.");
                }
                break;
            case 2697:
                if (c.getItems().playerHasItem(2697, 1)) {
                    c.getItems().deleteItem(2697, 1);
                    c.gfx100(263);
                    c.donatorPoints += 10;
                    c.amDonated += 10;
                    c.sendMessage("Thank you for donating! 10$ has been added to your total credit.");
                    c.updateRank();
                    c.getPA().closeAllWindows();
                    return;
                }
                break;
            case 1004:
                if (c.getItems().playerHasItem(1004)) {
                    c.getItems().deleteItem(1004, 1);
                    c.getItems().addItem(995, 20000000);
                    c.sendMessage("You claim the coin stack and it turns into 20m gp.");
                }
                break;
            case 8899:
                if (c.getItems().playerHasItem(8899)) {
                    c.getItems().deleteItem(8899, 1);
                    c.getItems().addItem(995, 50000000);
                    c.sendMessage("You claim the coin stack and it turns into 50m gp.");
                }
                break;
            case 6121:
                if (c.barbarian == false && c.getItems().playerHasItem(6121)) {
                    c.getItems().deleteItem(6121, 1);
                    c.barbarian = true;
                    c.breakVials = true;
                    c.getDH().sendStatement("You may now use ::vials to turn off and on vial smashing!", "It is now set to on.");

                }else if (c.barbarian == true) {
                    c.sendMessage("You already learned how to do this");
                }
                break;
            case 299:
                if (!Boundary.isIn(c, Boundary.FLOWER_POKER_AREA)) {
                    c.sendMessage("You cannot plant a flower here.");
                    return;
                }
                if (Boundary.isIn(c, Boundary.FLOWER_POKER_AREA)) {
                    if (c.isFping())
                        c.getFlowerPoker().plantSeed(c, true, false);
                    return;
                }
                if (c.getRegionProvider().getClipping(c.absX, c.absY, c.heightLevel) != 0
                        || Server.getGlobalObjects().anyExists(c.absX, c.absY, c.heightLevel)) {
                    c.sendMessage("You cannot plant a flower here.");
                    return;
                }
//                if (!c.getItems().playerHasItem(15098, 1)) {
//                    c.sendMessage("You need to purchase a dice bag to use mithril seeds.");
//                    return;
//                }
//                if(!Boundary.isIn(c, DICING_AREA)) {
//                    c.sendMessage("You must be in the dice area to plant mithril seeds.");
//                    return;
//                }
                if(System.currentTimeMillis() - c.lastPlant < 250) {
                    return;
                }
                c.lastPlant = System.currentTimeMillis();
                plantMithrilSeed(c);
                break;
            case 29972: //slayer m chest
                if (c.getItems().playerHasItem(29972)) {
                    new SlayerMysteryBox(c).quickOpen();
                }
                break;
            case 21027: //dark relic
                c.inLamp = true;
                c.usingLamp = true;
                c.normalLamp = true;
                c.antiqueLamp = false;
                c.sendMessage("You rub the lamp...");
                c.getPA().showInterface(2808);
                break;

            case 22711: //dark relic
                CollectionLog group = c.getGroupIronmanCollectionLog();
                if (group != null) {
                    new DialogueBuilder(c).option(
                            new DialogueOption("Personal", plr -> c.getCollectionLog().openInterface(plr)),
                            new DialogueOption("Group", group::openInterface)
                    ).send();
                    return;
                }

                c.getCollectionLog().openInterface(c);
                break;
            case 13148:
                c.start(new DialogueBuilder(c).statement("This lamp will reset a skill of your choice.").exit(plr -> {
                    c.inLamp = true;
                    c.usingLamp = true;
                    c.normalLamp = true;
                    c.antiqueLamp = false;
                    c.sendMessage("You have rubbed the skill reset lamp.");
                    c.getPA().showInterface(2808);
                }));
                break;
            case 30109:
                if (!(c.getBlackAodLootChest().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
                else if (c.getItems().playerHasItem(30109)) {
                    c.inDonatorBox = true;
                    c.getBlackAodLootChest().openInterface();
                    return;
                }
                break;
            case 30083:
                c.getPA().showInterface(39500);
                c.sendMessage("@blu@This shows the Drop table for all Npcs in Game.");
                return;
            case 29973:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
                else if (c.getItems().playerHasItem(29973)) {
                    c.inDonatorBox = true;
                    c.getYoutubeMysteryBox().openInterface();
                    return;
                }
                break;
            case 29971:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
                else if (c.getItems().playerHasItem(29971)) {
                    c.inDonatorBox = true;
                    c.getUltraMysteryBox().openInterface();
                    return;
                }
                break;
            case 787:
                if (!c.getResourceBoxLarge().canMysteryBox) {
                   // c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                } else
                    if (c.getItems().playerHasItem(787)) {
                    c.getResourceBoxLarge().openInterface();
                    c.inDonatorBox = true;
                    c.boxusing = 787;
                    c.stopMovement();
                    return;
                }
                break;
            case 29980:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
                else if (c.getItems().playerHasItem(29980)) {
                    c.getSuperMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 29979:
                if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) || !(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) || !(c.getYoutubeMysteryBox().canMysteryBox)) {
                    c.getPA().showInterface(47000);
                    c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
                    return;
                }
                else if (c.getItems().playerHasItem(29979)) {
                    c.getFoeMysteryBox().openInterface();
                    c.inDonatorBox = true;
                    c.stopMovement();
                    return;
                }
                break;
            case 21347:
                c.boltTips = true;
                c.arrowTips = false;
                c.javelinHeads = false;
                c.sendMessage("Your Amethyst method is now Bolt Tips!");
                break;
            case 20724:
                DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(session)) {
                    if (session.getRules().contains(Rule.NO_DRINKS)) {
                        c.sendMessage("Using the imbued heart with 'No Drinks' option is forbidden.");
                        return;
                    }
                }
                if (System.currentTimeMillis() - c.lastHeart < 100000) {
                    c.sendMessage("You must wait 2 minutes between each use.");
                } else {
                    c.getPA().imbuedHeart();
                    c.lastHeart = System.currentTimeMillis();
                }
                break;
            case 27641:
                DuelSession Session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
                if (Objects.nonNull(Session)) {
                    if (Session.getRules().contains(Rule.NO_DRINKS)) {
                        c.sendMessage("Using the saturated heart with 'No Drinks' option is forbidden.");
                        return;
                    }
                }
                if (System.currentTimeMillis() - c.lastHeart < 420000) {
                    c.sendMessage("You must wait 7 minutes between each use.");
                } else {
                    c.getPA().saturatedHeart();
                    c.lastHeart = System.currentTimeMillis();
                }
                break;
            case PvmCasket.PVM_CASKET: //Pvm Casket
                if (System.currentTimeMillis() - c.openCasketTimer > 350) {
                    if (c.getItems().playerHasItem(405)) {
                        c.getPvmCasket().roll(c);
                        c.openCasketTimer =  System.currentTimeMillis();
                    }
                }
                break;
            case Items.DWARF_CANNON_SET:
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(Items.DWARF_CANNON_SET, 1)) {
                    c.getItems().deleteItem(Items.DWARF_CANNON_SET, 1);
                    c.getItems().addItem(Items.CANNON_BASE, 1);
                    c.getItems().addItem(Items.CANNON_STAND, 1);
                    c.getItems().addItem(Items.CANNON_BARRELS, 1);
                    c.getItems().addItem(Items.CANNON_FURNACE, 1);
                }
                break;
            case 11666://full void token
                if (c.getItems().freeSlots() < 6) {
                    c.sendMessage("You need at least 6 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(11666)) {
                    c.getItems().deleteItem(11666, 1);
                    c.getItems().addItem(Items.ELITE_VOID_ROBE, 1);
                    c.getItems().addItem(Items.ELITE_VOID_TOP, 1);
                    c.getItems().addItem(Items.VOID_KNIGHT_GLOVES, 1);
                    c.getItems().addItem(Items.VOID_RANGER_HELM, 1);
                    c.getItems().addItem(Items.VOID_MAGE_HELM, 1);
                    c.getItems().addItem(Items.VOID_MELEE_HELM, 1);
                }
                break;
            case 8653 ://full void token
                if (c.getItems().freeSlots() < 6) {
                    c.sendMessage("You need at least 6 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(8653)) {
                    c.getItems().deleteItem(8653, 1);
                    c.getItems().addItem(Items.VOID_MAGE_HELM_OR, 1);
                    c.getItems().addItem(Items.VOID_MELEE_HELM_OR, 1);
                    c.getItems().addItem(Items.VOID_RANGER_HELM_OR, 1);
                    c.getItems().addItem(Items.ELITE_VOID_TOP_OR, 1);
                    c.getItems().addItem(Items.ELITE_VOID_ROBE_OR, 1);
                    c.getItems().addItem(Items.VOID_KNIGHT_GLOVES_OR, 1);
                }
                break;
            case 8701://full void token
                if (c.getItems().freeSlots() < 6) {
                    c.sendMessage("You need at least 6 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(8701)) {
                    c.getItems().deleteItem(8701, 1);
                    c.getItems().addItem(Items.VOID_MAGE_HELM_OR, 1);
                    c.getItems().addItem(Items.VOID_MELEE_HELM_OR, 1);
                    c.getItems().addItem(Items.VOID_RANGER_HELM_OR, 1);
                    c.getItems().addItem(Items.CORRUPTED_ELITE_VOID_TOP, 1);
                    c.getItems().addItem(Items.CORRUPTED_ELITE_VOID_ROBE , 1);
                    c.getItems().addItem(Items.VOID_KNIGHT_GLOVES_OR, 1);
                }
                break;
            case 12873://guthan
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12873, 1)) {
                    c.getItems().addItem(4724, 1);
                    c.getItems().addItem(4726, 1);
                    c.getItems().addItem(4728, 1);
                    c.getItems().addItem(4730, 1);
                }
                c.getItems().deleteItem(12873, 1);
                break;
            case 12875://verac
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12875, 1)) {
                    c.getItems().addItem(4753, 1);
                    c.getItems().addItem(4755, 1);
                    c.getItems().addItem(4757, 1);
                    c.getItems().addItem(4759, 1);
                }
                c.getItems().deleteItem(12875, 1);
                break;
            case 12877://dharok
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12877, 1)) {
                    c.getItems().addItem(4716, 1);
                    c.getItems().addItem(4718, 1);
                    c.getItems().addItem(4720, 1);
                    c.getItems().addItem(4722, 1);
                }
                c.getItems().deleteItem(12877, 1);
                break;
            case 12879://torags
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12879, 1)) {
                    c.getItems().addItem(4745, 1);
                    c.getItems().addItem(4747, 1);
                    c.getItems().addItem(4749, 1);
                    c.getItems().addItem(4751, 1);
                }
                c.getItems().deleteItem(12879, 1);
                break;
            case 12881://ahrims
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12881, 1)) {
                    c.getItems().addItem(4708, 1);
                    c.getItems().addItem(4710, 1);
                    c.getItems().addItem(4712, 1);
                    c.getItems().addItem(4714, 1);
                }
                c.getItems().deleteItem(12881, 1);
                break;
            case 25590://money bag 500m
                if (c.getItems().freeSlots() < 1) {
                    c.sendMessage("You need at least 1 free slot to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(25590, 1)) {
                    c.getItems().addItem(995, 500000000);
                }
                c.getItems().deleteItem(25590, 1);
                break;
            case 12883://karils
                if (c.getItems().freeSlots() < 4) {
                    c.sendMessage("You need at least 4 free slots to open this.");
                    return;
                }
                if (c.getItems().playerHasItem(12883, 1)) {
                    c.getItems().addItem(4732, 1);
                    c.getItems().addItem(4734, 1);
                    c.getItems().addItem(4736, 1);
                    c.getItems().addItem(4738, 1);
                }
                c.getItems().deleteItem(12883, 1);
                break;
            case 10006:
                Hunter.lay(c, new BirdSnare(c));
                break;
            case 10008:
                Hunter.lay(c, new BoxTrap(c));
                break;
            case 13249:
                if (Boundary.isIn(c, Boundary.LONE_SURVIVER_HUT)) {
                    c.sendMessage("Please leave the lone surviver hut area to teleport.");
                    return ;
                }
                if (Boundary.isIn(c, Boundary.LONE_SURVIVER)) {
                    c.sendMessage("Please leave the lone surviver hut area to teleport.");
                    return ;
                }
                if (Boundary.isIn(c, Boundary.WILDERNESS_PARAMETERS)) {
                    c.sendMessage("Please leave the wild.");
                    return ;
                }
//                if (!Boundary.isIn(c, Boundary.NEW_HOME)) {
//                    c.sendMessage("Please use this in home.");
//                    return ;
//                }
                c.isWc = false;
                if (c.isOverloading) {
                    c.sendMessage("You cannot teleport while taking overload damage.");
                    return;
                }
                if (c.isFping()) {
                    /**
                     * Cannot do action while fping
                     */
                    c.sendMessage("You cannot teleport while fping.");

                    return;
                }

                if (c.stopPlayerSkill) {
                    SkillHandler.resetPlayerSkillVariables(c);
                    c.stopPlayerSkill = false;
                }
                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && (!c.getSlayer().getTask().get().getPrimaryName().equals("hellhound") && !c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")))) {
                    c.sendMessage("You must have an active cerberus or hellhound task to use this.");
                    return;
                }
                c.getItems().deleteItem(13249, 1);

//                if (!c.getSlayer().isCerberusRoute()) {
//                    c.sendMessage("You have bought Route into cerberus cave. please wait till you will be teleported.");
//                    Cerberus.init(c);
//                    c.cerbDelay = System.currentTimeMillis();
//                    return;
//                }

                if (Server.getEventHandler().isRunning(c, "cerb")) {
                    c.sendMessage("You're about to fight start the fight, please wait.");
                    return;
                }
                Cerberus.init(c);
                break;

            case 13226:
                c.getHerbSack().fillSack();
                break;

            case 12020:
                c.getGemBag().fillBag();
                break;

            case 5509:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 0);
                break;
            case 5510:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 1);
                break;
            case 5512:
                Pouches.fill(c, Pouches.Pouch.forId(itemId), 2);
                break;

            /*case 952: //Spade
                int x = c.absX;
                int y = c.absY;
                if (Boundary.isIn(c, Barrows.GRAVEYARD)) {
                    c.getBarrows().digDown();
                }
                if (x == 3005 && y == 3376 || x == 2999 && y == 3375 || x == 2996 && y == 3377) {
                    if (!c.getRechargeItems().hasItem(13120)) {
                        c.sendMessage("You must have the elite falador shield to do this.");
                        return;
                    }
                    c.getPA().movePlayer(1760, 5163, 0);
                }
                break;*/
        }

        if (itemId == 2678) {
            c.getDH().sendDialogues(657, -1);
            return;
        }
        if (itemId == 8015 || itemId == 8014) {
            NonCombatSpellData.attemptDate(c, itemId);
        }
        if (itemId == 9553) {
            c.getPotions().eatChoc(itemSlot);
        }
        if (itemId == 12846) {
            c.getDH().sendDialogues(578, -1);
        }
        if (itemId == 12938) {
            c.getItems().deleteItem(12938, 1);
            c.getPA().startTeleport(2205, 3056, 0, "modern", false);
            return;
        }
        if (itemId == 4155) {
            if (!c.getSlayer().getTask().isPresent()) {
                c.sendMessage("You do not have a task, please talk with a slayer master!");
                return;
            }
            c.sendMessage("I currently have " + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + "'s to kill.");
            c.getPA().closeAllWindows();
        }
        if (itemId == 2839) {
            if (c.getSlayer().getUnlocks().contains(SlayerUnlock.MALEVOLENT_MASQUERADE)) {
                c.sendMessage("You have already learned this recipe. You have no more use for this scroll.");
                return;
            }
            if (c.getItems().playerHasItem(2839)) {
                c.getSlayer().getUnlocks().add(SlayerUnlock.MALEVOLENT_MASQUERADE);
                c.sendMessage("You have learned the slayer helmet recipe. You can now assemble it");
                c.sendMessage("using a Black Mask, Facemask, Nose peg, Spiny helmet and Earmuffs.");
                c.getItems().deleteItem(2839, 1);
            }
        }
        if (itemId == 15098) {
            DiceHandler.rollDice(c);
        }
        if (itemId == 2701) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
                return;
            }
            if (c.getItems().playerHasItem(2701, 1)) {
                c.getDH().sendDialogues(4004, -1);
            }
        }
        if (itemId == 7509) {
            if (c.getPosition().inDuelArena() || Boundary.isIn(c, Boundary.DUEL_ARENA)) {
                c.sendMessage("You cannot do this here.");
                return;
            }
            if (c.getHealth().getStatus().isPoisoned() || c.getHealth().getStatus().isVenomed()) {
                c.sendMessage("You are effected by venom or poison, you should cure this first.");
                return;
            }
            if (c.getHealth().getCurrentHealth() <= 1) {
                c.sendMessage("I better not do that.");
                return;
            }
            c.forcedChat("Ow! I nearly broke a tooth!");
            c.startAnimation(829);
            // c.getHealth().reduce(1);
            c.appendDamage(1, Hitmark.HIT);
            c.getPA().sendSound(1018);
            return;
        }
        if (itemId == 10269) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10269, 1)) {
                c.getItems().addItem(995, 30000);
                c.getItems().deleteItem(10269, 1);
            }
        }
        if (itemId == 10271) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10271, 1)) {
                c.getItems().addItem(995, 10000);
                c.getItems().deleteItem(10271, 1);
            }
        }
        if (itemId == 10273) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10273, 1)) {
                c.getItems().addItem(995, 14000);
                c.getItems().deleteItem(10273, 1);
            }
        }
        if (itemId == 10275) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10275, 1)) {
                c.getItems().addItem(995, 18000);
                c.getItems().deleteItem(10275, 1);
            }
        }
        if (itemId == 10277) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10277, 1)) {
                c.getItems().addItem(995, 22000);
                c.getItems().deleteItem(10277, 1);
            }
        }
        if (itemId == 10279) {
            if (c.getPosition().inWild() || c.getPosition().inDuelArena()) {
                return;
            }
            if (c.getItems().playerHasItem(10279, 1)) {
                c.getItems().addItem(995, 26000);
                c.getItems().deleteItem(10279, 1);
            }
        }

        /*Coin Bags */
        if (itemId == 10831)
            if (c.getItems().playerHasItem(10831)) {
                c.getCoinBagEmpty().open();
                return;
            }
        if (itemId == 10832)
            if (c.getItems().playerHasItem(10832)) {
                c.getCoinBagSmall().open();
                return;
            }
        if (itemId == 10833)
            if (c.getItems().playerHasItem(10833)) {
                c.getCoinBagMedium().open();
                return;
            }
        if (itemId == 10834)
            if (c.getItems().playerHasItem(10834)) {
                c.getCoinBagLarge().open();
                return;
            }
        if (itemId == 10835)
            if (c.getItems().playerHasItem(10835)) {
                c.getCoinBagBuldging().open();
                return;
            }
        if (itemId == 11739)
            if (c.getItems().playerHasItem(11739)) {
                c.getVoteMysteryBox().roll(c);
                return;
            }

        if (itemId == 20703) //Daily Gear Box
            if (c.getItems().playerHasItem(20703)) {
                c.getDailyGearBox().open();
                return;
            }
        if (itemId == 20791) //Daily Skilling Box
            if (c.getItems().playerHasItem(20791)) {
                c.getDailySkillBox().open();
                return;
            }
		/*if (itemId == 7310) //Skill Casket
			if (c.getItems().playerHasItem(7310)) {
				c.getSkillCasket().open();
				return;
			}*/

        if (itemId == 2528) {
            c.inLamp = true;
            c.usingLamp = true;
            c.normalLamp = true;
            c.antiqueLamp = false;
            c.sendMessage("You rub the lamp...");
            c.getPA().showInterface(80000);
        }

    }
}