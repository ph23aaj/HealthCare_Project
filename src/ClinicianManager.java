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

        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            Clinician c = Clinician.fromCSV(line);
            clinicians.add(c);
        }
    }

    public ArrayList<Clinician> getAllClinicians() {
        return new ArrayList<>(clinicians);
    }

    public Clinician findByClinicianID(String clinicianID) {
        for (Clinician c : clinicians) {
            if (c.getClinicianID().equals(clinicianID)) {
                return c;
            }
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

    public ArrayList<Clinician> findByTitle(String title) { // e.g. "GP", "Nurse", "Specialist"
        ArrayList<Clinician> result = new ArrayList<>();
        for (Clinician c : clinicians) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                result.add(c);
            }
        }
        return result;
    }
}
