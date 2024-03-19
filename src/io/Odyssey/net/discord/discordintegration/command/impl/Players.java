package io.Odyssey.net.discord.discordintegration.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

/*
 * @project Vanity-Server
 * @author Patrity - https://github.com/Patrity
 * Created on - 4/13/2020
 */
public class Players  extends ListenerAdapter {


    public void onGuildMessageReceived(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        int players = 55;
        eb.setTitle("Players");
        eb.setDescription("There are currently " + players + " players online!");

        eb.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        eb.setColor(new Color(0x296d98));
        e.getChannel().sendMessage((CharSequence) eb.build()).queue();
    }
}
