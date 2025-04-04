package model;

import java.sql.Date;
import java.sql.Time;

/*Η κλάση Appointment αναπαριστά ένα ραντεβού με πληροφορίες όπως ID, όνομα πελάτη, τηλέφωνο, email, υπηρεσία, ημερομηνία, ώρα και κατάσταση.
 * Χρησιμοποιείται για να αποθηκεύει και να διαχειρίζεται δεδομένα ραντεβού στην εφαρμογή. */
public class Appointment {
    private int appointmentId;
    private String customerName;
    private String phone;
    private String email;
    private int serviceId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;

    // Ο constructor της κλάσης αρχικοποιεί όλα τα πεδία ενός αντικειμένου Appointment με τις τιμές που δίνονται ως παράμετροι.
    public Appointment(int appointmentId, String customerName, String phone, String email, int serviceId, Date appointmentDate, Time appointmentTime, String status) {
        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Getters και Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    // Η μέθοδος toString() επιστρέφει μια συμβολοσειρά με όλες τις ιδιότητες του ραντεβού.
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", serviceId=" + serviceId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                '}';
    }
}
