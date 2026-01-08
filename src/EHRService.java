import java.util.ArrayList;

public class EHRService {

    /** If no EHR exists, we just create one (in-memory) */
    public EHR getOrCreateEHR(String patientID) {
        return new EHR(patientID);
    }

    /** Builds a complete EHR view for a patient */
    public String buildEHRText(
            Patient patient,
            ArrayList<Referral> referrals,
            ArrayList<Prescription> prescriptions
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== ELECTRONIC HEALTH RECORD (EHR) ===\n\n");

        // Patient details
        sb.append("[PATIENT DETAILS]\n");
        sb.append("Patient ID: ").append(patient.getPatientID()).append("\n");
        sb.append("Name: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
        sb.append("NHS Number: ").append(patient.getNhsNumber()).append("\n");
        sb.append("DOB: ").append(patient.getDateOfBirth()).append("\n");
        sb.append("Gender: ").append(patient.getGender()).append("\n");
        sb.append("Phone: ").append(patient.getPhoneNumber()).append("\n");
        sb.append("Email: ").append(patient.getEmail()).append("\n");
        sb.append("Address: ").append(patient.getAddress()).append("\n");
        sb.append("Postcode: ").append(patient.getPostcode()).append("\n");
        sb.append("Emergency Contact: ").append(patient.getEmergencyContactName())
                .append(" (").append(patient.getEmergencyContactPhone()).append(")\n");
        sb.append("Registered: ").append(patient.getRegisterDate()).append("\n");
        sb.append("GP Surgery ID: ").append(patient.getGpSurgeryID()).append("\n\n");

        // Referrals (via text files)
        sb.append("[REFERRALS]\n");
        if (referrals.isEmpty()) {
            sb.append("No referrals found.\n\n");
        } else {
            for (Referral r : referrals) {
                sb.append("--- Referral ").append(r.getReferralID()).append(" ---\n");
                sb.append(readTextFile("referral_" + r.getReferralID() + ".txt"));
                sb.append("\n");
            }
        }

        // Prescriptions (via text files)
        sb.append("[PRESCRIPTIONS]\n");
        if (prescriptions.isEmpty()) {
            sb.append("No prescriptions found.\n\n");
        } else {
            for (Prescription p : prescriptions) {
                sb.append("--- Prescription ").append(p.getPrescriptionID()).append(" ---\n");
                sb.append(readTextFile("prescription_" + p.getPrescriptionID() + ".txt"));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String readTextFile(String filename) {
        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            return "(No text file found: " + filename + ")\n";
        }
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
