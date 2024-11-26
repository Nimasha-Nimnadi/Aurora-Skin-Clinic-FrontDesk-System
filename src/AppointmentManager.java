import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Manages appointments for this system.
 * Handles creating, updating, viewing, searching appointments and calculating invoices.
 */
public class AppointmentManager {
    /*appointments- Stores all appointments by unique ID for fast retrieval.
      dermatologists- Maps simple keys to available dermatologists for easy selection.
      dermatologistSchedules- Manages each dermatologist's schedule by mapping their name to a HashMap of time
      slots and appointments.
    */
    private final HashMap<String, Appointment> appointments = new HashMap<>();
    private final HashMap<String, Dermatologist> dermatologists = new HashMap<>();
    private final HashMap<String, HashMap<LocalDateTime, Appointment>> dermatologistSchedules = new HashMap<>();

    //Define available time slots to each day for appointments
    private static final Map<DayOfWeek, List<LocalTime>> SCHEDULE = Map.of(
            DayOfWeek.MONDAY, List.of(LocalTime.of(10, 0), LocalTime.of(10, 15), LocalTime.of(10, 30), LocalTime.of(10, 45),
                    LocalTime.of(11, 0), LocalTime.of(11, 15), LocalTime.of(11, 30), LocalTime.of(11, 45),
                    LocalTime.of(12, 0), LocalTime.of(12, 15), LocalTime.of(12, 30), LocalTime.of(12, 45)),
            DayOfWeek.WEDNESDAY, List.of(LocalTime.of(14, 0), LocalTime.of(14, 15), LocalTime.of(14, 30), LocalTime.of(14, 45),
                    LocalTime.of(15, 0), LocalTime.of(15, 15), LocalTime.of(15, 30), LocalTime.of(15, 45)),
            DayOfWeek.FRIDAY, List.of(LocalTime.of(16, 0), LocalTime.of(16, 15), LocalTime.of(16, 30), LocalTime.of(16, 45),
                    LocalTime.of(17, 0), LocalTime.of(17, 15), LocalTime.of(17, 30), LocalTime.of(17, 45),
                    LocalTime.of(18, 0), LocalTime.of(18, 15), LocalTime.of(18, 30), LocalTime.of(18, 45),
                    LocalTime.of(19, 0), LocalTime.of(19, 15), LocalTime.of(19, 30), LocalTime.of(19, 45)),
            DayOfWeek.SATURDAY, List.of(LocalTime.of(9, 0), LocalTime.of(9, 15), LocalTime.of(9, 30), LocalTime.of(9, 45),
                    LocalTime.of(10, 0), LocalTime.of(10, 15), LocalTime.of(10, 30), LocalTime.of(10, 45),
                    LocalTime.of(11, 0), LocalTime.of(11, 15), LocalTime.of(11, 30), LocalTime.of(11, 45))
    );

    /**
     * Initializes the dermatologists available at the clinic and sets up their schedules.
     * This method must be called at the start to create the dermatologists list.
     */

    public void initializeDermatologists() {
        Dermatologist drSamanthi = new Dermatologist("Dr. Samanthi Perera");
        Dermatologist drPrabath = new Dermatologist("Dr. Prabath Akurugoda");

        dermatologists.put("1", drSamanthi);
        dermatologists.put("2", drPrabath);

        // Initialize schedules for each dermatologist with an empty schedule
        dermatologistSchedules.put(drSamanthi.getName(), new HashMap<>());
        dermatologistSchedules.put(drPrabath.getName(), new HashMap<>());
    }

    //This method creating a new appointment by gathering information from the user.
    public void makeAppointment(Scanner scanner) {
        try {
            String nic = getValidatedNIC(scanner);
            String name = getValidatedName(scanner);
            String email = getValidatedEmail(scanner);
            String phone = getValidatedPhoneNumber(scanner);

            Patient patient = new Patient(nic, name, email, phone);

            System.out.println("Choose Dermatologist:");
            System.out.println("1. Dr. Samanthi Perera");
            System.out.println("2. Dr. Prabath Akurugoda");
            System.out.print("Enter choice (1 or 2): ");
            String choice = scanner.nextLine().trim();
            Dermatologist dermatologist = dermatologists.get(choice);

            if (dermatologist == null) {
                System.out.println("Error: Choice is Invalid. Please select a valid number.");
                return;
            }

            LocalDateTime dateTime = selectAppointmentDateTime(scanner);
            Treatment treatment = selectTreatment(scanner);

            // Check if the selected time slot is available
            if (dermatologistSchedules.get(dermatologist.getName()).containsKey(dateTime)) {
                System.out.println("The selected time slot is already booked for " + dermatologist.getName() + ".");
                return;
            }

            // Confirm registration fee
            System.out.println("The registration fee is LKR 500. Would you like to proceed? (y/n)");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("y")) {
                System.out.println("Appointment not confirmed. Returning to main menu.");
                return;
            }

            // Create and store the appointment
            String appointmentID = "Appointment-" + (appointments.size() + 1);
            Appointment appointment = new Appointment(appointmentID, dateTime, patient, dermatologist, treatment);
            appointments.put(appointmentID, appointment);
            dermatologistSchedules.get(dermatologist.getName()).put(dateTime, appointment);

            System.out.println("Appointment created successfully with ID: " + appointmentID);
            Invoice.generateInvoice(appointment);
        } catch (Exception e) {
            System.out.println("An error occurred while creating the appointment: " + e.getMessage());
        }
    }

    // Validates and takes the NIC number from the user.
    private String getValidatedNIC(Scanner scanner) {
        while (true) {
            System.out.print("Enter Patient NIC: ");
            String nic = scanner.nextLine().trim();
            if (nic.matches("\\d{9}[Vv]|\\d{12}")) {
                return nic;
            } else {
                System.out.println("Error: NIC should be either 9 digits followed by a letter or 12 digits.");
            }
        }
    }

    //take patient name input from user
    private String getValidatedName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter Patient Name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                break; // Name is valid, exit the loop
            } else {
                System.out.println("Error: Name cannot be empty. Please enter a valid name.");
            }
        }
        return name;
    }

    // Validates and retrieves an email address from the user
    private String getValidatedEmail(Scanner scanner) {
        while (true) {
            System.out.print("Enter Patient Email: ");
            String email = scanner.nextLine().trim();
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return email;
            } else {
                System.out.println("Error: Invalid email format.");
            }
        }
    }

    // Validates and takes a phone number from the user.
    private String getValidatedPhoneNumber(Scanner scanner) {
        while (true) {
            System.out.print("Enter Patient Phone Number: ");
            String phone = scanner.nextLine().trim();
            if (phone.matches("\\d{10}") || phone.matches("\\+94\\d{9}")) {
                return phone;
            } else {
                System.out.println("Error: Phone number should be 10 digits or in the format +94 followed by 9 digits.");
            }
        }
    }

    //Display available treatment types and allow user to select one
    private Treatment selectTreatment(Scanner scanner) {
        System.out.println("Select The Treatment Type:");
        System.out.println("1. Acne Treatment - LKR 2750.00");
        System.out.println("2. Skin Whitening - LKR 7650.00");
        System.out.println("3. Mole Removal - LKR 3850.00");
        System.out.println("4. Laser Treatment - LKR 12500.00");
        while (true) {
            System.out.print("Enter choice (1-4): ");
            String treatmentChoice = scanner.nextLine().trim();
            switch (treatmentChoice) {
                case "1":
                    return Treatment.ACNE_TREATMENT;
                case "2":
                    return Treatment.SKIN_WHITENING;
                case "3":
                    return Treatment.MOLE_REMOVAL;
                case "4":
                    return Treatment.LASER_TREATMENT;
                default:
                    System.out.println("Invalid choice. Please choose a valid treatment.");
            }
        }
    }

    /**
     * Allow user to select a date and time for the appointment.
     * Ensures the date is within available clinic days and allows selection of time slots.
     */
    private LocalDateTime selectAppointmentDateTime(Scanner scanner) {
        LocalDate appointmentDate = null;

        while (appointmentDate == null) {
            System.out.print("Enter Appointment Date In Correct Format (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                appointmentDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
                DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();

                if (!SCHEDULE.containsKey(dayOfWeek)) {
                    System.out.println("Error: Appointments are only available on Monday, Wednesday, Friday, and Saturday.");
                    appointmentDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        DayOfWeek selectedDay = appointmentDate.getDayOfWeek();
        System.out.println("Available Time Slots on " + selectedDay + ":");
        List<LocalTime> timeSlots = SCHEDULE.get(selectedDay);
        for (int i = 0; i < timeSlots.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, timeSlots.get(i).format(DateTimeFormatter.ofPattern("hh:mm a")));
        }

        LocalTime appointmentTime = null;
        while (appointmentTime == null) {
            System.out.print("Choose a time slot (1-" + timeSlots.size() + "): ");
            try {
                int timeChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (timeChoice >= 0 && timeChoice < timeSlots.size()) {
                    appointmentTime = timeSlots.get(timeChoice);
                } else {
                    System.out.println("Invalid choice. Please choose a valid time slot.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return LocalDateTime.of(appointmentDate, appointmentTime);
    }


    //allow user to filter appointment by date with valid date format
    public void viewAppointmentsByDate(Scanner scanner) {
        System.out.print("Enter Date In Correct Format (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        if (!isValidDate(date)) {
            System.out.println("Error: Invalid date format.");
            return;
        }

        System.out.println("Appointments for " + date + ":");
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDateTime().toLocalDate().toString().equals(date)) {
                System.out.printf("ID: %s,\n Time: %s,\n Patient: %s,\n Dermatologist: %s,\n Treatment: %s\n",
                        appointment.getAppointmentID(),
                        appointment.getDateTime().toLocalTime(),
                        appointment.getPatient().getName(),
                        appointment.getDermatologist().getName(),
                        appointment.getTreatment());
            }
        }
    }

    //Allow user to search appoinment using appoint ID or Patient name
    public void searchAppointment(Scanner scanner) {
        System.out.print("Enter Appointment ID or Patient Name: ");
        String searchKey = scanner.nextLine().trim();

        for (Appointment appointment : appointments.values()) {
            if (appointment.getAppointmentID().equalsIgnoreCase(searchKey) ||
                    appointment.getPatient().getName().equalsIgnoreCase(searchKey)) {
                System.out.printf("Appointment ID: %s,\n Patient: %s,\n DateTime: %s,\n Dermatologist: %s,"
                                + "\n Treatment: %s\n",
                        appointment.getAppointmentID(),
                        appointment.getPatient().getName(),
                        appointment.getDateTime(),
                        appointment.getDermatologist().getName(),
                        appointment.getTreatment());
                return;
            }
        }
        System.out.println("Appointment not found.");
    }


    //show current appointment details, takes new values for update appointment and validate inputs
    public void updateAppointment(Scanner scanner) {
        System.out.print("Enter Appointment ID to update: ");
        String appointmentID = scanner.nextLine().trim();

        Appointment appointment = appointments.get(appointmentID);
        if (appointment != null) {
            System.out.println("Current Appointment Details:");
            System.out.printf("Date and Time: %s, Dermatologist: %s\n", appointment.getDateTime(),
                    appointment.getDermatologist().getName());

            System.out.println("Choose Dermatologist:");
            System.out.println("1. Dr. Samanthi Perera");
            System.out.println("2. Dr. Prabath Akurugoda");
            System.out.print("Enter choice (1 or 2): ");
            String choice = scanner.nextLine().trim();
            Dermatologist newDermatologist = dermatologists.get(choice);

            if (newDermatologist == null) {
                System.out.println("Error: Invalid choice. Please select a valid dermatologist.");
                return;
            }

            LocalDateTime newDateTime = selectAppointmentDateTime(scanner);
            Treatment newTreatment = selectTreatment(scanner);

            if (dermatologistSchedules.get(newDermatologist.getName()).containsKey(newDateTime)) {
                System.out.println("Error: The selected time slot is already booked for "
                        + newDermatologist.getName() + ".");
                return;
            }

            dermatologistSchedules.get(appointment.getDermatologist().getName()).remove(appointment.getDateTime());
            dermatologistSchedules.get(newDermatologist.getName()).put(newDateTime, appointment);

            Appointment updatedAppointment = new Appointment(appointmentID, newDateTime, appointment.getPatient(),
                    newDermatologist, newTreatment);
            appointments.put(appointmentID, updatedAppointment);

            System.out.println("Appointment updated successfully.");
            Invoice.generateInvoice(updatedAppointment);
        } else {
            System.out.println("Error: Appointment ID not found.");
        }
    }

    //Calculate invoice according to user input details
    public void calculateInvoice(Scanner scanner) {
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine().trim();
        Appointment appointment = appointments.get(appointmentID);

        if (appointment != null) {
            Invoice.generateInvoice(appointment);
        } else {
            System.out.println("Error: Requested Appointment not found.");
        }
    }

    //Checks if the provided date string is in a valid format (YYYY-MM-DD)
    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
