package com.vrptw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    // JDBC driver name and database URL
    private static String driverName = "com.mysql.jdbc.Driver";
    //static final String connectionUrl= "jdbc:mysql://127.0.0.1/borrego";
    private static String connectionUrl = "jdbc:mysql://localhost:3306/borrego";
    //  Database credentials
    private static  String userName = "root";
    private static final String userPass = "pass123";

    private static Connection con = null;

    static {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }

    public ConnectionManager() {}

    public Connection connect() throws SQLException {
        if (con == null) {
            con = DriverManager.getConnection(connectionUrl, userName, userPass);
        }
        return con;
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "ConnectionConfiguration: {"
                    + "driverName:" + driverName
                    + "connectionUrl:" + connectionUrl
                    + "userName:" + userName
                    + "userPass:" + userPass
                + "}";
    }
}