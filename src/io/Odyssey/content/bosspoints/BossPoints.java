package io.Odyssey.content.bosspoints;

import com.fasterxml.jackson.core.type.TypeReference;
import io.Odyssey.Server;
import io.Odyssey.annotate.Init;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.leaderboards.LeaderboardType;
import io.Odyssey.content.leaderboards.LeaderboardUtils;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.JsonUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Log
public class BossPoints {

    private static final Logger logger = LoggerFactory.getLogger(BossPoints.class);

    @Data
    private static final class BossPointEntry {
        private final String name;
        private final int points;

        /**
         * Are the points awarded manually (not on npc death)
         */
        private final boolean manual;

        private BossPointEntry() {
            name = "";
            points = 0;
            manual = false;
        }
    }

    private static final List<BossPointEntry> ENTRIES = new ArrayList<>();

    @Init
    public static void init() throws IOException {
        ENTRIES.clear();
      //  System.out.println("ere");
        List<BossPointEntry> list = JsonUtil.fromYaml(Server.getDataDirectory() + "/cfg/npc/boss_points.yaml", new TypeReference<List<BossPointEntry>>() {});
        ENTRIES.addAll(list);
    }

    public static int getPointsOnDeath(NPC npc) {
        return getPoints((entry) -> !entry.isManual() && entry.getName().equalsIgnoreCase(npc.getDefinition().getName()));
    }

    public static int getManualPoints(String name) {
        int points = getPoints((entry) -> entry.isManual() && entry.getName().equalsIgnoreCase(name));
        if (points == 0) {
            logger.warn("No manual points for name: " + name);
        }
        return points;
    }

    public static int getPoints(Predicate<BossPointEntry> predicate) {
        return ENTRIES.stream().filter(predicate).mapToInt(it -> it.points).sum();
    }

    public static void addManualPoints(Player player, String name) {
        addPoints(player, getManualPoints(name), true);
    }

    public static void addPoints(Player player, int points, boolean message) {
        if (points > 0) {
            //add double drops here
            if (AvatarOfCreation.activeBuchuSeed) {
                points *= 2;
            }
            if(player.getItems().isWearingItem(8813) && player.getItems().isWearingItem(8814)){
                points += 0.10;
                player.sendMessage("@red@<shad=2>Your Corrupted Void gives you 10% extra");
            }

            player.bossPoints += points;
            player.getQuestTab().updateInformationTab();
            player.getEventCalendar().progress(EventChallenge.GAIN_X_BOSS_POINTS, points);
           LeaderboardUtils.addCount(LeaderboardType.BOSS_POINTS, player, points);
            if (message) {
                player.sendMessage("Gained <col=FF0000>" + points + "</col> boss points.");
            }
        }
    }

    /**
     * Had an issue through june 1-2 2021 where boss points were wiped on logout.
     */
    public static void doRefund(Player player) {
        if (!player.bossPointsRefund) {
            int refund = ENTRIES.stream().mapToInt(it -> player.getNpcDeathTracker().getKc(it.getName().toLowerCase()) * it.getPoints()).sum();
            player.bossPointsRefund = true;
            if (refund > 0) {
                player.bossPoints += refund;
                player.sendMessage("Refunded " + refund + " boss points, sorry for the inconvenience!");
            }
        }
    }
}

