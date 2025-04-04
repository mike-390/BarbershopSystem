package view;

import dao.ContactDAO;
import model.Contact;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/* Η κλάση ContactUsPanel παρέχει ένα JPanel για την επικοινωνία με τους χρήστες, 
 * επιτρέποντάς τους να στείλουν μήνυμα με τα στοιχεία τους. */
public class ContactUsPanel extends JPanel {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea messageArea;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private ContactDAO contactDAO;

    /* Ο constructor αρχικοποιεί την σελίδα, προσθέτοντας
     * πεδία εισαγωγής για το όνομα, το email, το τηλέφωνο και το μήνυμα.*/
    public ContactUsPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.contactDAO = new ContactDAO();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Δημιουργία και προσθήκη τίτλου
        JLabel titleLabel = new JLabel("Contact Us");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        /* Δημιουργία και προσθήκη πεδίων εισαγωγής για το όνομα, το email, το τηλέφωνο και το μήνυμα.
         * Κάθε πεδίο περιλαμβάνει μια ετικέτα και ένα πεδίο εισαγωγής.
         * Το πεδίο μηνύματος περιέχει JScrollPane για την κύλιση. */
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(phoneLabel, gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(messageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        messageArea = new JTextArea(5, 20);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 16));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, gbc);
        // Προσθήκη των κουμπιών στο πάνελ
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.weighty = 0;
        
        // Δημιουργία κουμπιών υποβολής και επιστροφής
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        add(submitButton, gbc);
       
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.addActionListener(e -> goBack());
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        add(backButton, gbc);

        // Ο listener για το κουμπί υποβολής ελέγχει την εγκυρότητα των δεδομένων
        submitButton.addActionListener(e -> submitForm());
    }
    
    // Η μέθοδος goBack() επιστρέφει στην κύρια σελίδα της εφαρμογής
    private void goBack() {
        cardLayout.show(mainPanel, "Main Page");
    }

    /* Η μέθοδος submitForm() ελέγχει την εγκυρότητα των πεδίων, 
     * δημιουργεί ένα αντικείμενο Contact και το αποθηκεύει στη βάση δεδομένων.*/
    private void submitForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String message = messageArea.getText().trim();

        // Έλεγχος εγκυρότητας πεδίων
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Έλεγχος εγκυρότητας ονόματος
        if (!name.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Έλεγχος εγκυρότητας τηλεφώνου
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Έλεγχος εγκυρότητας email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!Pattern.matches(emailRegex, email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Δημιουργία αντικειμένου Contact και αποθήκευση στη βάση δεδομένων
        Contact contact = new Contact(name, email, phone, message);
        contactDAO.addContact(contact);
        JOptionPane.showMessageDialog(this, "Thank you for your message!", "Message Sent", JOptionPane.INFORMATION_MESSAGE);

        // Μέθοδος για καθαρισμό των πεδίων εισαγωγής
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        messageArea.setText("");
    }
}
