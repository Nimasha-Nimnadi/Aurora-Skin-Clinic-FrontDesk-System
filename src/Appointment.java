import java.time.LocalDateTime;

/**
 * Represents an appointment at the Aurora Skin Care Clinic.
 * Stores information - appointment ID, date and time, associated patient,
 * assigned dermatologist, treatment type, and a fixed registration fee.
 */
public class Appointment {
    private final String appointmentID;
    private final LocalDateTime dateTime;
    private final Patient patient;
    private final Dermatologist dermatologist;
    private final Treatment treatment;
    private final double registrationFee = 500;

    //Constructs a new Appointment with specified details
    public Appointment(String appointmentID, LocalDateTime dateTime, Patient patient, Dermatologist dermatologist, Treatment treatment) {
        this.appointmentID = appointmentID;
        this.dateTime = dateTime;
        this.patient = patient;
        this.dermatologist = dermatologist;
        this.treatment = treatment;
    }

    public String getAppointmentID() { return appointmentID; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Patient getPatient() { return patient; }
    public Dermatologist getDermatologist() { return dermatologist; }
    public Treatment getTreatment() { return treatment; }
    public double getRegistrationFee() { return registrationFee; }
}
