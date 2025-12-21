public enum AppointmentType {

    ROUTINE(" Routine Consultation"), VACCINATION(" Vaccination"),
    FOLLOWUP(" Follow-up"), URGENT(" Urgent Consultation"), SPECIALIST( " Specialist Consultation"),
    EMERGENCY(" Emergency"), HEALTHCHECK(" Health Check");
    private String type;

    private AppointmentType (String ty){
        type = ty;
    }

    @Override
    public String toString() {
        return "AppointmentType{" +
                "type='" + type + '\'' +
                '}';
    }
}
