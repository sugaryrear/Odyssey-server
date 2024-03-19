package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.CommandManager;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.NewVote;
import io.Odyssey.sql.vote.CheckHowManyVotesWeHaveQuery;
import io.Odyssey.sql.vote.ClaimVotesQuery;

import java.sql.*;

public class Checkvote extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        try {
          String[] args = input.split(" ");
     int checkvote = Integer.parseInt(args[0]);
if(checkvote == 1){//when u click
    CommandManager.execute(c, "voted");
return;
        }
            Server.getDatabaseManager().exec((context, connection) -> {
                int available;

                try {
                    available = new CheckHowManyVotesWeHaveQuery(c.getLoginName()).execute(context, connection);


                    if (available > 0) {
                       // c.sendMessage("You_have_"+available+"_vote_points!_Click_here_to_claim_them!:votecheck:");
                                c.addQueuedAction(plr ->
 plr.sendMessage("You_have_"+available+"_vote_points!_Click_here_to_claim_them!:votecheck:"));
                    } else {
                       // System.out.println("no votes.");
                        //playr.addQueuedAction(plr -> setDisplayName(plr, name));
                    }

                } catch (SQLException throwables) {
                    //  sendErrorDialogue(playr);
                    throwables.printStackTrace();
                    return null;
                }


                return null;
            });


//        String[] args = input.split(" ");
//        int checkvote = Integer.parseInt(args[0]);
//     // c.sendMessage("here");
//if(checkvote == 1){//when u click
//    new NewVote(c,false).run();
//    return;
//}
//        if (!connect(HOST, DATABASE, USER, PASS)) {
//            System.out.println("Can't connect to mysql");
//            return;
//        }
//
//        String name = c.getDisplayName().toLowerCase();
//        ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date != -1");
//
//        int rows=0;
//
//            if (rs.last()) {//this will tell you the number of votes you have to claim!
//
//                rows = rs.getRow();
//                rs.beforeFirst();
//            }
//            if(rows > 0){
//                c.sendMessage("You_have_"+rows+"_vote_points!_Click_here_to_claim_them!:votecheck:");
//            }
//
//
//        destroy();
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
    private Connection conn;
    private Statement stmt;
    private boolean probing = true;
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