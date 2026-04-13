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

        User(String userId, String name, String email, String phone, String password) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.medicalHistory = new ArrayList<>();
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
        // Here is your default user for the presentation
        User defaultUser = new User("USER1001", "Test Patient", "patient@example.com", "9876543210", "123");
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

    static void userDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n===== Patient Dashboard =====");
            System.out.println("Welcome, " + currentUser.name + " (" + currentUser.userId + ")");
            System.out.println("1. Browse Specialists & Request Slot");
            System.out.println("2. View My Appointments");
            System.out.println("3. View Medical History");
            System.out.println("4. Logout");
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
                    viewMedicalHistory();
                    break;
                case "4":
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
        displayDoctors(new ArrayList<>(doctors.values()));

        System.out.print("\nEnter Doctor ID to book appointment (or 'back' to cancel): ");
        String doctorId = scanner.nextLine();

        if (doctorId.equalsIgnoreCase("back")) return;

        if (doctors.containsKey(doctorId)) {
            bookAppointment(scanner, doctorId);
        } else {
            System.out.println("Invalid Doctor ID!");
        }
    }

    static void displayDoctors(List<Doctor> docList) {
        for (Doctor doc : docList) {
            System.out.println("[" + doc.doctorId + "] " + doc.name + " | " + doc.specialization + " | Rs. " + doc.consultationCharge);
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
        System.out.println("\n===== My Appointments =====");
        boolean found = false;
        for (Appointment appt : appointments) {
            if (appt.userId.equals(currentUser.userId)) {
                Doctor doc = doctors.get(appt.doctorId);
                System.out.println("ID: " + appt.appointmentId + " | " + doc.name + " | Date: " + appt.date + " | Time: " + appt.time + " | Status: " + appt.status);
                found = true;
            }
        }
        if (!found) System.out.println("No confirmed appointments found.");
    }

    static void viewMedicalHistory() {
        System.out.println("\n===== Medical History =====");
        if (currentUser.medicalHistory.isEmpty()) {
            System.out.println("No medical records found.");
        } else {
            for (String record : currentUser.medicalHistory) System.out.println(record);
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

    static void doctorDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n===== " + currentDoctor.name + "'s Dashboard =====");
            System.out.println(currentDoctor.specialization + " | ID: " + currentDoctor.doctorId);
            System.out.println("\n1. View Pending Requests (" + currentDoctor.pendingRequests.size() + " new)");
            System.out.println("2. View Today's Confirmed Schedule");
            System.out.println("3. Manage Patient Medical Records");
            System.out.println("4. Logout");
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
