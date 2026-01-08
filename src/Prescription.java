import java.time.LocalDate;
import java.util.ArrayList;

public class Prescription {

    private String prescriptionID;
    private String patientID;
    private String clinicianID;
    private String appointmentID;
    private LocalDate prescriptionDate;
    private String medicationName;
    private String dosage;
    private String frequency;
    private int durationDays;
    private String quantity;
    private String instructions;
    private String pharmacyName;
    private String status;
    private LocalDate issueDate;
    private LocalDate collectionDate;

    public Prescription(String prescriptionID, String patientID, String clinicianID, String appointmentID,
                        LocalDate prescriptionDate, String medicationName, String dosage, String frequency,
                        int durationDays, String quantity, String instructions, String pharmacyName,
                        String status, LocalDate issueDate, LocalDate collectionDate) {
        this.prescriptionID = prescriptionID;
        this.patientID = patientID;
        this.clinicianID = clinicianID;
        this.appointmentID = appointmentID;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getClinicianID() {
        return clinicianID;
    }

    public void setClinicianID(String clinicianID) {
        this.clinicianID = clinicianID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    private static String[] splitCsvLine(String line) {
        ArrayList<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                parts.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        parts.add(sb.toString());
        return parts.toArray(new String[0]);
    }

    private static LocalDate parseDateOrNull(String text) {
        if (text == null) return null;
        String t = text.trim();
        if (t.isEmpty()) return null;
        return LocalDate.parse(t); // ISO yyyy-MM-dd
    }

    private static String fmtDateOrEmpty(LocalDate d) {
        return (d == null) ? "" : d.toString();
    }

    // ----- CSV helpers -----
    private static String escapeCsv(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String v = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }

    // ----- toCSV / fromCSV -----
    public String toCSV() {
        return escapeCsv(prescriptionID) + "," +
                escapeCsv(patientID) + "," +
                escapeCsv(clinicianID) + "," +
                escapeCsv(appointmentID) + "," +
                fmtDateOrEmpty(prescriptionDate) + "," +
                escapeCsv(medicationName) + "," +
                escapeCsv(dosage) + "," +
                escapeCsv(frequency) + "," +
                durationDays + "," +
                escapeCsv(quantity) + "," +
                escapeCsv(instructions) + "," +
                escapeCsv(pharmacyName) + "," +
                escapeCsv(status) + "," +
                fmtDateOrEmpty(issueDate) + "," +
                fmtDateOrEmpty(collectionDate);
    }

    public static Prescription fromCSV(String csvLine) {
        try {
            String[] c = splitCsvLine(csvLine);
            if (c.length < 15) {
                throw new IllegalArgumentException("Expected 15 columns, got " + c.length);
            }

            return new Prescription(
                    c[0],                          // prescription_id
                    c[1],                          // patient_id
                    c[2],                          // clinician_id
                    c[3],                          // appointment_id
                    parseDateOrNull(c[4]),         // prescription_date
                    c[5],                          // medication_name
                    c[6],                          // dosage
                    c[7],                          // frequency
                    Integer.parseInt(c[8].trim()), // duration_days
                    c[9],                          // quantity
                    c[10],                         // instructions
                    c[11],                         // pharmacy_name
                    c[12],                         // status
                    parseDateOrNull(c[13]),        // issue_date
                    parseDateOrNull(c[14])         // collection_date (can be blank)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse prescription CSV line: " + csvLine, e);
        }
    }

    @Override
    public String toString() {
        return "Prescriptions{" +
                "prescriptionID='" + prescriptionID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", clinicianID='" + clinicianID + '\'' +
                ", appointmentID='" + appointmentID + '\'' +
                ", prescriptionDate=" + prescriptionDate +
                ", medicationName='" + medicationName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", Frequency='" + frequency + '\'' +
                ", durationDays=" + durationDays +
                ", quantity='" + quantity + '\'' +
                ", instructions='" + instructions + '\'' +
                ", pharmacyName='" + pharmacyName + '\'' +
                ", prescriptionStatus='" + status + '\'' +
                ", issueDate=" + issueDate +
                ", collectionDate=" + collectionDate +
                '}';
    }
}
