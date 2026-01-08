import java.time.LocalDate;

public class Clinician extends People{

    private String clinicianID;
    private String title;
    private String speciality;
    private String gmcNumber;
    private String workplaceID;
    private String workplaceType;
    private String clinicianEmploymentStatus;
    private LocalDate clinicianStartDate;

    public Clinician(String clinicianID, String firstName, String lastName,
                     String title, String speciality, String gmcNumber, String phoneNumber,
                     String email, String workplaceID, String workplaceType,
                     String clinicianEmploymentStatus, LocalDate clinicianStartDate) {

        super(firstName, lastName, phoneNumber, email);
        this.clinicianID = clinicianID;
        this.title = title;
        this.speciality = speciality;
        this.gmcNumber = gmcNumber;
        this.workplaceID = workplaceID;
        this.workplaceType = workplaceType;
        this.clinicianEmploymentStatus = clinicianEmploymentStatus;
        this.clinicianStartDate = clinicianStartDate;
    }

    public String getClinicianID() {
        return clinicianID;
    }

    public void setClinicianID(String clinicianID) {
        this.clinicianID = clinicianID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getGmcNumber() {
        return gmcNumber;
    }

    public void setGmcNumber(String gmcNumber) {
        this.gmcNumber = gmcNumber;
    }

    public String getWorkplaceID() {
        return workplaceID;
    }

    public void setWorkplaceID(String workplaceID) {
        this.workplaceID = workplaceID;
    }

    public String getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(String workplaceType) {
        this.workplaceType = workplaceType;
    }

    public String getClinicianEmploymentStatus() {
        return clinicianEmploymentStatus;
    }

    public void setClinicianEmploymentStatus(String clinicianEmploymentStatus) {
        this.clinicianEmploymentStatus = clinicianEmploymentStatus;
    }

    public LocalDate getClinicianStartDate() {
        return clinicianStartDate;
    }

    public void setClinicianStartDate(LocalDate clinicianStartDate) {
        this.clinicianStartDate = clinicianStartDate;
    }

    public String toCSV() {
        return String.join(",",
                clinicianID,
                escapeCsv(getFirstName()),
                escapeCsv(getLastName()),
                escapeCsv(title),
                escapeCsv(speciality),
                escapeCsv(gmcNumber),
                escapeCsv(getPhoneNumber()),
                escapeCsv(getEmail()),
                escapeCsv(workplaceID),
                escapeCsv(workplaceType),
                escapeCsv(clinicianEmploymentStatus),
                escapeCsv(clinicianStartDate.toString())

        );
    }

    public static Clinician fromCSV(String csvLine) {
        String[] c = CSVHandler.parseCsvLine(csvLine); // IMPORTANT: not split(",")

        if (c.length != 12) {
            throw new IllegalArgumentException("Invalid clinician row: expected 12 columns, got " + c.length);
        }




        return new Clinician(
                c[0],  // clinicianID
                c[1],  // firstName
                c[2],  // lastName
                c[3],  // title
                c[4],  // speciality
                c[5],  // gmcNumber
                c[6],  // phoneNumber
                c[7],  // email
                c[8],  // workplaceID
                c[9],  // workplaceType
                c[10], // employmentStatus
                LocalDate.parse(c[11].trim())  // startDate
        );
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";

        boolean needsQuotes = value.contains(",");

        String escapedValue = value.replace("\"", "\"\"");

        if (needsQuotes) {
            return "\"" + escapedValue + "\"";
        } else {
            return escapedValue;
        }
    }


    @Override
    public String toString() {
        return "Clinician{" +
                "clinicianID='" + clinicianID + '\'' +
                ", title='" + title + '\'' +
                ", speciality='" + speciality + '\'' +
                ", gmcNumber='" + gmcNumber + '\'' +
                ", workplaceID='" + workplaceID + '\'' +
                ", workplaceType='" + workplaceType + '\'' +
                ", clinicianEmploymentStatus='" + clinicianEmploymentStatus + '\'' +
                ", clinicianStartDate='" + clinicianStartDate + '\'' +
                '}';
    }
}

