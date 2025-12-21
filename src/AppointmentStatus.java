public enum AppointmentStatus {

    SCHEDULED("Scheduled"), CANCELLED("Cancelled");
    private String status;

    AppointmentStatus(String status) {

        this.status = status;
    }

    public static AppointmentStatus fromString(String text) {
        for (AppointmentStatus label : AppointmentStatus.values()) {
            if (label.status.equalsIgnoreCase(text.trim())) {
                return label;
            }
        }
        throw new IllegalArgumentException("Unknown AppointmentType: " + text);
    }

    @Override
    public String toString() {
        return status;
    }
}
