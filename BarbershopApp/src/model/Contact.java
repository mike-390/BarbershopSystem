package model;

// Η κλάση Contact αναπαριστά τα στοιχεία επικοινωνίας ενός χρήστη, όπως όνομα, email, τηλέφωνο και μήνυμα.
public class Contact {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String message;
    
    // Ο constructor αρχικοποιεί τα πεδία της κλάσης με τις τιμές που δίνονται για όνομα, email, τηλέφωνο και μήνυμα.
    public Contact(String name, String email, String phone, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.message = message;
    }

    // Getters και setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
