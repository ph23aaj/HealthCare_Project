import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class HealthcareController {

    private final HealthcareModel model;

    public HealthcareController(HealthcareModel model) {
        this.model = model;
    }

    // ---------------------- Appointments --------------------------------

    public Appointment bookAppointment(String nhs, String dateText, String timeText,
                                       AppointmentType type, String reason, String notes) {
        LocalDate date = LocalDate.parse(dateText.trim());
        LocalTime time = LocalTime.parse(timeText.trim());

        return model.getAppointmentManager().bookAppointment(
                nhs.trim(),
                date,
                time,
                type,
                reason,
                notes,
                model.getPatientManager(),
                model.getClinicianManager()
        );
    }

    public ArrayList<Appointment> getAllAppointments() {
        return model.getAppointmentManager().getAllAppointments();
    }

    public void reloadAppointments() {
        model.getAppointmentManager().load();
    }

    public void cancelAppointment(String appointmentID) {
        model.getAppointmentManager().cancelAppointment(appointmentID.trim());
    }

    public void rescheduleAppointment(String appointmentID, String dateText, String timeText) {
        LocalDate date = LocalDate.parse(dateText.trim());
        LocalTime time = LocalTime.parse(timeText.trim());
        model.getAppointmentManager().rescheduleAppointment(appointmentID.trim(), date, time);
    }

    public void deleteAppointment(String appointmentID) {
        model.getAppointmentManager().deleteAppointment(appointmentID.trim());
    }

    public Appointment getAppointmentByID(String appointmentID) {
        return model.getAppointmentManager().getAppointmentByID(appointmentID.trim());
    }

// ------------------------- Patients ------------------------------------

    public ArrayList<Patient> getAllPatients() {
        return model.getPatientManager().getAllPatients();
    }

    public void reloadPatients() {
        model.getPatientManager().load();
    }

    public Patient getPatientByID(String patientID) {
        return model.getPatientManager().getPatientByID(patientID);
    }

    public void removePatient(String patientID) {
        model.getPatientManager().removePatient(patientID);
    }

    public void updatePatientDetails(
            String patientID,
            String firstName,
            String lastName,
            String phone,
            String email,
            String address,
            String postcode,
            String emergencyName,
            String emergencyPhone
    ) {
        model.getPatientManager().updatePatientDetails(
                patientID,
                firstName,
                lastName,
                phone,
                email,
                address,
                postcode,
                emergencyName,
                emergencyPhone
        );
    }

    public Patient addPatientFromForm(
            String firstName, String lastName, String dobText, String nhsNumber, String gender,
            String phone, String email, String address, String postcode,
            String emergencyName, String emergencyPhone,
            String gpSurgeryID
    ) {
        return model.getPatientManager().createAndAddPatient(
                firstName, lastName, dobText, nhsNumber, gender,
                phone, email, address, postcode,
                emergencyName, emergencyPhone,
                gpSurgeryID
        );
    }



}
