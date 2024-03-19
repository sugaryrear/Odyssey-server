package io.Odyssey.content.combat.death;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.everythingrs.marketplace.Item;
import com.google.common.collect.Lists;
import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.melee.MeleeData;
import io.Odyssey.content.combat.pvp.PkpRewards;
import io.Odyssey.content.itemskeptondeath.ItemsLostOnDeath;
import io.Odyssey.content.itemskeptondeath.ItemsLostOnDeathList;
import io.Odyssey.content.minigames.Raids3.Raids3;
import io.Odyssey.content.minigames.bounty_hunter.BountyHunter;
import io.Odyssey.content.minigames.pest_control.PestControl;
import io.Odyssey.content.minigames.pk_arena.Highpkarena;
import io.Odyssey.content.minigames.pk_arena.Lowpkarena;
import io.Odyssey.content.minigames.raids.Raids;

import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.model.Items;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.collisionmap.doors.Location;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.entity.player.mode.Mode;
import io.Odyssey.model.entity.player.mode.ModeType;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.multiplayersession.MultiplayerSession;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.player.DeathItemsHeld;
import io.Odyssey.util.logging.player.DeathItemsKept;
import io.Odyssey.util.logging.player.DeathItemsLost;
import io.Odyssey.util.logging.player.DeathLog;

public class PlayerDeath {

    private static void beforeDeath(Player c) {
        TourneyManager.setFog(c, false, 0);
        c.getPA().sendFrame126(":quicks:off", -1);
        c.getItems().setEquipmentUpdateTypes();
        c.getPA().requestUpdates();
        c.respawnTimer = 15;
        c.isDead = false;
        c.graceSum = 0;
        c.freezeTimer = 1;
        c.recoilHits = 0;
        c.totalHunllefDamage = 0;
        c.tradeResetNeeded = true;
        c.setSpellId(-1);
        c.attacking.reset();
        c.getPA().resetAutocast();
        c.usingMagic = false;
    }

    private static void afterDeath(Player c) {
        c.playerStandIndex = 808;
        c.playerWalkIndex = 819;
        c.playerRunIndex = 824;
        PlayerSave.saveGame(c);
        c.getPA().requestUpdates();
        c.getPA().removeAllWindows();
        c.getPA().closeAllWindows();
        c.getPA().resetFollowers();
        c.getItems().addSpecialBar(c.playerEquipment[Player.playerWeapon]);
        c.specAmount = 10;
        c.attackTimer = 10;
        c.respawnTimer = 15;
        c.lastVeng = 0;
        c.recoilHits = 0;
        c.graceSum = 0;
        c.freezeTimer = 1;
        c.vengOn = false;
        c.isDead = false;
        c.tradeResetNeeded = true;
    }

    public static void applyDead(Player c) {
        beforeDeath(c);

        MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE);
        if (session != null && Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
            c.sendMessage("You have declined the trade.");
            session.getOther(c).sendMessage(c.getDisplayName() + " has declined the trade.");
            session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            return;
        }

        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
            duelSession = null;
        }

        if (c.getSlayer().superiorSpawned) {
            c.getSlayer().superiorSpawned = false;
        }
//        if (c.getRights().isOrInherits(Right.EVENT_MAN)) {
//            if (Boundary.isIn(c, Boundary.DUEL_ARENA) || Boundary.isIn(c, Boundary.FIGHT_CAVE)
//                    || c.getPosition().inClanWarsSafe() || Boundary.isIn(c, Boundary.INFERNO)
//                    || Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)
//                    || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_AREA)
//                    || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_LOBBY)
//                    || Boundary.isIn(c, Boundary.TOKKUL_PIT1)
//                    || Boundary.isIn(c, Boundary.PEST_CONTROL_AREA)
//                    || Boundary.isIn(c, Boundary.RAIDS)
//                    || Boundary.isIn(c, Boundary.OLM)
//                    || Boundary.isIn(c, Boundary.RAID_MAIN)
//                    || Boundary.isIn(c, Boundary.RAIDS3_MAIN)
//                    || Boundary.isIn(c, Boundary.XERIC)) { // TODO: Other areas.
//                return;
//            }
//            PlayerHandler.executeGlobalMessage("@red@News: @blu@" + c.getDisplayNameFormatted()
//                    + " @pur@has just died, with a skill total of " + c.totalLevel
//                    + "!");
//        }
        if (c.getMode().isHardcoreIronman()) {
            if (Boundary.isIn(c, Boundary.DUEL_ARENA) || Boundary.isIn(c, Boundary.FIGHT_CAVE)
                    || c.getPosition().inClanWarsSafe() || Boundary.isIn(c, Boundary.INFERNO)
                    || Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)
                    || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_AREA)
                    || Boundary.isIn(c, Boundary.LUMBRIDGE_OUTLAST_LOBBY)
                    || Boundary.isIn(c, Boundary.PEST_CONTROL_AREA)
                    || Boundary.isIn(c, Boundary.RAIDS)
                    || Boundary.isIn(c, Boundary.TOKKUL_PIT1)
                    || Boundary.isIn(c, Boundary.OLM)
                    || Boundary.isIn(c, Boundary.RAID_MAIN)
                    || Boundary.isIn(c, Boundary.RAIDS3_MAIN)
                    || Boundary.isIn(c, Boundary.XERIC)) { // TODO: Other areas.
                return;
            }

            if (!Configuration.DISABLE_HC_LOSS_ON_DEATH) {

                if (c.totalLevel > 500) {
                    PlayerHandler.executeGlobalMessage("@red@News: @blu@" + c.getDisplayNameFormatted()
                            + " @pur@has just died in hardcore ironman mode, with a skill total of " + c.totalLevel
                            + "!");
                }

                if (c.getMode().getType() == ModeType.HC_IRON_MAN) {
                    c.getRights().remove(Right.HC_IRONMAN);
                    c.setMode(Mode.forType(ModeType.IRON_MAN));
                    c.getRights().setPrimary(Right.IRONMAN);
                    c.sendMessage("You are now a normal Ironman.");
                }

                else {
                    throw new IllegalStateException("Not a hardcore: " + c.getMode());
                }

                PlayerSave.saveGame(c);
            }
        }

        // PvP Death
        if (Objects.isNull(duelSession)) {
            Entity killer = c.calculateKiller();
            if (killer != null) {
                c.setKiller(killer);
                if (killer instanceof Player) {
                    Player playerKiller = (Player) killer;
                    c.killerId = killer.getIndex();
                    //well here add messages to the KILLER lol
                }
                c.sendMessage("Oh dear, you are dead!");
            }
        }

        /*
         * Reset bounty hunter statistics
         */
        if (Configuration.BOUNTY_HUNTER_ACTIVE) {
            c.getBH().setCurrentHunterKills(0);
            c.getBH().setCurrentRogueKills(0);
            c.getBH().updateStatisticsUI();
            c.getBH().updateTargetUI();
        }

        c.startAnimation(2304);
        c.faceUpdate(0);
        c.stopMovement();

        /*
         * Death within the duel arena
         */
        if (duelSession != null && duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
            if (duelSession.getWinner().isEmpty()) {
                Player opponent = duelSession.getOther(c);
                if (opponent.getHealth().getCurrentHealth() != 0) {
                    c.sendMessage("You have lost the duel!");
                    c.setDuelLossCounter(c.getDuelLossCounter() + 1);
                    c.sendMessage("You have now lost a total of @blu@" + c.getDuelLossCounter() + " @bla@ duels.");

                    opponent.logoutDelay = System.currentTimeMillis();
                    if (!duelSession.getWinner().isPresent()) {
                        duelSession.setWinner(opponent);
                    }
                    PlayerSave.saveGame(opponent);
                }
            } else {
                c.sendMessage("Congratulations, you have won the duel.");
            }
            c.logoutDelay = System.currentTimeMillis();
        }
        if (c.prayerActive[CombatPrayer.WRATH] && !Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)) {
            c.gfx0(437);

            List<Entity> possibleTargets = Lists.newArrayList();

            if (c.getPosition().inMulti()) {
                Arrays.stream(PlayerHandler.players).filter(Objects::nonNull).forEach(p -> {
                    if (p != c && p.getPosition().withinDistance(c.getPosition(), 1))
                        possibleTargets.add(p);
                });
                Arrays.stream(NPCHandler.npcs).filter(Objects::nonNull).forEach(n -> {
                    //Size check for npcs like corp ect
                    if (n.getPosition().withinDistance(c.getPosition(), n.getSize()))
                        possibleTargets.add(n);
                });
            }

            Entity killer = c.getKiller();

            if (killer != null) {
                if (!possibleTargets.contains(killer))
                    possibleTargets.add(killer);
            }

            possibleTargets.forEach(e -> {
                if (possibleTargets.isEmpty())
                    return;
                double howmuchdamage = (c.getPA().getLevelForXP(c.playerXP[5]) * 2.5);
                e.appendDamage(Misc.random(1,  (int)howmuchdamage), Hitmark.HIT);
            });
        }
        if (c.prayerActive[CombatPrayer.RETRIBUTION] && !Boundary.isIn(c, Boundary.LONE_SURVIVER_AREA)) {
            c.gfx0(437);

            List<Entity> possibleTargets = Lists.newArrayList();

            if (c.getPosition().inMulti()) {
                Arrays.stream(PlayerHandler.players).filter(Objects::nonNull).forEach(p -> {
                    if (p != c && p.getPosition().withinDistance(c.getPosition(), 1))
                        possibleTargets.add(p);
                });
                Arrays.stream(NPCHandler.npcs).filter(Objects::nonNull).forEach(n -> {
                    //Size check for npcs like corp ect
                    if (n.getPosition().withinDistance(c.getPosition(), n.getSize()))
                        possibleTargets.add(n);
                });
            }

            Entity killer = c.getKiller();

            if (killer != null) {
                if (!possibleTargets.contains(killer))
                    possibleTargets.add(killer);
            }

            possibleTargets.forEach(e -> {
                if (possibleTargets.isEmpty())
                    return;
                e.appendDamage(Misc.random(1, Misc.random(1, (c.playerLevel[5] / 4))), Hitmark.HIT);
            });
        }

        afterDeath(c);
    }

    /**
     * Handles what happens after a player death
     */
    public static void giveLife(Player c) {


        c.isDead = false;
        c.totalHunllefDamage = 0;
        c.faceUpdate(-1);
        c.freezeTimer = 1;
        c.isAnimatedArmourSpawned = false;
//        c.setTektonDamageCounter(0);
//        if (c.getGlodDamageCounter() >= 80 || c.getIceQueenDamageCounter() >= 80) {
//            c.setGlodDamageCounter(79);
//            c.setIceQueenDamageCounter(79);
//        }
        if (c.petSummonId > 0) {
            PetHandler.Pets pet = PetHandler.forItem(c.petSummonId);
            if (pet != null) {
            //    boolean hasstoragepet = PetHandler.hasstoragepetout(c);
//                if(hasstoragepet) {
//                    c.sendMessage("Your pack yak scurries off...");
//                } else {
//                    PetHandler.spawn(c, pet, true, false);
//                }
                PetHandler.spawn(c, pet, true, false);
            }
        }
        c.getQuestTab().updateInformationTab();
        c.getPA().stopSkilling();
        Arrays.fill(c.activeMageArena2BossId, 0);

        handleAreaBasedDeath(c);

        MeleeData.setWeaponAnimations(c);
        c.getItems().setEquipmentUpdateTypes();
        CombatPrayer.resetPrayers(c);
        for (int i = 0; i < 20; i++) {
            c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
            c.getPA().refreshSkill(i);
        }
        c.startAnimation(65535);
        PlayerSave.saveGame(c);
        c.resetOnDeath();

    }

    private static void handleAreaBasedDeath(Player c) {
        if (c.getInstance() != null && c.getInstance().handleDeath(c)) {//ok so that instance DOES NOT HANDLE DEATH. so this never happens lol.
            System.out.println("here?");//no its null.
            return;
        }
        c.removeFromInstance();

     //   Server.getLogging().write(new DeathLog(c));


        if (TourneyManager.getSingleton().isInArena(c)) {
            Entity tourneyKiller = c.calculateTourneyKiller();
            c.outlastDeaths++;
            TourneyManager.getSingleton().handleDeath(c.getLoginName(), false);
            TourneyManager.getSingleton().handleKill(tourneyKiller);
        } else if (TourneyManager.getSingleton().isInLobbyBounds(c)) {
            TourneyManager.getSingleton().leaveLobby(c, false);
        }

        if (Boundary.isIn(c, Boundary.PEST_CONTROL_AREA)) {
            c.getPA().movePlayer(2657, 2639, 0);
        } else     if (Boundary.isIn(c, Boundary.NEX)) {
                c.getPA().movePlayer(2904, 5203, 0);
                return;
        } else     if (Boundary.isIn(c, Boundary.HUNLLEF_BOSS_ROOM)) {
            c.getPA().movePlayer(3031,6124,1);
            return;
        } else     if (Boundary.isIn(c, Boundary.VORKATH)) {
            c.getPA().movePlayer(2273,4039,0);
            return;
        } else     if (Boundary.isIn(c, Boundary.GROTESQUE_LAIR)) {
            c.getPA().movePlayer(3426,3540,2);
            return;
        } else if (Boundary.isIn(c, PestControl.GAME_BOUNDARY)) {
            c.getPA().movePlayer(2656 + Misc.random(2), 2614 - Misc.random(3), 0);
        } else if (Boundary.isIn_dontcareheight(c, Boundary.ZULRAH)) {

            c.getZulrahLostItems().store();
            return;
        } else if (Boundary.isIn(c, Boundary.KRAKEN_CAVE)) {
            c.getPA().movePlayer(2280, 10016, 0);
        }
        if (Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)) {
            c.getPA().movePlayer(1309, 1250, 0);
        } else if (Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)) {
            c.getPA().movePlayer(1665, 10045, 0);
        } else if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
            DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
            if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION) {
                duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
            }
        } else if (Boundary.isIn(c, Boundary.HYDRA_BOSS_ROOM)) {
            c.getPA().movePlayer(Configuration.RESPAWN_X, Configuration.RESPAWN_Y, 0);
        } else if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
            c.getFightCave().handleDeath();
        } else if (Boundary.isIn(c, Boundary.TOKKUL_PIT1)) {
            c.getTokkulPit1().handleDeath();
        } else if (Boundary.isIn(c, Boundary.INFERNO)) {
          //  c.getInferno().handleDeath();
        } else if (Boundary.isIn(c, Boundary.XERIC)) {
            c.getXeric().leaveGame(c, true);
        } else if (Boundary.isIn(c, Boundary.OLM)) {
            Raids raidInstance = c.getRaidsInstance();
            if (raidInstance != null) {
                Location olmWait = raidInstance.getOlmWaitLocation();
                c.getPA().movePlayer(olmWait.getX(), olmWait.getY(), raidInstance.currentHeight);
                raidInstance.resetOlmRoom(c);
            }
        } else if (Boundary.isIn(c, Boundary.ELIDINIS_WARDEN)) {
            Raids3 raids3Instance = c.getRaids3Instance();
            if (raids3Instance != null) {
                Location elidiniswardenWait = raids3Instance.getElidiniswardenWaitLocation();
                c.getPA().movePlayer(elidiniswardenWait.getX(), elidiniswardenWait.getY(), raids3Instance.currentHeight);
                raids3Instance.resetElidiniswardenRoom(c);
            }
        } else if (Boundary.isIn(c, Boundary.RAIDS)) {
            Raids raidInstance = c.getRaidsInstance();
            if (raidInstance != null) {
                Location startRoom = raidInstance.getStartLocation();
                c.getPA().movePlayer(startRoom.getX(), startRoom.getY(), raidInstance.currentHeight);
                raidInstance.resetRoom(c);
            }
        } else if (Highpkarena.getState(c) != null) {
            Highpkarena.handleDeath(c);
        } else if (Lowpkarena.getState(c) != null) {
            Lowpkarena.handleDeath(c);
        } else if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
            c.getPA().movePlayer(c.absX, 4759, 0);
        } else if (Boundary.isIn(c, Boundary.SAFEPKSAFE)) {
            c.getPA().movePlayer(Configuration.RESPAWN_X, Configuration.RESPAWN_Y, 0);
            onRespawn(c);
        } else {

                c.getPA().movePlayer(Configuration.RESPAWN_X, Configuration.RESPAWN_Y, 0);

                Entity killer = c.getKiller();
                Player playerKiller = killer != null && killer.isPlayer() ? killer.asPlayer() : null;

                ItemsLostOnDeathList itemsLostOnDeathList = ItemsLostOnDeath.generateModified(c);

//                Server.getLogging().write(new DeathItemsHeld(c, c.getItems().getInventoryItems(), c.getItems().getEquipmentItems()),
//                        new DeathItemsKept(c, itemsLostOnDeathList.getKept()),
//                        new DeathItemsLost(c, itemsLostOnDeathList.getLost()));

                c.getItems().deleteAllItems();
                List<GameItem> lostItems = itemsLostOnDeathList.getLost();

                // Drop untradeable cash (or other stuff depending on the item dropped) and put in lost property shop for victim (depending on item dropped)
                List<GameItem> untradeables = lostItems.stream().filter(it -> !it.getDef().isTradable()).collect(Collectors.toList());


//            List<GameItem> specialitems =  new ArrayList<>();
//
//                if(lostItems.contains(new GameItem(21817,1))){
//                    specialitems.add(new GameItem(21817,1));
//                }
                //todo: add dying with pak yak

                // Drop untradeable coins for killer, otherwise drop nothing
                if (playerKiller != null) {

//                    if(!untradeables.isEmpty()){
//                        if (untradeables.contains(new GameItem(21816, 1))) {
//                            lostItems.add(new GameItem(Items.REVENANT_ETHER, c.braceletEtherCount));
//                            c.sendMessage("Died with bracelet of ethereum.");
//                        }
//                    }
                    int coins = untradeables.stream().mapToInt(it -> it.getDef().getShopValue()).sum();
                    if (coins > 0)
                        lostItems.add(new GameItem(Items.COINS, coins));

                }

                lostItems.removeAll(untradeables);
          //  lostItems.removeAll(specialitems);
                untradeables.forEach(item -> c.getPerduLostPropertyShop().add(c, item));

                dropItemsForKiller(c, playerKiller, new GameItem(Items.BONES));




                if (playerKiller != null){
                    PkpRewards.award(c, playerKiller);
                    if (Boundary.isIn(c, Boundary.REV_CAVE)) {
                        lostItems.add(new GameItem(995, BountyHunter.revcavefee));
                    }

                }

            lostItems.forEach(item -> dropItemsForKiller(c, playerKiller, item));


                for (GameItem item : itemsLostOnDeathList.getKept()) {
                    if (c.getItems().hasRoomInInventory(item.getId(), item.getAmount())) {
                        c.getItems().addItem(item.getId(), item.getAmount());
                    } else {
                        c.getItems().sendItemToAnyTab(item.getId(), item.getAmount());
                    }
                }



         //   }
            onRespawn(c);
        }
    }


    public static void dropItemsForKiller(Player killed, Player killer, GameItem item) {
        if (killer != null) {

            // Removes the PvP HP overlay from the killers screen when the target dies
            if (killed.equals(killer.getTargeted())) {
                killer.setTargeted(null);
                killer.getPA().sendEntityTarget(0, killed);
            }

            if (!killer.getMode().isItemScavengingPermitted()) {
                Server.itemHandler.createUnownedGroundItem(item, killed.getPosition());
            } else {
                Server.itemHandler.createGroundItem(killer, item, killed.getPosition());
            }
        } else {
            Server.itemHandler.createGroundItem(killed, item, killed.getPosition(), Misc.toCycles(3, TimeUnit.MINUTES));
        }
    }

    public static void onRespawn(Player c) {
        c.isSkulled = false;
        c.skullTimer = 0;
        c.attackedPlayers.clear();
        c.getPA().removeAllWindows();
        c.getPA().closeAllWindows();
        c.resetDamageTaken();
        c.setKiller(null);
        c.killerId = 0;
    }

}
