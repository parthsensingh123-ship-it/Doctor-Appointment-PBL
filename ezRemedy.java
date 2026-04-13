import java.io.*;
import java.time.*;
import java.util.*;

public class EzRemedy {
    static class User {
        String userId;
        String name;
        String phone;
        String password;
        String problem;
        String consultationType;
        List<String> medicalHistory;

        User(String userId, String name, String phone, String password) {
            this.userId = userId;
            this.name = name;
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
        String userPhone;
        String problem;
        String consultationType;
        String doctorId;
        String requestDate;
        String status;

        AppointmentRequest(String requestId, String userId, String userName, String userPhone,
                          String problem, String consultationType, String doctorId) {
            this.requestId = requestId;
            this.userId = userId;
            this.userName = userName;
            this.userPhone = userPhone;
            this.problem = problem;
            this.consultationType = consultationType;
            this.doctorId = doctorId;
            this.requestDate = LocalDate.now().toString();
            this.status = "PENDING";
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
        initializeUsers(); // We added this line to load the default user!
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== EzRemedy =====");
            System.out.println("1. Login as User");
            System.out.println("2. Register as User");
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

    // --- NEW METHOD TO CREATE DEFAULT USER ---
    static void initializeUsers() {
        User defaultUser = new User("USER1001", "Test Patient", "9876543210", "password123");
        users.put("USER1001", defaultUser);
        userCounter = 1001; // Ensures the next person who registers gets USER1002
    }

    static void initializeDoctors() {
        doctors.put("DOC5001", new Doctor("DOC5001", "Dr. Rajesh Kumar", "Cardiology", "9876543210", 500, "doc123", 28.7041, 77.1025));
        doctors.put("DOC5002", new Doctor("DOC5002", "Dr. Priya Sharma", "Dermatology", "9876543211", 400, "doc123", 28.7041, 77.1026));
        doctors.put("DOC5003", new Doctor("DOC5003", "Dr. Amit Patel", "Orthopedics", "9876543212", 450, "doc123", 28.7042, 77.1025));
        doctors.put("DOC5004", new Doctor("DOC5004", "Dr. Neha Singh", "Pediatrics", "9876543213", 350, "doc123", 28.7043, 77.1024));
        doctors.put("DOC5005", new Doctor("DOC5005", "Dr. Vikram Rao", "Neurology", "9876543214", 600, "doc123", 28.7040, 77.1027));
    }

    static void userLoginMenu(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (users.containsKey(userId) && users.get(userId).password.equals(password)) {
            currentUser = users.get(userId);
            System.out.println("\nWelcome, " + currentUser.name + "!");
            userDashboard(scanner);
        } else {
            System.out.println("Invalid User ID or Password!");
        }
    }

    static void userRegistration(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String userId = "USER" + (++userCounter);
        User newUser = new User(userId, name, phone, password);
        users.put(userId, newUser);

        System.out.println("\nRegistration successful!");
        System.out.println("Your User ID: " + userId);
        currentUser = newUser;
        userDashboard(scanner);
    }

    static void userDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n===== User Dashboard =====");
            System.out.println("1. Browse Doctors");
            System.out.println("2. My Appointments");
            System.out.println("3. Medical History");
            System.out.println("4. My Profile");
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
                    viewMedicalHistory();
                    break;
                case "4":
                    viewUserProfile();
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
        System.out.println("\n===== Available Doctors =====");
        System.out.println("1. View All Doctors");
        System.out.println("2. Filter by Specialization");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        List<Doctor> availableDoctors = new ArrayList<>();

        if (choice.equals("1")) {
            availableDoctors.addAll(doctors.values());
        } else if (choice.equals("2")) {
            System.out.print("Enter Specialization (Cardiology/Dermatology/Orthopedics/Pediatrics/Neurology): ");
            String spec = scanner.nextLine();
            for (Doctor doc : doctors.values()) {
                if (doc.specialization.equalsIgnoreCase(spec)) {
                    availableDoctors.add(doc);
                }
            }
        }

        if (availableDoctors.isEmpty()) {
            System.out.println("No doctors found!");
            return;
        }

        displayDoctors(availableDoctors);

        System.out.print("\nEnter Doctor ID to book appointment (or 'back' to go back): ");
        String doctorId = scanner.nextLine();

        if (doctorId.equalsIgnoreCase("back")) {
            return;
        }

        if (doctors.containsKey(doctorId)) {
            bookAppointment(scanner, doctorId);
        } else {
            System.out.println("Invalid Doctor ID!");
        }
    }

    static void displayDoctors(List<Doctor> docList) {
        System.out.println("\n------------------------------------");
        for (Doctor doc : docList) {
            System.out.println("Doctor ID: " + doc.doctorId);
            System.out.println("Name: " + doc.name);
            System.out.println("Specialization: " + doc.specialization);
            System.out.println("Phone: " + doc.phone);
            System.out.println("Consultation Charge: Rs. " + doc.consultationCharge);
            System.out.println("Available Slots: " + String.join(", ", doc.availableSlots));
            System.out.println("------------------------------------");
        }
    }

    static void bookAppointment(Scanner scanner, String doctorId) {
        Doctor doctor = doctors.get(doctorId);

        System.out.println("\n===== Book Appointment =====");
        System.out.print("Enter Problem/Complaint: ");
        String problem = scanner.nextLine();

        System.out.println("Consultation Type: ");
        System.out.println("1. Online");
        System.out.println("2. In-Person");
        System.out.print("Choose: ");
        String consulType = scanner.nextLine().equals("1") ? "Online" : "In-Person";

        System.out.println("\nSelect Time Slot:");
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
            requestId, currentUser.userId, currentUser.name, currentUser.phone,
            problem, consulType, doctorId
        );

        appointmentRequests.add(appointmentRequest);
        doctor.pendingRequests.add(appointmentRequest);

        System.out.println("\nAppointment request submitted!");
        System.out.println("Request ID: " + requestId);
        System.out.println("Doctor: " + doctor.name);
        System.out.println("Selected Slot: " + selectedSlot);
        System.out.println("Consultation Type: " + consulType);
        System.out.println("Charge: Rs. " + doctor.consultationCharge);
        System.out.println("\nYour request is pending doctor approval...");
    }

    static void viewUserAppointments() {
        System.out.println("\n===== My Appointments =====");
        List<Appointment> userAppts = new ArrayList<>();

        for (Appointment appt : appointments) {
            if (appt.userId.equals(currentUser.userId)) {
                userAppts.add(appt);
            }
        }

        if (userAppts.isEmpty()) {
            System.out.println("No appointments yet!");
            return;
        }

        for (Appointment appt : userAppts) {
            Doctor doc = doctors.get(appt.doctorId);
            System.out.println("Appointment ID: " + appt.appointmentId);
            System.out.println("Doctor: " + doc.name + " (" + doc.specialization + ")");
            System.out.println("Date: " + appt.date);
            System.out.println("Time: " + appt.time);
            System.out.println("Status: " + appt.status);
            System.out.println("Consultation Charge: Rs. " + doc.consultationCharge);
            System.out.println("------------------------------------");
        }
    }

    static void viewMedicalHistory() {
        System.out.println("\n===== Medical History =====");

        if (currentUser.medicalHistory.isEmpty()) {
            System.out.println("No medical records yet!");
            return;
        }

        for (String record : currentUser.medicalHistory) {
            System.out.println(record);
            System.out.println("------------------------------------");
        }
    }

    static void viewUserProfile() {
        System.out.println("\n===== User Profile =====");
        System.out.println("User ID: " + currentUser.userId);
        System.out.println("Name: " + currentUser.name);
        System.out.println("Phone: " + currentUser.phone);
        System.out.println("Current Problem: " + (currentUser.problem != null ? currentUser.problem : "Not specified"));
        System.out.println("Consultation Type: " + (currentUser.consultationType != null ? currentUser.consultationType : "Not specified"));
    }

    static void doctorLoginMenu(Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (doctors.containsKey(doctorId) && doctors.get(doctorId).password.equals(password)) {
            currentDoctor = doctors.get(doctorId);
            System.out.println("\nWelcome, " + currentDoctor.name + "!");
            doctorDashboard(scanner);
        } else {
            System.out.println("Invalid Doctor ID or Password!");
        }
    }

    static void doctorDashboard(Scanner scanner) {
        boolean inDashboard = true;

        while (inDashboard) {
            System.out.println("\n===== Doctor Dashboard =====");
            System.out.println("1. View Pending Appointment Requests");
            System.out.println("2. View My Appointments");
            System.out.println("3. Manage Patient Records");
            System.out.println("4. My Profile");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

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
                    viewDoctorProfile();
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
            System.out.println("No pending requests!");
            return;
        }

        List<AppointmentRequest> toRemove = new ArrayList<>();
        int index = 1;

        for (AppointmentRequest req : currentDoctor.pendingRequests) {
            System.out.println("\n" + index + ". Request ID: " + req.requestId);
            System.out.println("   Patient Name: " + req.userName);
            System.out.println("   Phone: " + req.userPhone);
            System.out.println("   Problem: " + req.problem);
            System.out.println("   Consultation Type: " + req.consultationType);
            System.out.println("   Request Date: " + req.requestDate);
            System.out.println("   Status: " + req.status);
            index++;
        }

        System.out.println("\n0. Go Back");
        System.out.print("Select request number to approve/reject (or 0 to go back): ");

        String choice = scanner.nextLine();

        if (choice.equals("0")) {
            return;
        }

        try {
            int reqIndex = Integer.parseInt(choice) - 1;
            if (reqIndex >= 0 && reqIndex < currentDoctor.pendingRequests.size()) {
                AppointmentRequest selectedReq = currentDoctor.pendingRequests.get(reqIndex);

                System.out.println("\n1. Approve");
                System.out.println("2. Reject");
                System.out.print("Choose action: ");
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
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        Scanner scanner = new Scanner(System.in);
        String date = scanner.nextLine();

        System.out.print("Enter time slot (HH:MM AM/PM): ");
        String time = scanner.nextLine();

        String appointmentId = "APPT" + (appointmentCounter++);
        Appointment appointment = new Appointment(appointmentId, req.userId, currentDoctor.doctorId, date, time);
        appointment.userName = req.userName;
        appointment.doctorName = currentDoctor.name;

        appointments.add(appointment);
        req.status = "APPROVED";

        System.out.println("\nAppointment approved!");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient: " + req.userName);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
    }

    static void rejectAppointmentRequest(AppointmentRequest req) {
        req.status = "REJECTED";
        System.out.println("\nAppointment request rejected!");
    }

    static void viewDoctorAppointments() {
        System.out.println("\n===== My Appointments (Today & Upcoming) =====");

        List<Appointment> docAppts = new ArrayList<>();
        for (Appointment appt : appointments) {
            if (appt.doctorId.equals(currentDoctor.doctorId)) {
                docAppts.add(appt);
            }
        }

        if (docAppts.isEmpty()) {
            System.out.println("No appointments scheduled!");
            return;
        }

        for (Appointment appt : docAppts) {
            System.out.println("Appointment ID: " + appt.appointmentId);
            System.out.println("Patient: " + appt.userName);
            System.out.println("Date: " + appt.date);
            System.out.println("Time: " + appt.time);
            System.out.println("Status: " + appt.status);
            System.out.println("------------------------------------");
        }
    }

    static void managePatientRecords(Scanner scanner) {
        System.out.println("\n===== Manage Patient Records =====");
        System.out.println("1. View Patient Records");
        System.out.println("2. Add Medical Record");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            viewPatientRecords();
        } else if (choice.equals("2")) {
            addMedicalRecord(scanner);
        }
    }

    static void viewPatientRecords() {
        System.out.println("\n===== Patient Medical Records =====");

        List<MedicalRecord> docRecords = new ArrayList<>();
        for (MedicalRecord record : medicalRecords) {
            if (record.doctorId.equals(currentDoctor.doctorId)) {
                docRecords.add(record);
            }
        }

        if (docRecords.isEmpty()) {
            System.out.println("No medical records yet!");
            return;
        }

        for (MedicalRecord record : docRecords) {
            User patient = users.get(record.userId);
            System.out.println("Record ID: " + record.recordId);
            System.out.println("Patient: " + (patient != null ? patient.name : "Unknown"));
            System.out.println("Diagnosis: " + record.diagnosis);
            System.out.println("Prescription: " + record.prescription);
            System.out.println("Date: " + record.date);
            System.out.println("------------------------------------");
        }
    }

    static void addMedicalRecord(Scanner scanner) {
        System.out.print("Enter Patient User ID: ");
        String patientId = scanner.nextLine();

        if (!users.containsKey(patientId)) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Prescription: ");
        String prescription = scanner.nextLine();

        String recordId = "MED" + (recordCounter++);
        MedicalRecord record = new MedicalRecord(recordId, patientId, currentDoctor.doctorId, diagnosis, prescription);
        medicalRecords.add(record);

        User patient = users.get(patientId);
        patient.medicalHistory.add("Date: " + record.date + " | Doctor: " + currentDoctor.name + 
                                   " | Diagnosis: " + diagnosis + " | Prescription: " + prescription);

        System.out.println("\nMedical record added successfully!");
        System.out.println("Record ID: " + recordId);
    }

    static void viewDoctorProfile() {
        System.out.println("\n===== Doctor Profile =====");
        System.out.println("Doctor ID: " + currentDoctor.doctorId);
        System.out.println("Name: " + currentDoctor.name);
        System.out.println("Specialization: " + currentDoctor.specialization);
        System.out.println("Phone: " + currentDoctor.phone);
        System.out.println("Consultation Charge: Rs. " + currentDoctor.consultationCharge);
        System.out.println("Available Slots: " + String.join(", ", currentDoctor.availableSlots));
    }
}
