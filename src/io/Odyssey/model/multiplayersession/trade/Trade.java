package io.Odyssey.model.multiplayersession.trade;

import java.util.Arrays;
import java.util.Objects;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.DiceHandler;
import io.Odyssey.content.minigames.pest_control.PestControl;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.multiplayersession.Multiplayer;
import io.Odyssey.model.multiplayersession.MultiplayerSession;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;

public class Trade extends Multiplayer {

	public static boolean requestable(Player player, Player requested) {
		if (requested == null) {
			player.sendMessage("The requested player cannot be found.");
			return false;
		}

		if (player.getLootingBag().isWithdrawInterfaceOpen() || player.getLootingBag().isDepositInterfaceOpen() ||
				requested.getLootingBag().isWithdrawInterfaceOpen() || requested.getLootingBag().isDepositInterfaceOpen()) {
			return false;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return false;
		}
		if (requested.getBankPin().requiresUnlock()) {
			return false;
		}
		if (Boundary.AvatarOfCreation.in(player) || Boundary.AvatarOfCreation.in(requested)) {
			player.sendMessage("You can't trade inside AvatarOfCreation.");
			return false;
		}
		if (Boundary.RAIDS_LOBBY.in(player) || Boundary.LOBBY.in(requested)) {
			player.sendMessage("You can't trade inside Raids Lobby.");
			return false;
		}
		if (!player.getMode().isTradingPermitted(player, requested)) {
			player.sendMessage("You are not permitted to trade other players.");
			return false;
		}
		if (!requested.getMode().isTradingPermitted(requested, player)) {
			player.sendMessage("That player is on a game mode that restricts trading.");
			return false;
		}
		if (!Server.isDebug()) {
			if (!player.getRights().isOrInherits(Right.GROUP_IRONMAN)) {
				if (!player.ignoreNewPlayerRestriction(requested)) {
					if (player.hasNewPlayerRestriction()) {
						player.sendMessage("You cannot request a trade, you must play for at least "
								+ Configuration.NEW_PLAYER_RESTRICT_TIME_MIN + " minutes.");
						return false;
					}
					if (requested.hasNewPlayerRestriction()) {
						player.sendMessage("You cannot trade this player, they have not played for "
								+ Configuration.NEW_PLAYER_RESTRICT_TIME_MIN + " minutes.");
						return false;
					}
				}
			}
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot request a trade whilst in a session.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(requested)) {
			player.sendMessage("This player is currently is a session with another player.");
			return false;
		}

		if (Boundary.isIn(player, PestControl.GAME_BOUNDARY) || Boundary.isIn(requested, PestControl.GAME_BOUNDARY)) {
			player.sendMessage("You cannot trade in the pest control minigame.");
			return false;
		}
		if (requested.getMode().isGroupIronman() && !player.getMode().isGroupIronman()) {
			player.sendMessage("You cannot trade with Group Ironman mode accounts.");
			return false;
		}
		
		return true;
	}
	
	public Trade(Player player) {
		super(player);
	}

	@Override
	public boolean requestable(Player requested) {
		if (Server.getMultiplayerSessionListener().requestAvailable(requested, player, MultiplayerSessionType.TRADE) != null) {
			player.sendMessage("You have already sent a request to this player.");
			return false;
		}
		return requestable(player, requested);
	}

	@Override
	public void request(Player requested) {
		if (Objects.isNull(requested)) {
			player.sendMessage("The player cannot be found, try again shortly.");
			return;
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return;
		}
		if (Boundary.isIn(player, Boundary.LONE_SURVIVER_HUT)) {
			player.sendMessage("Please leave the lone survivor hut area to trade.");
			return;
		}
		if (requested.underAttackByPlayer > 0 || requested.underAttackByNpc > 0 && requested.underAttackByNpc != requested.lastNpcAttacked && !requested.getPosition().inMulti()) {
			player.sendMessage("You cannot trade this person whilst he has been recently in combat or in multi.");
			return;
		}

		if (DiceHandler.inDicingArea(player) || DiceHandler.inDicingArea(requested)) {
			if (player.isGambleBanned()) {
				player.sendMessage("You cannot gamble.");
				return;
			}
			if (requested.isGambleBanned()) {
				player.sendMessage("That player cannot gamble.");
				return;
			}
		}

		player.faceUpdate(requested.getIndex());
		MultiplayerSession session = Server.getMultiplayerSessionListener().requestAvailable(player, requested, MultiplayerSessionType.TRADE);
		if (session != null) {
			session.getStage().setStage(MultiplayerSessionStage.OFFER_ITEMS);
			session.populatePresetItems();
			session.updateMainComponent();
			Server.getMultiplayerSessionListener().removeOldRequests(player);
			Server.getMultiplayerSessionListener().removeOldRequests(requested);
			session.getStage().setAttachment(null);
		} else {
			session = new TradeSession(Arrays.asList(player, requested), MultiplayerSessionType.TRADE);
			if (Server.getMultiplayerSessionListener().appendable(session)) {
				player.sendMessage("Sending trade offer...");
				requested.sendMessage(player.getDisplayName() + ":tradereq:");
				session.getStage().setAttachment(player);
				Server.getMultiplayerSessionListener().add(session);
			}
		}
	}

}
