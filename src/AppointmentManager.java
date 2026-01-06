import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentManager {

    private final String filename;
    private final ArrayList<Appointment> appointments;

    public AppointmentManager(String filename) {
        this.filename = filename;
        this.appointments = new ArrayList<>();
    }

    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();

        // Header must match appointments.csv exactly
        out.add(
                "appointment_id,patient_id,clinician_id,facility_id," +
                        "appointment_date,appointment_time,duration_minutes," +
                        "appointment_type,status,reason_for_visit,notes," +
                        "created_date,last_modified"
        );

        for (Appointment a : appointments) {
            out.add(a.toCSV());
        }

        CSVHandler.writeLines(filename, out);
    }


    public void load() {
        appointments.clear();

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No data found in: " + filename);
            return;
        }

        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            appointments.add(Appointment.fromCSV(line));
        }
    }

    public ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public ArrayList<Appointment> getAppointmentsByPatientID(String patientID) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatientID().equals(patientID)) {
                result.add(a);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getAppointmentsByClinicianID(String clinicianID) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getClinicianID().equals(clinicianID)) {
                result.add(a);
            }
        }
        return result;
    }

    /** True if clinician has no appointment at the exact same date+time that is not cancelled. */
    public boolean isClinicianAvailable(String clinicianID, LocalDate date, LocalTime time) {
        for (Appointment a : appointments) {
            if (a.getClinicianID().equals(clinicianID)
                    && a.getAppointmentDate().equals(date)
                    && a.getAppointmentTime().equals(time)
                    && a.getStatus() != AppointmentStatus.CANCELLED) {
                return false;
            }
        }
        return true;
    }
}
