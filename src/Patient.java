import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient extends People {

private String patientID;
private Date dateOfBirth;
private String nhsNumber;
private String gender;
private String address;
private String postcode;
private String emergencyContactName;
private String emergencyContactPhone;
private Date registerDate;
private String gpSurgeryID;

    public Patient(String patientID, String firstName, String lastName, Date dateOfBirth,
                   String nhsNumber, String gender, String phoneNumber, String email,
                   String address, String postcode, String emergencyContactName,
                   String emergencyContactPhone, Date registerDate, String gpSurgeryID) {
        super(firstName, lastName, phoneNumber, email);
        this.patientID = patientID;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.registerDate = registerDate;
        this.gpSurgeryID = gpSurgeryID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getGpSurgeryID() {
        return gpSurgeryID;
    }

    public void setGpSurgeryID(String gpSurgeryID) {
        this.gpSurgeryID = gpSurgeryID;
    }

    public String toCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return String.join(",",
                patientID,
                escapeCsv(getFirstName()),
                escapeCsv(getLastName()),
                sdf.format(dateOfBirth),
                nhsNumber,
                gender,
                escapeCsv(getPhoneNumber()),
                escapeCsv(getEmail()),
                escapeCsv(address),
                escapeCsv(postcode),
                escapeCsv(emergencyContactName),
                escapeCsv(emergencyContactPhone),
                sdf.format(registerDate),
                gpSurgeryID
        );
    }

    public static Patient fromCSV(String csvLine) {
        try {
            String[] c = CSVHandler.parseCsvLine(csvLine);

            if (c.length != 14) {
                throw new IllegalArgumentException("Invalid patient row: expected 14 columns, got " + c.length);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = sdf.parse(c[3]);
            Date reg = sdf.parse(c[12]);

            return new Patient(
                    c[0],  // patientID
                    c[1],  // firstName
                    c[2],  // lastName
                    dob,
                    c[4],  // nhsNumber
                    c[5],  // gender
                    c[6],  // phoneNumber
                    c[7],  // email
                    c[8],  // address
                    c[9],  // postcode
                    c[10], // emergencyContactName
                    c[11], // emergencyContactPhone
                    reg,
                    c[13]  // gpSurgeryID
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse patient CSV line: " + csvLine, e);
        }
    }



    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.contains(",");
        // Escape any double quotes inside the value
        String escapedValue = value.replace("\"", "\"\"");

        // Wrap in quotes only if required
        if (needsQuotes) {
            return "\"" + escapedValue + "\"";
        } else {
            return escapedValue;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID='" + patientID + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", nhsNumber='" + nhsNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", emergencyContactPhone='" + emergencyContactPhone + '\'' +
                ", registerDate=" + registerDate +
                ", gpSurgeryID='" + gpSurgeryID + '\'' +
                '}';
    }
}
