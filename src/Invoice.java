import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;

 //This class calculates the total amount, applying tax on the treatment fee and including a fixed registration fee
public class Invoice {
    public static double calculateTotalWithTax(double treatmentFee, double registrationFee) {
        double taxAmount = treatmentFee * 0.025;  // Calculate 2.5% tax based on treatment fee only
        double totalBeforeRounding = treatmentFee + taxAmount + registrationFee;

        // Round up to the nearest whole number
        BigDecimal totalWithTax = new BigDecimal(totalBeforeRounding).setScale(0, RoundingMode.UP);
        return totalWithTax.doubleValue();
    }

    //Generates an invoice for the specified appointment
    public static void generateInvoice(Appointment appointment) {
        double treatmentFee = appointment.getTreatment().getPrice();
        double registrationFee = appointment.getRegistrationFee();
        double taxAmount = treatmentFee * 0.025;  // Tax on treatment fee only
        double totalWithTax = calculateTotalWithTax(treatmentFee, registrationFee);

        //Output the appointment conformation and Billing summary
        System.out.println("\n--- Appointment Conformation---");
        System.out.printf("Appointment ID: %s\n", appointment.getAppointmentID());
        System.out.printf("Patient Name: %s\n", appointment.getPatient().getName());
        System.out.printf("Patient NIC: %s\n", appointment.getPatient().getNIC());
        System.out.printf("Dermatologist Name: %s\n", appointment.getDermatologist().getName());
        System.out.printf("Appointment Date: %s\n", appointment.getDateTime().toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.printf("Appointment Time: %s\n", appointment.getDateTime().toLocalTime()
                .format(DateTimeFormatter.ofPattern("hh:mm a")));

        System.out.println("\n--- Billing Summary ---");
        System.out.printf("Total fee for Treatment: %s - LKR %.2f\n", appointment.getTreatment(), treatmentFee);
        System.out.printf("Registration Fee: LKR %.2f\n", registrationFee);
        System.out.printf("Total Tax (2.5%% on treatment fee): LKR %.2f\n", taxAmount);
        System.out.printf("Total Amount: LKR %.0f"+".00\n", totalWithTax);

        System.out.println("\nThank you for choosing Aurora Skin Care Clinic.");
        System.out.println("Your appointment has been placed successfully.");
    }
}
