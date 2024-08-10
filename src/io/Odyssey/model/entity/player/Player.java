package io.Odyssey.model.entity.player;

import com.google.common.collect.Lists;
import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.*;
import io.Odyssey.content.achievement.AchievementHandler;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.achievement_diary.AchievementDiaryManager;
import io.Odyssey.content.achievement_diary.RechargeItems;
import io.Odyssey.content.battlepass.BattlePass;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.Cerberus;
import io.Odyssey.content.bosses.Skotizo;
import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.bosses.godwars.God;
import io.Odyssey.content.bosses.godwars.Godwars;
import io.Odyssey.content.bosses.godwars.GodwarsEquipment;
import io.Odyssey.content.bosses.nightmare.NightmareConstants;
import io.Odyssey.content.bosses.nightmare.NightmareLostItems;
import io.Odyssey.content.bosses.zulrah.Zulrah;
import io.Odyssey.content.bosses.zulrah.ZulrahLostItems;
import io.Odyssey.content.cheatprevention.RandomEventInterface;
import io.Odyssey.content.collection_log.CollectionLog;
import io.Odyssey.content.combat.CombatItems;
import io.Odyssey.content.combat.EntityDamageQueue;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.core.AttackEntity;
import io.Odyssey.content.combat.death.PlayerDeath;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl.ToragsEffect;
import io.Odyssey.content.combat.formula.MeleeMaxHit;
import io.Odyssey.content.combat.magic.CombatSpellData;
import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.content.combat.magic.items.PvpWeapons;
import io.Odyssey.content.combat.magic.items.pouch.GemBag;
import io.Odyssey.content.combat.magic.items.pouch.HerbSack;
import io.Odyssey.content.combat.magic.items.pouch.RunePouch;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.melee.MeleeData;
import io.Odyssey.content.combat.melee.MeleeExtras;
import io.Odyssey.content.combat.melee.QuickPrayers;
import io.Odyssey.content.combat.pvp.Killstreak;
import io.Odyssey.content.combat.stats.BossTimers;
import io.Odyssey.content.combat.stats.NPCDeathTracker;
import io.Odyssey.content.combat.weapon.CombatStyle;
import io.Odyssey.content.combat.weapon.WeaponMode;
import io.Odyssey.content.commands.all.Reclaim;
import io.Odyssey.content.dailyrewards.DailyRewards;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.donationrewards.DonationRewards;
import io.Odyssey.content.dwarfmulticannon.Cannon;
import io.Odyssey.content.event.eventcalendar.EventCalendar;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.event.eventcalendar.EventChallengeMonthlyReward;
import io.Odyssey.content.infotabs.TabHandling;
import io.Odyssey.content.item.lootable.impl.*;
import io.Odyssey.content.item.lootable.unref.*;
import io.Odyssey.content.itemskeptondeath.perdu.PerduLostPropertyShop;
import io.Odyssey.content.leaderboards.LeaderboardPeriodicity;
import io.Odyssey.content.leaderboards.LeaderboardUtils;
import io.Odyssey.content.lootbag.LootingBag;
import io.Odyssey.content.lootbag.LootingBagItem;
import io.Odyssey.content.mail.Mail;
import io.Odyssey.content.minigames.Raids3.Raids3;
import io.Odyssey.content.minigames.Raids3.Raids3Constants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutContainer;
import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.content.minigames.barrows.Barrows;
import io.Odyssey.content.minigames.barrows.TunnelEvent_regular;
import io.Odyssey.content.minigames.bounty_hunter.BountyHunter;
import io.Odyssey.content.minigames.fight_cave.FightCave;
import io.Odyssey.content.minigames.inferno.Inferno;
import io.Odyssey.content.minigames.pest_control.PestControl;
import io.Odyssey.content.minigames.pest_control.PestControlRewards;
import io.Odyssey.content.minigames.pk_arena.Highpkarena;
import io.Odyssey.content.minigames.pk_arena.Lowpkarena;
import io.Odyssey.content.minigames.raids.RaidConstants;
import io.Odyssey.content.minigames.raids.Raids;
import io.Odyssey.content.minigames.tob.TobConstants;
import io.Odyssey.content.minigames.tob.TobContainer;
import io.Odyssey.content.minigames.tob.instance.TobInstance;
import io.Odyssey.content.minigames.tokkul_pit.TokkulPit1;
import io.Odyssey.content.minigames.warriors_guild.WarriorsGuild;
import io.Odyssey.content.minigames.warriors_guild.WarriorsGuildbasement;
import io.Odyssey.content.minigames.xeric.XericLobby;
import io.Odyssey.content.miniquests.MageArena;
import io.Odyssey.content.miniquests.magearenaii.MageArenaII;
import io.Odyssey.content.newteleport.NewTeleInterface;
import io.Odyssey.content.newteleport.SpecificTeleport;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.content.preset.Preset;
import io.Odyssey.content.prestige.PrestigeSkills;
import io.Odyssey.content.pricechecker.PriceChecker;
import io.Odyssey.content.privatemessaging.FriendsList;
import io.Odyssey.content.questing.Questing;
import io.Odyssey.content.randomevents.Genie;
import io.Odyssey.content.skills.Agility;
import io.Odyssey.content.skills.ExpLock;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.SkillInterfaces;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.agility.impl.*;
import io.Odyssey.content.skills.agility.impl.rooftop.*;
import io.Odyssey.content.skills.farming.Farming;
import io.Odyssey.content.skills.firemake.LightSources;
import io.Odyssey.content.skills.fletching.Fletching;
import io.Odyssey.content.skills.herblore.Herblore;
import io.Odyssey.content.skills.hunter.Hunter;
import io.Odyssey.content.skills.mining.Mining;
import io.Odyssey.content.skills.prayer.Prayer;
import io.Odyssey.content.skills.slayer.Slayer;
import io.Odyssey.content.skills.slayer.SlayerUnlock;
import io.Odyssey.content.skills.smithing.Smelting;
import io.Odyssey.content.skills.smithing.Smithing;
import io.Odyssey.content.skills.smithing.SmithingInterface;
import io.Odyssey.content.skills.thieving.Thieving;
import io.Odyssey.content.tasks.TaskMasterManager;
import io.Odyssey.content.tasks.impl.Tasks;
import io.Odyssey.content.teleportation.newest.PortalTeleports;
import io.Odyssey.content.titles.Titles;
import io.Odyssey.content.tournaments.OutlastLeaderboardEntry;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.content.trails.TreasureTrails;
import io.Odyssey.content.tutorial.ModeSelection;
import io.Odyssey.content.tutorial.TutorialDialogue;
import io.Odyssey.content.vote_panel.VotePanelManager;
import io.Odyssey.content.wogw.WogwContributeInterface;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.model.*;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.collisionmap.doors.Location;
import io.Odyssey.model.controller.Controller;
import io.Odyssey.model.controller.ControllerRepository;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.cycleevent.impl.DesertEvent;
import io.Odyssey.model.cycleevent.impl.MinigamePlayersEvent;
import io.Odyssey.model.cycleevent.impl.RunEnergyEvent;
import io.Odyssey.model.cycleevent.impl.SkillRestorationEvent;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.EntityReference;
import io.Odyssey.model.entity.HealthStatus;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.broadcasts.Broadcast;
import io.Odyssey.model.entity.player.lock.PlayerLock;
import io.Odyssey.model.entity.player.lock.Unlocked;
import io.Odyssey.model.entity.player.mode.Mode;
import io.Odyssey.model.entity.player.mode.ModeRevertType;
import io.Odyssey.model.entity.player.mode.ModeType;
import io.Odyssey.model.entity.player.mode.RegularMode;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanGroup;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanRepository;
import io.Odyssey.model.entity.player.save.PlayerAddresses;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.items.*;
import io.Odyssey.model.items.bank.Bank;
import io.Odyssey.model.items.bank.BankPin;
import io.Odyssey.model.items.bank.DepositBox;
import io.Odyssey.model.lobby.LobbyManager;
import io.Odyssey.model.lobby.LobbyType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.Duel;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.multiplayersession.flowerpoker.FlowerPoker;
import io.Odyssey.model.multiplayersession.flowerpoker.FlowerPokerHand;
import io.Odyssey.model.multiplayersession.trade.Trade;
import io.Odyssey.model.shops.ShopAssistant;
import io.Odyssey.model.tickable.Tickable;
import io.Odyssey.model.tickable.TickableContainer;
import io.Odyssey.model.timers.TickTimer;
import io.Odyssey.model.world.Clan;
import io.Odyssey.net.Packet;
import io.Odyssey.net.PacketBuilder;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.net.discord.discordintegration.DiscordIntegration;
import io.Odyssey.net.login.LoginReturnCode;
import io.Odyssey.net.login.RS2LoginProtocol;
import io.Odyssey.net.outgoing.UnnecessaryPacketDropper;
import io.Odyssey.sql.Hiscores;
import io.Odyssey.sql.NewStone_claim;
import io.Odyssey.sql.NewStore;
import io.Odyssey.sql.etc.HiscoresUpdateQuery;
import io.Odyssey.sql.outlast.OutlastLeaderboardAdd;
import io.Odyssey.util.*;
import io.Odyssey.util.discord.Discord;
import io.Odyssey.util.logging.player.ChangeAddressLog;
import io.Odyssey.util.logging.player.ConnectionLog;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Player extends Entity {
public boolean unlockedallteles;

    public boolean firstropesaradomingwd;

    public boolean secondropesaradomingwd;

    public boolean hasunlockedfrozendoor;
public void walkthroughalkharidgate(){
getItems().deleteItem(995,10);

        getPA().object(44599, 3268, 3227, 3, 0);
        getPA().object(44598, 3268, 3228, 1, 0);


    PathFinder.getPathFinder().findRouteNoclip(this, absX > 3267 ? 3267 : 3268, absY, true, 0, 0);
    setNewWalkCmdIsRunning(false);

				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {

                        getPA().object(44599, 3268, 3228, 0, 0);
                        getPA().object(44598, 3268, 3227, 0, 0);
						container.stop();
					}

				}, 3);
}


    public boolean learnedGWDentrance = false;



    public static final Position[] abysspos = {new Position(3049, 4807), new Position(3065, 4830),
            new Position(3054, 4851), new Position(3031, 4858), new Position(3014, 4849)};

    public void teleToAbyss() {
        final NPC WIZARD = NPCHandler.npcs[clickedNpcIndex];//           NPC npc = NPCHandler.npcs[npcIndex];//c.talkingNpc
        WIZARD.facePlayer(this.getIndex());
        WIZARD.startAnimation(1818);
        WIZARD.gfx0(343);
        WIZARD.forceChat("Veniens! Sallakar! Rinnesset!");
        Position telepos =  abysspos[Misc.random(abysspos.length-1)];
     getPA().startTeleport(telepos.getX(), telepos.getY(), 0,"teleother", false);
        CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {

                playerLevel[5] = 0;

                getPA().refreshSkill(5);
                isSkulled = true;
                skullTimer = Configuration.SKULL_TIMER;
                headIconPk = 0;
                getPA().requestUpdates();
                container.stop();
            }

        }, 6);

     //drain prayer and skull


      getPA().removeAllWindows();

    }
public boolean paidrevfee;

    public void checkforregionunlocks() {
        Region region = getRegionProvider().get(getX(), getY());
        if (region == null)
            return;

        if (Region.regionnames.containsKey(region.id()))
            if (!regionsunlocked.containsKey(region.id())){//issue for existing players :/ oh well xd
                sendMessage("New area discovered: " + Color.DARK_GREEN.wrap(Region.regionnames.get(region.id())));

        regionsunlocked.put(region.id(), true);
    }
    }

    public void handleaSlayerRing() {
        if(operatingSlayerRingId != 0){
            switch (operatingSlayerRingId) {
                case 11866://8
                case 11867://7
                case 11868://6
                case 11869://5
                case 11870://4
                case 11871://3
                case 11872://2
                    getItems().setEquipment(operatingSlayerRingId+1, 1, playerRing,false);
                    break;
                case 11873:
                    getItems().setEquipment(-1, -1, playerRing,true);
                  //  getItems().unequipItem(11873, playerRing);
                   // getItems().deleteItem(11873,playerRing, 1);
                    break;
            }
        }
        operatingSlayerRingId = 0;
    }

    public int operatingSlayerRingId = 0;
public Map<Integer,Boolean> regionsunlocked  = new HashMap<>();
    private ZulrahLostItems lostItemsZulrah;
    public ZulrahLostItems getZulrahLostItems() {
        if (lostItemsZulrah == null) {
            lostItemsZulrah = new ZulrahLostItems(this);
        }
        return lostItemsZulrah;
    }


    private NightmareLostItems lostItemsNightmare;
    public NightmareLostItems getNightmareLostItems() {
        if (lostItemsNightmare == null) {
            lostItemsNightmare = new NightmareLostItems(this);
        }
        return lostItemsNightmare;
    }
    public void handlecursesunlocks() {
        //if you havent ever unlocked curses you cant possibly have that sidebar.
//        if(!getSlayer().getUnlocks().contains(SlayerUnlock.CURSES)){
//            usingcurseprayers = false;
//            getPA().sendFrame70(0, -1000, 127_890);
//            getPA().sendFrame70(0, -1000, 127_891);
////System.out.println("here?");
//            //move prayer down
//            getPA().sendFrame70(0, 15, 5651);
//            getPA().sendFrame70(0, 15, 687);
//        } else {
//            getPA().sendFrame70(0, 0, 5651);
//            getPA().sendFrame70(0, 0, 687);
//            getPA().sendFrame70(0, 0, 127_890);
//            getPA().sendFrame70(0, 0, 127_891);
//        }
        getPA().sendFrame70(0, 0, 5651);
        getPA().sendFrame70(0, 0, 687);
        getPA().sendFrame70(0, 0, 127_890);
        getPA().sendFrame70(0, 0, 127_891);
    }

public void checkpackyackslots() {
    if (getRights().contains(Right.ONYX_CLUB)) {
        packyakSlots += 3;
    } else if (getRights().contains(Right.DIAMOND_CLUB)) {
        packyakSlots += 3;
    } else if (getRights().contains(Right.LEGENDARY_DONATOR)) {
        packyakSlots += 3;
    } else if (getRights().contains(Right.EXTREME_DONOR)) {
        packyakSlots += 3;
    } else if (getRights().contains(Right.REGULAR_DONATOR)) {
        packyakSlots += 3;
    }
}
    public void enterYakName() {
        String header = "Name your pack yak";
        getPA().sendEnterString(header, (pl, otherName) -> {
            //pl.sendMessage("@red@Opening wiki ");
           // pl.getPA().sendFrame126("https://oldschool.runescape.wiki/w/"+ otherName.replace(" ", "_"), 12000);
            yakname = otherName.replaceAll(" ","_");
            sendMessage("You named your Pack Yak '@blu@"+otherName+"@bla@'.");

        });
    }
   public List<Mail> mailboxEntries = new ArrayList<>();
//where handling of donation items is handled
public void handlemailboxclick(int buttonId){
    int slot = buttonId - 177_157;

    int theid = slot > mailboxEntries.size()-1 ? -1 : mailboxEntries.get(slot).getId();
  // sendMessage("id: "+theid);
    if(theid == -1){
        sendMessage("Nothing there.");
        return;
    }
    new NewStone_claim(this,theid).run();

}


    public void openmailbox() {

        mailboxEntries.clear();
        for (int i = 0; i < 20; i++) {

            //   getPA().sendInterfaceHidden(1, 111_005+i);
            getPA().sendInterfaceHidden(1, 111_100 + i);
            getPA().itemOnInterface(-1, -1, 21614 + i, 0);
            getPA().sendInterfaceHidden(1, 111_200 + i);
            getPA().sendInterfaceHidden(1, 111_300 + i);
            getPA().sendString(111_200 + i, "");
            getPA().sendString(111_300 + i, "");

        }

        new NewStore(this, true).run();
        int i = 0;
        // sendMessage("handlemailboxslots##"+mailboxEntries.size());
        for (Mail entry : mailboxEntries) {

            //    getPA().sendInterfaceHidden(0, 111_005+i);
            getPA().sendInterfaceHidden(0, 111_100 + i);
            getPA().sendInterfaceHidden(0, 111_200 + i);
            getPA().sendInterfaceHidden(0, 111_300 + i);
            getPA().itemOnInterface(entry.getItemId(), entry.getAmount(), 21614 + i, 0);
            getPA().sendString(111_200 + i, "From: @gre@Runescape");
            getPA().sendString(111_300 + i, entry.getTextmsg());
            i++;
        }
        //    sendMessage("handlemailboxslots##"+mailboxEntries.size());//highlights the slots


        getPA().showInterface(111_000);
    }
    public void acceptslayerinv(Player me, Player guyinvitingme){
       // Slayer thatguystask_ = theguyinvitingme.getSlayer();
      //  Task thatguystask = theguyinvitingme.getSlayer().getTask().get();
        Consumer<Player> tutorialdialogue = p -> {
            //if (mode == ModeType.STANDARD) {//
           // finish(player, mode);
         //   p.start(new DialogueBuilder(p).setNpcId(DAILY_REWARDS_NPC).option(XP_RATES));
          //  Task task = theguyinvitingme.getSlayer().getTask().get();

           // getSlayer().setTask(Optional.of(task));
           // taskAmount = Misc.random(Range.between(minimum, maximum));
      //   getSlayer().setTaskAmount(amountorig);
       //  lastTask = task.getFormattedName();
            //taskAmountorig = taskAmount;
          //  master = m.getId();
p.sendMessage("is partner!");
            partnername = guyinvitingme.getDisplayName();
guyinvitingme.sendMessage(this.getDisplayName()+" has accepted your task!");
        };

       // me.getPA().closeAllWindows();
               new DialogueBuilder(me).option(
                                    new DialogueOption("Personal", plr -> plr.getCollectionLog().openInterface(plr)),
            new DialogueOption("Group", p->p.sendMessage("test"))
                            ).send();
//        start(new DialogueBuilder(me)
//                .setNpcId(TUTORIAL_NPC)
//                .option(new DialogueOption("Confirm accept slayer partner invite from "+guyinvitingme.getDisplayName()+"?", tutorialdialogue),
//                        new DialogueOption("No", me2 ->me2.getPA().closeAllWindows()))
//        );
    }

    public void openslayerpartner() {
    String partnername_ = partnername.equals("") ?  "@red@(none)" : "@gre@thename";
       getPA().sendString(partnername_, 96505);
getPA().showInterface(96500);
    }
    public String partnername = "";//save this
    public void slayerpartner() {
        String header = "Who would you like as your partner?";
        getPA().sendEnterString(header, (pl, otherName) -> {
            //pl.sendMessage("@red@Opening wiki ");
            // pl.getPA().sendFrame126("https://oldschool.runescape.wiki/w/"+ otherName.replace(" ", "_"), 12000);
            partnername = otherName.replaceAll(" ", "_");
            Player p = PlayerHandler.getPlayerByDisplayName(partnername);
//            Task thetaskimsharing = getSlayer().getTask().get();
//            if(thetaskimsharing == null){
//                sendMessage("You don't have a task.");
//                return;
//            }
            if (p == null){
                sendMessage("Not online or something?");
                return;

        }
            if (!p.acceptAid) {
                sendMessage("This player is currently not accepting aid.");
                return;
            }
//            if (p.getSlayer().getTask().isPresent()){
//                sendMessage("The player already has a task.");
//                return;
//            }
//            SlayerMaster.get(getSlayer().getMaster()).ifPresent(m -> {
//                if (p.calculateCombatLevel() < m.getLevel()) {
//                    p.sendMessage("You need a combat level of " + m.getLevel() + " to receive tasks from me. Please come back when you have this combat level.");
//                }
//            });
//            if(!p.getSlayer().canigetatask(thetaskimsharing)){
// sendMessage("You need a combat level of " + thetaskimsharing.getLevel() + " to receive tasks from me. Please come back when you have this combat level.");
//
//                return;
//            }
            int amountorig = getSlayer().getTaskAmountorig();
//            acceptslayerinv(p,this,thetaskimsharing,amountorig);
            acceptslayerinv(p,this);
           // sendMessage("You named your Pack Yak '@blu@"+otherName+"@bla@'.");

        });
    }
public void startDiscordIntegration() {
    new DialogueBuilder(this).statement("To link your account go to the #discordintegration channel", "And type !connect", "after that press continue here.")
            .option(new DialogueOption("Open discord channel", p -> p.getPA().sendFrame126("https://discord.com/", 12000)),
                    new DialogueOption("I have received my code", plr -> plr.getPA().sendEnterString("Enter the code",DiscordIntegration::integrateAccount))).send();

}

    @Getter
    @Setter
    private long discordUser;

    @Getter
    @Setter
    private String discordTag;

public String yakname;
public boolean deserttreasure;
    public int maRound = 0;
    public int[] holybook = new int[4];
    public int[] unholybook = new int[4];
    public int[] balancebook = new int[4];

    public List<LootingBagItem> pakyakitems = new ArrayList<>();
    public boolean inpakyak;
    public int packyakSlots = 3;
    private PakYak pakYak = new PakYak(this);
    public PakYak getPakYak() {
        return pakYak;
    }

    public int itemUsing;
    public boolean isUsingWealth;
    public boolean usingWealth;
    public boolean isUsingCombatBracelet = false;
    public boolean usingCombatBracelet = false;
    public boolean isUsingDuelling = false;
    public boolean usingDuelling = false;


    public void customclips() {

        //todo: make this only in barrows
        if (Boundary.isIn(this,Barrows.TUNNEL)) {
            if (!Server.getEventHandler().isRunning(this, "barrows_tunnel_regular")) {
                Server.getEventHandler().submit(new TunnelEvent_regular("barrows_tunnel_regular", this, 30));
            }
        }
//        if (Boundary.isIn(this,Boundary.TAVELRY_DUNGEON)) {
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2907,9698, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2908,9698, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2907,9697, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2908,9697, getHeight());
//        }
        if (Boundary.isIn(this,Boundary.WARRIORS_GUILD)) {
            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2854,3546, getHeight());
            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2855,3546, getHeight());
        }
        if (Boundary.isIn(this,Boundary.NEX_ENCAMPMENT)) {
            //[11/4/23, 12:12 AM]: new Position(2902, 5200, 0),
            //[11/4/23, 12:12 AM]: new Position(2906, 5202, 0),
            for (int x = 2902; x < 2906; x++) {
                for (int y = 5200; y < 5202; y++) {

                    getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG, x, y, getHeight());
                }
            }
        }
//        if (Boundary.isIn(this,Boundary.HOME_ISLAND)) {
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2004,3620, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2004,3625, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2004,3626, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2012,3656, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2014,3639, getHeight());
//
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2012,3630, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2012,3626, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2014,3626, getHeight());
//
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2009,3648, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2008,3598, getHeight());
//
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2012,3652, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2035, 3676, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2030,3677, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2034,3678, getHeight());
//
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2034,3667, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2014,3624, getHeight());
//            getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2016, 3649, getHeight());
//        }
    }

    public int NOTIFICATION_BROADCASTS = 1456;
    public boolean notification_broadcast = true;
    public boolean puremode = false;
//    public void updateNotifications() {
//        getPA().sendConfig(NOTIFICATION_BROADCASTS,notification_broadcast ? 0 : 1);
//    }


    public int turmoilattack = 0;
    public int turmoildefence=0;
    public int turmoilstrength = 0;
    public int prestigepoints;
    public boolean extralogperk;
    public boolean usingcurseprayers = false;
public int totalquests = 3;
public int questsCompleted=0;
    public int totalqp =0;
    public  void updatePlayerPanel() {
        //the player panel stuff
    //getPA().sendFrame126(getDisplayName(), 80114);
     // getPA().sendFrame126("Membership: " + getRights().buildCrownString() + " " + getRights().getPrimary().toString(), 80113);
//the quest panel stuff
        getCollectionLog().cltopen(this);
        getPA().sendString(32580, ""+ Misc.capitalizeJustFirst(getDisplayName()));
        getPA().sendString(32572,""+calculateCombatLevel());
         getPA().sendString(32573,""+getPA().calculateTotalLevel());
        getPA().sendString(32574,""+Misc.insertCommasToNumber(String.valueOf(getExperienceCounter())));
        getPA().sendString(32575,questsCompleted+"/"+totalquests);//quests completed
        int amount = 0;
        for (Achievements.Achievement achievement : Achievements.Achievement.ACHIEVEMENTS) {
            if (this.getAchievements().isComplete(achievement.getTier().getId(), achievement.getId()))
                amount++;
        }

        getPA().sendString(32590,""+Server.getDropManager().ordered.size());//amount of drops - opens drop table
        getPA().sendString(29469,"Diaries Completed: "+getDiariesCompleted()+"/"+getDiariesTotal());//diaries completed


        getPA().sendString(32576, ""+ getViewingCollectionLog().totalitems_thatyouhave(this)+"/"+getViewingCollectionLog().totalitems_tocollect(this));//achievements completed - opens achievements

        getPA().sendString(620, "Quest Points: "+totalqp);//quest points
        getPA().sendString(32577, amount+"/"+Achievements.getMaximumAchievements());//combat amt


    }
    private final TaskMasterManager taskMasterManager = new TaskMasterManager(this);

    public TaskMasterManager getTaskMasterManager() {
        return taskMasterManager;
    }
    public Tasks TASK;
    public int TASK_AMOUNT;
    public int TASK_COMPLETE_AMOUNT;
    public int TASKS_COMPLETED;
    public int DAILY_TASK_DATE;
    public boolean COMPLETED_DAILY_TASK;
    public boolean CAN_CLAIM_TASK_REWARD;
    public int wild_diary_done;
    public int wild_diary_total;
    public int west_diary_done;
    public int west_diary_total;
    public int lumb_diary_done;
    public int lumb_diary_total;
    public int karamja_diary_done;
    public int karamja_diary_total;
    public int kandarin_diary_done;
    public int kandarin_diary_total;
    public int fremennik_diary_done;
    public int fremennik_diary_total;
    public int falador_diary_done;
    public int falador_diary_total;
    public int desert_diary_done;
    public int desert_diary_total;
    public int ardougne_diary_done;
    public int ardougne_diary_total;
    public int morytania_diary_done;
    public int morytania_diary_total;
    public int varrock_diary_done;
    public int varrock_diary_total;
    public int kourend_diary_done = 0;
    public int kourend_diary_total;
    public void updateDiary() {

        getDiaryManager().getArdougneDiary().updateDiary();
        getDiaryManager().getDesertDiary().updateDiary();
        getDiaryManager().getFaladorDiary().updateDiary();
        getDiaryManager().getFremennikDiary().updateDiary();
        getDiaryManager().getKandarinDiary().updateDiary();
        getDiaryManager().getKaramjaDiary().updateDiary();
        getDiaryManager().getLumbridgeDraynorDiary().updateDiary();
        getDiaryManager().getVarrockDiary().updateDiary();
        getDiaryManager().getWesternDiary().updateDiary();
        getDiaryManager().getWildernessDiary().updateDiary();
        getDiaryManager().getMorytaniaDiary().updateDiary();
        // getDiaryManager().getKourendDiary().updateDiary();
    }
    public int getDiariesCompleted() {
        updateDiary();
        int amtofachievementscompleted;
        amtofachievementscompleted = wild_diary_done + west_diary_done + lumb_diary_done + karamja_diary_done + kandarin_diary_done + fremennik_diary_done +
                falador_diary_done + desert_diary_done + ardougne_diary_done + morytania_diary_done + varrock_diary_done + kourend_diary_done;
        return amtofachievementscompleted;
    }
    public int getDiariesTotal() {
        int amtofachievementstotal;
        amtofachievementstotal = wild_diary_total + west_diary_total + lumb_diary_total + karamja_diary_total + kandarin_diary_total + fremennik_diary_total +
                falador_diary_total + desert_diary_total + ardougne_diary_total + morytania_diary_total + varrock_diary_total + kourend_diary_total;
        return amtofachievementstotal;
    }
    private static Logger logger = LoggerFactory.getLogger(Player.class);

    public static final int playerHat = 0;
    public static final int playerCape = 1;
    public static final int playerAmulet = 2;
    public static final int playerWeapon = 3;
    public static final int playerChest = 4;
    public static final int playerShield = 5;
    public static final int playerLegs = 7;
    public static final int playerHands = 9;
    public static final int playerFeet = 10;
    public static final int playerRing = 12;
    public static final int playerArrows = 13;
    public static final int playerAttack = 0;
    public static final int playerDefence = 1;
    public static final int playerStrength = 2;
    public static final int playerHitpoints = 3;
    public static final int playerRanged = 4;
    public static final int playerPrayer = 5;
    public static final int playerMagic = 6;
    public static final int playerCooking = 7;
    public static final int playerWoodcutting = 8;
    public static final int playerFletching = 9;
    public static final int playerFishing = 10;
    public static final int playerFiremaking = 11;
    public static final int playerMining = 14;
    public static final int playerHerblore = 15;
    public static final int playerAgility = 16;
    public static final int playerThieving = 17;
    public static final int playerSlayer = 18;
    public static final int playerFarming = 19;
    public static final int playerRunecrafting = 20;


    public int[] tempInventory = new int[28], tempInventoryN = new int[28], tempEquipment = new int[28], tempEquipmentN = new int[28];
    public boolean rubyBoltSpecial;
    public int bryophytaStaffCharges;
    public boolean inSafeBox;
    public long lastBankDeposit;

    private Barrows barrows = new Barrows(this);
    public Barrows getBarrows() {
        return barrows;
    }

    public long lastManualSeedPlant, lastForcedSeedPlant;
    public int flowerPokerWins, flowerPokerLoses, flowerPokerGames;
    public long biggestFlowerPokerPotWon;
    public long biggestFlowerPokerPotLost;
    public int modifierforpetdroprate;
    public boolean droprateboostedbypet;
    public int modifierforrangepet;
    public boolean saverunes;

    public void saveItemsForMinigame() {
        /**
         * Clones items
         */
        this.tempInventory = this.playerItems.clone();
        this.tempInventoryN = this.playerItemsN.clone();
        this.tempEquipment = this.playerEquipment.clone();
        this.tempEquipmentN = this.playerEquipmentN.clone();
        /**
         * Deletes
         */
        this.getItems().deleteAllItems();
        /**
         * Refreshes items
         */
        getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
        getItems().addContainerUpdate(ContainerUpdate.EQUIPMENT);
    }

    public void restoreItemsForMinigame() {
        /**
         * Clones items
         */
        this.playerItems = this.tempInventory.clone();
        this.playerItemsN = this.tempInventoryN.clone();
        this.playerEquipment = this.tempEquipment.clone();
        this.playerEquipmentN = this.tempEquipmentN.clone();
        /**
         * Restores
         */
        this.tempInventory = new int[28];
        this.tempInventoryN = new int[28];
        this.tempEquipment = new int[14];
        this.tempEquipmentN = new int[14];
        /**
         * Refreshes items
         */
        getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
        getItems().addContainerUpdate(ContainerUpdate.EQUIPMENT);
    }
    private BattlePass battlepass = new BattlePass(this);
    private Channel session;
    public Stream inStream;
    public Stream outStream;
    private static final Stream appearanceUpdateBlockCache;
    private long lastPacketReceived = System.currentTimeMillis();
    private Queue<Integer> previousPackets = new ConcurrentLinkedQueue<>();
    public int mapRegionX;
    public int mapRegionY;
    public int absX;
    public int absY;
    public int lastX;
    public int lastY;
    public int currentX;
    public int currentY;
    public int heightLevel;
    public int walkDirection = -1;
    public int runDirection = -1;
    public int crawlDirection = -1;
    private boolean crawlToggled = false;
    public int poimiX;
    public int poimiY;
    public int playerListSize;
    public int wQueueReadPtr;
    public int wQueueWritePtr;
    private int teleportToX = -1;
    private int teleportToY = -1;
    public int face = -1;
    public int FocusPointX = -1;
    public int FocusPointY = -1;
    public int newWalkCmdSteps;
    public int playerMagicBook;
    public final int walkingQueueSize = 50;
    public int[] walkingQueueX = new int[walkingQueueSize];
    public int[] walkingQueueY = new int[walkingQueueSize];
    private int[] newWalkCmdX = new int[walkingQueueSize];
    private int[] newWalkCmdY = new int[walkingQueueSize];
    protected int[] travelBackX = new int[walkingQueueSize];
    protected int[] travelBackY = new int[walkingQueueSize];

    public static final int maxPlayerListSize = Configuration.MAX_PLAYERS;
    public static final int maxNPCListSize = Configuration.MAX_NPCS_IN_LOCAL_LIST;
    public Player[] playerList = new Player[maxPlayerListSize];
    public byte[] playerInListBitmap = new byte[(Configuration.MAX_PLAYERS + 7) >> 3];

    public NPC[] npcList = new NPC[maxNPCListSize];
    public int npcListSize;

    private final HashSet<GroundItem> localGroundItems = new HashSet<>();

    private byte[] chatText = new byte[4096];
    private byte chatTextSize;

    private int migrationVersion;
    public EntityReference lastAttackedEntity = EntityReference.getReference(null);
    public EntityReference lastDefend;
    public long lastDefendTime;
    public int oldNpcIndex;
    public int playerAttackingIndex;
    public int npcAttackingIndex;
    public int mask400Var1 = -1;
    public int mask400Var2 = -1;
    public int forceMovementDirection = -1;
    public int prayerId = -1;
    public int headIcon = -1;
    public int headIconPk = -1;
    protected RightGroup rights;
    public AttackEntity attacking = new AttackEntity(this);
    private DialogueBuilder dialogueBuilder = null;
    public StringInput stringInputHandler;
    public AmountInput amountInputHandler;
    private long aggressionTimer = System.currentTimeMillis();
    private boolean printAttackStats = Server.isTest();
    private boolean printDefenceStats = Server.isTest();
    private boolean helpCcMuted = false;
    private boolean gambleBanned = false;
    public long lastDialogueSkip = 0;
    private boolean has2XDamage;
    public void set2XDamage(boolean set2xDamage) {
        this.has2XDamage = set2xDamage;
    }
    public boolean get2XDamage(){
        return has2XDamage;
    }
    public boolean lastDialogueNewSystem;
    public boolean gargoyleStairsUnlocked;
    private Controller controller;
    private Controller loadedController;
    private boolean joinedIronmanGroup;
    private long lastDatabaseAccess;

    private PlayerLock lock = new Unlocked();

    /**
     * THe Combat configurations for the player
     */
    private final CombatConfig combatConfigs = new CombatConfig(this);

    public CombatConfig getCombatConfigs() { return this.combatConfigs; }

    public void resetAggressionTimer() {
        aggressionTimer = System.currentTimeMillis();
    }

    public boolean isAggressionTimeout(Player player) {
        if (Boundary.isIn(player, Boundary.GODWARS_AREA) || Boundary.isIn(player, Boundary.CERBERUS_BOSSROOMS)) {
            return false;
        }
        return System.currentTimeMillis() - aggressionTimer >= TimeUnit.MINUTES.toMillis(15);
    }

    private boolean receivedCalendarCosmeticJune2021;
    public long serpHelmCombatTicks;
    //FOE
    public int currentExchangeItem;
    public int currentExchangeItemAmount;
    // Interface checks
    public boolean inTradingPost;
    public boolean inBank;
    public boolean inUimBank;
    public boolean unlockedUltimateChest;
    public boolean inPresets;
    public boolean inDonatorBox;
    public int boxusing = 0;
    public boolean inLamp;
    // Raids
    public int xericDamage;
    public int raidCount;

    public int raids3Count;
    public int tobCompletions;

    public int tombsOfAmascutCompletions;

    public int raids3Completions;
    public int pollOption;
    // Bank
    private Bank bank;
    public boolean placeHolderWarning;
    public int lastPlaceHolderWarning;
    public boolean placeHolders;
    public int previousTab;
    // Tournaments
    public int tourneyX;
    public int tourneyY;
    public boolean canAttack = true;
    public ArrayList<Integer> tourneyItemsReceived = new ArrayList<>();
    // Presets
    public boolean presetViewingDefault;
    public int presetViewingIndex;
    public Preset currentPreset;
    public io.Odyssey.content.preset.Set lastPreset;
    public boolean viewingInitialPreset;
    public boolean viewingPresets;
    public long lastPresetClick;
    public boolean[] itemsondoor = new boolean[6];
    // Tournaments
    private List<SkillExperience> outlastSkillBackup = new ArrayList<>();
    public int magicBookBackup;
    // Collection log

    private CollectionLog viewingCollectionLog;
    private CollectionLog collectionLog = new CollectionLog();

    public List<GameItem> dropItems;
    public CollectionLog.CollectionTabType collectionLogTab;
    public int previousSelectedCell;
    public int previousSelectedTab;
    // Vote panel
    public long dropBoostStart;
    public boolean usedReferral;
    // Teleporting
    public int lastTeleportX;
    public int lastTeleportY;
    public int lastTeleportZ;

    public int teleGfx;
    public int teleGfxTime;
    public int teleGfxDelay;
    public int teleEndAnimation;
    public int teleHeight;
    public int teleSound;
    public int teleX;
    public int teleY;
    public int teleAction;
    private final Inventory inventory = new Inventory(this);
    // Config

    public boolean showingtimeplayed = false;
    public boolean debugMessage = Server.isDebug(); // On by default on debug mode
    public boolean barbarian;
    public boolean breakVials;
    public boolean collectCoins;
    private boolean runningToggled = true;
    // Trading post.
    public long lastTradingPostView;
    public boolean inSelecting;
    public boolean isListing;
    public int item;
    public int uneditItem;
    public int quantity;
    public int price;
    public int pageId = 1;
    public int searchId;
    public String lookup;
    public List<Integer> saleResults;
    public List<Integer> saleItems = Lists.newArrayList();
    public List<Integer> saleAmount = Lists.newArrayList();
    public List<Integer> salePrice = Lists.newArrayList();
    public int[] historyItems = new int[15];
    public int[] historyItemsN = new int[15];
    public int[] historyPrice = new int[15];
    private final RooftopAlkharid rooftopAlkharid = new RooftopAlkharid();
    private final RooftopFalador rooftopFalador = new RooftopFalador();
    private final RooftopDraynor rooftopDraynor = new RooftopDraynor();
    private final RooftopCanafis rooftopCanafis = new RooftopCanafis();
    private final RooftopPollnivneach rooftopPollnivneach = new RooftopPollnivneach();
    private final RooftopRellekka rooftopRellekka = new RooftopRellekka();
    private final BarbarianAgility barbarianAgility = new BarbarianAgility();
    // Boxes
    public int boxCurrentlyUsing;
    private final YoutubeMysteryBox youtubeMysteryBox = new YoutubeMysteryBox(this);
    private final UltraMysteryBox ultraMysteryBox = new UltraMysteryBox(this);
    private final NormalMysteryBox normalMysteryBox = new NormalMysteryBox(this);
    private final SuperMysteryBox superMysteryBox = new SuperMysteryBox(this);
    private final BlackAodLootChest blackAodLootChest = new BlackAodLootChest(this);
    private final FoeMysteryBox foeMysteryBox = new FoeMysteryBox(this);
    private final SlayerMysteryBox slayerMysteryBox = new SlayerMysteryBox(this);
    private final CoinBagSmall coinBagSmall = new CoinBagSmall(this);
    private final CoinBagEmpty coinBagEmpty = new CoinBagEmpty(this);
    private final ResourceBoxLarge resourceBox = new ResourceBoxLarge(this);
    private final CoinBagMedium coinBagMedium = new CoinBagMedium(this);
    private final CoinBagLarge coinBagLarge = new CoinBagLarge(this);
    private final CoinBagBuldging coinBagBuldging = new CoinBagBuldging(this);
    private final VoteMysteryBox voteMysteryBox = new VoteMysteryBox();
    private final PvmCasket pvmCasket = new PvmCasket();
    private final DailyGearBox dailyGearBox = new DailyGearBox(this);
    private final DailySkillBox dailySkillBox = new DailySkillBox(this);
    // Combat
    private final EntityDamageQueue entityDamageQueue = new EntityDamageQueue(this);
    private BountyHunter bountyHunter = new BountyHunter(this);
    private final Zulrah zulrah = new Zulrah(this);
    private Entity targeted;//wym?
    // Minigames
    private final MageArena mageArena = new MageArena(this);
    private final Genie genie = new Genie(this);
    private final PestControlRewards pestControlRewards = new PestControlRewards(this);
    private final WarriorsGuild warriorsGuild = new WarriorsGuild(this);

    private final WarriorsGuildbasement warriorsGuildbasement = new WarriorsGuildbasement(this);
    // Items
    private RunePouch runePouch = new RunePouch(this);
    private HerbSack herbSack = new HerbSack(this);
    private GemBag gemBag = new GemBag(this);
    private final RechargeItems rechargeItems = new RechargeItems(this);
    private LootingBag lootingBag = new LootingBag(this);


    public int[] degradableItem = new int[Degrade.getMaximumItems()];
    public boolean[] claimDegradableItem = new boolean[Degrade.getMaximumItems()];
    // Skilling
    private final ExpLock explock = new ExpLock(this);
    private final PrestigeSkills prestigeskills = new PrestigeSkills(this);
    private final Mining mining = new Mining(this);
    public Smelting.Bars bar;
    // Instances..
    private PlayerParty party = null;
    private final TobContainer tobContainer = new TobContainer(this);
    private final TombsOfAmascutContainer tombsOfAmascutContainer = new TombsOfAmascutContainer(this);
    private final Questing questing = new Questing(this);
    private final NotificationsTab notificationsTab = new NotificationsTab(this);
    private final DonationRewards donationRewards = new DonationRewards(this);
    private final WogwContributeInterface wogwContributeInterface = new WogwContributeInterface(this);
    private final Farming farming = new Farming(this);
    private final DailyRewards dailyRewards = new DailyRewards(this);
    private Cannon cannon;
    public final Stopwatch last_trap_layed = new Stopwatch();
    public List<Integer> dropInterfaceSearchList = new ArrayList<>();
    private final QuickPrayers quick = new QuickPrayers();
    private final QuestTab questTab = new QuestTab(this);
    private final EventCalendar eventCalendar = new EventCalendar(this);
    private final RandomEventInterface randomEventInterface = new RandomEventInterface(this);
    private final NPCDeathTracker npcDeathTracker = new NPCDeathTracker(this);
    private final BossTimers bossTimers = new BossTimers(this);
    private final UnnecessaryPacketDropper packetDropper = new UnnecessaryPacketDropper();
    private LocalDate lastVote = LocalDate.of(1970, 1, 1);
    private LocalDate lastVotePanelPoint = LocalDate.of(1970, 1, 1);
    private long lastContainerSearch;
    private AchievementHandler achievementHandler;
    private String macAddress;
    private String uuid = "";
    private final Duel duelSession = new Duel(this);
    private Player itemOnPlayer;
    private Killstreak killstreaks;
    private Mode mode = new RegularMode(ModeType.STANDARD);
    private ModeRevertType modeRevertType = ModeRevertType.STANDARD;
    private final ModeSelection modeSelection = new ModeSelection(this);
    private final Trade trade = new Trade(this);


    /**
     * Weight of the player
     */
    private double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public ItemAssistant itemAssistant = new ItemAssistant(this);
    private final ShopAssistant shopAssistant = new ShopAssistant(this);
    private final PlayerAssistant playerAssistant = new PlayerAssistant(this);
    private final CombatItems combatItems = new CombatItems(this);
    private final ActionHandler actionHandler = new ActionHandler(this);
    private final DialogueHandler dialogueHandler = new DialogueHandler(this);
    private final FriendsList friendsList = new FriendsList(this);
    private final Queue<Packet> queuedPackets = new ConcurrentLinkedQueue<>();
    private final Queue<Packet> priorityPackets = new ConcurrentLinkedQueue<>();
    private final Potions potions = new Potions(this);
    private final Food food = new Food(this);
    private final SkillInterfaces skillInterfaces = new SkillInterfaces(this);
    private final ChargeTrident chargeTrident = new ChargeTrident(this);
    private PlayerMovementState movementState = PlayerMovementState.getDefault();
    private Slayer slayer;
    private final AgilityHandler agilityHandler = new AgilityHandler();
    private final PointItems pointItems = new PointItems(this);
    private final GnomeAgility gnomeAgility = new GnomeAgility();
    private final WildernessAgility wildernessAgility = new WildernessAgility();
    private final Shortcuts shortcuts = new Shortcuts();
    private final Lighthouse lighthouse = new Lighthouse();
    private final Agility agility = new Agility(this);
    private final Prayer prayer = new Prayer(this);
    private final Smithing smith = new Smithing(this);
    private FightCave fightcave;
    private TokkulPit1 tokkulPit1;
    private final SmithingInterface smithInt = new SmithingInterface(this);
    private final Herblore herblore = new Herblore(this);
    private final Thieving thieving = new Thieving(this);
    private final Fletching fletching = new Fletching(this);
    private Godwars godwars = new Godwars(this);
    private final TreasureTrails trails = new TreasureTrails(this);
    private Optional<ItemCombination> currentCombination = Optional.empty();
    private List<God> equippedGodItems;
    private Titles titles = new Titles(this);

    private final TabHandling tabHandler = new TabHandling(this);
    public TabHandling getTabHandler() {
        return tabHandler;
    }

    // Consumable item timers
    private final TickTimer foodTimer = new TickTimer();
    private final TickTimer potionTimer = new TickTimer();

    /**
     * The {@link TickTimer} associated with combo eating
     */
    private final TickTimer comboTimer = new TickTimer();

    public Clan clan;
    private final CollectionBox collectionBox = new CollectionBox();

    private final PerduLostPropertyShop perduLostPropertyShop = new PerduLostPropertyShop();
    private final FlowerPoker flowerPoker = new FlowerPoker(this);


    /**
     * Actions queued from any thread.
     */
    private final Queue<Consumer<Player>> queuedActions = new ConcurrentLinkedQueue<>();
    private final Queue<Consumer<Player>> queuedLoginActions = new ArrayDeque<>();
    private final List<TickableContainer<Player>> tickables = new ArrayList<>();
    private TickableContainer<Player> tickable = null;
    public int diariesCompleted;
    // Combat vars
    public int underAttackByPlayer;
    public int underAttackByNpc;
    public int autoRet;
    public int specBarId;
    public int playerFollowingIndex;
    public int skullTimer;
    public int lastNpcAttacked;
    public int autocastId;
    public int followDistance;
    public int npcFollowingIndex;
    public int arrowUsedOnAttack = -1;

    public int killcount;
    public int deathcount;
    public int hydraAttackCount;
    public int waveId;
    public int waveId1;
    public int rfdWave;
    public int rfdChat;
    public int rfdGloves;
    public int fightCavesWaveType;
    public int fightCavesWaveType1;
    public int rfdRound;
    public int roundNpc;

    public int sireHits;
    public int slayerTasksCompleted;
    public int pestControlDamage;
    public int gwdAltarTimer;
    public int dreamSpellTimer;


    /*
     * battlepass
     */

    public int battlePage = 1;
    public int currentTier = 1;
    public int currentTierXP = 1;
    public boolean boughtBP = false;
    public int battlePassSeason = 1;

    public double specAmount = 10;
    public double prayerPoint = 1.0;
    // PvP Weapons

    /**
     * Manages PvP weapons for players
     */
    private PvpWeapons pvpWeapons = new PvpWeapons(this);

    /**
     * Manages the charges for the Tome of Fire
     */
    private TomeOfFire tomeOfFire = new TomeOfFire(this);


    private DepositBox depositBox = new DepositBox(this);
    public DepositBox getDepositBox() {
        return depositBox;
    }
    public int braceletEtherCount;
    public int crystalCharge;
    public int crystalStaff;
    public int elvenCharge;
    public int crystalBowArrowCount;
    // Skill
    public int unfPotHerb;
    public int unfPotAmount;
    public int smeltAmount;
    public int smeltEventId = 5567;
    public int smithingCounter;
    public int grimyHerbToClean;
    public int grimyHerbAmount;
    // Points
    public int pkp;
    public int bossPoints;
    public boolean bossPointsRefund;
    public int achievementPoints;
    public int raidPoints;

    public int raids3Points;
    public int tombsOfAmascutPoints;
    public int votePoints;
    public int bloodPoints;
    public int pcPoints;
    public int donatorPoints;
    public int loyaltyPoints;
    public int voteKeyPoints;
    // Interfaces
    public int lastClickedItem;
    public int unNoteItemId;
    public int dialogueId;
    public int dialogueOptions;
    public int nextChat;
    public int talkingNpc = -1;
    public int dialogueAction;
    public int xInterfaceId;
    public int xRemoveId;
    public int xRemoveSlot;
    public int enterAmountInterfaceId;
    public int safeBoxSlots = 28;
    public int lootValue;
    public int emoteCommandId;
    public int gfxCommandId;
    public int countUntilPoison;
    public int diceItem;
    public int dicePage;
    public int specRestore;
    public int petSummonId;
    public int yakLevel = 0;
    public int clickedNpcIndex;
    public int diceMin;
    public int diceMax;
    public int otherDiceId;
    public int playTime;
    public int yakplayTime;
    public int totalLevel;
    public int killStreak;
    public int xpMaxSkills;
    public int exchangePoints;
    public int totalEarnedExchangePoints;
    public int referallFlag;
    public int amDonated;
    public int showcase;
    public int streak;
    public int lastLoginDate;
    public int clanId = -1;
    // Timers
    public int wildLevel;
    public int teleTimer;
    public int respawnTimer;
    public int teleBlockLength;
    public int operateEquipmentItemId;
    public int antiqueItemResetSkillId;
    public int leatherType = -1;
    public int amountToCook;
    public int rangeEndGFX;
    public int recoilHits;
    public int slaughterCharge;
    public int freezeDelay;
    public int killerId;
    public int weaponUsedOnAttack;
    public int npcClickIndex;
    public int npcType;
    public int oldSpellId;
    private int spellId = -1;
    public int hitDelay;
    public int bowSpecShot;
    public int clickNpcType;
    public int clickObjectType;
    public int myShopId;
    public int tradeStatus;
    public int[] playerAppearance = new int[13];
    public int wearId;
    public int wearSlot;
    public int wearItemInterfaceId;
    public int npcId2;
    public int combatLevel;
    public int playerStandIndex = 808;
    public int playerTurnIndex = 823;
    public int playerWalkIndex = 819;
    public int playerTurn180Index = 820;
    public int playerTurn90CWIndex = 821;
    public int playerTurn90CCWIndex = 822;
    public int playerRunIndex = 824;
    public int destroyingItemId;
    public int itemX;
    public int itemY;
    public int itemId;
    public int objectId;
    public int objectX;
    public int objectY;
    public int objectXOffset;
    public int objectYOffset;
    public int objectDistance;

    public int tablet;
    public int wellItem = -1;
    public int wellItemPrice = -1;
    /**
     * Combat
     */
    public int graniteMaulSpecialCharges;
    private int chatTextColor;
    private int chatTextEffects;
    private int dragonfireShieldCharge;
    private int runEnergy = 10000;
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;
    private int privateChat;
    private int shayPoints;
    private int arenaPoints;
    private int toxicStaffOfTheDeadCharge;
    private int toxicBlowpipeCharge;
    private int toxicBlowpipeAmmo;
    private int toxicBlowpipeAmmoAmount;
    private int serpentineHelmCharge;
    private int tridentCharge;
    private int toxicTridentCharge;
    private int arcLightCharge;
    private int sangStaffCharge;

    private int accursedCharge;
    private int TumekensShadowCharge;
    public int getRunningDistanceTravelled() {
        return runningDistanceTravelled;
    }

    private int runningDistanceTravelled;
    private int openInterface;
    public static int playerCrafting = 12;
    public static int playerSmithing = 13;
    protected int numTravelBackSteps;
    protected AtomicInteger packetsReceived = new AtomicInteger();
    public AtomicInteger attemptedPackets = new AtomicInteger();
    /**
     * Arrays
     */
    public ArrayList<int[]> coordinates;
    public int[] masterClueRequirement = new int[4];
    public int[] waveInfo = new int[3];
    public int[] voidStatus = new int[5];
    public int[] playerStats = new int[8];
    public int[] playerBonus = new int[Bonus.values().length];
    public int[] playerEquipment = new int[14];
    public int[] playerEquipmentN = new int[14];
    public int[] playerLevel = new int[25];
    public int[] playerXP = new int[25];
    public long[] gained200mTime = new long[25];
    public int[] runeEssencePouch = new int[3];
    public int[] pureEssencePouch = new int[3];
    public int[] prestigeLevel = new int[25];
    public boolean[] skillLock = new boolean[25];

    // This is done really badly
    // When your grabbing an item here and comparing it, e.g. playerItems[5] == 4151, do playerItems[5] == 4151 + 1
    // You can also do playerItems[5] - 1 == 4151
    public int[] playerItems = new int[28];

    public int[] playerItemsN = new int[28];
    public int[] counters = new int[20]; // didnt work hmm
    public int[] gwdkc = new int[5];
    public int[] raidsDamageCounters = new int[16];

    public int[] raids3DamageCounters = new int[15];
    public boolean[] maxCape = new boolean[5];
    public int[][] playerSkillProp = new int[20][15];
    public boolean receivedStarter;
    public boolean combatFollowing;
    /**
     * Strings
     */
    public String CERBERUS_ATTACK_TYPE = "";
    public String forcedText = "null";
    public String connectedFrom = "";
    private String loginName;
    private String displayName;
    private long displayNameLong;
    public String playerPass;
    public String barType = "";
    public String playerTitle = "";
    public String rottenPotatoOption = "";
    private String lastClanChat = "";
    private String revertOption = "";
    private String konarSlayerLocation;
    public String lastTask = "";

    private String honourguard5SlayerLocation;

    /**
     * Booleans
     */
    public boolean[] playerSkilling = new boolean[20];
    public boolean[] clanWarRule = new boolean[10];
    public boolean teleportingToDistrict;
    public boolean usingGraniteCannonballs = false;
    public boolean morphed;
    public boolean isIdle;
    public boolean boneOnAltar;
    public boolean dropRateInKills = true;
    public ItemToDestroy destroyItem;
    public boolean acceptAid = true;
    public boolean settingUnnoteAmount;
    public boolean settingLootValue;
    public boolean didYouKnow = true;
    public boolean documentGraphic;
    public boolean isStuck;
    public boolean hasOverloadBoost;
    public boolean hasDivineCombatBoost;
    public boolean hasDivineRangeBoost;
    public boolean hasDivineMagicBoost;
    public boolean keepTitle;
    public boolean killTitle;
    public boolean settingMin;
    public boolean settingMax;
    public boolean settingBet;
    public boolean playerIsCrafting;
    public boolean viewingRunePouch;
    public boolean hasFollower;
    public boolean updateItems;
    public boolean claimedReward;
    public boolean craftDialogue;
    public boolean battlestaffDialogue;
    public boolean braceletDialogue;
    public boolean isAnimatedArmourSpawned;
    public boolean isSmelting;
    public boolean expLock;
    public boolean buyingX;
    public boolean leverClicked;
    public boolean isBanking = true;
    public boolean isCooking;
    public boolean initialized;
    private boolean forceLogout;
    public boolean disconnected;
    public boolean ruleAgreeButton;
    public boolean isActive;
    public boolean isOverloading;
    public boolean isSkulled;
    public boolean hasMultiSign;
    public boolean saveCharacter;
    public boolean mouseButton;
    public boolean splitChat;
    public boolean chatEffects = true;
    public boolean shiftDrop = true;
    public boolean autocasting;
    public boolean autocastingDefensive;
    public boolean dbowSpec;
    public boolean properLogout;
    private boolean destructed;
    public boolean vengOn;
    public boolean completedTutorial;
    public boolean accountFlagged;
    public boolean doricOption;
    public boolean doricOption2;
    public boolean caOption2;
    public boolean caOption2a;
    public boolean caOption4a;
    public boolean caOption4c;
    public boolean caPlayerTalk1;
    public boolean rfdOption;
    public boolean spawned;
    public boolean hasBankpin;
    public boolean appearanceUpdateRequired = true;
    public boolean canChangeAppearance;
    public boolean isFullBody;
    public boolean isFullHelm;
    public boolean isFullMask;
    public boolean isOperate;
    public boolean usingLamp;
    public boolean normalLamp;
    public boolean antiqueLamp;
    public boolean setPin;
    public boolean teleporting;
    public boolean isWc;
    public boolean multiAttacking;
    public boolean rangeEndGFXHeight;
    public boolean playerIsFiremaking;
    public boolean stopPlayerSkill;
    public boolean stopPlayerPacket;
    public boolean ignoreDefence;
    public boolean usingArrows;
    public boolean usingOtherRangeWeapons;
    public boolean usingCross;
    public boolean usingBallista;
    public boolean spellSwap;
    public boolean protectItem;
    public boolean usingSpecial;
    public boolean usingRangeWeapon;
    public boolean usingBow;
    public boolean usingMagic;
    public boolean usingClickCast;
    public boolean usingMelee;
    public boolean isMoving;
    public boolean walkingToItem;
    public boolean isShopping;
    public boolean updateShop;
    public boolean forcedChatUpdateRequired;
    public boolean inDuel;
    public boolean inTrade;
    public boolean tradeResetNeeded;
    public boolean smeltInterface;
    public boolean usingGlory;
    public boolean usingSkills;
    public boolean fishing;
    public boolean takeAsNote;
    public boolean swaping;
    public boolean isNpc;
    public boolean inPits;
    public boolean didTeleport;
    public boolean mapRegionDidChange;
    public boolean inArdiCC;
    public boolean attackSkill;
    public boolean strengthSkill;
    public boolean defenceSkill;
    public boolean mageSkill;
    public boolean rangeSkill;
    public boolean prayerSkill;
    public boolean healthSkill;
    public boolean pkDistrict;
    public boolean crystalDrop;
    public boolean hourlyBoxToggle = true;
    public boolean fracturedCrystalToggle = true;
    public boolean boltTips;
    public boolean arrowTips;
    public boolean spectatingTournament;
    public boolean javelinHeads;
    public boolean hasPotionBoost;
    public boolean canHarvestAvatarOfCreation = false;
    public boolean canLeaveAvatarOfCreation = false;
    public boolean canEnterAvatarOfCreation;
    private boolean dropWarning = true;
    private boolean alchWarning = true;
    private boolean chatTextUpdateRequired;
    private boolean newWalkCmdIsRunning;
    private boolean forceMovement;
    private boolean godmode;
    private boolean safemode;

    public boolean Coughing = false;

    public boolean FrozenKey = false;
    public long nexCoughDelay;
    public long nexVirusTimer;
    public boolean hasNexVirus = false;
    private boolean forceMovementActive;
    public boolean insidePost;

    /**
     * @return the forceMovement
     */
    public boolean isForceMovementActive() {
        return forceMovementActive;
    }

    protected boolean faceUpdateRequired;
    private final AchievementDiaryManager diaryManager = new AchievementDiaryManager(this);
    public long totalGorillaDamage;
    public long totalMissedGorillaHits;
    public long totalHunllefDamage;
    public long totalMissedHunllefHits;
    public long lastImpling;
    public long lastWheatPass;
    public long lastCloseOfInterface;
    public long lastPerformedEmote;
    public long lastPickup;
    public long lastTeleport;
    public long lastMarkDropped;
    public long lastObstacleFail;
    public long lastDropTableSelected;
    public long lastDropTableSearch;
    public long buySlayerTimer;
    public long buyPestControlTimer;
    public long fightCaveLeaveTimer;
    public long fightLeaveTimer;
    public long infernoLeaveTimer;
    public long lastFire;
    public long lastMove;
    public long bonusXpTime;
    public long lastSmelt;
    public long lastMysteryBox;
    public long lastYell;
    public long diceDelay;
    public long lastPlant;
    public long lastCast;
    public long miscTimer;
    public long jailEnd;
    public long muteEnd;
    public long stopPrayerDelay;
    public long lastAntifirePotion;
    public long antifireDelay;
    public long staminaDelay;
    public long lastHeart;
    public long openCasketTimer;
    public long lastSpear = System.currentTimeMillis();
    public long lastProtItem = System.currentTimeMillis();
    public long lastVeng = System.currentTimeMillis();
    public long switchDelay = System.currentTimeMillis();
    public long potDelay = System.currentTimeMillis();
    public long protMageDelay = System.currentTimeMillis();
    public long protMeleeDelay = System.currentTimeMillis();
    public long protRangeDelay = System.currentTimeMillis();
    public long lastLockPick = System.currentTimeMillis();
    public long alchDelay = System.currentTimeMillis();
    public long specDelay = System.currentTimeMillis();
    public long teleBlockStartMillis;
    public long godSpellDelay = System.currentTimeMillis();
    public long singleCombatDelay = System.currentTimeMillis();
    public long singleCombatDelay2 = System.currentTimeMillis();
    public long reduceStat = System.currentTimeMillis();
    public long homeTeleportDelay = System.currentTimeMillis();
    public int homeTeleportLength;

    public long randomeventDelay = System.currentTimeMillis();
    public int randomeventLength;



    public long teleinterfaceDelay = System.currentTimeMillis();
    public int teleinterfaceLength;

    public long infrunenergyDelay = System.currentTimeMillis();
    public int infrunenergyLength;


    public int timer;
    public long logoutDelay = System.currentTimeMillis();
    public long cerbDelay = System.currentTimeMillis();
    public long chestDelay = System.currentTimeMillis();
    private long revertModeDelay;
    private long experienceCounter;
    private long bestZulrahTime;
    private long lastOverloadBoost;
    private long nameAsLong;
    private long lastDragonfireShieldAttack;
    public long clickDelay;
    public long lastHealChest = System.currentTimeMillis();
    public long healchesttime = 0L;
    public boolean hasPetSpawned;
    private boolean receivedVoteStreakRefund; // TODO DELETE ME AFTER September 29th 2021
    /**
     * The amount of time before we are out of combat.
     */
    private long inCombatDelay = Configuration.IN_COMBAT_TIMER;

    public void setInCombatDelay(long inCombatDelay) {
        this.inCombatDelay = inCombatDelay;
    }

    /**
     * Others
     */
    public ArrayList<String> lastConnectedFrom = new ArrayList<>();
    public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();

    @Override
    public String toString() {
        return String.format("player[loginName=%s, displayName=%s, ip=%s, mac=%s, uuid=%s]", getLoginName(),
                getDisplayName(), getIpAddress(), getMacAddress(), getUUID());
    }

    public String getNamesDescription() {
        return String.format("[login=%s, display=%s]", getLoginName(), getDisplayName());
    }

    /**
     * Gets a description of a player including their state.
     * Append to every logged error. Needs expanded to including other states.
     *
     * @return player state description string.
     */
    public String getStateDescription() {
        return String.format("[loginName=%s, displayName=%s, position=%s]", getLoginName(), getDisplayName(), getPosition());
    }

    public Position getPosition() {
        return new Position(absX, absY, heightLevel);
    }

    public boolean isManagement() {
        return getRights().isOrInherits(Right.ADMINISTRATOR, Right.OWNER);
    }

    private boolean bot = false;

    public boolean isBot() {
        return bot;
    }

    public static Player createBot(String username, Right right) {
        return createBot(username, right, Configuration.START_POSITION);
    }

    public static Player createBot(String username, Right right, Position position) {
        username = username.toLowerCase();
        Player player = new Player(null);
        player.getRights().setPrimary(right);
        player.setMode(right.getMode());
        player.saveCharacter = true;
        player.completedTutorial = true;
        player.setLoginName(username);
        player.setDisplayName(player.getLoginName());
        player.macAddress = "";
        player.bot = true;
        player.nameAsLong = Misc.playerNameToInt64(username);
        player.playerPass = "playerbot123";
        player.setIpAddress("");
        player.addQueuedAction(plr -> plr.moveTo(position));

        Server.getIoExecutorService().submit(() -> {
            try {
                LoginReturnCode code = RS2LoginProtocol.loadPlayer(player, player.getLoginNameLower(), LoginReturnCode.SUCCESS, true);
                if (code != LoginReturnCode.SUCCESS) {
                    logger.error("Could not login bot, return code was {}", code);
                    return;
                }

                PlayerHandler.addLoginQueue(player);
                if (Server.isDebug()) { // This is so bots get packet writing for profiling on debug
                    player.outStream = new Stream(new byte[Configuration.BUFFER_SIZE]);
                    player.outStream.currentOffset = 0;
                    player.inStream = new Stream(new byte[Configuration.BUFFER_SIZE]);
                    player.inStream.currentOffset = 0;
                    player.outStream.packetEncryption = new ISAACCipher(new int[]{0, 0, 0, 0});
                    player.inStream.packetEncryption = new ISAACCipher(new int[]{0, 0, 0, 0});
                }
            } catch (Exception e) {
                logger.error("Error loading bot {}", player, e);
            }
        });

        return player;
    }

    /**
     * what happens when a new player logins in for the first time
     * @param channel
     */
    public Player(Channel channel) {
        this.session = channel;
        freezeTimer = -6;
        rights = new RightGroup(this, Right.PLAYER);
        for (int i = 0; i < playerItems.length; i++) {
            playerItems[i] = 0;
        }
        for (int i = 0; i < playerItemsN.length; i++) {
            playerItemsN[i] = 0;
        }
        resetSkills();
        //   setMovementState(new PlayerMovementStateBuilder().setLocked(true).createPlayerMovementState());
        //  ChangeAppearance.generateRandomAppearance(this);

        playerEquipment[playerHat] = -1;
        playerEquipment[playerCape] = -1;
        playerEquipment[playerAmulet] = -1;
        playerEquipment[playerChest] = -1;
        playerEquipment[playerShield] = -1;
        playerEquipment[playerLegs] = -1;
        playerEquipment[playerHands] = -1;
        playerEquipment[playerFeet] = -1;
        playerEquipment[playerRing] = -1;
        playerEquipment[playerArrows] = -1;
        playerEquipment[playerWeapon] = -1;
        heightLevel = 0;

        setTeleportToX(Configuration.START_LOCATION_X);
        setTeleportToY(Configuration.START_LOCATION_Y);
        absX = absY = -1;
        mapRegionX = mapRegionY = -1;
        currentX = currentY = 0;
        resetWalkingQueue();
        if (channel != null) {
            outStream = new Stream(new byte[Configuration.BUFFER_SIZE]);
            outStream.currentOffset = 0;
            inStream = new Stream(new byte[Configuration.BUFFER_SIZE]);
            inStream.currentOffset = 0;
        }
    }

    public PlayerAddresses getValidAddresses() {
        String ip = getIpAddress();
        String mac = null;
        String uuid = null;
        if (getMacAddress() != null && getMacAddress().length() > 0 && !getMacAddress().equals(getIpAddress()))
            mac = getMacAddress();
        if (getUUID() != null && getUUID().length() > 0)
            uuid = getUUID();
        return new PlayerAddresses(ip, mac, uuid);
    }

    /**
     * Reset skills to default.
     */
    public void resetSkills() {
        for (int i = 0; i < playerLevel.length; i++) {
            if (i == 3) {
                playerLevel[i] = 10;
            } else {
                playerLevel[i] = 1;
            }
        }
        for (int i = 0; i < playerXP.length; i++) {
            if (i == 3) {
                playerXP[i] = 1300;
            } else {
                playerXP[i] = 0;
            }
        }
    }

    /**
     * Actions to take when a player's mac/uuid address has a change detected.
     */
    public void setAddressChanged(String type, String previous, String current, boolean staffAlertMessage) {
        addQueuedLoginAction(plr -> {
            String message = Misc.replaceBracketsWithArguments("{} changed {} address", getNamesDescription(), type);
            if (staffAlertMessage) {
                Discord.writeAddressSwapMessage(message);
            } else {
                DiscordMessager.sendLogs(message);
            }

            Server.getLogging().write(new ChangeAddressLog(this, type, previous, current));

            if (plr.getBankPin().hasBankPin()) {
                setRequiresPinUnlock(true);
                plr.sendMessage("@dre@You're logging in from a different computer, please enter your account pin.");
            }
        });
    }

    public void lock(PlayerLock lock) {
        this.lock = lock;
        debug("Locked: " + lock.getClass().getName());
    }

    public void unlock() {
        lock = new Unlocked();
        debug("Unlocked.");
    }

    public PlayerLock getLock() {
        return lock;
    }

    /**
     * Check if the player has hit the database access rate limit. If not it will set the database access time.
     * @return true if server should refuse access, false and set access time if they aren't at limit.
     */
    public boolean hitDatabaseRateLimit(boolean message) {
        if (System.currentTimeMillis() - lastDatabaseAccess <= 3_000) {
            if (message) sendMessage("You are doing that too often, please wait.");
            return true;
        }
        lastDatabaseAccess = System.currentTimeMillis();
        return false;
    }

    public boolean isApartOfGroupIronmanGroup() {
        return GroupIronmanRepository.getGroupForOnline(this).isPresent();
    }

    public boolean isInIronmanGroupWith(final Player other) {
        var group = GroupIronmanRepository.getGroupForOnline(this);
        if (group.isEmpty()) {
            return false;
        }
        return group.get().getOnline().contains(other);
    }

    public long getTotalXp() {
        long temp = 0;
        for (int i = 0; i < playerXP.length; i++) {
            temp += playerXP[i];
        }
        return temp;
    }

    public boolean viewable(NPC npc, boolean updatingLocalList) {
        if (updatingLocalList) { // Only check against these when updating local npc list, not when adding to local list
            if (npc.teleporting) {
                return false;
            }
        }

        return withinDistance(npc) && !npc.isInvisible() && npc.getInstance() == getInstance()
                && !npc.needRespawn && npc.getIndex() > 0;
    }

    /**
     * Check if this player is on the computer with the specified parameters
     * (or has the same name so is the same player).
     */
    public boolean isSameComputer(long usernameAsLong, String ipAddress, String macAddress) {
        if (Server.isDebug()) {
            logger.debug("Skipping Player#isSameComputer addresses check, only checking usernames.");
            return usernameAsLong == getNameAsLong();
        }
        return usernameAsLong == getNameAsLong() || ipAddress.equals(getIpAddress()) || macAddress.equals(getMacAddress());
    }

    public boolean isSameComputer(Player other) {
        return isSameComputer(other.getNameAsLong(), other.getIpAddress(), other.getMacAddress());
    }

    public boolean ignoreNewPlayerRestriction() {
        return getRights().ignoreNewPlayerRestriction();
    }

    public boolean ignoreNewPlayerRestriction(Player other) {
        return getRights().ignoreNewPlayerRestriction() || other.getRights().ignoreNewPlayerRestriction()
                || getMode().isGroupIronman() || other.getMode().isGroupIronman();
    }

    public boolean hasNewPlayerRestriction() {
        if (Server.isDebug()) return false;
        return !ignoreNewPlayerRestriction() && playTime < Configuration.NEW_PLAYER_RESTRICT_TIME_TICKS;
    }

    /**
     * Check if a player is busy (interface open, etc).
     *
     * @return <code>true</code> if the player is busy and shouldn't be disturbed.
     */
    public boolean isBusy() {
        return openInterface > 0;
    }

    public Bank getBank() {
        if (bank == null) bank = new Bank(this);
        return bank;
    }

    private BankPin pin;
    private boolean requiresPinUnlock;

    public BankPin getBankPin() {
        if (pin == null) pin = new BankPin(this);
        return pin;
    }

    public void sendMessage(String s, int color) {
        sendMessage("<col=" + color + ">" + s + "</col>");
    }

    public int tournamentWins, tournamentPoints, outlastKills, outlastDeaths, tournamentTotalGames;
    public Player tournamentTarget;

    public long tournamentTargetCooldown;

    public void resetVengeance() {
        vengOn = false;
        lastCast = System.currentTimeMillis();
    }

    public void flushOutStream() {
        if (!initialized || session == null || !session.isActive() || outStream == null || outStream.currentOffset == 0)
            return;
        byte[] temp = new byte[outStream.currentOffset];
        System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
        Packet packet = new Packet(-1, Packet.Type.FIXED, Unpooled.wrappedBuffer(temp));
        session.writeAndFlush(packet);
        getOutStream().currentOffset = 0;
    }

    public void setLoadedController(Controller loadedController) {
        this.loadedController = loadedController;
    }

    private void loadController() {
        if (loadedController != null && loadedController.inBoundaryOrNoBoundaries(this)) {
            setController(loadedController);
        } else {
            setController(ControllerRepository.getOrDefault(this));
        }
        loadedController = null;
    }

    public void setController(Controller controller) {
        if (this.controller != null)
            this.controller.removed(this);
        this.controller = controller;
        controller.added(this);
        if (!isBot())
            logger.debug("Set controller to {}, {}", controller.getKey(), controller);
    }

    public Controller getController() {
        return controller;
    }

    /**
     * If the current controller allows switching automatically
     * {@link Controller#allowSwitch()}, then we will check all boundary
     * controllers and set the controller is a new one is returned.
     */
    public void updateController() {
        if (getController().allowSwitch()) {
            Controller newController = ControllerRepository.getOrDefault(this);
            if (newController != controller) {
                setController(newController);
            }
        }
    }

    public int getSpellId() {
        return spellId;
    }

    public boolean usingGodSpell() {
        return oldSpellId >= 28 && oldSpellId <= 30;
    }

    public boolean hasUnlockedGodSpell(int spell) {
        if (spell == 28)
            return saradominStrikeCasts >= 100;
        else if (spell == 29)
            return clawsOfGuthixCasts >= 100;
        else if (spell == 30)
            return flamesOfZamorakCasts >= 100;
        return true;
    }

    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    public int getCombatLevelDifference(Player other) {
        return Math.abs(calculateCombatLevel() - other.calculateCombatLevel());
    }

    public void moveTo(Position position) {
        stopMovement();
        this.teleportToX = position.getX();
        this.teleportToY = position.getY();
        this.heightLevel = position.getHeight();
    }

    public void climbLadderTo(Position position) {
        climbLadderTo(position, null);
    }

    public void climbLadderTo(Position position, Consumer<Player> finishConsumer) {
        startAnimation(position.getHeight() < getHeight() ? 827 : 828);
        setTickable((container, player) -> {
            if (container.getTicks() == 1) {
                container.stop();
                player.moveTo(position);
                if (finishConsumer != null) {
                    finishConsumer.accept(this);
                }
            }
        });
    }

    public int getTeleportToX() {
        return teleportToX;
    }

    public void setTeleportToX(int teleportToX) {
        this.teleportToX = teleportToX;
    }

    public int getTeleportToY() {
        return teleportToY;
    }

    public void setTeleportToY(int teleportToY) {
        this.teleportToY = teleportToY;
    }

    public boolean protectingRange() {
        return this.prayerActive[17] || this.prayerActive[CombatPrayer.DEFLECT_MISSILES];
    }

    public boolean protectingMagic() {
        return this.prayerActive[16] || this.prayerActive[CombatPrayer.DEFLECT_MAGIC];
    }

    public boolean protectingMelee() {
        return this.prayerActive[18] || this.prayerActive[CombatPrayer.DEFLECT_MELEE];
    }
    public boolean isProtectionPrayersShiftRight() {
        return protectionPrayersShiftRight;
    }

    public void setProtectionPrayersShiftRight(boolean protectionPrayersShiftRight) {
        this.protectionPrayersShiftRight = protectionPrayersShiftRight;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public boolean hunllefDead;
    public int VERIFICATION;

    public void TournamentHiscores(Player c) {
        c.getDH().sendDialogues(983, 311);
    }

    public long disconnectTime;

    public void start(DialogueBuilder dialogueBuilder) {
        this.dialogueBuilder = dialogueBuilder;
        dialogueBuilder.initialise();
        lastDialogueNewSystem = true;
    }

    public boolean canUseGodSpellsOutsideOfMageArena() {
        return this.flamesOfZamorakCasts >= 100 && this.clawsOfGuthixCasts >= 100 && this.saradominStrikeCasts >= 100;
    }

    public void openedInterface(int interfaceId) {
        setOpenInterface(interfaceId);
    }

    public void closedInterface() {
        setOpenInterface(0);
    }

    public boolean isInterfaceOpen(int interfaceId) {
        return getOpenInterface() == interfaceId;
    }

    public void attemptLogout() {
        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this, MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
            if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERATION) {
                sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
                sendMessage("lose all of your staked items to your opponent.");
            }
        }
        if (!isIdle && underAttackByNpc > 0) {
            sendMessage("You can\'t log out until 10 seconds after the end of combat.");
            return;
        }
        if (underAttackByPlayer > 0) {
            sendMessage("You can\'t log out until 10 seconds after the end of combat.");
            return;
        }

        if (getLock().cannotLogout(this)) {
            sendMessage("You can't logout at the moment.");
            return;
        }

        if (!isDisconnected() && System.currentTimeMillis() - logoutDelay > 1000) {
         //   if(!getRights().contains(Right.OWNER) && combatLevel >= 10)
                //new Hiscores(this).run();
            PriceChecker.clearConfig(this);
            lastLoginDate = getLastLogin();
            properLogout = true;
            setDisconnected(true);
            ConnectedFrom.addConnectedFrom(this, connectedFrom);
        }
    }
    public int getLastLogin() {
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return (year * 10000) + (month * 100) + day;
    }

    /**
     * Force the player to immediately disconnect from the game.
     */
    public void forceLogout() {
        forceLogout = true;
    }


    public void setDisconnected() {
        setDisconnected(true);
        disconnectTime = System.currentTimeMillis();
    }

    public boolean isReadyToLogout() {
        if (forceLogout)
            return true;
        if (getLock().cannotLogout(this))
            return false;
        return properLogout
                || isDisconnected() && System.currentTimeMillis() - logoutDelay > 10000
                || isDisconnected() && disconnectTime != 0 && (System.currentTimeMillis() - disconnectTime >= 30000)
                || !bot && System.currentTimeMillis() - lastPacketReceived > 30000;
    }

    public void destruct() {
        if (destructed)
            return;
        destructed = true;
        getPA().sendLogout();

        if (session != null && session.isOpen()) {
            session.writeAndFlush(new PacketBuilder(109).toPacket()).addListener(ChannelFutureListener.CLOSE);
            session = null;
        }

        if (party != null) {
            party.remove(this);
        }
        if (getPA().viewingOtherBank) {
            getPA().resetOtherBank();
        }
        if (this.clan != null) {
            this.clan.removeMember(this);
        }

        getFriendsList().onLogout();
        GroupIronmanRepository.onLogout(this);

        getController().onLogout(this);

        clearUpPlayerNPCsForLogout();

        declineTrades();

        if (Configuration.BOUNTY_HUNTER_ACTIVE) {
            if (getBH().hasTarget()) {
                getBH().setWarnings(getBH().getWarnings() + 1);
            }
        }

        potions.resetOverload();
        potions.resetRangeDivine();
        potions.resetMageDivine();
        potions.resetCombatDivine();
        if (getCannon() != null) {
            getCannon().pickup(this, false);
        }
        if (combatLevel >= 100) {
            if (Highpkarena.getState(this) != null) {
                Highpkarena.removePlayer(this, true);
            }
        } else if (combatLevel >= 80 && combatLevel <= 99) {
            if (Lowpkarena.getState(this) != null) {
                Lowpkarena.removePlayer(this, true);
            }
        }
        Hunter.abandon(this, null, true);

        if (getXeric() != null) {
            getXeric().removePlayer(this);
        }
        if (Boundary.isIn(this, Boundary.XERIC_LOBBY)) {
            XericLobby.removePlayer(this);
        }
        if (getRaidsInstance() != null) {
            getRaidsInstance().logout(this);
        }
        if (getRaids3Instance() != null) {
            getRaids3Instance().logout(this);
        }
        if (Vorkath.inVorkath(this)) {
            this.getPA().movePlayer(2272, 4052, 0);
        }
        if (Vorkath.inVorkath(this)) {
            getPA().movePlayer(2272, 4052, 0);
        }
        if (getPA().viewingOtherBank) {
            getPA().resetOtherBank();
        }
        if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
            PestControl.removeGameMember(this);
        }
        if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
            PestControl.removeFromLobby(this);
        }
        Server.getMultiplayerSessionListener().removeOldRequests(this);
        Server.getEventHandler().stop(this);
        CycleEventHandler.getSingleton().stopEvents(this);

        if (!getRights().contains(Right.OWNER)) {
            Server.getDatabaseManager().batch(Server.getConfiguration().getHiscoresDatabase(), new HiscoresUpdateQuery(this));
            Server.getDatabaseManager().batch(new OutlastLeaderboardAdd(new OutlastLeaderboardEntry(this)));
        }
        /*
        Highscores by the plateau
         */
        boolean debugMessage = false;


        removeFromInstance();
        if (clan != null) {
            clan.removeMember(this);
        }
        inStream = null;
        //outStream = null;
        playerListSize = 0;
        npcListSize = 0;
        for (int i = 0; i < maxPlayerListSize; i++) playerList[i] = null;
        for (int i = 0; i < maxNPCListSize; i++) npcList[i] = null;
        if (Server.isTest() && !isBot()) {
            logger.info(Misc.formatPlayerName(getLoginName()) + " is logging out..");
        }
    }

    public void declineTrades() {
        if (Server.getMultiplayerSessionListener().inSession(this, MultiplayerSessionType.TRADE)) {
            Server.getMultiplayerSessionListener().finish(this, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
        }

        if (Server.getMultiplayerSessionListener().inSession(this, MultiplayerSessionType.FLOWER_POKER)) {
            Server.getMultiplayerSessionListener().finish(this, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
        }

        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this, MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
            if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
                duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            } else {
                Player winner = duelSession.getOther(this);
                duelSession.setWinner(winner);
                duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
            }
        }
    }

    public boolean isOnline() {
        return getIndex() > 0;
    }

    public void finishLogin() {

       // Server.getLogging().write(new ConnectionLog(this, true, null));
        queuedLoginActions.forEach(it -> it.accept(this));

        if (getMode() == Mode.forType(ModeType.STANDARD) && getRights().isIronman()) {
            System.err.println(getLoginName() + " has ironman rights with standard mode, removing ironman rights.");
            Right.IRONMAN_SET.forEach(it -> getRights().remove(it));
            getRights().updatePrimary();
        }

        // Normalise death tracker to fix previous mapping errors
        getNpcDeathTracker().normalise();
        // Old equipment correction?
        for (int i = 0; i < playerEquipment.length; i++) {
            if (playerEquipment[i] == 0) {
                playerEquipment[i] = -1;
                playerEquipmentN[i] = 0;
            }
        }
        if (getOutStream() != null) {
            getOutStream().createFrame(249);
            getOutStream().writeByteA(1);
            getOutStream().writeWordBigEndianA(getIndex());
        }

        loadController();
        getController().onLogin(this);

        if (PlayerHandler.updateRunning)
            getPA().sendUpdateTimer();

        getHealth().requestUpdate();
        GroupIronmanRepository.onLogin(this);
        getFriendsList().onLogin();
        CompletionistCape.onLogin(this);
        getAchievements().onLogin();

        getDiaryManager().setDiariesCompleted();
        graceSum();
        setStopPlayer(false);
        inPresets = false;
        inTradingPost = false;
        inBank = false;
        inLamp = false;
        inDonatorBox = false;
        getSuperMysteryBox().canMysteryBox();
        getBlackAodLootChest().canMysteryBox();
        getNormalMysteryBox().canMysteryBox();
        getUltraMysteryBox().canMysteryBox();
        getYoutubeMysteryBox().canMysteryBox();
        getFoeMysteryBox().canMysteryBox();
        getSlayerMysteryBox().canMysteryBox();
        getResourceBoxLarge().canMysteryBox();
        isFullHelm = ItemDef.forId(playerEquipment[playerHat]).getEquipmentModelType() == EquipmentModelType.FULL_HELMET;
        isFullMask = ItemDef.forId(playerEquipment[playerHat]).getEquipmentModelType() == EquipmentModelType.FULL_MASK;
        isFullBody = ItemDef.forId(playerEquipment[playerChest]).getEquipmentModelType() == EquipmentModelType.FULL_BODY;

        getPA().setConfig(427, acceptAid ? 1 : 0);//way better than saving shit client side imo.
        potions.resetOverload();

        if (completedTutorial) {
            sendMessage("@bla@Welcome back to " + Configuration.SERVER_NAME+".");
        } else {
            sendMessage("@bla@Welcome to " + Configuration.SERVER_NAME + ", don't forget to join the <col=255>::discord</col>!");
        }

        if (Reclaim.isReclaimPeriod() && Server.isPublic()) {
            sendMessage("The donation reclaim period is open, use ::reclaim to reclaim old donations.");
        }

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            sendMessage("@gre@It's Bonus XP Weekend!");
        }
        if (bonusXpTime > 0) {
            getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, VotePanelManager.getBonusXPTimeInMinutes(this));
        }
        if (xpScrollTicks > 0) {
            getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, (int) (xpScrollTicks / 100));
        }
        if (ddScrollTicks > 0) {
            getPA().sendGameTimer(ClientGameTimer.DOUBLES_DROPS, TimeUnit.MINUTES, (int) (xpScrollTicks / 100));
        }
        if (skillingPetRateTicks > 0) {
            getPA().sendGameTimer(ClientGameTimer.BONUS_SKILLING_PET_RATE, TimeUnit.MINUTES, (int) (skillingPetRateTicks / 100));
        }

        if (fasterCluesTicks > 0) {
            getPA().sendGameTimer(ClientGameTimer.BONUS_CLUES, TimeUnit.MINUTES, (int) (fasterCluesTicks / 100));
        }
        if (combatLevel >= 126) {
            getEventCalendar().progress(EventChallenge.HAVE_126_COMBAT);
        }

        if (getSlayer().superiorSpawned) {
            getSlayer().superiorSpawned = false;
        }
        if (getMode().isIronmanType()) {
            ArrayList<RankUpgrade> orderedList = new ArrayList<>(Arrays.asList(RankUpgrade.values()));
            orderedList.sort((one, two) -> Integer.compare(two.amount, one.amount));
            orderedList.stream().filter(r -> amDonated >= r.amount).findFirst().ifPresent(rank -> {
                RightGroup rights = getRights();
                Right right = rank.rights;
                if (!rights.contains(right)) {
                    sendMessage("@blu@Congratulations, your rank has been upgraded to " + right.toString() + ".");
                    sendMessage("@blu@This rank is hidden, but you will have all it\'s perks.");
                    rights.add(right);
                }
            });
        }
        combatLevel = calculateCombatLevel();
        for (int p = 0; p < CombatPrayer.PRAYER_NAME.length; p++) {
            // reset prayer glows
            prayerActive[p] = false;
            getPA().sendConfig(CombatPrayer.PRAYER_GLOW[p], 0);
        }
        getPA().sendString(99682,"0%");//just the visuals lmao...
        getPA().sendString(99683,"0%");//just the visuals lmao...
        getPA().sendString(99684,"0%");//just the visuals lmao...
        getPA().sendString(99685,"0%");//just the visuals lmao...
        getPA().sendString(99686,"0%");//just the visuals lmao...

        accountFlagged = getPA().checkForFlags();
        getPA().sendConfig(108, 0);
        getPA().sendConfig(172, 1);
        getPA().resetScreenShake(); // reset screen
       // PollTab.updatePollTabDisplay(this);
        //PollTab.clicktab(this);
        setSidebarInterface(0, 2423);
        setSidebarInterface(1, 13917); // Skilltab was 3917
        setSidebarInterface(2, 10220);//quest tab
        setSidebarInterface(3, 3213);
        setSidebarInterface(4, 1644);



        setSidebarInterface(5, usingcurseprayers ? 27674 : 15608);
        switch (playerMagicBook) {
            case 0:
                setSidebarInterface(6, 938); // modern
                break;
            case 1:
                setSidebarInterface(6, 838); // ancient
                break;
            case 2:
                setSidebarInterface(6, 29999); // ancient
                break;
            case 3:
                setSidebarInterface(6, 23100); // arceeus
                break;
        }
        if (hasFollower) {
            if (petSummonId > 0) {
                PetHandler.Pets pet = PetHandler.forItem(petSummonId);
                if (pet != null) {
                    PetHandler.spawn(this, pet, true, false);
                }
            }
        }
        for (int m = 0; m < activeMageArena2BossId.length; m++) {
            activeMageArena2BossId[m] = 0;
        }

        if (mageArena2Spawns == null)
            MageArenaII.assignSpawns(this);

        if (splitChat) {
            getPA().sendConfig(502, 1);
            getPA().sendConfig(287, 1);
        }
        setSidebarInterface(7, 18128);
        setSidebarInterface(8, 50650);//was 5065
        setSidebarInterface(9, 51500);//used to be the ignore tab
        setSidebarInterface(10, 2449);
        setSidebarInterface(11, 42500); // wrench tab
        setSidebarInterface(12, 147); // run tab
        setSidebarInterface(13, 962); // music tab
        getPA().showOption(4, 0, "Follow");
        getPA().showOption(5, 0, "Trade with");
        getItems().sendInventoryInterface(3214);
        getItems().setEquipment(playerEquipment[playerHat], 1, playerHat, false);
        getItems().setEquipment(playerEquipment[playerCape], 1, playerCape, false);
        getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet, false);
        getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows, false);
        getItems().setEquipment(playerEquipment[playerChest], 1, playerChest, false);
        getItems().setEquipment(playerEquipment[playerShield], 1, playerShield, false);
        getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs, false);
        getItems().setEquipment(playerEquipment[playerHands], 1, playerHands, false);
        getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet, false);
        getItems().setEquipment(playerEquipment[playerRing], 1, playerRing, false);
        getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon, false);
        getItems().calculateBonuses();
        getItems().sendEquipmentContainer();
        MeleeData.setWeaponAnimations(this);
        getItems().sendEquipmentContainer();
        if (getPrivateChat() > 2) {
            setPrivateChat(0);
        }
        if (getOutStream() != null) {
            getOutStream().createFrame(221);
            getOutStream().writeByte(2);
            getOutStream().createFrame(206);
            getOutStream().writeByte(0);
            getOutStream().writeByte(getPrivateChat());
            getOutStream().writeByte(0);
        }
        getFarming().handleLogin();
       // getQuestTab().openTab(QuestTab.Tab.INFORMATION);
        getItems().addSpecialBar(playerEquipment[playerWeapon]);
        spawnedbarrows = false;
        saveCharacter = true;
        Server.playerHandler.updatePlayer(this, outStream);
        Server.playerHandler.updateNPC(this, outStream);
        flushOutStream();
        totalLevel = getPA().calculateTotalLevel();
        getPA().updateQuestTab(); //diary tab
        /**
         * Welcome messages
         */
        getQuestTab().updateInformationTab();
        getPA().sendFrame126("Combat Level: " + combatLevel + "", 3983);
        getPA().sendFrame126("Total level:", 19209);
        getPA().sendFrame126(totalLevel + "", 71121);
        getPA().resetFollow();
        getPA().clearClanChat();
        getPA().resetFollow();
        getPA().setClanData();
        updateRank();

        getBank().onLogin();
        getPA().resetsidebartabs();

        getRunePouch().sendPouchRuneInventory();
        getPA().updatePoisonStatus();
        getQuesting().updateQuestList();
        if (TourneyManager.getSingleton().isInArenaBoundsOnLogin(this)) {
            TourneyManager.getSingleton().handleLoginWithinArena(this);
        }
        if (TourneyManager.getSingleton().isInLobbyBoundsOnLogin(this)) {
            TourneyManager.getSingleton().handleLoginWithinLobby(this);
        }
        if (totalLevel >= 2000) {
            getEventCalendar().progress(EventChallenge.HAVE_2000_TOTAL_LEVEL);
        }
        if (totalLevel >= 2277) {
            Achievements.increase(this, AchievementType.MAX, 1);
        }

        if (!completedTutorial) {


            Server.clanManager.getHelpClan().addMember(this);
            mode = Mode.forType(ModeType.STANDARD);
            receivedVoteStreakRefund = true;

        } else {
            if (mode == null) {
                mode = Mode.forType(ModeType.STANDARD);
            }
            Server.clanManager.joinOnLogin(this);
        }

//        if (!receivedVoteStreakRefund) {
//            receivedVoteStreakRefund = true;
//            VoteUser user = VotePanelManager.getUser(this);
//            if (user != null && user.getDayStreak() < 4) {
//                user.setDayStreak(4);
//                sendMessage("<clan=6> @dre@You've received a 4 Vote Panel streak, thanks for being patient!");
//                VotePanelManager.saveToJSON();
//            }
//        }

        getPA().sendConfig(172, autoRet);
        addEvents();
        if (Configuration.BOUNTY_HUNTER_ACTIVE) {
            bountyHunter.updateTargetUI();
        }
        for (int i = 0; i < 23; i++) {
            getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
            getPA().refreshSkill(i);
        }
        health.setMaximumHealth(getPA().getLevelForXP(playerXP[playerHitpoints]));
        BankPin pin = getBankPin();
        if (pin.requiresUnlock()) {
            pin.open(2);
        }
        if (health.getCurrentHealth() < 10) {
            health.setCurrentHealth(10);
        }

        // Update experience counter on login
        int[] ids = new int[playerLevel.length];
        for (int skillId = 0; skillId < ids.length; skillId++) {
            ids[skillId] = skillId;
        }
//        if (experienceCounter > 0L) {
//            playerAssistant.sendExperienceDrop(false, experienceCounter, ids);
//        }
        playerAssistant.sendExperienceDrop(true, getTotalXp(), ids);
        rechargeItems.onLogin();
        for (int i = 0; i < getQuick().getNormal().length; i++) {
            if (getQuick().getNormal()[i]) {
                getPA().sendConfig(QuickPrayers.CONFIG + i, 1);
            } else {
                getPA().sendConfig(QuickPrayers.CONFIG + i, 0);
            }
        }
        //PollTab.updateInterface(this);
        if (EventCalendar.isEventRunning()) {
            //sendMessage(EventCalendar.LOGIN_MESSAGE);
        }
        if (Server.getConfiguration().getServerState().getLoginMessages() != null) {
            Arrays.stream(Server.getConfiguration().getServerState().getLoginMessages()).forEach(this::sendMessage);
        }
      getDailyRewards().onLogin();
        PlayerSave.login(this);
        correctCoordinates();
       // BossPoints.doRefund(this);
        EventChallengeMonthlyReward.onLogin(this);
     LeaderboardUtils.checkRewards(this);

//        if (!getBankPin().hasBankPin() && isCompletedTutorial()) {
//            sendMessage("@bla@You don't have an account pin, it's highly recommended you set one with ::pin.");
//        }
        getCollectionLog().cltopen(this);

      //  CompromisedAccounts.onLogin(this);
       // PlayerMigrationRepository.migrate(this);
        broadcastlatestupdates();//
        getTaskMasterManager().onLogin();

       // getQuestTab().openTab(QuestTab.Tab.DIARY);

        getQuestTab().openTab(QuestTab.Tab.ACHIEVEMENTS);

        checkinfrunenergy();
        getPA().updateRunEnergy(true);
        getPA().updateRunningToggle();
        DiscordIntegration.setIntegration(this);
        checkpackyackslots();
        checkcurses();
        getDL().LoggedIn();
        getPA().hidestufffrompanels();
        loginScreen();
        handlecursesunlocks();
getPA().setspellbook(playerMagicBook);
//fixautocast();
    }
    public void fixautocast()  {
  //      getPA().sendFrame70(-5, -10, 18584);//word for autocast spell
     //   getPA().sendFrame70(-2,-2,18583);//spell icon for autocast - needs to be based on spell.
    //    getPA().sendFrame70(-10,0,18584);//spell icon for autocast
    }
    private DailyLogin DL = new DailyLogin(this);
    public DailyLogin getDL() {
        return DL;
    }
    public void loginScreen() {
//	getPA().sendFrame200(63118, 592);
//	getPA().sendFrame75(9135, 63118,2);
//        int whichitem = Misc.random((Config.ELVENWEAPONS.length)-1);
//        int whichhelm = Misc.random((Config.SLAYER_HELMS.length)-1);
//        getPA().itemOnInterface(Config.SLAYER_HELMS[whichhelm],1,15769,0);
//        //whichitem = Misc.random(Config.ITEM_SELLABLE.length);
//        getPA().itemOnInterface(Config.ELVENWEAPONS[whichitem],1,15769,1);
        List<String> events = WorldEventContainer.getInstance().getWorldEventStatuses();
        for (String event : events) {
            getPA().sendFrame126("@or1@- " + event, 15259);
        }

    // getPA().showInterface(15244);

        getPA().sendFrame126("Welcome to Runescape!", 15257);
        int currentDay = getLastLogin() - lastLoginDate;


        if (lastLoginDate <= 0) {
            getPA().sendFrame126("This is your first time logging in!", 15258);
        } else if (lastLoginDate == 1) {
            getPA().sendFrame126("You last logged @blu@yesterday@bla@", 15258);
        } else {
            getPA().sendFrame126("You last logged in @red@" + (currentDay > 1 ? (currentDay + " @bla@days ago") : ("earlier today")) + " @bla@", 15258);
        }
 //  int random = Misc.random(welcomeMessages.length-1);
    //getPA().sendFrame126(welcomeMessages[random], 15771);

//					if (DoubleExperience.isDoubleExperience(this))
//			//	sendMessage("");
//		getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Bonus Experience Weekend is ACTIVE!", 15771);
//		//getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Bonus Experience Weekend is ACTIVE!", 15772);
//			if (DoubleSlayerPoints.isDoubleSlayer())
//				getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Double Slayer Tuesday is ACTIVE! Double Slayer points from all tasks!", 15771);
//			if (Config.DOUBLE_VOTE_INCENTIVES)
//				getPA().sendFrame126("<img=10><Col=6092><shad=6092><col=F8AA36>The Staff Team is hosting a Voting Event!"
//						+ "Receive 50% more tickets when claiming votes!</col>", 15771);
//			if (DoubleRaidSaturday.isDoubleRaid())
//				getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Double Raid Saturday! Double loot from the Raids Chest!", 15771);
//			if (DropRateBoost.isDropRateBoost())
//				getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Drop boost Thursday! 5% drop rate boost for the whole day!", 15771);
//			if (DoubleMinigames.isDoubleMinigames())
//				getPA().sendFrame126("@cr10@<Col=6092><shad=6092><col=F8AA36>Bonus point Monday! You receive double bonus points from pest control all day!", 15771);

        getPA().sendFrame126("Runescape (c)", 15265);
        getPA().sendFrame126("Runescape (c)", 15266);

       // getPA().sendFrame126("What do i write here?\\n uhh words here \\n even more words here ", 15259);
        getPA().sendFrame126("Runescape will NEVER email you.\\n We use discord or we \\nwill contact you ingame.", 15260);

       // getPA().sendFrame126("You have 0 unread messages\\nin your mailbox", 15261);
        getPA().sendFrame126("You are currently a "+getRights().buildCrownString() + " " + getRights().getPrimary().toString(), 15262);


        getPA().sendFrame126("CLICK HERE TO PLAY", 15263);
        if (!setPin) {
            getPA().sendFrame126("@red@You currently have no bank pin set!\\n@red@If it is your first time setting a bank pin\\n @red@you will receive an XP book!", 15270);
        } else {
            getPA().sendFrame126("\\n@gre@You currently have a bank pin set.", 15270);
        }

    }
    private String[] welcomeMessages = {
            "Talk to the Runescape Guide to learn about what \\nquests are available!",
            "Talk to Perdu to learn about lockable items!",
            "Use the Wiki button next to the minimap to \\nquickly find info about anything!",
            "The wilderness is a dangerous place with great rewards!"
    };

    public boolean firsttimetutorial = false;
    public void checkcompletedtutorial() {
        if(!firsttimetutorial){
            sendMessage("You have not completed the server tutorial! Claim your free 450k gold by talking to");
            sendMessage("to the Runescape guide!");
        }

    }
    public void checkcurses(){
        if (usingcurseprayers)
            sendMessage("curseicon##0");
         else
            sendMessage("curseicon##1");

    }
    public void checkinfrunenergy(){
        if (System.currentTimeMillis() - infrunenergyDelay < infrunenergyLength) {

            sendMessage("infrunenergy##0");
        } else {
            sendMessage("infrunenergy##1");
        }
    }

    public void broadcastlatestupdates() {
        new Broadcast("<icon=300> Latest server updates!").addLink("serverupdates").submit_justforme(this);

    }
public boolean instatspanel = false;
    public void sendMessage(String s, long delay) {
        String key = "message_delay" + s;
        if (System.currentTimeMillis() - getAttributes().getLong(key, 0) >= delay) {
            getAttributes().setLong(key, System.currentTimeMillis());
            sendMessage(s);
        }
    }

    public void debug(String message, Object...args) {
        debug(Misc.replaceBracketsWithArguments(message, args));
    }

    public void debug(String message) {
        if (debugMessage) {
            sendMessage(message);
        }
    }

    public void sendMessage(String s, Object... args) {
        String message = args.length > 0 ? String.format(s, (Object[]) args) : s;
        sendMessage(message);
    }
    private List<SpecificTeleport> newtelefavs = new ArrayList<>();
    public int[] price2 = new int[28];
    public int[] priceN = new int[28];
    public boolean isChecking;
    public long total;
    public List<SpecificTeleport> getnewfavs() {
        return newtelefavs;
    }
    public void setnewtelefavs(List<SpecificTeleport> newtelefavs) {
        this.newtelefavs = newtelefavs;
    }
    private final NewTeleInterface newteleInterface = new NewTeleInterface(this);

    public NewTeleInterface getnewteleInterface() {
        return newteleInterface;
    }

    public void sendMessageIf(boolean bool, String s, Object... args) {
        if (bool) {
            sendMessage(s, args);
        }
    }

    public void sendMessage(String s) {
        if (s.length() >= 220) {
            logger.error("String is greater than a 130 characters! ({}), player={} {}", s.length(), this, new Exception());
        }

        if (getOutStream() != null) {
            getOutStream().createFrameVarSize(253);
            getOutStream().writeString(s);
            getOutStream().endFrameVarSize();
        }
    }

    public void sendStatement(String... statement) {
        start(new DialogueBuilder(this).statement(statement));
    }

    /**
     * A cache of the side bar interfaces currently set for the player
     */
    private Map<Integer, Integer> sideBarInterfaces = new HashMap<>();

    public void setSidebarInterface(int menuId, int form) {
        if (getOutStream() != null) {
            int cachedMenuForm = sideBarInterfaces.getOrDefault(menuId, -1);
            // Only send sidebar interface if it changes
            if (cachedMenuForm == form)
                return;

            getOutStream().createFrame(71);
            getOutStream().writeUnsignedWord(form);
            getOutStream().writeByteA(menuId);
            sideBarInterfaces.put(menuId, form);
        }
    }

    public boolean firstMove;

    public void addEvents() {
        Server.getEventHandler().submit(new MinigamePlayersEvent(this));
        Server.getEventHandler().submit(new SkillRestorationEvent("skillrestorationevent",this,100));
        Server.getEventHandler().submit(new RunEnergyEvent(this, 1));
        CycleEventHandler.getSingleton().addEvent(this, bountyHunter, 1);
    }

    public void update() {
        Server.playerHandler.updatePlayer(this, outStream);
        Server.playerHandler.updateNPC(this, outStream);
        flushOutStream();
    }

    public void healEverything() {
        setRunEnergy(10000, true);

        if (getHealth().getCurrentHealth() < getHealth().getMaximumHealth()) {
            getHealth().reset();
        }
        if (Boundary.isIn(this,Boundary.HOME_ISLAND))
       startAnimation(645);
       // playerLevel[5] =getPA().getLevelForXP(playerXP[5]);

       // getPA().refreshSkill(5);
        specAmount = 10.0;
        specRestore = 120;
        getItems().addSpecialBar(playerEquipment[playerWeapon], false);
      // playerLevel[5] = getPA().getLevelForXP(playerXP[5]);
        setProtectionPrayersShiftRight(false);
       getHealth().removeAllStatuses();
    getHealth().reset();
        for (int i = 0; i < 23; i++) {
            if(playerLevel[i] > getPA().getLevelForXP(playerXP[i]))
                continue;
            playerLevel[i] = getPA().getLevelForXP(playerXP[i]);
            getPA().refreshSkill(i);
        }
    }

    public void heal(int amount) {
        setRunEnergy(10000, true);
        int heal = amount;
        if (heal > getHealth().getMaximumHealth()) {
            getHealth().reset();
        }
        getHealth().increase(amount);
        getPA().refreshSkill(3);

    }

    public void resetOnDeath() {
        PlayerSave.saveGame(this);
        resetDamageTaken();
        totalHunllefDamage = 0;
        attacking.reset();
        getPA().frame1();
        getPA().resetTb();
        isSkulled = false;
        attackedPlayers.clear();
        headIconPk = -1;
        skullTimer = -1;
        getHealth().reset();
        getHealth().removeAllStatuses();
        getHealth().removeAllImmunities();
        getPA().requestUpdates();
        tradeResetNeeded = true;
      setRunEnergy(10000, true);
        MeleeData.setWeaponAnimations(this);
        Arrays.stream(ClientGameTimer.values()).filter(timer -> timer.isResetOnDeath()).forEach(timer -> getPA().sendGameTimer(timer, TimeUnit.SECONDS, 0));
    }

    /**
     * Update {@link #equippedGodItems}, which is a list of all gods of which the
     * player has at least 1 item equipped.
     */
    public void updateGodItems() {
        equippedGodItems = new ArrayList<>();
        for (God god : God.values()) {
//            if(GodwarsEquipment.EQUIPMENT.containsKey(god)){
//                System.out.println("s:"+GodwarsEquipment.EQUIPMENT.size());
//                System.out.println("pr: "+GodwarsEquipment.EQUIPMENT.get(god).toString());
//            }

if(GodwarsEquipment.EQUIPMENT.get(god) == null){
   // System.out.println("here?");
    continue;
}


            for (int itemId : GodwarsEquipment.EQUIPMENT.get(god)) {

                if (getItems().isWearingItem(itemId)) {
                    equippedGodItems.add(god);
                    break;
                }
            }
        }
    }

    public List<God> getEquippedGodItems() {
        return equippedGodItems;
    }

    public int totalRaidsFinished;

    public int totalRaids3Finished;

    public int[] BLACK_MASKS = {Items.BLACK_MASK, Items.BLACK_MASK_1, Items.BLACK_MASK_2, Items.BLACK_MASK_3, Items.BLACK_MASK_4, Items.BLACK_MASK_5, Items.BLACK_MASK_6, Items.BLACK_MASK_7, Items.BLACK_MASK_8, Items.BLACK_MASK_9, Items.BLACK_MASK_10};
    public int[] SLAYER_HELMETS = {11864, 11865, 19639, 19641, 19643, 19645, 19647, 19649, 21888, 21890, 21264, 21266, 23075, 24444, 24370};
    public int[] IMBUED_SLAYER_HELMETS = {Items.SLAYER_HELMET_I, Items.TWISTED_SLAYER_HELMET_I, Items.TURQUOISE_SLAYER_HELMET_I, Items.RED_SLAYER_HELMET_I, Items.PURPLE_SLAYER_HELMET_I, Items.PURPLE_SLAYER_HELMET_I, Items.BLACK_SLAYER_HELMET_I, Items.GREEN_SLAYER_HELMET_I, Items.HYDRA_SLAYER_HELMET_I};
    public int graceSum;

    public void graceSum() {
        graceSum = 0;
        for (int grace : AgilityHandler.graceful_ids) {
            if (getItems().isWearingItem(grace)) {
                graceSum++;
            }
        }
        if (SkillcapePerks.AGILITY.isWearing(this) || SkillcapePerks.isWearingMaxCape(this)) {
            graceSum++;
        }
    }

    public void checkInstanceCoords() {
        if (getInstance() != null && (getInstance().getBoundaries().stream().noneMatch(boundary -> boundary.in(this)) || getInstance().isDisposed())) {
            logger.debug("Remove player because not in instance boundary or instance was disposed {}", this);
            getInstance().remove(this);
        }
    }

    public double getEnergyDeprecation() {

       // double weightFactor = (67 + ( (67 * Math.min(64,getWeight())) / (64) ) ) / 100;//92 if 24 kg
        double weightFactor = 67 + ( (67 * Math.min(64,getWeight())) / (64) ) ;//92 if 24 kg

        return weightFactor;
    }


    private void lowerEnergy() {
        graceSum();
        int ticks = 1 + graceSum;
        if (staminaDelay> 0)//TODO
            ticks += 7;
        double change = getEnergyDeprecation();

        if (change < 0)
            change = 0.05;

        if (runningDistanceTravelled >= ticks) {
            runningDistanceTravelled = 0;

            setRunEnergy(getRunEnergy() - (int)( change), true);
        }
    }

    public void addQueuedAction(Consumer<Player> action) {
        queuedActions.add(action);
    }

    /**
     * Add an action that will happen first in the {@link Player#finishLogin()}} method.
     * If the player has already logged in this will have no effect.
     */
    public void addQueuedLoginAction(Consumer<Player> action) {
        queuedLoginActions.add(action);
    }

    public void processQueuedActions() {
        Consumer<Player> action;
        while ((action = queuedActions.poll()) != null) {
            action.accept(this);
        }
    }
    public void checkforhealingadditionsorsomething() {

    }

    private void processTickables() {
        List<TickableContainer<Player>> removeList = new ArrayList<>();
        List<TickableContainer<Player>> tickablesCopy = new ArrayList<>(tickables);
        for (TickableContainer<Player> tickable : tickablesCopy) {
            if (tickable.isStopped() || !tickable.tick(this)) {
                removeList.add(tickable);
            }
        }
        tickables.removeAll(removeList);
    }

    public TickableContainer<Player> addTickable(Tickable<Player> tickable) {
        TickableContainer<Player> container = new TickableContainer<>(tickable);
        tickables.add(container);
        return container;
    }

    public TickableContainer<Player> setTickable(Tickable<Player> tickable) {
        if (this.tickable != null) {
            this.tickable.stop();
        }
        if (tickable != null) {
            this.tickable = addTickable(tickable);
            return this.tickable;
        } else {
            this.tickable = null;
            return null;
        }
    }

    public int tournamentFogDuration;
    public int tournamentDamageFromFog;
    public boolean wasInRaids = false;

    public void raidsClipFix() {
        if (Boundary.RAIDS.in(this)) {
            wasInRaids = true;
        } else if ((wasInRaids)) {
            Raids raidInstance = this.getRaidsInstance();
            if (raidInstance != null) {
                sendMessage("@blu@Sending you back to starting room...");
                Location startRoom = raidInstance.getStartLocation();
                getPA().movePlayer(startRoom.getX(), startRoom.getY(), raidInstance.currentHeight);
                raidInstance.resetRoom(this);
                wasInRaids = false;
                PlayerSave.saveGame(this);
            }
        }
    }

    public boolean wasInRaids3 = false;

    public void raids3ClipFix() {
        if (Boundary.RAIDS3.in(this)) {
            wasInRaids3 = true;
        } else if ((wasInRaids3)) {
            Raids3 raids3Instance = this.getRaids3Instance();
            if (raids3Instance != null) {
                sendMessage("@blu@Sending you back to starting room...");
                Location startRoom = raids3Instance.getStartLocation();
                getPA().movePlayer(startRoom.getX(), startRoom.getY(), raids3Instance.currentHeight);
                raids3Instance.resetRoom(this);
                wasInRaids3 = false;
                PlayerSave.saveGame(this);
            }
        }
    }

    public void interruptActions(boolean stopWalk, boolean closeInterfaces, boolean stopAll) {
        /**
         * Just an idea
         */
        if (stopAll) {
            getPA().stopSkilling();
            getPA().resetVariables();
            resetWalkingQueue();
            getPA().removeAllWindows();
            getPA().closeAllWindows();
            interruptActions();
        }
        if (stopWalk) {
            resetWalkingQueue();
        }
        if (closeInterfaces) {
            getPA().removeAllWindows();
            getPA().closeAllWindows();
        }
    }

    /**
     * Reset things like skilling (tickables, combat, etc) when needed.
     */
    public void interruptActions() {
        for (int i = 0; i < playerSkilling.length; i++) {
            playerSkilling[i] = false;
        }
        setTickable(null);
    }

    public void process() {
     getDonationRewards().tick();
        raidsClipFix();
        raids3ClipFix();
        processQueuedActions();
        processTickables();
        lowerEnergy();
        getDailyRewards().notifyWhenReady(false);
//        if (getCannon() != null) {
//            getCannon().tick(this);
//        }
        // If player hasn't completed tutorial, no dialogues are open and mode selection interface isn't open, then we open it.
//        if (!isCompletedTutorial()
//                && (getDialogueBuilder() == null || getDialogueBuilder().getCurrent() == null)
//                && !isInterfaceOpen(ModeSelection.INTERFACE_ID)) {
//            modeSelection.openInterface();
//        }
        if (teleBlockStartMillis > 0 && System.currentTimeMillis() - teleBlockStartMillis >= teleBlockLength) {
            teleBlockLength = 0;
            teleBlockStartMillis = 0;
            sendMessage("The spell blocking your teleport has expired.");
        }
        if(hasNexVirus && nexCoughDelay <= 0) {
            int[] toDecrease = { 0, 1, 2, 4, 6 };

            for (int tD : toDecrease) {
                playerLevel[tD] -= getLevelForXP(playerXP[tD]) * .01; // .01 = 1 level
                if (playerLevel[tD] < 0)
                    playerLevel[tD] = 1;
                getPA().refreshSkill(tD);
                getPA().setSkillLevel(tD, playerLevel[tD], playerXP[tD]);
                forcedChat("*Cough*");
            }
        }
        if(hasNexVirus && nexVirusTimer <= 0)
            hasNexVirus = false;
        if (hasOverloadBoost) {
            if (Boundary.isIn(this, Boundary.DUEL_ARENA) || Boundary.isIn(this, Boundary.WILDERNESS)) {
                getPotions().resetOverload();
                getPA().sendGameTimer(ClientGameTimer.OVERLOAD, TimeUnit.MINUTES, 0);
            }
        }
        if ((underAttackByPlayer > 0 || underAttackByNpc > 0)) {
            if (this.serpHelmCombatTicks < 8) this.serpHelmCombatTicks++;
            this.getCombatItems().checkCombatTickBasedItems();
        }
        if (xpScrollTicks > 0) {
            xpScrollTicks--;
            if (xpScrollTicks <= 0) {
                xpScrollTicks = 0;
                xpScroll = false;
                sendMessage("@red@Your xp scroll has run out!");
            }
        }
        if (ddScrollTicks > 0) {
            ddScrollTicks--;
            if (ddScrollTicks <= 0) {
                ddScrollTicks = 0;
                ddScroll = false;
                sendMessage("@red@Your double drops sigil has run out!");
            }
        }
        if (fasterCluesTicks > 0) {
            fasterCluesTicks--;
            if (fasterCluesTicks <= 0) {
                fasterCluesTicks = 0;
                fasterCluesScroll = false;
                sendMessage("@red@Your faster clue scroll has run out!");
            }
        }
        if (skillingPetRateTicks > 0) {
            skillingPetRateTicks--;
            if (skillingPetRateTicks <= 0) {
                skillingPetRateTicks = 0;
                skillingPetRateScroll = false;
                sendMessage("@red@Your skilling pet rate bonus has ran out!");
            }
        }
        if (getInstance() != null) {
            getInstance().tick(this);
        }
        if (isRunningToggled() && runEnergy <= 0) {
            updateRunningToggled(false);
        }
        if (staminaDelay > 0) {
            staminaDelay--;
        }
        if(nexCoughDelay > 0) {
            nexCoughDelay--;
        }
        if(nexVirusTimer > 0) {
            nexVirusTimer--;
        } else
            hasNexVirus = false;
        if (gwdAltarTimer > 0) {
            gwdAltarTimer--;
        }
        if (gwdAltarTimer == 1) {
            sendMessage("You can now operate the godwars prayer altar again.");
        }
        if (updateItems) {
            itemAssistant.updateItems();
            updateItems = false;
        }
        if (bonusXpTime > 0) {
            bonusXpTime--;
        }
        if (bonusXpTime == 1) {
            sendMessage("@blu@Your time is up. Your XP is no longer boosted by the voting reward.");
        }
        if (isDead && respawnTimer == -6) {
            PlayerDeath.applyDead(this);
        }
        if (respawnTimer == 9) {
            respawnTimer = -6;
            PlayerDeath.giveLife(this);
        } else if (respawnTimer == 12) {
            // Set killer in combat delay
            if (underAttackByPlayer > 0 && underAttackByPlayer < PlayerHandler.players.length && PlayerHandler.players[underAttackByPlayer] != null) {
                PlayerHandler.players[underAttackByPlayer].setInCombatDelay(Configuration.IN_COMBAT_TIMER);
            }
            // Animation
            respawnTimer--;
            startAnimation(2304);
        }
        if (Boundary.isIn(this, Boundary.ZULRAH) && getZulrahEvent().isInToxicLocation()) {
            appendDamage(null, 1 + Misc.random(3), Hitmark.VENOM);
        }
        if (respawnTimer > -6) {
            respawnTimer--;
        }
        if (hitDelay > 0) {
            hitDelay--;
        }
        getAgilityHandler().agilityProcess(this);
        if (specRestore > 0) {
            specRestore--;
        }
        if (playTime < Integer.MAX_VALUE && !isIdle) {
            playTime++;
        }
        if (yakLevel < 2 && PetHandler.hasstoragepetout(this)) {
            yakplayTime++;
            if(yakLevel == 0 && yakplayTime > 10_000){
                yakLevel = 1;
               // PetHandler.Pets pet = PetHandler.forItem(petSummonId);
                NPC yourpet = getSpawnedNPC();
                if(yourpet != null){
                    yourpet.requestTransform(15572);
                    yourpet.forceChat("Baa!");
                    sendMessage("Your yak grew! @bla@(@blu@6@bla@) slots.");
                    packyakSlots = 6;
                }
            } else if(yakLevel == 1 && yakplayTime > 30_000) {
                NPC yourpet = getSpawnedNPC();
                yourpet.requestTransform(15573);
                yourpet.forceChat("Baa!");
                sendMessage("Your yak grew! @bla@(@blu@9@bla@) slots.");
                packyakSlots = 9;
                yakLevel = 2;
            }
        }
        //getPA().sendFrame126("@or1@Players Online: @gre@" + PlayerHandler.getPlayerCount() + "", 10222);
        if (System.currentTimeMillis() - specDelay > Configuration.INCREASE_SPECIAL_AMOUNT) {
            specDelay = System.currentTimeMillis();
            if (specAmount < 10) {
                specAmount += 1;
                if (specAmount > 10) specAmount = 10;
                getItems().updateSpecialBar();
                getItems().addSpecialBar(playerEquipment[playerWeapon]);
            }
        }
        checkforhealingadditionsorsomething();
        CombatPrayer.handlePrayerDrain(this);
        if (underAttackByPlayer > 0) {
            if (System.currentTimeMillis() - singleCombatDelay > inCombatDelay) {
                underAttackByPlayer = 0;
                setInCombatDelay(Configuration.IN_COMBAT_TIMER);
            }
        }
        if (underAttackByNpc > 0) {
            if (System.currentTimeMillis() - singleCombatDelay2 > inCombatDelay) {
                underAttackByNpc = 0;
                setInCombatDelay(Configuration.IN_COMBAT_TIMER);
            }
        }
        if (hasOverloadBoost) {
            if (System.currentTimeMillis() - lastOverloadBoost > 15000) {
                getPotions().doOverloadBoost();
                lastOverloadBoost = System.currentTimeMillis();
            }
        }
        sendAreaInterfaces();

        if (!getPosition().inWild()) {
            wildLevel = 0;
        }
//        if (Boundary.isIn(this, Boundary.EDGEVILLE_PERIMETER) && !Boundary.isIn(this, Boundary.EDGE_BANK) && getHeight() == 8) {
//            wildLevel = 126;
//        }
        if (!hasMultiSign && getPosition().inMulti() || Boundary.isIn(this, Boundary.REV_CAVE)) {
            hasMultiSign = true;
            getPA().multiWay(1);
        }
        if (hasMultiSign && !getPosition().inMulti() && !Boundary.isIn(this, Boundary.REV_CAVE)) {
            hasMultiSign = false;
            getPA().multiWay(-1);
        }
        if (!getPosition().inMulti() && !Boundary.isIn(this, Boundary.REV_CAVE) && getPosition().inWild())
            getPA().sendFrame70(30, 0, 196);
        else if ((getPosition().inMulti() || Boundary.isIn(this, Boundary.REV_CAVE) )&& getPosition().inWild())
            getPA().sendFrame70(30, -35, 196);
        if (this.skullTimer > 0) {
            --skullTimer;
            if (skullTimer == 1) {
                isSkulled = false;
                attackedPlayers.clear();
                headIconPk = -1;
                skullTimer = -1;
                getPA().requestUpdates();
            }
        }
        if (freezeTimer > -6) {
            freezeTimer--;
            if (frozenBy != null) {
                Entity frozenByReference = frozenBy.get();
                if (frozenByReference == null) {
                    freezeTimer = -1;
                    frozenBy = null;
                } else if (distance(frozenByReference.getPosition()) > 13) {
                    freezeTimer = -1;
                    frozenBy = null;
                }
            }
        }
        if (teleTimer > 0) {
            teleTimer--;
        }
        if (attackTimer > 0) {
            attackTimer--;
        }
        if (targeted != null) {
            if (distanceToPoint(targeted.getX(), targeted.getY()) > 10) {
                getPA().sendEntityTarget(0, targeted);
                targeted = null;
            }
        }
        getItems().processContainerUpdates();
    }

    public void processCombat() {
        if (npcAttackingIndex > 0 && clickNpcType == 0 || playerAttackingIndex > 0) {
            // Attempt to execute a granite maul special if queued
            if (getCombatItems().doQueuedGraniteMaulSpecials())
                return;
        }
        if (npcAttackingIndex > 0 && clickNpcType == 0) {
            attacking.attackEntity(NPCHandler.npcs[npcAttackingIndex]);
        }
        if (playerAttackingIndex > 0) {
            attacking.attackEntity(PlayerHandler.players[playerAttackingIndex]);
        }
    }

    private void sendAreaInterfaces() {
        if (!getController().isDefault()) { // Only do this on default, otherwise use controller enter and exit to set these
            //System.out.println("uhh here?");
            return;
        }
        // Player options in this if-else
        if (Boundary.isIn(this, FlowerPoker.BOUNDARIES)) {
            getPA().showOption(1, 0, "<img=29>Gamble with");
        } else if (getPosition().inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENA)) {
            if (Boundary.isIn(this, Boundary.DUEL_ARENA)) {//the interior
                getPA().showOption(3, 0, "Attack");
                getPA().showOption(1, 0, "null");
            } else {
                getPA().showOption(1, 0, "Challenge");
                getPA().showOption(3, 0, "null");
            }
        } else if (getPosition().inWild() || getPosition().inClanWars() && getPosition().inWild() || inPits) {
            getPA().showOption(3, 0, "Attack");
        } else {
            getPA().showOption(3, 0, "null");
            getPA().showOption(1, 0, "null");
        }

        // Walkable interfaces in this if-else
        if (getPosition().inWild() && !getPosition().inClanWars()) {
            int modY = absY > 6400 ? absY - 6400 : absY;
            wildLevel = (((modY - 3520) / 8) + 1);
            if (Configuration.SINGLE_AND_MULTI_ZONES) {
                //System.out.println("ATTEMPTING TO SEND LEVEL: " + wildLevel);
                getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
            } else {
                getPA().multiWay(-1);
                getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
            }
            if (Configuration.BOUNTY_HUNTER_ACTIVE && !getPosition().inClanWars()) {
                getPA().walkableInterface(28000);
                getPA().sendInterfaceHidden(1, 28070);
                getPA().sendInterfaceHidden(0, 196);
            } else {
                getPA().walkableInterface(197);
            }
            if (Boundary.isIn(this, Boundary.DEEP_WILDY_CAVES)) {
                getPA().sendFrame126("", 199);
                getPA().sendFrame126("@yel@Level: " + wildLevel, 250);
            } else {
                getPA().sendFrame126("", 250);
            }
        } else if (getPosition().inClanWars() && getPosition().inWild()) {
            getPA().walkableInterface(197);
            getPA().sendFrame126("@yel@3-126", 199);
            wildLevel = 126;
        } else if (Boundary.isIn(this, Boundary.SCORPIA_LAIR)) {
            getPA().sendFrame126("@yel@Level: 54", 199);
            // getPA().walkableInterface(197);
            wildLevel = 54;
        } else if (getItems().isWearingItem(10501, 3) && !getPosition().inWild()) {
            getPA().showOption(3, 0, "Throw-At");
        } else if (getPosition().inEdgeville()) {
            if (Configuration.BOUNTY_HUNTER_ACTIVE) {
                if (bountyHunter.hasTarget()) {
                    getPA().sendFrame99(0);
                    getPA().walkableInterface(28000);
                    getPA().sendInterfaceHidden(0, 28070);
                    getPA().sendInterfaceHidden(1, 196);
                    bountyHunter.updateOutsideTimerUI();
                } else {
                    getPA().sendFrame99(0);
                    getPA().walkableInterface(-1);
                }
            } else {
                getPA().sendFrame99(0);
                getPA().walkableInterface(-1);
                getPA().showOption(3, 0, "Null");
            }
            getPA().showOption(3, 0, "null");
        } else if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
            getPA().walkableInterface(21119);
            PestControl.drawInterface(this, "lobby");
        } else if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
            getPA().walkableInterface(21100);
            PestControl.drawInterface(this, "game");
        } else if ((getPosition().inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENA))) {
            getPA().walkableInterface(201);
            wildLevel = 126;
        } else if (getPosition().inGodwars()) {
            godwars.drawInterface();
            getPA().walkableInterface(16210);
        } else if (Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM)) {
            getPA().walkableInterface(29230);
        } else if (getBarrows().inBarrows()) {//
            getPA().walkableInterface(4535);
            getPA().sendFrame126("Killcount: " + getBarrows().getMonsterKillCount(),
                    4536);
            if (Boundary.isIn(this, Barrows.TOMBS) || Boundary.isIn(this, Barrows.TUNNEL))
                getPA().sendFrame99(2);
            else
                getPA().sendFrame99(0);

        } else if (getPosition().inRaidLobby()) {
            getPA().walkableInterface(6673);
        } else if (getPosition().inRaids3Lobby()) {
            getPA().walkableInterface(6673);
        } else if (Boundary.isIn(this, Boundary.FALADOR_MOLE_LAIR)) {
            if (!getItems().hasItemOnOrInventory(LightSources.LITITEMS)) {
                getPA().sendFrame99(2);
                getPA().walkableInterface(13583);
                //  sendMessage("You do not have a light source.");
            }
        } else if (Boundary.isIn(this, DesertEvent.DESERT)) {
            if (!Server.getEventHandler().isRunning(this, "desert")) {
                Server.getEventHandler().submit(new DesertEvent("desert", this, (EquipmentSet.DESERT_ROBES.isWearing(this) ? 250 : 150)));
                } else {
                if (Server.getEventHandler().isRunning(this, "desert")) {
                    Server.getEventHandler().stop(this, "desert");
                }
            }
        } else if (getInstance() == null || !getInstance().handleInterfaceUpdating(this)) {
            getPA().walkableInterface(-1);
            getPA().sendFrame99(0);
        } else {
            getPA().walkableInterface(-1);
            getPA().sendFrame99(0);
        }
    }

    public Stream getInStream() {
        return inStream;
    }

    public Stream getOutStream() {
        return outStream;
    }

    public ItemAssistant getItems() {
        return itemAssistant;
    }

    public PlayerAssistant getPA() {
        return playerAssistant;
    }
    public SpecificTeleport specificteleport;
    public CollectionLog getCollectionLog() {
        return collectionLog;
    }

    public CollectionLog getGroupIronmanCollectionLog() {
        if (getRights().contains(Right.GROUP_IRONMAN)) {
            GroupIronmanGroup group = GroupIronmanRepository.getGroupForOnline(this).orElse(null);
            if (group != null && group.getCollectionLog() != null) {
                return group.getCollectionLog();
            }
        }

        return null;
    }

    public CollectionLog getViewingCollectionLog() {
        return viewingCollectionLog;
    }

    public void setViewingCollectionLog(CollectionLog viewingCollectionLog) {
        this.viewingCollectionLog = viewingCollectionLog;
    }

    public DialogueHandler getDH() {
        return dialogueHandler;
    }

    public ChargeTrident getCT() {
        return chargeTrident;
    }

    public ShopAssistant getShops() {
        return shopAssistant;
    }

    public CombatItems getCombatItems() {
        return combatItems;
    }

    public ActionHandler getActions() {
        return actionHandler;
    }

    public Channel getSession() {
        return session;
    }

    public Potions getPotions() {
        return potions;
    }

    public Food getFood() {
        return food;
    }

    public PlayerAssistant getPlayerAssistant() {
        return playerAssistant;
    }

    public SkillInterfaces getSI() {
        return skillInterfaces;
    }

    public int getRuneEssencePouch(int index) {
        return runeEssencePouch[index];
    }

    public void setRuneEssencePouch(int index, int runeEssencePouch) {
        this.runeEssencePouch[index] = runeEssencePouch;
    }

    public int getPureEssencePouch(int index) {
        return pureEssencePouch[index];
    }

    public void setPureEssencePouch(int index, int pureEssencePouch) {
        this.pureEssencePouch[index] = pureEssencePouch;
    }

    public Slayer getSlayer() {
        if (slayer == null) {
            slayer = new Slayer(this);
        }
        return slayer;
    }

    public Agility getAgility() {
        return agility;
    }

    public Thieving getThieving() {
        return thieving;
    }

    public Herblore getHerblore() {
        return herblore;
    }
    public Map<God, Integer> gwdkillcount = new HashMap<>();
    public Godwars getGodwars() {

        return godwars;
    }

    public TreasureTrails getTrails() {
        return trails;
    }

    public GnomeAgility getGnomeAgility() {
        return gnomeAgility;
    }

    public PointItems getPoints() {
        return pointItems;
    }

    public void setMovementState(PlayerMovementState movementState) {
        this.movementState = movementState;
    }

    public PlayerMovementState getMovementState() {
        return movementState == null ? PlayerMovementState.getDefault() : movementState;
    }

    public WildernessAgility getWildernessAgility() {
        return wildernessAgility;
    }

    public Shortcuts getAgilityShortcuts() {
        return shortcuts;
    }

    public RooftopPollnivneach getRooftopPollnivneach() {
        return this.rooftopPollnivneach;
    }

    public RooftopCanafis getRooftopCanafis() {
        return this.rooftopCanafis;
    }

    public RooftopAlkharid getRooftopAlkharid() {
        return this.rooftopAlkharid;
    }

    public RooftopFalador getRooftopFalador() {
        return this.rooftopFalador;
    }

    public RooftopDraynor getRoofTopDraynor() {
        return this.rooftopDraynor;
    }

    public RooftopRellekka getRooftopRellekka() {
        return this.rooftopRellekka;
    }

    public Lighthouse getLighthouse() {
        return lighthouse;
    }

    public BarbarianAgility getBarbarianAgility() {
        return barbarianAgility;
    }

    public AgilityHandler getAgilityHandler() {
        return agilityHandler;
    }

    public Smithing getSmithing() {
        return smith;
    }

    public FightCave getFightCave() {
        if (fightcave == null) fightcave = new FightCave(this);
        return fightcave;
    }
    public TokkulPit1 getTokkulPit1() {
        if (tokkulPit1 == null) tokkulPit1 = new TokkulPit1(this);
        return tokkulPit1;
    }
    public Skotizo getSkotizo() {
        if (getInstance() != null && getInstance() instanceof Skotizo) {
            return (Skotizo) getInstance();
        }

        return null;
    }

    public SmithingInterface getSmithingInt() {
        return smithInt;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    /*
     * public Fletching getFletching() { return fletching; }
     */
    public Prayer getPrayer() {
        return prayer;
    }

    public void queueMessage(Packet packet, boolean priority) {
        attemptedPackets.incrementAndGet();
        packetsReceived.incrementAndGet();
        if (priority)
            priorityPackets.add(packet);
        else
            queuedPackets.add(packet);
    }

    public void processQueuedPackets(boolean priority) {
        processQueuedPackets(priority ? priorityPackets : queuedPackets);
    }

    private void processQueuedPackets(Queue<Packet> queue) {
        Packet p;
        attemptedPackets.set(0);
        packetsReceived.set(0);
        while ((p = queue.poll()) != null) {
            lastPacketReceived = System.currentTimeMillis();
            inStream.currentOffset = 0;
            inStream.buffer = p.getPayload().array();

            if (p.getOpcode() > 0) {
                PacketHandler.processPacket(this, p.getOpcode(), p.getLength());
            }
        }
    }

    public void correctCoordinates() {
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.OBOR_AREA)) {
            setTeleportToX(3095);
            setTeleportToY(9833);
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.BRYOPHYTA_ROOM)) {
            setTeleportToX(3174);
            setTeleportToY(9900);
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.KRAKEN_BOSS_ROOM)) {
            setTeleportToX(2280);
            setTeleportToY(10016);
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.NexAngelOfDeath)) {
            setTeleportToX(3739);
            setTeleportToY(3971);
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.GROTESQUE_LAIR)) {
            setTeleportToX(3428);
            setTeleportToY(3541);
            heightLevel = 2;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.PEST_CONTROL_AREA)) {
            setTeleportToX(2657);
            setTeleportToY(2639);
            heightLevel = 0;
        }
        if (Boundary.isIn(this, Boundary.XERIC) || Boundary.isIn(this, Boundary.XERIC_LOBBY)) {
            setTeleportToX(3033);
            setTeleportToY(6068);
            setHeight(0);
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.FIGHT_CAVE)) {
            heightLevel = getIndex() * 4;
            sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
            getFightCave().spawn();
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.TOKKUL_PIT1)) {
            heightLevel = getIndex() * 4;
            sendMessage("Wave " + (this.waveId1 + 1) + " will start in approximately 5-10 seconds. ");
            getTokkulPit1().spawn();
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.INFERNO)) {
            Inferno.moveToExit(this);
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.ZULRAH)) {
            moveToHome();
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.AvatarOfCreation)) {
            moveToHome();
            AvatarOfCreation.deleteEventItems(this);
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), NightmareConstants.BOUNDARY)) {
            moveTo(NightmareConstants.NIGHTMARE_PLAYER_EXIT_POSITION);
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), NightmareConstants.LOBBY_BOUNDARY)) {
            moveToHome();
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.RAIDROOMS)) {
            moveToHome();
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.RAIDS3ROOMS)) {
            moveToHome();
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.CRYSTAL_CAVE_STAIRS)) {
            moveToHome();
            heightLevel = 0;
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.CRYSTAL_CAVE_ENTRANCE)) {
            moveToHome();
            heightLevel = 0;
        }
//        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.GRAND_EXCHANGE)) {
//            moveToHome();
//            heightLevel = 0;
//        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.VORKATH)) {
            moveTo(new Position(2272, 4051, 0));
        }
        if (Arrays.stream(Boundary.CERBERUS_BOSSROOMS).anyMatch(cerb -> {
            return Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), cerb);
        })) {
            moveTo(Cerberus.EXIT);
        }
        if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), Boundary.DAGANNOTH_MOTHER_HFTD)) {
            moveTo(new Position(2515, 4629, 0));
        }
        for (Boundary boundary : TombsOfAmascutConstants.ALL_BOUNDARIES1) {
            if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), boundary)) {
                moveTo(TombsOfAmascutConstants.FINISHED_TOMBS_OF_AMASCUT_POSITION);
            }
        }
        for (Boundary boundary : TobConstants.ALL_BOUNDARIES) {
            if (Boundary.isIn(new Position(teleportToX, teleportToY, heightLevel), boundary)) {
                moveTo(TobConstants.FINISHED_TOB_POSITION);
            }
        }
    }

    private void moveToHome() {
        setTeleportToX(Configuration.START_LOCATION_X);
        setTeleportToY(Configuration.START_LOCATION_Y);
        this.heightLevel = 0;
    }

    public void updateRank() {
        if (amDonated <= 0) {
            amDonated = 0;
        }

        if (amDonated >= 25 && amDonated <= 99) {
            if (getRights().isOrInherits(Right.YOUTUBER) || getRights().isOrInherits(Right.IRONMAN)
                    || getRights().isOrInherits(Right.ULTIMATE_IRONMAN)
                    || getRights().isOrInherits(Right.OSRS)
                    || getRights().isOrInherits(Right.HELPER)
                    || getRights().isOrInherits(Right.MODERATOR)
                    || getRights().isOrInherits(Right.HC_IRONMAN)) {
                getRights().add(Right.REGULAR_DONATOR);
            } else
       //     sendMessage("Your hidden donator rank is now active.");
            {
                getRights().setPrimary(Right.REGULAR_DONATOR);
                //sendMessage("Please relog to receive your donator rank.");
            }
        }
        if (amDonated >= 100 && amDonated <= 249) {
            if (getRights().isOrInherits(Right.YOUTUBER) || getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
                getRights().add(Right.EXTREME_DONOR);
            } else
            //sendMessage("Your hidden extreme donator rank is now active.");
            {
                getRights().setPrimary(Right.EXTREME_DONOR);
                //sendMessage("Please relog to receive your extreme donator rank.");
            }
        }
        if (amDonated >= 250 && amDonated <= 499) {
            if (getRights().isOrInherits(Right.YOUTUBER) || getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
                getRights().add(Right.LEGENDARY_DONATOR);
            } else
            //sendMessage("Your hidden legendary donator rank is now active.");
            {
                getRights().setPrimary(Right.LEGENDARY_DONATOR);
                //sendMessage("Please relog to receive your legendary donator rank.");
            }
        }
        if (amDonated >= 500 && amDonated <= 999) {
            if (getRights().isOrInherits(Right.YOUTUBER) || getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
                getRights().add(Right.DIAMOND_CLUB);
            } else
            //sendMessage("Your hidden diamond club rank is now active.");
            {
                getRights().setPrimary(Right.DIAMOND_CLUB);
                //sendMessage("Please relog to receive your diamond club rank.");
            }
        }
        if (amDonated >= 1000) {
            if (getRights().isOrInherits(Right.YOUTUBER) || getRights().isOrInherits(Right.IRONMAN) || getRights().isOrInherits(Right.ULTIMATE_IRONMAN) || getRights().isOrInherits(Right.OSRS) || getRights().isOrInherits(Right.HELPER) || getRights().isOrInherits(Right.MODERATOR) || getRights().isOrInherits(Right.HC_IRONMAN)) {
                getRights().add(Right.ONYX_CLUB);
            } else
            //sendMessage("Your hidden onyx club donator rank is now active.");
            {
                getRights().setPrimary(Right.ONYX_CLUB);
                //sendMessage("Please relog to receive your Onyx Club donator rank.");
            }
        }
        updatePlayerPanel();
        getPA().upgradenow();
        //sendMessage("Your updated total amount donated is now $" + amDonated + ".");
    }

    public int getPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(int option) {
        this.privateChat = option;
    }

    public Trade getTrade() {
        return trade;
    }

    public FlowerPokerHand flowerPokerHand;

    public FlowerPoker getFlowerPokerRequest() {
        return flowerPoker;
    }

    public FlowerPokerHand getFlowerPoker() {
        if (flowerPokerHand == null)
            this.flowerPokerHand = new FlowerPokerHand(this);
        return flowerPokerHand;
    }

    public boolean isFping() {
        return flowerPokerHand != null && flowerPokerHand.other != null;
    }

    public AchievementHandler getAchievements() {
        if (achievementHandler == null) achievementHandler = new AchievementHandler(this);
        return achievementHandler;
    }

    public long getLastContainerSearch() {
        return lastContainerSearch;
    }

    public void setLastContainerSearch(long lastContainerSearch) {
        this.lastContainerSearch = lastContainerSearch;
    }
    public BattlePass getBattlePass() {
        return battlepass;
    }

    public CoinBagEmpty getCoinBagEmpty() {
        return coinBagEmpty;
    }

    public CoinBagSmall getCoinBagSmall() {
        return coinBagSmall;
    }

    public CoinBagMedium getCoinBagMedium() {
        return coinBagMedium;
    }

    public CoinBagLarge getCoinBagLarge() {
        return coinBagLarge;
    }

    public CoinBagBuldging getCoinBagBuldging() {
        return coinBagBuldging;
    }

    public SuperMysteryBox getSuperMysteryBox() {return superMysteryBox;}
    public BlackAodLootChest getBlackAodLootChest() {return blackAodLootChest;}
    public FoeMysteryBox getFoeMysteryBox() {
        return foeMysteryBox;
    }

    public SlayerMysteryBox getSlayerMysteryBox() {
        return slayerMysteryBox;
    }

    public VoteMysteryBox getVoteMysteryBox() {
        return voteMysteryBox;
    }

    public PvmCasket getPvmCasket() {
        return pvmCasket;
    }

    public DailyGearBox getDailyGearBox() {
        return dailyGearBox;
    }

    public DailySkillBox getDailySkillBox() {
        return dailySkillBox;
    }

    public EntityDamageQueue getDamageQueue() {
        return entityDamageQueue;
    }

    public long[] reduceSpellDelay = new long[6];
    public int reduceSpellId;
    public static boolean[] canUseReducingSpell = {true, true, true, true, true, true};
    public boolean usingPrayer;
    public boolean isSelectingQuickprayers;
    public boolean[] prayerActive = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
    private boolean protectionPrayersShiftRight;

    /**
     * Retrieves the bounty hunter instance for this client object. We use lazy
     * initialization because we store values from the player save file in the
     * bountyHunter object upon login. Without lazy initialization the value would
     * be overwritten.
     *
     * @return the bounty hunter object
     */
    public BountyHunter getBH() {
        if (Objects.isNull(bountyHunter)) {
            bountyHunter = new BountyHunter(this);
        }
        return bountyHunter;
    }

    public UnnecessaryPacketDropper getPacketDropper() {
        return packetDropper;
    }

    public Optional<ItemCombination> getCurrentCombination() {
        return currentCombination;
    }

    public void setCurrentCombination(Optional<ItemCombination> combination) {
        this.currentCombination = combination;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return connectedFrom;
    }

    public void setIpAddress(String ipAddress) {
        this.connectedFrom = ipAddress;
    }

    public Duel getDuel() {
        return duelSession;
    }

    public void setItemOnPlayer(Player player) {
        this.itemOnPlayer = player;
    }

    public Player getItemOnPlayer() {
        return itemOnPlayer;
    }

    public Killstreak getKillstreak() {
        if (killstreaks == null) {
            killstreaks = new Killstreak(this);
        }
        return killstreaks;
    }

    /**
     * Returns the single instance of the {@link NPCDeathTracker} class for this
     * player.
     *
     * @return the tracker clas
     */
    public NPCDeathTracker getNpcDeathTracker() {
        return npcDeathTracker;
    }

    /**
     * The zulrah event
     *
     * @return event
     */
    public Zulrah getZulrahEvent() {
        return zulrah;
    }

    /**
     * The single {@link WarriorsGuild} instance for this player
     *
     * @return warriors guild
     */
    public WarriorsGuild getWarriorsGuild() {
        return warriorsGuild;
    }


    public WarriorsGuildbasement getWarriorsGuildbasement() {
        return warriorsGuildbasement;
    }

    /**
     * The single instance of the {@link PestControlRewards} class for this player
     *
     * @return the reward class
     */
    public PestControlRewards getPestControlRewards() {
        return pestControlRewards;
    }

    public Mining getMining() {
        return mining;
    }

    public SpellBook getSpellBook() {
        switch (playerMagicBook) {
            case 0:
                return SpellBook.MODERN;
            case 1:
                return SpellBook.ANCIENT;
            case 2:
                return SpellBook.LUNAR;
            case 3:
                return SpellBook.LUNAR;
            default:
                throw new IllegalArgumentException("Book out of bounds: " + playerMagicBook);
        }
    }

    public void setSpellBook(SpellBook spellBook) {
        switch (spellBook) {
            case MODERN:
                setSidebarInterface(6, 938);
                playerMagicBook = 0;
                sendMessage("You feel a drain on your memory.");
                getPA().resetAutocast();
                break;
            case ANCIENT:
                playerMagicBook = 1;
                setSidebarInterface(6, 838);
                sendMessage("An ancient wisdom fills your mind.");
                getPA().resetAutocast();
                break;
            case LUNAR:
                sendMessage("You switch to the lunar spellbook.");
                setSidebarInterface(6, 29999);
                playerMagicBook = 2;
                getPA().resetAutocast();
                break;
        }
        getPA().resetAutocast();
    }

    public boolean isAutoButton(int button) {
        for (int j = 0; j < CombatSpellData.AUTOCAST_IDS.length; j += 2) {
            if (CombatSpellData.AUTOCAST_IDS[j] == button)
                return true;
        }
        return false;
    }

//    public void assignAutocast(int button) {
//        for (int j = 0; j < CombatSpellData.AUTOCAST_IDS.length; j++) {
//            if (CombatSpellData.AUTOCAST_IDS[j] == button) {
//                Player c = PlayerHandler.players[this.getIndex()];
//                autocasting = true;
//                autocastId = CombatSpellData.AUTOCAST_IDS[j + 1];
//                if (c.autocastingDefensive) {
//                    c.getPA().sendConfig(109, 1);
//                    c.getPA().sendConfig(108, 0);
//                } else {
//                    c.getPA().sendConfig(108, 1);
//                    c.getPA().sendConfig(109, 0);
//                }
//                c.setSidebarInterface(0, 328);
//                break;
//            }
//        }
//    }

    public boolean defensiveauto = false;
public void assignAutocast(int button) {
    for (int j = 0; j < CombatSpellData.AUTOCAST_IDS.length; j++) {
        if (CombatSpellData.AUTOCAST_IDS[j] == button) {

            autocasting = true;
            autocastId = CombatSpellData.AUTOCAST_IDS[j + 1];//1
                if (autocastingDefensive) {
               //     getPA().sendConfig(109, 1);
                  //  getPA().sendConfig(108, 0);
                    getPA().sendFrame126(getSpellName(CombatSpellData.AUTOCAST_IDS[j + 1]+1),24113);
                    getPA().sendChangeSprite(24114, (byte) getSpellSprite(button));

                    //reset other..
                    getPA().sendFrame126("Spell",18584);
                    getPA().sendChangeSprite(18583,(byte) 0);
                } else {
                  //  getPA().sendConfig(108, 1);
                //   getPA().sendConfig(109, 0);
                    getPA().sendFrame126(getSpellName(CombatSpellData.AUTOCAST_IDS[j + 1]+1),18584);
                   getPA().sendChangeSprite(18583, (byte) getSpellSprite(button));
                    getPA().sendFrame70(0, -5, 18584);//word for autocast spell
                   //reset other
                    getPA().sendFrame126("Spell",24113);
                    getPA().sendChangeSprite(24114,(byte) 0);

                }
            getPA().sendConfig(109, autocastingDefensive ? 1 : 0);
            getPA().sendConfig(108,  autocastingDefensive ? 0 : 1);
//System.out.println("here:autocastingDefensive :"+autocastingDefensive);
         setSidebarInterface(0, 29767);


            break;
        }
    }
}
    public int getSpellSprite(int buttonID) {
        switch(buttonID) {
            case 51080://ice barrage
                return 1;
            case 51058://ice blitz
                return 2;
            case 51069://ice burst
                return 3;
            case 24018://ice rush
                return 4;

            case 51122://BLOOD barrage
                return 5;
            case 51102://blood blitz
                return 6;
            case 51111://blood burst
                return 7;
            case 51091://blood rush
                return 8;


            case 51224://shadow barrage
                return 9;
            case 51198://shadow blitz
                return 10;
            case 51211://shadow burst
                return 11;
            case 51185://shadow rush
                return 12;

            case 51172://smoke barrage
                return 13;
            case 51146://smoke blitz
                return 14;
            case 51159://smoke burst
                return 15;
            case 51133://smoke rush
                return 16;

            case 7038://strikes
                return 17;//the 17th sprite in that array
            case 7039://strikes
                return 18;
            case 7040://strikes
                return 19;
            case 7041://strikes
                return 20;

            case 7042: //bolts
                return 21;
            case 7043:
                return 22;
            case 7044:
                return 23;
            case 7045:
                return 24;

            case 7046: //blasts
                return 25;
            case 7047:
                return 26;
            case 7048:
                return 27;
            case 7049:
                return 28;

            case 7050: //waves
                return 29;
            case 7051:
                return 30;
            case 7052:
                return 31;
            case 7053:
                return 32;

            case 167236://surges
                return 33;
            case 167237:
                return 34;
            case 167238:
                return 35;
            case 167239:
                return 36;
            default:
                return 0;
        }
    }
    public String getSpellName(int id) {
        switch (id) {
            case 1:
                return "Air\\nStrike";
            case 2:
                return "Water\\nStrike";
            case 3:
                return "Earth\\nStrike";
            case 4:
                return "Fire\\nStrike";
            case 5:
                return "Wind\\nBolt";
            case 6:
                return "Water\\nBolt";
            case 7:
                return "Earth\\nBolt";
            case 8:
                return "Fire\\nBolt";
            case 9:
                return "Water\\nBlast";
            case 10:
                return "Water\\nBlast";
            case 11:
                return "Earth\\nBlast";
            case 12:
                return "Fire\\nBlast";
            case 13:
                return "Wind\\nWave";
            case 14:
                return "Water\\nWave";
            case 15:
                return "Earth\\nWave";
            case 16:
                return "Fire\\nWave";
            case 33:
                return "Smoke Rush";
            case 34:
                return "Shadow Rush";
            case 35:
                return "Blood Rush";
            case 36:
                return "Ice Rush";
            case 37:
                return "Smoke Burst";
            case 38:
                return "Shadow Burst";
            case 39:
                return "Blood Burst";
            case 40:
                return "Ice Burst";
            case 41:
                return "Smoke Blitz";
            case 42:
                return "Shadow Blitz";
            case 43:
                return "Blood Blitz";
            case 44:
                return "Ice Blitz";
            case 45:
                return "Smoke Barrage";
            case 46:
                return "Shadow Barrage";
            case 47:
                return "Blood Barrage";
            case 48:
                return "Ice Barrage";
            case 97:
                return "Wind\\nSurge";
            case 98:
                return "Water Surge";
            case 99:
                return "Earth Surge";
            case 100:
                return "Fire Surge";
            default:
                return "Spell";
        }
    }
    public int getLocalX() {
        return getX() - 8 * getMapRegionX();
    }

    public int getLocalY() {
        return getY() - 8 * getMapRegionY();
    }

    public boolean fullVoidRange() {
        if (getItems().isWearingItem(11664) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839) && getItems().isWearingItem(8842)) {
            return true;
        }
        return getItems().isWearingItem(11664) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072) && getItems().isWearingItem(8842);
    }

    public boolean fullEliteVoidRange() {
        return getItems().isWearingItem(11664) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072) && getItems().isWearingItem(8842);
    }

    public boolean fullEliteVoidMage() {
        return getItems().isWearingItem(11663) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072) && getItems().isWearingItem(8842);
    }

    public boolean fullVoidMage() {
        // return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] ==
        // 8840 || playerEquipment[playerLegs] == 13073 && playerEquipment[playerChest]
        // == 8839
        // || playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] ==
        // 8842;
        if (getItems().isWearingItem(11663) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839) && getItems().isWearingItem(8842)) {
            return true;
        }
        return getItems().isWearingItem(11663) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072) && getItems().isWearingItem(8842);
    }

    public boolean fullVoidMelee() {
        if (getItems().isWearingItem(11665) && getItems().isWearingItem(8840) && getItems().isWearingItem(8839) && getItems().isWearingItem(8842)) {
            return true;
        }
        return getItems().isWearingItem(11665) && getItems().isWearingItem(13073) && getItems().isWearingItem(13072) && getItems().isWearingItem(8842);
    }

    public boolean maxRequirements(Player c) {
        int amount = 0;
        for (int i = 0; i <= 20; i++) {
            if (getLevelForXP(c.playerXP[i]) >= 99) {
                amount++;
            }
            if (amount == 21) {
                return true;
            }
        }
        return false;
    }

    public boolean maxedCertain(Player c, int min, int max) {
        int amount = 0;
        int total = min + max;
        for (int i = min; i <= max; i++) {
            if (getLevelForXP(c.playerXP[i]) >= 99) {
                amount++;
            }
            if (amount == total) {
                return true;
            }
        }
        return false;
    }

    public boolean maxedSkiller(Player c) {
        int amount = 0;
        for (int id = 0; id <= 7; id++) {
            if (getLevelForXP(c.playerXP[id]) >= 2 && id != 3) {
                amount++;
            }
        }
        for (int i = 8; i <= 22; i++) {
            if (c.playerLevel[i] >= 99) {
                amount++;
            }
        }
        return amount == 15;
    }

    public void updateshop(int i) {
        Player p = PlayerHandler.players[getIndex()];
        p.getShops().resetShop(i);
    }

    public void println_debug(String str) {
        System.err.println("[player-" + getIndex() + "][User: " + getLoginName() + "]: " + str);
    }

    public boolean withinDistance(Player otherPlr) {
        if (heightLevel != otherPlr.heightLevel) return false;
        int deltaX = otherPlr.absX - absX;
        int deltaY = otherPlr.absY - absY;
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public boolean withinDistance(NPC npc) {
        if (heightLevel != npc.heightLevel) return false;
        if (npc.needRespawn) return false;
        int deltaX = npc.absX - absX;
        int deltaY = npc.absY - absY;
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public int getHeightLevel() {
        return heightLevel;
    }

    public int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
    }

    @Override
    public void resetWalkingQueue() {
        wQueueReadPtr = wQueueWritePtr = 0;
        for (int i = 0; i < walkingQueueSize; i++) {
            walkingQueueX[i] = currentX;
            walkingQueueY[i] = currentY;
        }
    }

    public void addToWalkingQueue(int x, int y) {
        int next = (wQueueWritePtr + 1) % walkingQueueSize;
        if (next == wQueueWritePtr) return;
        walkingQueueX[wQueueWritePtr] = x;
        walkingQueueY[wQueueWritePtr] = y;
        wQueueWritePtr = next;
    }

    public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        return Misc.goodDistance(objectX, objectY, playerX, playerY, distance);
    }

    public int otherDirection;
    public boolean invincible;

    public boolean isWalkingQueueEmpty() {
        return wQueueReadPtr == wQueueWritePtr || Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]) == -1;
    }

    public int getNextWalkingDirection() {
        if (wQueueReadPtr == wQueueWritePtr) return -1;
        int dir;
        do {
            dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
            if (dir == -1 && otherDirection != dir) {
                otherDirection = dir;
            }
            if (dir == -1) {
                wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
            } else if ((dir & 1) != 0) {
                println_debug("Invalid waypoint in walking queue!");
                resetWalkingQueue();
                return -1;
            }
        } while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
        if (dir == -1) {
            return -1;
        }
        dir >>= 1;
        lastX = absX;
        lastY = absY;
        currentX += Misc.directionDeltaX[dir];
        currentY += Misc.directionDeltaY[dir];
//
        this.getRegionProvider().removeNpcClipping(RegionProvider.NPC_TILE_FLAG, absX, absY, this.getHeight());
        absX += Misc.directionDeltaX[dir];
        absY += Misc.directionDeltaY[dir];

        if (this.getRegionProvider().isOccupiedByNpc(absX, absY, this.getHeight())) {
            this.getRegionProvider().removeNpcClipping(RegionProvider.NPC_TILE_FLAG, absX, absY, this.getHeight());
        } else {
            this.getRegionProvider().addNpcClipping(RegionProvider.NPC_TILE_FLAG, absX, absY, this.getHeight());
        }

        updateController();
        return dir;
    }

    public boolean isRunning() {
        return isNewWalkCmdIsRunning() || runDirection > -1;
    }

    public void stopMovement() {
        resetWalkingQueue();
    }

    public void preProcessing() {
        newWalkCmdSteps = 0;
    }

    public void postProcessing() {
        if (newWalkCmdSteps > 0) {
            int firstX = getNewWalkCmdX()[0];
            int firstY = getNewWalkCmdY()[0];
            int lastDir = 0;
            boolean found = false;
            numTravelBackSteps = 0;
            int ptr = wQueueReadPtr;
            int dir = Misc.direction(currentX, currentY, firstX, firstY);
            if (dir != -1 && (dir & 1) != 0) {
                do {
                    lastDir = dir;
                    if (--ptr < 0) ptr = walkingQueueSize - 1;
                    travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
                    travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
                    dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
                    if (lastDir != dir) {
                        found = true;
                        break;
                    }
                } while (ptr != wQueueWritePtr);
            } else found = true;
            if (!found) println_debug("Fatal: couldn\'t find connection vertex! Dropping packet.");
            else {
                wQueueWritePtr = wQueueReadPtr;
                addToWalkingQueue(currentX, currentY);
                if (dir != -1 && (dir & 1) != 0) {
                    for (int i = 0; i < numTravelBackSteps - 1; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }
                    int wayPointX2 = travelBackX[numTravelBackSteps - 1];
                    int wayPointY2 = travelBackY[numTravelBackSteps - 1];
                    int wayPointX1;
                    int wayPointY1;
                    if (numTravelBackSteps == 1) {
                        wayPointX1 = currentX;
                        wayPointY1 = currentY;
                    } else {
                        wayPointX1 = travelBackX[numTravelBackSteps - 2];
                        wayPointY1 = travelBackY[numTravelBackSteps - 2];
                    }
                    dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
                    if (dir == -1 || (dir & 1) != 0) {
                        println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1 + "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
                    } else {
                        dir >>= 1;
                        found = false;
                        int x = wayPointX1;
                        int y = wayPointY1;
                        while (x != wayPointX2 || y != wayPointY2) {
                            x += Misc.directionDeltaX[dir];
                            y += Misc.directionDeltaY[dir];
                            if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=(" + wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2 + "), " + "first=(" + firstX + ", " + firstY + ")");
                        } else addToWalkingQueue(wayPointX1, wayPointY1);
                    }
                } else {
                    for (int i = 0; i < numTravelBackSteps; i++) {
                        addToWalkingQueue(travelBackX[i], travelBackY[i]);
                    }
                }
                for (int i = 0; i < newWalkCmdSteps; i++) {
                    addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
                }
            }
        }
    }

    public void getNextPlayerMovement() {
        mapRegionDidChange = false;
        didTeleport = false;
        walkDirection = runDirection = -1;
        if (getTeleportToX() != -1 && getTeleportToY() != -1) {
            mapRegionDidChange = true;
            if (mapRegionX != -1 && mapRegionY != -1) {
                int relX = getTeleportToX() - mapRegionX * 8;
                int relY = getTeleportToY() - mapRegionY * 8;
                if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8) mapRegionDidChange = false;
            }
            if (mapRegionDidChange) {
                mapRegionX = (getTeleportToX() >> 3) - 6;
                mapRegionY = (getTeleportToY() >> 3) - 6;
            }
            currentX = getTeleportToX() - 8 * mapRegionX;
            currentY = getTeleportToY() - 8 * mapRegionY;

            this.getRegionProvider().removeNpcClipping(RegionProvider.NPC_TILE_FLAG, absX, absY, heightLevel);
            absX = getTeleportToX();
            absY = getTeleportToY();
            this.getRegionProvider().addNpcClipping(RegionProvider.NPC_TILE_FLAG, absX, absY, heightLevel);
            lastX = absX;
            lastY = absY - 1;
            updateController();
            getFarming().doConfig();

            resetWalkingQueue();
            setTeleportToX(-1);
            setTeleportToY(-1);
            didTeleport = true;
            postTeleportProcessing();
            runningDistanceTravelled = 0;
        } else {
            if (freezeTimer > 0) {
                resetWalkingQueue();
                return;
            }
            walkDirection = getNextWalkingDirection();
            if (walkDirection == -1) {
                runningDistanceTravelled = 0;
                return;
            }
            if(isCrawlingToggled() && getMovementState().isCrawlingEnabled()) {
                crawlDirection = getNextWalkingDirection();
            }
            if (isRunningToggled() && getMovementState().isRunningEnabled()) {
                runDirection = getNextWalkingDirection();
                runningDistanceTravelled++;
            } else {
                runningDistanceTravelled = 0;
            }
            int deltaX = 0;
            int deltaY = 0;
            if (currentX < 2 * 8) {
                deltaX = 4 * 8;
                mapRegionX -= 4;
                mapRegionDidChange = true;
            } else if (currentX >= 11 * 8) {
                deltaX = -4 * 8;
                mapRegionX += 4;
                mapRegionDidChange = true;
            }
            if (currentY < 2 * 8) {
                deltaY = 4 * 8;
                mapRegionY -= 4;
                mapRegionDidChange = true;
            } else if (currentY >= 11 * 8) {
                deltaY = -4 * 8;
                mapRegionY += 4;
                mapRegionDidChange = true;
            }
            if (mapRegionDidChange) {
                currentX += deltaX;
                currentY += deltaY;
                for (int i = 0; i < walkingQueueSize; i++) {
                    walkingQueueX[i] += deltaX;
                    walkingQueueY[i] += deltaY;
                }
            }
        }
        if (firstMove) {
            firstMove = false;
            checkLocationOnLogin();
        }
    }

    public void checkLocationOnLogin() {
        if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
            getPA().movePlayerUnconditionally(2657, 2639, 0);
        }
        if (Boundary.isIn(this, Boundary.FIGHT_CAVE)) {
            getPA().movePlayerUnconditionally(2401, 5087, (getIndex() + 1) * 4);
            sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
            getFightCave().spawn();
        }
        if (Boundary.isIn(this, Boundary.TOKKUL_PIT1)) {
            getPA().movePlayerUnconditionally(2401, 5087, (getIndex() + 1) * 4);
            sendMessage("Wave " + (this.waveId1 + 1) + " will start in approximately 5-10 seconds. ");
            getTokkulPit1().spawn();
        }
        if (Boundary.isIn(this, Boundary.ZULRAH)) {
            getPA().movePlayerUnconditionally(Configuration.EDGEVILLE_X, Configuration.EDGEVILLE_Y, 0);
        }
        for (LobbyType lobbyType : LobbyType.values()) {
            LobbyManager.get(lobbyType).ifPresent(lobby -> {
                if (lobby.inLobby(this)) {
                    if (lobby.canJoin(this)) lobby.attemptJoin(this);
                    else getPA().movePlayerUnconditionally(3033, 6068, 0);//TODO Make this independent for all lobbies
                }
            });
        }
        if (Boundary.isIn(this, Boundary.RAIDS) || Boundary.isIn(this, Boundary.OLM)) {
            RaidConstants.checkLogin(this);
        }
        if (Boundary.isIn(this, Boundary.RAIDS3) || Boundary.isIn(this, Boundary.OLM)) {
            Raids3Constants.checkLogin(this);
        }
    }

    public void postTeleportProcessing() {
        if (getPosition().inGodwars()) {
            if (equippedGodItems == null) {
                updateGodItems();
            }
        } else if (equippedGodItems != null) {
            equippedGodItems = null;
            godwars.initialize();
        }
    }

    public void updateThisPlayerMovement(Stream str) {
        if (didTeleport) {
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            str.writeBits(1, 1);
            str.writeBits(2, 3);
            str.writeBits(2, heightLevel);
            str.writeBits(1, 1);
            str.writeBits(1, (isUpdateRequired()) ? 1 : 0);
            str.writeBits(7, currentY);
            str.writeBits(7, currentX);
            return;
        }
        if (walkDirection == -1) {
            // don't have to update the character position, because we're just
            // standing
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            isMoving = false;
            if (isUpdateRequired()) {
                // tell client there's an update block appended at the end
                str.writeBits(1, 1);
                str.writeBits(2, 0);
            } else {
                str.writeBits(1, 0);
            }
        } else {
            str.createFrameVarSizeWord(81);
            str.initBitAccess();
            str.writeBits(1, 1);
            if (runDirection == -1) {
                isMoving = true;
                str.writeBits(2, 1);
                str.writeBits(3, Misc.xlateDirectionToClient[walkDirection]);
                if (isUpdateRequired()) str.writeBits(1, 1);
                else str.writeBits(1, 0);
            } else {
                isMoving = true;
                str.writeBits(2, 2);
                str.writeBits(1, crawlDirection == -1 ? 1 : 0);
                if(crawlDirection == -1) {
                    str.writeBits(3, Misc.xlateDirectionToClient[walkDirection]);
                    str.writeBits(3, Misc.xlateDirectionToClient[runDirection]);
                } else {
                    str.writeBits(3, Misc.xlateDirectionToClient[crawlDirection]);
                }
                if (isUpdateRequired()) str.writeBits(1, 1);
                else str.writeBits(1, 0);
            }
        }
    }

    public void updatePlayerMovement(Stream str) {
        // synchronized(this) {
        if (walkDirection == -1) {
            if (isUpdateRequired() || isChatTextUpdateRequired()) {
                str.writeBits(1, 1);
                str.writeBits(2, 0);
            } else str.writeBits(1, 0);
        } else if (runDirection == -1 && crawlDirection == -1) {
            str.writeBits(1, 1);
            str.writeBits(2, 1);
            str.writeBits(3, Misc.xlateDirectionToClient[walkDirection]);
            str.writeBits(1, (isUpdateRequired() || isChatTextUpdateRequired()) ? 1 : 0);
        } else {
            str.writeBits(1, 1);
            str.writeBits(2, 2);
            str.writeBits(1, crawlDirection == -1 ? 1 : 0);
            if(crawlDirection == -1) {
                str.writeBits(3, Misc.xlateDirectionToClient[walkDirection]);
                str.writeBits(3, Misc.xlateDirectionToClient[runDirection]);
            } else {
                str.writeBits(3, Misc.xlateDirectionToClient[crawlDirection]);
            }
            str.writeBits(1, (isUpdateRequired() || isChatTextUpdateRequired()) ? 1 : 0);
        }
    }

    public void addNewNPC(NPC npc, Stream str, Stream updateBlock, boolean flag) {
        int id = npc.getIndex();
        npcList[npcListSize++] = npc;
        str.writeBits(14, id);
        int y = npc.absY - absY;
        int x = npc.absX - absX;
        str.writeBits(5, y < 0 ? y + 32 : y);
        str.writeBits(5, x < 0 ? x + 32 : x);
        str.writeBits(1, flag ? 1 : 0);
        str.writeBits(16, npc.getNpcId());
        boolean pet = PetHandler.getItemIdForNpcId(npc.getNpcId()) != 0;
        if (pet && npc.spawnedBy == getIndex()) {
            str.writeBits(2, 2);
        } else if (pet) {
            str.writeBits(2, 1);
        } else {
            str.writeBits(2, 0);
        }
        boolean savedUpdateRequired = npc.isUpdateRequired();
        npc.setUpdateRequired(true);
        npc.appendNPCUpdateBlock(this, updateBlock);
        npc.setUpdateRequired(savedUpdateRequired);
        str.writeBits(1, 1);
    }

    public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
        if (playerListSize >= Configuration.MAX_PLAYERS_IN_LOCAL_LIST) {
            return;
        }
        int id = plr.getIndex();
        playerInListBitmap[id >> 3] |= 1 << (id & 7);
        playerList[playerListSize++] = plr;
        str.writeBits(11, id);
        str.writeBits(1, 1);
        boolean savedFlag = plr.isAppearanceUpdateRequired();
        boolean savedUpdateRequired = plr.isUpdateRequired();
        plr.setAppearanceUpdateRequired(true);
        plr.setUpdateRequired(true);
        plr.appendPlayerUpdateBlock(updateBlock);
        plr.setAppearanceUpdateRequired(savedFlag);
        plr.setUpdateRequired(savedUpdateRequired);
        str.writeBits(1, 1);
        int z = plr.absY - absY;
        if (z < 0) z += 32;
        str.writeBits(5, z);
        z = plr.absX - absX;
        if (z < 0) z += 32;
        str.writeBits(5, z);
    }

    protected void appendPlayerAppearance(Stream str) {
        appearanceUpdateBlockCache.currentOffset = 0;
        appearanceUpdateBlockCache.writeByte(playerAppearance[0]);
        StringBuilder sb = new StringBuilder(titles.getCurrentTitle());
        if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
            sb.delete(0, sb.length());
        }
        appearanceUpdateBlockCache.writeString(sb.toString());
        sb = new StringBuilder(rights.getPrimary().getColor());
        if (titles.getCurrentTitle().equalsIgnoreCase("None")) {
            sb.delete(0, sb.length());
        }
        appearanceUpdateBlockCache.writeString(sb.toString());
        appearanceUpdateBlockCache.writeByte(getHealth().getStatus().getMask());
        appearanceUpdateBlockCache.writeByte(headIcon);
        appearanceUpdateBlockCache.writeByte(headIconPk);
        if (isNpc == false) {
            if (playerEquipment[playerHat] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerHat]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerCape] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerCape]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerAmulet] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerAmulet]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerWeapon] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerWeapon]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerChest] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerChest]);
            } else {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[2]);
            }
            if (playerEquipment[playerShield] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerShield]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (ItemDef.forId(playerEquipment[playerChest]).getEquipmentModelType() != EquipmentModelType.FULL_BODY) {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[3]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerLegs] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerLegs]);
            } else {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[5]);
            }
            if (ItemDef.forId(playerEquipment[playerHat]).getEquipmentModelType() != EquipmentModelType.FULL_MASK && ItemDef.forId(playerEquipment[playerHat]).getEquipmentModelType() != EquipmentModelType.FULL_HELMET) {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[1]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
            if (playerEquipment[playerHands] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerHands]);
            } else {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[4]);
            }
            if (playerEquipment[playerFeet] > 1) {
                appearanceUpdateBlockCache.writeUnsignedWord(512 + playerEquipment[playerFeet]);
            } else {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[6]);
            }
            if (playerAppearance[0] != 1 && ItemDef.forId(playerEquipment[playerHat]).getEquipmentModelType() != EquipmentModelType.FULL_MASK) {
                appearanceUpdateBlockCache.writeUnsignedWord(256 + playerAppearance[7]);
            } else {
                appearanceUpdateBlockCache.writeByte(0);
            }
        } else {
            appearanceUpdateBlockCache.writeUnsignedWord(-1);
            appearanceUpdateBlockCache.writeUnsignedWord(npcId2);
        }
        appearanceUpdateBlockCache.writeByte(playerAppearance[8]);
        appearanceUpdateBlockCache.writeByte(playerAppearance[9]);
        appearanceUpdateBlockCache.writeByte(playerAppearance[10]);
        appearanceUpdateBlockCache.writeByte(playerAppearance[11]);
        appearanceUpdateBlockCache.writeByte(playerAppearance[12]);
        appearanceUpdateBlockCache.writeUnsignedWord(playerStandIndex); // standAnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerTurnIndex); // standTurnAnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerWalkIndex); // walkAnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerTurn180Index); // turn180AnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerTurn90CWIndex); // turn90CWAnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
        appearanceUpdateBlockCache.writeUnsignedWord(playerRunIndex); // runAnimIndex
        appearanceUpdateBlockCache.writeString(getDisplayName());
        appearanceUpdateBlockCache.writeByte(isInvisible() ? 1 : 0);
        combatLevel = calculateCombatLevel();
        appearanceUpdateBlockCache.writeByte(combatLevel); // combat level
        Set<Right> rightsSet = rights.getSet();
        appearanceUpdateBlockCache.writeByte(rightsSet.size());
        for (Right right : rightsSet) {
            appearanceUpdateBlockCache.writeByte(right.ordinal());
        }
        appearanceUpdateBlockCache.writeUnsignedWord(0);
        str.writeByteC(appearanceUpdateBlockCache.currentOffset);
        str.writeBytes(appearanceUpdateBlockCache.buffer, appearanceUpdateBlockCache.currentOffset, 0);
    }

    public int calculateCombatLevel() {
        int j = getLevelForXP(playerXP[playerAttack]);
        int k = getLevelForXP(playerXP[playerDefence]);
        int l = getLevelForXP(playerXP[playerStrength]);
        int i1 = getLevelForXP(playerXP[playerHitpoints]);
        int j1 = getLevelForXP(playerXP[playerPrayer]);
        int k1 = getLevelForXP(playerXP[playerRanged]);
        int l1 = getLevelForXP(playerXP[playerMagic]);
        int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.24798) + 1;
        double d = (j + l) * 0.325;
        double d1 = Math.floor(k1 * 1.5) * 0.325;
        double d2 = Math.floor(l1 * 1.5) * 0.325;
        if (d >= d1 && d >= d2) {
            combatLevel += d;
        } else if (d1 >= d && d1 >= d2) {
            combatLevel += d1;
        } else if (d2 >= d && d2 >= d1) {
            combatLevel += d2;
        }
        return combatLevel;
    }

    /**
     * Permanently set a skill level and update health and reset it to full if applicable.
     */
    public void setLevel(Skill skill, int experience, boolean clientUpdate) {
        playerXP[skill.getId()] = experience;
        playerLevel[skill.getId()] = getLevelForXP(experience);

        if (skill == Skill.HITPOINTS) {
            getHealth().setCurrentHealth(getLevel(Skill.HITPOINTS));
            getHealth().setMaximumHealth(getLevel(Skill.HITPOINTS));
            getHealth().reset();
        }

        if (clientUpdate)
            getPA().refreshSkill(skill.getId());
    }

    public int getLevel(Skill skill) {
        if (skill == Skill.DEFENCE) {
            if (ToragsEffect.INSTANCE.canUseEffect(this)) {
                return (int) ToragsEffect.modifyDefenceLevel(this);
            }
        }

        return playerLevel[skill.getId()];
    }

    public int getExperience(Skill skill) {
        return playerXP[skill.getId()];
    }

    /**
     * Restore a skill level, doesn't go over max.
     */
    public void restore(Skill skill, int amount) {
        playerLevel[skill.getId()] += amount;
        int maxLevel = getLevelForXP(playerXP[skill.getId()]);
        if (playerLevel[skill.getId()] > maxLevel) {
            playerLevel[skill.getId()] = maxLevel;
        }
        getPA().refreshSkill(skill.getId());
    }

    public int getLevelForXP(int exp) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp) return lvl;
        }
        return 99;
    }

    protected void appendPlayerChatText(Stream str) {
        str.writeWordBigEndian(((getChatTextColor() & 255) << 8) + (getChatTextEffects() & 255));
        str.writeByte(rights.getPrimary().getValue());
        str.writeByteC(getChatTextSize());
        str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);
    }

    public void forcedChat(String text) {
        forcedText = text;
        forcedChatUpdateRequired = true;
        setUpdateRequired(true);
        setAppearanceUpdateRequired(true);
    }

    public void appendForcedChat(Stream str) {
        str.writeString(forcedText);
    }

    public void appendMask100Update(Stream str) {
        str.writeWordBigEndian(getGraphic().getId());
        str.writeDWord(getGraphic().getHeight() + (getGraphic().getDelay() & 65535));
    }

    public void gfx100(int gfx) {
        startGraphic(new Graphic(gfx, Graphic.GraphicHeight.MIDDLE));
    }

    public void gfx0(int gfx) {
        startGraphic(new Graphic(gfx, Graphic.GraphicHeight.LOW));
    }

    /**
     * Animations
     */
    public void startAnimation(int animId) {
        startAnimation(new Animation(animId));
    }

    public void startAnimation(int animId, int time) {
        startAnimation(new Animation(animId, time));
    }

    public void stopAnimation() {
        startAnimation(new Animation(65535));
    }

    public void appendAnimationRequest(Stream str) {
        str.writeWordBigEndian((getAnimation() == null || getAnimation().getId() == -1) ? 65535 : getAnimation().getId());
        str.writeByteC(getAnimation().getDelay());
    }

    public void faceEntity(Entity entity) {
        faceUpdate(entity.getIndex() + (entity.isPlayer() ? 32768 : 0));
    }

    public void faceUpdate(int index) {
        face = index;
        faceUpdateRequired = true;
        setUpdateRequired(true);
    }

    public void appendFaceUpdate(Stream str) {
        str.writeWordBigEndian(face);
    }

    public void facePosition(Position position) {
        facePosition(position.getX(), position.getY());
    }

    public void facePosition(int pointX, int pointY) {
        FocusPointX = 2 * pointX + 1;
        FocusPointY = 2 * pointY + 1;
        setUpdateRequired(true);
    }

    private void appendSetFocusDestination(Stream str) {
        // synchronized(this) {
        str.writeWordBigEndianA(FocusPointX);
        str.writeWordBigEndian(FocusPointY);
    }

    @Override
    public boolean isFreezable() {
        return true;
    }

    @Override
    public void appendHeal(int amount, Hitmark h) {
        if (teleTimer <= 0) {
            if (!invincible) {
                getHealth().increase(amount);
            }
            if (amount > 0 && h != null && h == Hitmark.MISS) {
                h = Hitmark.HIT;
            }
            if (!hitUpdateRequired) {
                hitUpdateRequired = true;
                hitDiff = amount;
                hitmark1 = h;
            } else if (!hitUpdateRequired2) {
                hitUpdateRequired2 = true;
                hitDiff2 = amount;
                hitmark2 = h;
            }
        } else {
            if (hitUpdateRequired) {
                hitUpdateRequired = false;
            }
            if (hitUpdateRequired2) {
                hitUpdateRequired2 = false;
            }
        }
        setUpdateRequired(true);
    }

    @Override
    public void appendDamage(Entity entity, int damage, Hitmark h) {
        // Attempting a fix to dying after teleport here.
        if (entity != null && distance(entity.getPosition()) > 36) {
            return;
        }

        // Converts all hits to 0, but processes effects

        if (getAttributes().getBoolean("GODMODE")) {
            damage = 0;
        }

        // Fix for being killed inside Theatre of Blood cage after death at final boss
        if (getAttributes().getBoolean(TobInstance.TOB_DEAD_ATTR_KEY, false)) {
            if (Boundary.isIn(this, TobConstants.ALL_BOUNDARIES) && getInstance() != null) {
                return;
            }

            getAttributes().removeBoolean(TobInstance.TOB_DEAD_ATTR_KEY); // Remove cause not in TOB anymore
        }

        // Fix for being killed inside Theatre of Blood cage after death at final boss
        if (getAttributes().getBoolean(TombsOfAmascutInstance.TOMBS_OF_AMASCUT_DEAD_ATTR_KEY, false)) {
            if (Boundary.isIn(this, TombsOfAmascutConstants.ALL_BOUNDARIES1) && getInstance() != null) {
                return;
            }

            getAttributes().removeBoolean(TombsOfAmascutInstance.TOMBS_OF_AMASCUT_DEAD_ATTR_KEY); // Remove cause not in TOB anymore
        }
        // Degrade items when hitting by normal hits
        if (h == Hitmark.HIT || h == Hitmark.MISS) {
            Degrade.degradeDefending(this);
        }

        if (damage < 0) {
            damage = 0;
            h = Hitmark.MISS;
        }
        if (getHealth().getCurrentHealth() - damage < 0) {
            damage = getHealth().getCurrentHealth();
        }


        MeleeExtras.handleRedemption(this, damage);

        if (teleTimer <= 0) {
            if (!invincible) {
                getHealth().reduce(damage);
            }
            if (damage > 0 && h != null && h == Hitmark.MISS) {
                h = Hitmark.HIT;
            }
            if (!hitUpdateRequired) {
                hitUpdateRequired = true;
                hitDiff = damage;
                hitmark1 = h;
            } else if (!hitUpdateRequired2) {
                hitUpdateRequired2 = true;
                hitDiff2 = damage;
                hitmark2 = h;
            }
            lastDamageTaken = damage;
        } else {
            if (hitUpdateRequired) {
                hitUpdateRequired = false;
            }
            if (hitUpdateRequired2) {
                hitUpdateRequired2 = false;
            }
        }
        setUpdateRequired(true);
    }

    @Override
    protected void appendHitUpdate(Stream str) {
        str.writeByte(hitDiff);
        if (hitmark1 == null) {
            str.writeByteA(0);
        } else {
            str.writeByteA(hitmark1.getId());
        }
        if (getHealth().getCurrentHealth() <= 0) {
            isDead = true;
        }
        str.writeByteC(getHealth().getCurrentHealth());
        str.writeByte(getHealth().getMaximumHealth());
    }

    @Override
    protected void appendHitUpdate2(Stream str) {
        str.writeByte(hitDiff2);
        if (hitmark2 == null) {
            str.writeByteS(0);
        } else {
            str.writeByteS(hitmark2.getId());
        }
        if (getHealth().getCurrentHealth() <= 0) {
            isDead = true;
        }
        str.writeByte(getHealth().getCurrentHealth());
        str.writeByteC(getHealth().getMaximumHealth());
    }

    /**
     * Direction, 2 = South, 0 = North, 3 = West, 2 = East?
     *
     * @param xOffset
     * @param yOffset
     * @param speed1
     * @param speed2
     * @param direction
     * @param emote
     */
    private int xOffsetWalk;
    private int yOffsetWalk;
    public int dropSize;
    public boolean sellingX;
    public int currentPrestigeLevel;
    public int prestigeNumber;
    public boolean canPrestige;
    public int prestigePoints;
    public boolean newStarter;
    public boolean spawnedbarrows;
    public boolean absorption = true;
    public boolean announce = true;
    public boolean lootPickUp;
    public boolean xpScroll;
    public boolean ddScroll;
    public long xpScrollTicks;
    public long ddScrollTicks;
    public boolean skillingPetRateScroll;
    public long skillingPetRateTicks;
    public boolean fasterCluesScroll;
    public long fasterCluesTicks;
    public boolean augury;
    public boolean rigour;
    public boolean usedFc;
    public int startDate = -1;
    public int LastLoginYear;
    public int LastLoginMonth;
    public int LastLoginDate;
    public int LoginStreak;
    /*
     * diary completion
     */
    public boolean d1Complete;
    public boolean d2Complete;
    public boolean d3Complete;
    public boolean d4Complete;
    public boolean d5Complete;
    public boolean d6Complete;
    public boolean d7Complete;
    public boolean d8Complete;
    public boolean d9Complete;
    public boolean d10Complete;
    public boolean d11Complete;

    /**
     * 0 North 1 East 2 South 3 West
     */
    public void setForceMovement(int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet, int animation) {
        if (isForceMovementActive() || forceMovement) {
            return;
        }
        stopMovement();
        xOffsetWalk = xOffset - absX;
        yOffsetWalk = yOffset - absY;
        playerStandIndex = animation;
        playerRunIndex = animation;
        playerWalkIndex = animation;
        forceMovementActive = true;
        getPA().requestUpdates();
        setAppearanceUpdateRequired(true);
        Server.getEventHandler().submit(new Event<Player>("force_movement", this, 2) {
            @Override
            public void execute() {
                if (attachment == null || attachment.isDisconnected()) {
                    super.stop();
                    return;
                }
                attachment.setUpdateRequired(true);
                attachment.forceMovement = true;
                attachment.x1 = currentX;
                attachment.y1 = currentY;
                attachment.x2 = currentX + xOffsetWalk;
                attachment.y2 = currentY + yOffsetWalk;
                attachment.mask400Var1 = speedOne;
                attachment.mask400Var2 = speedTwo;
                attachment.forceMovementDirection = directionSet == null ? -1 : directionSet == "NORTH" ? 0 : directionSet == "EAST" ? 1 : directionSet == "SOUTH" ? 2 : directionSet == "WEST" ? 3 : 0;
                super.stop();
            }
        });
        int ticks = Math.abs(xOffsetWalk) + Math.abs(yOffsetWalk);
        if (ticks <= 0) ticks = 1;
        Server.getEventHandler().submit(new Event<Player>("force_movement", this, ticks) {
            @Override
            public void execute() {
                if (attachment == null || attachment.isDisconnected()) {
                    super.stop();
                    return;
                }
                forceMovementActive = false;
                attachment.getPA().movePlayer(xOffset, yOffset, attachment.heightLevel);
                if (attachment.playerEquipment[playerWeapon] == -1) {
                    attachment.playerStandIndex = 808;
                    attachment.playerTurnIndex = 823;
                    attachment.playerWalkIndex = 819;
                    attachment.playerTurn180Index = 820;
                    attachment.playerTurn90CWIndex = 821;
                    attachment.playerTurn90CCWIndex = 822;
                    attachment.playerRunIndex = 824;
                } else {
                    MeleeData.setWeaponAnimations(attachment);
                }
                forceMovement = false;
                super.stop();
            }
        });
    }

    public void appendMask400Update(Stream str) {
        str.writeByteS(x1);
        str.writeByteS(y1);
        str.writeByteS(x2);
        str.writeByteS(y2);
        str.writeWordBigEndianA(mask400Var1);
        str.writeWordA(mask400Var2);
        str.writeByteS(forceMovementDirection);
    }

    public void appendPlayerUpdateBlock(Stream str) {
        if (!isUpdateRequired() && !isChatTextUpdateRequired()) return;
        int updateMask = 0;
        if (forceMovement) {
            updateMask |= 1024;
        }
        if (isGfxUpdateRequired()) {
            updateMask |= 256;
        }
        if (isAnimationUpdateRequired()) {
            updateMask |= 8;
        }
        if (forcedChatUpdateRequired) {
            updateMask |= 4;
        }
        if (isChatTextUpdateRequired()) {
            updateMask |= 128;
        }
        if (isAppearanceUpdateRequired()) {
            updateMask |= 16;
        }
        if (faceUpdateRequired) {
            updateMask |= 1;
        }
        if (FocusPointX != -1) {
            updateMask |= 2;
        }
        if (hitUpdateRequired) {
            updateMask |= 32;
        }
        if (hitUpdateRequired2) {
            updateMask |= 512;
        }
        if (updateMask >= 256) {
            updateMask |= 64;
            str.writeByte(updateMask & 255);
            str.writeByte(updateMask >> 8);
        } else {
            str.writeByte(updateMask);
        }
        if (forceMovement) {
            appendMask400Update(str);
        }
        if (isGfxUpdateRequired()) {
            appendMask100Update(str);
        }
        if (isAnimationUpdateRequired()) {
            appendAnimationRequest(str);
        }
        if (forcedChatUpdateRequired) {
            appendForcedChat(str);
        }
        if (isChatTextUpdateRequired()) {
            appendPlayerChatText(str);
        }
        if (faceUpdateRequired) {
            appendFaceUpdate(str);
        }
        if (isAppearanceUpdateRequired()) {
            appendPlayerAppearance(str);
        }
        if (FocusPointX != -1) {
            appendSetFocusDestination(str);
        }
        if (hitUpdateRequired) {
            appendHitUpdate(str);
        }
        if (hitUpdateRequired2) {
            appendHitUpdate2(str);
        }
    }

    public void clearUpdateFlags() {
        setUpdateRequired(false);
        setChatTextUpdateRequired(false);
        setAppearanceUpdateRequired(false);
        hitUpdateRequired = false;
        hitUpdateRequired2 = false;
        forcedChatUpdateRequired = false;
        FocusPointX = -1;
        FocusPointY = -1;
        faceUpdateRequired = false;
        forceMovement = false;
        face = 65535;
        resetAfterUpdate();
    }

    public long lastWebSlash;

    public int getPacketsReceived() {
        return packetsReceived.get();
    }

    public int getMapRegionX() {
        return mapRegionX;
    }

    public int getMapRegionY() {
        return mapRegionY;
    }

    public int getX() {
        return absX;
    }

    @Override
    public void setX(int x) {
        this.absX = x;
        updateController();
    }

    public int getY() {
        return absY;
    }

    @Override
    public void setY(int y) {
        this.absY = y;
        updateController();
    }

    public int getHeight() {
        return this.heightLevel;
    }

    @Override
    public void setHeight(int height) {
        this.heightLevel = heightLevel;
    }

    @Override
    public int getDefenceLevel() {
        return getLevel(Skill.DEFENCE);
    }

    @Override
    public int getDefenceBonus(CombatType combatType, Entity attacker) {
        if (combatType == CombatType.RANGE) {
            return getItems().getBonus(Bonus.DEFENCE_RANGED);
        } else if (combatType == CombatType.MAGE) {
            return getItems().getBonus(Bonus.DEFENCE_MAGIC);
        } else if (combatType == CombatType.MELEE && attacker.isPlayer()) {
            WeaponMode weaponMode = attacker.asPlayer().getCombatConfigs().getWeaponMode();

            CombatStyle style = weaponMode.getCombatStyle();

            if (style == null) {
                System.err.println("No melee weapon style for: " + weaponMode);
                return getItems().getBonus(Bonus.DEFENCE_SLASH);
            }
            switch (style) {
                case STAB:
                    return getItems().getBonus(Bonus.DEFENCE_STAB);
                case SLASH:
                    return getItems().getBonus(Bonus.DEFENCE_SLASH);
                case CRUSH:
                    return getItems().getBonus(Bonus.DEFENCE_CRUSH);
            }
        }
        return playerBonus[MeleeMaxHit.bestMeleeDef(this)];
    }

    @Override
    public boolean hasBlockAnimation() {
        return true;
    }

    @Override
    public Animation getBlockAnimation() {
        return new Animation(MeleeData.getBlockEmote(this));
    }

    @Override
    public boolean isAutoRetaliate() {
        return autoRet == 1 && playerAttackingIndex == 0 && npcAttackingIndex == 0 && isWalkingQueueEmpty();
    }

    public void unfollow() {
        getPA().resetFollow();
        faceUpdate(0);
    }

    @Override
    public void attackEntity(Entity entity) {
        combatFollowing = true;
        if (entity.isPlayer()) {
            playerAttackingIndex = entity.getIndex();
            playerFollowingIndex = entity.getIndex();
            npcAttackingIndex = 0;
            npcFollowingIndex = 0;
        } else {
            npcAttackingIndex = entity.getIndex();
            npcFollowingIndex = entity.getIndex();
            playerAttackingIndex = 0;
            playerFollowingIndex = 0;
        }
    }

    public boolean isTeleblocked() {
        return System.currentTimeMillis() - teleBlockStartMillis < teleBlockLength;
    }

    public Coordinate getCoordinate() {
        return new Coordinate(absX, absY, heightLevel);
    }

    public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
        this.appearanceUpdateRequired = appearanceUpdateRequired;
    }

    public boolean isAppearanceUpdateRequired() {
        return appearanceUpdateRequired;
    }

    public void setChatTextEffects(int chatTextEffects) {
        this.chatTextEffects = chatTextEffects;
    }

    public int getChatTextEffects() {
        return chatTextEffects;
    }

    public void setChatTextSize(byte chatTextSize) {
        this.chatTextSize = chatTextSize;
    }

    public byte getChatTextSize() {
        return chatTextSize;
    }

    public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
        this.chatTextUpdateRequired = chatTextUpdateRequired;
    }

    public boolean isChatTextUpdateRequired() {
        return chatTextUpdateRequired;
    }

    public byte[] getChatText() {
        return chatText;
    }

    public void setChatText(byte[] chatText) {
        this.chatText = chatText;
    }

    public void setChatTextColor(int chatTextColor) {
        this.chatTextColor = chatTextColor;
    }

    public int getChatTextColor() {
        return chatTextColor;
    }

    public int[] getNewWalkCmdX() {
        return newWalkCmdX;
    }

    public int[] getNewWalkCmdY() {
        return newWalkCmdY;
    }

    public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
        this.newWalkCmdIsRunning = newWalkCmdIsRunning;
    }

    public boolean isNewWalkCmdIsRunning() {
        return newWalkCmdIsRunning;
    }

    public boolean getRingOfLifeEffect() {
        return maxCape[0];
    }

    public boolean setRingOfLifeEffect(boolean effect) {
        return maxCape[0] = effect;
    }

    public boolean getFishingEffect() {
        return maxCape[1];
    }

    public boolean setFishingEffect(boolean effect) {
        return maxCape[1] = effect;
    }

    public boolean getMiningEffect() {
        return maxCape[2];
    }

    public boolean setMiningEffect(boolean effect) {
        return maxCape[2] = effect;
    }

    public boolean getWoodcuttingEffect() {
        return maxCape[3];
    }

    public boolean setWoodcuttingEffect(boolean effect) {
        return maxCape[3] = effect;
    }

    public int getSkeletalMysticDamageCounter() {
        return raidsDamageCounters[0];
    }

    public int getNexAngelOfDeathDamageCounter() { return raidsDamageCounters[15]; }

    public void setNexAngelOfDeathDamageCounter(int damage) {
        this.raidsDamageCounters[15] = damage;
    }

    public void setSkeletalMysticDamageCounter(int damage) {
        this.raidsDamageCounters[0] = damage;
    }
    public int getSkeletalMysticDamageCounter1() {
        return raids3DamageCounters[0];
    }

    public void setSkeletalMysticDamageCounter1(int damage) {
        this.raids3DamageCounters[0] = damage;
    }
    public int getTektonDamageCounter() {
        return raidsDamageCounters[1];
    }

    public void setTektonDamageCounter(int damage) {
        this.raidsDamageCounters[1] = damage;
    }
    public int getTektonDamageCounter1() {
        return raids3DamageCounters[1];
    }

    public void setTektonDamageCounter1(int damage) {
        this.raids3DamageCounters[1] = damage;
    }
    public int getGlodDamageCounter() {
        return raidsDamageCounters[9];
    }

    public void setGlodDamageCounter(int damage) {
        this.raidsDamageCounters[9] = damage;
    }

    public int getAvatarOfCreationDamageCounter() {
        return raidsDamageCounters[12];
    }

    public void setAvatarOfCreationDamageCounter(int damage) {
        this.raidsDamageCounters[12] = damage;
    }

    public int getIceQueenDamageCounter() {
        return raidsDamageCounters[4];
    }

    public void setIceQueenDamageCounter(int damage) {
        this.raidsDamageCounters[4] = damage;
    }


    public int getEasyClueCounter() {
        return counters[0];
    }

    public void setEasyClueCounter(int counters) {
        this.counters[0] = counters;
    }

    public int getMediumClueCounter() {
        return counters[1];
    }

    public void setMediumClueCounter(int counters) {
        this.counters[1] = counters;
    }

    public int getHardClueCounter() {
        return counters[2];
    }

    public void setHardClueCounter(int counters) {
        this.counters[2] = counters;
    }

    public int getMasterClueCounter() {
        return counters[3];
    }

    public void setMasterClueCounter(int counters) {
        this.counters[3] = counters;
    }

    public int getTealAodChestCounter() {
        return counters[3];
    }

    public void setTealAodChestCounter(int counters) {
        this.counters[3] = counters;
    }

    public int getRedAodChestCounter() {
        return counters[8];
    }

    public void setRedAodChestCounter(int counters) {
        this.counters[8] = counters;
    }

    public int getBlackAodChestCounter() {
        return counters[9];
    }

    public void setBlackAodChestCounter(int counters) {
        this.counters[9] = counters;
    }

    public int getAodChestCounter() {
        return counters[10];
    }

    public void setAodChestCounter(int counters) {
        this.counters[10] = counters;
    }

    public int getBarrowsChestCounter() {
        return counters[4];
    }

    public void setBarrowsChestCounter(int counters) {
        this.counters[4] = counters;
    }

    public int getDuelWinsCounter() {
        return counters[5];
    }

    public void setDuelWinsCounter(int counters) {
        this.counters[5] = counters;
    }

    public int getDuelLossCounter() {
        return counters[6];
    }

    public void setDuelLossCounter(int counters) {
        this.counters[6] = counters;
    }

    public String getLastClanChat() {
        return lastClanChat;
    }

    public void setLastClanChat(String founder) {
        lastClanChat = founder;
    }

    public long getNameAsLong() {
        return nameAsLong;
    }

    public void setNameAsLong(long hash) {
        this.nameAsLong = hash;
    }

    public void setStopPlayer(boolean stopPlayer) {
    }

    public boolean isDead() {
        return getHealth().getCurrentHealth() <= 0 || this.isDead;
    }

    public void setTrading(boolean trading) {
    }

    public boolean inGodmode() {
        return godmode;
    }

    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }

    public boolean inSafemode() {
        return safemode;
    }

    public void setSafemode(boolean safemode) {
        this.safemode = safemode;
    }

    public void setDragonfireShieldCharge(int charge) {
        this.dragonfireShieldCharge = charge;
    }

    public int getDragonfireShieldCharge() {
        return dragonfireShieldCharge;
    }

    public void setLastDragonfireShieldAttack(long lastAttack) {
        this.lastDragonfireShieldAttack = lastAttack;
    }

    public long getLastDragonfireShieldAttack() {
        return lastDragonfireShieldAttack;
    }

    /**
     * Retrieves the rights for this player.
     *
     * @return the rights
     */
    public RightGroup getRights() {
        if (rights == null) {
            rights = new RightGroup(this, Right.PLAYER);
        }
        return rights;
    }

    /**
     * Returns a single instance of the Titles class for this player
     *
     * @return the titles class
     */
    public Titles getTitles() {
        if (titles == null) {
            titles = new Titles(this);
        }
        return titles;
    }

    public RandomEventInterface getInterfaceEvent() {
        return randomEventInterface;
    }

    public UltraMysteryBox getUltraInterface() {
        return ultraMysteryBox;
    }

    public FoeMysteryBox getFoeInterface() {
        return foeMysteryBox;
    }

    public NormalMysteryBox getNormalBoxInterface() {
        return normalMysteryBox;
    }

    public SuperMysteryBox getSuperBoxInterface() {
        return superMysteryBox;
    }
    public BlackAodLootChest getBlackAodInterface() {
        return blackAodLootChest;
    }
    /**
     * Modifies the current interface open
     *
     * @param openInterface the interface id
     */
    public void setOpenInterface(int openInterface) {
        this.openInterface = openInterface;
    }

    /**
     * The interface that is opened
     *
     * @return the interface id
     */
    public int getOpenInterface() {
        return openInterface;
    }

    /**
     * Determines whether a warning will be shown when dropping an item.
     *
     * @return True if it's the case, False otherwise.
     */
    public boolean showDropWarning() {
        return dropWarning;
    }

    /**
     * Change whether a warning will be shown when dropping items.
     *
     * @param shown True in case a warning must be shown, False otherwise.
     */
    public void setDropWarning(boolean shown) {
        dropWarning = shown;
    }

    public boolean isAlchWarning() {
        return alchWarning;
    }

    public void setAlchWarning(boolean alchWarning) {
        this.alchWarning = alchWarning;
    }

    public boolean getHourlyBoxToggle() {
        return hourlyBoxToggle;
    }

    public void setHourlyBoxToggle(boolean toggle) {
        hourlyBoxToggle = toggle;
    }

    public boolean getFracturedCrystalToggle() {
        return fracturedCrystalToggle;
    }

    public void setFracturedCrystalToggle(boolean toggle1) {
        fracturedCrystalToggle = toggle1;
    }

    public long setBestZulrahTime(long bestZulrahTime) {
        return this.bestZulrahTime = bestZulrahTime;
    }

    public long getBestZulrahTime() {
        return bestZulrahTime;
    }

    public int getArcLightCharge() {
        return arcLightCharge;
    }

    public void setArcLightCharge(int chargeArc) {
        this.arcLightCharge = chargeArc;
    }

    public int getToxicBlowpipeCharge() {
        return toxicBlowpipeCharge;
    }

    public void setToxicBlowpipeCharge(int charge) {
        this.toxicBlowpipeCharge = charge;
    }

    public int getToxicBlowpipeAmmo() {
        return toxicBlowpipeAmmo;
    }

    public void increaseSlaughterCharge(int slaughterCharge) {
        this.slaughterCharge += slaughterCharge;
    }

    public void decreaseSlaughterCharge(int slaughterCharge) {
        this.slaughterCharge -= slaughterCharge;
    }

    public int getToxicBlowpipeAmmoAmount() {
        return toxicBlowpipeAmmoAmount;
    }

    public void setToxicBlowpipeAmmoAmount(int amount) {
        this.toxicBlowpipeAmmoAmount = amount;
    }

    public void setToxicBlowpipeAmmo(int ammo) {
        this.toxicBlowpipeAmmo = ammo;
    }

    public int getSerpentineHelmCharge() {
        return this.serpentineHelmCharge;
    }

    public void setSerpentineHelmCharge(int charge) {
        this.serpentineHelmCharge = charge;
    }

    public int getTridentCharge() {
        return tridentCharge;
    }

    public void setTridentCharge(int tridentCharge) {
        this.tridentCharge = tridentCharge;
    }

    public int getToxicTridentCharge() {
        return toxicTridentCharge;
    }

    public void setToxicTridentCharge(int toxicTridentCharge) {
        this.toxicTridentCharge = toxicTridentCharge;
    }

    public int getSangStaffCharge() {return sangStaffCharge;}

    public void setSangStaffCharge(int sangStaffCharge) {
        this.sangStaffCharge = sangStaffCharge;
    }

    public int getAccursedCharge() {
        return accursedCharge;
    }

    public void setAccursedCharge(int accursedCharge) {
        this.accursedCharge = accursedCharge;
    }

    public int getTumekensShadowCharge() { return TumekensShadowCharge; }

    public void setTumekensShadowCharge(int TumekensShadowCharge) { this.TumekensShadowCharge = TumekensShadowCharge; }


    public Fletching getFletching() {
        return fletching;
    }

    public Mode getMode() {
        return mode;
    }
    public boolean SigilOfProsperity;
    public long SigilOfProsperityDrop;
    public Mode setMode(Mode mode) {
        return this.mode = mode;
    }

    public String getRevertOption() {
        return revertOption;
    }

    public void setRevertOption(String revertOption) {
        this.revertOption = revertOption;
    }

    public long getRevertModeDelay() {
        return revertModeDelay;
    }

    public void setRevertModeDelay(long revertModeDelay) {
        this.revertModeDelay = revertModeDelay;
    }

    /**
     * @param skillId
     * @param amount
     */
    public void replenishSkill(int skillId, int amount) {
        if (skillId < 0 || skillId > playerLevel.length - 1) {
            return;
        }
        int maximum = getLevelForXP(playerXP[skillId]);
        if (playerLevel[skillId] == maximum) {
            return;
        }
        playerLevel[skillId] += amount;
        if (playerLevel[skillId] > maximum) {
            playerLevel[skillId] = maximum;
        }
        playerAssistant.refreshSkill(skillId);
    }

    public int lastSymbolDistanceCheck;

    public List<NPC> derwens_orbs = Lists.newArrayList();

    public int[] activeMageArena2BossId = new int[3];

    public Position[] mageArena2Spawns = null;
    public int[] mageArena2SpawnsX = new int[3];
    public int[] mageArena2SpawnsY = new int[3];
    /**
     * Saved
     */

    public boolean[] mageArenaBossKills = new boolean[3];

    public boolean[] mageArena2Stages = new boolean[5];
public boolean hasSacrificedFcape;
    public int flamesOfZamorakCasts, clawsOfGuthixCasts, saradominStrikeCasts;

    public boolean completedMageArena2() {
        int count = 0;
        for (boolean b : mageArenaBossKills) {
            if (b)
                count++;
        }
        return count >= 3 || mageArena2Stages[1];
    }

    public boolean hasMageArena2BossItem(int bossEnumId) {
        int itemIdRequired = -1;
        if (bossEnumId == 0)
            itemIdRequired = 21797;
        else if (bossEnumId == 1)
            itemIdRequired = 21798;
        else
            itemIdRequired = 21799;
        return this.getItems().playerHasItem(itemIdRequired) || getBank().containsItem(itemIdRequired);
    }

    /**
     * Removes custom spawned npcs e.g for minigames
     */
    public void clearUpPlayerNPCsForLogout() {
        for (NPC n : NPCHandler.npcs) {
            if (n != null) {
                if (n.spawnedBy == this.getIndex()) {
                    if (n.isPet)
                        continue;
                    n.unregister();
                }
            }
        }
    }
    /**
     * gets everything u need for the spawned npc you have following you
     */
    public NPC getSpawnedNPC() {
        for (NPC n : NPCHandler.npcs) {
            if (n != null) {
                if (n.spawnedBy == this.getIndex()) {
                    return n;

                }
            }
        }
        return null;
    }
    public void clearDerwensOrbs() {
        for (NPC n : derwens_orbs) {
            if (n != null) {
                n.unregister();
            }
            derwens_orbs = Lists.newArrayList();
        }
    }

    public void setArenaPoints(int arenaPoints) {
        this.arenaPoints = arenaPoints;
    }

    public int getArenaPoints() {
        return arenaPoints;
    }

    public String getKonarSlayerLocation() {
        return konarSlayerLocation;
    }

    public void setKonarSlayerLocation(String location) {
        this.konarSlayerLocation = location;
    }
    public String getHonourguard5SlayerLocation() { return honourguard5SlayerLocation; }

    public void setHonourguard5SlayerLocation(String location)  {
        this.honourguard5SlayerLocation = location;
    }

    public String getLastTask() {
        return lastTask;
    }

    public void setLastTask(String location) {
        this.lastTask = location;
    }

    public void setShayPoints(int shayPoints) {
        this.shayPoints = shayPoints;
    }

    public int getShayPoints() {
        return shayPoints;
    }

    public void setRaidPoints(int raidPoints) {
        this.raidPoints = raidPoints;
    }

    public int getRaidPoints() {
        return raidPoints;
    }

    public void braceletDecrease(int ether) {
        this.braceletEtherCount -= ether;
    }

    public void braceletIncrease(int ether) {
        this.braceletEtherCount += ether;
    }
    public void crystalDecrease(int bofas) {
        this.crystalCharge -= bofas;
    }

    public void crystaIncrease(int bofas) {
        this.crystalCharge += bofas;
    }

    public void StaffDecrease(int staff) {
        this.crystalStaff -= staff;
    }

    public void staffIncrease(int staff) {
        this.crystalStaff += staff;
    }
    static {
        appearanceUpdateBlockCache = new Stream(new byte[100]);
    }

    public int triviaPoints;
    public int getTriviaPoints;

    public int getTriviaPoints(){
        return triviaPoints;
    }

    public void setTriviaPoints(int triviaPoints) {
        this.triviaPoints = triviaPoints;
    }

    @Override
    public boolean susceptibleTo(HealthStatus status) {
        return !getItems().isWearingItem(12931, playerHat) && !getItems().isWearingItem(13199, playerHat) && !getItems().isWearingItem(13197, playerHat);
    }

    @Override
    public void removeFromInstance() {
        if (getInstance() != null) {
            getInstance().remove(this);
        }
    }

    @Override
    public int getEntitySize() {
        return 1;
    }

    public int getToxicStaffOfTheDeadCharge() {
        return toxicStaffOfTheDeadCharge;
    }

    public void setToxicStaffOfTheDeadCharge(int toxicStaffOfTheDeadCharge) {
        this.toxicStaffOfTheDeadCharge = toxicStaffOfTheDeadCharge;
    }

    public long getExperienceCounter() {
        return experienceCounter;
    }

    public void setExperienceCounter(long experienceCounter) {
        this.experienceCounter = experienceCounter;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public void setRunEnergy(int runEnergy, boolean update) {
        if (System.currentTimeMillis() - infrunenergyDelay < infrunenergyLength) {
            this.runEnergy = 10000;
            return;
        }
        if (runEnergy < 0) {
            runEnergy = 0;
        }
        this.runEnergy = runEnergy;
        if (update) {
            getPA().updateRunEnergy(false);
        }
    }

    public Entity getTargeted() {
        return targeted;
    }

    public void setTargeted(Entity targeted) {
        this.targeted = targeted;
    }

    public LootingBag getLootingBag() {
        return lootingBag;
    }

    public PrestigeSkills getPrestige() {
        return prestigeskills;
    }

    public ExpLock getExpLock() {
        return explock;
    }

    public RunePouch getRunePouch() {
        return runePouch;
    }

    public HerbSack getHerbSack() {
        return herbSack;
    }

    public GemBag getGemBag() {
        return gemBag;
    }

    public AchievementDiaryManager getDiaryManager() {
        return diaryManager;
    }

    public QuickPrayers getQuick() {
        return quick;
    }

    public void setInfernoBestTime(long infernoBestTime) {
    }

    public QuestTab getQuestTab() {
        return questTab;
    }

    public EventCalendar getEventCalendar() {
        return eventCalendar;
    }

    public LocalDate getLastVote() {
        return lastVote;
    }

    public void setLastVote(LocalDate lastVote) {
        this.lastVote = lastVote;
    }

    public LocalDate getLastVotePanelPoint() {
        return lastVotePanelPoint;
    }

    public void setLastVotePanelPoint(LocalDate lastVotePanelPoint) {
        this.lastVotePanelPoint = lastVotePanelPoint;
    }

    public int getEnterAmountInterfaceId() {
        return enterAmountInterfaceId;
    }

    public void setEnterAmountInterfaceId(int enterAmountInterfaceId) {
        this.enterAmountInterfaceId = enterAmountInterfaceId;
    }

    public void updateRunningToggled(boolean runningToggled) {
        this.runningToggled = runningToggled;
        getPA().updateRunningToggle();
    }

    public void setRunningToggled(boolean runningToggled) {
        this.runningToggled = runningToggled;
    }

    public boolean isRunningToggled() {
        return runningToggled;
    }
    public void setCrawlingToggled(boolean crawlingToggled) {
        this.crawlToggled = crawlingToggled;}
    public boolean isCrawlingToggled() {
        return crawlToggled;}
    public RechargeItems getRechargeItems() {
        return rechargeItems;
    }

    public UltraMysteryBox getUltraMysteryBox() {
        return ultraMysteryBox;
    }

    public YoutubeMysteryBox getYoutubeMysteryBox() {
        return youtubeMysteryBox;
    }

    public ResourceBoxLarge getResourceBoxLarge() {
        return resourceBox;
    }

    public NormalMysteryBox getNormalMysteryBox() {
        return normalMysteryBox;
    }

    public boolean isInTradingPost() {
        return inTradingPost;
    }

    public void setInTradingPost(boolean inTradingPost) {
        this.inTradingPost = inTradingPost;
    }

    public Inferno getInferno() {
        if (getInstance() != null && getInstance() instanceof Inferno) {
            return (Inferno) getInstance();
        }
        return null;
    }


    public MageArena getMageArena() {
        return mageArena;
    }
    public Genie getGenie() {
        return genie;
    }
    public Cannon getCannon() {
        return cannon;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public DialogueBuilder getDialogueBuilder() {
        return dialogueBuilder;
    }

    public void setDialogueBuilder(DialogueBuilder dialogueBuilder) {
        this.dialogueBuilder = dialogueBuilder;
    }

    public DailyRewards getDailyRewards() {
        return dailyRewards;
    }

    public Farming getFarming() {
        return farming;
    }

    public ModeSelection getModeSelection() {
        return modeSelection;
    }

    public ModeRevertType getModeRevertType() {
        return modeRevertType;
    }

    public void setModeRevertType(ModeRevertType modeRevertType) {
        this.modeRevertType = modeRevertType;
    }

    public BossTimers getBossTimers() {
        return bossTimers;
    }

    public WogwContributeInterface getWogwContributeInterface() {
        return wogwContributeInterface;
    }

    public DonationRewards getDonationRewards() {
        return donationRewards;
    }

    public TobContainer getTobContainer() {
        return tobContainer;
    }

    public TombsOfAmascutContainer getTombsOfAmascutContainer() {
        return tombsOfAmascutContainer;
    }

    public boolean inParty(String type) {
        return getParty() != null && getParty().isType(type);
    }

    public PlayerParty getParty() {
        return party;
    }

    public void setParty(PlayerParty party) {
        this.party = party;
    }

    public NotificationsTab getNotificationsTab() {
        return notificationsTab;
    }

    public boolean isPrintAttackStats() {
        return printAttackStats;
    }

    public void setPrintAttackStats(boolean printAttackStats) {
        this.printAttackStats = printAttackStats;
    }

    public boolean isPrintDefenceStats() {
        return printDefenceStats;
    }

    public void setPrintDefenceStats(boolean printDefenceStats) {
        this.printDefenceStats = printDefenceStats;
    }

    public TickTimer getPotionTimer() {
        return potionTimer;
    }

    /**
     * Gets the combo timer associated with combo eating
     * @return The {@link TickTimer} associated with Combo eating
     */
    public TickTimer getComboTimer() {
        return this.comboTimer;
    }

    public TickTimer getFoodTimer() {
        return foodTimer;
    }

    public Questing getQuesting() {
        return questing;
    }

    public boolean isHelpCcMuted() {
        return helpCcMuted;
    }

    public void setHelpCcMuted(boolean helpCcMuted) {
        this.helpCcMuted = helpCcMuted;
    }

    public boolean isGambleBanned() {
        return gambleBanned;
    }

    public void setGambleBanned(boolean gambleBanned) {
        this.gambleBanned = gambleBanned;
    }

    public boolean isValidUUID() {
        return getUUID() != null && getUUID().length() > 0;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets a queue of the player'sprevious packets (50 maximum).
     * These packets were not neccesarily handled by the server but
     * they contain every packet that was sent from the client to the server.
     *
     * @return
     */
    public Queue<Integer> getPreviousPackets() {
        return previousPackets;
    }

    public boolean isJoinedIronmanGroup() {
        return joinedIronmanGroup;
    }

    public void setJoinedIronmanGroup(boolean joinedIronmanGroup) {
        this.joinedIronmanGroup = joinedIronmanGroup;
    }

    public void drainPrayer() {
        sendMessage("You have run out of prayer points!");
        playerLevel[5] = 0;
        CombatPrayer.resetPrayers(this);
        prayerId = -1;
        getPA().refreshSkill(5);
    }

    public boolean isReceivedCalendarCosmeticJune2021() {
        return receivedCalendarCosmeticJune2021;
    }

    public void setReceivedCalendarCosmeticJune2021(boolean receivedCalendarCosmeticJune2021) {
        this.receivedCalendarCosmeticJune2021 = receivedCalendarCosmeticJune2021;
    }

    public HashSet<GroundItem> getLocalGroundItems() {
        return localGroundItems;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getDisplayNameLower() {
        return displayName.toLowerCase();
    }

    public String getDisplayNameFormatted() {
        return getRights().buildCrownString() + "" + WordUtils.capitalize(getDisplayName());
    }

    /**
     * Set {@link Player#displayName} and {@link Player#displayNameLong}.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.displayNameLong = Misc.playerNameToInt64(displayName.toLowerCase());
    }

    /**
     * Get the player's lowercased display name as a long value.
     */
    public long getDisplayNameLong() {
        return displayNameLong;
    }

    public boolean isCompletedTutorial() {
        return completedTutorial;
    }

    public void setCompletedTutorial(boolean completedTutorial) {
        this.completedTutorial = completedTutorial;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return {@link Player#getLoginName()} lowercase.
     */
    public String getLoginNameLower() {
        return getLoginName().toLowerCase();
    }

    public FriendsList getFriendsList() {
        return friendsList;
    }

    public void sendDestroyItem(int itemId) {
        ItemDef def = ItemDef.forId(itemId);

        if (def == null) {
            return;
        }

        if (!this.getItems().playerHasItem(itemId))
            return;

        final String itemName = def.getName();

        this.start(new DialogueBuilder(this).option(
                "Destroy " + itemName + "?",
                new DialogueOption("Yes", player -> {
                    this.getPA().closeAllWindows();
                    this.getItems().deleteItem(itemId, 1);
                }),
                new DialogueOption("No", player -> {
                    this.getPA().closeAllWindows();
                })
        ).send());
        return;
    }

    public boolean isRequiresPinUnlock() {
        return requiresPinUnlock;
    }

    public void setRequiresPinUnlock(boolean requiresPinUnlock) {
        this.requiresPinUnlock = requiresPinUnlock;
        if (requiresPinUnlock)
            logger.debug("Requires account pin unlock.");
    }

    public List<SkillExperience> getOutlastSkillBackup() {
        return outlastSkillBackup;
    }

    public PerduLostPropertyShop getPerduLostPropertyShop() {
        return perduLostPropertyShop;
    }

    public LeaderboardPeriodicity getCurrentLeaderboardPeriod() {
        if (currentLeaderboardPeriod == null)
            return LeaderboardPeriodicity.TODAY;
        return currentLeaderboardPeriod;
    }

    public void setCurrentLeaderboardPeriod(LeaderboardPeriodicity currentLeaderboardPeriod) {
        this.currentLeaderboardPeriod = currentLeaderboardPeriod;
    }

    private LeaderboardPeriodicity currentLeaderboardPeriod;

    public CollectionBox getCollectionBox() {
        return collectionBox;
    }

    public PvpWeapons getPvpWeapons() {
        return pvpWeapons;
    }

    @Override
    public int getBonus(Bonus bonus) {
        return this.getItems().getBonus(bonus);
    }

    public TomeOfFire getTomeOfFire() {
        return this.tomeOfFire;
    }

    public boolean isReceivedVoteStreakRefund() {
        return receivedVoteStreakRefund;
    }

    public void setReceivedVoteStreakRefund(boolean receivedVoteStreakRefund) {
        this.receivedVoteStreakRefund = receivedVoteStreakRefund;
    }

    public int getMigrationVersion() {
        return migrationVersion;
    }

    public void setMigrationVersion(int migrationVersion) {
        this.migrationVersion = migrationVersion;
    }

    public boolean fullNewArmadyl() {
        return getItems().isWearingItem(26502) && getItems().isWearingItem(26503) && getItems().isWearingItem(26504);
    }

    private PortalTeleports portalTeleports = new PortalTeleports(this);
    public PortalTeleports getPortalTeleports() {
        return portalTeleports;
    }

    private NewTeleInterface newTeleInterface = new NewTeleInterface(this);
    public boolean ismaging, isranging, ismeleeing = false;

    public String getStyle(){
        String style = "";
        if(ismeleeing)
            style = "melee";
        else if(ismaging)
            style = "mage";
        else
            style = "range";
        return style;
    }
    public NewTeleInterface getNewteleInterface() {
        return newTeleInterface;
    }


}

