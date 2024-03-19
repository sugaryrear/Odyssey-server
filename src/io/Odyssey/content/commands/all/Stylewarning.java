package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.combat.core.StyleWarning;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Stylewarning extends Command {

    @Override
    public void execute(Player player, String commandName, String input) {
        boolean enabled = player.getAttributes().flipBoolean(StyleWarning.STYLE_WARNING_TOGGLE_KEY);
        StringBuilder builder = new StringBuilder();
        builder.append("Style warning is now set to ");
        if (enabled) {
            builder.append("on. You will receive warning messages.");
        } else {
            builder.append("off. You will no longer receive warning messages.");
        }
        player.sendMessage(builder.toString());
    }
    @Override
    public Optional<String> getDescription() {
        return Optional.of("Toggle optimal combat style warning.");
    }
}
