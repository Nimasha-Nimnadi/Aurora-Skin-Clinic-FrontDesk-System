import java.util.Scanner;

/**
 * Entry point for the Aurora Skin Care Clinic Management System.
 * This class provides a menu in console interface for managing appointments,
 * updating appointments, viewing appointments by date, searching for appointments,
 * and calculating invoices.
 */

public class Service {
    public static void main(String[] args) {
        AppointmentManager appointmentManager = new AppointmentManager();
        appointmentManager.initializeDermatologists();
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("\nAurora Skin Care Clinic Management System");
            System.out.println("1. Make New Appointment");
            System.out.println("2. Update Appointment");
            System.out.println("3. View Appointments by Date");
            System.out.println("4. Search Appointment");
            System.out.println("5. Calculate Invoice");
            System.out.println("6. Exit");

            int choice = getValidatedChoice(scanner);

            switch (choice) {
                case 1 -> appointmentManager.makeAppointment(scanner);
                case 2 -> appointmentManager.updateAppointment(scanner);
                case 3 -> appointmentManager.viewAppointmentsByDate(scanner);
                case 4 -> appointmentManager.searchAppointment(scanner);
                case 5 -> appointmentManager.calculateInvoice(scanner);
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }


 /** Validates and retrieves a menu choice from the user.
 * Ensures the choice is within the valid range.
 **/
    private static int getValidatedChoice(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 6) return choice;
                else System.out.println("Please enter a number between 1 and 6.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
