package io.Odyssey;

import io.Odyssey.annotate.Init;
import io.Odyssey.annotate.PostInit;
import io.Odyssey.content.boosts.Boosts;
import io.Odyssey.content.bosses.AngelofdeathSpawner;
import io.Odyssey.content.bosses.RevenantMaledictus;
import io.Odyssey.content.bosses.godwars.GodwarsEquipment;
import io.Odyssey.content.bosses.godwars.GodwarsNPCs;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.bosses.nightmare.NightmareStatusNPC;
import io.Odyssey.content.bosses.sarachnis.SarachnisNpc;
import io.Odyssey.content.collection_log.CollectionLog;
import io.Odyssey.content.combat.stats.TrackedMonster;
import io.Odyssey.content.commands.CommandManager;
import io.Odyssey.content.dailyrewards.DailyRewardContainer;
import io.Odyssey.content.dailyrewards.DailyRewardsRecords;
import io.Odyssey.content.donationrewards.DonationReward;
import io.Odyssey.content.event.eventcalendar.EventCalendar;
import io.Odyssey.content.event.eventcalendar.EventCalendarWinnerSelect;
import io.Odyssey.content.polls.PollTab;
import io.Odyssey.content.preset.PresetManager;
import io.Odyssey.content.referral.ReferralCode;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.content.trails.TreasureTrailsRewards;
import io.Odyssey.content.vote_panel.VotePanelManager;
import io.Odyssey.content.wogw.Wogw;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.ObjectDef;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.collisionmap.doors.Doors;
import io.Odyssey.model.collisionmap.doors.DoubleDoors;
import io.Odyssey.model.cycleevent.impl.BonusApplianceEvent;
import io.Odyssey.model.cycleevent.impl.LeaderboardUpdateEvent;
import io.Odyssey.model.cycleevent.impl.NexGodWarsEvent;

import io.Odyssey.model.definitions.*;
import io.Odyssey.model.entity.npc.NPCRelationship;
import io.Odyssey.model.entity.npc.NpcSpawnLoader;
import io.Odyssey.model.entity.npc.stats.NpcCombatDefinition;
import io.Odyssey.model.entity.player.PlayerFactory;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.items.EquipmentInfo;
import io.Odyssey.model.items.GlobalDropsHandler;
import io.Odyssey.model.items.ItemWeight;
import io.Odyssey.model.lobby.LobbyManager;
import io.Odyssey.model.world.ShopHandler;
import io.Odyssey.net.discord.discordintegration.Bot;
import io.Odyssey.net.discord.discordintegration.DiscordIntegration;
import io.Odyssey.punishments.PunishmentCycleEvent;
import io.Odyssey.util.ExamineRepository;
import io.Odyssey.util.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.Odyssey.content.bosses.RevenantMaledictus.REVENANT_MALEDICTUS_ID;

/**
 * Stuff to do on startup.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class ServerStartup {

    private static final Logger logger = LoggerFactory.getLogger(ServerStartup.class);

    static void load() throws Exception {
        Reflection.getMethodsAnnotatedWith(Init.class).forEach(method -> {
            try {
                method.invoke(null);
            } catch (Exception e) {
                logger.error("Error loading @Init annotated method[{}] inside class[{}]", method, method.getClass(), e);
                e.printStackTrace();
                System.exit(1);
            }
        });

        DonationReward.load();
        PlayerSave.loadPlayerSaveEntries();
        EventCalendarWinnerSelect.getInstance().init();
        EquipmentInfo.getInstance().loadEquipmentRequirements();
        TrackedMonster.init();
        Boosts.init();
        ItemDef.load();
        ShopDef.load();
        ShopHandler.load();
        NpcStats.load();
        ItemStats.load();
        GlobalDropsHandler.initialize();
       DiscordIntegration.loadConnectedAccounts();

       // Server.getWorld().discordBot = new Bot();
      //  Server.getWorld().discordBot.init();
        try {
            ItemWeight.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NpcDef.load();
        // Npc Combat Definition must be above npc load
        NpcCombatDefinition.load();
        Server.npcHandler.init();
        NPCRelationship.setup();
        EventCalendar.verifyCalendar();
        Server.getPunishments().initialize();
      //  Server.getEventHandler().submit(new DidYouKnowEvent());
  Server.getEventHandler().submit(new BonusApplianceEvent());
        Server.getEventHandler().submit(new PunishmentCycleEvent(Server.getPunishments(), 50));
     // Server.getEventHandler().submit(new NexGodWarsEvent());
        Server.getEventHandler().submit(new LeaderboardUpdateEvent());
        Server.getEventHandler().submit(new GlobalDropsHandler());
        Listing.init();
        Wogw.init();
        PollTab.init();


        Doors.getSingleton().load();
        DoubleDoors.getSingleton().load();
        GodwarsEquipment.load();
        GodwarsNPCs.load();
        LobbyManager.initializeLobbies();
        VotePanelManager.init();
        TourneyManager.initialiseSingleton();
        TourneyManager.getSingleton().init();
        Server.getDropManager().read();
        TreasureTrailsRewards.load();
        AnimationLength.startup();
        PresetManager.getSingleton().init();
        ObjectDef.loadConfig();
        CollectionLog.init();
        Region.load();
      Server.getGlobalObjects().loadGlobalObjectFile();
        Server.examineRepository = new ExamineRepository();
        // Keep this below region load and object loading
        NpcSpawnLoader.load();
     //   MonsterHunt.spawnNPC();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        CommandManager.initializeCommands();
        NightmareStatusNPC.init();//what starts nightmare?
        if (Server.isDebug()) {
            PlayerFactory.createTestPlayers();
        }
        ReferralCode.load();
     DailyRewardContainer.load();
      DailyRewardsRecords.load();
       WorldEventContainer.getInstance().initialise();
     //   FireOfExchangeBurnPrice.init();
       // Server.getLogging().schedule();

      //  ZamorakGuardian.spawn();
        new SarachnisNpc(Npcs.SARACHNIS, SarachnisNpc.SPAWN_POSITION);
       // AngelofdeathSpawner.spawnNPC();
 // new NexNPC(11278, NexNPC.SPAWN_POSITION);
        new RevenantMaledictus(REVENANT_MALEDICTUS_ID, RevenantMaledictus.SPAWN_POSITION);
//        if (Server.isPublic()) {
//            PlayerSaveBackup.start(Configuration.PLAYER_SAVE_TIMER_MILLIS, Configuration.PLAYER_SAVE_BACKUP_EVERY_X_SAVE_TICKS);
//        }
        Doors.getSingleton().processLineByLine_toload();//has to be here.
        Reflection.getMethodsAnnotatedWith(PostInit.class).forEach(method -> {
            try {
                method.invoke(null);
            } catch (Exception e) {
                logger.error("Error loading @PostInit annotated method[{}] inside class[{}]", method, method.getClass(), e);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }


}
