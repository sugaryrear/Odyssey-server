package io.Odyssey.sql;

import java.sql.Connection;
import java.sql.SQLException;


public interface DatabaseTable {

    /**
     * The database name (always uppercase)
     * @return the database name
     */
    String getName();

    void createTable(Connection connection)  throws SQLException;

}
