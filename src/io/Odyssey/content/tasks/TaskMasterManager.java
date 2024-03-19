package io.Odyssey.content.tasks;

import io.Odyssey.content.tasks.impl.Tasks;
import io.Odyssey.content.tasks.rewards.TaskReward;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Color;
import io.Odyssey.util.Misc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskMasterManager {

    public static final int PRICE_TO_RESET_TASK = 1000;
    private final Player player;

    public TaskMasterManager(Player player) {
        this.player = player;
    }

    public static int getTodayDate() {
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        return (month * 100 + day);
    }

    public void increase(Tasks taskToIncrease) {
        increase(taskToIncrease, 1);
    }

    /**
     * Activates, increases the task and also handles completion.
     *
     * @param taskToIncrease The task
     */
    public void increase(Tasks taskToIncrease, int increaseBy) {
        // Safety checks
        if (taskToIncrease == null) {
            return;
        }

        Tasks task = player.TASK;
        if (task == null) return;

        if (task == Tasks.NONE) {
            return;
        }

        //Can't increase during tourneys
//        if(player.inActiveTournament() || player.isInTournamentLobby()) {
//            return;
//        }

        if (task == taskToIncrease) {

            int before = player.TASK_AMOUNT;

            var current = player.TASK_AMOUNT + increaseBy;
            var completeAmount = player.TASK_COMPLETE_AMOUNT;
            if (current >= completeAmount)
                current = completeAmount;
            player.TASK_AMOUNT = current;
            //  player.putAttrib(TASK_AMOUNT, current);

            int after = player.TASK_AMOUNT;

            if (after != before) {
                if (task.isPvpTask() || task.isPvmTask()) {
                    var taskType = task.isPvpTask() ? "PVP" : "PVM";
                    player.sendMessage(Color.GRASS.wrap("Your kill counts towards your " + Color.BLUE.wrap(taskType) + " <col=03700C>task. Kills left: <col=A30072>" + current + "</col> <col=03700C>/ <col=A30072>" + completeAmount + "</col><col=03700C>."));
                } else if (task.isSkillingTask()) {
                    player.sendMessage(Color.GRASS.wrap("This actions counts towards your " + Color.BLUE.wrap("skilling") + " <col=03700C>task. Actions left: <col=A30072>" + current + "</col> <col=03700C>/ <col=A30072>" + completeAmount + "</col><col=03700C>."));
                }

                //Task completed
                if (after >= completeAmount) {
                    player.sendMessage("@blu@You've completed your task, you can now claim your reward!");
                    int tasks_completed = (Integer) player.TASKS_COMPLETED + 1;
                    player.TASKS_COMPLETED = tasks_completed;

                    //    player.sendMessage("You have now completed <col=" + Color.BLUE.getColorValue() + ">" + tasks_completed + "</col> tasks.");
                    //      player.putAttrib(TASK, Tasks.NONE);
                    player.CAN_CLAIM_TASK_REWARD = true;
                }
            }
        }
    }

    public void resetTask() {
        player.TASK = Tasks.NONE;
        player.TASK_AMOUNT = 0;
        player.TASK_COMPLETE_AMOUNT = 0;

        player.sendMessage(Color.RED.wrap("Your task has been reset."));
    }

    public boolean hasTask() {
        Tasks task = player.TASK;
        if (task == null)
            return false;
        return task != Tasks.NONE;
    }

    public void giveTask(boolean pvpTask, boolean skillingTask, boolean pvmTask) {

        if (hasTask()) {
            player.sendMessage("You already have a task.");
            player.getPA().closeAllWindows();
            return;
        }

        //Randomize a task
        Tasks randomTask = Tasks.NONE;

        if (pvpTask) {
            randomTask = Tasks.randomPVPTask();
        } else if (skillingTask) {
            randomTask = Tasks.randomSkillingTask();
        } else if (pvmTask) {
            randomTask = Tasks.randomPVMTask();
        }

        if (randomTask != null) {
            //Save the enum type
            player.TASK = randomTask;
            player.TASK_AMOUNT = 0;
            player.TASK_COMPLETE_AMOUNT = randomTask.getTaskAmount();

        }
        open();
    }


    public Tasks gettaskbynpcid(int npcid) {
        List<Tasks> tasks = Arrays.stream(Tasks.values()).filter(task -> task != Tasks.NONE && task.isPvmTask() && IntStream.of(task.getnpcId()).anyMatch(type -> type == npcid)).collect(Collectors.toList());
        return tasks.size() == 0 ? null : tasks.get(0);
    }

    public void open() {
        // Tasks task = player.getAttribOr(TASK, Tasks.NONE);
        Tasks task = player.TASK;
        if (task == null)
            return;
        //   player.getInterfaceManager().open(54731);
        player.getPA().showInterface(54731);
        //    player.getPacketSender().sendString(54733, "Task Manager");

        var completed = player.TASK_AMOUNT;
        var completionAmount = player.TASK_COMPLETE_AMOUNT;
        if (completionAmount == 0)
            completionAmount = 1;
        var progress = (int) (completed * 100 / (double) completionAmount);

        player.getPA().sendString(54762, "(" + progress + "%) (" + completed + "/" + completionAmount + ")");
        player.sendMessage("sendprogbar##54760##" + completed + "##" + completionAmount);
        //    player.getPA().sendProgressBar(54760, progress);

        //     player.getPacketSender().sendItemOnInterface(54759, TaskReward.getPossibleRewards());
        player.getItems().sendItemContainer(54759, TaskReward.getPossibleRewards());

//        for (int i = 54738; i < 54741; i++) {
//            player.getPA().sendString(i, "");//Clear old
//        }

//        StringBuilder stringBuilder = new StringBuilder();
//
//        if (task.getTaskRequirements() != null) {
//            stringBuilder.append("@lre@                 Requirements:");
//            for (String s : task.getTaskRequirements()) {
//                stringBuilder.append(s).append("@whi@ ");
//            }
//
//            stringBuilder.append("\\n@lre@                         Task(s): ").append("@whi@\\n"+task.task());
//        }

        player.getPA().sendString(54738, task.task());
        player.getPA().sendString(54740, task.getTaskRequirements()[0]);
    }

    public boolean checkfordate() {
        var dailyTaskDate = player.DAILY_TASK_DATE;
        if (dailyTaskDate == getTodayDate()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime nextDayTime = currentDateTime.toLocalDate().plusDays(1).atTime(0, 0);
            Duration duration = Duration.between(currentDateTime, nextDayTime);
            long millisecondsDifference = duration.toMillis();
            player.sendMessage("A new daily task will be available in "+Misc.howMuchTimeLeft_dailytask(millisecondsDifference));
           player.getPA().closeAllWindows();
            return false;
        }
    return true;
}
    public void onLogin() {
        Tasks task = player.TASK;
//        var dailyTaskDate = player.DAILY_TASK_DATE;
//        var completedDailyTask = player.COMPLETED_DAILY_TASK;
//        if (dailyTaskDate == getTodayDate()) {
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            LocalDateTime nextDayTime = currentDateTime.toLocalDate().plusDays(1).atTime(0, 0);
//            Duration duration = Duration.between(currentDateTime, nextDayTime);
//            long millisecondsDifference = duration.toMillis();
//            player.sendMessage("A new daily task will be available in "+Misc.howMuchTimeLeft_dailytask(millisecondsDifference));
//           // player.getPA().closeAllWindows();
//            return;
//        }

if(!checkfordate()){
    return;
}

        if(task == null || task == Tasks.NONE){
            player.sendMessage("A new daily task is available from the task master at home!");
            return;
        }
        final int completed = player.TASK_AMOUNT;
        final int completeAmt = player.TASK_COMPLETE_AMOUNT;
        player.sendMessage("Current daily task: " + Misc.format(completed) + "/" + completeAmt + " "+task.task());
    }

    public void claimReward() {
        if(!hasTask()) {
            player.sendMessage("There are no rewards pending, you have no task.");
            return;
        }

        boolean canClaimReward = player.CAN_CLAIM_TASK_REWARD;

        if (!canClaimReward) {
            final int completed = player.TASK_AMOUNT;
            final int completeAmt = player.TASK_COMPLETE_AMOUNT;
            player.sendMessage("Your task isn't finished yet, you still have to complete (" + Misc.format(completed) + "/" + completeAmt + ").");
            return;
        }
        player.CAN_CLAIM_TASK_REWARD = false;

        TaskReward.reward(player);

//        AchievementsManager.activate(player, Achievements.TASK_MASTER_I, 1);
//        AchievementsManager.activate(player, Achievements.TASK_MASTER_II, 1);
//        AchievementsManager.activate(player, Achievements.TASK_MASTER_III, 1);
        //    player.getInterfaceManager().close();
        player.getPA().closeAllWindows();

        //Reset old task
//        player.putAttrib(TASK_AMOUNT, 0);
//        player.putAttrib(TASK_COMPLETE_AMOUNT, 0);
//        player.putAttrib(TASK, Tasks.NONE);
//        player.putAttrib(PREVIOUS_TASK, Tasks.NONE);
//        player.putAttrib(DAILY_TASK_DATE,getTodayDate());
//        player.putAttrib(COMPLETED_DAILY_TASK,true);

        player.TASK_AMOUNT = 0;
        player.TASK_COMPLETE_AMOUNT = 0;
        player.TASK = Tasks.NONE;
        player.DAILY_TASK_DATE = getTodayDate();
        player.COMPLETED_DAILY_TASK = true;

    }
}