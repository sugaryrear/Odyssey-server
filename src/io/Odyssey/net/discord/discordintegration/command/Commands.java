package io.Odyssey.net.discord.discordintegration.command;


import io.Odyssey.net.discord.discordintegration.Bot;
import io.Odyssey.net.discord.discordintegration.command.impl.Players;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Role;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


/*
 * @project Vanity-Server
 * @author Patrity - https://github.com/Patrity
 * Created on - 4/13/2020
 */
@Getter
public enum Commands {

    //User commands

    PLAYERS("players", "Displays online player count", new Players(), null),
    ;

    private final String command, description;
    private final ListenerAdapter adapter;
    private final String[] rolesCanUse;

    public static final Commands[] VALUES = Commands.values();

    public static String prefix = Bot.PREFIX;

    Commands(String command, String description, ListenerAdapter adapter, String[] rolesCanUse) {
        this.command = command;
        this.description = description;
        this.adapter = adapter;
        this.rolesCanUse = rolesCanUse;
    }

    public static Commands isCommand(MessageReceivedEvent e) {
        String text = e.getMessage().getContentRaw().toLowerCase();
        for (Commands command : Commands.VALUES) {
            if (text.contains(prefix + command.getCommand())) {
                for (Role roles : e.getMember().getRoles()) {
                    if (command.getRolesCanUse() == null)
                        return command;

//                    if (command == PLAYER_PASS) {
//                        if (roles.getId().contains("945869673306656829")
//                        || roles.getId().contains("945871923605287002")) {
//                            return command;
//                        }
//                    }

                    for (String role : command.getRolesCanUse()) {
                        if (roles.getId().contains(role == "802140111332311042" ? "824740072058257449" : role)) {
                            return command;
                        }
                    }
                }
            }
        }
        return null;
    }
}
