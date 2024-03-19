package io.Odyssey.net.discord.discordintegration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.Odyssey.Server;
import io.Odyssey.content.skills.DoubleExpScroll;
import io.Odyssey.model.entity.player.ClientGameTimer;
import io.Odyssey.model.entity.player.Player;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class DiscordIntegration {

    public static Map<String, Long> connectedAccounts = new HashMap<>();
    public static ArrayList<Long> disableMessage = new ArrayList<>();

    public static Map<String, Long> idForCode = new HashMap<>();

    public static String generateCode(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }

    public static void sendPrivateMessage(User user, TextChannel c, String content) {

        ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER, (error) -> {
            c.sendMessage(user.getAsMention() + " You must enable your private messages first!").queue();
        });

        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(content).queue(null, handler);
        });
    }


//    public static void sendPMS(String content) {
//
//        System.out.println("sending discord pms");
//
//        Guild guild = Bot.discord.getGuildById(707331223374266378L);
//        for (Entry<String, Long> entry : DiscordIntegration.connectedAccounts.entrySet()) {
//            Optional<Player> player = World.getWorld().getPlayerByName(entry.getKey());
//            if (player == null)
//                continue;
//            Member member = guild.getMemberById(entry.getValue());
//            if (member == null) {
//                continue;
//            }
//            if (disableMessage.contains(entry.getValue()))
//                continue;
//
//            User user = member.getUser();
//
//            if (user != null) {
//                ErrorHandler handler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER, (error) -> {
//                    // c.sendMessage(user.getAsMention() + " You must enable your private messages first!").queue();
//                });
//
//                user.openPrivateChannel().queue((channel) -> {
//                    channel.sendMessage("A new update has just released on Lunite!").queue(null, handler);
//                });
//                user.openPrivateChannel().queue((channel) -> {
//                    channel.sendMessage("https://lunite.io/updates/33.php").queue(null, handler);
//                });
//            }
//        }
//    }

    public static void integrateAccount(Player player, String code) {

        if (player.getDiscordUser() > 0L) {
            player.sendMessage("You already have a connected discord account!");
            return;
        }

        if (!idForCode.containsKey(code)) {
            player.sendMessage("You have entered an invalid code! Try again.");
            return;
        }

        long userId = idForCode.get(code);

        idForCode.remove(code);

        if (connectedAccounts.containsValue(userId) &&
                connectedAccounts.get(player.getDisplayName()) !=  userId) {
            player.sendMessage("This discord account is already linked to another player!");
            return;
        }
player.getPA().closeAllWindows();
        String name = Bot.discord.getUserById(userId).getAsTag();
       // player.getInventory().add(new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,1));
        DoubleExpScroll.openScroll(player);
        player.sendMessage("@red@You have activated 1 hour of bonus experience.");
        player.getPA().sendGameTimer(ClientGameTimer.BONUS_XP, TimeUnit.MINUTES, 60);
        player.sendMessage("You have connected the discord account '" + name + "'.");
        connectedAccounts.put(player.getDisplayName(), userId);
        player.setDiscordUser(userId);
        player.setDiscordTag(name);
        player.getPA().sendFrame126(player.getDiscordTag(), 80114);
        DiscordIntegration.saveConnectedAccounts();


    }


    public static void setIntegration(Player player) {

        if (player.getDiscordUser() > 0L) {
            connectedAccounts.put(player.getDisplayName(), player.getDiscordUser());
            player.setDiscordUser(player.getDiscordUser());
            player.setDiscordTag(player.getDiscordTag());
            player.getPA().sendFrame126(player.getDiscordTag(), 80114);
        } else {
            player.getPA().sendFrame126("", 80114);
        }
    }

    public static void loadConnectedAccounts() {
        File file = new File(Server.getDataDirectory() + "/cfg/discord/discordConnectedAccounts.json");
       // File file = new File("data/saves/discord/discordConnectedAccounts.json");

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);
            if (reader.has("connectedAccounts")) {
                Map<String, Long> accounts = builder.fromJson(reader.get("connectedAccounts"),
                        new TypeToken<Map<String, Long>>() {
                        }.getType());

                connectedAccounts = accounts;
            }

            if (reader.has("disableMessage")) {
                Long[] pricesData = builder.fromJson(reader.get("disableMessage"), Long[].class);
                for (Long data : pricesData) {
                    disableMessage.add(data);
                }
            }

            System.out.println("Loaded Discord Connected Accounts!");
        } catch (Exception e) {
            System.out.println("Error Loading Discord Connected Accounts! "+e.getMessage());
        }
    }

    public static void saveConnectedAccounts() {//when does this get called? atm im calling on saveall, shutdownhook etc. Just put it wherever u normally save everything like POS etckkits all setup?

        File file = new File(Server.getDataDirectory() + "/cfg/discord/discordConnectedAccounts.json");
        try (FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();

            object.add("connectedAccounts", builder.toJsonTree(connectedAccounts));

            object.add("disableMessage", builder.toJsonTree(disableMessage));


            writer.write(builder.toJson(object));
            writer.close();
            System.out.println("Saved Discord Connected Accounts!");
        } catch (Exception e) {
            System.out.println("Error Saving Discord Connected Accounts!");
        }
    }



    public static void sendMessage(String message, long channel) {

        Bot.discord.getTextChannelById(channel).sendMessage(message).queue();
    }


}
