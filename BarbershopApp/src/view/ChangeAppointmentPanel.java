package view;

import com.toedter.calendar.JDateChooser;
import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

/* Η κλάση ChangeAppointmentPanel δημιουργεί ένα JPanel για την επεξεργασία ραντεβού με βάση το όνομα και τον αριθμό τηλεφώνου του πελάτη.
 * Περιλαμβάνει φόρμα αναζήτησης ραντεβού και, αν βρεθεί, εμφανίζει μια φόρμα για αλλαγή της ημερομηνίας και της ώρας του ραντεβού.*/
public class ChangeAppointmentPanel extends JPanel {

    private JTextField nameField;
    private JTextField phoneField;
    private AppointmentDAO appointmentDAO;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    /* Ο constructor αρχικοποιεί τη φόρμα για την αλλαγή ραντεβού, περιλαμβάνοντας πεδία εισαγωγής ονόματος και τηλεφώνου.
     * Περιέχει κουμπιά για αναζήτηση του ραντεβού ή επιστροφή στην κύρια σελίδα.*/
    public ChangeAppointmentPanel(AppointmentDAO appointmentDAO, CardLayout cardLayout, JPanel mainPanel) {
        this.appointmentDAO = appointmentDAO;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout(20, 20)); 

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Προσθέτει κενά
        gbc.insets = new Insets(10, 10, 10, 10); 

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nameField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(phoneField, gbc);

        // Δημιουργία πεδίων για την εισαγωγή ονόματος και τηλεφώνου, μαζί με κουμπιά ελέγχου και επιστροφής
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); 
        JButton checkButton = new JButton("Check Appointment");
        buttonPanel.add(checkButton);
        JButton backButton = new JButton("Back");
        // Η επιστροφή γίνεται μέσω του κουμπιού back που δείχνει την κύρια σελίδα με χρήση του CardLayout
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main Page"));
        buttonPanel.add(backButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(buttonPanel, gbc);
        // Το κουμπί check ελέγχει αν υπάρχει ραντεβού με βάση τα στοιχεία που εισήχθησαν και εμφανίζει κατάλληλο μήνυμα
        checkButton.addActionListener(e -> checkAppointment());

        add(formPanel, BorderLayout.CENTER);
    }
    
   /* Η μέθοδος checkAppointment() εκτελεί αναζήτηση του ραντεβού με βάση το όνομα και το τηλέφωνο του πελάτη.
    * Αν δεν βρεθεί το ραντεβού, εμφανίζεται μήνυμα σφάλματος. Αν βρεθεί, ανοίγει ένα διάλογος για να αλλάξει το ραντεβού.*/
    private void checkAppointment() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment appointment = appointmentDAO.getAppointmentByNameAndPhone(name, phone);

        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Appointment not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Appointment found!", "Success", JOptionPane.INFORMATION_MESSAGE);
            openChangeAppointmentDialog(appointment);
        }
    }
   
    /* Η μέθοδος openChangeAppointmentDialog() ανοίγει ένα παράθυρο που επιτρέπει στον χρήστη να αλλάξει την ημερομηνία και την ώρα του ραντεβού.
     * Η ημερομηνία επιλέγεται μέσω του JDateChooser και η ώρα μέσω JComboBox. Αν τα νέα δεδομένα είναι έγκυρα, ενημερώνεται το ραντεβού στη βάση δεδομένων. */
    private void openChangeAppointmentDialog(Appointment appointment) {
        JDialog dialog = new JDialog((Frame) null, "Change Appointment", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        // Δημιουργία επιλογών για αλλαγή της ημερομηνίας και της ώρας του ραντεβού
        JLabel dateLabel = new JLabel("New Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(dateLabel, gbc);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(dateChooser, gbc);

        JLabel timeLabel = new JLabel("New Time:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(timeLabel, gbc);

        String[] times = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"
        };

        JComboBox<String> timeComboBox = new JComboBox<>(times);
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(timeComboBox, gbc);

        JButton updateButton = new JButton("Update Appointment");
        gbc.gridx = 1;
        gbc.gridy = 2;
        dialog.add(updateButton, gbc);
     
        // Το κουμπί update ενημερώνει τη βάση δεδομένων με τις νέες πληροφορίες αν είναι έγκυρες οι τιμές
        updateButton.addActionListener(e -> {
            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
            String newTime = (String) timeComboBox.getSelectedItem();

            if (newDate.isEmpty() || newTime.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select both date and time.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    appointment.setAppointmentDate(Date.valueOf(newDate));
                    appointment.setAppointmentTime(Time.valueOf(newTime + ":00"));
                    appointmentDAO.updateAppointment(appointment);
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
