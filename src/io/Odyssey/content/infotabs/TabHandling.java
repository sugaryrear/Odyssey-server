package io.Odyssey.content.infotabs;

import io.Odyssey.content.bonus.DoubleExperience;
import io.Odyssey.content.boosts.BoostType;
import io.Odyssey.content.boosts.Booster;
import io.Odyssey.content.boosts.Boosts;
import io.Odyssey.content.boosts.XPBoostInformation;
import io.Odyssey.content.combat.stats.MonsterKillLog;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.content.worldevent.WorldEventInformation;
import io.Odyssey.model.entity.npc.drops.DropManager;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TabHandling {

    private final Player player;

    public TabHandling(Player player) {
        this.player = player;
    }

    public boolean handleTabSwitch(int buttonId) {
        switch (buttonId) {
            case 136186:
                player.getPA().sendInterfaceHidden(0, 35018);
                player.getPA().sendInterfaceHidden(1, 35070);
                player.getPA().sendInterfaceHidden(1, 35119);
            //    updateInfoTab();
                return true;
            case 136189:
                player.getPA().sendInterfaceHidden(0, 35070);
                player.getPA().sendInterfaceHidden(1, 35018);
                player.getPA().sendInterfaceHidden(1, 35119);
                return true;
            case 136192:
                player.getPA().sendInterfaceHidden(0, 35119);
                player.getPA().sendInterfaceHidden(1, 35070);
                player.getPA().sendInterfaceHidden(1, 35018);
                return true;
            case 136195:
                player.getPA().sendInterfaceHidden(1, 35119);
                player.getPA().sendInterfaceHidden(1, 35070);
                player.getPA().sendInterfaceHidden(1, 35018);
                return true;

            case 136215:
                WorldEventInformation.openInformationInterface(player);
                return true;
            case 136230:
                XPBoostInformation.openInformationInterface(player);
                return true;
            case 136255:
                player.getCollectionLog().openInterface(player);
                return true;
            case 137003:
                MonsterKillLog.openInterface(player);
                return true;
        }
        return false;
    }

    public void updateInfoTab() {
        String rank = "Player";
        if (!player.getRights().getPrimary().equals(Right.PLAYER)) {
            rank = player.getRights().buildCrownString() + " " + player.getRights().getPrimary().getFormattedName();
        }
        player.getPA().sendString(35023, "@gre@" + PlayerHandler.getPlayerCount());

        int playersInWild = Boundary.getPlayersInBoundary(Boundary.WILDERNESS);
        player.getPA().sendString(35029, "@gre@" + playersInWild);

        String events = WorldEventContainer.getInstance().getCurrentActiveEvent();
        player.getPA().sendString("@gre@" + events, 35035);
        player.getPA().sendString("@gre@" + WorldEventContainer.getInstance().getTimeUntilWildEvent(), 35040);
        player.getPA().sendString("@gre@" + WorldEventContainer.getInstance().getTimeUntilLoneSurvivor(), 35045);


        List<? extends Booster<?>> boosts = Boosts.getBoostsOfType(player, null, BoostType.EXPERIENCE);
        if (!boosts.isEmpty() && boosts.size() > 1) {
            player.getPA().sendString("Multiple Server XP Boosts Acitve", 35049);
        } else {
            player.getPA().sendString(DoubleExperience.isDoubleExperience() ? "+50% XP Bonus Weekend" : "Inactive Server XP Boosts", 35049);
        }

        player.getPA().sendString(35054, "@gre@" + rank);
        player.getPA().sendString(35059, "@gre@$" + player.amDonated);
        player.getPA().sendString(35064, "@gre@" + DropManager.getModifier1(player));

        long milliseconds = (long) player.playTime * 600;
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
        String time = days + " days, " + hours + " hrs";
        player.getPA().sendString(35069, "@gre@" + time);

    }

}
