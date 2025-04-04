package dao;

import model.Contact;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Η κλάση περιέχει τη μέθοδο για την εισαγωγή ενός νέου ραντεβού στη βάση δεδομένων.
public class ContactDAO {
    // Η μέθοδος addContact λαμβάνει ένα αντικείμενο Contact και εισάγει τα στοιχεία του (όνομα, email, τηλέφωνο και μήνυμα) στη βάση δεδομένων.
    public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts (name, email, phone, message) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getPhone());
            pstmt.setString(4, contact.getMessage());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
