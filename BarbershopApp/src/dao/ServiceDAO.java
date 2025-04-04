package dao;

import model.Service;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Η κλάση ServiceDAO διαχειρίζεται υπηρεσίες στη βάση δεδομένων μέσω CRUD λειτουργιών.
public class ServiceDAO {

    // Ανακτά όλες τις υπηρεσίες από τη βάση δεδομένων και τις επιστρέφει ως λίστα.
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                double price = rs.getDouble("price");

                Service service = new Service(serviceId, serviceName, description, duration, price);
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    //  Προσθέτει μια νέα υπηρεσία στη βάση δεδομένων
    public void addService(Service service) {
        String sql = "INSERT INTO services (service_name, description, duration, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setInt(3, service.getDuration());
            pstmt.setDouble(4, service.getPrice());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    service.setServiceId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to obtain service ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ανακτά μια υπηρεσία από τη βάση δεδομένων
    public Service getServiceById(int serviceId) {
        Service service = null;
        String sql = "SELECT * FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, serviceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String serviceName = rs.getString("service_name");
                    String description = rs.getString("description");
                    int duration = rs.getInt("duration");
                    double price = rs.getDouble("price");

                    service = new Service(serviceId, serviceName, description, duration, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return service;
    }

    // Ενημερώνει μια υπάρχουσα υπηρεσία στη βάση δεδομένων με νέα δεδομένα.
    public void updateService(Service service) {
        String sql = "UPDATE services SET service_name = ?, description = ?, duration = ?, price = ? WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setInt(3, service.getDuration());
            pstmt.setDouble(4, service.getPrice());
            pstmt.setInt(5, service.getServiceId());

            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Διαγράφει μια υπηρεσία από τη βάση δεδομένων
    public void deleteService(int serviceId) {
        String sql = "DELETE FROM services WHERE service_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, serviceId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
