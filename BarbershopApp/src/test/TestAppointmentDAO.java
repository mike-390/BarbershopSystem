package test;

import dao.AppointmentDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//Αυτή η κλάση χρησιμοποιείται για να δοκιμάσει τις λειτουργίες της κλάσης AppointmentDAO και ServiceDAO.
public class TestAppointmentDAO {
    public static void main(String[] args) {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ServiceDAO serviceDAO = new ServiceDAO();

        // Ελέγχει αν υπάρχει υπηρεσία με ID 1, και αν δεν υπάρχει, προσθέτει μια νέα υπηρεσία στη βάση δεδομένων.
        Service service = serviceDAO.getServiceById(1); 
        if (service == null) {
            service = new Service(0, "New Service", "A new test service", 30, 15.0);
            serviceDAO.addService(service);
            System.out.println("Service added with ID: " + service.getServiceId());
        }

        // Προσθέτει ένα νέο ραντεβού στη βάση δεδομένων χρησιμοποιώντας τις τρέχουσες ημερομηνίες και ώρες.
        Appointment newAppointment = new Appointment(0, "John Doe", "123-456-7890", "johndoe@example.com", 
                service.getServiceId(), Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), "Scheduled");
        appointmentDAO.addAppointment(newAppointment);
        System.out.println("New appointment added with ID: " + newAppointment.getAppointmentId());

        // Ανακτά και εμφανίζει όλα τα ραντεβού από τη βάση δεδομένων, ελέγχοντας τη λειτουργία της ανάκτησης όλων των ραντεβού.
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        System.out.println("List of appointments:");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }

         // Ελέγχει αν υπάρχουν ραντεβού και ανακτά το πρώτο από αυτά με βάση το ID του, επιβεβαιώνοντας τη λειτουργία ανάκτησης μέσω ID.
        if (!appointments.isEmpty()) {
            int appointmentId = appointments.get(0).getAppointmentId();
            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
            System.out.println("Appointment with ID " + appointmentId + ": " + appointment);
        }

        // Ενημερώνει το status του πρώτου ραντεβού σε "Completed" και αποθηκεύει την αλλαγή στη βάση δεδομένων.
        if (!appointments.isEmpty()) {
            Appointment appointmentToUpdate = appointments.get(0);
            appointmentToUpdate.setStatus("Completed"); 
            appointmentDAO.updateAppointment(appointmentToUpdate);
            System.out.println("Updated Appointment with ID " + appointmentToUpdate.getAppointmentId() + ": " + appointmentDAO.getAppointmentById(appointmentToUpdate.getAppointmentId()));
        }

        // Διαγράφει το πρώτο ραντεβού από τη βάση δεδομένων με βάση το ID του.
        if (!appointments.isEmpty()) {
            int appointmentIdToDelete = appointments.get(0).getAppointmentId();
            appointmentDAO.deleteAppointment(appointmentIdToDelete);
            System.out.println("Appointment with ID " + appointmentIdToDelete + " has been deleted.");
        }
    }
}
