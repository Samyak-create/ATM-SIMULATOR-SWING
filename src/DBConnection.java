import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/atm_db";
    private static final String USER = "root";
    private static final String PASS = "54321"; // change this

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
