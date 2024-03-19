package io.Odyssey.sql.vote;


import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckHowManyVotesWeHaveQuery implements SqlQuery<Integer> {

    private String newDisplayName;//thats what final means u cant change in in the tolowercase shit

    public CheckHowManyVotesWeHaveQuery(String newDisplayName) {
        this.newDisplayName = newDisplayName;
    }

    @Override
    public Integer execute(DatabaseManager context, Connection connection) throws SQLException {

       // System.out.println("login before: "+newDisplayName+" ");//)
     //   PreparedStatement select = connection.prepareStatement("SELECT display_name FROM display_names WHERE display_name_lower = ?");
        PreparedStatement select = connection.prepareStatement("SELECT * FROM fx_votes WHERE username= ? AND claimed=0 AND callback_date != -1",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        select.setString(1, newDisplayName);
        ResultSet rs = select.executeQuery();

        int rows=0;

        if (rs.last()) {//this will tell you the number of votes you have to claim!

            rows = rs.getRow();
            rs.beforeFirst();
        }
//        if(rows > 0){
//            c.sendMessage("You_have_"+rows+"_vote_points!_Click_here_to_claim_them!:votecheck:");
//        }
        return rows;
     //   return !rs.next(); // Return true if the display name is not taken
    }
}

