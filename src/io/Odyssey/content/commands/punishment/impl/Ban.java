package io.Odyssey.content.commands.punishment.impl;

import io.Odyssey.content.commands.punishment.OnlinePlayerPunishmentPCP;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.punishments.PunishmentType;
import io.Odyssey.util.dateandtime.TimeSpan;

public class Ban extends OnlinePlayerPunishmentPCP {

    @Override
    public String name() {
        return "ban";
    }

    @Override
    public PunishmentType getPunishmentType() {
        return PunishmentType.BAN;
    }

    @Override
    public void onPunishment(Player staff, Player player, TimeSpan duration) {
        player.forceLogout();
    }

    @Override
    public void onRemovePunishment(Player staff, Player player) { }

    @Override
    public String extract(Player player) {
        return player.getDisplayNameLower();
    }
}
