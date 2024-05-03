package io.Odyssey.content.questing;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.Odyssey.content.questing.LearningTheRopes.LearningTheRopesQuest;
import io.Odyssey.content.questing.hftd.HftdQuest;
import io.Odyssey.content.questing.MonkeyMadness.MonkeyMadnessQuest;
import io.Odyssey.content.questing.recipefordisaster.RecipeForDisasterQuest;
import io.Odyssey.model.Items;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import static io.Odyssey.model.Items.*;

public class Questing {

    private static final List<Integer> QUEST_LIST_INTERFACE_IDS = Collections.unmodifiableList(Lists.newArrayList(
            50615, 10135, 18306, 11132));

    private final Player player;
    private final List<Quest> questList;

    public Questing(Player player) {
        this.player = player;
        questList = Collections.unmodifiableList(Lists.newArrayList(
                new LearningTheRopesQuest(player),
                new HftdQuest(player),
                new RecipeForDisasterQuest(player),
        new MonkeyMadnessQuest(player)
        ));
    }

    public void updateQuestList() {
        for (int index = 0; index < questList.size(); index++)
        {
            Quest quest = questList.get(index);
           // player.getPA().sendString( quest.getName(), QUEST_LIST_INTERFACE_IDS.get(index));
        }
        player.getPA().sendString("Quest Points: "+player.totalqp, 620);
    }

    private String getQuestLineColor(Quest quest) {
       // return quest.getStage() == 0 ? "" : quest.getStage() == quest.getCompletionStage() ? "@gre@" : "@yel@";
        return "";
    }

    private void openQuestJournal(Quest quest) {
        player.getPA().openQuestInterface(quest.getName(), quest.getJournalText(quest.getStage()));
    }

    protected void openQuestCompletedJournal(Quest quest) {
        player.questsCompleted++;
        List<String> awarded = quest.getCompletedRewardsList();
        Preconditions.checkState(awarded.size() > 0 && awarded.size() < 7, "Need 1-5 lines for awarded information.");
        player.getPA().sendString("You have completed " + quest.getName() + "!", 12144);
        for (int index = 0; index < awarded.size(); index++)
            player.getPA().sendString(awarded.get(index), 12150 + index);
        for (int index = awarded.size(); index < 6; index++)
            player.getPA().sendString("", 12150 + index);
        player.getPA().sendString("", 12146);
        player.getPA().sendString("", 12147);
        player.getPA().showInterface(12140);

        int itemoninterface = quest.getName().equalsIgnoreCase("horror from the deep") ? RUSTY_CASKET : quest.getName().equalsIgnoreCase("monkey madness")  ? MONKEY : LAMP;
        player.getPA().itemOnInterface(12145, 240, itemoninterface);
        player.sendMessage("Congratulations, you have completed '" + quest.getName() + "'!");
        player.updatePlayerPanel();
    }

    public boolean clickButton(int buttonId) {
        for (int index = 0; index < QUEST_LIST_INTERFACE_IDS.size(); index++) {
            if (buttonId == QUEST_LIST_INTERFACE_IDS.get(index)) {
                if (questList.size() > index) {
                    openQuestJournal(questList.get(index));
                }

                return true;
            }
        }
        return false;
    }

    public boolean handleObjectClick(WorldObject object, int option) {
        for (Quest quest : questList) {
            if (quest.handleObjectClick(object, option)) {
                return true;
            }
        }
        return false;
    }

    public boolean handleItemonObject(int itemId, int objectId) {
        for (Quest quest : questList) {
            if (quest.handleItemonObject(itemId, objectId)) {
                return true;
            }
        }
        return false;
    }
    public boolean handleItemClick(int itemId) {
        for (Quest quest : questList) {
            if (quest.handleItemClick(itemId)) {
                return true;
            }
        }
        return false;
    }
    public boolean handleNpcClick(NPC npc, int option) {
        for (Quest quest : questList) {
            if (quest.handleNpcClick(npc, option)) {
                return true;
            }
        }
        return false;
    }

    public void handleNpcKilled(NPC npc) {
        questList.forEach(quest -> quest.handleNpcKilled(npc));
    }
    public void handleHelpTabActionButton(int button){
        questList.forEach(quest -> quest.handleHelpTabActionButton(button));
    }
    public void exchangeItemForPoints(Player player){
        questList.forEach(quest -> quest.exchangeItemForPoints(player));
    }
    public void updateQuestProgressOnLoad(String key, int value) {
        for (Quest quest : questList) {
            if (quest.getName().equalsIgnoreCase(key)) {
                quest.setStage(value);
                return;
            }
        }
    }

    public List<Quest> getQuestList() {
        return questList;
    }
}
