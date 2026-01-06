import java.time.LocalDate;
import java.time.LocalTime;

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



    }
}
