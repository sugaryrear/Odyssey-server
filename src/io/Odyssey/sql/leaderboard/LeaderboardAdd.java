package io.Odyssey.sql.leaderboard;

import io.Odyssey.content.leaderboards.LeaderboardEntry;
import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.SqlQuery;

import java.sql.*;

public class LeaderboardAdd implements SqlQuery<Boolean> {

    private final LeaderboardEntry entry;

    public LeaderboardAdd(LeaderboardEntry entry) {
        this.entry = entry;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        PreparedStatement insert = connection.prepareStatement("INSERT INTO leaderboards VALUES(?, ?, ?, curdate()) ON DUPLICATE KEY UPDATE amount = amount + ?");
        insert.setString(1, entry.getLoginName().toLowerCase());
        insert.setInt(2, (int) entry.getAmount());
        insert.setInt(3, entry.getType().ordinal());
        insert.setInt(4, (int) entry.getAmount());
        insert.execute();
        return true;
    }
}
