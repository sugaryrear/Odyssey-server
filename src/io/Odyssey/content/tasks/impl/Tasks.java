package io.Odyssey.content.tasks.impl;

import io.Odyssey.model.entity.player.mode.Mode;
import io.Odyssey.model.entity.player.mode.ModeType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum Tasks {

    //#Default
    NONE(true,false,false,0,"", ""),

    //#Skilling tasks

    CRAFT_AIR_RUNES(false,true,false,70,"Craft air runes","Level 1 Runecrafting"),

    MINE_IRON_ORE(false,true,false,30,"mine iron ore","Level 1 Mining"),
    //#PVM tasks
    MAN_FUCKER(new int[]{6987,6988,6989},false,false,true, 10, "Kill men in Lumbridge","None"),
    WOMEN_FUCKER(new int[]{6990},false,false,true, 10, "Kill women in Lumbridge","None"),
    ;

    private final boolean pvpTask;
    private final boolean skillingTask;
    private final boolean pvmTask;
    private final int taskAmount;
    private final String task;
    private final String[] taskRequirements;

    public boolean isPvpTask() {
        return pvpTask;
    }

    public boolean isSkillingTask() {
        return skillingTask;
    }

    public boolean isPvmTask() {
        return pvmTask;
    }

    public int getTaskAmount() {
        return taskAmount;
    }

    public String task() {
        return task;
    }

    public String[] getTaskRequirements() {
        return taskRequirements;
    }

    Tasks(boolean pvpTask, boolean skillingTask, boolean pvmTask, int taskAmount, String task, String... requirements) {
        this.pvpTask = pvpTask;
        this.skillingTask = skillingTask;
        this.pvmTask = pvmTask;
        this.taskAmount = taskAmount;
        this.task = task;
        this.taskRequirements = requirements;
        this.npcid = null;
    }
    Tasks(int[] npcid, boolean pvpTask, boolean skillingTask, boolean pvmTask, int taskAmount, String task, String... requirements) {
        this.npcid = npcid;
        this.pvpTask = pvpTask;
        this.skillingTask = skillingTask;
        this.pvmTask = pvmTask;
        this.taskAmount = taskAmount;
        this.task = task;
        this.taskRequirements = requirements;
    }
    private final int[] npcid;
    public int[] getnpcId() {
        return npcid;
    }




    /**
     * Picks a random PVP task from the Tasks enum.
     */
    public static Tasks randomPVPTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.skillingTask && !task.pvmTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }

    /**
     * Picks a random Skilling task from the Tasks enum.
     */
    public static Tasks randomSkillingTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.pvpTask && !task.pvmTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }

    /**
     * Picks a random PVM task from the Tasks enum.
     */
    public static Tasks randomPVMTask() {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != NONE && !task.skillingTask && !task.pvpTask).collect(Collectors.toList());
        Collections.shuffle(tasks);
        return tasks.get(0);
    }


}
