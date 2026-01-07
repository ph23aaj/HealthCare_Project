public enum ReferralStatus {

    COMPLETED("Completed"), PENDING("Pending"),
    INPROGRESS("In Progress"), NEW("New");
    private String type;

    private ReferralStatus (String type){

        this.type = type;
    }

    public static ReferralStatus fromString(String text) {
        for (ReferralStatus label : ReferralStatus.values()) {
            if (label.type.equalsIgnoreCase(text.trim())) {
                return label;
            }
        }
        throw new IllegalArgumentException("Unknown Referral Status: " + text);
    }

    @Override
    public String toString() {
        return type;
    }

}
