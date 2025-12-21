import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private String appointmentID;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;
    private AppointmentType type;
    private AppointmentStaus status;
    private String reasonForVist;
    private String appointmentNotes;
    private LocalDate appointmentCreated;
    private LocalDate appointmentModified;

    public Appointment(String appointmentID, LocalDate appointmentDate, LocalTime appointmentTime,
                       int durationMinutes, AppointmentType type, AppointmentStaus status,
                       String reasonForVist, String appointmentNotes, LocalDate appointmentCreated,
                       LocalDate appointmentModified) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.status = status;
        this.reasonForVist = reasonForVist;
        this.appointmentNotes = appointmentNotes;
        this.appointmentCreated = appointmentCreated;
        this.appointmentModified = appointmentModified;
    }
}
