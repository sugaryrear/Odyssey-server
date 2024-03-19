package io.Odyssey.sql.vote;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClaimVotesQuery implements SqlQuery<Boolean> {

    private  String newDisplayName;
private Player player;
    public ClaimVotesQuery(String newDisplayName, Player player) {
        this.newDisplayName = newDisplayName;
        this.player=player;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        PreparedStatement select = connection.prepareStatement("SELECT * FROM fx_votes WHERE username='"+newDisplayName.replaceAll(" ","_")+"' AND claimed=0 AND callback_date != -1",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        select.setString(1, newDisplayName.toLowerCase());
        ResultSet rs = select.executeQuery();
       // ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date != -1");

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
            return false;
            // System.out.println("no data");
        }
        player.sendMessage("You now have @blu@"+player.votePoints+"@bla@ vote points.");
        return true;
    }
}
