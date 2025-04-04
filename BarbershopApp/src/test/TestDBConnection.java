package test;

import util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

//Αυτή η κλάση χρησιμοποιείται για να δοκιμάσει τη σύνδεση με τη βάση δεδομένων μέσω της κλάσης DBConnection.
public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connection to the database established successfully!");
            } else {
                System.out.println("Failed to establish connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
