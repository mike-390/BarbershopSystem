package view;

import dao.ServiceDAO;
import model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Η κλάση ViewServicesPanel εμφανίζει μια λίστα υπηρεσιών που παρέχονται από το κομμωτήριο, με δυνατότητα κράτησης.
public class ViewServicesPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Ο constructor αρχικοποιεί το πάνελ και φορτώνει τις υπηρεσίες από το DAO για να τις εμφανίσει σε πίνακα.
    public ViewServicesPanel(ServiceDAO serviceDAO, CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());

        // Δημιουργία του πίνακα
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Duration", "Price"}, 0);

        // Ανάκτηση υπηρεσιών και προσθήκη τους στο πίνακα
        List<Service> services = serviceDAO.getAllServices();
        for (Service service : services) {
            tableModel.addRow(new Object[]{
                    service.getServiceId(),
                    service.getServiceName(),
                    service.getDescription(),
                    service.getDuration(),
                    service.getPrice()
            });
        }

        // Δημιουργία του πίνακα
        JTable servicesTable = new JTable(tableModel);

        // Προσθήκη του πίνακα σε scroll pane
        JScrollPane scrollPane = new JScrollPane(servicesTable);

        // Δημιουργία πάνελ για τα κουμπιά
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Δημιουργία κουμπιού "Book Now"
        JButton bookNowButton = new JButton("Book Now");
        bookNowButton.addActionListener(e -> cardLayout.show(mainPanel, "Book Appointment"));

        // Δημιουργία κουμπιού "Back"
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Main Page"));

        // Προσθήκη κουμπιών στο πάνελ
        buttonPanel.add(bookNowButton);
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);

        // Δημιουργία πάνελ κάτω και προσθήκη κουμπιών
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
