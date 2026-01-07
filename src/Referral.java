import java.time.LocalDate;

public class Referral {

    private String referralID;
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
    private String referralNotes;
    private LocalDate referralCreated;
    private LocalDate referralLastUpdated;

    public Referral(String referralID, String fromClinician, String toClinician, String fromFacility,
                    String toFacility, LocalDate referralDate, String urgencyLevel, String reasonForReferral,
                    String clinicalSummary, String requestedInvestigations, ReferralStatus referralStatus,
                    String referralNotes, LocalDate referralCreated, LocalDate referralLastUpdated) {
        this.referralID = referralID;
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

    @Override
    public String toString() {
        return "Referral{" +
                "referralID='" + referralID + '\'' +
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
                ", referralNotes='" + referralNotes + '\'' +
                ", referralCreated=" + referralCreated +
                ", referralLastUpdated=" + referralLastUpdated +
                '}';
    }
}
