import java.util.ArrayList;

public class PatientManager {

    private final String filename;
    private final ArrayList<Patient> patients;

    public PatientManager(String filename) {
        this.filename = filename;
        this.patients = new ArrayList<>();
    }

    /** Loads patients from CSV into memory (clears existing data first). */
    public void load() {
        patients.clear();

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No data found in: " + filename);
            return;
        }

        // Skip header (line 0)
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            Patient p = Patient.fromCSV(line);
            patients.add(p);
        }
    }

    /** Returns a copy of the list so outside code can't accidentally modify internal data. */
    public ArrayList<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public Patient findByPatientID(String patientID) {
        for (Patient p : patients) {
            if (p.getPatientID().equals(patientID)) {
                return p;
            }
        }
        return null;
    }

    public Patient findByNhsNumber(String nhsNumber) {
        for (Patient p : patients) {
            if (p.getNhsNumber().equals(nhsNumber)) {
                return p;
            }
        }
        return null;
    }

    /** Adds a patient to memory only (saveAll() would persist it). */
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    /** Saves all patients back to the CSV file (overwrites file). */
    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();

        // Header (match your patients.csv header exactly)
        out.add("patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,email,address,postcode,emergency_contact_name,emergency_contact_phone,registration_date,gp_surgery_id");

        for (Patient p : patients) {
            out.add(p.toCSV());
        }

        CSVHandler.writeLines(filename, out);
    }
}
