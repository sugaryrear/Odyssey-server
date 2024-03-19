package io.Odyssey.sql;


import io.Odyssey.content.mail.Mail;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Color;
import io.Odyssey.util.Misc;

import java.sql.*;


public class NewStore_onlogin implements Runnable {

    public static final String HOST = "198.12.12.226";
    public static final String USER = "zodianx2_user1";
    public static final String PASS = "thepassword12345";
    public static final String DATABASE = "zodianx2_store";

    private Player player;
    private Connection conn;
    private Statement stmt;
private boolean onlogin;

    public NewStore_onlogin(Player player, boolean onlogin) {
        this.player = player;
        this.onlogin = onlogin;
    }

    @Override
    public void run(){
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getLoginName().replace("_", " ");



            ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");
    int rows=0;

    if (rs.last()) {//this will tell you the number of votes you have to claim!

        rows = rs.getRow();
        rs.beforeFirst();
    }
    if(rows > 0){
        if(onlogin)
        player.sendMessage("You have " + Color.DARK_GREEN.wrap(String.valueOf(rows)) + " unread "+(rows > 1 ? "messages" : "message")+" in your mailbox!");


        player.getPA().sendString(80125,"Inbox: @gre@"+rows+"@lre@ unread "+(rows > 1 ? "messages" : "message")+"");
        player.getPA().sendFrame126("You have @gre@"+rows+"@lre@unread "+(rows > 1 ? "messages" : "message")+"\\nin your mailbox", 15261);
    } else {

        player.getPA().sendString(80125,"Inbox: 0 unread messages.");

        player.getPA().sendFrame126("You have 0 unread messages\\nin your mailbox", 15261);
    }
            destroy();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param host the host ip address or url
     * @param database the name of the database
     * @param user the user attached to the database
     * @param pass the users password
     * @return true if connected
     */
    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
         //   System.out.print("Connected");
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and destroy the connection
     * and statement instances
     */
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

    /**
     * Executes an update query on the database
     * @param query
     * @see {@link Statement#executeUpdate}
     */
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

    /**
     * Executres a query on the database
     * @param query
     * @see {@link Statement#executeQuery(String)}
     * @return the results, never null
     */
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

