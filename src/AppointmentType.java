public enum AppointmentType {

    ROUTINE("Routine Consultation"), VACCINATION("Vaccination"),
    FOLLOWUP("Follow-up"), URGENT("Urgent Consultation"), SPECIALIST( "Specialist Consultation"),
    EMERGENCY("Emergency"), HEALTHCHECK("Health Check");
    private String type;

    private AppointmentType (String type){

        this.type = type;
    }

    public static AppointmentType fromString(String text) {
        for (AppointmentType label : AppointmentType.values()) {
            if (label.type.equalsIgnoreCase(text.trim())) {
                return label;
            }
        }
        throw new IllegalArgumentException("Unknown AppointmentType: " + text);
    }

    @Override
    public String toString() {
        return type;
    }
}
