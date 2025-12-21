public enum AppointmentStatus {

    SCHEDULED(" Scheduled"), CANCELLED(" Cancelled");
    private String staus;

    AppointmentStatus(String staus) {
        this.staus = staus;
    }

    @Override
    public String toString() {
        return "AppointmentStaus{" +
                "staus='" + staus + '\'' +
                '}';
    }
}
