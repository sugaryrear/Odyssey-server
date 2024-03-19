package io.Odyssey.content.commands.test;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.util.Captcha;
import io.Odyssey.util.Misc;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Bots extends Command {

    private static int botCounter = 50;

    @Override
    public void execute(Player player, String commandName, String input) {
        if (!player.getRights().contains(Right.OWNER)) {
            player.sendMessage("Only owners can use this command.");
            return;
        }

        String[] args = input.split(" ");
        switch (args[0]) {
            case "spawn":
                int amount = Integer.parseInt(args[1]);
                player.sendMessage("Adding " + amount + " bots.");
                for (int i = 0; i < amount; i++) {
                    int x = 3089 + Misc.random(0, 25); // that would work
                    int y = 3487 + Misc.random(0, 25);// all good I was going to hid them here
                    Player.createBot("Bot " + botCounter++, Right.PLAYER, new Position(x, y));
                }
                break;
            case "talk":
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        getBots().forEach(it -> it.forcedChat(Captcha.generateCaptchaString()));
                    }
                }, 1);
                break;
            default:
                player.sendMessage("No actionable command with '{}'", args[0]);
        }
    }

    @NotNull
    private List<Player> getBots() {
        return PlayerHandler.nonNullStream().filter(Player::isBot).collect(Collectors.toList());
    }

    public Optional<String> getDescription() {
        return Optional.of("functions for bot players, ::bots spawn 10 to spawn.");
    }
}
