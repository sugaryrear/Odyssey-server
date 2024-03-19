package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ImmutableItem;

/**
 * @author Arthur Behesnilian 1:26 PM
 */
public class Cash extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        player.getInventory().addToInventory(new ImmutableItem(Items.COINS, Integer.MAX_VALUE));
        player.sendMessage("You spawn a stack of cash.");
    }

}
