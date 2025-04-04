package test;

import dao.ServiceDAO;
import model.Service;

import java.util.List;

// Αυτή η κλάση χρησιμοποιείται για να δοκιμάσει τις λειτουργίες της κλάσης ServiceDAO.
public class TestServiceDAO {
    public static void main(String[] args) {
        ServiceDAO serviceDAO = new ServiceDAO();

        // Test 1: Προσθήκη νέας υπηρεσίας
        //Service newService = new Service(0, "Beard Trim", "A quick trim for your beard", 15, 8.00);
        //serviceDAO.addService(newService);

        // Test 2: Ανάκτηση και εμφάνιση όλων των υπηρεσιών από τη βάση δεδομένων.
        List<Service> services = serviceDAO.getAllServices();
        for (Service service : services) {
            System.out.println(service);
        }

        // Test 3: Ανάκτηση υπηρεσίας με βάση το ID
        //Service service = serviceDAO.getServiceById(7);
        //System.out.println("Service with ID 7: " + service);

        // Test 4: Ενημέρωση μιας υπηρεσίας
        //if (service != null) {
          //  service.setPrice(10.00); // Update the price
          //  serviceDAO.updateService(service);
           // System.out.println("Updated Service with ID 5: " + serviceDAO.getServiceById(5));
       // }

        // Test 5: Διαγραφή μιας υπηρεσίας από τη βάση δεδομένων με βάση το ID της.
        serviceDAO.deleteService(6);
        System.out.println("Service with ID 4 after deletion: " + serviceDAO.getServiceById(6));
    }
}
