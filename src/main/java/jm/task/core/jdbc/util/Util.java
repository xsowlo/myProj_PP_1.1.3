package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/mynewdbtest";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Myrootsqlpass22";

    private static Connection connection;

    static {
        try{
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Util() {

    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection закрыт");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
