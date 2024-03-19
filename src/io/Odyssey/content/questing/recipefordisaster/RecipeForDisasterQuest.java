package io.Odyssey.content.questing.recipefordisaster;

import com.google.common.collect.Lists;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.questing.Quest;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.SkillLevel;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static io.Odyssey.model.Items.BARROWS_GLOVES;
import static io.Odyssey.model.Items.HARDLEATHER_GLOVES;

public class RecipeForDisasterQuest extends Quest {


    private static final int SHOP_ID = 190;

    public RecipeForDisasterQuest(Player player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Recipe for Disaster";
    }

    @Override
    public List<SkillLevel> getStartRequirements() {
        return Lists.newArrayList();
    }

    @Override
    public List<String> getJournalText(int stage) {
        List<String> lines = Lists.newArrayList();
        switch (stage) {
            case 0:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I can start this quest by speaking to the @dre@Cook@bla@ inside");
                lines.add("@dre@Lumbridge Castle@bla@.");
                lines.add("To complete this quest I need:");
                lines.add("@dre@Ability to defeat level 100+ enemies");
                lines.add("@dre@without using protection prayers");
                break;
            case 1:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I must enter the Portal to begin the encounter.");
                lines.add("");
                break;
//            case 2:
//                lines.add("The Gnome Glider mentioned the 10th squad being near the");
//                lines.add("monkey king. I should go check it out.");
//                break;
//            case 3:
//                lines.add("The spy gave me the 10th squad sigil.");
//                lines.add("I should use it once I'm ready to fight the demon.");
//                break;
            case 7:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I defeated the Culinaromancer and gained access");
                lines.add("to the chest in Lumbridge castle basement.");
                break;
        }
        return lines;
    }

    @Override
    public int getCompletionStage() {
        return 8;
    }

    @Override
    public List<String> getCompletedRewardsList() {
        return Lists.newArrayList("1 Quest Point", "An Antique Lamp", "Full Culinaromancer's", "Chest Access");
    }

    @Override
    public void giveQuestCompletionRewards() {
        player.totalqp +=1;
        player.getItems().addItemUnderAnyCircumstance(2528, 1);
        player.updatePlayerPanel();
    }

    @Override
    public boolean handleNpcClick(NPC npc, int option) {
        Consumer<Player> autocomplete = p -> {
            if(p.donatorPoints < 10){
                p.sendMessage("You need @blu@10@bla@ Donation points to complete this quest.");
            } else {
                p.donatorPoints-=10;
                autocomplete();
            }


        };

        if (npc.getNpcId() == Npcs.COOK) {
            if (option == 1) {
 if (getStage() == 0) {
                    player.start(getCookDialogue()
                            .npc(DialogueExpression.DISTRESSED, "Can you check out whats happening in the dining room?")
                            .option(new DialogueOption("Start Quest @blu@Recipe for Disaster", p -> {
                                      //  p.getPA().startTeleport(2811, 2755, 0, "modern", false);
                                      p.getPA().closeAllWindows();
                                        incrementStage();
                                    }

                                    ),
                                    new DialogueOption("Auto Complete (@blu@Recipe for Disaster@bla@) (@blu@10@bla@ Donation Points)", autocomplete))

                            );
                } else if (getStage() >= 2 || isQuestCompleted()) {
                    player.start(getCookDialogue()
                            .npc(DialogueExpression.HAPPY, "That was wild!")

                            );
                }
            }

            return true;
        }

//        if (npc.getNpcId() == Npcs.GARKOR) {
//            if (option == 1) {
//                if (player.getItems().freeSlots() < 1) {
//                    player.sendMessage("You need at least one free slot to talk to Garkor.");
//                } else {
//                    if (getStage() == 0) {
//                        player.start(getGarkor()
//                                .npc(DialogueExpression.DISTRESSED, "How did you get here?")
//                        );
//                    } else if (getStage() == 2) {
//                        player.start(getGarkor()
//                                .player("Hello?")
//                                .npc(DialogueExpression.LAUGH_1, "A fine day you have chosen to visit this hellish island, human.")
//                                .player("Good day to you Sergeant.", "I've been sent by your King Narnode to...")
//                                .npc(DialogueExpression.SPEAKING_CALMLY, "Investigate the circumstances surrounding the mysterious", "disappearance of my squad. Yes, I know.")
//                                .player("So what am I doing here?")
//                                .npc(DialogueExpression.SPEAKING_CALMLY, "Long story short, we have to kill a demon.")
//                                .npc(DialogueExpression.SPEAKING_CALMLY, "When you are ready to fight, use this to teleport to the demon.", "Good luck.")
//                                .itemStatement(4035, "Garkor hands you the sigil.")
//                                .exit(plr -> {
//                                    incrementStage();
//                                    player.getItems().addItem(4035, 1);
//                                }));
//                    } else if (getStage() == 3) {
//                        player.start(getGarkor()
//                                .npc(DialogueExpression.CALM, "Use the sigil when you are ready human.")
//                        );
//                    } else if (getStage() == getCompletionStage()) {
//                        player.start(getGarkor()
//                                .npc(DialogueExpression.HAPPY, "Thank you human.")
//
//                        );
//                    }
//                }
//            }
//            return true;
//
//        }

//        if (npc.getNpcId() == Npcs.KING_NARNODE_SHAREEN) {
//            if (option == 1) {
//                if (getStage() == 0) {
//                    player.start(getKingNarnodeDialogue()
//                            .player("Hello King, how fares the tree?")
//                            .npc(DialogueExpression.DISTRESSED, "The tree? It is fine..")
//                            .player("King, you look worried. Is anything the matter?")
//                            .npc(DialogueExpression.ANGER_1, "Nothing in particular... Well actually, yes... there is.", "The 10th squad hasn't reported back from a recent mission.")
//                            .player("Maybe I can find them.")
//                            .npc(DialogueExpression.HAPPY, "That would be great! Please report", "back to me when you find out anything!", "You should talk to the pilot at the top of the tree.")
//                            .exit(plr -> {
//                                incrementStage();
//                            }));
//                } else if (getStage() == 1 || getStage() == 2) {
//                    player.start(getKingNarnodeDialogue()
//                            .npc(DialogueExpression.ANGER_1, "Please, go find the 10th squad!", "Talk to the pilot at the top of the tree."));
//                } else if (getStage() == 3) {
//                    player.start(getKingNarnodeDialogue()
//                            .player(DialogueExpression.HAPPY,"I've found the 10th squad!")
//                            .npc(DialogueExpression.HAPPY, "I know. They told me already.")
//                            .player("...")
//                            .npc(DialogueExpression.ANNOYED, "Don't you have a demon to kill?")
//                            .exit(plr -> {
//                                plr.getPA().closeAllWindows();
//                            }));
//                } else if (getStage() == 4) {
//                    player.start(getKingNarnodeDialogue()
//                            .npc(DialogueExpression.ANNOYED, "How is the mission going?", "It has been quite some time since I", "you on your way.")
//                            .player("I've defeated the demon!")
//                            .npc(DialogueExpression.HAPPY, "Well done, human! Please take this as a reward.", "Come by my shop any time you like.")
//                            .exit(plr -> {
//                                plr.getPA().closeAllWindows();
//                                incrementStage();
//                                giveQuestCompletionRewards();
//                            }));
//                }
//            } else if (option == 2) {
//                if (isQuestCompleted()) {
//                    player.getShops().openShop(SHOP_ID);
//                } else {
//                    if (getStage() == 3) {
//                        player.start(getKingNarnodeDialogue().npc(DialogueExpression.ANGER_1, "Don't you have a demon to kill, human."));
//                    } else {
//                        player.start(getKingNarnodeDialogue().npc(DialogueExpression.ANGER_1, "and you are?"));
//                    }
//                }
//            }
//            return true;
//        }
        return false;
    }

    @Override
    public boolean handleObjectClick(WorldObject object, int option) {
        switch (object.getId()) {
            case 12365://start encounter
                if(getStage() < getCompletionStage()){
                    player.start(getCookDialogue()
                            .option(new DialogueOption("Begin Encounter", p -> {
                                        //  p.getPA().startTeleport(2811, 2755, 0, "modern", false);
                                        p.getPA().closeAllWindows();
                                        new RFDInstance(player, !isQuestCompleted()).init();
                                    }

                                    ),
                                    new DialogueOption("Cancel", p -> p.getPA().closeAllWindows())
                            ));
                }


                break;
            //door to cooks
            case 12348:
                if(getStage() < 1){
                    player.sendMessage("I should probably talk to the cook first before barging in.");
                    return false;
                }

                if(getStage() == 1){
                    incrementStage();
                }
                player.getPA().movePlayer(1862,5317);
                return true;
            case 14880://trapdoor
                AgilityHandler.delayEmote(player, "CLIMB_DOWN", 3209,9617, 0, 2);
                break;
            case 12351:
                player.getPA().movePlayer(3207,3217);
                return true;
        }

        return false;
    }

    @Override
    public boolean handleItemClick(int itemId) {
        if(itemId >= HARDLEATHER_GLOVES && itemId <= BARROWS_GLOVES) {
            if (!isQuestCompleted()) {
                player.sendMessage("You must have completed @blu@Recipe for Disaster@bla@ to wear this.");
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleNpcKilled(NPC npc) {
        Optional<RFDInstance> rfdinstance = NPCHandler.getRFDInstance(npc);
      //  this.hydraInstance = hydraInstance.orElse(null);
        if (Boundary.RFD_FIGHT_AREA.in(player)) {
           // if (getStage() == 1) {//first spawn

                incrementStage();
               // System.out.println("stage: "+getStage());
if(getStage() == getCompletionStage()){
    player.getPA().movePlayer(1862,5317, 0);
    return;
}
                    if(rfdinstance.isPresent()){
                        rfdinstance.get().spawnnext(npc.getNpcId());
                }
             //   new MMDemonInstance(player, !isQuestCompleted()).init();
             //   spawnnext();
             //   player.start(new DialogueBuilder(player).player("I should tell the king the good news."));
            }
//            if (getStage() == 2) {//first spawn
//                incrementStage();
//                new MMDemonInstance(player, !isQuestCompleted()).init();
//             //   spawnnext();
//             //   player.start(new DialogueBuilder(player).player("I should tell the king the good news."));
//            }
    //    }
    }

    private DialogueBuilder getCulinaromancerDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.CULINAROMANCER_10);
        return builder;
    }
    private DialogueBuilder getCookDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.COOK);
        return builder;
    }
    private DialogueBuilder getGypsyDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.GYPSY_ARIS);
        return builder;
    }
}
