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

    public Appointment getAppointmentByID(String appointmentID) {
        for (Appointment a : appointments){
            if (a.getAppointmentID().equals(appointmentID)){
                return a;
            }
        }
        return null;
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
    // Generates AppointmentID Automatically
    private String generateNextAppointmentID() {
        int max = 0;

        for (Appointment a : appointments) {
            String id = a.getAppointmentID(); // e.g. "A001"
            if (id != null && id.startsWith("A")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {
                    // ignore malformed IDs
                }
            }
        }

        int next = max + 1;
        return String.format("A%03d", next); // A001, A002, ...
    }


    public Appointment bookAppointment(
            String nhsNumber,
            LocalDate date,
            LocalTime time,
            AppointmentType type,
            String reasonForVisit,
            String notes,
            PatientManager patientManager,
            ClinicianManager clinicianManager
    ) {
        // Find patient by NHS number
        Patient patient = patientManager.findByNhsNumber(nhsNumber);
        if (patient == null) {
            throw new IllegalArgumentException("No patient found with NHS number: " + nhsNumber);
        }

        // Find first available clinician (you can filter by title if you want)
        Clinician chosen = null;
        for (Clinician c : clinicianManager.getAllClinicians()) {
            if (isClinicianAvailable(c.getClinicianID(), date, time)) {
                chosen = c;
                break;
            }
        }

        if (chosen == null) {
            throw new IllegalStateException("No clinicians available for " + date + " at " + time);
        }



        // Build appointment
        String newId = generateNextAppointmentID();

        Appointment newAppointment = new Appointment(
                newId,
                patient.getPatientID(),
                chosen.getClinicianID(),
                chosen.getWorkplaceID(),
                date,
                time,
                15, // default duration
                type,
                AppointmentStatus.SCHEDULED,
                reasonForVisit,
                notes,
                LocalDate.now(),
                LocalDate.now()
        );

        // 4) Save to in-memory list + persist
        appointments.add(newAppointment);
        saveAll();

        return newAppointment;
    }

    public void cancelAppointment(String appointmentID) {
        Appointment a = getAppointmentByID(appointmentID);

        if (a == null) {
            throw new IllegalArgumentException("No appointment found with ID: " + appointmentID);
        }

        // If you want to prevent double-cancel:
        if (a.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment already cancelled: " + appointmentID);
        }

        a.setStatus(AppointmentStatus.CANCELLED);
        a.setAppointmentModified(LocalDate.now());

        saveAll();
    }

    public void rescheduleAppointment(String appointmentID, LocalDate newDate, LocalTime newTime) {
        Appointment a = getAppointmentByID(appointmentID);

        if (a == null) {
            throw new IllegalArgumentException("No appointment found with ID: " + appointmentID);
        }

        if (a.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule a cancelled appointment: " + appointmentID);
        }

        // If date/time unchanged, no-op
        if (a.getAppointmentDate().equals(newDate) && a.getAppointmentTime().equals(newTime)) {
            return;
        }

        // Check availability ignoring this appointment itself
        if (!isClinicianAvailable(a.getClinicianID(), newDate, newTime)) {
            throw new IllegalStateException("Clinician not available on " + newDate + " at " + newTime);
        }

        a.setAppointmentDate(newDate);
        a.setAppointmentTime(newTime);
        a.setAppointmentModified(LocalDate.now());

        saveAll();
    }

    public void deleteAppointment(String appointmentID) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(appointmentID)) {
                appointments.remove(i);
                saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("No appointment found with ID: " + appointmentID);
    }



}
