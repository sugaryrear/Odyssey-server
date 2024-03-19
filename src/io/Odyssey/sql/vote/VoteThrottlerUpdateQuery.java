package io.Odyssey.sql.vote;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.DatabaseTable;
import io.Odyssey.sql.SqlQuery;
import io.Odyssey.sql.voterecord.VoteRecordTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chris | 8/14/21
 */
public class VoteThrottlerUpdateQuery implements SqlQuery<Object> {
    private static final DatabaseTable TABLE = new VoteRecordTable();

    private final Player player;

    private final List<VoteRecord> votes;

    public VoteThrottlerUpdateQuery(final Player player, List<VoteRecord> votes) {
        this.player = player;

        this.votes = votes;
    }

    @Override
    public Object execute(DatabaseManager context, Connection connection) throws Exception {
        PreparedStatement insertRecent = connection.prepareStatement("INSERT INTO " + TABLE.getName() + " VALUES(?, ?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE " + VoteRecordTable.DATE_CLAIMED_COLUMN + "=VALUES(" + VoteRecordTable.DATE_CLAIMED_COLUMN + ")");

        for (VoteRecord voteRecord : votes) {
            insertRecent.setString(1, player.getIpAddress());
            insertRecent.setString(2, player.getMacAddress());
            insertRecent.setString(3, player.getUUID());
            insertRecent.setTimestamp(4, Timestamp.valueOf(voteRecord.getVotedLocalDateTime()));
            insertRecent.setInt(5, voteRecord.getSiteId());
            insertRecent.addBatch();
        }

        insertRecent.executeBatch();
        return null;
    }
}
