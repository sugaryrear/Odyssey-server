package io.Odyssey.content.commands.admin;

import io.Odyssey.content.worldevent.impl.AvatarOfCreationWorldEvent;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.model.entity.player.Player;


public class Starttheavatarofcreation extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        WorldEventContainer.getInstance().startEvent(new AvatarOfCreationWorldEvent());
        player.sendMessage("AvatarOfCreation will start soon.");
    }
}
