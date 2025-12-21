public enum AppointmentStaus {

    SCHEDULED(" Scheduled"), CANCELLED(" Cancelled");
    private String staus;

    AppointmentStaus(String staus) {
        this.staus = staus;
    }

    @Override
    public String toString() {
        return "AppointmentStaus{" +
                "staus='" + staus + '\'' +
                '}';
    }
}
