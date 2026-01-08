import java.time.LocalDate;
import java.util.ArrayList;

public class ClinicianManager {

    private final String filename;
    private final ArrayList<Clinician> clinicians;

    public ClinicianManager(String filename) {
        this.filename = filename;
        this.clinicians = new ArrayList<>();
    }

    public void load() {
        clinicians.clear();

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No data found in: " + filename);
            return;
        }

        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            Clinician c = Clinician.fromCSV(line);
            clinicians.add(c);
        }
    }

    public ArrayList<Clinician> getAllClinicians() {
        return new ArrayList<>(clinicians);
    }

    public Clinician getClinicianByID(String clinicianID) {
        for (Clinician c : clinicians) {
            if (c.getClinicianID().equals(clinicianID)) return c;
        }
        return null;
    }

    public ArrayList<Clinician> findByWorkplaceID(String workplaceID) {
        ArrayList<Clinician> result = new ArrayList<>();
        for (Clinician c : clinicians) {
            if (c.getWorkplaceID().equals(workplaceID)) {
                result.add(c);
            }
        }
        return result;
    }

    public ArrayList<Clinician> findByTitle(String title) {
        ArrayList<Clinician> result = new ArrayList<>();
        for (Clinician c : clinicians) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                result.add(c);
            }
        }
        return result;
    }

    // ---------- Persistence + CRUD ----------

    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();

        out.add("clinician_id,first_name,last_name,title,speciality,gmc_number," +
                "phone_number,email,workplace_id,workplace_type,employment_status,start_date");

        for (Clinician c : clinicians) {
            out.add(c.toCSV());
        }

        CSVHandler.writeLines(filename, out);
    }

    public void addClinician(Clinician clinician) {
        clinicians.add(clinician);
        saveAll();
    }

    public void removeClinician(String clinicianID) {
        for (int i = 0; i < clinicians.size(); i++) {
            if (clinicians.get(i).getClinicianID().equals(clinicianID)) {
                clinicians.remove(i);
                saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("No clinician found with ID: " + clinicianID);
    }

    public void updateClinicianDetails(
            String clinicianID,
            String firstName,
            String lastName,
            String phone,
            String email,
            String title,
            String speciality,
            String gmcNumber,
            String workplaceID,
            String workplaceType,
            String employmentStatus,
            LocalDate startDate
    ) {
        Clinician c = getClinicianByID(clinicianID);
        if (c == null) throw new IllegalArgumentException("No clinician found with ID: " + clinicianID);

        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setPhoneNumber(phone);
        c.setEmail(email);

        c.setTitle(title);
        c.setSpeciality(speciality);
        c.setGmcNumber(gmcNumber);
        c.setWorkplaceID(workplaceID);
        c.setWorkplaceType(workplaceType);
        c.setClinicianEmploymentStatus(employmentStatus);
        c.setClinicianStartDate(startDate);

        saveAll();
    }

    // Auto-generate next clinician ID
    private String generateNextClinicianID() {
        int max = 0;
        for (Clinician c : clinicians) {
            String id = c.getClinicianID();
            if (id != null && id.startsWith("C")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("C%03d", max + 1);
    }

    public Clinician createAndAddClinician(
            String firstName, String lastName,
            String title, String speciality, String gmcNumber,
            String phone, String email,
            String workplaceID, String workplaceType,
            String employmentStatus, LocalDate startDate
    ) {
        String id = generateNextClinicianID();

        Clinician c = new Clinician(
                id,
                firstName,
                lastName,
                title,
                speciality,
                gmcNumber,
                phone,
                email,
                workplaceID,
                workplaceType,
                employmentStatus,
                startDate
        );

        addClinician(c);
        return c;
    }
}
