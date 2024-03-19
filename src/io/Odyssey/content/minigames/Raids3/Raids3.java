package io.Odyssey.content.minigames.Raids3;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.commands.owner.Npc;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.instances.InstanceHeight;
import io.Odyssey.content.leaderboards.LeaderboardType;
import io.Odyssey.content.leaderboards.LeaderboardUtils;
import io.Odyssey.model.collisionmap.doors.Location;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.NpcStats;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Raids3 {

    private static final Logger logger = LoggerFactory.getLogger(Raids3.class);

    private static final String RAIDS3_DAMAGE_ATTRIBUTE_KEY = "cox_damage";
    private static final int RAIDS3_DAMAGE_FOR_REWARD = 550;

    public static int COMMON_KEY = 3456;
    public static int RARE_KEY = 3464;
    public long lastActivity = -1;
    private final Map<String, Long> playerLeftAt = Maps.newConcurrentMap();
    private final Map<String, Integer> raids3Players = Maps.newConcurrentMap();
    private final Map<String, Integer> activeRoom = Maps.newConcurrentMap();
    private List<Raids3Rank> ranks = null;
    private int groupPoints;

    public static boolean isMissingRequirements(Player c) {
        if (c.totalLevel < c.getMode().getTotalLevelNeededForRaids3()) {
            c.sendMessage("You need a total level of at least " + c.getMode().getTotalLevelNeededForRaids3() + " to join this raid!");
            return true;
        }

        return false;
    }

    public void filterPlayers() {
        raids3Players.entrySet().stream().filter(entry -> !PlayerHandler.getOptionalPlayerByDisplayName(entry.getKey()).isPresent()).forEach(entry -> raids3Players.remove(entry.getKey()));
    }

    public void removePlayer(Player player) {
        raids3Players.remove(player.getLoginNameLower());
        groupPoints = raids3Players.entrySet().stream().mapToInt(val -> val.getValue()).sum();
        if (raids3Players.isEmpty()) {
            lastActivity = System.currentTimeMillis();
        }
    }

    public List<Player> getPlayers() {
        List<Player> activePlayers = Lists.newArrayList();
        filterPlayers();
        raids3Players.keySet().stream().forEach(playerName -> {
            PlayerHandler.getOptionalPlayerByDisplayName(playerName).ifPresent(player -> activePlayers.add(player));
        });
        return activePlayers;
    }

    /**
     * Add points
     */
    public int addPoints(Player player, int points) {
        if (!raids3Players.containsKey(player.getLoginNameLower())) return 0;
        int currentPoints = raids3Players.getOrDefault(player.getLoginNameLower(), 0);
        raids3Players.put(player.getLoginNameLower(), currentPoints + points);
        groupPoints = raids3Players.entrySet().stream().mapToInt(val -> val.getValue()).sum();
        return currentPoints + points;
    }

    public int currentHeight;
    /**
     * The current path
     */
    private int path;
    /**
     * The current way
     */
    private int way;
    /**
     * Current room
     */
    public int currentRoom;
    private boolean chestRoomDoorOpen = true;
    private final int chestToOpenTheDoor = 5 + Misc.random(20);
    private final HashSet<Integer> chestRoomChestsSearched = new HashSet<>();
//	/**
//	 * Instance;
//	 */
    //private InstancedArea instance = null;
//
    /**
     * Monster spawns (No Double Spawning)
     */

    public boolean startroom;
    public boolean startroom2;
    public boolean pathofcrondis;
    public boolean tumekenswarden;
    public boolean pathofhet;
    public boolean akkha;

    public boolean akkhaDead;

    public boolean pathofapmeken;
    public boolean baba;
    public boolean kephri;
    public boolean zebak;
    public boolean pathofscabaras;

    public boolean elidiniswarden;
    public boolean elidiniswardenDead;
    public boolean rightHand;
    public boolean leftHand;
    /**
     * The door location of the current paths
     */
    private final ArrayList<Location> roomPaths = new ArrayList<Location>();
    /**
     * The names of the current rooms in path
     */
    private final ArrayList<String> roomNames = new ArrayList<String>();
    /**
     * Current monsters needed to kill
     */
    private int mobAmount;

    /**
     * Gets the start location for the path
     * @return path
     */
    public Location getStartLocation() {
        switch (path) {
            case 0:
                return Raid3Rooms.STARTING_ROOM_2.doorLocation;
        }
        return Raid3Rooms.STARTING_ROOM.doorLocation;
    }

    public Location getElidiniswardenWaitLocation() {
        switch (path) {
            case 0:
                return Raid3Rooms.LOOT_ROOM.doorLocation;
        }
        return Raid3Rooms.LOOT_ROOM_2.doorLocation;
    }


    /**
     * Handles raids3 rooms
     * @author Goon
     */
    public enum Raid3Rooms {
        STARTING_ROOM("start_room", 1, 0, new Location(3684, 5284)), PATH_OF_CRONDIS("pathofcrondis", 1, 0, new Location(3929, 5276)), BABA("baba", 1, 0, new Location(3805, 5406)), AKKHA("akkha", 1, 0, new Location(3677, 5404,1)), PATH_OF_APMEKEN("pathofapmeken", 1, 0, new Location(3804, 5276)), PATH_OF_SCABARAS("pathofscabaras", 1, 0, new Location(3311, 5374)),
        ZEBAK("zebak", 1, 0, new Location(3933, 5402)), KEPHRI("kephri", 1, 0, new Location(3546, 5404)), ELIDINIS_WARDEN_ROOM_WAIT("Elidiniswarden_wait", 1, 0, new Location(3232, 5721)), ELIDINIS_WARDEN_ROOM("elidiniswarden", 1, 0, new Location(3804, 5146,2)), TUMEKENS_WARDEN("tumekenswarden", 1, 0, new Location(3804, 5146,2)), LOOT_ROOM("loot", 1, 0, new Location(3680, 5169)),
        STARTING_ROOM_2("start_room2", 1, 1, new Location(3684, 5284)), ZEBAK_2("zebak", 1, 1, new Location(3933, 5402)), AKKHA_1("Akkha", 1, 1, new Location(3677, 5404,1)), PATH_OF_APMEKEN1("path of apmeken", 1, 1, new Location(3804, 5276)), PATH_OF_SCABARAS1("pathofscabaras", 1, 0, new Location(3311, 5374)),
        BABA_1("baba", 1, 1, new Location(3805, 5406)), KEPHRI_1("kephri", 1, 1, new Location(3546, 5404)), PATH_OF_CRONDIS1("pathofcrondis", 1, 1, new Location(3929, 5276)), ELIDINIS_WARDEN_ROOM_WAIT_2("elidiniswarden_wait", 1, 1, new Location(3804, 5146,2)), ELIDINIS_WARDEN_ROOM_2("elidiniswarden", 1, 1, new Location(3804, 5146,2)), TUMEKENS_WARDEN1("tumekenswarden", 1, 1, new Location(3804, 5146,2)),LOOT_ROOM_2("loot", 1, 1, new Location(3680, 5169));
        private final Location doorLocation;
        private final int path;
        private final int way;
        private final String roomName;

        Raid3Rooms(String name, int path1, int way1, Location door) {
            doorLocation = door;
            roomName = name;
            path = path1;
            way = way1;
        }

        public Location getDoor() {
            return doorLocation;
        }

        public int getPath() {
            return path;
        }

        public int getWay() {
            return way;
        }

        public String getRoomName() {
            return roomName;
        }
    }

    /**
     * Starts the raids3.
     */
    public void startRaids3(List<Player> players, boolean party) {
        //Initializes the raids3
        currentHeight = Raids3Constants.currentRaids3Height;
        Raids3Constants.currentRaids3Height += 0;
        path = 1;
        way = Misc.random(1);
        for (Raid3Rooms room : Raid3Rooms.values()) {
            if (room.getWay() == way) {
                roomNames.add(room.getRoomName());
                roomPaths.add(room.getDoor());
            }
        }
        for (Player lobbyPlayer : players) {
            if (!party) {
                //gets all players in lobby
                if (lobbyPlayer == null) continue;
                if (!lobbyPlayer.getPosition().inRaids3Lobby()) {
                    lobbyPlayer.sendMessage("You were not in the lobby you have been removed from the raids3 queue.");
                    continue;
                }
            }
            lobbyPlayer.getPA().closeAllWindows();
            raids3Players.put(lobbyPlayer.getLoginNameLower(), 0);
            activeRoom.put(lobbyPlayer.getLoginNameLower(), 0);
            lobbyPlayer.setRaids3Instance(this);
            //lobbyPlayer.setInstance(instance);
            lobbyPlayer.getPA().movePlayer(getStartLocation().getX(), getStartLocation().getY(), currentHeight);
            lobbyPlayer.sendMessage("@red@The raid has now started! Good Luck! type ::leaveraids3 to leave!");
            lobbyPlayer.sendMessage("[TEMP] @blu@If you get stuck in a wall, type ::stuckraids3 to be sent back to room 1!");
        }
        Raids3Constants.raids3Games.add(this);
    }

    public boolean hadPlayer(Player player) {
        long leftAt = playerLeftAt.getOrDefault(player.getLoginNameLower(), (long) -1);
        return leftAt > 0;
    }

    public boolean login(Player player) {
        long leftAt = playerLeftAt.getOrDefault(player.getLoginNameLower(), (long) -1);
        if (leftAt > 0) {
            playerLeftAt.remove(player.getLoginNameLower());
            if (System.currentTimeMillis() - leftAt <= 60000) {
                raids3Players.put(player.getLoginNameLower(), 0);
                player.  setRaids3Instance(this);
                player.sendMessage("@red@You rejoin the raids3!");
                lastActivity = -1;
                return true;
            }
        }
        return false;
    }

    public void logout(Player player) {
        player.setRaids3Instance(null);
        removePlayer(player);
        playerLeftAt.put(player.getLoginNameLower(), System.currentTimeMillis());
    }

    public void resetElidiniswardenRoom(Player player) {
        this.activeRoom.put(player.getLoginNameLower(), 9);
    }

    public void resetRoom(Player player) {
        this.activeRoom.put(player.getLoginNameLower(), 0);
    }

    /**
     * Kill all spawns for the raid leader if left
     */
    public void killAllSpawns() {
        NPCHandler.kill(currentHeight, currentHeight, 394, 3341, 7563, 7566, 7585, 7560, 7544, 7573, 7604, 7606, 7605, 7559, 7527, 7528, 7529, 7553, 7554, 7555);
    }

    /**
     * Leaves the raids3.
     * @param player
     */
    public void leaveGame(Player player) {
        if (System.currentTimeMillis() - player.infernoLeaveTimer < 15000) {
            player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
            return;
        }
        player.sendMessage("@red@You have left the Chambers of Xeric.");
        player.getPA().movePlayer(1247, 3559, 0);
        player.setRaids3Instance(null);
        //player.setInstance(null);
        removePlayer(player);
        player.specRestore = 120;
        player.specAmount = 10.0;
        player.setRunEnergy(10000, true);
        player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
        player.getPA().refreshSkill(Player.playerPrayer);
        player.getHealth().removeAllStatuses();
        player.getHealth().reset();
        player.getPA().refreshSkill(5);
    }

    public static List<Raids3Rank> buildRankList(List<Raids3Rank> ranks) {
        ranks.sort(Comparator.comparingInt(it -> it.damage));
        ranks = Lists.reverse(ranks);
        for (int index = 0; index < ranks.size(); index++) {
            ranks.get(index).rank = index + 1;
        }

        return ranks;
    }

    /**
     * Handles giving the raids3 reward
     */
    public void giveReward(Player player, Boolean kronosReward) {
        if (ranks == null) {
            ranks = buildRankList(getPlayers().stream().map(it -> new Raids3Rank(it, getDamage(it))).collect(Collectors.toList()));
            logger.debug("Ranks {}", ranks);
        }

        if (getDamage(player) < RAIDS3_DAMAGE_FOR_REWARD) {
            player.sendMessage("@red@You didn't do enough damage to earn a reward, you must do at least " + RAIDS3_DAMAGE_FOR_REWARD + " damage!");
            return;
        }

        int myRank;
        Optional<Raids3Rank> rank = ranks.stream().filter(it -> it.player.equals(player)).findFirst();
        if (rank.isEmpty()) {
            myRank = 100;
            logger.error("No rank for player {}", player);
        } else {
            myRank = rank.get().rank;
        }

        logger.debug("Rank for {} is {}", player.getDisplayName(), myRank);
        player.sendMessage("[@pur@RAIDS3@bla@] Your place was @pur@#" + myRank + " @bla@with @red@" + getDamage(player) + "@bla@ damage done.");
        if (myRank > 5) {
            myRank = myRank + 4;
        }
        if (myRank > 24) {
            myRank = 24;
        }

        int chance = Misc.random(1000);
        int rareChance = 975 + myRank;
        if (player.getItems().playerHasItem(21046)) {
            rareChance = 1975 + myRank;
            player.getItems().deleteItem(21046, 1);
            player.sendMessage("@red@You sacrifice your @cya@tablet @red@for an increased drop rate.");
            player.getEventCalendar().progress(EventChallenge.USE_X_CHEST_RATE_INCREASE_TABLETS, 1);
        }
        if (chance >= 0 && chance < rareChance) {
            player.getItems().addItemUnderAnyCircumstance(COMMON_KEY, 1);
            player.getEventCalendar().progress(EventChallenge.COMPLETE_X_RAIDS3);
             LeaderboardUtils.addCount(LeaderboardType.COX, player, 1);
            Achievements.increase(player, AchievementType.COX, 1);
            player.sendMessage("@red@You have just received a @bla@Common Key.");
        } else if (chance >= rareChance) {
            player.getItems().addItemUnderAnyCircumstance(RARE_KEY, 1);
            player.getEventCalendar().progress(EventChallenge.COMPLETE_X_RAIDS3);
            LeaderboardUtils.addCount(LeaderboardType.COX, player, 1);
            Achievements.increase(player, AchievementType.COX, 1);
            player.sendMessage("@red@You have just received a @pur@Rare Key.");
            PlayerHandler.executeGlobalMessage("@bla@[@blu@RAIDS@bla@] " + player.getDisplayName() + "@pur@ has just received a @bla@Rare Raids Key!");
        }
        if (!kronosReward) {
            if (player.raids3Count == 25) {
                player.getItems().addItemUnderAnyCircumstance(22388, 1);
                PlayerHandler.executeGlobalMessage("@blu@[@pur@" + player.getDisplayName() + "@blu@] has completed 25 Raids3 and obtained the Xeric\'s Guard Cape!");
            }
            if (player.raids3Count == 50) {
                player.getItems().addItemUnderAnyCircumstance(22390, 1);
                PlayerHandler.executeGlobalMessage("@blu@[@pur@" + player.getDisplayName() + "@blu@] has completed 50 Raids3 and obtained the Xeric\'s Warrior Cape!");
            }
            if (player.raids3Count == 100) {
                player.getItems().addItemUnderAnyCircumstance(22392, 1);
                PlayerHandler.executeGlobalMessage("@blu@[@pur@" + player.getDisplayName() + "@blu@] has completed 100 Raids3 and obtained the Xeric\'s Sentinel Cape!");
            }
            if (player.raids3Count == 250) {
                player.getItems().addItemUnderAnyCircumstance(22394, 1);
                PlayerHandler.executeGlobalMessage("@blu@[@pur@" + player.getDisplayName() + "@blu@] has completed 250 Raids3 and obtained the Xeric\'s General Cape!");
            }
            if (player.raids3Count == 500) {
                player.getItems().addItemUnderAnyCircumstance(22396, 1);
                PlayerHandler.executeGlobalMessage("@blu@[@pur@" + player.getDisplayName() + "@blu@] has completed 500 Raids3 and obtained the Xeric\'s Champions Cape!");
            }
        }
    }

    final int Elidiniswarden = 7554;
    final int OLM_RIGHT_HAND = 7553;
    final int OLM_LEFT_HAND = 7555;

    public void handleMobDeath(Player killer, int npcType) {
        mobAmount -= 1;
        switch (npcType) {
            case Elidiniswarden:
                //*
                // * Crystal & Olm removal after olm's death
                // *
                elidiniswardenDead = true;
//			Server.getGlobalObjects().add(new GlobalObject(-1, 3233, 5751, currentHeight, 3, 10).setInstance(instance));
                Server.getGlobalObjects().remove(30018, 3220, 5743, 0);
                Server.getGlobalObjects().remove(30018, 3220, 5743, currentHeight);
                Server.getGlobalObjects().remove(new GlobalObject(30018, 3232, 5749, currentHeight, 3, 10));
                getPlayers().stream().forEach(player -> {
                    player.getPA().sendPlayerObjectAnimation(3220, 5738, 7348, 10, 3);
                    player.sendMessage("@red@Congratulations you have defeated The Great Olm and completed the raids3!");
                    player.sendMessage("@red@Please go up the stairs beyond the Crystals to get your reward " );
                });
                return;

            case OLM_RIGHT_HAND:
                rightHand = true;
                if(leftHand == true) {
                    getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable."));
                    Server.getGlobalObjects().add(new GlobalObject(29888, 3220, 5733, currentHeight, 3, 10));
                }else {
                    getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!"));
                }
                //Server.getGlobalObjects().remove(new GlobalObject(29887, 3220, 5733, currentHeight, 3, 10).setInstance(instance));

                //Server.getGlobalObjects().add(new GlobalObject(29888, 3220, 5733, currentHeight, 3, 10).setInstance(instance));
                getPlayers().stream()
                        .forEach(otherPlr -> {
                            otherPlr.getPA().sendPlayerObjectAnimation(3220, 5733, 7352, 10, 3);
                            if(leftHand) {
                                otherPlr.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable.");
                            } else {
                                otherPlr.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!");
                            }
                        });

                return;
            case OLM_LEFT_HAND:
                leftHand = true;
                Server.getGlobalObjects().remove(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
                Server.getGlobalObjects().remove(30018, 3220, 5743, currentHeight);
                Server.getGlobalObjects().add(new GlobalObject(29885, 3220, 5743, currentHeight, 3, 10));
                getPlayers().stream()
                        .forEach(otherPlr -> {
                            otherPlr.getPA().sendPlayerObjectAnimation(3220, 5743, 7360, 10, 3);
                            if(rightHand) {
                                otherPlr.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable.");
                            } else {
                                otherPlr.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!");
                            }

                        });
                if(rightHand == true) {
                    Server.getGlobalObjects().remove(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
                    Server.getGlobalObjects().remove(new GlobalObject(30018, 3232, 5749, currentHeight, 3, 10));
                    Server.getGlobalObjects().add(new GlobalObject(29885, 3220, 5743, currentHeight, 3, 10));
                    getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated both of The Great Olm's hands he is now vulnerable."));
                }else {
                    getPlayers().stream().forEach(player ->	player.sendMessage("@red@ You have defeated one of The Great Olm's hands destroy the other one quickly!"));
                }
                return;
        }
        if(killer != null) {
            int randomPoints = Misc.random(500);
            int newPoints = addPoints(killer, randomPoints);

            killer.sendMessage("@red@You receive "+ randomPoints +" points from killing this monster.");
            killer.sendMessage("@red@You now have "+ newPoints +" points.");
        }
        if(mobAmount <= 0) {
            getPlayers().stream().forEach(player ->	player.sendMessage("@red@The room has been cleared and you are free to pass."));
            roomSpawned = false;
        }else {
            getPlayers().stream().forEach(player ->	player.sendMessage("@red@There are "+ mobAmount+" enemies remaining."));
        }
    }

    public void spawnRaids3Npc(int npcType, int x, int y, int z, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer) {
        int[] stats = getScaledStats(HP, attack, defence, getPlayers().size());
        attack = stats[0];
        defence = stats[1];
        HP = stats[2];
        NPC npc = NPCSpawning.spawn(npcType, x, y, z, WalkingType, maxHit, attackPlayer,
                NpcStats.builder().setAttackLevel(attack).setHitpoints(HP).setDefenceLevel(defence).createNpcStats());
        npc.setRaids3Instance(this);
        npc.getBehaviour().setRespawn(false);
        //System.out.println(modifier + " | "  + lowModifier);
    }

    public static int[] getScaledStats(int HP, int attack, int defence, int groupsize) {
        int modifier = 0;
        int attackScale =1;
        int defenceScale =1;
        int baseMod = 100;
        int baseLowMod = 35;
        if (groupsize > 1) {
            if (HP < 200) {
                baseMod = 10;
            }
            modifier = (baseMod + (groupsize * (int) (HP * 0.15))); // groupsize:modifier | 1:1 | 2:1.8 | 3:2.2
            attackScale = (baseLowMod + (groupsize * 10)); // groupsize:modifier | 1:1 | 2:1.2 | 3:1.3
            defenceScale = (baseLowMod + (groupsize * 10)); // groupsize:modifier | 1:1 | 2:1.2 | 3:1.3
        }
        defence = (defence + defenceScale);
        HP = (HP + modifier);
        attack = (attack + attackScale);

        return new int[] { attack, defence, HP };
    }

    public static final int PATH_OF_CRONDIS_HP = 150;
    public static final int PATH_OF_CRONDIS_ATTACK = 200;
    public static final int PATH_OF_CRONDIS_DEFENCE = 100;

    public static final int TUMEKENS_HP = 400;
    public static final int TUMEKENS_ATTACK = 250;
    public static final int TUMEKENS_DEFENCE = 130;

    public static final int Elidiniswarden_HP = 500;
    public static final int Elidiniswarden_ATTACK = 272;
    public static final int Elidiniswarden_DEFENCE = 350;

    /**
     * Spawns npc for the current room
     * @param currentRoom The room
     */
    public void spawnNpcs(int currentRoom) {

        int height = currentHeight;
        switch(roomNames.get(currentRoom)) {
            case "startroom":
                if(startroom) {
                    return;
                }
                if(path == 0) {

                    spawnRaids3Npc(11707, 3274, 5262, height, -1, 500, 45, 350, 160,true);
                }else {
                    spawnRaids3Npc(11707, 3307,5265, height,  -1, 500, 45, 350, 160,true);
                }
                startroom = true;
                mobAmount+=1;
                break;
            case "startroom2":
                if(startroom2) {
                    return;
                }
                if(path == 0) {

                    spawnRaids3Npc(11707, 3274, 5262, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                }else {
                    spawnRaids3Npc(11707, 3307,5265, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                }
                startroom2 = true;
                mobAmount+=1;
                break;
            case "pathofcrondis":
                if(pathofcrondis) {
                    return;
                }
                if(path == 0) {

                    spawnRaids3Npc(7573, 3274, 5262, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                    spawnRaids3Npc(7573, 3282, 5266, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                    spawnRaids3Npc(7573, 3275, 5269, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                }else {
                    spawnRaids3Npc(7573, 3307,5265, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                    spawnRaids3Npc(7573, 3314,5265, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                    spawnRaids3Npc(7573, 3314,5261, height, 1, PATH_OF_CRONDIS_HP, 25, PATH_OF_CRONDIS_ATTACK, PATH_OF_CRONDIS_DEFENCE,true);
                }
                pathofcrondis = true;
                mobAmount+=3;
                break;
            case "tumekenswarden":
                if(tumekenswarden) {
                    return;
                }

                if(path == 0) {
                    spawnRaids3Npc(7566, 3280,5295, height, -1, TUMEKENS_HP, 25, TUMEKENS_ATTACK, TUMEKENS_DEFENCE,true);
                }else {
                    spawnRaids3Npc(7566, 3311,5295, height, -1, TUMEKENS_HP, 25, TUMEKENS_ATTACK, TUMEKENS_DEFENCE,true);
                }
                tumekenswarden = true;
                mobAmount+=1;
                break;
//            case "pathofhet":
//                if(pathofhet) {
//                    return;
//                }
//                if(path == 0) {
//                    spawnRaids3Npc(7527, 3277,5326, height, -1, 170, 25, 140, 120,true);// melee vanguard
//                    spawnRaids3Npc(7528, 3277,5332, height, -1, 170, 25, 140, 120,true); // range vanguard
//                    spawnRaids3Npc(7529, 3285,5329, height, -1, 170, 25, 140, 120,true); // magic vanguard
//                }else {
//                    spawnRaids3Npc(7527, 3310,5324, height, -1, 170, 25, 140, 120,true); // melee vanguard
//                    spawnRaids3Npc(7528, 3310,5331, height, -1, 170, 25, 140, 120,true); // range vanguard
//                    spawnRaids3Npc(7529, 3316,5331, height, -1, 170, 25, 140, 120,true);// magic vanguard
//                }
//                pathofhet = true;
//                mobAmount+=3;
//                break;
            case "akkha":
                if(akkha) {
                    return;
                }
                if(path == 0) {
                    spawnRaids3Npc(11789, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11791, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11792, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11793, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                }else {
                    spawnRaids3Npc(11790, 3681,5408, currentHeight, -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11794, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11795, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                    spawnRaids3Npc(11796, 3681,5408,currentHeight , -1, 500, 45, 350, 160,true);
                }
                akkha = true;
                mobAmount+=1;
//                akkhaDead = true;
//                spawnRaids3Npc(11693, 3681,5408, 1, -1, 500, 0, 0, 0,false);
                break;
            case "baba":
                if(baba) {
                    return;
                }
                if(path == 0) {
                    spawnRaids3Npc(7604, 3279,5271, height, -1, 150, 25, 400, 150,true);
                    spawnRaids3Npc(7605, 3290,5268, height, -1, 150, 25, 500, 150,true);
                    spawnRaids3Npc(7606, 3279,5264, height, -1, 150, 25, 400, 150,true);
                }else {
                    spawnRaids3Npc(7604, 3318,5262,height, -1, 180, 25, 400, 150,true);
                    spawnRaids3Npc(7605, 3307,5258, height, -1, 180, 25, 500, 150,true);
                    spawnRaids3Npc(7606, 3301,5262, height, -1, 180, 25, 400, 150,true);
                }
                mobAmount+=3;
                baba = true;
                break;
            case "kephri":
                if(kephri) {
                    return;
                }
                if(path == 0) {
                    spawnRaids3Npc(7544, 3280,5295, height, -1, 550, 45, 450, 230,true);
                }else {
                    spawnRaids3Npc(7544, 3310, 5293, height, -1, 550, 45, 450, 230,true);
                }
                mobAmount+=1;
                kephri = true;
                break;
            case "zebak":
                if(zebak) {
                    return;
                }
                if(path == 0) {
                    spawnRaids3Npc(11730, 3917,5405,height, -1, 300, 25, 400, 220,true);
                }else {
                    spawnRaids3Npc(11730, 3917,5405, height, -1, 300, 25, 400, 220,true);
                }
                mobAmount+=1;
                zebak = true;
                break;
            case "pathofscabaras":
                if(pathofscabaras) {
                    return;
                }
                if(path == 0) {
                    spawnRaids3Npc(7559, 3287,5364, height, -1, 150, 25, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3287,5363, height, -1, 150, 25, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3285,5363, height, -1, 150, 30, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3285,5364, height, -1, 150, 30, 100, 100,true); // deathly ranger

                    spawnRaids3Npc(7560, 3286,5369, height, -1, 150, 25, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3284,5369, height, -1, 150, 25, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3286,5370, height, -1, 150, 30, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3284,5370, height, -1, 150, 30, 100, 100,true); // deathly mager
                }else {
                    spawnRaids3Npc(7559, 3319,5363, height, -1, 150, 25, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3317,5363, height, -1, 150, 25, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3317,5364, height, -1, 150, 30, 100, 100,true); // deathly ranger
                    spawnRaids3Npc(7559, 3319,5364, height, -1, 150, 30, 100, 100,true); // deathly ranger

                    spawnRaids3Npc(7560, 3318,5370, height, -1, 150, 25, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3318,5369, height, -1, 150, 25, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3316,5369, height, -1, 150, 30, 100, 100,true); // deathly mager
                    spawnRaids3Npc(7560, 3316,5370, height, -1, 150, 30, 100, 100,true); // deathly mager
                }
                pathofscabaras = true;
                mobAmount+=8;
                break;
            case "Elidiniswarden":
                if(elidiniswarden) {
                    return;
                }

                // TODO custom region object clipping for instances like this
                Server.getGlobalObjects().add(new GlobalObject(29884, 3220, 5743, currentHeight, 3, 10));
                Server.getGlobalObjects().add(new GlobalObject(29887, 3220, 5733, currentHeight, 3, 10));
                Server.getGlobalObjects().add(new GlobalObject(29881, 3220, 5738, currentHeight, 3, 10));
                getPlayers().stream()
                        .forEach(otherPlr -> {
                            otherPlr.getPA().sendPlayerObjectAnimation(3220, 5733, 7350, 10, 3);
                            otherPlr.getPA().sendPlayerObjectAnimation(3220, 5743, 7354, 10, 3);
                            otherPlr.getPA().sendPlayerObjectAnimation(3220, 5738, 7335, 10, 3);
                        });
                spawnRaids3Npc(7553, 3223, 5733, height, -1, 200, 33, 272, 200,false); // left claw
                spawnRaids3Npc(7554, 3223, 5738, height, -1, Elidiniswarden_HP, 33, Elidiniswarden_ATTACK, Elidiniswarden_DEFENCE,true); // olm head
                spawnRaids3Npc(7555, 3223, 5742, height, -1, 200, 33, 272, 200 ,false); // right claw

                elidiniswarden = true;
                mobAmount+=3;
                break;
            default:
                roomSpawned = false;

                break;
        }

    }
    /**
     * Handles object clicking for raid objects
     * @param player The player
     * @param objectId The object id
     * @param x
     * @param y
     * @return
     */
    public boolean handleObjectClick(Player player, int objectId, int x, int y) {
        player.objectDistance = 3;
        switch(objectId) {
            // Searching chest to
            case 29742: //// chest
                if (chestRoomDoorOpen) {
                    player.sendMessage("The room is already opened, no need for more searching.");
                } else {
                    if (chestRoomChestsSearched.contains(Objects.hash(x, y))) {
                        player.sendMessage("This chest has already been searched.");
                    } else {
                        player.startAnimation(6387);
                        chestRoomChestsSearched.add(Objects.hash(x, y));
                        player.sendMessage("You search the chest..");

                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                if (chestRoomChestsSearched.size() == chestToOpenTheDoor) {
                                    player.sendMessage("You find a lever to open the door..");
                                    player.forcedChat("I found the lever!");
                                    getPlayers().forEach(plr -> plr.sendMessage("@red@The door has been opened."));
                                    chestRoomDoorOpen = true;
                                } else {
                                    player.sendMessage("You find nothing.");
                                    player.startAnimation(65535);
                                }

                                container.stop();
                            }
                        }, 2);
                    }
                }
                return true;
            case 45131://First entrance
                player.objectDistance = 3;
            case 45509:
                player.objectDistance = 3;
            case 45397: //exit crondis
                player.objectDistance = 3;
            case 29734: // hole to go to olms
                player.objectDistance = 3;
            case 29879: // olm gate
                if (roomNames.get(getRoomForPlayer(player)).equalsIgnoreCase("chest") && !chestRoomDoorOpen) {
                    player.sendMessage("This passage way is blocked, you must search the boxes to find the lever to open it.");
                } else {
                    player.objectDistance = 3;
                    nextRoom(player);
                }
                return true;
            case 30018:
                player.objectDistance = 3;
                return true;
            case 44596: /// cox entrance
                player.objectDistance = 3;
            case 29778: /// stairs
                player.objectDistance = 3;
                if(!elidiniswardenDead) {
                    if(player.objectX == 3298 && player.objectY == 5185) {
                        player.getDH().sendDialogues(10000, -1);
                        return true;
                    }
                    player.sendMessage("You need to complete the raids3!");
                    return true;
                }
                if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
                    return true;
                }
                player.objectDistance = 3;
                player.lastMysteryBox = System.currentTimeMillis();
                player.raids3Count+=1;
                if (Boundary.isIn(player, Boundary.FULL_RAIDS3)) {
                    giveReward(player, false);
                    if (AvatarOfCreation.activeKronosSeed == true) {
                        giveReward(player, true);
                        player.sendMessage("@red@The @gre@Kronos seed@red@ doubles your chances!" );
                    }
                }
                resetDamage(player);
                player.healEverything();
                player.sendMessage("@red@You receive your reward." );
                player.sendMessage("@red@You have completed "+player.raids3Count+" raids3." );
                leaveGame(player);
                break;

            case 30027:
                player.objectDistance = 3;
                player.getPA().showInterface(57000);

                return true;
        }
        return false;
    }

    private boolean roomSpawned;

    private int getRoomForPlayer(Player player) {
        return activeRoom.getOrDefault(player.getLoginNameLower(), 0);
    }

    /**
     * Goes to the next room, Handles spawning etc.
     */
    public void nextRoom(Player player) {
        player.objectDistance = 3;
        if(activeRoom.getOrDefault(player.getLoginNameLower(), 0) == currentRoom && mobAmount > 0) {
            player.objectDistance = 3;
            player.sendMessage("You need to defeat the current room before moving on!");
            return;
        }
        if(!roomSpawned) {
            player.objectDistance = 3;
            currentRoom+=1;
            roomSpawned = true;
            spawnNpcs(currentRoom);
        }

        int playerRoom = activeRoom.getOrDefault(player.getLoginNameLower(), 0) + 1;
        if (playerRoom >= roomPaths.size()) {
            player.sendMessage("You can't go this way.");
            return;
        }
        player.getPA().movePlayer(roomPaths.get(playerRoom).getX(),
                roomPaths.get(playerRoom).getY(),
                roomPaths.get(playerRoom).getZ());
        activeRoom.put(player.getLoginNameLower(), playerRoom);

    }

    public static void damage(Player player, int damage) {
        int current = getDamage(player);
        player.getAttributes().setInt(RAIDS3_DAMAGE_ATTRIBUTE_KEY, current + damage);
    }

    public static int getDamage(Player player) {
        return player.getAttributes().getInt(RAIDS3_DAMAGE_ATTRIBUTE_KEY, 0);
    }

    private static void resetDamage(Player player) {
        player.getAttributes().removeInt(RAIDS3_DAMAGE_ATTRIBUTE_KEY);
    }

    public static class Raids3Rank {
        private final Player player;
        private int rank;
        private final int damage;

        Raids3Rank(Player player, int damage) {
            this.player = player;
            this.damage = damage;
        }

        @Override
        public String toString() {
            return "Raids3Rank{" +
                    "player=" + (player == null ? null : player.getDisplayName()) +
                    ", rank=" + rank +
                    ", damage=" + damage +
                    '}';
        }

        public Player getPlayer() {
            return player;
        }

        public int getRank() {
            return rank;
        }

        public int getDamage() {
            return damage;
        }
    }

}
