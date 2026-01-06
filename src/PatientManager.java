import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Patient getPatientByID(String patientID) {
        for (Patient p : patients) {
            if (p.getPatientID().equals(patientID)) return p;
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

    public void addPatient(Patient patient) {
        patients.add(patient);
        saveAll();
    }

    private String generateNextPatientID() {
        int max = 0;

        for (Patient p : patients) {
            String id = p.getPatientID(); // e.g. "P001"
            if (id != null && id.startsWith("P")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }

        return String.format("P%03d", max + 1);
    }

    /** Saves all patients back to the CSV file (overwrites file). */
    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();

        // Header
        out.add("patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,email,address,postcode,emergency_contact_name,emergency_contact_phone,registration_date,gp_surgery_id");

        for (Patient p : patients) {
            out.add(p.toCSV());
        }

        CSVHandler.writeLines(filename, out);
    }

    public void removePatient(String patientID) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientID().equals(patientID)) {
                patients.remove(i);
                saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("No patient found with ID: " + patientID);
    }

    public void updatePatientDetails(String patientID, String firstName, String lastName,
                                     String phone, String email, String address, String postcode,
                                     String emergencyName, String emergencyPhone) {

        Patient p = getPatientByID(patientID);
        if (p == null) throw new IllegalArgumentException("No patient found with ID: " + patientID);

        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setPhoneNumber(phone);
        p.setEmail(email);
        p.setAddress(address);
        p.setPostcode(postcode);
        p.setEmergencyContactName(emergencyName);
        p.setEmergencyContactPhone(emergencyPhone);

        saveAll();
    }

    public Patient createAndAddPatient(
            String firstName, String lastName, String dobText, String nhsNumber, String gender,
            String phone, String email, String address, String postcode,
            String emergencyName, String emergencyPhone,
            String gpSurgeryID
    ) {
        // Validate NHS uniqueness
        if (findByNhsNumber(nhsNumber) != null) {
            throw new IllegalArgumentException("A patient with this NHS number already exists.");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            Date dob = sdf.parse(dobText.trim());
            Date regDate = new Date(); // today

            String newPatientID = generateNextPatientID();

            Patient p = new Patient(
                    newPatientID,
                    firstName.trim(),
                    lastName.trim(),
                    dob,
                    nhsNumber.trim(),
                    gender.trim(),
                    phone.trim(),
                    email.trim(),
                    address.trim(),
                    postcode.trim(),
                    emergencyName.trim(),
                    emergencyPhone.trim(),
                    regDate,
                    gpSurgeryID.trim()
            );

            addPatient(p); // will saveAll if you updated it
            return p;

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date of birth. Use YYYY-MM-DD.");
        }
    }

}
