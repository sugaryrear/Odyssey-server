package io.Odyssey.content.commands.all;


import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.NewStore;
import io.Odyssey.sql.NewStore_onlogin;
import io.Odyssey.sql.NewVote;

import java.sql.*;

public class Checkdono extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        new NewStore_onlogin(c,true).run();
    }

}
