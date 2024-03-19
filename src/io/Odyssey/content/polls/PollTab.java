package io.Odyssey.content.polls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.Odyssey.Server;
import io.Odyssey.model.definitions.ItemStats;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

/**
 * @author Grant_ | www.rune-server.ee/members/grant_ | 2/10/20
 * Functionality for a polling system
 */
public class PollTab {
    /*
    Variables
     */
    private static Poll poll;
    private static List<Poll> polls;
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(PollTab.class.getName());
    /**
     * Initializing all the poll data
     */
    public static void init() {
        try {
            Path path = Paths.get(Server.getDataDirectory() + "/cfg/poll/polls_backup.json");
            File file = path.toFile();

            JsonParser parser = new JsonParser();
            if (!file.exists()) {
                return;
            }

           // Object obj = parser.parse(new FileReader(file));
//            JsonArray jsonUpdates = (JsonArray) obj;
//
//            Type listType = new TypeToken<List<Poll>>() {
//            }.getType();
            try (FileReader fr = new FileReader(new File(Server.getDataDirectory() + "/cfg/poll/polls_backup.json"))) {
                polls = new Gson().fromJson(fr, new TypeToken<List<Poll>>() {
                }.getType());
                log.info("Loaded " + polls.size() + " polls.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

//            for(Poll poll1 : polls){
//                System.out.println("poll: "+poll1.getQuestion());
//                poll = poll1;
//            }
            //polls = new Gson().fromJson(jsonUpdates, listType);

            if (polls.size() > 0) { //Grab the most recent one, and if its active start using it
                Poll lastPoll = polls.get(polls.size() - 1);
                if (isPollActive(lastPoll)) {
                    poll = lastPoll;
                } else {
                    poll = new Poll("uhhh", 10,0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), System.currentTimeMillis(), Right.PLAYER);
                }
            } else {
                poll = new Poll("", 1, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), System.currentTimeMillis(), Right.PLAYER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while reading in polls");
            polls = new ArrayList<>();
        }
    }

    /**
     * Loads in a new poll
     */
    public static void reloadPoll() {
        try {
            Path path = Paths.get(Server.getDataDirectory() + "/cfg/poll/poll.json");
            File file = path.toFile();

            JsonParser parser = new JsonParser();
            if (!file.exists()) {
                return;
            }

            Object obj = parser.parse(new FileReader(file));
            JsonObject jsonUpdates = (JsonObject) obj;

            Type listType = new TypeToken<Poll>() {
            }.getType();

            poll = new Gson().fromJson(jsonUpdates, listType);
            poll.setStartTime(System.currentTimeMillis());
         //   System.out.println("s: "+System.currentTimeMillis());//set this in polls_backup.json
            List<Integer> votes = new ArrayList<>();
            for(int i = 0; i < poll.getAnswers().size(); i++) {
                votes.add(0);
            }
            poll.setVotes(votes);
            poll.setVoters(new ArrayList<>());
            poll.setTotalVotes(0);
            polls.add(poll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No poll found.");
            poll = new Poll("", 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), System.currentTimeMillis(), Right.PLAYER);
        }
    }


    public static void updatePollTabDisplay(Player player) {

        if (isPollActive(getPoll())) {
          //  player.setSidebarInterface(13, 21406);
          //  player.getPA().sendTabAreaOverlayInterface(21406);

            player.getPA().sendString(80152,"Current poll: @gre@Active");
        } else {
            player.getPA().sendString(80152,"Current poll: @red@Inactive");
         //   player.setSidebarInterface(13, 962);
        }
    }

    /**
     * Updates the entire poll interface
     * @param player
     */
    public static void updateInterface(Player player) {
        if (poll == null) {
            player.sendMessage("no poll is active.");
            return;
        }

        player.pollOption = -1;

        if (!isPollActive(poll) || poll.getQuestion().equalsIgnoreCase("")) {

            updatePollTabDisplay(player);
            player.getPA().sendFrame126(":pollHeight-1", 21406);
          //  player.getPA().sendFrame126("Poll: @red@Inactive", 21408);
            player.getPA().sendFrame126("---", 21409);
//System.out.println("here?");
            for(int i = 0; i < 5; i++) {
                player.getPA().sendFrame126("---", 21412 + (i * 3));
                player.getPA().sendConfig(30 + i, 0);
            }
            return;
        }

        if (!poll.getVoters().contains(player.getLoginName())) {
            player.sendMessage("changemiddlevotepanel##1");
            updatePollTabDisplay(player);

            poll.getAnswers().removeIf(String::isEmpty);
            player.getPA().sendFrame126(":pollHeight-" + poll.getQuestion().split("\\|").length, 21406);//how many  times it was split.
           // System.out.println("l: "+poll.getQuestion().split("\\|").length);
            player.getPA().sendFrame126(poll.getQuestion().replace("|", "\\n"), 21408);
            player.getPA().sendFrame126("Time Left to Vote: @whi@" + (getHoursLeft() == -1 ? "TBD" : getHoursLeft() <= 0 ? "~ 1" : "" + getHoursLeft()) + " hour(s)", 21499);

            for (int i = 0; i < 5; i++) {
                if (i < poll.getAnswers().size()) {
                    player.getPA().sendFrame126(poll.getAnswers().get(i).replace("|", "\\n"), 21411 + (i * 3));
                } else {
                    player.getPA().sendFrame126("---", 21411 + (i * 3));
                }
                player.getPA().sendConfig(30 + i, 0);
            }
        } else {
            player.sendMessage("changemiddlevotepanel##2");
           // player.setSidebarInterface(13, 21429);
            player.getPA().sendFrame126("Current Votes: (" + poll.getTotalVotes() + ")", 21430);

            int highestIndex = -1;
            int highestValue = 0;
            for (int i = 0; i < poll.getVotes().size(); i++) {
                if (poll.getVotes().get(i) > highestValue) {
                    highestValue = poll.getVotes().get(i);
                    highestIndex = i;
                }
            }
            player.getPA().sendFrame126(poll.getQuestion().replace("|", "\\n"), 21466);
            player.getPA().sendFrame126("Time Left to Vote: @whi@" + (getHoursLeft() == -1 ? "TBD" : getHoursLeft() <= 0 ? "~ 1" : "" + getHoursLeft()) + " hour(s)", 21467);
            for(int i = 0; i < 5; i++) {
                player.getPA().sendInterfaceHidden(0,21410 + (i * 3));
            }
            for(int i = 0; i < poll.getAnswers().size(); i++) {
                player.getPA().sendInterfaceHidden(1,21410 + (i * 3));
            }
            for(int i = 0; i < 5; i++) {
                if (i < poll.getAnswers().size()) {
                    double percentage = (poll.getTotalVotes() > 0 ? (100.0 * ((double) poll.getVotes().get(i) / (double) poll.getTotalVotes())) : 0);
                    player.getPA().sendFrame126((i == highestIndex ? "@gre@" : "") + poll.getAnswers().get(i).replace("|", "\\n") + ") " + poll.getVotes().get(i) + " votes (" + String.format("%.2f", percentage) + "%)", 21431 + i);
                    //   player.getPA().sendFrame126((i + 1) + ") " + poll.getAnswers().get(i).replace("|", "\\n"), 21436 + i);
                } else {
                    player.getPA().sendFrame126("", 21431 + i);
                    player.getPA().sendFrame126("", 21436 + i);
                }
            }
        }
    }

    /**
     * Handles all buttons on the interface
     * @param player
     * @param buttonId
     * @return
     */
    public static boolean handleActionButton(Player player, int buttonId) {
        if (buttonId >= 83162 && buttonId <= 83175) {
            for(int i = 0; i < 5; i++) {
                player.getPA().sendConfig(30 + i, 0);
            }

            int index = ((buttonId - 83162) / 3);
            if (poll == null || poll.getQuestion().equalsIgnoreCase("") || !isPollActive(poll) || index >= poll.getAnswers().size()) {
                player.sendMessage("You cant select this option right now.");
                return true;
            }
            player.getPA().sendConfig(30 + index, 1);
            player.pollOption = index;
            return true;
        }

        if (buttonId == 136_201) {
            vote(player);
            return true;
        }

        if (buttonId == 56229) {

           // clicktab(player);

        }
        if (buttonId == 56229) {

            player.sendMessage("changemiddlevotepanel##0");

        }
        if (buttonId == 136200) {

            player.sendMessage("changemiddlevotepanel##0");

        }
        if (buttonId == 56229) {

            updatePollTabDisplay(player);

        }

        if (buttonId == 57026) {
//            player.sendMessage("changemiddlevotepanel##1");
            updateInterface(player);

        }
        return false;
    }

    /**
     * Handles a player voting
     * @param player
     */
    public static void vote(Player player) {
        if (poll == null || poll.getQuestion().equalsIgnoreCase("")) {
            player.sendMessage("There is not an active poll right now.");
            return;
        }

        if (!isPollActive(poll)) {
            player.sendMessage("This poll is not active anymore.");
            return;
        }

        if (poll.getRight() != Right.PLAYER && player.getRights().getPrimary() != poll.getRight()) {
            player.sendMessage("You need to be a " + poll.getRight().toString() + "to participate in this poll.");
            return;
        }

        if (poll.getVoters().contains(player.getLoginName())) {
            player.sendMessage("You have already voted in this poll!");
            return;
        }

        if (player.pollOption == -1) {
            player.sendMessage("Please select an option first.");
            return;
        }

        int amount = poll.getVotes().get(player.pollOption);
        poll.getVotes().set(player.pollOption, amount + 1);
        poll.setTotalVotes(poll.getTotalVotes() + 1);
        poll.getVoters().add(player.getLoginName());
        //player.votePoints += 1;
        player.sendMessage("Thank you for voting!");
        updateInterface(player);
        savePolls();
    }

    /**
     * Gets the current poll
     * @return
     */
    public static Poll getPoll() {
        return poll;
    }

    /**
     * Saves all polls
     */
    public static void savePolls() {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = prettyGson.toJson(polls);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(Server.getDataDirectory() + "/cfg/poll/polls_backup.json")));
            bw.write(prettyJson);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a poll is active
     * @param poll
     * @return
     */
    public static boolean isPollActive(Poll poll) {
        if (poll.getQuestion().isEmpty()) {
            System.out.println("poll: "+poll.getQuestion());
            return false;
        }
        if (poll.getHours() == 0) {
            System.out.println("poll here?: "+poll.getQuestion());
            return true;
        }
        long milliseconds = poll.getHours() * 3600000;
        return poll.getStartTime() + milliseconds > System.currentTimeMillis();
    }

    /**
     * Gets the number of hours left on a poll
     * @return
     */
    public static int getHoursLeft() {
        if (poll.getHours() == 0) {
            return -1;
        }
        long millisecondsLeft = System.currentTimeMillis() - poll.getStartTime();
        return (int) (((double) (poll.getHours())) - (millisecondsLeft / 3600000.0));
    }

    /**
     * Ends a poll manually
     */
    public static void endPollManually() {
        poll = new Poll("", 0, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), System.currentTimeMillis(), Right.PLAYER);
    }
}
