package io.Odyssey.content.questing.hftd;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.questing.Quest;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.SkillLevel;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PathFinder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.model.items.ItemAssistant;

import static io.Odyssey.content.dailyrewards.DailyRewardsDialogue.DAILY_REWARDS_NPC;
import static io.Odyssey.model.Items.*;
import static io.Odyssey.model.Npcs.*;

public class HftdQuest extends Quest {

    public static final int CASKET_TO_BUY_BOOK = 3849;
    private static final int AGILITY_REQUIREMENT = 35;
    private static final int SHOP_ID = 13;

    public HftdQuest(Player player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Horror From The Deep";
    }

    @Override
    public List<SkillLevel> getStartRequirements() {
        return Lists.newArrayList(new SkillLevel(Skill.AGILITY, AGILITY_REQUIREMENT));
    }

    @Override
    public List<String> getJournalText(int stage) {
        List<String> lines = Lists.newArrayList();
        switch (stage) {
            case 0:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I can start this quest by speaking to @dre@Larrissa @bla@at the");
                lines.add("@dre@Lighthouse@bla@ to the @dre@North@bla@ of the @dre@Barbarian Outpost.");
                lines.add("To complete this quest I need:");
                lines.add("@dre@Level 35 Agility");
                lines.add("@dre@Level 13 or higher magic");
                lines.add("@dre@Ability to defeat level 100+ enemies");

               // lines.addAll(getStartRequirementLines());
                break;
            case 1:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I agreed to help Larrissa at the lighthouse </str>");
                lines.add("I need to talk to @dre@Gunnjorn@bla@ at the barbarian outpost.");
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("");
                break;
            case 2:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I received a spare lighthouse key from Gunnjorn</str>");
                lines.add("I need to talk to Larrissa back at the lighthouse.");
                break;
            case 3:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I have repaired the bridge</str>");
                lines.add("I need to speak to Larrissa at the lighthouse.");
                break;
            case 4:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I have repaired the bridge</str>");
                lines.add("I need to find out how to fix the light on top of the lighthouse.");
                break;
            case 5:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I have fixed the lighthouse beacon</str>");
                lines.add("I need to find out what is going on in the basement.");

            case 6:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("<str=1>I passed through the strange wall and found Jossik</str>");
                lines.add("");
                break;

            case 7:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I survived the Horror From the Deep and received");
                lines.add("a rusty casket!");
                break;
        }
        return lines;
    }

    @Override
    public int getCompletionStage() {
        return 7;
    }

    @Override
    public List<String> getCompletedRewardsList() {
        return Lists.newArrayList("2 Quest Points", "4,662 Strength XP","4,662 Ranged XP", "4,662 Magic XP", "A damaged Book","Lighthouse dungeon access");
    }

    @Override
    public void giveQuestCompletionRewards() {
        player.totalqp +=2;

        player.getPA().addSkillXPMultiplied(4662.5, Skill.MAGIC.getId(), true);
        player.getPA().addSkillXPMultiplied(4662.5, Skill.STRENGTH.getId(), true);
        player.getPA().addSkillXPMultiplied(4662.5, Skill.RANGED.getId(), true);
player.getItems().addItemUnderAnyCircumstance(RUSTY_CASKET,1);
        player.updatePlayerPanel();
      //  player.getItems().addItemUnderAnyCircumstance(2528, 1);
        //player.getItems().addItemUnderAnyCircumstance(2528, 1);
    }

    @Override
    public boolean handleNpcClick(NPC npc, int option) {
        Consumer<Player> acceptquest = p -> {
            incrementStage();
            p.start(getLarrissaDialogue().npc(DialogueExpression.ANGER_1, "Well, we have to do something to get the lighthouse", "working again!", "Also, as you may have noticed, the storm", "that knocked the bridge out")
                    .npc(DialogueExpression.ANGER_1, "has trapped me on this causeway! You seem to have got", "here okay somehow, so if you could go and visit my", "cousin and get the spare key I left him.")
                    .npc(DialogueExpression.ANGER_1, "You can find him at the barbarian outpost south of here.")

            );

        };
        Consumer<Player> getgunnjornkey = p -> {
            incrementStage();
            p.getItems().addItem(LIGHTHOUSE_KEY,1);

        };
        Consumer<Player> autocomplete = p -> {
            if(p.donatorPoints < 10){
                p.sendMessage("You need @blu@10@bla@ Donation points to complete this quest.");
            } else {
                p.donatorPoints-=10;
                autocomplete();
            }


        };
        Consumer<Player> beginencounter = p -> {
            NPCSpawning.spawnNpc(player, 979,2517,10021, player.heightLevel, 1, 20, true,
                    true);

        };
        if (npc.getNpcId() == GUNNJORN) {
            if (getStage() == 1) {
                player.start(getGunnjornDialogue()
                        .player("Hello there. Your Gunnjorn right?")
                        .npc(DialogueExpression.ANGER_1, "Hmm... who wants to know?")
                        .player("Larrissa at the lighthouse needs a spare key.")
                        .npc(DialogueExpression.ANGER_1, "Oh yeah because of the storm thing.")
                                .itemStatement(LIGHTHOUSE_KEY,"Gunnjorn hands you a lighthouse key.").exit(getgunnjornkey)

                );
            }
        }
        if (npc.getNpcId() == JOSSIK) {//jossik in  the lighthouse
            if (option == 1) {
                if(getStage() == getCompletionStage()) {

                    if (player.getItems().playerHasItem(RUSTY_CASKET,1)) {
                        Consumer<Player> saradomin = p -> {
                            p.getPA().closeAllWindows();

                            p.getItems().replaceItem(p,RUSTY_CASKET, 3839);

                        };
                        Consumer<Player> guthix = p -> {

                            p.getPA().closeAllWindows();
                            p.getItems().replaceItem(p,RUSTY_CASKET, 3843);

                        };
                        Consumer<Player> zamorak = p -> {

                            p.getPA().closeAllWindows();
                            p.getItems().replaceItem(p,RUSTY_CASKET, 3841);
                        };
                        player.start(getJossikDialogue()
                                .player("I see you managed to escape from those monsters","intact!")
                                .npc(DialogueExpression.ANGER_1, "It seems I was not as injured as I thought I was after"," all! I must thank you again for all of your help! Now,"," about that casket you found on that monster's corpse...")
                                .player("I have it here. You said you might be able to tell me","something about it...")
                                .npc(DialogueExpression.ANGER_1,"I can indeed! Here, let me have a closer look..."," Yes! There is something written on it!")
                                .npc(DialogueExpression.ANGER_1,"It is very faint, however... Can you read it?")
                                .option(
                                        new DialogueOption("Saradomin",saradomin),
                                        new DialogueOption("Guthix", guthix),
                                        new DialogueOption("Zamorak",zamorak))

                        );
                    } else {
                        player.start(getJossikDialogue()
                                .npc(DialogueExpression.ANGER_1, "Did you find a rusty casket by any chance?"));

                    }
                }
            }
            if (option == 2) {
                    player.getShops().openShop(159);

            }
            if (option == 3) {
                player.getShops().openShop(165);

            }

            }
        if (npc.getNpcId() == JOSSIK_2) {//jossik in the dungeon
            if(getStage() < getCompletionStage()){
                player.start(getJossikDialogue()
                        .npc(DialogueExpression.ANGER_1, "Wow! What a battle!","Meet me on the second floor of the lighthouse","and let's see what's in that casket!")
                );
            }
            player.start(getJossikDialogue()
                    .npc(DialogueExpression.ANGER_1, "*cough*", "Please... please help me...","I think my leg is broken, and those creatures will be", "back any minute now!")
                    .player("I guess you're Jossik then...","What creatures are you talking about?")
                    .npc(DialogueExpression.ANGER_1, "I... I do not know", "I have never seen their like before!")
                    .npc(DialogueExpression.ANGER_1, "I was searching for information about my uncle Silas", "who vanished mysteriously from this lighthouse many", "months ago. I found the secret of that strange wall, and", "discovered that I could use it as a door, but when I")   
                    .npc(DialogueExpression.ANGER_1, "- watch out here it comes!").
                    exit(beginencounter)
            );
        }
        if (npc.getNpcId() == LARRISSA) {
            if (option == 1) {
                if (getStage() == 0) {
                    player.start(getLarrissaDialogue()
                            .player("Hello there.")
                            .npc(DialogueExpression.ANGER_1, "Oh, thank Armadyl! I am in such a worry...", "Please help me!")
                            .player("With what?")
                            .npc(DialogueExpression.ANGER_1, "Oh...it is terrible... horrible... My boyfriend lives here", "in this lighthouse, but I haven't seen him the last few", "Days! I think something terrible has happened!")
                                    .player("Maybe he just went on holiday or something? Must be", "pretty boring living in a lighthouse.")
                            .npc(DialogueExpression.ANGER_1, "That is terribly irresponsible! He is far too thoughtful", "for that! He would never leave it unattended! He would", "also never leave without telling me!")
                            .npc(DialogueExpression.ANGER_1, "Please, I know something terrible has happened to him...","I can sense it! Please.. please help me adventurer!")
                            .option(
                                    new DialogueOption("Accept Quest (@blu@Horror From the Deep@bla@)",acceptquest),
                                    new DialogueOption("Auto Complete (@blu@Horror From the Deep@bla@) (@blu@10@bla@ Donation Points)", autocomplete))
                    );
                } else if (getStage() == 1) {
                    player.start(getLarrissaDialogue()
                            .npc(DialogueExpression.ANGER_1, "Come back when you found the lighthouse key.")
                            .exit(plr -> { plr.getPA().closeAllWindows();
                            }));
                } else if (getStage() == 2) {
                    player.start(getLarrissaDialogue()
                            .player("I found the lighthouse key.")
                            .npc(DialogueExpression.HAPPY, "Thank you so much, " + player.getDisplayName() + "!", "Can you fix the bridge to the east?")
                          );
                } else if (getStage() == 3) {
                    player.start(getLarrissaDialogue()
                            .player("I fixed the bridge.")
                            .npc(DialogueExpression.HAPPY, "Can you look inside the lighthouse and find out", "why the light beacon at the top isn't working?")
                            .exit(plr -> {
                                incrementStage();
                                //giveQuestCompletionRewards();
                            }));
                } else if (getStage() == 4) {
                    player.start(getLarrissaDialogue()
                            .player("I fixed the bridge.")
                            .npc(DialogueExpression.HAPPY, "Did you find out how to fix the lighthouse?"));
                } else if (getStage() == 5) {
                    player.start(getLarrissaDialogue()
                            .npc(DialogueExpression.HAPPY, "I hear screams from the basement!"));
                } else if (getStage() == getCompletionStage()) {
                    player.start(getLarrissaDialogue().npc(DialogueExpression.HAPPY, "Thanks for killing that damned thing.", "Unfortunately there's more spawning, let me know when",
                            "you kill more and I'll give you some books."));
                }
            } else if (option == 2) {
                if (isQuestCompleted()) {
                    player.getShops().openShop(SHOP_ID);
                } else {
                    if (getStage() == 1) {
                        player.start(getLarrissaDialogue().npc(DialogueExpression.ANGER_1, "Are you retarded? Kill that fucking Dagannoth!"));
                    } else {
                        player.start(getLarrissaDialogue().npc(DialogueExpression.ANGER_1, "I don't have time for this right now!"));
                    }
                }
            }
            return true;
        }
        return false;
    }

public void checkwegotallinwall() {
        int count = 0;
    for (boolean b : player.itemsondoor) {
        if (b)
            count++;
    }
   // System.out.println("count: "+count);
    if(count == 6){
        incrementStage();
        player.sendMessage("The mechanism unlocks. You may now pass through the sides.");
    }
}
//		String shieldName = ItemAssistant.getItemName(shieldId).toLowerCase();
//		if (shieldName.contains("dragonfire")) {
    @Override
    public boolean handleItemonObject(int itemId, int objectId) {
        switch (objectId) {
            case 4543:
            case 4544:
                if(getStage() == 5) {


                    String swordname = ItemAssistant.getItemName(itemId).toLowerCase();
                    String arrowname = ItemAssistant.getItemName(itemId).toLowerCase();
                    if (swordname.contains("sword")) {
                        if (player.getItems().playerHasItem(itemId, 1))
                            if (player.itemsondoor[4]) {
                                player.sendMessage("You already put a sword into the door.");
                            } else {
                                player.getItems().deleteItem(itemId, 1);
                                player.sendMessage("You place the " + swordname + " into the slot in the wall.");
                                player.itemsondoor[4]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                    if (swordname.contains("arrow")) {
                        if (player.getItems().playerHasItem(itemId, 1))
                            if (player.itemsondoor[5]) {
                                player.sendMessage("You already put an arrowinto the door.");
                            } else {
                                player.getItems().deleteItem(itemId, 1);
                                player.sendMessage("You place the " + arrowname + " into the slot in the wall.");
                                player.itemsondoor[5]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                    if (itemId == FIRE_RUNE) {
                        if (player.getItems().playerHasItem(FIRE_RUNE, 1))
                            if (player.itemsondoor[0]) {
                                player.sendMessage("You already put a fire rune into the door.");
                            } else {
                                player.getItems().deleteItem(FIRE_RUNE, 1);
                                player.sendMessage("You place a fire rune into the slot in the wall.");
                                player.itemsondoor[0]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                    if (itemId == AIR_RUNE) {
                        if (player.getItems().playerHasItem(AIR_RUNE, 1))
                            if (player.itemsondoor[1]) {
                                player.sendMessage("You already put a air rune into the door.");
                            } else {
                                player.getItems().deleteItem(AIR_RUNE, 1);
                                player.sendMessage("You place an air rune into the slot in the wall.");
                                player.itemsondoor[1]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                    if (itemId == WATER_RUNE) {
                        if (player.getItems().playerHasItem(WATER_RUNE, 1))
                            if (player.itemsondoor[2]) {
                                player.sendMessage("You already put a water rune into the door.");
                            } else {
                                player.getItems().deleteItem(WATER_RUNE, 1);
                                player.sendMessage("You place a water rune into the slot in the wall.");
                                player.itemsondoor[2]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                    if (itemId == EARTH_RUNE) {
                        if (player.getItems().playerHasItem(EARTH_RUNE, 1))
                            if (player.itemsondoor[3]) {
                                player.sendMessage("You already put an earth rune into the door.");
                            } else {
                                player.getItems().deleteItem(EARTH_RUNE, 1);
                                player.sendMessage("You place an earth rune into the slot in the wall.");
                                player.itemsondoor[3]=true;
                                checkwegotallinwall();
                            }
                        return true;
                    }
                }
                return true;
            case 4588:
                if (player.getItems().playerHasItem(MOLTEN_GLASS,1) && player.getItems().playerHasItem(SWAMP_TAR,1) && player.getItems().playerHasItem(TINDERBOX)) {
                    player.startAnimation(898);
                    player.getItems().deleteItem(MOLTEN_GLASS, 1);
                    player.getItems().deleteItem(SWAMP_TAR, 1);
                    player.getDH().sendStatement("You managed to repair the lighthouse torch!");
                    player.getPA().object(4587, 2443, 4599, 0, 10);
                    incrementStage();
                    return true;
                } else {
                    player.sendMessage("You need molten glass, swamp tar, and a tinderbox.");
                }
            return true;
            case 4615://broken bridge
            case 4616://broken bridge
                if (getStage() == 2) {
                    if (player.getItems().playerHasItem(PLANK,2) && player.getItems().playerHasItem(STEEL_NAILS,60) && player.getItems().playerHasItem(HAMMER)) {
                        player.startAnimation(898);
                        player.getItems().deleteItem(PLANK, 2);
                        player.getItems().deleteItem(STEEL_NAILS, 60);
                        player.getDH().sendStatement("You have now made a makeshift walkway over the bridge.");
                        incrementStage();
                       return true;
                    } else {
                        player.sendMessage("You need a hammer, 2 planks, and 60 steel nails.");
                    }
                } else {

                }
                return true;
        }
        return false;
    }
    @Override
    public boolean handleObjectClick(WorldObject object, int option) {
        switch (object.getId()) {
            case 4569:

                if(option == 1){
                    player.getDH().sendOption2("Up", "Down");
                    player.dialogueAction = 4569;

                }
                if(option == 2){
                    player.climbLadderTo(new Position(2505, 3641, 2));
                }
                if(option == 3){
                    player.climbLadderTo(new Position(2508, 3640, 0));
                }
               return true;
            case 4577://lighthouse door
                if(getStage() >= 4 && getStage() <= 6){
                    if(player.objectX == 2445 && player.objectY == 4596) {//out of instanced version
                       // player.getPA().object(objectType, obX, obY, 3, 0);
                        player.getPA().movePlayer(2508, 3635, 0);
                    } else {
                        player.getPA().movePlayer(2445, 4596, 0);
                    }
//                    } else {
//
//                        if (player.getY() >= 3636)
//                            player.getAgilityHandler().move(player, 0, -1, 756, -1);
//                        else
//                            player.getAgilityHandler().move(player, 0, 1, 756, -1);
//                    }

                } else if(isQuestCompleted()){

                    PathFinder.getPathFinder().findRouteNoclip(player, 2509, player.absY > 3635  ?3635 : 3636, true, 0, 0);
                    player.setNewWalkCmdIsRunning(false);
        }
                return true;
            case 4568:
                if(player.objectX == 2442 && player.objectY == 4600){
                    player.getPA().movePlayer(2441,4601,2);
                }
                if(player.objectX == 2506 && player.objectY == 3640 && getStage() == getCompletionStage()){
                    player.getPA().movePlayer(2508,3640,1);
                }
                return true;
            case 4570:
                if(player.objectX == 2442 && player.objectY == 4601){
                    player.getPA().movePlayer(2441,4601,0);
                } else {
                    player.getPA().movePlayer(2508,3640,1);
                }
                return true;
            case 4412:
                if(getStage() == getCompletionStage()){
                    if(player.objectX == 2519 && player.objectY == 4618) {
                        player.climbLadderTo(new Position(2510, 3644, 0));
                    }
                    return true;
                }
                if(player.objectX == 2444 && player.objectY == 4604) {
                    player.climbLadderTo(new Position(2510, 3644, 0));

                }
                if(player.objectX == 2519 && player.objectY == 9914) {
                    player.climbLadderTo(new Position(2446, 4604, 0));
                }
                return true;
            case 4545:
                if (player.getAgilityHandler().hotSpot(player, player.objectX, player.absY >= player.objectY ? player.objectY  : player.objectY -1 )) {
             if(getStage() >=6) {
                //change x y to left of door
                     player.getPA().object(4545, player.objectX, player.objectY, 0, 0);
                     PathFinder.getPathFinder().findRouteNoclip(player, player.absX, player.absY >= player.objectY ? player.absY - 1 : player.absY + 1, true, 0, 0);
                     player.setNewWalkCmdIsRunning(false);
                     CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                         @Override
                         public void execute(CycleEventContainer container) {

                             player.getPA().object(4545, player.objectX, player.objectY, 3, 0);
                             container.stop();

                         }
                     }, 3);

                 } else {

                 }
             }
                return true;
            case 4546:
                if (player.getAgilityHandler().hotSpot(player, player.objectX, player.absY >= player.objectY ? player.objectY  : player.objectY -1 )) {
                    if (getStage() >= 6) {

                        player.getPA().object(4546, player.objectX, player.objectY, 0, 0);
                        PathFinder.getPathFinder().findRouteNoclip(player, player.absX, player.absY <= player.objectY ? player.absY + 1 : player.absY - 1, true, 0, 0);
                        player.setNewWalkCmdIsRunning(false);
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {

                                player.getPA().object(4546, player.objectX, player.objectY, 3, 0);
                                container.stop();

                            }
                        }, 3);

                    } else {

                    }
                }
                return true;
            case 4543:
            case 4544:

                player.getPA().showInterface(10116);
                return true;
            case 4380://iron ladder to go down
                if(getStage() >= 5 && getStage() < getCompletionStage()){
                    if(player.objectX == 2444 && player.objectY == 4604){
                        player.getPA().movePlayer(2518,9994,0);
                    } else {

//                        if (player.getY() >= 3636)
//                            player.getAgilityHandler().move(player, 0, -1, 756, -1);
//                        else
//                            player.getAgilityHandler().move(player, 0, 1, 756, -1);
                    }

                } else {
                    player.climbLadderTo(new Position(2519, 4617));
                }
                return true;
            case 4615://bridge horror from the deep
            case 4616:

                    if (player.getX() >= 2598) {
                        player.startAnimation(1115);
                      player.getPA().movePlayer(2596,3608);
                    } else {
                        player.getAgilityHandler().move(player, 4, 0, 756, -1);
                    }
                return true;
            case 4485:
                if(isQuestCompleted()){
                    player.climbLadderTo(new Position(2515, 4633));
                } else {

                    player.climbLadderTo(new Position(2515, 10008));
                }

                //NPCSpawning.spawnNpc(player, 4424, 2518,10009, player.heightLevel, 0, 7, false, false);
                return true;

            case 4413:
                if(isQuestCompleted()){
                    player.climbLadderTo(new Position(2515, 4629, 0));
                } else {
                    player.climbLadderTo(new Position(2515, 10005));
                }

                return true;
        }

        return false;
    }

    @Override
    public void handleNpcKilled(NPC npc) {
        if(npc.getNpcId() == 979){//level 100 dagannoth
            Consumer<Player> beginencounter = p -> {
                NPCSpawning.spawnNpc(player, 979,2517,10021, 0, 1, 20, true,
                        true);

            };           Consumer<Player> begindagmother = p -> {
                new DagannothMotherInstance(player, !isQuestCompleted()).init();

            };

            player.start(getJossikDialogue()
                    .player("Okay, now that the creature's dead we can get you out","of here.")
                    .npc(DialogueExpression.ANGER_1, "No you do not understand...")
                    .npc(DialogueExpression.ANGER_1, "That was not the creature that attacked me...")
                    .npc(DialogueExpression.ANGER_1, "That was one one of its babies...").
                    exit(begindagmother)
            );
        }
        if (Boundary.DAGANNOTH_MOTHER_HFTD.in(player) && Arrays.stream(DagannothMother.DAGANNOTH_MOTHER_TRANSFORMS).anyMatch(id -> npc.getNpcId() == id)) {
         //   Achievements.increase(player, AchievementType.SLAY_DAGGANOTH_MOTHER, 1);
       //     player.getInventory().addAnywhere(new ImmutableItem(CASKET_TO_BUY_BOOK, 1));
          //  player.sendMessage("You receive a casket from killing the beast.");
            //if (getStage() == 1) {
            player.getPA().movePlayer(2514, 4626, 0);
                incrementStage();
             //   player.start(new DialogueBuilder(player).player("I've defeated the damned beast, I should return to Jossik."));
         //   }
        }
    }
    private DialogueBuilder getJossikDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.JOSSIK);
        return builder;
    }
    private DialogueBuilder getLarrissaDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.LARRISSA);
        return builder;
    }
    private DialogueBuilder getGunnjornDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.GUNNJORN);
        return builder;
    }
}
