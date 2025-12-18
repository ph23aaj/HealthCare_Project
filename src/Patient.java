import java.util.Date;

public class Patient {

private String patientID;
private String dateOfBirth;
private String nhsNumber;
private String gender;
private String address;
private String postcode;
private String emergencyContactName;
private String emergencyContactPhone;
private Date registerDate;
private String gpSurgeryID;

    public Patient(String patientID, String dateOfBirth, String nhsNumber, String gender,
                   String address, String postcode, String emergencyContactName,
                   String emergencyContactPhone, Date registerDate, String gpSurgeryID) {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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
