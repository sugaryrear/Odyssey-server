package io.Odyssey.model.entity.player.packets;

import java.util.Objects;
import java.util.Optional;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.combat.magic.AccursedScepter;
import io.Odyssey.content.combat.magic.SanguinestiStaff;
import io.Odyssey.content.combat.magic.items.ItemCombinations;
import io.Odyssey.content.combat.magic.TumekensShadow;
import io.Odyssey.content.combat.magic.items.PvpWeapons;
import io.Odyssey.content.miniquests.magearenaii.MageArenaII;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanRepository;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemCombination;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.shops.ShopAssistant;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.player.ItemDroppedLog;

/**
 * Drop Item Class
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getMovementState().isLocked() || c.getLock().cannotInteract(c))
			return;
		if (c.isFping()) {
			/**
			 * Cannot do action while fping
			 */
			return;
		}
		c.interruptActions();
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();

		if (c.debugMessage) {
			c.sendMessage(String.format("DropItem[item=%d, slot=%d]", itemId, slot));
		}

		if (!c.getItems().isItemInInventorySlot(itemId, slot))
			return;

		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (!c.getItems().playerHasItem(itemId)) {
			return;
		}
		if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
			return;
		}
		if (c.isStuck) {
			c.isStuck = false;
			c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return;
		}
		if (c.isNpc) {
			return;
		}
		if ((Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_LOBBY) || Boundary.isIn(c, Boundary.OUTLAST_LOBBY)) &&
				(itemId != 13341 || itemId != 11936 || itemId != 3144 || itemId != 385 )) {
			c.getItems().deleteItem2(itemId, 1);
			return;
		}
		switch(itemId){
			case Items.VIGGORAS_CHAINMACE: // Uncharging pvp weapons
			case Items.THAMMARONS_SCEPTRE:
			case Items.CRAWS_BOW:
			case Items.URASINE_CHAINMACE:
			case Items.ACCURSED_SCEPTRE:
			case Items.WEBWEAVER_BOW:
				PvpWeapons.handleItemOption(c, itemId, 3);
				break;
		}

		if (itemId == Items.SANGUINESTI_STAFF) {
			SanguinestiStaff.clickItem(c, itemId, 5);
			return;
		}
		if (itemId == Items.ACCURSED_SCEPTRE) {
			AccursedScepter.clickItem(c, itemId, 5);
			return;
		}
		if (itemId == Items.TUMEKENS_SHADOW)	 {
			TumekensShadow.clickItem(c, itemId, 5);
		}
		if (Boundary.isIn(c, Boundary.LONE_SURVIVER_HUT)) {
			c.sendMessage("Please leave the Lone Survivor hut area to drop your items.");
			return;
		}
		if (itemId == 9699) {
			c.getItems().deleteItem2(9699, 1);
			return;
		}
		if (itemId == 9698) {
			c.getItems().deleteItem2(9698, 1);
			return;
		}
		if (itemId == 9017) {
			c.getItems().deleteItem2(9017, 1);
			return;
		}
		if (itemId == 23783) {
			c.getItems().deleteItem2(23783, 1);
			return;
		}
		if (itemId == 23778) {
			c.getItems().deleteItem2(23778, 1);
			return;
		}

		ItemDef itemDef = ItemDef.forId(itemId);

		if (itemDef.isDestroyable() || MageArenaII.isUntradable(itemId)) {
			c.getPA().destroyInterface(new ItemToDestroy(itemId, slot, DestroyType.DESTROY));
			return;
		}

		if (!itemDef.isDroppable()) {
			c.sendMessage("You can't drop this item!");
			return;
		}

		if (!Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)) {
			int amount = c.getItems().getItemAmount(itemId);
			ItemDef def = itemDef;
			if (def.getShopValue() * amount > 100000 ) {
				DiscordMessager.sendLogs("[DROP]" + c.getDisplayName() + " dropped " + def.getName() + " x " + Misc.insertCommas(amount) + " at " + c.absX + ", " + c.absY);
			}
		}

		if (itemDef.isCheckBeforeDrop()) {
			c.destroyingItemId = itemId;
			c.getDH().sendDialogues(858, 7456);
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
			for (int item : Configuration.TOURNAMENT_ITEMS_DROPPABLE) {
				if ((Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)  || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_AREA))&& (item == itemId)) {
					c.getItems().deleteItem(itemId, 1);
					c.sendMessage("Your food dissapears as it hits the floor..");
					return;
				}
			}
			if (c.itemId == 5509 || c.itemId == 5510 || c.itemId == 5512 || c.itemId == 5514 || c.itemId == 6819 ||
					c.itemId == 13199 || c.itemId == 12931 || c.itemId == 13197) {
					c.getDH().sendDialogues(858, 7456);
					return;
				}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		PetHandler.Pets pet = PetHandler.forItem(itemId);

		if (pet != null) {
			c.startAnimation(827);
			PetHandler.spawn(c, pet, false, false);
			return;
		}

		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}

		if (c.playerAttackingIndex > 0 && !TourneyManager.getSingleton().isInArena(c)) {
			c.sendMessage("You can't drop items during player combat.");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		if (slot >= c.playerItems.length || slot < 0 || slot >= c.playerItems.length) {
			return;
		}
		if (itemId == 21816) {
			int uncharged = 21817;
			int charge = c.braceletEtherCount;
			if (charge <= 0) {
				c.sendMessage("The bracelet has no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(uncharged, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().replaceItem(c,itemId, uncharged);
			c.getItems().addItem(21820, charge);
			c.braceletEtherCount-=charge;
			return;
		}
		switch (itemId) {

		case 19722:
			c.getItems().deleteItem(19722, 1);
			c.getItems().addItem(12954, 1);
			c.getItems().addItem(20143, 1);
			c.sendMessage("Your trimmed dragon defender has turned into regular again.");
			break;
		}

		if (itemId == 12904) {
			if (c.getToxicStaffOfTheDeadCharge() <= 0) {
				c.getItems().deleteItem2(12904, 1);
				c.getItems().addItem(12902, 1);
				c.sendMessage("The staff had no charge, but has been reverted to uncharged.");
				return;
			}
			if (c.getItems().freeSlots() <= 0) {
				c.sendMessage("You need one free slot to do this.");
				return;
			}
			c.getItems().deleteItem2(12904, 1);
			c.getItems().addItem(12902, 1);
			c.getItems().addItem(12934, c.getToxicStaffOfTheDeadCharge());
			c.setToxicStaffOfTheDeadCharge(0);
			c.sendMessage("You uncharged the toxic staff of the dead and retain.");
		}

		if (itemId == 12926) {
			int ammo = c.getToxicBlowpipeAmmo();
			int amount = c.getToxicBlowpipeAmmoAmount();
			int charge = c.getToxicBlowpipeCharge();
			if (ammo > 0 && amount > 0) {
				c.sendMessage("You must unload before you can uncharge.");
				return;
			}

			c.sendMessage("ammo: " + ammo);
			c.sendMessage("ammo amount: " + amount);
			c.sendMessage("charge: " + charge);
			if (charge <= 0) {
				c.sendMessage("The toxic blowpipe had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(12924, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12924, 1);
			c.getItems().addItem(12934, charge);
			c.setToxicBlowpipeAmmo(0);
			c.setToxicBlowpipeAmmoAmount(0);
			c.setToxicBlowpipeCharge(0);
			return;
		}

		if (itemId == 12931 || itemId == 13199 || itemId == 13197) {
			int uncharged = itemId == 12931 ? 12929 : itemId == 13199 ? 13198 : 13196;
			int charge = c.getSerpentineHelmCharge();
			if (charge <= 0) {
				c.sendMessage("The serpentine helm had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(uncharged, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(uncharged, 1);
			c.getItems().addItem(12934, charge);
			c.setSerpentineHelmCharge(0);
			return;
		}

		Optional<ItemCombination> revertableItem = ItemCombinations.isRevertable(new GameItem(itemId));

		if (revertableItem.isPresent()) {
			// revertableItem.get().sendRevertConfirmation(c);
			revertableItem.get().revert(c);
			c.dialogueAction = 555;
			c.nextChat = -1;
			return;
		}

		if (!itemDef.isDroppable())
			return;

		if (c.underAttackByPlayer > 0) {
			if (ShopAssistant.getItemShopValue(itemId) > 1000000 && !Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)) {
				c.sendMessage("You may not drop items worth more than 1000000 while in combat.");
				return;
			}
		}

//		if (c.showDropWarning()) {
//			c.destroyItem = new ItemToDestroy(itemId, slot, DestroyType.DROP);
//			c.getPA().destroyInterface("drop");
//			return;
//		}

		dropItem(c, itemId, slot);
	}

	public static void dropItem(Player c, int itemId, int itemSlot) {
		if (!c.getItems().isItemInInventorySlot(itemId, itemSlot) || c.isDead)
			return;
//if(itemId == 3695){//pet rock
//
//}
		//Server.getLogging().write(new ItemDroppedLog(c, new GameItem(itemId, c.playerItemsN[itemSlot]), c.getPosition()));
		Server.itemHandler.createGroundItemFromDrop(c, itemId, c.absX, c.absY, c.heightLevel,
				c.playerItemsN[itemSlot], c.getIndex());
		c.getItems().deleteItem(itemId, itemSlot, c.playerItemsN[itemSlot]);
		c.getPA().removeAllWindows();
		c.getPA().sendSound(376);

		// Gim drop log
		GroupIronmanRepository.getGroupForOnline(c).ifPresent(group -> group.addDropItemLog(c, new GameItem(itemId, itemSlot)));
	}
}
