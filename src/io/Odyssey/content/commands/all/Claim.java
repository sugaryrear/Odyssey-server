package io.Odyssey.content.commands.all;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.sql.NewStore;
import io.Odyssey.sql.donation.query.ClaimDonationsQuery;
import io.Odyssey.sql.donation.model.DonationItem;
import io.Odyssey.sql.donation.model.DonationItemList;
import io.Odyssey.sql.donation.query.GetDonationsQuery;
import io.Odyssey.util.logging.player.DonatedLog;

/**
 * Changes the password of the player.
 *
 * @author Emiel
 *
 */
public class Claim extends Command {

	public static void claimDonations(Player player) {
		System.out.println("name: "+player.getLoginName());
		Server.getDatabaseManager().exec(Server.getConfiguration().getStoreDatabase(), (context, connection) -> {
			DonationItemList donationItemList = new GetDonationsQuery(player.getLoginName()).execute(context, connection);

			player.addQueuedAction(plr -> {
				List<DonationItem> claimed = new ArrayList<>();

				try {
					donationItemList.newDonations().forEach(item -> {
						if (giveDonationItem(player, item)) {
							claimed.add(item);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (!claimed.isEmpty()) {
						Server.getDatabaseManager().exec(Server.getConfiguration().getStoreDatabase(), new ClaimDonationsQuery(player, claimed));
					}
				}
			});

			return null;
		});
	}

	public static boolean giveDonationItem(Player plr, DonationItem item) {
		int itemId = item.getItemId();
		int itemQuantity = item.getItemAmount();
		if (plr.getItems().hasRoomInInventory(itemId, itemQuantity)) {
			plr.getItems().addItem(itemId, itemQuantity);
			Server.getLogging().write(new DonatedLog(plr, item));
			plr.getDonationRewards().increaseDonationAmount(item.getItemCost() * itemQuantity);
			plr.sendMessage("You've received x" + item.getItemAmount() + " " + item.getItemName());
			PlayerHandler.message(Right.OWNER, "@blu@[" + plr.getDisplayName() + "]@pur@ has just donated for " + itemQuantity + " " + item.getItemName() + "!");
			return true;
		} else {
			plr.sendMessage("Not enough room in inventory to claim " + item.getItemName() + ", make space and try again.");
			return false;
		}
	}

	@Override
	public void execute(Player c, String commandName, String input) {

	}


	@Override
	public Optional<String> getDescription() {
		return Optional.of("Claim your donated item.");
	}
}
