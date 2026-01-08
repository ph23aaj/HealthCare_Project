public class HealthcareModel {

    private final PatientManager patientManager;
    private final ClinicianManager clinicianManager;
    private final AppointmentManager appointmentManager;
    private final PrescriptionManager prescriptionManager;

    public HealthcareModel() {
        patientManager = new PatientManager("patients.csv");
        clinicianManager = new ClinicianManager("clinicians.csv");
        appointmentManager = new AppointmentManager("appointments.csv");
        prescriptionManager = new PrescriptionManager("prescriptions.csv");
    }

    public void loadAll() {
        patientManager.load();
        clinicianManager.load();
        appointmentManager.load();
        prescriptionManager.load();

    }

    public PatientManager getPatientManager() { return patientManager; }
    public ClinicianManager getClinicianManager() { return clinicianManager; }
    public AppointmentManager getAppointmentManager() { return appointmentManager; }
    public PrescriptionManager getPrescriptionManager() {return prescriptionManager;}

}
