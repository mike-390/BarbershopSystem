package view;

import dao.ServiceDAO;
import dao.AppointmentDAO;

import javax.swing.*;
import java.awt.*;

/* Η κλάση MainFrame είναι το κύριο παράθυρο της εφαρμογής,
 * που διαχειρίζεται την πλοήγηση μεταξύ των διαφορετικών πάνελ.*/
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Ο constructor αρχικοποιεί το κύριο παράθυρο, ρυθμίζει τις ρυθμίσεις και δημιουργεί τα απαραίτητα πάνελ.
    public MainFrame() {
        setTitle("Barbershop Management System");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Δημιουργία DAO instances
        ServiceDAO serviceDAO = new ServiceDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        // Δημιουργία και προσθήκη πάνελ
        
        JPanel mainPagePanel = createMainPagePanel();
        mainPanel.add(mainPagePanel, "Main Page");

        ViewServicesPanel viewServicesPanel = new ViewServicesPanel(serviceDAO, cardLayout, mainPanel);
        mainPanel.add(viewServicesPanel, "View Services");

        BookAppointmentPanel bookAppointmentPanel = new BookAppointmentPanel(serviceDAO);
        mainPanel.add(bookAppointmentPanel, "Book Appointment");

        ChangeAppointmentPanel changeAppointmentPanel = new ChangeAppointmentPanel(appointmentDAO, cardLayout, mainPanel);
        mainPanel.add(changeAppointmentPanel, "Change Appointment");

        DeleteAppointmentPanel deleteAppointmentPanel = new DeleteAppointmentPanel(appointmentDAO, cardLayout, mainPanel);
        mainPanel.add(deleteAppointmentPanel, "Delete Appointment");

        ContactUsPanel contactUsPanel = new ContactUsPanel(mainPanel, cardLayout);  
        mainPanel.add(contactUsPanel, "Contact Us");

        // Προσθήκη του κύριου πάνελ στο παράθυρο
        add(mainPanel);
        
        // Εμφάνιση της κύριας σελίδας αρχικά
        cardLayout.show(mainPanel, "Main Page"); 
    }
    
    /* Δημιουργεί και ρυθμίζει το πάνελ της κύριας σελίδας,
     * περιλαμβάνοντας το λογότυπο και πληροφορίες για το κομμωτήριο */
    private JPanel createMainPagePanel() {
        JPanel mainPagePanel = new JPanel(new BorderLayout());

        // Φόρτωση της εικόνας του λογότυπου
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon(getClass().getResource("/images/klodi.png"));
            Image image = logoIcon.getImage();
            Image scaledImage = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
        } catch (NullPointerException e) {
            System.out.println("Logo image not found at the specified path.");
        }

        // Δημιουργία του header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        if (logoIcon != null) {
            JLabel logoLabel = new JLabel(logoIcon);
            headerPanel.add(logoLabel);
        }

        // Δημιουργία κουμπιών πλοήγησης
        JButton viewServicesButton = new JButton("View Services");
        JButton appointmentsButton = new JButton("Appointments");
        JButton contactUsButton = new JButton("Contact Us");

        headerPanel.add(viewServicesButton);
        headerPanel.add(appointmentsButton);
        headerPanel.add(contactUsButton);

        mainPagePanel.add(headerPanel, BorderLayout.NORTH);

        // Δημιουργία separator
        JSeparator separator = new JSeparator();
        mainPagePanel.add(separator, BorderLayout.CENTER);

        String aboutUsText = "<html><div style='text-align: justify; padding: 10px 20px;'>"
                + "Welcome to UNIWA Barbershop, a premier grooming destination located at the heart of our university community. "
                + "At UNIWA, we combine traditional techniques with modern styles to deliver personalized haircuts and grooming services.<br><br>"
                + "Our skilled barbers are dedicated to providing top-notch service in a friendly, welcoming atmosphere. "
                + "We understand the busy lives of students and staff, so we offer flexible hours tailored to fit your schedule.<br><br>"
                + "Whether you're preparing for an interview, a graduation, or just in need of a quick trim, UNIWA Barbershop is your go-to spot "
                + "for quality and convenience. Visit us today and experience the best in campus grooming."
                + "</div></html>";

        JLabel aboutUsLabel = new JLabel(aboutUsText);
        aboutUsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutUsLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel aboutUsPanel = new JPanel(new BorderLayout());
        aboutUsPanel.add(aboutUsLabel, BorderLayout.NORTH);
        mainPagePanel.add(aboutUsPanel, BorderLayout.CENTER);

        // Δημιουργία πίνακα για φωτογραφίες
        JPanel haircutsPanel = new JPanel();
        haircutsPanel.setLayout(new GridLayout(2, 2, 20, 20)); 

        addSampleHaircut(haircutsPanel, "/images/one.jpg");
        addSampleHaircut(haircutsPanel, "/images/two.jpg");
        addSampleHaircut(haircutsPanel, "/images/three.jpg");
        addSampleHaircut(haircutsPanel, "/images/four.jpg");

        mainPagePanel.add(haircutsPanel, BorderLayout.SOUTH);

        // Προσθήκη Action Listeners
        viewServicesButton.addActionListener(e -> cardLayout.show(mainPanel, "View Services"));
        appointmentsButton.addActionListener(e -> showAppointmentDialog());
        contactUsButton.addActionListener(e -> cardLayout.show(mainPanel, "Contact Us"));

        return mainPagePanel;
    }

    // Φορτωσή εικόνας
    private void addSampleHaircut(JPanel panel, String imagePath) {
        try {
            ImageIcon haircutIcon = new ImageIcon(getClass().getResource(imagePath));
            Image image = haircutIcon.getImage();
            Image scaledImage = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
            haircutIcon = new ImageIcon(scaledImage);
            JLabel haircutLabel = new JLabel(haircutIcon);
            panel.add(haircutLabel);
        } catch (NullPointerException e) {
            System.out.println("Haircut image not found at path: " + imagePath);
        }
    }

    // Εμφανίζει ένα διάλογο για να επιλέξει ο χρήστης αν θέλει να κλείσει, να αλλάξει ή να διαγράψει ραντεβού.
    private void showAppointmentDialog() {
        String[] options = {"Book Appointment", "Change Appointment", "Delete Appointment"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an option:",
                "Appointments",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            cardLayout.show(mainPanel, "Book Appointment");
        } else if (choice == 1) {
            cardLayout.show(mainPanel, "Change Appointment");
        } else if (choice == 2) {
            cardLayout.show(mainPanel, "Delete Appointment");
        }
    }
    
    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
