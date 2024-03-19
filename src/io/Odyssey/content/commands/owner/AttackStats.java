package io.Odyssey.content.commands.owner;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Misc;

public class AttackStats extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
//        player.setPrintAttackStats(!player.isPrintAttackStats());
//        player.sendMessage("Combat attack messages are now " + Misc.booleanToString(player.isPrintAttackStats()) + ".");
//
       // player.getnewteleInterface().open();
        player.ismeleeing = true;
        PlayerHandler.executeGlobalMessage("killfeed##"+player.getLoginName()+"##Sugary2##"+player.getStyle());

    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Prints out combat attack stats while in combat.");
    }
}
