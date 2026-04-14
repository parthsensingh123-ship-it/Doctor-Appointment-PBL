import java.io.*;
import java.time.*;
import java.util.*;

public class EzRemedy {

    static class User {
        String userId;
        String name;
        String email;
        String phone;
        String password;
        String problem;
        String consultationType;
        List<String> medicalHistory;
        List<String> pastIllnesses;
        List<String> medicationReminders;

        User(String userId, String name, String email, String phone, String password) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.medicalHistory = new ArrayList<>();
            this.pastIllnesses = new ArrayList<>();
            this.medicationReminders = new ArrayList<>();
        }
    }

    static class Doctor {
        String doctorId;
        String name;
        String specialization;
        String phone;
        double consultationCharge;
        String password;
        double latitude;
        double longitude;
        boolean isAvailable; 
        List<String> availableSlots;
        List<AppointmentRequest> pendingRequests;
        List<Appointment> appointments;

        Doctor(String doctorId, String name, String specialization, String phone, 
               double consultationCharge, String password, double latitude, double longitude) {
            this.doctorId = doctorId;
            this.name = name;
            this.specialization = specialization;
            this.phone = phone;
            this.consultationCharge = consultationCharge;
            this.password = password;
            this.latitude = latitude;
            this.longitude = longitude;
            this.isAvailable = true; // Default to online
            this.availableSlots = new ArrayList<>();
            this.pendingRequests = new ArrayList<>();
            this.appointments = new ArrayList<>();
            initializeSlots();
        }

        void initializeSlots() {
            availableSlots.add("09:00 AM");
            availableSlots.add("10:00 AM");
            availableSlots.add("11:00 AM");
            availableSlots.add("02:00 PM");
            availableSlots.add("03:00 PM");
            availableSlots.add("04:00 PM");
        }
    }

    static class AppointmentRequest {
        String requestId;
        String userId;
        String userName;
        String userEmail;
        String userPhone;
        String problem;
        String consultationType;
        String requestedTime;
        String doctorId;
        String requestDate;
        String status;

        AppointmentRequest(String requestId, String userId, String userName, String userEmail, String userPhone,
                          String problem, String consultationType, String requestedTime, String doctorId) {
            this.requestId = requestId;
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userPhone = userPhone;
            this.problem = problem;
            this.consultationType = consultationType;
            this.requestedTime = requestedTime;
            this.doctorId = doctorId;
            this.requestDate = LocalDate.now().toString();
            this.status = "PENDING APPROVAL";
        }
    }

    static class Appointment {
        String appointmentId;
        String userId;
        String doctorId;
        String date;
        String time;
        String status;
        String userName;
        String doctorName;

        Appointment(String appointmentId, String userId, String doctorId, String date, String time) {
            this.appointmentId = appointmentId;
            this.userId = userId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.status = "CONFIRMED";
        }
    }

    static class MedicalRecord {
        String recordId;
        String userId;
        String doctorId;
        String diagnosis;
        String prescription;
        String date;

        MedicalRecord(String recordId, String userId, String doctorId, String diagnosis, String prescription) {
            this.recordId = recordId;
            this.userId = userId;
            this.doctorId = doctorId;
            this.diagnosis = diagnosis;
            this.prescription = prescription;
            this.date = LocalDate.now().toString();
        }
    }

    static Map<String, User> users = new HashMap<>();
    static Map<String, Doctor> doctors = new HashMap<>();
    static List<AppointmentRequest> appointmentRequests = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static List<MedicalRecord> medicalRecords = new ArrayList<>();
    
    static int userCounter = 1000;
    static int doctorCounter = 5000;
    static int requestCounter = 1;
    static int appointmentCounter = 1;
    static int recordCounter = 1;

    static User currentUser = null;
    static Doctor currentDoctor = null;

    public static void main(String[] args) {
        initializeDoctors();
        initializeUsers(); 
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== EzRemedy Portal =====");
            System.out.println("1. Login as Patient");
            System.out.println("2. Register as Patient");
            System.out.println("3. Login as Doctor");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userLoginMenu(scanner);
                    break;
                case "2":
                    userRegistration(scanner);
                    break;
                case "3":
                    doctorLoginMenu(scanner);
                    break;
                case "4":
                    running = false;
                    System.out.println("Thank you for using EzRemedy!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    static void initializeUsers() {
        User defaultUser = new User("USER1001", "Test Patient", "patient@example.com", "9876543210", "123");
        
        // Adding dummy data for presentation
        defaultUser.pastIllnesses.add("Mild Asthma (Diagnosed 2018)");
        defaultUser.pastIllnesses.add("Appendectomy (2021)");
        defaultUser.medicationReminders.add("Albuterol Inhaler - 2 puffs as needed");
        defaultUser.medicationReminders.add("Vitamin D3 - 1 tablet after breakfast");
        
        users.put("USER1001", defaultUser);
        userCounter = 1001; 
    }

    static void initializeDoctors() {
        doctors.put("DOC5001", new Doctor("DOC5001", "Dr. Rajesh Kumar", "Cardiology", "9876543210", 500, "doc123", 28.7041, 77.1025));
        doctors.put("DOC5002", new Doctor("DOC5002", "Dr. Priya Sharma", "Dermatology", "9876543211", 400, "doc123", 28.7041, 77.1026));
        doctors.put("DOC5003", new Doctor("DOC5003", "Dr. Amit Patel", "Orthopedics", "9876543212", 450, "doc123", 28.7042, 77.1025));
        doctors.put("DOC5004", new Doctor("DOC5004", "Dr. Neha Singh", "Pediatrics", "9876543213", 350, "doc123", 28.7043, 77.1024));
        doctors.put("DOC5005", new Doctor("DOC5005", "Dr. Vikram Rao", "Neurology", "9876543214", 600, "doc123", 28.7040, 77.1027));
    }

    static void userLoginMenu(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (users.containsKey(userId) && users.get(userId).password.equals(password)) {
            currentUser = users.get(userId);
            System.out.println("\nLogin Successful! Redirecting to Patient Dashboard...");
            userDashboard(scanner);
        } else {
            System.out.println("Invalid ID or Password!");
        }
    }

    static void userRegistration(Scanner scanner) {
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Create Password: ");
        String password = scanner.nextLine();

        String userId = "USER" + (++userCounter);
        User newUser = new User(userId, name, email, phone, password);
        users.put(userId, newUser);

        System.out.println("\nRegistration successful!");
        System.out.println("Your Patient ID: " + userId);
        currentUser = newUser;
        userDashboard(scanner);
    }

    // --- EXPANDED PATIENT DASHBOARD ---
    static void userDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n=========================================");
            System.out.println("   PATIENT DASHBOARD: " + currentUser.name.toUpperCase());
            System.out.println("=========================================");
            System.out.println("1. Browse Specialists & Request Slot");
            System.out.println("2. Appointment Reminders");
            System.out.println("3. Personal Medical Records & History");
            System.out.println("4. Medication Reminders");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    browseDoctors(scanner);
                    break;
                case "2":
                    viewUserAppointments();
                    break;
                case "3":
                    viewMedicalProfile();
                    break;
                case "4":
                    manageMedicationReminders(scanner);
                    break;
                case "5":
                    inDashboard = false;
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void browseDoctors(Scanner scanner) {
        System.out.println("\n===== Available Specialists =====");
        
        boolean doctorsAvailable = false;
        for (Doctor doc : doctors.values()) {
            if (doc.isAvailable) {
                System.out.println("[" + doc.doctorId + "] " + doc.name + " | " + doc.specialization + " | Rs. " + doc.consultationCharge);
                doctorsAvailable = true;
            }
        }

        if (!doctorsAvailable) {
            System.out.println("No doctors are currently online.");
            return;
        }

        System.out.print("\nEnter Doctor ID to book appointment (or 'back' to cancel): ");
        String doctorId = scanner.nextLine();

        if (doctorId.equalsIgnoreCase("back")) return;

        if (doctors.containsKey(doctorId) && doctors.get(doctorId).isAvailable) {
            bookAppointment(scanner, doctorId);
        } else {
            System.out.println("Invalid Doctor ID or Doctor is offline!");
        }
    }

    static void bookAppointment(Scanner scanner, String doctorId) {
        Doctor doctor = doctors.get(doctorId);

        System.out.println("\n===== Appointment Request Form =====");
        System.out.println("Booking with: " + doctor.name);
        
        System.out.print("Confirm Email Address [" + currentUser.email + "]: ");
        String emailInput = scanner.nextLine();
        String emailToUse = emailInput.isEmpty() ? currentUser.email : emailInput;

        System.out.print("Describe your problem/complaint in detail: ");
        String problem = scanner.nextLine();

        System.out.println("\nConsultation Type:");
        System.out.println("1. Online / Telehealth");
        System.out.println("2. In-Person Clinic Visit");
        System.out.print("Choose (1/2): ");
        String consulType = scanner.nextLine().equals("1") ? "Online" : "In-Person";

        System.out.println("\nPreferred Time Slot:");
        for (int i = 0; i < doctor.availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + doctor.availableSlots.get(i));
        }
        System.out.print("Choose slot number: ");
        int slotChoice = Integer.parseInt(scanner.nextLine()) - 1;

        if (slotChoice < 0 || slotChoice >= doctor.availableSlots.size()) {
            System.out.println("Invalid slot selection!");
            return;
        }

        String selectedSlot = doctor.availableSlots.get(slotChoice);

        currentUser.problem = problem;
        currentUser.consultationType = consulType;

        String requestId = "REQ" + (requestCounter++);
        AppointmentRequest appointmentRequest = new AppointmentRequest(
            requestId, currentUser.userId, currentUser.name, emailToUse, currentUser.phone,
            problem, consulType, selectedSlot, doctorId
        );

        appointmentRequests.add(appointmentRequest);
        doctor.pendingRequests.add(appointmentRequest);

        System.out.println("\n>>> Success! Your medical details and slot request have been sent.");
        System.out.println(">>> Status: " + appointmentRequest.status);
    }

    static void viewUserAppointments() {
        System.out.println("\n===== Appointment Reminders =====");
        boolean found = false;
        for (Appointment appt : appointments) {
            if (appt.userId.equals(currentUser.userId)) {
                Doctor doc = doctors.get(appt.doctorId);
                System.out.println("ID: " + appt.appointmentId + " | " + doc.name + " | Date: " + appt.date + " | Time: " + appt.time + " | Status: " + appt.status);
                found = true;
            }
        }
        if (!found) System.out.println("No upcoming appointments.");
    }

    // --- NEW: Detailed Medical Profile Method ---
    static void viewMedicalProfile() {
        System.out.println("\n===== Personal Medical Profile =====");
        System.out.println("Patient Name: " + currentUser.name);
        System.out.println("Contact: " + currentUser.phone + " | " + currentUser.email);
        
        System.out.println("\n--- Past Illnesses & Conditions ---");
        if (currentUser.pastIllnesses.isEmpty()) {
            System.out.println("No past illnesses recorded.");
        } else {
            for (String illness : currentUser.pastIllnesses) System.out.println("- " + illness);
        }

        System.out.println("\n--- Doctor Prescriptions & Records ---");
        if (currentUser.medicalHistory.isEmpty()) {
            System.out.println("No medical records found.");
        } else {
            for (String record : currentUser.medicalHistory) System.out.println(record);
        }
    }

    // --- NEW: Medication Reminders Method ---
    static void manageMedicationReminders(Scanner scanner) {
        System.out.println("\n===== Medication Reminders =====");
        if (currentUser.medicationReminders.isEmpty()) {
            System.out.println("No active medication reminders.");
        } else {
            for (int i = 0; i < currentUser.medicationReminders.size(); i++) {
                System.out.println((i + 1) + ". " + currentUser.medicationReminders.get(i));
            }
        }

        System.out.println("\nOptions: 1. Add Reminder | 2. Go Back");
        System.out.print("Choose: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Enter Medication Name & Schedule (e.g., Aspirin - 1 pill morning): ");
            String med = scanner.nextLine();
            currentUser.medicationReminders.add(med);
            System.out.println("Reminder added successfully!");
        }
    }

    static void doctorLoginMenu(Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (doctors.containsKey(doctorId) && doctors.get(doctorId).password.equals(password)) {
            currentDoctor = doctors.get(doctorId);
            System.out.println("\nSecure Login Successful! Redirecting to Provider Dashboard...");
            doctorDashboard(scanner);
        } else {
            System.out.println("Invalid Doctor ID or Password!");
        }
    }

    // --- EXPANDED DOCTOR DASHBOARD ---
    static void doctorDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n=========================================");
            System.out.println("   WELCOME, " + currentDoctor.name.toUpperCase() + "!");
            System.out.println("=========================================");
            String statusStr = currentDoctor.isAvailable ? "ONLINE (Accepting Patients)" : "OFFLINE (Not Accepting Patients)";
            System.out.println("Current Status: [" + statusStr + "]");
            System.out.println("\n1. View Pending Requests (" + currentDoctor.pendingRequests.size() + " new)");
            System.out.println("2. Today's Schedule & Meetings");
            System.out.println("3. Manage Patient Medical Records");
            System.out.println("4. Toggle Availability (Go Offline/Online)");
            System.out.println("5. Logout");
            System.out.print("Choose an action: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewPendingRequests(scanner);
                    break;
                case "2":
                    viewDoctorAppointments();
                    break;
                case "3":
                    managePatientRecords(scanner);
                    break;
                case "4":
                    currentDoctor.isAvailable = !currentDoctor.isAvailable;
                    System.out.println(">>> Status changed to: " + (currentDoctor.isAvailable ? "ONLINE" : "OFFLINE"));
                    break;
                case "5":
                    inDashboard = false;
                    currentDoctor = null;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void viewPendingRequests(Scanner scanner) {
        System.out.println("\n===== Pending Appointment Requests =====");

        if (currentDoctor.pendingRequests.isEmpty()) {
            System.out.println("No pending requests right now.");
            return;
        }

        List<AppointmentRequest> toRemove = new ArrayList<>();
        int index = 1;

        for (AppointmentRequest req : currentDoctor.pendingRequests) {
            System.out.println("\n[" + index + "] Request ID: " + req.requestId);
            System.out.println("    Patient: " + req.userName + " (" + req.userId + ")");
            System.out.println("    Contact: " + req.userEmail + " | " + req.userPhone);
            System.out.println("    Problem: " + req.problem);
            System.out.println("    Type: " + req.consultationType + " | Requested Slot: " + req.requestedTime);
            System.out.println("    Status: [" + req.status + "]");
            index++;
        }

        System.out.println("\n0. Go Back to Dashboard");
        System.out.print("Select request number to process (or 0 to exit): ");

        String choice = scanner.nextLine();
        if (choice.equals("0")) return;

        try {
            int reqIndex = Integer.parseInt(choice) - 1;
            if (reqIndex >= 0 && reqIndex < currentDoctor.pendingRequests.size()) {
                AppointmentRequest selectedReq = currentDoctor.pendingRequests.get(reqIndex);

                System.out.println("\nAction for " + selectedReq.userName + ":");
                System.out.println("1. Approve & Add to Schedule");
                System.out.println("2. Reject Request");
                System.out.print("Choose (1/2): ");
                String action = scanner.nextLine();

                if (action.equals("1")) {
                    approveAppointmentRequest(selectedReq);
                    toRemove.add(selectedReq);
                } else if (action.equals("2")) {
                    rejectAppointmentRequest(selectedReq);
                    toRemove.add(selectedReq);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }

        currentDoctor.pendingRequests.removeAll(toRemove);
    }

    static void approveAppointmentRequest(AppointmentRequest req) {
        System.out.print("Confirm appointment date (YYYY-MM-DD): ");
        Scanner scanner = new Scanner(System.in);
        String date = scanner.nextLine();

        System.out.print("Confirm time slot (Patient requested " + req.requestedTime + "): ");
        String timeInput = scanner.nextLine();
        String finalTime = timeInput.isEmpty() ? req.requestedTime : timeInput;

        String appointmentId = "APPT" + (appointmentCounter++);
        Appointment appointment = new Appointment(appointmentId, req.userId, currentDoctor.doctorId, date, finalTime);
        appointment.userName = req.userName;
        appointment.doctorName = currentDoctor.name;

        appointments.add(appointment);
        req.status = "APPROVED";

        System.out.println("\n>>> Appointment Approved and added to schedule!");
        System.out.println("Appointment ID: " + appointmentId + " | Time: " + finalTime);
    }

    static void rejectAppointmentRequest(AppointmentRequest req) {
        req.status = "REJECTED";
        System.out.println("\n>>> Appointment request rejected.");
    }

    static void viewDoctorAppointments() {
        System.out.println("\n===== Today's Confirmed Schedule =====");

        boolean found = false;
        for (Appointment appt : appointments) {
            if (appt.doctorId.equals(currentDoctor.doctorId)) {
                System.out.println("[" + appt.time + "] Patient: " + appt.userName + " | Date: " + appt.date + " | Status: " + appt.status);
                found = true;
            }
        }
        if (!found) System.out.println("Your schedule is clear for now.");
    }

    static void managePatientRecords(Scanner scanner) {
        System.out.println("\n===== Manage Patient Records =====");
        System.out.println("1. View Patient Records");
        System.out.println("2. Add Medical Record");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();
        if (choice.equals("1")) viewPatientRecords();
        else if (choice.equals("2")) addMedicalRecord(scanner);
    }

    static void viewPatientRecords() {
        System.out.println("\n===== Patient Medical Records =====");
        boolean found = false;
        for (MedicalRecord record : medicalRecords) {
            if (record.doctorId.equals(currentDoctor.doctorId)) {
                User patient = users.get(record.userId);
                System.out.println("Record: " + record.recordId + " | Patient: " + (patient != null ? patient.name : "Unknown") + " | Diagnosis: " + record.diagnosis);
                found = true;
            }
        }
        if (!found) System.out.println("No medical records found.");
    }

    static void addMedicalRecord(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        if (!users.containsKey(patientId)) { System.out.println("Patient not found!"); return; }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Prescription: ");
        String prescription = scanner.nextLine();

        String recordId = "MED" + (recordCounter++);
        MedicalRecord record = new MedicalRecord(recordId, patientId, currentDoctor.doctorId, diagnosis, prescription);
        medicalRecords.add(record);

        User patient = users.get(patientId);
        patient.medicalHistory.add("[" + record.date + "] Dr. " + currentDoctor.name + " | Diagnosis: " + diagnosis + " | Rx: " + prescription);
        System.out.println("\n>>> Medical record saved successfully! (ID: " + recordId + ")");
    }
}
