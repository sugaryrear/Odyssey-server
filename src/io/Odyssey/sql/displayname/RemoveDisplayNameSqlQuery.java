package io.Odyssey.sql.displayname;

import io.Odyssey.sql.DatabaseManager;
import io.Odyssey.sql.SqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveDisplayNameSqlQuery implements SqlQuery<Boolean> {

    private final String loginName;

    public RemoveDisplayNameSqlQuery(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public Boolean execute(DatabaseManager context, Connection connection) throws SQLException {
        PreparedStatement delete = connection.prepareStatement("DELETE FROM display_names WHERE login_name = ?");
        delete.setString(1, loginName.toLowerCase());
        return delete.execute();
    }
}
