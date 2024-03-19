package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.SkillcapePerks;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Restorehealth extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        c.getHealth().tick(SkillcapePerks.HITPOINTS.isWearing(c) || SkillcapePerks.isWearingMaxCape(c) ? 2 : 1);
    }
}