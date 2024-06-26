package io.Odyssey.content.combat.death;

import java.util.Optional;
import java.util.stream.IntStream;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.achievement_diary.impl.FremennikDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.MorytaniaDiaryEntry;
import io.Odyssey.content.bosses.Hunllef;
import io.Odyssey.content.bosses.Kraken;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.nightmare.NightmareConstants;
import io.Odyssey.content.bosses.wildypursuit.FragmentOfSeren;
import io.Odyssey.content.bosses.wildypursuit.TheUnbearable;
import io.Odyssey.content.bosspoints.BossPoints;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.events.monsterhunt.MonsterHunt;
import io.Odyssey.content.minigames.warriors_guild.AnimatedArmour;
import io.Odyssey.content.questing.recipefordisaster.RFDInstance;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.drops.DropManager;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.items.EquipmentSet;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.util.Location3D;
import io.Odyssey.util.Misc;

public class NPCDeath {

    public static void dropItems(NPC npc) {
        Player c = PlayerHandler.players[npc.killedBy];
        if (c != null) {
            dropItemsFor(npc, c, npc.getNpcId());
        }
    }

    public static void dropItemsFor(NPC npc, Player c, int npcId) {
        if (c.getTargeted() != null && npc.equals(c.getTargeted())) {
            c.setTargeted(null);
            c.getPA().sendEntityTarget(0, npc);
        }
        c.getAchievements().kill(npc);
        PetHandler.rollOnNpcDeath(c, npc);
     //   if (npcId >= 1610 && npcId <= 1612) {
            //	c.setArenaPoints(c.getArenaPoints() + 1);
           // c.getQuestTab().updateInformationTab();
            //	c.sendMessage("@red@You gain 1 point for killing the Mage! You now have " + c.getArenaPoints()
            //+ " Arena Points.");
      //  }

        if (npcId == 2266 || npcId == 2267 || npcId == 2265) {
            c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.KILL_DAGANNOTH_KINGS);
        }
        if (npcId == 411) {
            c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.KILL_KURASK);
        }



        if (npcId == 9021 || npcId == 9022 || npcId == 9023 || npcId == 9024) {
          //  c.hunllefDead = true;
            //PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ Hunllef has been defeated by @bla@" + c.playerName + "@blu@!");
            Optional<Hunllef> hunlleffinstance = NPCHandler.getHunlleffInstance(npc);

                if(hunlleffinstance.isPresent())
                    hunlleffinstance.get().rewardPlayers();
           // Hunllef.rewardPlayers(c);
        }
        if (npcId == 1047) {
            c.getDiaryManager().getMorytaniaDiary().progress(MorytaniaDiaryEntry.KILL_CAVE_HORROR);
        }

      //  if (npcId == 1673) { //barrows npcs
           // Achievements.increase(c, AchievementType.DH_KILLS, 1);
     //   }
//        if (npcId == 5744 || npcId == 5762) {
//            c.setShayPoints(c.getShayPoints() + 1);
//            c.sendMessage("@red@You gain 1 point for killing the Penance! You now have " + c.getShayPoints()
//                    + " Assault Points.");
//        }

        if (npc.getNpcId() == 3162 || npc.getNpcId() == 3129 || npc.getNpcId() == 2205 || npc.getNpcId() == 2215) {
            c.getEventCalendar().progress(EventChallenge.KILL_X_GODWARS_BOSSES_OF_ANY_TYPE);
        }
        if (npc.getNpcId() == 9293) {
            c.getEventCalendar().progress(EventChallenge.KILL_BASILISK_KNIGHTS_X_TIMES);
        }

        if (npc.getNpcId() == 2042 || npc.getNpcId() == 2043 || npc.getNpcId() == 2044) {
            c.getEventCalendar().progress(EventChallenge.KILL_ZULRAH_X_TIMES);
        }
        if (npc.getNpcId() == 9021) {
            c.getEventCalendar().progress(EventChallenge.KILL_HUNLLEF_X_TIMES);
        }
        if (IntStream.of(DropManager.wildybossesforgiveaway).anyMatch(id -> id == npc.getNpcId()) && c.getPosition().inWild()) {
            c.getEventCalendar().progress(EventChallenge.KILL_X_WILDY_BOSSES);
        }
        if (npcId >= 7931 && npcId <= 7940 || npcId == 7881) {
            c.getEventCalendar().progress(EventChallenge.KILL_X_REVS_IN_WILDY);
        }

        if (npcId == Npcs.CORPOREAL_BEAST) {//lol interesting way to clean up world npcs haha
            NPCHandler.kill(Npcs.DARK_ENERGY_CORE, npc.heightLevel);
        }
        if (npcId == 7278) {
            if ((c.getSlayer().getTask().isPresent() && c.getSlayer().getTask().get().getPrimaryName().equals("nechryael"))) {
                c.getPA().addSkillXPMultiplied(100, Skill.SLAYER.getId(), true);
            }
        }

        if (npcId == Npcs.DUSK_9) {
            Achievements.increase(c, AchievementType.GROTESQUES, 1);
        }
        if (npcId == Npcs.ALCHEMICAL_HYDRA_7) {
            Achievements.increase(c, AchievementType.HYDRA, 1);
        }

        if (npcId == 2266 || npcId == 2267 || npcId == 2265) {
            if ((c.getSlayer().getTask().isPresent() && c.getSlayer().getTask().get().getPrimaryName().equals("dagannoth"))) {
                c.getPA().addSkillXPMultiplied(165, Skill.SLAYER.getId(), true);
            }
        }


        if (npcId == 7144 || npcId == 7145 || npcId == 7146) {
            c.getEventCalendar().progress(EventChallenge.KILL_X_DEMONIC_GORILLAS, 1);
        }

//        if (npcId == 1739 || npcId == 1740 || npcId == 1741 || npcId == 1742)
//        {
//            c.getEventCalendar().progress(EventChallenge.GAIN_X_PEST_CONTROL_POINTS, 7);
//            c.pcPoints += 7;
//        }
//
//        if (npcId == FragmentOfSeren.NPC_ID && npc.getHealth().getCurrentHealth() <= 0) {
//            // Player p = PlayerHandler.players[npc.killedBy];
//            int randomPkp = Misc.random(15) + 10;
//            c.pkp += randomPkp;
//            c.getQuestTab().updateInformationTab();
//            MonsterHunt.setCurrentLocation(null);
//            c.sendMessage("Well done! You killed Seren!");
//            c.sendMessage("You received: " + randomPkp + " PK Points for killing the wildy boss.");
//
//        }

//        if (npcId == TheUnbearable.NPC_ID && npc.getHealth().getCurrentHealth() <= 0) {
//            int randomPkp = Misc.random(15) + 10;
//            c.pkp += randomPkp;
//            c.getQuestTab().updateInformationTab();
//            MonsterHunt.setCurrentLocation(null);
//            c.sendMessage("Well done! You killed The Unbearable!");
//            c.sendMessage("You received: " + randomPkp + " PK Points for killing the wildy boss.");
//        }
//
//        if (npcId == AvatarOfCreation.NPC_ID && npc.getHealth().getCurrentHealth() <= 0) {
//            c.getQuestTab().updateInformationTab();
//            c.sendMessage("Well done! You killed AvatarOfCreation!");
//        }
        int dropX = npc.absX;
        int dropY = npc.absY;
        int dropHeight = npc.heightLevel;

        if (!PathFinder.getPathFinder().accessable(c, dropX, dropY)) {
            for (Position border : npc.getBorder()) {
                if (PathFinder.getPathFinder().accessable(c, dropX, dropY)) {
                    dropX = border.getX();
                    dropY = border.getY();
                    break;
                }
            }
        }

        if (npcId == 492 || npcId == 494 || npcId == NightmareConstants.NIGHTMARE_ACTIVE_ID || npcId == Npcs.VORKATH) {
            dropX = c.absX;
            dropY = c.absY;
        }
        if (npcId == 2042 || npcId == 2043 || npcId == 2044
                || npcId == 6720) {
            dropX = 2268;
            dropY = 3069;
            c.getItems().addItem(12938, 1);
            c.getZulrahEvent().stop();
        }
        if (npcId == Kraken.KRAKEN_ID) {
            dropX = 2280;
            dropY = 10031;
        }
        NpcDef npcDefinition = NpcDef.forId(npcId);
        if(npcDefinition != null)
            if(npcDefinition.getCombatLevel() >= 80){
                int randomchance = Misc.random(20);
                if(randomchance == 1){
                    Server.itemHandler.createGroundItem(c, 787, dropX, dropY, npc.heightLevel,1, c.getIndex());
                }
            }

        /**
         * Warriors guild
         */
        c.getWarriorsGuild().dropDefender(npc.absX, npc.absY);
        c.getWarriorsGuildbasement().dropDefender(npc.absX, npc.absY);
        if (AnimatedArmour.isAnimatedArmourNpc(npcId)) {
            if (npc.getX() == 2851 && npc.getY() == 3536) {
                dropX = 2851;
                dropY = 3537;
                AnimatedArmour.dropTokens(c, npcId, npc.absX, npc.absY + 1);
            } else if (npc.getX() == 2857 && npc.getY() == 3536) {
                dropX = 2857;
                dropY = 3537;
                AnimatedArmour.dropTokens(c, npcId, npc.absX, npc.absY + 1);
            } else {
                AnimatedArmour.dropTokens(c, npcId, npc.absX, npc.absY);
            }
        }

//if(npcId == 2259){
//    Server.itemHandler.createGroundItem(c, 995, dropX, dropY, npc.heightLevel,10, c.getIndex());
//
//}
        if(npcId == 7018){//reanimated goblin
            c.getPA().addSkillXPMultiplied(130, Skill.PRAYER.getId(), true);

        }
        Location3D location = new Location3D(dropX, dropY, dropHeight);
        int amountOfDrops = 1;


        int bossPoints = BossPoints.getPointsOnDeath(npc);
        BossPoints.addPoints(c, bossPoints, false);
        if (bossPoints > 0 && isDoubleDrops(c)) {
            amountOfDrops++;
        }
        if (NpcDef.forId(npcId).getCombatLevel() >= 2) {
            c.getNpcDeathTracker().add(NpcDef.forId(npcId).getName(), NpcDef.forId(npcId).getCombatLevel(), bossPoints);
        }

        Server.getDropManager().create(c, npc, location, amountOfDrops, npcId);//and HERE we get the items from the drop table ONLY
    }

    public static void announce(Player player, GameItem item, int npcId) {
        //if (!player.getDisplayName().equalsIgnoreCase("thimble") && !player.getDisplayName().equalsIgnoreCase("top hat")) {
            announceKc(player, item, player.getNpcDeathTracker().getKc(NpcDef.forId(npcId).getName()),npcId);
        //}
    }

    public static void announceKc(Player player, GameItem item, int kc, int npcId) {
        String npcName = NpcDef.forId(npcId).getName();
        PlayerHandler.executeGlobalMessage("@pur@" + player.getDisplayNameFormatted() + " received a drop: " +
                "" + ItemDef.forId(item.getId()).getName() + " x " + item.getAmount() + " at <col=E9362B>" + kc  + "</col>@pur@ kills.");
        DiscordMessager.sendLootations("[:drop_of_blood:] " + player.getDisplayName() + " received a rare drop: " + ItemDef.forId(item.getId()).getName() + " from "+npcName);
    }

    public static boolean isDoubleDrops(Player player) {
        // return (Configuration.DOUBLE_DROPS_TIMER > 0 || Configuration.DOUBLE_DROPS || player.ddScrollTicks > 0);
        return (player.ddScrollTicks > 0) ;
    }
}
