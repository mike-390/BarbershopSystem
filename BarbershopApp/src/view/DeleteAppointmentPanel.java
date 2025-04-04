package view;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import java.awt.*;

/* Η κλάση DeleteAppointmentPanel παρέχει ένα JPanel για τη διαγραφή ραντεβού,
 * επιτρέποντας στους χρήστες να ελέγξουν και να διαγράψουν ραντεβού με βάση το όνομα και το τηλέφωνο.*/
public class DeleteAppointmentPanel extends JPanel {

    private JTextField nameField;
    private JTextField phoneField;
    private AppointmentDAO appointmentDAO;

    // Ο constructor αρχικοποιεί την διεπαφή προσθέτοντας πεδία εισαγωγής για το όνομα και το τηλέφωνο.
    public DeleteAppointmentPanel(AppointmentDAO appointmentDAO, CardLayout cardLayout, JPanel mainPanel) {
        this.appointmentDAO = appointmentDAO;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Δημιουργία και προσθήκη πεδίων εισαγωγής
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(nameField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(phoneField, gbc);

        // Δημιουργία κουμπιών
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton checkButton = new JButton("Check Appointment");
        JButton backButton = new JButton("Back");
        buttonPanel.add(checkButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Προσθήκη action listeners στα κουμπιά
        checkButton.addActionListener(e -> checkAndDeleteAppointment());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main Page"));
    }
    
     /* Η μέθοδος checkAndDeleteAppointment() ελέγχει αν υπάρχει ραντεβού με βάση
      * το όνομα και το τηλέφωνο, και αν ναι, επιβεβαιώνει τη διαγραφή.*/
    private void checkAndDeleteAppointment() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // Έλεγχος αν τα πεδία είναι κενά
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Ανάκτηση ραντεβού
        Appointment appointment = appointmentDAO.getAppointmentByNameAndPhone(name, phone);
        
        // Έλεγχος αν βρέθηκε το ραντεβού
        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Appointment not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } else { // Επιβεβαίωση διαγραφής
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                appointmentDAO.deleteAppointment(appointment.getAppointmentId());
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
