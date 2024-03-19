package io.Odyssey.content.questing;


import java.util.List;

import com.google.common.collect.Lists;
import io.Odyssey.model.Items;
import io.Odyssey.model.SkillLevel;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

public abstract class Quest {

    protected final Player player;
    private int stage;

    public abstract String getName();

    public abstract List<SkillLevel> getStartRequirements();

    public abstract List<String> getJournalText(int stage);

    public abstract int getCompletionStage();

    public abstract List<String> getCompletedRewardsList();

    public abstract void giveQuestCompletionRewards();

    public Quest(Player player) {
        this.player = player;
    }

    public List<String> getStartRequirementLines() {
        List<String> stringList = Lists.newArrayList();
        List<SkillLevel> startRequirements = getStartRequirements();
        if (startRequirements != null && startRequirements.size() > 0) {
            stringList.add("To start this quest you need:");
            for (SkillLevel requirement : startRequirements) {
                stringList.add(requirement.getLevel() + " " + requirement.getSkill().toString());
            }
        }
        return stringList;
    }

    public boolean isQuestCompleted() {
        return getStage() == getCompletionStage();
    }

    public boolean handleObjectClick(WorldObject object, int option) {
        return false;
    }
    public boolean handleItemClick(int itemId) {
        return false;
    }
    public boolean handleItemonObject(int itemId, int objectId) {
        return false;
    }
    public void handleHelpTabActionButton(int button) { }
    public void exchangeItemForPoints(Player c) { }

    public boolean handleNpcClick(NPC npc, int option) {
        return false;
    }

    public void handleNpcKilled(NPC npc) { }

    public void incrementStage() {
        stage++;
        player.getQuesting().updateQuestList();
        System.out.println("stage: "+stage);
        if (stage == getCompletionStage()) {
            player.getQuesting().openQuestCompletedJournal(this);
        }
    }
    public void autocomplete() {
        stage=getCompletionStage();
        player.getQuesting().updateQuestList();
        if (stage == getCompletionStage()) {
            player.getQuesting().openQuestCompletedJournal(this);
        }
    }

    public int getStage() {
        return stage;
    }

    /**
     * Only used on login, use {@link Quest#incrementStage()} for quest progress through actions.
     * @param stage the stage to set
     */
    public void setStage(int stage) {
        this.stage = stage;
    }
}
