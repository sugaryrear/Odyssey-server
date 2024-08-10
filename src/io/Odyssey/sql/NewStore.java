package io.Odyssey.sql;


import io.Odyssey.content.mail.Mail;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Misc;

import java.sql.*;


public class NewStore implements Runnable {

    public static final String HOST = "198.12.12.226";
    public static final String USER = "2_user1";
    public static final String PASS = "thepassword12345";
    public static final String DATABASE = "x2_store";

    private Player player;
    private Connection conn;
    private Statement stmt;
private boolean probe;

    public NewStore(Player player, boolean probe) {
        this.player = player;
        this.probe = probe;
    }

    @Override
    public void run(){
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getLoginName().replace("_", " ");



            ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");
if(probe){
    while (rs.next()) {
        int item_number = 0;
       // int i  =0;
        int id = rs.getInt("id");
        item_number = rs.getInt("item_number");

        double paid = rs.getDouble("amount");
        int quantity = rs.getInt("quantity");
      //  int quantity = rs.getInt("2023-08-12 17:18:23");
        player.mailboxEntries.add(new Mail(id,item_number, quantity,"Thank you for supporting\\nthe server!"));
      //  player.buttonsmapofmailbox.put(i, item_number);
       // i++;
    }
    //player.sendMessage("items: "+player.mailboxEntries.size()+" and at first slot: "+player.mailboxEntries.get(0).getId());
//    int rows=0;
//
//    if (rs.last()) {//this will tell you the number of votes you have to claim!
//
//        rows = rs.getRow();
//        rs.beforeFirst();
//    }
//    if(rows > 0){
//        player.sendMessage("unclaim items: "+rows);
//    }
} else {
    while (rs.next()) {
        int item_number = 0;

        item_number = rs.getInt("item_number");

        double paid = rs.getDouble("amount");
        int quantity = rs.getInt("quantity");

        switch (item_number) {// add products according to their ID in the ACP

            case Items.SCYTHE_OF_VITUR: // example
                player.getItems().addItemUnderAnyCircumstance(Items.SCYTHE_OF_VITUR, quantity);
                player.sendMessage("Thank you for donating!");
                break;

            case 13190: // example
                player.getItems().addItemUnderAnyCircumstance(13190, quantity);
                player.sendMessage("Thank you for donating!");
                break;
            case 31015: // inf run energy
                player.getItems().addItemUnderAnyCircumstance(31015, quantity);
                player.sendMessage("Thank you for donating!");
                break;
            case 31016: // ak 47
                player.getItems().addItemUnderAnyCircumstance(31016, quantity);
                player.getItems().addItemUnderAnyCircumstance(31017, 1000);
                player.sendMessage("Thank you for donating!");
                break;

        }
        rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
        rs.updateRow();
        if (item_number != 13190)//don't give it out for bonds - they press redeem to get the amDonated to increase.
            player.amDonated += paid;
        player.updateRank();
    }
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
