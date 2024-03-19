package io.Odyssey.model.entity.player.packets.itemoptions;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.battlepass.BattlePass;
import io.Odyssey.content.bosspoints.JarsToPoints;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.magic.AccursedScepter;
import io.Odyssey.content.combat.magic.SanguinestiStaff;
import io.Odyssey.content.displayname.ChangeDisplayName;
import io.Odyssey.content.combat.magic.items.PvpWeapons;
import io.Odyssey.content.combat.magic.items.item_combinations.Godswords;
import io.Odyssey.content.lootbag.LootingBag;
import io.Odyssey.content.skills.crafting.BryophytaStaff;
import io.Odyssey.content.skills.hunter.impling.Impling;
import io.Odyssey.content.skills.runecrafting.Pouches;
import io.Odyssey.content.teleportation.TeleportTablets;
import io.Odyssey.model.Items;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.items.GodBooks;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.util.Misc;

import java.util.Objects;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemOptionTwo implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		if (player.getMovementState().isLocked())
			return;
		player.interruptActions();
		int itemId = player.getInStream().readUnsignedWord();

		if (player.debugMessage) {
			player.sendMessage(String.format("ItemClick[item=%d, option=%d, interface=%d, slot=%d]", itemId, 2, -1, -1));
		}

		if (player.getLock().cannotClickItem(player, itemId))
			return;
		if (!player.getItems().playerHasItem(itemId, 1))
			return;
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return;
		}
		if (LootingBag.isLootingBag(itemId)) {
			player.getLootingBag().openDepositMode();
			return;
		}
		if(GodBooks.check(player,itemId)){
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			player.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		if (JarsToPoints.open(player, itemId)) {
			return;
		}

		if (BryophytaStaff.handleItemOption(player, itemId, 2))
			return;
		if (SanguinestiStaff.clickItem(player, itemId, 2)) {
			return;

		}
		if (AccursedScepter.clickItem(player, itemId, 2)) {
			return;
		}


		TeleportTablets.operate(player, itemId);
		ItemDef def = ItemDef.forId(itemId);
		switch (itemId) {

			case 11866://8
			case 11867://7
			case 11868://6
			case 11869://5
			case 11870://4
			case 11871://3
			case 11872://2
			case 11873://1
				if(player.getSlayer().getMaster() != 0) {
						player.getItems().deleteItem2(itemId, 1);
						if(itemId >= 11866 && itemId <= 11872)
							player.getItems().addItem(itemId + 1, 1);

				}
				player.getSlayer().teletoMaster(player.getSlayer().getMaster());

				break;
			case 11980:
			case 2572:
			case 11982:
			case 11984:
			case 11986:
			case 11988:
			case 12785:
				player.getDH().sendOption2("Kill Log", "Toggle currency pickup");
				player.dialogueAction = 11980;


				break;

			case 12885:
			case 13277:
			case 19701:
			case 13245:
			case 12007:
			case 22106:
			case 12936:
			case 24495:
			player.getDH().sendDialogues(361, Npcs.BOSS_POINT_SHOP);
			break;
		case 21183:
			player.sendMessage("Your bracelet of slaughter has @red@"+ player.slaughterCharge +"@bla@ charges left.");
			break;
		case 20714:
			int pages = player.getTomeOfFire().getPages();
			int charges = player.getTomeOfFire().getCharges();
			player.sendMessage("You currently have "+ pages +" pages and " + charges + " charges left in your tome of fire.");
			break;

		case 26258: //h'ween sweater reverse
			 player.getItems().deleteItem(26258, 1);
			 player.getItems().addItem(26256, 1);
			 break;
		case 26256:
			 player.getItems().deleteItem(26256, 1);
			 player.getItems().addItem(26258, 1);
			 break;
		case 30083:
			 player.getPA().showInterface(44942);
			 break;
	 	case 26471:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to use this command.");
					return;
				}
				player.getItems().deleteItem(26471, 1);
				player.getItems().addItem(13073, 1);
				player.getItems().addItem(26421, 1);
				player.sendMessage("@blu@You have dismantled your elite void.");
				break;
			case 26469:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to use this command.");
					return;
				}
				player.getItems().deleteItem(26469, 1);
				player.getItems().addItem(13072, 1);
				player.getItems().addItem(26421, 1);
				player.sendMessage("@red@You have dismantled your elite void.");
				break;
			case 26475:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to use this command.");
					return;
				}
				player.getItems().deleteItem(26475, 1);
				player.getItems().addItem(11664, 1);
				player.getItems().addItem(26421, 1);
				player.sendMessage("@blu@You have dismantled your void.");
				break;
			case 26477:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to use this command.");
					return;
				}
				player.getItems().deleteItem(26477, 1);
				player.getItems().addItem(11665, 1);
				player.getItems().addItem(26421, 1);
				player.sendMessage("@blu@You have dismantled your void.");
				break;
			case 26473:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to use this command.");
					return;
				}
				player.getItems().deleteItem(26473, 1);
				player.getItems().addItem(11663, 1);
				player.getItems().addItem(26421, 1);
				player.sendMessage("@blu@You have dismantled your void.");
				break;
			case 25865:
			case 25867:
			case 25884:
			case 29988:
			case 29985:
			case 25886:
			case 25888:
			case 25890:
			case 25892:
				if (player.getItems().freeSlots() < 2) {
					player.sendMessage("You need at least two free slots to do this.");
					return;
				}
				if(player.crystalCharge == 0){
					player.sendMessage("There no crystal shards in your bow.");
					return;
				}
				player.getItems().addItem(23877, player.crystalCharge);
				player.sendMessage("@blu@You have removed @red@"+player.crystalCharge +"@blu@ Crystal Shards into your inventory");
				player.crystalCharge = 0;
				break;
			case 21817:
				player.getDH().sendOption2("Turn absorption on", "Turn absorption off");
				player.dialogueAction = 21817;
				break;
			case 21816:
				player.sendMessage("@blu@You have @red@"+player.braceletEtherCount +"@blu@ in your bracelet.");
				break;
//			case 21816:
//		 if (player.getItems().freeSlots() < 2) {
//			 player.sendMessage("You need at least two free slots.");
//             return;
//         }
//		if (player.braceletEtherCount <= 0) {
//			player.getItems().deleteItem(21816, 1);
//			player.getItems().addItem(21817, 1);
//			return;
//		}
//		player.getItems().addItem(21820, player.braceletEtherCount);
//		player.braceletDecrease(player.braceletEtherCount);
//		player.sendMessage("@blu@You have removed @red@"+player.braceletEtherCount +"@blu@ ether into your inventory");
//		if (player.braceletEtherCount <= 0) {
//			player.getItems().deleteItem(21816, 1);
//			player.getItems().addItem(21817, 1);
//			return;
//		}
//		break;
		case 7509:
            if (player.getPosition().inDuelArena() || Boundary.isIn(player, Boundary.DUEL_ARENA)) {
            	player.sendMessage("You cannot do this here.");
                return;
            }
            if (player.getHealth().getStatus().isPoisoned() || player.getHealth().getStatus().isVenomed()) {
            	player.sendMessage("You are effected by venom or poison, you should cure this first.");
                return;
            }
            if (player.getHealth().getCurrentHealth() <= 10 && player.getHealth().getCurrentHealth() > 1 ) {
                player.appendDamage(1, Hitmark.HIT);
                player.forcedChat("URGHHHHH!");
                return;
            }
            if (player.getHealth().getCurrentHealth() <= 1) {
            	player.sendMessage("I better not do that.");
                return;
            }
            player.forcedChat("URGHHHHH!");
            player.startAnimation(829);
			player.getPA().sendSound(1018);
            int currentHealth = player.getHealth().getCurrentHealth() / 10;
            player.appendDamage(currentHealth, Hitmark.HIT);
            break;
		case 9762:
			if (!Boundary.isIn(player, Boundary.EDGEVILLE_PERIMETER)) {
				player.sendMessage("This cape can only be operated within the edgeville perimeter.");
				return;
			}
			if (player.getPosition().inWild()) {
				return;
			}
				if (player.playerMagicBook == 0) {
					player.playerMagicBook = 1;
					player.setSidebarInterface(6, 838);
					player.autocasting = false;
					player.sendMessage("An ancient wisdom fills your mind.");
					player.getPA().resetAutocast();
				} else if (player.playerMagicBook == 1) {
					player.sendMessage("You switch to the lunar spellbook.");
					player.setSidebarInterface(6, 29999);
					player.playerMagicBook = 2;
					player.autocasting = false;
					player.autocastId = -1;
					player.getPA().resetAutocast();
				} else if (player.playerMagicBook == 2) {
					player.setSidebarInterface(6, 938);
					player.playerMagicBook = 0;
					player.autocasting = false;
					player.sendMessage("You feel a drain on your memory.");
					player.autocastId = -1;
					player.getPA().resetAutocast();
				}
				break;
		/*case 10556:
        	player.sendMessage("@red@Attacker Icon gives 10% on max melee hit.");
        	break;
        case 10557:
        	player.sendMessage("@red@Collector Icon gives 7% chance boost on pets.");
        	break;
        case 10558:
        	player.sendMessage("@red@Defender Icon will reduce protect prayers by 5%.");
        	break;
        case 10559:
        	player.sendMessage("@red@Collector Icon shares the same effects as guthans.");
        	break;*/
		case 10832:
			player.getCoinBagSmall().openall();
			break;
		case 10833:
			player.getCoinBagMedium().openall();
			break;
		case 10834:
			player.getCoinBagLarge().openall();
			break;
		case 10835:
			player.getCoinBagBuldging().openall();
			break;
		case 9780:
			player.getPA().spellTeleport(3810, 3550, 0, false);
			player.sendMessage("You have teleported to the Crafting Shop.");
			break;

		case Items.VIGGORAS_CHAINMACE: // Checking charges for pvp weapons
		case Items.THAMMARONS_SCEPTRE:
		case Items.CRAWS_BOW:
		case Items.URASINE_CHAINMACE: // Checking charges for pvp weapons
		case Items.ACCURSED_SCEPTRE:
		case Items.WEBWEAVER_BOW:
			PvpWeapons.handleItemOption(player, itemId, 2);
			break;

		case 29981:
		case 6828:
		case 29979:
			//player.getPA().sendFrame126("https://www.sovark.com/topic/23-mystery-box-drop-tables/", 12000);
			break;
		case 21347:
			player.boltTips = false;
			player.arrowTips = true;
			player.javelinHeads = false;
			player.sendMessage("Your Amethyst method is now Arrowtips!");
			break;

		case 11238:
		case 11240:
		case 11242:
		case 11244:
		case 11246:
		case 11248:
		case 11250:
		case 11252:
		case 11254:
		case 11256:
		case 19732:
			Impling.getReward(player, itemId);
			break;
		case 20164: //Spade
		case 20243:
				if (System.currentTimeMillis() - player.lastPerformedEmote < 2500)
					return;			
				player.startAnimation(7268);
				player.lastPerformedEmote = System.currentTimeMillis();
			break;
		
		case 13136:
			if (player.getPosition().inClanWars() || player.getPosition().inClanWarsSafe()) {
				player.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			if (Server.getMultiplayerSessionListener().inAnySession(player)) {
				player.sendMessage("You cannot do that right now.");
				return;
			}
			if (player.wildLevel > Configuration.NO_TELEPORT_WILD_LEVEL) {
				player.sendMessage("You can't teleport above level " + Configuration.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return;
			}
			player.getPA().spellTeleport(3426, 2927, 0, false);
			break;
		
		case 13117:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(4);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13118:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(2);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13119:
		case 13120:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(1);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
			
		case 13111:
			if (player.getRechargeItems().useItem(itemId)) {
				player.getPA().spellTeleport(3236, 3946, 0, false);
			}
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
			player.getDH().sendDialogues(76, 1);
			break;

			case 11802:
			case 11804:
			case 11806:
			case 11808:
				Godswords.dismantle(player,itemId);
				break;

		case 13226:
			player.getHerbSack().withdrawAll();
			break;
			
		case 12020:
			player.getGemBag().check();
			break;
			
		case 5509:
			Pouches.check(player, 0);
			break;
		case 5510:
			Pouches.check(player, 1);
			break;
		case 5512:
			Pouches.check(player, 2);
			break;
		case 12904:
			player.sendMessage("The toxic staff of the dead has " + player.getToxicStaffOfTheDeadCharge() + " charges remaining.");
			break;
		case 13199:
		case 13197:
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charges remaining.");
			break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? player.getTridentCharge() : player.getToxicTridentCharge();
			player.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
			break;
		case 12926:
			player.getCombatItems().checkBlowpipeShotsRemaining();
			break;

		case 12931:
			def = ItemDef.forId(itemId);
			if (def == null) {
				return;
			}
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charge remaining.");
			break;
		case 8901:
			player.getPA().assembleSlayerHelmet();
			break;
		case 19675:

			player.getDH().sendItemStatement("Your arc light has "+player.getArcLightCharge()+ " charges.",19675);
			break;
		case 11283:
		case 11285:
		case 11284:
			player.sendMessage("Your dragonfire shield currently has " + player.getDragonfireShieldCharge() + " charges.");
			break;
		case 4155:
			player.openslayerpartner();

			break;
		default:
			if (player.getRights().isOrInherits(Right.OWNER)) {
				Misc.println("[DEBUG] Item Option #2-> Item id: " + itemId);
			}
			break;

		}

	}

}
