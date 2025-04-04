package model;

// Η κλάση Service αναπαριστά μια υπηρεσία, με ιδιότητες όπως ID, όνομα, περιγραφή, διάρκεια και τιμή.
public class Service {
    private int serviceId;
    private String serviceName;
    private String description;
    private int duration; 
    private double price;

    // Ο constructor αρχικοποιεί όλες τις ιδιότητες μιας υπηρεσίας με τις τιμές που δίνονται.
    public Service(int serviceId, String serviceName, String description, int duration, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.duration = duration;
        this.price = price;
    }

    // Getters και Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return serviceName;
    }
}
