package io.Odyssey.model.entity.player.packets;

import java.util.Objects;
import java.util.Optional;

import com.everythingrs.marketplace.Item;
import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.Hunllef;
import io.Odyssey.content.bosses.bryophyta.Bryophyta;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.bosses.mimic.StrangeCasketDialogue;
import io.Odyssey.content.bosses.nex.Nex;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.bosses.wildypursuit.FragmentOfSeren;
import io.Odyssey.content.bosses.wildypursuit.TheUnbearable;
import io.Odyssey.content.bosses.obor.OborInstance;
import io.Odyssey.content.commands.owner.Npc;
import io.Odyssey.content.commands.owner.Objectanim;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dwarfmulticannon.Cannon;
import io.Odyssey.content.minigames.tob.TobConstants;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.item.lootable.impl.SerenChest;
import io.Odyssey.content.item.lootable.impl.UnbearableChest;
import io.Odyssey.content.skills.Cooking;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.hunter.impling.PuroPuro;
import io.Odyssey.content.skills.runecrafting.ouriana.OurianaAltar;
import io.Odyssey.content.skills.runecrafting.ouriana.OurianaBanker;
import io.Odyssey.content.skills.slayer.LarrensKey;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.tickable.impl.WalkToTickable;
import io.Odyssey.util.logging.player.ClickObjectLog;


/**
 * Click Object
 */
public class ClickObject implements PacketType {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 228,
            FIFTH_CLICK = 234; // Already option 4 but not sure if it's used so just adding another one

    public static WorldObject getObject(Player player, int objectId, int objectX, int objectY) {
        Optional<WorldObject> worldObject = player.getRegionProvider().get(objectX, objectY)
                .getWorldObject(objectId, objectX, objectY, player.getHeight());
        return worldObject.orElse(null);
    }

    private static void walkTo(Player player, int option) {
        //mole hole ? yep prolly sending wrong shit

        WorldObject object = getObject(player, player.objectId, player.objectX, player.objectY);
        if (object != null) {
         //   System.out.println("obj: "+player.objectId+" x: "+player.objectX+" y: "+player.objectY+" opt: "+option+" d: "+player.distance(object.getPosition()));
            Position size = object.getObjectSize();
         //   Server.getLogging().write(new ClickObjectLog(player, object, option));
            //add stupid objects that can be clicked from a distance here.
            if (object.getId() == 31561) { // Rev cave agility shortcut pillars
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                player.setTickable((container, plr) -> {
                    if (plr.distance(object.getPosition()) < 2.5) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });
            } else if (object.getId() == 17062) {//catherby grapple
                    PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                    player.setTickable((container, plr) -> {
                     //   System.out.println("d: "+plr.distance(object.getPosition()));

                        if (plr.distance(object.getPosition()) < (player.absY < 3429 ? 10 : 1)) {
                            finishObjectClick(plr, option, object);
                            container.stop();
                        }
                    });
            } else if (object.getId() == 26380) {//grapple to armadyl eerie
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                player.setTickable((container, plr) -> {
                    //   System.out.println("d: "+plr.distance(object.getPosition()));

                    if (plr.distance(object.getPosition()) < (11)) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });
            } else if (object.getId() == 4550) {//lighthouse beach thing
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                //    System.out.println("d: "+player.distance(object.getPosition()));
                player.setTickable((container, plr) -> {

                    if (plr.distance(object.getPosition()) < 2) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });

            } else if (object.getId() == 30282) {//inferno
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
            //    System.out.println("d: "+player.distance(object.getPosition()));
                player.setTickable((container, plr) -> {

                    if (plr.distance(object.getPosition()) < 10) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });

            } else if (object.getId() == 17068) {
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                player.setTickable((container, plr) -> {
                    if (plr.distance(object.getPosition()) < 8.5) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });
            } else if (object.getId() == TobConstants.TREASURE_ROOM_ENTRANCE_OBJECT_ID) {
                PathFinder.getPathFinder().findRoute(player, object.getX(), object.getY(), true, 1, 1);
                player.setTickable((container, plr) -> {
                    if (plr.distance(object.getPosition()) < 2.5) {
                        finishObjectClick(plr, option, object);
                        container.stop();
                    }
                });
            } else {
                player.setTickable(new WalkToTickable(player, object.getPosition(), size.getX(), size.getY(), player1 -> finishObjectClick(player1, option, object)));
            }
        } else {
            System.out.println("obj is null");
            player.stopMovement();
        }
    }

    @Override
    public void processPacket(final Player c, int packetType, int packetSize) {
        if (c.getMovementState().isLocked() || c.getLock().cannotInteract(c))
            return;
        c.interruptActions();
        c.clickObjectType = c.objectX = c.objectId = c.objectY = 0;
        c.objectYOffset = c.objectXOffset = 0;
        c.getPA().resetFollow();
        c.attacking.reset();
        c.getPA().stopSkilling();

        if (c.isForceMovementActive()) {
            return;
        }
        if (c.getLootingBag().isWithdrawInterfaceOpen() || c.getLootingBag().isDepositInterfaceOpen() || c.viewingRunePouch) {
            return;
        }
        if (c.isFping()) {
            /**
             * Cannot do action while fping
             */
            return;
        }
        if (c.getBankPin().requiresUnlock()) {
            c.getBankPin().open(2);
            return;
        }
        if (c.getInterfaceEvent().isActive()) {
            c.sendMessage("Please finish what you're doing.");
            return;
        }

        c.xInterfaceId = -1;


        switch (packetType) {
            case FIRST_CLICK:
                c.objectX = c.getInStream().readSignedWordBigEndianA();
                c.objectId = c.getInStream().readInteger();
                c.objectY = c.getInStream().readUnsignedWordA();
                c.objectDistance = 1;
                walkTo(c, 1);
                break;
            case SECOND_CLICK:
                c.objectId = c.getInStream().readInteger();
                c.objectY = c.getInStream().readSignedWordBigEndian();
                c.objectX = c.getInStream().readUnsignedWordA();
                c.objectDistance = 1;
                walkTo(c, 2);
                break;
            case THIRD_CLICK:
                c.objectX = c.getInStream().readSignedWordBigEndian();
                c.objectY = c.getInStream().readUnsignedWord();
                c.objectId = c.getInStream().readInteger();
                walkTo(c, 3);
                break;
            case FIFTH_CLICK:
//                c.objectX = c.getInStream().readSignedWordBigEndian();
//                c.objectY = c.getInStream().readUnsignedWord();
//                c.objectId = c.getInStream().readInteger();

                c.objectX = c.getInStream().readSignedWordBigEndianA();
                c.objectId = c.getInStream().readInteger();
                c.objectY = c.getInStream().readUnsignedWordA();

                //			c.objectX = c.getInStream().readSignedWordBigEndianA();
                //			c.objectId = c.getInStream().readUnsignedWord();
                //			c.objectY = c.getInStream().readUnsignedWordA();
                walkTo(c, 5);
                break;
            case FOURTH_CLICK:
//                c.objectX = c.getInStream().readSignedWordBigEndianA();
//                c.objectY = c.getInStream().readSignedWordBigEndianA();
//                c.objectId = c.getInStream().readInteger();

                c.objectX = c.getInStream().readSignedWordBigEndianA();
                c.objectId = c.getInStream().readInteger();
                c.objectY = c.getInStream().readUnsignedWordA();
                //            c.objectX = c.getInStream().readSignedWordBigEndianA();
                //                c.objectId = c.getInStream().readInteger();
                //                c.objectY = c.getInStream().readUnsignedWordA();
                walkTo(c, 4);
                break;
        }
    }

    private static void finishObjectClick(Player c, int option, WorldObject worldObject) {
        c.facePosition(c.objectX, c.objectY);

        if (Math.abs(c.getX() - c.objectX) > 25 || Math.abs(c.getY() - c.objectY) > 25) {
            c.resetWalkingQueue();
            return;
        }

        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
                MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
                && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
            c.sendMessage("Your actions have declined the duel.");
            duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            return;
        }

        if (c.getInstance() != null && c.getInstance().handleClickObject(c, worldObject, option)) {
            return;
        }

        if (c.getTobContainer().handleClickObject(worldObject, option)) {
            return;
        }

        if (c.getTombsOfAmascutContainer().handleClickObject(worldObject, option)) {
            return;
        }

        if (LarrensKey.clickObject(c, worldObject)) {
            return;
        }

        if (TourneyManager.handleObjects(c, worldObject, option)) {
            return;
        }

        if (c.getQuesting().handleObjectClick(worldObject, option)) {
            return;
        }

        if (OurianaAltar.clickObject(c, worldObject)) {
            return;
        }

        switch (option) {
            case 1:
                if (c.objectId == 11834 && Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
                    c.getFightCave().leaveGame();
                    return;
                }
                if (c.objectId == 30283 && Boundary.isIn(c, Boundary.TOKKUL_PIT1)) {
                    c.getTokkulPit1().leaveGame();
                    return;
                }
                if (Cooking.clickRange(c, c.objectId)) {
                    return;
                }

                if (Cannon.clickObject(c, c.objectId, new Position(c.objectX, c.objectY, c.getHeight()), 1)) {
                    return;
                }
                if (OurianaBanker.clickObject(c, c.objectId, option)) {
                    return;
                }
                if (AvatarOfCreation.clickObject(c, c.objectId)) {
                    return;
                }

                if (c.objectId == 9357) {
                    if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
                        c.getFightCave().leaveGame();
                    }
                    return;
                }
                if (c.objectId == 30283) {
                    if (Boundary.isIn(c, Boundary.TOKKUL_PIT1)) {
                        c.getTokkulPit1().leaveGame();
                    }
                    return;
                }
				if (c.debugMessage) {
					c.sendMessage("Object Option One: " + c.objectId + " objectX: " + c.objectX + " objectY: " + c.objectY);
				}
				c.getFarming().handleObjectClick(c.objectId, c.objectX, c.objectY, 1);
				switch (c.objectId) {



                    case 32534: {
                        /**
                         * Bryophyta boss gate
                         */
                        c.start(new DialogueBuilder(c).statement("Warning! You're about to enter an instanced area, anything left on",
                                        "the ground when you leave will be lost. Would you like to continue?").

                                option("Are you sure you wish to open it?",
                                        new DialogueOption("Yes, let's go!", player -> {
                                            if (!player.getItems().playerHasItem(Bryophyta.KEY, 1)) {
                                                /**
                                                 * Requires mossy key.
                                                 */
                                                player.sendMessage("It's locked!");
                                                player.getPA().closeAllWindows();
                                                return;
                                            }
                                            new Bryophyta().enter(player);
                                            player.getPA().closeAllWindows();

                                        }),
                                        new DialogueOption("I don't think I'm quite ready yet.", plr -> {
                                            plr.getPA().closeAllWindows();
                                        })));
                        return;
                    }


                    case 34733:
                        if (c.getItems().playerHasItem(23184)) {
                            StrangeCasketDialogue.open(c);
                        } else {
                            c.sendMessage("You need a mimic casket to use this.");
                        }
                        break;
                    case 16683: // ladder in edge general store up
                        if (c.objectX == 3083 && c.objectY == 3513) {
                            c.climbLadderTo(new Position(3083, 3512, 1));
                        }
                        break;
                    case 43868: // ladder in edge general store up
                        if (c.objectX == 3244 && c.objectY == 10215) {
                            c.climbLadderTo(new Position(3124, 3806, 0));
                        }
                        break;
                    case 16679: // ladder in edge general store down
                        if (c.objectX == 3083 && c.objectY == 3513) {
                            c.climbLadderTo(new Position(3083, 3512, 0));
                        }
                        break;
                    case 33222:
                        if (!AvatarOfCreationSpawner.isSpawned()) {
                            c.sendMessage("@red@You cannot do this right now.");
                            return;
                        }
                        if (AvatarOfCreation.TOXIC_GEM_AMOUNT < 80) {
                            c.sendMessage("@red@You need to lower AvatarOfCreation's defence first.");
                            return;
                        }
                        break;
                    case 33223:
                    case 1206:
                        if (!AvatarOfCreationSpawner.isSpawned()) {
                            c.sendMessage("@red@You cannot do this right now.");
                            return;
                        }
                        break;
                    case 33730:
                        int randomTele = 1;
                        if (c.getItems().playerHasItem(AvatarOfCreation.KEY)) {
                            c.getPA().spellTeleport(3072 + randomTele, 3505 + randomTele, 0, false);
                            return;
                        }
                        break;

                    case 29487:
                    case 29486: {
                        boolean locked = !c.getItems().playerHasItem(OborInstance.KEY);
                        if (locked) {
                            c.sendMessage("The gate is locked shut.");
                            return;
                        }
                        /**
                         * Obor boss gate
                         */
                        c.start(new DialogueBuilder(c).option("Enter Obor's Lair?",
                                new DialogueOption("Yes.", player -> {
                                    if (locked) {
                                        /**
                                         * Requires mossy key.
                                         */
                                        c.sendMessage("The gate is locked shut.");
                                        player.getPA().closeAllWindows();
                                        return;
                                    }
                                    new OborInstance().begin(player);
                                    player.getPA().closeAllWindows();

                                }),
                                new DialogueOption("No.", plr -> {
                                    plr.getPA().closeAllWindows();
                                })));
                        return;
                    }
                    case 30720:
                        c.objectDistance = 3;
                        c.sendMessage("@blu@Use @red@::Lt @blu@to see package rewards!");
                        if (c.getItems().freeSlots() < 3) {
                            c.objectDistance = 3;
                            c.getDH().sendStatement("@red@You need at least 3 free slot to open this.");
                            return;
                        }
                        if (c.getItems().playerHasItem(FragmentOfSeren.KEY, 1)) {
                            c.objectDistance = 3;
                            c.getEventCalendar().progress(EventChallenge.OPEN_X_WILDY_CHESTS);
                            new SerenChest().roll(c);
                            return;
                        }
                        if (c.getItems().playerHasItem(TheUnbearable.KEY, 1)) {
                            c.objectDistance = 3;
                            c.getEventCalendar().progress(EventChallenge.OPEN_X_WILDY_CHESTS);
                            new UnbearableChest().roll(c);
                            return;
                        }
                        c.getDH().sendStatement("@red@You need a Unbearable or Seren key to open this.");
                        break;
                    case 28686:
                        c.objectDistance = 3;
                        break;
                    case 26738:
                        c.getPA().movePlayer(new Coordinate(3080, 3510));
                        break;
                    case 43751:
                        if (c.absX == 3739) {
                            c.getPA().movePlayer(3744, 3987, 0);
                        } else if (c.absX == 3743) {
                            c.getPA().movePlayer(3738, 3987, 0);
                        }
                        break;

                    case 47140: {
                        /**
                         * Callisto Cave Entrance
                         */
                        c.start(new DialogueBuilder(c).statement("Warning! This is a Multi Combat area, inside is the mighty",
                                        "Callisto. You may encounter players and have to fight as well").

                                option("Are you sure you wish to go in ?",
                                        new DialogueOption("Yes, let's go!", player -> {
                                            if (!player.getItems().playerHasItem(995, 20000)) {
                                                /**
                                                 * Requires 250k to enter.
                                                 */
                                                player.sendMessage("It's closed!");
                                                player.getPA().closeAllWindows();

                                                return;
                                            }
                                            if (c.absX == 3293) {
                                                AgilityHandler.delayEmote(c, "CRAWL", 1759, 11551, 0, 2);
                                            }
                                            player.getPA().closeAllWindows();

                                        }),
                                        new DialogueOption("I don't think I'm quite ready yet.", plr -> {
                                            plr.getPA().closeAllWindows();
                                        })));
                        return;
                    }
                    //callisto exit
                    case 47122:
                        if (c.absX == 1759) {
                            AgilityHandler.delayEmote(c, "CRAWL", 3374, 10291, 0, 2);
                        }
                        break;
                        //escape cave exit 1
                    case 47149:
                            AgilityHandler.delayEmote(c, "CLIMB_UP", 3285, 3805, 0, 2);
                        break;
                        // escape cave exit 2
                    case 47147:
                        AgilityHandler.delayEmote(c, "CRAWL", 3118, 3758, 0, 2);
                        break;
                        //AgilityHandler.delayEmote(c, "CLIMB_UP", 1651, 3619, 0, 2);
                    case 30018:
                        c.objectDistance = 4;
                        break;
                    case 2996:
                        c.objectDistance = 1;
                        break;
                    case 29776:
                    case 44596:
                    case 29734:
                    case 10777:
                 //   case 29879:
                    case 31624:
                        c.objectDistance = 4;
                        break;
                    case 33320:
                        c.objectDistance = 4;
                        break;
                    case 8929:
                       // c.objectDistance = 4;
                        break;
                    case 36691:
                    case 36556:
                    case 36692:
                    case 36693:
                    case 36694:
                    case 36695:
                    case 30283:
                        if (c.getInferno() != null) {
                            c.getInferno().leaveGame();
                        }
                        break;
                    case 25016:
                    case 25017:
                    case 25018:
                    case 41730:
                        c.setOpenInterface(22931);
                        System.out.println("open interface");
                        c.getWogwContributeInterface();
                        break;
                    case 25029:
                        PuroPuro.magicalWheat(c);
                        break;
                    case 21728:
                        if (c.playerLevel[16] < 34) {
                            c.sendMessage("You need an Agility level of 34 to pass this.");
                            return;
                        }
                        if (c.absY == 9566) {
                            AgilityHandler.delayEmote(c, "CRAWL", 2655, 9573, 0, 2);
                        } else {
                            AgilityHandler.delayEmote(c, "CRAWL", 2655, 9566, 0, 2);
                        }
                        break;
                    case 31556:
                    case 36201:
                        c.objectDistance = 3;
                        break;
                    case 11633:// al kharid
                        if (c.playerLevel[16] < 20) {
                            c.sendMessage("You need an Agility level of 20 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14412:// varrock
                        if (c.playerLevel[16] < 30) {
                            c.sendMessage("You need an Agility level of 30 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14843:// canafis
                        if (c.playerLevel[16] < 40) {

                            c.sendMessage("You need an Agility level of 40 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14898:// falador
                        if (c.playerLevel[16] < 50) {
                            c.sendMessage("You need an Agility level of 50 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14927:// seers
                        if (c.playerLevel[16] < 60) {
                            c.sendMessage("You need an Agility level of 60 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14935:// pollnivneach
                        if (c.playerLevel[16] < 70) {
                            c.sendMessage("You need an Agility level of 70 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 14964:// rellekka
                        if (c.playerLevel[16] < 80) {
                            c.sendMessage("You need an Agility level of 80 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 15608:// ardougne
                        if (c.playerLevel[16] < 90) {
                            c.sendMessage("You need an Agility level of 90 to pass this.");
                            return;
                        }
                        c.objectDistance = 4;
                        break;
                    case 11701:
                        c.getPA().startTeleport(2202, 3056, 0, "modern", false);
                        break;
                    case 9398:// deposit
                        c.getPA().sendFrame126("The Bank of " + Configuration.SERVER_NAME + " - Deposit Box", 7421);
                        c.getPA().sendFrame248(4465, 197);// 197 just because you can't
                        c.getItems().sendInventoryInterface(7423);
                        break;
                    case 29735:// Basic training
                        if (c.objectX != 3277 && c.objectY != 5169) {
                            c.getPA().movePlayer(2634, 5069, 0);
                            c.sendMessage("Welcome to the Basic training dungeon, you can find basic monsters here!");
                        }
                        break;
                    case 6450:// Basic training ladder
                        c.getPA().movePlayer(1644, 3673, 0);
                        break;

                    case 27785:
                        c.getDH().sendDialogues(70300, -1);
                        break;
                    case 26709:// strongholdslayer cave
                        c.getPA().movePlayer(2429, 9825, 0);
                        c.sendMessage("Welcome to the Stronghold slayer cave, you can find many slayer monsters here!");
                        break;
                    case 26710:// strongholdslayer caveexit
                    case 27258:
                        c.getPA().movePlayer(2430, 3425, 0);
                        break;
                    case 28892:// catacomb agility
                        if (c.playerLevel[16] < 34) {
                            c.sendMessage("You need an Agility level of 34 to pass this.");
                            return;
                        }
                        if (c.absX == 1648) {
                            AgilityHandler.delayEmote(c, "CRAWL", 1646, 10000, 0, 2);
                        } else if (c.absX == 1716) {
                            AgilityHandler.delayEmote(c, "CRAWL", 1706, 10078, 0, 2);
                        } else if (c.absX == 1706) {
                            AgilityHandler.delayEmote(c, "CRAWL", 1716, 10056, 0, 2);
                        } else if (c.absX == 1646) {
                            AgilityHandler.delayEmote(c, "CRAWL", 1648, 10009, 0, 2);
                        }
                        break;
                    case 30175:// Stronghold short
                        if (c.playerLevel[16] < 72) {
                            c.sendMessage("You need an Agility level of 72 to pass this.");
                            return;
                        }
                        if (c.absX == 2429) {
                            AgilityHandler.delayEmote(c, "CRAWL", 2435, 9806, 0, 2);
                        } else if (c.absX == 2435) {
                            AgilityHandler.delayEmote(c, "CRAWL", 2429, 9806, 0, 2);
                        }
                        break;

                    case 32153://lithriken barriers
                        if (c.getX() == 1562)
                            c.getAgilityHandler().move(c, -2, 0, 819, -1);
                        if (c.getX() == 1564)
                            c.getAgilityHandler().move(c, 2, 0, 819, -1);
                        if (c.getX() == 1573)
                            c.getAgilityHandler().move(c, 2, 0, 819, -1);
                        if (c.getX() == 1575)
                            c.getAgilityHandler().move(c, -2, 0, 819, -1);
                        if (c.getY() == 5089)
                            c.getAgilityHandler().move(c, 0, -2, 819, -1);
                        if (c.getY() == 5087)
                            c.getAgilityHandler().move(c, 0, 2, 819, -1);
                        if (c.getX() == 1560)
                            c.getAgilityHandler().move(c, 2, 0, 819, -1);
                        break;
                    case 23566:// rune and addy dragons
                        if (c.absY == 9963) {// rune
                            c.getPA().movePlayer(3120, 9970, 0);
                        } else if (c.absY == 9970) {// addy
                            c.getPA().movePlayer(3120, 9963, 0);
                        }
                        break;
                    case 535:// Smoke Devil Entrance
                        if (c.absX == 2379) {
                            AgilityHandler.delayEmote(c, "CRAWL", 2376, 9452, 0, 2);
                        }
                        break;
                    case 536:// Smoke Devil Exit
                        if (c.absX == 2376) {
                            AgilityHandler.delayEmote(c, "CRAWL", 2379, 9452, 0, 2);
                        }
                        break;
                    /*
                     * case 17385://taveryly exit AgilityHandler.delayEmote(c, "CLIMB_UP", 1662,
                     * 3529, 0, 2); break;
                     */
                    case 1738:// tav entrance
                        AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2884, 9798, 0, 2);
                        break;
                    case 2123:// relleka entrance
                        AgilityHandler.delayFade(c, "CRAWL", 2808, 10002, 0, "You crawl into the entrance.",
                                "and you end up in a dungeon.", 3);
                        c.sendMessage("Welcome to the Relleka slayer dungeon, find many slayer tasks here.");
                        break;
                    case 2268:// ice dung exit
                        AgilityHandler.delayEmote(c, "CLIMB_UP", 1651, 3619, 0, 2);
                        break;
                    case 2141:// relleka exit
                        c.getPA().movePlayer(1259, 3502, 0);
                        break;
                    /*
                     * case 29734://dgorillas if (c.objectX == 1349 && c.objectY == 3591) {
                     * c.getPA().movePlayer(2130, 5646, 0); c.
                     * sendMessage("Welcome to the Demonic Gorilla's Dungeon, try your luck for a heavy frame!"
                     * ); } break;
                     */
                    case 28687:// dgexit
                        c.getPA().movePlayer(1348, 3590, 0);
                        break;
                    case 4153:// corpexit
                        c.getPA().movePlayer(1547, 3571, 0);
                        break;
                    case 2544:// daggentrence
                        c.getPA().movePlayer(2446, 10147, 0);
                        break;
                    case 8966:// dagexit
                        c.getPA().movePlayer(1547, 3571, 0);
                        break;


                    case 2823:// mdragsentrance
                        AgilityHandler.delayFade(c, "CRAWL", 1746, 5323, 0, "You crawl into the entrance.",
                                "and you end up in a dungeon.", 3);
                        c.sendMessage("Welcome to the Mith Dragons Cave, try your luck for a visage or d full helm!");
                        break;
                    case 37337: // fence to funleff
                        c.getPA().movePlayer(1252, 2653, 0);
                        Hunllef.start(c);  // how Can I remove the key needed to enter ?
                        break;
                    case 37340:// tele to gauntlet
                        Hunllef.start(c);  // how Can I remove the key needed to enter ?
                       // c.getPA().movePlayer(1252, 2646, 0);
                        break;
                    case 25337:// mdragsexit
                        c.getPA().movePlayer(1792, 3709, 0);
                        break;
                    case 4150:// warriors guild
                        c.sendMessage("Welcome to the Warriors guild, good luck with your defenders!");
                        break;
                    case 4151:// barrows
                        c.getPA().movePlayer(3565, 3308, 0);
                        c.sendMessage("Welcome to Barrows, good luck with your rewards!");
                        break;
                    case 31561:
                        c.objectDistance = 2;
                        break;
                    case 1733:
                    case 10779:
                        c.objectYOffset = 2;
                        break;
                    case 11756:
                    case 4551:
                    case 4553:
                    case 4555:
                    case 4557:
                    case 23556:
                    case 677:
                    case 21578:
                    case 6461:
                    case 6462:
                    case 26645:
                    case 26646:
                    case 26762:
                    case 26763:
                    case 11374:
                    case 26567:
                    case 26568:
                    case 26569:
                    case 11406:
                    case 11430:
                  //  case 26461:
                    case 26766:
                    case 1759:
                    case 1761:
                    case 1753:
                    case 14918:
                    case 1754:
                    case 11819:
                    case 11826:
                    case 1751:
                    case 7471:
                    case 11377:
                    case 11375:
                    case 11376:
                    case 10817:
                    case 10595:
                    case 10596:
                    case 29082:
                    case 7811:
                    case 28893:
                        c.objectDistance = 4;
                        break;
                    case 14938:
                    case 14402:
                    case 14403:
                    case 14404:
                    case 11634:
                    case 14409:
                    case 14398:
                    case 14399:
                    case 14413:
                    case 14414:
                    case 14832:
                        c.objectDistance = 4;
                        break;
                    case 14833:
                    case 14834:
                    case 14835:
                    case 14836:
                    case 14841:
                    case 14844:
                    case 14845:
                    case 14848:
                    case 14846:
                    case 14894:
                    case 14847:
                    case 14897:
                    case 14899:
                    case 14901:
                    case 14903:
                    case 14904:
                    case 14905:
                    case 14911:
                    case 14919:
                    case 14920:
                    case 14921:
                    case 14922:
                    case 14923:
                    case 14924:
                    case 14928:
                    case 14932:
                    case 14929:
                    case 14930:
                    case 14931:
                        c.objectDistance = 4;
                        break;
                    case 14936:
                    case 14937:
                    case 14939:
                    case 14940:
                    case 14941:
                    case 14944:
                    case 14945:
                    case 6260:
                    case 14947:
                    case 14987:
                    case 14990:
                    case 14991:
                    case 14992:
                    case 14994:
                    case 15609:
                    case 26635:
                    case 15610:
                    case 15611:
                    case 28912:
                    case 15612:
                        c.objectDistance = 4;
                    case 31925:
                        c.objectDistance = 2;
                        break;
                    case 31858:
                        c.objectYOffset = 1;
                        break;
                    case 23131:
                    case 26366: // Godwars altars
                    case 26365:
                    case 26364:
                    case 26363:
                    case 678:
                    case 16466:
                    case 23569:
                    case 23568:
                    case 23132:
                    case 29730:
                    case 29729:
                    case 29728:
                        c.objectDistance = 5;
                        break;
                    case 3044:
                    case 21764:
                    case 17010:
                    case 2561:
                    case 2562:
                    case 2563:
                    case 2564:
                    case 2565:
                    case 17068:
                    case 27057:
                        c.objectDistance = 6;
                        break;
                    case 5094:
                    case 5096:
                    case 5097:
                    case 5098:
                    case 14912:
                    case 16511:
                    case 34773:
                        c.objectDistance = 7;
                        break;
                    case 26562:
                        c.objectDistance = 2;
                        break;
                    case 26503:
                        c.objectDistance = 1;
                        break;
                    case 26518:
                        c.objectDistance = 1;
                        break;
                    case 26380:
                       // c.objectDistance = 9;
                        break;
                    case 26502:
                        c.objectDistance = 1;
                        break;
                    case 20720:
                    case 20721:
                    case 20722:
                    case 20770:
                    case 20771:
                    case 20772:
                    case 10778:
                    case 27255:
                    case 31557:
                   // case 24303:
                    case 16671:
                    case 172:
                    case 2144:
                    case 1549:

                        c.objectDistance = 3;
                        break;
                    case 7674:
                        c.objectDistance = 2;
                        break;
                    case 11758:
                    case 11764:
                    case 11762:
                    case 11759:
                    case 1756:
                    case 26724:
                        c.objectDistance = 5;
                        break;
                    case 23271:
                        c.objectDistance = 5;
                        break;
                    case 2491:
                        c.objectDistance = 10;
                        break;
                    case 245:
                        c.objectYOffset = -1;
                        c.objectDistance = 0;
                        break;
                    case 272:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 273:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 246:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 4493:
                    case 4494:
                    case 4496:
                    case 4495:
                        c.objectDistance = 5;
                        break;
                    case 20667: // Barrows tomb staircases
                    case 20668:
                    case 20669:
                    case 20670:
                        c.objectDistance = 1;
                        break;
                    case 20671:
                    case 20672:
                        c.objectDistance = 3;
                        break;
                    case 10229:
                    case 6522:
                    case 11734:
                    case 11833:
                    case 38003:
                    case 11834:
                        c.objectDistance = 7;
                        break;
                    case 29681:
                    case 29682:
                        if (c.objectX == 1570 && c.objectY == 3484)
                            c.objectYOffset = 1;
                        break;
                    case 8959:
                        c.objectYOffset = 1;
                        break;
                    case 4417:
                        if (c.objectX == 2425 && c.objectY == 3074)
                            c.objectYOffset = 2;
                        break;
                    case 4420:
                        if (c.getX() >= 2383 && c.getX() <= 2385) {
                            c.objectYOffset = 1;
                        } else {
                            c.objectYOffset = -2;
                        }
                        break;
                    case 6552:
                    case 409:
                    case 28900:
                    case 12941:
                        c.objectDistance = 3;
                        break;
                    case 2114:
                    case 2118:
                    case 2119:
                    case 2120:
                    case 16509:
                    case 21725:
                    case 21727:
                    case 23104:
                    case 1728:
                    case 1727:
                    case 1569:
                    case 1568:
                     //   c.objectDistance = 4;
                        break;
                    case 2879:
                    case 2878:
                        c.objectDistance = 3;
                        break;
                    case 29668:
                    case 29670:
                        if (c.objectX == 1572)
                            c.objectXOffset = 1;
                        else if (c.objectY == 3494)
                            c.objectYOffset = 1;
                        break;
                    case 2558:
                        c.objectDistance = 0;
                        if (c.absX > c.objectX && c.objectX == 3044)
                            c.objectXOffset = 1;
                        if (c.absY > c.objectY)
                            c.objectYOffset = 1;
                        if (c.absX < c.objectX && c.objectX == 3038)
                            c.objectXOffset = -1;
                        break;
                    case 9356:
                        c.objectDistance = 2;
                        break;
                    case 5959:
                    case 1815:
                    case 5960:
                    case 1816:
                        c.objectDistance = 0;
                        break;
                    case 9293:
                        c.objectDistance = 2;
                        break;
                    case 4418:
                        if (c.objectX == 2374 && c.objectY == 3131)
                            c.objectYOffset = -2;
                        else if (c.objectX == 2369 && c.objectY == 3126)
                            c.objectXOffset = 2;
                        else if (c.objectX == 2380 && c.objectY == 3127)
                            c.objectYOffset = 2;
                        else if (c.objectX == 2369 && c.objectY == 3126)
                            c.objectXOffset = 2;
                        else if (c.objectX == 2374 && c.objectY == 3131)
                            c.objectYOffset = -2;
                        break;
                    case 36917:
                        c.objectDistance = 2;
                        break;
                    case 9706:
                        c.objectDistance = 0;
                        c.objectXOffset = 1;
                        break;
                    case 9707:
                        c.objectDistance = 1;
                        c.objectYOffset = -1;
                        break;
                    case 4419:
                    case 6707: // verac
                        c.objectYOffset = 3;
                        break;
                    case 6823:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;


                    case 46220:
                        c.getPA().sendPlayerObjectAnimation(3679,5140,9504,1,1);
                        break;
                    case 43724:
                        c.getPA().movePlayer(2485, 5727, 0);
                        break;
                    case 6706: // torag
                        c.objectXOffset = 2;
                        break;
                    case 6772:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 6705: // karils
                        c.objectYOffset = -1;
                        break;
                    case 6822:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 6704: // guthan stairs
                        c.objectYOffset = -1;
                        break;
                    case 6773:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 6703: // dharok stairs
                        c.objectXOffset = -1;
                        break;
                    case 6771:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 6702: // ahrim stairs
                        c.objectXOffset = -1;
                        break;
                    case 6821:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 3192:
                        c.objectDistance = 7;
                        break;
                    case 1276:
                    case 1278:// trees
                    case 1279:
                    case 1281: // oak
                    case 1758:
                    case 1760:
                    case 1750:
                    case 9036:
                    case 3037:
                    case 29763:
                    case 11761:
                    case 11763:
                    case 11755:
                    case 1308: // willow
                    case 1307: // maple
                    case 1309: // yew
                    case 1306: // yew
                    case 10820:
                    case 10833:
                    case 10834:
                    case 10832:
                    case 10822:
                    case 10829: // willow
                    case 10819: // willow
                    case 10831: // willow
                    case 10828:
                    case 8513:
                    case 8512:
                    case 8511:
                    case 8510:
                    case 8509:
                    case 8508:
                    case 8507:
                    case 8506:
                    case 8504:
                    case 8503:
                    case 5121:
                    case 4536:
                    case 8441:
                    case 8442:
                    case 8443:
                    case 8444:
                    case 8440:
                    case 8439:
                    case 8438:
                    case 8437:
                    case 8436:
                    case 8435:
                    case 5126:
                    case 4674:
                    case 4535:
                        c.objectDistance = 3;
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                if (c.debugMessage)
                c.sendMessage("obj: "+ worldObject.toString());

                c.getActions().firstClickObject(c.objectId, c.objectX, c.objectY);
                break;
            case 2:
                if (c.debugMessage) {
                    c.sendMessage("Object Option Two: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY
                            + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }
                if (c.objectId == 12309) {
                    c.getShops().openShop(14);
                }
                if (Cannon.clickObject(c, c.objectId, new Position(c.objectX, c.objectY, c.getHeight()), 2)) {
                    return;
                }
                if (OurianaBanker.clickObject(c, c.objectId, option)) {
                    return;
                }

                c.getFarming().handleObjectClick(c.objectId, c.objectX, c.objectY, 2);
                switch (c.objectId) {


                    case 2030: // Allows for the furnace to be used from the other side too
                        c.objectDistance = 4;
                        c.objectXOffset = 3;
                        c.objectYOffset = 3;
                        break;

                    case 2561:
                    case 2562:
                    case 2563:
                    case 2564:
                    case 2565:
                    case 2478:
                    case 2483:
                    case 2484:
                    case 11734:
                    case 11731:
                    case 11732:
                    case 23104:
                    case 24009:
                    case 14011:
                    case 7811:
                    case 28900:
                    case 29165:
                    case 172:
                    case 10834:
                        c.objectDistance = 3;
                        break;
                    case 4874:// theiving stalls
                    case 4875:
                    case 4876:
                    case 4877:
                    case 4878:
                        c.objectDistance = 1;
                        break;
                    case 6163:
                    case 6165:
                    case 6166:
                    case 6164:
                    case 6162:
                        break;
                    case 29777:
                    case 29734:
                    case 10777:
                   // case 29879:
                        c.objectDistance = 4;
                        break;
                    case 33320:
                        c.objectDistance = 4;
                        break;
                    case 3192:
                        c.objectDistance = 7;
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;

                }
                c.getActions().secondClickObject(c.objectId, c.objectX, c.objectY);
                break;
            case 3:
                if (c.debugMessage) {
                    c.sendMessage("Object Option Three: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: "
                            + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }
                if (Cannon.clickObject(c, c.objectId, new Position(c.objectX, c.objectY, c.getHeight()), 3)) {
                    return;
                }
                c.getFarming().handleObjectClick(c.objectId, c.objectX, c.objectY, 3);

                c.getActions().thirdClickObject(c.objectId, c.objectX, c.objectY);
                break;
            case 4:
                if (c.debugMessage) {
                    c.sendMessage("Object Option Four: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: "
                            + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }
                switch (c.objectId) {
                    case 25029:
                        PuroPuro.magicalWheat(c);
                        break;
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                c.getActions().fourthClickObject(c.objectId, c.objectX, c.objectY);
                break;
            case 5:
                if (c.debugMessage) {
                    c.sendMessage("Object Option five: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: "
                            + c.objectY + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }
                switch (c.objectId) {

                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                c.getActions().fifthClickObject(c.objectId, c.objectX, c.objectY);
                break;
        }
    }
}
