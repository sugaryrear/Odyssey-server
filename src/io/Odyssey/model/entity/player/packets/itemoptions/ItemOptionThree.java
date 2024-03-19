package io.Odyssey.model.entity.player.packets.itemoptions;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import io.Odyssey.Server;
import io.Odyssey.content.battlepass.BattlePass;
import io.Odyssey.content.collection_log.CollectionLog;
import io.Odyssey.content.combat.magic.AccursedScepter;
import io.Odyssey.content.combat.magic.SanguinestiStaff;
import io.Odyssey.content.combat.magic.TumekensShadow;
import io.Odyssey.content.combat.stats.MonsterKillLog;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.AmethystChiselDialogue;
import io.Odyssey.content.item.lootable.impl.BlackAodLootChest;
import io.Odyssey.content.item.lootable.impl.NormalMysteryBox;
import io.Odyssey.content.item.lootable.impl.SuperMysteryBox;
import io.Odyssey.content.item.lootable.impl.UltraMysteryBox;
import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.content.combat.magic.items.Degrade.DegradableItem;
import io.Odyssey.content.combat.magic.items.PvpWeapons;
import io.Odyssey.content.combat.magic.items.TomeOfFire;
import io.Odyssey.content.combat.magic.items.pouch.RunePouch;
import io.Odyssey.content.lootbag.LootingBag;
import io.Odyssey.content.skills.crafting.BryophytaStaff;
import io.Odyssey.content.teleportation.TeleportTablets;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 *
 * @author Ryan / Lmctruck30
 * <p>
 * Proper Streams
 */

public class ItemOptionThree implements PacketType {


    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        if (c.getMovementState().isLocked())
            return;
        c.interruptActions();
        int itemId11 = c.getInStream().readSignedWordBigEndianA();
        int itemId1 = c.getInStream().readSignedWordA();
        int itemId = c.getInStream().readUnsignedWord();

        if (c.debugMessage) {
            c.sendMessage(String.format("ItemClick[item=%d, option=%d, interface=%d, slot=%d]", itemId, 3, -1, -1));
        }

        if (c.getLock().cannotClickItem(c, itemId))
            return;
        if (!c.getItems().playerHasItem(itemId, 1)) {
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
        if (RunePouch.isRunePouch(itemId)) {
            c.getRunePouch().emptyBagToInventory();
            return;
        }
        TeleportTablets.operate(c, itemId);
        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
                && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
            c.sendMessage("Your actions have declined the duel.");
            duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            return;
        }
        Optional<DegradableItem> d = DegradableItem.forId(itemId);
        if (d.isPresent()) {
            Degrade.checkPercentage(c, itemId);
            return;
        }
        if (SanguinestiStaff.clickItem(c, itemId, 3)) {
            return;
        }
        if (AccursedScepter.clickItem(c, itemId, 3)) {
            return;
        }
        if (TumekensShadow.clickItem(c, itemId, 3)) {
            return;
        }

        if (BryophytaStaff.handleItemOption(c, itemId, 3))
            return;
        switch (itemId) {
            case 27:
                BattlePass.execute(c);
                break;
            case 21816:
                c.getDH().sendOption2("Turn absorption on", "Turn absorption off");
                c.dialogueAction = 21817;
                break;
            case 21817:
                c.getItems().deleteItem(21817, 1);
                c.getItems().addItem(21820, 250);
                break;

            case 11866://8
            case 11867://7
            case 11868://6
            case 11869://5
            case 11870://4
            case 11871://3
            case 11872://2
            case 11873://1
            case 21268: //eternal
                c.npcType = (c.getSlayer().getMaster() > 0 ? c.getSlayer().getMaster() : 401);
                //c.getDH().sendDialogues(3300, (c.getSlayer().getMaster() > 0 ? c.getSlayer().getMaster() : 401));
                c.getDH().sendDialogues(3310,c.npcType);
                break;
            case 4155:
                MonsterKillLog.openInterface(c);
                break;
            case 11980:
            case 11982:
            case 11984:
            case 11986:
            case 11988:
                if(c.wildLevel > 30) {
                    c.sendMessage("You can't teleport above level 30 in the wilderness.");
                    return;
                }
                c.getPA().handleWealth();
                c.isUsingWealth = true;
                c.itemUsing = itemId;
                break;
            case 11972:
            case 11974:
            case 11118:
            case 11120:
            case 11122:
            case 11124:
                if(c.wildLevel > 30) {
                    c.sendMessage("You can't teleport above level 30 in the wilderness.");
                    return;
                }
                c.getPA().handleCombatBracelet(itemId);
                c.isUsingCombatBracelet = true;
                c.itemUsing = itemId;

                break;
            case 2552:
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
                if(c.wildLevel > 30) {
                    c.sendMessage("You can't teleport above level 30 in the wilderness.");
                    return;
                }
                c.getPA().handleDueling(itemId);
                c.isUsingDuelling = true;
                c.itemUsing = itemId;
                break;
            case 25865:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25867:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25884:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 29988:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 29985:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25886:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25888:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25890:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case 25892:
                c.sendMessage("You current crystal shard Charge is "+c.crystalCharge);
                break;
            case LootingBag.LOOTING_BAG:
            case LootingBag.LOOTING_BAG_OPEN:
                c.getDH().sendDialogues(LootingBag.OPTIONS_DIALOGUE_ID, 0);
                break;
            case 30083:
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
            case 21183:
                if (c.getItems().freeSlots() < 1) {
                    c.sendMessage("@blu@You need at least 1 free slot to do this.");
                    return;
                }
                c.getItems().addItem(23824, c.slaughterCharge);
                c.sendMessage("You remove @red@" + c.slaughterCharge + " @bla@charges from your bracelet of slaughter.");
                c.slaughterCharge = 0;
                break;
            case 20714:
                c.getDH().sendDialogues(265, 2897);
                break;
            case 24271:
                if (!c.getItems().playerHasItem(24271)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your helmet.");
                c.getItems().deleteItem(24271, 1);
                c.getItems().addItem(24268, 1);
                c.getItems().addItem(10828, 1);
                break;
            case 26482:
                if (!c.getItems().playerHasItem(26482)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your whip.");
                c.getItems().deleteItem(26482, 1);
                c.getItems().addItem(4151, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 26469:
                if (!c.getItems().playerHasItem(26469)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your void.");
                c.getItems().deleteItem(26469, 1);
                c.getItems().addItem(13072, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 26471:
                if (!c.getItems().playerHasItem(26471)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your whip.");
                c.getItems().deleteItem(26471, 1);
                c.getItems().addItem(13073, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 26475:
                if (!c.getItems().playerHasItem(26475)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your void.");
                c.getItems().deleteItem(26475, 1);
                c.getItems().addItem(11664, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 26477:
                if (!c.getItems().playerHasItem(26477)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your void.");
                c.getItems().deleteItem(26477, 1);
                c.getItems().addItem(11665, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 26473:
                if (!c.getItems().playerHasItem(26473)) {
                    c.sendMessage("@blu@You do not have the item to do this.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to dismantle this item.");
                    return;
                }
                c.sendMessage("@blu@You have dismantled your void.");
                c.getItems().deleteItem(26473, 1);
                c.getItems().addItem(11663, 1);
                c.getItems().addItem(26421, 1);
                break;
            case 20716:
                TomeOfFire.store(c);
                break;
            case 24423:
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least two free slots to use this command.");
                    return;
                }
                if (!(c.getItems().playerHasItem(24423, 1))) {
                    return;
                }
                c.getItems().deleteItem(24423, 1);
                c.getItems().addItem(24422, 1);
                c.getItems().addItem(24511, 1);
                break;
            case 24424:
                if (c.getItems().freeSlots() < 1) {
                    c.sendMessage("You need at least one free slots to use this command.");
                    return;
                }
                if (!(c.getItems().playerHasItem(24424, 1))) {
                    return;
                }
                c.getItems().deleteItem(24424, 1);
                c.getItems().addItem(24422, 1);
                c.getItems().addItem(24514, 1);
                break;
            case 24425:
                if (c.getItems().freeSlots() < 1) {
                    c.sendMessage("You need at least one free slots to use this command.");
                    return;
                }
                if (!(c.getItems().playerHasItem(24425, 1))) {
                    return;
                }
                c.getItems().deleteItem(24425, 1);
                c.getItems().addItem(24422, 1);
                c.getItems().addItem(24517, 1);
                break;
            case Items.VIGGORAS_CHAINMACE: // Uncharging pvp weapons
            case Items.THAMMARONS_SCEPTRE:
            case Items.CRAWS_BOW:
            case Items.URASINE_CHAINMACE:
            case Items.ACCURSED_SCEPTRE:
            case Items.WEBWEAVER_BOW:
                PvpWeapons.handleItemOption(c, itemId, 3);
                break;
            case 22322:
                c.getDH().sendDialogues(333, 7456);
                break;
            case 1704:
                c.sendMessage("@red@You currently have no charges in your glory.");
                break;
            case 12932:
            case 12922:
            case 12929:
            case 12927:
            case 12924:
                String name = ItemDef.forId(itemId).getName();
                Consumer<Player> dismantle = plr -> {
                    plr.getPA().closeAllWindows();
                    if (!plr.getItems().playerHasItem(itemId))
                        return;
                    plr.getItems().deleteItem(itemId, 1);
                    plr.getItems().addItemUnderAnyCircumstance(Items.ZULRAHS_SCALES, 20_000);
                    plr.sendMessage("You dismantle the {} and receive 20,000 scales.", name);
                };

                new DialogueBuilder(c)
                        .itemStatement(itemId, "Are you sure you want to dismantle your " + name + "?", "You will receive 20,000 Zulrah scales.")
                        .option(new DialogueOption("Yes, dismantle it.", dismantle), DialogueOption.nevermind()).send();
                break;
            case 13346:
                new UltraMysteryBox(c).quickOpen();
                break;
            case 29981:
                new NormalMysteryBox(c).quickOpen();
                break;
            case 6828:
                new SuperMysteryBox(c).quickOpen();
                break;
            case 30109:
                new BlackAodLootChest(c).quickOpen();
                break;
            case 21347:
                c.start(new AmethystChiselDialogue(c));
                break;
            case 13125:
            case 13126:
            case 13127:
                if (c.getRunEnergy() < 10000) {
                    if (c.getRechargeItems().useItem(itemId)) {
                        c.getRechargeItems().replenishRun(5000);
                    }
                } else {
                    c.sendMessage("You already have full run energy.");
                    return;
                }
                break;

            case 13128:
                if (c.getRunEnergy() < 10000) {
                    if (c.getRechargeItems().useItem(itemId)) {
                        c.getRechargeItems().replenishRun(10000);
                    }
                } else {
                    c.sendMessage("You already have full run energy.");
                    return;
                }
                break;

            case 13226:
                c.getHerbSack().check();
                break;

            case 12020:
                c.getGemBag().withdrawAll();
                break;

            case 12902: //Toxic staff dismantle
                if (!c.getItems().playerHasItem(12902))
                    return;
                if (c.getItems().freeSlots() < 2)
                    return;

                c.getItems().deleteItem(12902, 1);
                c.getItems().addItem(12932, 1);
                c.getItems().addItem(11791, 1);
                c.sendMessage("You dismantle your toxic staff of the dead.");
                break;

            case 12900: //Toxic trident dismantle
                if (!c.getItems().playerHasItem(12900))
                    return;
                if (c.getItems().freeSlots() < 2)
                    return;

                c.getItems().deleteItem(12900, 1);
                c.getItems().addItem(12932, 1);
                c.getItems().addItem(11907, 1);
                c.sendMessage("You dismantle your toxic trident.");
                break;

            case 11283:
                if (c.getDragonfireShieldCharge() == 0) {
                    c.sendMessage("Your dragonfire shield has no charge.");
                    return;
                }
                c.setDragonfireShieldCharge(0);
                c.sendMessage("Your dragonfire shield has been emptied.");
                break;
            case 13196:
            case 13198:
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least 2 free slots for this.");
                    return;
                }
                c.getItems().deleteItem2(itemId, 1);
                c.getItems().addItem(12929, 1);
                c.getItems().addItem(itemId == 13196 ? 13200 : 13201, 1);
                c.sendMessage("You revoke the mutagen from the helmet.");
                break;
            case 11907:
            case 12899:
                int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
                if (charge <= 0) {
                    if (itemId == 12899) {
                        if (c.getToxicTridentCharge() == 0) {
                            if (c.getItems().freeSlots() > 1) {
                                c.getItems().deleteItem(12899, 1);
                                c.getItems().addItem(12932, 1);
                                c.getItems().addItem(11907, 1);
                                c.sendMessage("You dismantle your Trident of the swamp.");
                                return;
                            } else {
                                c.sendMessage("You need at least 2 inventory spaces to dismantle the trident.");
                                return;
                            }
                        }
                    } else {
                        c.sendMessage("Your trident currently has no charge.");
                        return;
                    }
                }

                if (c.getItems().freeSlots() < 3) {
                    c.sendMessage("You need at least 3 free slots for this.");
                    return;
                }
                c.getItems().addItem(554, 5 * charge);
                c.getItems().addItem(560, 1 * charge);
                c.getItems().addItem(562, 1 * charge);

                if (itemId == 12899) {
                    c.getItems().addItem(12934, 1 * charge);
                }

                if (itemId == 11907) {
                    c.setTridentCharge(0);
                } else {
                    c.setToxicTridentCharge(0);
                }
                c.sendMessage("You revoke " + charge + " charges from the trident.");
                break;

            case 12926:
                if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0) {
                    c.sendMessage("You have no ammo in the pipe.");
                    return;
                }
                if (c.getItems().freeSlots() < 2) {
                    c.sendMessage("You need at least 2 free slots for this.");
                    return;
                }
                if (c.getItems().addItem(c.getToxicBlowpipeAmmo(), c.getToxicBlowpipeAmmoAmount())) {
                    c.setToxicBlowpipeAmmoAmount(0);
                    c.sendMessage("You unload the pipe.");
                }
                break;

            case 11968:
            case 11970:
            case 11105:
            case 11107:
            case 11109:
            case 11111:
                c.getPA().handleSkills();
                c.isOperate = true;
                c.operateEquipmentItemId = itemId;
                break;
            case 1712:
            case 1710:
            case 1708:
            case 1706:
            case 19707:
                c.getPA().handleGlory();
                c.operateEquipmentItemId = itemId;
                c.itemUsing = itemId;
              c.isOperate = false;
                break;

            case 24444:
            case 19639:
            case 19641:
            case 19643:
            case 19645:
            case 19647:
            case 19649:
            case 11864:
            case 11865:
                c.getSlayer().revertHelmet(itemId);
                break;

            case Items.COMPLETIONIST_CAPE:
            case 13280:
            case 13329:
            case 13337:
            case 21898:
            case 13331:
            case 13333:
            case 13335:
            case 20760:
            case 21285:
            case 21776:
            case 21778:
            case 21780:
            case 21782:
            case 21784:
            case 21786:
                if (Server.getMultiplayerSessionListener().inAnySession(c)) {
                    return;
                }

                c.getPA().openQuestInterface("@dre@Max Cape Features",
                        "While wielding the cape you will:",
                        "Have a chance of saving ammo.",
                        "Deplete run energy slower.",
                        "Get more herbs & faster growth time.",
                        "Have less chance of burning food",
                        "Have 20% of saving a bar while smithing.",
                        "Have 20% of saving a herb while mixing potions.",
                        "Regenerate 2 hitpoints instead of 1 at a time.",
                        "Get 5+ restore per prayer/super restore sip.",
                        "Get double seeds while thieving the master farmer.",
                        "Be able to operate for additional options."
                );
                break;

            default:
                if (c.getRights().isOrInherits(Right.OWNER)) {
                    Misc.println("[DEBUG] Item Option #3-> Item id: " + itemId);
                }
                break;
        }

    }

}
