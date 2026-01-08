import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.io.File;


public class Test {
    public static void main(String[] args) {
        ClinicianManager cm = new ClinicianManager("clinicians.csv");
        cm.load();

        System.out.println("Loaded clinicians: " + cm.getAllClinicians().size());

        Clinician c1 = cm.findByClinicianID("C001");
        System.out.println("C001: " + c1);

        System.out.println("GP clinicians count: " + cm.findByTitle("GP").size());
        System.out.println("Clinicians at S001: " + cm.findByWorkplaceID("S001").size());

        //------------------------ Appointments Test --------------------------------

        AppointmentManager am = new AppointmentManager("appointments.csv");
        am.load();

        System.out.println("Loaded appointments: " + am.getAllAppointments().size());

        System.out.println("Appointments for P001: " + am.getAppointmentsByPatientID("P001").size());
        System.out.println("Appointments for C001: " + am.getAppointmentsByClinicianID("C001").size());

        boolean available = am.isClinicianAvailable("C001", LocalDate.parse("2025-09-20"), LocalTime.parse("09:00"));
        System.out.println("C001 available on 2025-09-20 at 09:00? " + available);

//        AppointmentManager al = new AppointmentManager("appointments.csv");
//        am.load();
//
//        System.out.println("Before save: " + am.getAllAppointments().size());
//
//        // Save without changes (should preserve file)
//        am.saveAll();
//
//        System.out.println("Saved appointments.csv successfully");

        PatientManager pm = new PatientManager("patients.csv");
        pm.load();

        ClinicianManager cliniciantest = new ClinicianManager("clinicians.csv");
        cliniciantest.load();

        AppointmentManager appointtest = new AppointmentManager("appointments.csv");
        appointtest.load();

        System.out.println("Appointments before: " + am.getAllAppointments().size());



//        Appointment booked = am.bookAppointment(
//                "1234567890",
//                LocalDate.parse("2025-09-20"),
//                LocalTime.parse("10:00"),
//                AppointmentType.ROUTINE,
//                "Booking test via code",
//                "Created during test",
//                pm,
//                cliniciantest
//        );
//
//        System.out.println("Booked: " + booked);
//        System.out.println("Appointment1: " + am.getAppointmentByID("A001"));
//        System.out.println("Appointments after: " + am.getAllAppointments().size());
//
//        // Test Cancel Appointment
//        am.cancelAppointment(booked.getAppointmentID());

        // Test Modify Appointment
        // am.rescheduleAppointment("A016", LocalDate.parse("2027-09-20"), LocalTime.parse("12:00"));

        // Test Delete Appointment



        System.out.println("Before: " + am.getAllAppointments().size());



        am.load(); // reload from file to confirm persistence
        System.out.println("After: " + am.getAllAppointments().size());




        ReferralManager rm = ReferralManager.getInstance();
        rm.setFilename("referrals.csv");
        System.out.println("Using referrals file: " + new java.io.File("referrals.csv").getAbsolutePath());

        rm.load();

        int before = rm.getAllReferrals().size();
        System.out.println("Before create, count = " + before);

        // Use a date that matches your dataset style conceptually
        LocalDate referralDate = LocalDate.now();

        Referral created = rm.createReferral(
                "P001",                 // patientID
                "C001",                 // fromClinician
                "C005",                 // toClinician
                "S001",                 // fromFacility
                "H001",                 // toFacility
                referralDate,
                "Routine",              // urgencyLevel (matches your CSV example)
                "Referral reason test",
                "Clinical summary test (created by test)",
                "ECG|Echo",
                "Notes created by TestReferralCreate"
        );

        System.out.println("Created referral: " + created.getReferralID());

        // Reload and confirm it's saved
        rm.load();
        int after = rm.getAllReferrals().size();
        System.out.println("After reload, count = " + after);

        if (after != before + 1) {
            throw new RuntimeException("Referral count did not increase after creation!");
        }

        boolean found = false;
        for (Referral r : rm.getAllReferrals()) {
            if (r.getReferralID().equals(created.getReferralID())) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("Created referral not found after reload!");
        }

        // Check output text file
        String txtName = "referral_" + created.getReferralID() + ".txt";
        File f = new File(txtName);
        System.out.println("Referral text file exists? " + f.exists() + " (" + txtName + ")");

        if (!f.exists()) {
            throw new RuntimeException("Referral text file was not created: " + txtName);
        }

        System.out.println("✅ Create + persist + output file test passed.");
    }
}
