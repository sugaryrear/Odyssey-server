package io.Odyssey.sql; // dont forget to change packaging ^-^


import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.discord.DiscordMessager;
import io.Odyssey.util.Misc;
import io.Odyssey.util.discord.Discord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


public class NewVote implements Runnable {
    public static final String HOST = "198.12.12.226";
    public static final String USER = "x2_user1";
    public static final String PASS = "thepassword12345";
    public static final String DATABASE = "x2_foxvote";


    private Player player;
    private Connection conn;
    private Statement stmt;
    private boolean probing;

    public NewVote(Player player, boolean probing) {
        this.player = player;
        this.probing = probing;
    }

    @Override
    public void run() {

        //System.out.println("here44?");
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                System.out.println("Can't connect to mysql");
                return;
            }

            String name = player.getLoginName().toLowerCase().replaceAll(" ", "_");
            System.out.println("sL  "+name);
            ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date != -1");

            int rows=0;


           // System.out.println("Number of entries with claimed rowc: "+rows);

            if (rs.next()) {
                do {
                  //  player.sendMessage("Thank you for voting!");
                    player.votePoints ++;

                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
                } while(rs.next());
            } else {
                player.getPA().closeAllWindows();
                player.sendMessage("You have no votes to claim.");
                return;
                // System.out.println("no data");
            }
            player.sendMessage("You now have @blu@"+player.votePoints+"@bla@ vote points.");
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
