package io.Odyssey.sql.vote;

import io.Odyssey.Server;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VoteClaimQuery implements SqlQuery<List<VoteRecord>> {

    private final Player player;
    private final LocalDateTime now = LocalDateTime.now();

    public VoteClaimQuery(Player player) {
        this.player = player;
    }

    @Override
    public List<VoteRecord> execute(DatabaseManager context, Connection connection) throws Exception {
        List<VoteRecord> voteRecords = new ArrayList<>();

        PreparedStatement select = connection.prepareStatement(
            //    "SELECT * FROM votes WHERE username = ? AND date_claimed IS NULL AND completed=1",
           //     "SELECT * FROM votes WHERE username = ? AND date_claimed IS NULL AND completed=1",
"SELECT * FROM fx_votes WHERE username= ? AND claimed=0 AND callback_date != -1",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


        select.setString(1, player.getLoginName().replaceAll(" ","_"));


        ResultSet rs = select.executeQuery();

        while (rs.next()) {
//            rs.updateTimestamp("date_claimed", Timestamp.valueOf(now));
//            rs.updateRow();

            rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
            rs.updateRow();

            int siteId = rs.getInt("site_id");
            var timestamp = rs.getTimestamp("callback_date");//the time you voted.
            //var timestamp = rs.getTimestamp("date_voted");


            voteRecords.add(new VoteRecord(siteId, timestamp, false));//adds that record for that site (site id, time voted)
        }

        // Check if they've voted within 24 hours on this site already
        Server.getDatabaseManager().executeImmediate((context1, connection1) -> {
            for (VoteRecord voteRecord : voteRecords) {
                Boolean claimed = new VoteThrottlerCheckQuery(player, voteRecord.getSiteId()).execute(context1, connection1);

                if (claimed != null && claimed) {
//if it gets here it means it checked a site that you CANT vote yet (<24 hours)
                    voteRecord.setThrottled(true);
                } else {

                }
            }
            return null;
        });
//throttled = true  means you CANT vote on that site yet!


        // Throttle all votes on the same site after one
        HashSet<Integer> voteSites = new HashSet<>();
        for (VoteRecord voteRecord : voteRecords) {
            if (voteRecord.isThrottled())
                continue;
            if (voteSites.contains(voteRecord.getSiteId())) {//for when u vote next time
                voteRecord.setThrottled(true);
                continue;
            }
            voteSites.add(voteRecord.getSiteId());
        }
//voteSites.forEach(f -> System.out.println("votesite: "+f));
        return voteRecords;
    }

}
