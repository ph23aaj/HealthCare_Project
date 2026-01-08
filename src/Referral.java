import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;



public class Referral {

    private String referralID;
    private String patientID;
    private String fromClinician;
    private String toClinician;
    private String fromFacility;
    private String toFacility;
    private LocalDate referralDate;
    private String urgencyLevel;
    private String reasonForReferral;
    private String clinicalSummary;
    private String requestedInvestigations;
    private ReferralStatus referralStatus;
    private String appointmentID;
    private String referralNotes;
    private LocalDate referralCreated;
    private LocalDate referralLastUpdated;
    private static final DateTimeFormatter REF_DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;


    public Referral(String referralID, String patientID, String fromClinician, String toClinician, String fromFacility,
                    String toFacility, LocalDate referralDate, String urgencyLevel, String reasonForReferral,
                    String clinicalSummary, String requestedInvestigations, ReferralStatus referralStatus,
                    String appointmentID, String referralNotes, LocalDate referralCreated, LocalDate referralLastUpdated) {
        this.referralID = referralID;
        this.patientID = patientID;
        this.fromClinician = fromClinician;
        this.toClinician = toClinician;
        this.fromFacility = fromFacility;
        this.toFacility = toFacility;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.reasonForReferral = reasonForReferral;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.referralStatus = referralStatus;
        this.appointmentID = appointmentID;
        this.referralNotes = referralNotes;
        this.referralCreated = referralCreated;
        this.referralLastUpdated = referralLastUpdated;
    }

    public String getReferralID() {
        return referralID;
    }

    public void setReferralID(String referralID) {
        this.referralID = referralID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getFromClinician() {
        return fromClinician;
    }

    public void setFromClinician(String fromClinician) {
        this.fromClinician = fromClinician;
    }

    public String getToClinician() {
        return toClinician;
    }

    public void setToClinician(String toClinician) {
        this.toClinician = toClinician;
    }

    public String getFromFacility() {
        return fromFacility;
    }

    public void setFromFacility(String fromFacility) {
        this.fromFacility = fromFacility;
    }

    public String getToFacility() {
        return toFacility;
    }

    public void setToFacility(String toFacility) {
        this.toFacility = toFacility;
    }

    public LocalDate getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(LocalDate referralDate) {
        this.referralDate = referralDate;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getReasonForReferral() {
        return reasonForReferral;
    }

    public void setReasonForReferral(String reasonForReferral) {
        this.reasonForReferral = reasonForReferral;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getRequestedInvestigations() {
        return requestedInvestigations;
    }

    public void setRequestedInvestigations(String requestedInvestigations) {
        this.requestedInvestigations = requestedInvestigations;
    }

    public ReferralStatus getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(ReferralStatus referralStatus) {
        this.referralStatus = referralStatus;
    }

    public String getReferralNotes() {
        return referralNotes;
    }

    public void setReferralNotes(String referralNotes) {
        this.referralNotes = referralNotes;
    }

    public LocalDate getReferralCreated() {
        return referralCreated;
    }

    public void setReferralCreated(LocalDate referralCreated) {
        this.referralCreated = referralCreated;
    }

    public LocalDate getReferralLastUpdated() {
        return referralLastUpdated;
    }

    public void setReferralLastUpdated(LocalDate referralLastUpdated) {
        this.referralLastUpdated = referralLastUpdated;
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String v = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }

    /** Splits a CSV line with quote support. */
    private static String[] splitCsvLine(String line) {
        ArrayList<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"'); // escaped quote
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

    public String toCSV() {
        return escapeCsv(referralID) + "," +
                escapeCsv(patientID) + "," +
                escapeCsv(fromClinician) + "," +
                escapeCsv(toClinician) + "," +
                escapeCsv(fromFacility) + "," +
                escapeCsv(toFacility) + "," +
                REF_DATE_FMT.format(referralDate) + "," +
                escapeCsv(urgencyLevel) + "," +
                escapeCsv(reasonForReferral) + "," +
                escapeCsv(clinicalSummary) + "," +
                escapeCsv(requestedInvestigations) + "," +
                escapeCsv(referralStatus.toString()) + "," +
                escapeCsv(appointmentID) + "," +
                escapeCsv(referralNotes) + "," +
                REF_DATE_FMT.format(referralCreated) + "," +
                REF_DATE_FMT.format(referralLastUpdated);
    }

    public static Referral fromCSV(String csvLine) {
        try {
            String[] c = splitCsvLine(csvLine);

            if (c.length < 16) {
                throw new IllegalArgumentException("Expected 16 columns, got " + c.length);
            }

            return new Referral(
                    c[0],                          // referral_id
                    c[1],                          // patient_id
                    c[2],                          // referring_clinician_id
                    c[3],                          // referred_to_clinician_id
                    c[4],                          // referring_facility_id
                    c[5],                          // referred_to_facility_id
                    LocalDate.parse(c[6].trim(), REF_DATE_FMT),         // referral_date
                    c[7],                          // urgency_level
                    c[8],                          // referral_reason
                    c[9],                          // clinical_summary
                    c[10],                         // requested_investigations
                    ReferralStatus.fromString(c[11]), // status
                    c[12],                         // appointment_id
                    c[13],                         // notes
                    LocalDate.parse(c[14].trim(), REF_DATE_FMT),        // created_date
                    LocalDate.parse(c[15].trim(), REF_DATE_FMT)         // last_updated
            );

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse referral CSV line: " + csvLine, e
            );
        }
    }


    @Override
    public String toString() {
        return "Referral{" +
                "referralID='" + referralID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", fromClinician='" + fromClinician + '\'' +
                ", toClinician='" + toClinician + '\'' +
                ", fromFacility='" + fromFacility + '\'' +
                ", toFacility='" + toFacility + '\'' +
                ", referralDate=" + referralDate +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", reasonForReferral='" + reasonForReferral + '\'' +
                ", clinicalSummary='" + clinicalSummary + '\'' +
                ", requestedInvestigations='" + requestedInvestigations + '\'' +
                ", referralStatus=" + referralStatus +
                ", appointmentID='" + appointmentID + '\'' +
                ", referralNotes='" + referralNotes + '\'' +
                ", referralCreated=" + referralCreated +
                ", referralLastUpdated=" + referralLastUpdated +
                '}';
    }
}
