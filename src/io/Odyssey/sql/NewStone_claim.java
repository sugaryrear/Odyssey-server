package io.Odyssey.sql;


import io.Odyssey.content.commands.all.Claim;
import io.Odyssey.content.mail.Mail;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;

import java.sql.*;


public class NewStone_claim implements Runnable {

    public static final String HOST = "";
    public static final String USER = "";
    public static final String PASS = "thepassword12345";
    public static final String DATABASE = "";

    private Player player;
    private Connection conn;
    private Statement stmt;

    private PreparedStatement preparedStatement = null;
    private int id;

    public NewStone_claim(Player player, int id) {
        this.player = player;
        this.id = id;
    }

    @Override
    public void run(){
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getLoginName().replace("_", " ");

            if (player.hitDatabaseRateLimit(true))
                return;


       //  ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");
ResultSet rs = executeQuery("SELECT * FROM payments WHERE id='" + id + "'");
            while (rs.next()) {
                int item_number = 0;

                item_number = rs.getInt("item_number");

                double paid = rs.getDouble("amount");
                int quantity = rs.getInt("quantity");
              player.getItems().addItemUnderAnyCircumstance(item_number, quantity);
                player.sendMessage("Thank you for supporting the server!");
             rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
               rs.updateRow();
                if (item_number != 13190)//don't give it out for bonds - they press redeem to get the amDonated to increase.
                    player.amDonated += paid;
                player.updateRank();

                new NewStore_onlogin(player,false).run();
            }
            destroy();
            player.openmailbox();
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
            System.out.print("Connected");
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

