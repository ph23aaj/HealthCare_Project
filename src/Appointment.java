import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private String appointmentID;
    private String patientID;
    private String clinicianID;
    private String facilityID;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;
    private AppointmentType type;
    private AppointmentStatus status;
    private String reasonForVisit;
    private String appointmentNotes;
    private LocalDate appointmentCreated;
    private LocalDate appointmentModified;

    public Appointment(String appointmentID, String patientID, String clinicianID, String facilityID, LocalDate appointmentDate, LocalTime appointmentTime,
                       int durationMinutes, AppointmentType type, AppointmentStatus status,
                       String reasonForVisit, String appointmentNotes, LocalDate appointmentCreated,
                       LocalDate appointmentModified) {

        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.clinicianID = clinicianID;
        this.facilityID = facilityID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.appointmentNotes = appointmentNotes;
        this.appointmentCreated = appointmentCreated;
        this.appointmentModified = appointmentModified;
    }

    public String getAppointmentID() {
        return appointmentID;
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

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getReasonForVisit() {
        return reasonForVisit;
    }

    public void setReasonForVisit(String reasonForVisit) {
        this.reasonForVisit = reasonForVisit;
    }

    public String getAppointmentNotes() {
        return appointmentNotes;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
    }

    public LocalDate getAppointmentCreated() {
        return appointmentCreated;
    }

    public void setAppointmentCreated(LocalDate appointmentCreated) {
        this.appointmentCreated = appointmentCreated;
    }

    public LocalDate getAppointmentModified() {
        return appointmentModified;
    }

    public void setAppointmentModified(LocalDate appointmentModified) {
        this.appointmentModified = appointmentModified;
    }

    public String toCSV() {
        return String.join(",",
                appointmentID,
                patientID,
                clinicianID,
                facilityID,
                appointmentDate.toString(),
                appointmentTime.toString(),
                String.valueOf(durationMinutes),
                escapeCsv(type.toString()),
                escapeCsv(status.toString()),
                escapeCsv(reasonForVisit),
                escapeCsv(appointmentNotes),
                appointmentCreated.toString(),
                appointmentModified.toString()
        );
    }

    public static Appointment fromCSV(String csvLine) {
        String[] c = CSVHandler.parseCsvLine(csvLine);

        if (c.length != 13) {
            throw new IllegalArgumentException("Invalid appointment row: expected 13 columns, got " + c.length);
        }

        return new Appointment(
                c[0],                          // appointment_id
                c[1],                          // patient_id
                c[2],                          // clinician_id
                c[3],                          // facility_id
                LocalDate.parse(c[4]),         // appointment_date
                LocalTime.parse(c[5]),         // appointment_time
                Integer.parseInt(c[6]),        // duration_minutes
                AppointmentType.fromString(c[7]),
                AppointmentStatus.fromString(c[8]),
                c[9],                          // reason_for_visit
                c[10],                         // notes
                LocalDate.parse(c[11]),        // created_date
                LocalDate.parse(c[12])         // last_modified
        );
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";

        boolean containsComma = value.contains(",");
        boolean containsQuote = value.contains("\"");
        boolean containsNewLine = value.contains("\n") || value.contains("\r");
        boolean needsQuotes = containsComma || containsQuote || containsNewLine;

        String escapedValue = value.replace("\"", "\"\"");

        if (needsQuotes) {
            return "\"" + escapedValue + "\"";
        } else {
            return escapedValue;
        }
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID='" + appointmentID + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", durationMinutes=" + durationMinutes +
                ", type=" + type +
                ", status=" + status +
                ", reasonForVist='" + reasonForVisit + '\'' +
                ", appointmentNotes='" + appointmentNotes + '\'' +
                ", appointmentCreated=" + appointmentCreated +
                ", appointmentModified=" + appointmentModified +
                '}';
    }
}
