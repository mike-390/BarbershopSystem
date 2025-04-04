package view;

import com.toedter.calendar.JDateChooser;
import dao.AppointmentDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.Service;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

// Η κλάση BookAppointmentPanel υλοποιεί ένα γραφικό περιβάλλον για την κράτηση ραντεβού.
public class BookAppointmentPanel extends JPanel {

    private JDateChooser dateChooser;
    private JComboBox<String> timeComboBox;
    private ServiceDAO serviceDAO;
    private AppointmentDAO appointmentDAO;
    
    /* Ο constructor της κλάσης δημιουργεί το περιβάλλον κράτησης ραντεβού. Φορτώνει τη λίστα υπηρεσιών από τη βάση δεδομένων,
     * δίνει τη δυνατότητα επιλογής ημερομηνίας και ώρας, και περιέχει κουμπιά για την υποβολή του ραντεβού.*/
    public BookAppointmentPanel(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
        this.appointmentDAO = new AppointmentDAO();

        setLayout(new BorderLayout(20, 20)); 

        JLabel label = new JLabel("Book an Appointment", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        // Δημιουργία φόρμας
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the form
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Service:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;

        /* Εισάγονται πεδία για όνομα πελάτη, τηλέφωνο, email, υπηρεσία, ημερομηνία και ώρα
         * Το JComboBox serviceComboBox φορτώνει τη λίστα υπηρεσιών από τη βάση δεδομένων
         * Το JDateChooser επιτρέπει την επιλογή ημερομηνίας και το JComboBox timeComboBox επιτρέπει την επιλογή ώρας. */
        List<Service> services = serviceDAO.getAllServices();
        JComboBox<Service> serviceComboBox = new JComboBox<>(services.toArray(new Service[0]));
        serviceComboBox.setPrototypeDisplayValue(new Service(0, "Select a service", "", 0, 0.0)); // Placeholder
        formPanel.add(serviceComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Appointment Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMinSelectableDate(new java.util.Date()); // Set minimum selectable date to today
        formPanel.add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Appointment Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;

        String[] timeSlots = generateTimeSlots("09:00", "17:00");
        timeComboBox = new JComboBox<>(timeSlots);
        formPanel.add(timeComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Προσθήκη κουμπιών για την κράτηση ή την επιστροφή στην κύρια σελίδα
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton bookButton = new JButton("Book Appointment");
        JButton backButton = new JButton("Back");

        buttonPanel.add(bookButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; 
        formPanel.add(buttonPanel, gbc); 

        // Δράση για το κουμπί κράτησης που εκτελεί έλεγχο εγκυρότητας των δεδομένων και αποθηκεύει το ραντεβού
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Main Page");
        });

         /* Ελέγχεται αν όλα τα πεδία έχουν συμπληρωθεί σωστά (όνομα, τηλέφωνο, email, υπηρεσία, ημερομηνία, ώρα)
          * Εκτελείται έλεγχος εγκυρότητας για το όνομα, το τηλέφωνο και το email
          * Αν η ημερομηνία είναι η σημερινή, ελέγχεται αν η ώρα είναι στο παρελθόν */
        bookButton.addActionListener(e -> {
            String customerName = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            Service selectedService = (Service) serviceComboBox.getSelectedItem();
            java.util.Date selectedDate = dateChooser.getDate();
            Date appointmentDate = selectedDate != null ? new Date(selectedDate.getTime()) : null;
            String timeString = (String) timeComboBox.getSelectedItem();

            if (customerName.isEmpty() || phone.isEmpty() || email.isEmpty() || selectedService == null || appointmentDate == null || timeString.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be completed to make a booking.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!customerName.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Name must only contain letters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be 10 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Calendar calendar = Calendar.getInstance();
            if (appointmentDate != null && isSameDay(appointmentDate, calendar.getTime())) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Time appointmentTime;
                try {
                    appointmentTime = new Time(sdf.parse(timeString).getTime());
                    Time currentTime = new Time(calendar.getTime().getTime());

                    if (appointmentTime.before(currentTime)) {
                        JOptionPane.showMessageDialog(this, "You cannot book an appointment for a time in the past.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

            // Δημιουργείται ένα αντικείμενο Appointment και αποθηκεύεται στη βάση δεδομένων μέσω του AppointmentDAO
            Appointment appointment = new Appointment(
                    0, customerName, phone, email, 
                    selectedService.getServiceId(), 
                    appointmentDate, Time.valueOf(timeString + ":00"), 
                    "Scheduled"
            );

            appointmentDAO.addAppointment(appointment);

            // Εμφανίζεται μήνυμα επιβεβαίωσης μετά την κράτηση
            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
        });
    }

    // Η μέθοδος generateTimeSlots() δημιουργεί και επιστρέφει τα διαθέσιμα χρονικά διαστήματα (σε 30λεπτα) από μια ώρα έναρξης μέχρι την ώρα λήξης.
    private String[] generateTimeSlots(String startTime, String endTime) {
        List<String> timeSlots = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            java.util.Date start = timeFormat.parse(startTime);
            java.util.Date end = timeFormat.parse(endTime);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(start);

            while (calendar.getTime().before(end) || calendar.getTime().equals(end)) {
                timeSlots.add(timeFormat.format(calendar.getTime()));
                calendar.add(java.util.Calendar.MINUTE, 30); // Προσθέτει 30 λεπτά σε κάθε χρονική στιγμή
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeSlots.toArray(new String[0]);
    }

    // Η μέθοδος isValidEmail() εκτελεί έλεγχο εγκυρότητας για ένα email χρησιμοποιώντας regex.
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Η μέθοδος isSameDay() ελέγχει αν δύο ημερομηνίες είναι η ίδια ημέρα συγκρίνοντας το έτος και την ημέρα του έτους.
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
