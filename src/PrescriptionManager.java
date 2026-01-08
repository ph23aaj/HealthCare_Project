import java.time.LocalDate;
import java.util.ArrayList;

public class PrescriptionManager {

    private final String filename;
    private final ArrayList<Prescription> prescriptions;

    public PrescriptionManager(String filename) {
        this.filename = filename;
        this.prescriptions = new ArrayList<>();
    }

    public void load() {
        prescriptions.clear();

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No data found in: " + filename);
            return;
        }

        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            prescriptions.add(Prescription.fromCSV(line));
        }
    }

    public ArrayList<Prescription> getAllPrescriptions() {
        return new ArrayList<>(prescriptions);
    }

    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();
        out.add("prescription_id,patient_id,clinician_id,appointment_id,prescription_date,medication_name,dosage,frequency,duration_days,quantity,instructions,pharmacy_name,status,issue_date,collection_date");

        for (Prescription p : prescriptions) {
            out.add(p.toCSV());
        }
        CSVHandler.writeLines(filename, out);
    }

    public Prescription getByID(String prescriptionID) {
        for (Prescription p : prescriptions) {
            if (p.getPrescriptionID().equals(prescriptionID)) return p;
        }
        return null;
    }

    private String generateNextPrescriptionID() {
        int max = 0;
        for (Prescription p : prescriptions) {
            String id = p.getPrescriptionID(); // e.g. RX001 or P001? (depends on your file)
            if (id != null) {
                // Try common patterns: "RX001" or "PR001"
                String digits = id.replaceAll("\\D+", "");
                if (!digits.isEmpty()) {
                    try {
                        int n = Integer.parseInt(digits);
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return String.format("RX%03d", max + 1);
    }

    public Prescription createPrescription(
            String patientID,
            String clinicianID,
            String appointmentID,
            LocalDate prescriptionDate,
            String medicationName,
            String dosage,
            String frequency,
            int durationDays,
            String quantity,
            String instructions,
            String pharmacyName
    ) {
        String newId = generateNextPrescriptionID();
        LocalDate now = LocalDate.now();

        Prescription p = new Prescription(
                newId,
                patientID,
                clinicianID,
                appointmentID,
                prescriptionDate,
                medicationName,
                dosage,
                frequency,
                durationDays,
                quantity,
                instructions,
                pharmacyName,
                "Issued",       // fixed on create (your requirement)
                now,            // issue_date
                null            // collection_date optional
        );

        prescriptions.add(p);
        saveAll();

        PrescriptionTextWriter.writePrescriptionText(p);
        return p;
    }

    public void deletePrescription(String prescriptionID) {
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getPrescriptionID().equals(prescriptionID)) {
                prescriptions.remove(i);
                saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("No prescription found with ID: " + prescriptionID);
    }

    /** Change status to "Collected" and set collection date to today (or allow a specific date). */
    public void markCollected(String prescriptionID, LocalDate collectionDate) {
        Prescription p = getByID(prescriptionID);
        if (p == null) throw new IllegalArgumentException("No prescription found with ID: " + prescriptionID);

        p.setStatus("Collected");
        p.setCollectionDate(collectionDate == null ? LocalDate.now() : collectionDate);

        saveAll();
        PrescriptionTextWriter.writePrescriptionText(p); // optional regenerate
    }
}
