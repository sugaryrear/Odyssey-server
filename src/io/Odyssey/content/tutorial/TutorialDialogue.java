package io.Odyssey.content.tutorial;

import java.util.function.Consumer;

import io.Odyssey.content.commands.all.Changepass;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.combat.magic.items.Starter;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerMovementState;
import io.Odyssey.model.entity.player.PlayerMovementStateBuilder;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.entity.player.mode.Mode;
import io.Odyssey.model.entity.player.mode.ModeType;
import io.Odyssey.model.entity.player.mode.group.GroupIronman;

import static io.Odyssey.content.dailyrewards.DailyRewardsDialogue.DAILY_REWARDS_NPC;

public class TutorialDialogue extends DialogueBuilder {

    public static final int TUTORIAL_NPC = 2813;
    private static final String IN_TUTORIAL_KEY = "in_tutorial";
    private static final DialogueOption[] XP_RATES = {
            new DialogueOption("Tutorial", p ->  p.start(new TutorialDialogue(p, false))),
            new DialogueOption("Skip", p -> skiptut(p))
    };

    public static boolean inTutorial(Player player) {
        return player.getAttributes().getBoolean(IN_TUTORIAL_KEY);
    }

    public static void setInTutorial(Player player, boolean inTutorial) {
        player.getAttributes().setBoolean(IN_TUTORIAL_KEY, inTutorial);
        if (inTutorial) {
            player.setMovementState(new PlayerMovementStateBuilder().setLocked(true).createPlayerMovementState());
        } else {
            player.setMovementState(PlayerMovementState.getDefault());
        }
    }

    public static void selectedMode(Player player, ModeType mode) {


        Consumer<Player> tutorialdialogue = p -> {
            //if (mode == ModeType.STANDARD) {
            finish(player, mode);
            p.start(new DialogueBuilder(p).setNpcId(DAILY_REWARDS_NPC).option(XP_RATES));

        };
        Consumer<Player> tutorialdialogue_toconfirmmodern = p -> {

            finish(player, ModeType.ROGUE_HARDCORE_IRONMAN);
            p.start(new DialogueBuilder(p).setNpcId(DAILY_REWARDS_NPC).option(XP_RATES));

        };
        Consumer<Player> tutorialdialogue_letsselectlegendmodeornot = p -> {
            player.start(new DialogueBuilder(player)
                    .setNpcId(TUTORIAL_NPC)
                    .option("choose a sub-mode", new DialogueOption("OSRS (+5% Bonus Drop Rate)", tutorialdialogue),
                            new DialogueOption("Modern (+50% more XP)", tutorialdialogue_toconfirmmodern))
            );
        };


        player.start(new DialogueBuilder(player)
                .setNpcId(TUTORIAL_NPC)
                .option(new DialogueOption("Confirm: @blu@"+mode.getFormattedName()+"@bla@", mode == ModeType.STANDARD ? tutorialdialogue_letsselectlegendmodeornot :tutorialdialogue),
                        new DialogueOption("No", p -> p.getModeSelection().openInterface()))
        );
    }


    private static void skiptut(Player player) {
        player.getPA().closeAllWindows();
        player.setMovementState(PlayerMovementState.getDefault());

    }
    public static void finishtut(Player player) {
        //    System.out.println("here>");
        player.setMovementState(PlayerMovementState.getDefault());
        if(!player.firsttimetutorial){
           // player.getItems().addItem(995,450_000);
        }
        player.firsttimetutorial=true;
        player.getPA().closeAllWindows();
        setInTutorial(player, false);
    }


    public static void finish(Player player, ModeType modeType) {
        switch (modeType) {
            case IRON_MAN:
                player.setMode(Mode.forType(ModeType.IRON_MAN));
                player.getRights().setPrimary(Right.IRONMAN);
                break;
            case ULTIMATE_IRON_MAN:
                player.setMode(Mode.forType(ModeType.ULTIMATE_IRON_MAN));
                player.getRights().setPrimary(Right.ULTIMATE_IRONMAN);
                break;
            case HC_IRON_MAN:
                player.setMode(Mode.forType(ModeType.HC_IRON_MAN));
                player.getRights().setPrimary(Right.HC_IRONMAN);
                break;
            case OSRS:
                player.setMode(Mode.forType(ModeType.OSRS));
                player.getRights().setPrimary(Right.OSRS);
                break;
            case ROGUE:
                player.setMode(Mode.forType(ModeType.ROGUE));
                player.getRights().setPrimary(Right.ROGUE);
                break;
            case ROGUE_HARDCORE_IRONMAN:
                player.setMode(Mode.forType(ModeType.ROGUE_HARDCORE_IRONMAN));
                player.getRights().setPrimary(Right.ROGUE_HARDCORE_IRONMAN);
                break;
            case GROUP_IRONMAN:
                player.setMode(Mode.forType(ModeType.GROUP_IRONMAN));
                player.getRights().setPrimary(Right.GROUP_IRONMAN);
                break;
            default:
                player.setMode(Mode.forType(ModeType.STANDARD));
                break;
        }
        player.setMovementState(new PlayerMovementStateBuilder().setLocked(true).createPlayerMovementState());
        player.getPA().requestUpdates();
        //setInTutorial(player, true);
        Starter.addStarter(player);
        //player.setCompletedTutorial(true);
        //player.getPA().closeAllWindows();
      //  player.setMovementState(new PlayerMovementStateBuilder().setLocked(false).createPlayerMovementState());
      ///  player.getPA().givestarter();
        if (player.getRights().contains(Right.GROUP_IRONMAN)) {
            GroupIronman.moveToFormingLocation(player);

        }

    }

    public TutorialDialogue(Player player, boolean repeat) {
        super(player);

        setNpcId(DAILY_REWARDS_NPC);

        //			player.start(new DialogueBuilder(player)
        //				.npc(Npcs.DONATOR_SHOP, "Processing, please wait. *beep boop*", "@red@Do not logout.")
        //				.action(Reclaim::verify)


        Consumer<Player> tutorialdialogue_10 = p -> {
            //p.moveTo(new Position(2019, 3647, 0));
            p.sendMessage("sendvotenotification##");
            p.moveTo(new Position(2012, 3638, 0));
            exit(TutorialDialogue::finishtut);

        };
        Consumer<Player> tutorialdialogue_9 = p -> {
            player.sendMessage("flashworldmap##");
            p.moveTo(new Position(2012, 3651, 0));
            statement("The Player Tab is located to the left of the logout button", "You can view the mailbox, membership benefits, and,"," other important links from the Player Tab.")
                    .action(tutorialdialogue_10);

        };
        Consumer<Player> tutorialdialogue_8 = p -> {
            p.moveTo(new Position(2012, 3651, 0));
            statement("Every Server needs a means to teleport!" , "On Runescape players can teleport around the map by," , "clicking the Runescape icon underneath the minimap")
                    .action(tutorialdialogue_9);

        };
        Consumer<Player> tutorialdialogue_7 = p -> {
           // p.moveTo(new Position(2019, 3646, 0));
            p.moveTo(new Position(2019, 3647, 0));
            statement("This is the Runescape Inn from here you can," , "access our voting store & donation store aswell,", "as the crystal key chest and brimstone chest")
            //exit(TutorialDialogue::finishtut);
            .action(tutorialdialogue_8);
        };

        Consumer<Player> tutorialdialogue_6 = p -> {
            //p.moveTo(new Position(2019, 3647, 0));
            p.moveTo(new Position(2013, 3623, 0));
            statement("This is our Daily Task Board" , "You can come here to be assigned a new daily task" , "as well as check on the progress" +
                    " of your daily task!")
                    .action(tutorialdialogue_7);

        };
        Consumer<Player> tutorialdialogue_5 = p -> {
            //p.moveTo(new Position(2013, 3623, 0));
            p.moveTo(new Position(2030, 3649, 0));
            statement("This is our Southern Town Square you'll find our" , "Shopping area aswell as the Mage Of Zamorak!" , "Gertrudes farm is located to the south aswell!")
                    .action(tutorialdialogue_6);

        };
        Consumer<Player> tutorialdialogue_4 = p -> {
            //p.moveTo(new Position(2030, 3649, 0));
            p.moveTo(new Position(2055, 3575, 0));
            statement("This is our Northern Town Square you'll find our" , "Fishing Pond, Our anvil & furnace," , "aswell as our Northern Bank here!")
                    .action(tutorialdialogue_5);

        };
        Consumer<Player> tutorialdialogue_3 = p -> {
            //player.sendMessage("flashworldmap##");
            //statement("Talk to the Zodian guide to replay this!")
            //statement("This is our Slayer Masters Lair, You can be assigned various different tasks from each other them including boss tasks!")
            // statement("This is our Southern Town Square you'll find many" , "different shops located here that'll help you with your journey")
            p.moveTo(new Position(2903, 5203, 0));
            statement("This is our Slayer Lair it is located to the south," , " of the island, you can be assigned various different tasks," , "from each slayer master including boss tasks!")
                    .action(tutorialdialogue_4);

        };

        Consumer<Player> tutorialdialogue_2 = p -> {
            p.moveTo(new Position(2012, 3638, 0));
            statement("On Runescape, We have over 20+ bosses to battle & Conquer!" , "Some will require a team but you'll have" +
                    " all the fun nonetheless!")
                    .action(tutorialdialogue_3);
        };
        statement("Welcome to Runescape, we are a,", "Community driven RSPS, with many features like," , "Full Raids 1 & 2, All skills working, Curse Prayers",
                "We're always adding loads of new content every week!")
                .action(tutorialdialogue_2);

    }

    @Override
    public void initialise() {
        setInTutorial(getPlayer(), true);
        super.initialise();
    }

    private void npc(Position teleport, String...text) {
        npc(text).action(player -> player.moveTo(teleport));
    }
    private void npcc(Position teleport, String...text) {
        npc(text).action(player -> player.moveTo(teleport));
    }

}
