public class Facility {

private String facilityID;
private String faciltiyType;
private String facilityAddress;
private String facilityPostcode;
private String facilityPhoneNumber;
private String openingTime;
private String managerName;
private int capacity;
private String specialitiesOffered;

    public Facility(String specialitiesOffered, int capacity, String managerName, String openingTime,
                    String facilityPhoneNumber, String facilityPostcode, String facilityAddress,
                    String faciltiyType, String facilityID) {
        this.specialitiesOffered = specialitiesOffered;
        this.capacity = capacity;
        this.managerName = managerName;
        this.openingTime = openingTime;
        this.facilityPhoneNumber = facilityPhoneNumber;
        this.facilityPostcode = facilityPostcode;
        this.facilityAddress = facilityAddress;
        this.faciltiyType = faciltiyType;
        this.facilityID = facilityID;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getFaciltiyType() {
        return faciltiyType;
    }

    public void setFaciltiyType(String faciltiyType) {
        this.faciltiyType = faciltiyType;
    }

    public String getFacilityAddress() {
        return facilityAddress;
    }

    public void setFacilityAddress(String facilityAddress) {
        this.facilityAddress = facilityAddress;
    }

    public String getFacilityPostcode() {
        return facilityPostcode;
    }

    public void setFacilityPostcode(String facilityPostcode) {
        this.facilityPostcode = facilityPostcode;
    }

    public String getFacilityPhoneNumber() {
        return facilityPhoneNumber;
    }

    public void setFacilityPhoneNumber(String facilityPhoneNumber) {
        this.facilityPhoneNumber = facilityPhoneNumber;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getSpecialitiesOffered() {
        return specialitiesOffered;
    }

    public void setSpecialitiesOffered(String specialitiesOffered) {
        this.specialitiesOffered = specialitiesOffered;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "facilityID='" + facilityID + '\'' +
                ", faciltiyType='" + faciltiyType + '\'' +
                ", facilityAddress='" + facilityAddress + '\'' +
                ", facilityPostcode='" + facilityPostcode + '\'' +
                ", facilityPhoneNumber='" + facilityPhoneNumber + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", managerName='" + managerName + '\'' +
                ", capacity=" + capacity +
                ", specialitiesOffered='" + specialitiesOffered + '\'' +
                '}';
    }
}
