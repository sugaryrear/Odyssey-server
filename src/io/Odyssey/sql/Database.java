package io.Odyssey.sql;

import java.sql.*;

class Database {

    public Connection conn;
    public PreparedStatement stmt;
    public Statement stmt1;

    // do not modify
    private String host;
    private String user;
    private String pass;
    private String database;

    public Database(String host, String user, String pass, String db) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.database = db;
    }

    public Connection getConnection() {
        return conn;
    }

    public PreparedStatement getStatement() {
        return stmt;
    }

    public boolean init() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database+"?"+"user="+user+"&password="+pass+"&&characterEncoding=utf8");
            System.out.print("We are connected to: "+database+" \n");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public PreparedStatement prepare(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public int executeUpdate(String query) {
        try {
            stmt1  = conn.createStatement();
            int results = stmt1.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            stmt1 = conn.createStatement(1005, 1008);
            ResultSet results = stmt1.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void destroyAll() {
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
}
