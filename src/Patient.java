/**
 * This class represents a patient at the Aurora Skin Care Clinic.
 * Stores patient details like NIC, name, email, and phone number.
 */
public class Patient {
    private final String NIC;
    private String name;
    private String email;
    private String phoneNumber;

    public Patient(String NIC, String name, String email, String phoneNumber) {
        this.NIC = NIC;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getNIC() { return NIC; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
}
