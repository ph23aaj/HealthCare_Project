import java.util.ArrayList;

public class PrescriptionTextWriter {

    public static void writePrescriptionText(Prescription p) {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("=== PRESCRIPTION (SIMULATED OUTPUT) ===");
        lines.add("Prescription ID: " + p.getPrescriptionID());
        lines.add("Status: " + p.getStatus());
        lines.add("");

        lines.add("Patient ID: " + p.getPatientID());
        lines.add("Clinician ID: " + p.getClinicianID());
        lines.add("Appointment ID: " + p.getAppointmentID());
        lines.add("");

        lines.add("Prescription Date: " + p.getPrescriptionDate());
        lines.add("Issue Date: " + p.getIssueDate());
        lines.add("Collection Date: " + p.getCollectionDate());
        lines.add("");

        lines.add("Medication: " + p.getMedicationName());
        lines.add("Dosage: " + p.getDosage());
        lines.add("Frequency: " + p.getFrequency());
        lines.add("Duration Days: " + p.getDurationDays());
        lines.add("Quantity: " + p.getQuantity());
        lines.add("");

        lines.add("Instructions:");
        lines.add(p.getInstructions());
        lines.add("");

        lines.add("Pharmacy: " + p.getPharmacyName());
        lines.add("=== END ===");

        String file = "prescription_" + p.getPrescriptionID() + ".txt";
        CSVHandler.writeLines(file, lines);
    }
}
