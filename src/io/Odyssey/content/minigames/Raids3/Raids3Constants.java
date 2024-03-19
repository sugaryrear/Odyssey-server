package io.Odyssey.content.minigames.Raids3;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public class Raids3Constants {

    public static List<Raids3> raids3Games = Lists.newArrayList();

    public static int currentRaids3Height = 0;

    public static int getCurrentRaids3Height1 = 1;

    public static void checkInstances() {
        Lists.newArrayList(raids3Games).stream().filter(Objects::nonNull).forEach(raids3 -> {
            if(raids3.getPlayers().size() == 0 && System.currentTimeMillis() - raids3.lastActivity > 60000) {
                raids3.killAllSpawns();
                System.out.println("raids3 destroyed");
                raids3Games.remove(raids3);
            }
        });
    }

    public static void checkLogin(Player player) {
        checkInstances();
        if (Boundary.isIn(player, Boundary.RAIDS3) || Boundary.isIn(player, Boundary.RAIDS3)) {
            boolean[] addedToGame = {false};
            Lists.newArrayList(raids3Games)
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(raids3 -> raids3.hadPlayer(player))
                    .findFirst()
                    .ifPresent(raids3 -> {
                        boolean added = raids3.login(player);
                        if(added) {
                            addedToGame[0] = true;
                        }
                    });
            if(!addedToGame[0]) {
                player.sendMessage("You logged out and were removed from the raids3 instance!");
                player.getPA().movePlayerUnconditionally(1247, 3559, 0);
                player.setRaids3Instance(null);
                player.specRestore = 120;
                player.specAmount = 10.0;
                player.setRunEnergy(10000, true);
                player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
                player.getPA().refreshSkill(Player.playerPrayer);
                player.getHealth().removeAllStatuses();
                player.getHealth().reset();
                player.getPA().refreshSkill(5);
            }
        }

    }

}
