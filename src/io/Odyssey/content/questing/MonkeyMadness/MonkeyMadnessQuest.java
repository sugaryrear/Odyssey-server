package io.Odyssey.content.questing.MonkeyMadness;

import com.google.common.collect.Lists;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.questing.Quest;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.SkillLevel;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.mode.ModeType;

import java.util.List;
import java.util.function.Consumer;

import static io.Odyssey.model.Items.DIAMOND;

public class MonkeyMadnessQuest extends Quest {


    private static final int SHOP_ID = 191;

    public MonkeyMadnessQuest(Player player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Monkey Madness";
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
                lines.add("To start this I need to talk to @dre@King Nardone Shareen");
                lines.add("at the @dre@Tree Gnome Stronghold@bla@.");
                break;
            case 1:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("King Narnode wants me to check in with his spy at Ape Atoll.");
                lines.add("I should talk to Errdo to go there.");
                break;
            case 2:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("Errdo mentioned the 10th squad being near the");
                lines.add("monkey king. I should go check it out.");
                break;
            case 3:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("The spy gave me the 10th squad sigil.");
                lines.add("I should use it once I'm ready to fight the demon.");
                break;
            case 4:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("The demon is dead! I should report back to the king.");
                break;
            case 5:
                lines.add("");
                lines.add("");
                lines.add("");
                lines.add("I defeated Glough's demon and gained access to Ape Atoll!");
                break;
        }
        return lines;
    }

    @Override
    public int getCompletionStage() {
        return 5;
    }

    @Override
    public List<String> getCompletedRewardsList() {
        return Lists.newArrayList("3 Quest Points", "Access to Ape Atoll", "10,000 coins", "3 Diamonds");
    }

    @Override
    public void giveQuestCompletionRewards() {
       // System.out.println("twice?");
        player.totalqp +=3;

        player.getItems().addItemUnderAnyCircumstance(995, 10000);
        player.getItems().addItemUnderAnyCircumstance(1602, 3);
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

        if (npc.getNpcId() == Npcs.CAPTAIN_ERRDO) {
            if (option == 1) {
                if (getStage() == 0) {
                    player.start(getCaptainErrdo()
                            .player("Hello there.")
                            .npc(DialogueExpression.HAPPY, "Hello.")
                            .exit(plr -> {
                            }));
                } else if (getStage() == 1) {
                    player.start(getCaptainErrdo()
                            .player(DialogueExpression.DISTRESSED, "Do you know anything about the 10th squad?")
                            .npc("I sure do, I can take you to the island they are", "currently at. I think one of them already", "made there way towards the monkey King.")
                            .option(new DialogueOption("Yes, take me there.", p -> {
                                        p.getPA().startTeleport(2811, 2755, 0, "modern", false);
                                        incrementStage();
                                    }

                                    ),
                                    new DialogueOption("No.", p -> p.getPA().closeAllWindows())
                            ));
                } else if (getStage() >= 2 || isQuestCompleted()) {
                    player.start(getCaptainErrdo()
                            .npc(DialogueExpression.HAPPY, "Would you like to go back to Ape Atoll?")
                            .option(new DialogueOption("Yes, take me back.", p -> p.getPA().startTeleport(2811, 2755, 0, "modern", false)),
                                    new DialogueOption("No.", p -> p.getPA().closeAllWindows())

                            ));
                }
            } else if (option == 2) {
                if (isQuestCompleted()) {
                    player.getPA().startTeleport(2811, 2755, 0, "modern", false);
                } else {
                    if (getStage() >= 2) {
                          player.start(getCaptainErrdo()
                                .npc("Do you want to go back to the island?")
                                .option(new DialogueOption("Yes, take me there.", p -> {
                                            p.getPA().startTeleport(2811, 2755, 0, "modern", false);
                                            incrementStage();
                                        }

                                        ),
                                        new DialogueOption("No.", p -> p.getPA().closeAllWindows())
                                ));
                    } else {
                        player.start(getCaptainErrdo().npc(DialogueExpression.ANGER_1, "I don't have time for this right now!"));
                    }
                }
            }
            return true;
        }

        if (npc.getNpcId() == Npcs.GARKOR) {
            if (option == 1) {
                if (player.getItems().freeSlots() < 1) {
                    player.sendMessage("You need at least one free slot to talk to Garkor.");
                } else {
                    if (getStage() == 0) {
                        player.start(getGarkor()
                                .npc(DialogueExpression.DISTRESSED, "How did you get here?")
                        );
                    } else if (getStage() == 2) {
                        player.start(getGarkor()
                                .player("Hello?")
                                .npc(DialogueExpression.LAUGH_1, "A fine day you have chosen to visit this hellish island, human.")
                                .player("Good day to you Sergeant.", "I've been sent by your King Narnode to...")
                                .npc(DialogueExpression.SPEAKING_CALMLY, "Investigate the circumstances surrounding the mysterious", "disappearance of my squad. Yes, I know.")
                                .player("So what am I doing here?")
                                .npc(DialogueExpression.SPEAKING_CALMLY, "Long story short, we have to kill a demon.")
                                .npc(DialogueExpression.SPEAKING_CALMLY, "When you are ready to fight, use this to teleport to the demon.", "Good luck.")
                                .itemStatement(4035, "Garkor hands you the sigil.")
                                .exit(plr -> {
                                    incrementStage();
                                    player.getItems().addItem(4035, 1);
                                }));
                    } else if (getStage() == 3) {
                        player.start(getGarkor()
                                .npc(DialogueExpression.CALM, "Use the sigil when you are ready human.")
                        );
                    } else if (getStage() == 4) {
                        player.start(getGarkor()
                                .npc(DialogueExpression.CALM, "You're...alive??").option(new DialogueOption("Teleport back to the Grand Tree", p -> {
                                            p.getPA().startTeleport(2465, 3495, 0, "modern", false);

                                    p.getPA().closeAllWindows();

                                }),
                                new DialogueOption("Cancel", new Consumer<Player>() {
                                    @Override
                                    public void accept(Player player) {
                                        player.getPA().closeAllWindows();
                                    }
                                }))

                            );

                    } else if (getStage() == getCompletionStage()) {
                        player.start(getGarkor()
                                .npc(DialogueExpression.HAPPY, "Thank you human.")

                        );
                    }
                }
            }
                return true;

        }

        if (npc.getNpcId() == Npcs.KING_NARNODE_SHAREEN) {
            if (option == 1) {
                if (getStage() == 0) {
                    player.start(getKingNarnodeDialogue()
                            .player("Hello King, how fares the tree?")
                            .npc(DialogueExpression.DISTRESSED, "The tree? It is fine..")
                            .player("King, you look worried. Is anything the matter?")
                            .npc(DialogueExpression.ANGER_1, "Nothing in particular... Well actually, yes... there is.", "The 10th squad hasn't reported back from a recent mission.")
                            .option(new DialogueOption("Start Quest @blu@Monkey Madness", p -> {
                                        p.getPA().closeAllWindows();
                                        incrementStage();
                                    }),
                                    new DialogueOption("Auto Complete (@blu@Monkey Madnessbla@) (@blu@10@bla@ Donation Points)", autocomplete))

                            );
//                            .npc(DialogueExpression.HAPPY, "That would be great! Please report", "back to me when you find out anything!", "You should talk to the pilot at the top of the tree.")
//                            .exit(plr -> {
//                                incrementStage();
//                            }));
                } else if (getStage() == 1 || getStage() == 2) {
                    player.start(getKingNarnodeDialogue()
                            .npc(DialogueExpression.ANGER_1, "Please, go find the 10th squad!", "Talk to the pilot at the top of the tree."));
                } else if (getStage() == 3) {
                    player.start(getKingNarnodeDialogue()
                            .player(DialogueExpression.HAPPY,"I've found the 10th squad!")
                            .npc(DialogueExpression.HAPPY, "I know. They told me already.")
                            .player("...")
                            .npc(DialogueExpression.ANNOYED, "Don't you have a demon to kill?")
                            .exit(plr -> {
                                plr.getPA().closeAllWindows();
                            }));
                } else if (getStage() == 4) {
                    player.start(getKingNarnodeDialogue()
                            .npc(DialogueExpression.ANNOYED, "How is the mission going?", "It has been quite some time since I", "you on your way.")
                            .player("I've defeated the demon!")
                            .npc(DialogueExpression.HAPPY, "Well done, human! Please take this as a reward.", "Come by my shop any time you like.")
                            .exit(plr -> {
                                plr.getPA().closeAllWindows();
                                incrementStage();
                                //giveQuestCompletionRewards();
                            }));
                }
            } else if (option == 2) {
                if (isQuestCompleted()) {
                    player.getShops().openShop(SHOP_ID);
                } else {
                    if (getStage() == 3) {
                        player.start(getKingNarnodeDialogue().npc(DialogueExpression.ANGER_1, "Don't you have a demon to kill, human."));
                    } else {
                        player.start(getKingNarnodeDialogue().npc(DialogueExpression.ANGER_1, "and you are?"));
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleObjectClick(WorldObject object, int option) {
        switch (object.getId()) {

            case 4485:

                return true;
        }

        return false;
    }

    @Override
    public boolean handleItemClick(int itemId) {
        switch (itemId) {
            case 4587:
            case 20000:
                if (!isQuestCompleted()) {
                    player.sendMessage("You must have completed @blu@Monkey Madness@bla@ before you are able to wear this.");
                    return true;
                }
                return false;
            case 4035:
                if (getStage() == 3 && !player.getPosition().inWild()) {
                    player.start(getGarkor()
                            .option(new DialogueOption("Yes, teleport me. I am ready to fight.", p -> {
                                        new MMDemonInstance(player, !isQuestCompleted()).init();
                                    }
                                    ),
                                    new DialogueOption("No, I am not ready to fight.", p -> p.getPA().closeAllWindows())
                            ));
                    return true;
                } else if (player.getPosition().inWild()) {
                    player.sendMessage("Please use this outside of the wilderness.");
                } else {
                    player.sendMessage("You can no longer teleport back.");
                }
                return true;
        }

        return false;
    }

    @Override
    public void handleNpcKilled(NPC npc) {
        if (Boundary.MONKEY_MADNESS_DEMON.in(player) && npc.getNpcId() == Npcs.JUNGLE_DEMON) {
            if (getStage() == 3) {
                player.getPA().movePlayer(2809,2759,0);
                incrementStage();
              //  player.start(new DialogueBuilder(player).player("I should tell the king the good news."));
            }
        }
    }

    private DialogueBuilder getKingNarnodeDialogue() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.KING_NARNODE_SHAREEN);
        return builder;
    }
    private DialogueBuilder getCaptainErrdo() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.CAPTAIN_ERRDO);
        return builder;
    }
    private DialogueBuilder getGarkor() {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.GARKOR);
        return builder;
    }
}
