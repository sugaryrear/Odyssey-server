package io.Odyssey.sql;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

public class Hiscores extends Database {


    static final String HOST = "198.12.12.226";
    static final String USER = "zodianx2_user1";
    static final String PASS = "thepassword12345";
    static final String DATABASE = "zodianx2_hiscores";

    static String generateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO hs_users (");
        sb.append("username, ");
        sb.append("rights, ");
        sb.append("overall_xp, ");
        sb.append("attack_xp, ");
        sb.append("defence_xp, ");
        sb.append("strength_xp, ");
        sb.append("constitution_xp, ");
        sb.append("ranged_xp, ");
        sb.append("prayer_xp, ");
        sb.append("magic_xp, ");
        sb.append("cooking_xp, ");
        sb.append("woodcutting_xp, ");
        sb.append("fletching_xp, ");
        sb.append("fishing_xp, ");
        sb.append("firemaking_xp, ");
        sb.append("crafting_xp, ");
        sb.append("smithing_xp, ");
        sb.append("mining_xp, ");
        sb.append("herblore_xp, ");
        sb.append("agility_xp, ");
        sb.append("thieving_xp, ");
        sb.append("slayer_xp, ");
        sb.append("farming_xp, ");
        sb.append("runecrafting_xp, ");
        sb.append("hunter_xp, ");
        sb.append("construction_xp ) ");
        sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return sb.toString();
    }

    private Player player;


    public Hiscores(Player player) {
        super(HOST, USER, PASS, DATABASE);
        this.player = player;
    }

    public void run() {
        try {
            if (!init()) {
                return;
            }
//System.out.println("works!");
            String name = player.getLoginName().replace(" ", "_");
            executeUpdate("DELETE FROM hs_users WHERE username='" + name + "'");


            stmt = prepare(generateQuery());
            stmt.setString(1, name);
            stmt.setInt(2, player.getRights().getPrimary().getValue());
            stmt.setLong(3, player.getTotalXp());

            for (int i = 0; i < 23; i++)
                stmt.setInt(4 + i, (int) player.playerXP[i]);

            stmt.execute();

            destroyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}