package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    static private Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/kata_schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234567890";

    private Util() {

    }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }


        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
