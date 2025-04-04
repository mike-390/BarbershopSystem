package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Η κλάση DBConnection παρέχει μια μέθοδο για τη δημιουργία και επιστροφή μιας σύνδεσης με τη βάση δεδομένων.
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/barbershop_db";
    private static final String USER = "root";
    private static final String PASSWORD = "d3F#2T!9";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
