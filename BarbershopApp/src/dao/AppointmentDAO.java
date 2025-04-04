package dao;

import model.Appointment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Η κλάση AppointmentDAO περιέχεί το μοτίβο DAO για να διαχειρίζεται την επικοινωνία με τη βάση δεδομένων και να εκτελεί τις
 * λειτουργείες (Create, Read, Update, Delete) για τα ραντεβού στη βάση δεδομένων. */
public class AppointmentDAO {

    /* Προσθήκη νέου ραντεβού στη βάση δεδομένων. Η μέθοδος αυτή λαμβάνει 
     * ένα αντικείμενο Appointment και εισάγει τα δεδομένα του στη βάση. */
    public void addAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (customer_name, phone, email, service_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, appointment.getCustomerName());
            pstmt.setString(2, appointment.getPhone());
            pstmt.setString(3, appointment.getEmail());
            pstmt.setInt(4, appointment.getServiceId());
            pstmt.setDate(5, appointment.getAppointmentDate());
            pstmt.setTime(6, appointment.getAppointmentTime());
            pstmt.setString(7, appointment.getStatus());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setAppointmentId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to obtain appointment ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ανάκτηση ραντεβού με βάση το ID
    public Appointment getAppointmentById(int appointmentId) {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    appointment = createAppointmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    /* Ανάκτηση ραντεβού με βάση τα στοιχεία πελάτη. Αναζητά το ραντεβού με βάση 
     * το όνομα του πελάτη και τον αριθμό τηλεφώνου. Επιστρέφει το πρώτο που θα βρεθεί.*/
    public Appointment getAppointmentByCustomerDetails(String customerName, String phone) {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointments WHERE customer_name = ? AND phone = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerName);
            pstmt.setString(2, phone);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    appointment = createAppointmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

     // Ανάκτηση όλων των ραντεβού. Η μέθοδος αυτή επιστρέφει μια λίστα με όλα τα ραντεβού από τη βάση δεδομένων.
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Appointment appointment = createAppointmentFromResultSet(rs);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /* Ενημέρωση υπάρχοντος ραντεβού. Η μέθοδος αυτή ενημερώνει τα
     * στοιχεία ενός ραντεβού στη βάση δεδομένων με βάση το ID του ραντεβού.*/
    public void updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET customer_name = ?, phone = ?, email = ?, service_id = ?, appointment_date = ?, appointment_time = ?, status = ? WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, appointment.getCustomerName());
            pstmt.setString(2, appointment.getPhone());
            pstmt.setString(3, appointment.getEmail());
            pstmt.setInt(4, appointment.getServiceId());
            pstmt.setDate(5, appointment.getAppointmentDate());
            pstmt.setTime(6, appointment.getAppointmentTime());
            pstmt.setString(7, appointment.getStatus());
            pstmt.setInt(8, appointment.getAppointmentId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Διαγραφή ραντεβού. Η μέθοδος αυτή διαγράφει ένα ραντεβού από τη βάση δεδομένων με βάση το appointment_id του.
    public void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   // Χρησιμοποιείται για να μετατρέψει τα αποτελέσματα μιας SQL ερώτησης σε αντικείμενο Appointment.
    private Appointment createAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int appointmentId = rs.getInt("appointment_id");
        String customerName = rs.getString("customer_name");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        int serviceId = rs.getInt("service_id");
        Date appointmentDate = rs.getDate("appointment_date");
        Time appointmentTime = rs.getTime("appointment_time");
        String status = rs.getString("status");

        return new Appointment(appointmentId, customerName, phone, email, serviceId, appointmentDate, appointmentTime, status);
    }
    
    // Αναζήτηση ραντεβού με βάση το όνομα και το τηλέφωνο του πελάτη
    public Appointment getAppointmentByNameAndPhone(String name, String phone) {
    Appointment appointment = null;
    String sql = "SELECT * FROM appointments WHERE customer_name = ? AND phone = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, name);
        pstmt.setString(2, phone);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                String customerName = rs.getString("customer_name");
                String phoneNumber = rs.getString("phone");
                String email = rs.getString("email");
                int serviceId = rs.getInt("service_id");
                Date appointmentDate = rs.getDate("appointment_date");
                Time appointmentTime = rs.getTime("appointment_time");
                String status = rs.getString("status");

                appointment = new Appointment(appointmentId, customerName, phoneNumber, email, serviceId, appointmentDate, appointmentTime, status);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return appointment;
}

}
