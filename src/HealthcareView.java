import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthcareView extends JFrame {

    private final HealthcareController controller;

    private JTextField nhsField, dateField, timeField;
    private JComboBox<AppointmentType> typeBox;
    private JTextField reasonField, notesField;

    private JTextField apptIdField, newDateField, newTimeField;
    private JLabel statusLabel;
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsTableModel;
    private JTable patientsTable;
    private javax.swing.table.DefaultTableModel patientsTableModel;
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
    private JTable cliniciansTable;
    private javax.swing.table.DefaultTableModel cliniciansTableModel;
    private JTable referralsTable;
    private javax.swing.table.DefaultTableModel referralsTableModel;
    private JTable prescriptionsTable;
    private javax.swing.table.DefaultTableModel prescriptionsTableModel;
    private JComboBox<String> ehrPatientBox;
    private JTextArea ehrTextArea;






    public HealthcareView(HealthcareController controller) {
        this.controller = controller;

        setTitle("Healthcare Management System");
        setSize(800, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);

        tabs.add("Patients", buildPatientsTab());
        tabs.add("Appointments", buildAppointmentsTab());
        tabs.add("Clinicians", buildCliniciansTab());
        tabs.add("Referrals", buildReferralsTab());
        tabs.add("Prescriptions", buildPrescriptionsTab());
        tabs.add("EHR", buildEHRTab());


        add(tabs, BorderLayout.CENTER);


    }

    private String fmtDate(Date d) {
        return (d == null) ? "" : DATE_FMT.format(d);
    }

    //------------------------- Appointments ------------------------------

    private JPanel buildAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        // ----- Table -----
        String[] cols = {
                "Appointment ID", "Patient ID", "Clinician ID", "Facility ID",
                "Date", "Time", "Duration", "Type", "Status",
                "Reason", "Notes", "Created", "Last Modified"
        };

        appointmentsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentsTable = new JTable(appointmentsTableModel);
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ----- Buttons -----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addBtn = new JButton("Add Appointment");
        JButton cancelBtn = new JButton("Cancel Appointment");
        JButton modifyBtn = new JButton("Modify Appointment");

        bottom.add(addBtn);
        bottom.add(cancelBtn);
        bottom.add(modifyBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener(e -> handleAddAppointment());
        cancelBtn.addActionListener(e -> handleCancelAppointment());
        modifyBtn.addActionListener(e -> handleModifyAppointment());

        // Load table data initially
        refreshAppointmentsTable();

        return panel;
    }


    private void refreshAppointmentsTable() {
        // Reload from CSV
        controller.reloadAppointments();

        // Clear table
        appointmentsTableModel.setRowCount(0);

        ArrayList<Appointment> all = controller.getAllAppointments();
        for (Appointment a : all) {
            appointmentsTableModel.addRow(new Object[]{
                    a.getAppointmentID(),
                    a.getPatientID(),
                    a.getClinicianID(),
                    a.getFacilityID(),
                    a.getAppointmentDate().toString(),
                    a.getAppointmentTime().toString(),
                    a.getDurationMinutes(),
                    a.getType().toString(),
                    a.getStatus().toString(),
                    a.getReasonForVisit(),
                    a.getAppointmentNotes(),
                    a.getAppointmentCreated().toString(),
                    a.getAppointmentModified().toString()
            });
        }
    }


    private void handleAddAppointment() {
        try {
            // Build form panel
            JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));

            JTextField nhsField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField timeField = new JTextField();
            JComboBox<AppointmentType> typeBox = new JComboBox<>(AppointmentType.values());
            JTextField reasonField = new JTextField();
            JTextField notesField = new JTextField();

            form.add(new JLabel("NHS Number:"));
            form.add(nhsField);

            form.add(new JLabel("Date (YYYY-MM-DD):"));
            form.add(dateField);

            form.add(new JLabel("Time (HH:MM):"));
            form.add(timeField);

            form.add(new JLabel("Appointment Type:"));
            form.add(typeBox);

            form.add(new JLabel("Reason for Visit:"));
            form.add(reasonField);

            form.add(new JLabel("Notes:"));
            form.add(notesField);

            // Show dialog
            int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Add Appointment",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return; // user cancelled
            }

            // Book appointment via controller
            Appointment booked = controller.bookAppointment(
                    nhsField.getText(),
                    dateField.getText(),
                    timeField.getText(),
                    (AppointmentType) typeBox.getSelectedItem(),
                    reasonField.getText(),
                    notesField.getText()
            );

            statusLabel.setText("Booked appointment: " + booked.getAppointmentID());
            refreshAppointmentsTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void handleCancelAppointment() {
        try {
            int row = appointmentsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment in the table first.");
                return;
            }

            String appointmentID = appointmentsTableModel.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Cancel appointment " + appointmentID + "?",
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            controller.cancelAppointment(appointmentID);

            statusLabel.setText("Cancelled appointment: " + appointmentID);
            refreshAppointmentsTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void handleModifyAppointment() {
        try {
            int row = appointmentsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment in the table first.");
                return;
            }

            String appointmentID = appointmentsTableModel.getValueAt(row, 0).toString();
            String currentDate = appointmentsTableModel.getValueAt(row, 4).toString();
            String currentTime = appointmentsTableModel.getValueAt(row, 5).toString();

            String newDate = JOptionPane.showInputDialog(this, "New Date (YYYY-MM-DD):", currentDate);
            if (newDate == null || newDate.trim().isEmpty()) return;

            String newTime = JOptionPane.showInputDialog(this, "New Time (HH:MM):", currentTime);
            if (newTime == null || newTime.trim().isEmpty()) return;

            controller.rescheduleAppointment(appointmentID, newDate, newTime);

            statusLabel.setText("Modified appointment: " + appointmentID);
            refreshAppointmentsTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //----------------------------- Patients ---------------------------

    private JPanel buildPatientsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        // ---- Table columns ----
        String[] cols = {
                "Patient ID", "First Name", "Last Name", "NHS Number",
                "DOB", "Gender", "Phone", "Email",
                "Address", "Postcode",
                "Emergency Name", "Emergency Phone",
                "Register Date", "GP Surgery ID"
        };

        patientsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        patientsTable = new JTable(patientsTableModel);
        patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(patientsTable), BorderLayout.CENTER);

        // ---- Buttons at bottom ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");
        JButton modifyBtn = new JButton("Modify");

        bottom.add(addBtn);
        bottom.add(removeBtn);
        bottom.add(modifyBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        // ---- Actions ----
        addBtn.addActionListener(e -> handleAddPatient());
        removeBtn.addActionListener(e -> handleRemovePatient());
        modifyBtn.addActionListener(e -> handleModifyPatient());

        // initial load
        refreshPatientsTable();

        return panel;
    }

    private void refreshPatientsTable() {
        controller.reloadPatients();
        patientsTableModel.setRowCount(0);

        for (Patient p : controller.getAllPatients()) {
            patientsTableModel.addRow(patientRow(p));
        }
    }

    private Object[] patientRow(Patient p) {
        return new Object[]{
                p.getPatientID(),
                p.getFirstName(),
                p.getLastName(),
                p.getNhsNumber(),
                fmtDate(p.getDateOfBirth()),
                p.getGender().name(),
                p.getPhoneNumber(),
                p.getEmail(),
                p.getAddress(),
                p.getPostcode(),
                p.getEmergencyContactName(),
                p.getEmergencyContactPhone(),
                fmtDate(p.getRegisterDate()),
                p.getGpSurgeryID()
        };
    }



    private void handleRemovePatient() {
        int row = patientsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return;
        }

        String patientID = patientsTableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove patient " + patientID + "?\n(This will delete the record from patients.csv)",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.removePatient(patientID);
            statusLabel.setText("Removed patient: " + patientID);
            refreshPatientsTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleModifyPatient() {
        int row = patientsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return;
        }

        String patientID = patientsTableModel.getValueAt(row, 0).toString();

        Patient p = controller.getPatientByID(patientID);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Could not load patient: " + patientID);
            return;
        }

        // Build dialog panel
        JPanel form = new JPanel(new GridLayout(8, 2, 6, 6));

        JTextField firstNameField = new JTextField(p.getFirstName());
        JTextField lastNameField = new JTextField(p.getLastName());
        JTextField phoneField = new JTextField(p.getPhoneNumber());
        JTextField emailField = new JTextField(p.getEmail());
        JTextField addressField = new JTextField(p.getAddress());
        JTextField postcodeField = new JTextField(p.getPostcode());
        JTextField emergencyNameField = new JTextField(p.getEmergencyContactName());
        JTextField emergencyPhoneField = new JTextField(p.getEmergencyContactPhone());

        form.add(new JLabel("First Name:")); form.add(firstNameField);
        form.add(new JLabel("Last Name:")); form.add(lastNameField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Address:")); form.add(addressField);
        form.add(new JLabel("Postcode:")); form.add(postcodeField);
        form.add(new JLabel("Emergency Name:")); form.add(emergencyNameField);
        form.add(new JLabel("Emergency Phone:")); form.add(emergencyPhoneField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Modify Patient " + patientID,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            controller.updatePatientDetails(
                    patientID,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    postcodeField.getText(),
                    emergencyNameField.getText(),
                    emergencyPhoneField.getText()
            );

            statusLabel.setText("Updated patient: " + patientID);
            refreshPatientsTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleAddPatient() {
        try {
            JPanel form = new JPanel(new GridLayout(12, 2, 6, 6));

            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField dobField = new JTextField();
            JTextField nhsField = new JTextField();
            JComboBox<PatientGender> genderBox = new JComboBox<>(PatientGender.values());
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField postcodeField = new JTextField();
            JTextField emergencyNameField = new JTextField();
            JTextField emergencyPhoneField = new JTextField();
            JTextField gpSurgeryField = new JTextField();

            form.add(new JLabel("First Name:")); form.add(firstNameField);
            form.add(new JLabel("Last Name:")); form.add(lastNameField);
            form.add(new JLabel("Date of Birth (YYYY-MM-DD):")); form.add(dobField);
            form.add(new JLabel("NHS Number:")); form.add(nhsField);
            form.add(new JLabel("Gender:")); form.add(genderBox);
            form.add(new JLabel("Phone Number:")); form.add(phoneField);
            form.add(new JLabel("Email:")); form.add(emailField);
            form.add(new JLabel("Address:")); form.add(addressField);
            form.add(new JLabel("Postcode:")); form.add(postcodeField);
            form.add(new JLabel("Emergency Contact Name:")); form.add(emergencyNameField);
            form.add(new JLabel("Emergency Contact Phone:")); form.add(emergencyPhoneField);
            form.add(new JLabel("GP Surgery ID:")); form.add(gpSurgeryField);


            int result = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Add Patient",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            Patient created = controller.addPatientFromForm(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dobField.getText(),
                    nhsField.getText(),
                    genderBox.getSelectedItem().toString(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    postcodeField.getText(),
                    emergencyNameField.getText(),
                    emergencyPhoneField.getText(),
                    gpSurgeryField.getText()
            );

            statusLabel.setText("Added patient: " + created.getPatientID());
            refreshPatientsTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    //----------------------------- Clinicians -------------------------------------------

    private JPanel buildCliniciansTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        String[] cols = {
                "Clinician ID", "First Name", "Last Name",
                "Title", "Speciality", "GMC Number",
                "Phone", "Email",
                "Workplace ID", "Workplace Type",
                "Employment Status", "Start Date"
        };

        cliniciansTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        cliniciansTable = new JTable(cliniciansTableModel);
        cliniciansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(cliniciansTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");
        JButton modifyBtn = new JButton("Modify");
        JButton referralBtn = new JButton("Make Referral");

        bottom.add(addBtn);
        bottom.add(removeBtn);
        bottom.add(modifyBtn);
        bottom.add(referralBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAddClinician());
        removeBtn.addActionListener(e -> handleRemoveClinician());
        modifyBtn.addActionListener(e -> handleModifyClinician());
        referralBtn.addActionListener(e -> handleMakeReferralFromClinician());

        refreshCliniciansTable();

        return panel;
    }

    private void refreshCliniciansTable() {
        controller.reloadClinicians();
        cliniciansTableModel.setRowCount(0);

        for (Clinician c : controller.getAllClinicians()) {
            cliniciansTableModel.addRow(clinicianRow(c));
        }

    }

    private Object[] clinicianRow(Clinician c) {
        return new Object[]{
                c.getClinicianID(),
                c.getFirstName(),
                c.getLastName(),
                c.getTitle(),
                c.getSpeciality(),
                c.getGmcNumber(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getWorkplaceID(),
                c.getWorkplaceType(),
                c.getClinicianEmploymentStatus(),
                c.getClinicianStartDate()
        };
    }

    private void handleAddClinician() {
        JPanel form = new JPanel(new GridLayout(11, 2, 6, 6));

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField specialityField = new JTextField();
        JTextField gmcField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField workplaceIdField = new JTextField();
        JTextField workplaceTypeField = new JTextField();
        JTextField employmentField = new JTextField();
        JTextField startDateField = new JTextField();

        form.add(new JLabel("First Name:")); form.add(firstNameField);
        form.add(new JLabel("Last Name:")); form.add(lastNameField);
        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Speciality:")); form.add(specialityField);
        form.add(new JLabel("GMC Number:")); form.add(gmcField);
        form.add(new JLabel("Phone Number:")); form.add(phoneField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Workplace ID:")); form.add(workplaceIdField);
        form.add(new JLabel("Workplace Type:")); form.add(workplaceTypeField);
        form.add(new JLabel("Employment Status:")); form.add(employmentField);
        form.add(new JLabel("Start Date (YYYY-MM-DD):")); form.add(startDateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add Clinician",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
            controller.addClinicianFromForm(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    titleField.getText(),
                    specialityField.getText(),
                    gmcField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    workplaceIdField.getText(),
                    workplaceTypeField.getText(),
                    employmentField.getText(),
                    startDate
            );

            statusLabel.setText("Added clinician.");
            refreshCliniciansTable();

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            JOptionPane.showMessageDialog(this,
                    "Start Date must be in YYYY-MM-DD format",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
        }


    }


    private void handleRemoveClinician() {
        int row = cliniciansTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a clinician first.");
            return;
        }

        String clinicianID = cliniciansTableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove clinician " + clinicianID + "?\n(This will delete the record from clinicians.csv)",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.removeClinician(clinicianID);
            statusLabel.setText("Removed clinician: " + clinicianID);
            refreshCliniciansTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleModifyClinician() {
        int row = cliniciansTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a clinician first.");
            return;
        }

        String clinicianID = cliniciansTableModel.getValueAt(row, 0).toString();
        Clinician c = controller.getClinicianByID(clinicianID);

        if (c == null) {
            JOptionPane.showMessageDialog(this, "Could not load clinician: " + clinicianID);
            return;
        }

        JPanel form = new JPanel(new GridLayout(11, 2, 6, 6));

        JTextField firstNameField = new JTextField(c.getFirstName());
        JTextField lastNameField = new JTextField(c.getLastName());
        JTextField titleField = new JTextField(c.getTitle());
        JTextField specialityField = new JTextField(c.getSpeciality());
        JTextField gmcField = new JTextField(c.getGmcNumber());
        JTextField phoneField = new JTextField(c.getPhoneNumber());
        JTextField emailField = new JTextField(c.getEmail());
        JTextField workplaceIdField = new JTextField(c.getWorkplaceID());
        JTextField workplaceTypeField = new JTextField(c.getWorkplaceType());
        JTextField employmentField = new JTextField(c.getClinicianEmploymentStatus());
        JTextField startDateField = new JTextField(c.getClinicianStartDate().toString());

        form.add(new JLabel("First Name:")); form.add(firstNameField);
        form.add(new JLabel("Last Name:")); form.add(lastNameField);
        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Speciality:")); form.add(specialityField);
        form.add(new JLabel("GMC Number:")); form.add(gmcField);
        form.add(new JLabel("Phone Number:")); form.add(phoneField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Workplace ID:")); form.add(workplaceIdField);
        form.add(new JLabel("Workplace Type:")); form.add(workplaceTypeField);
        form.add(new JLabel("Employment Status:")); form.add(employmentField);
        form.add(new JLabel("Start Date (YYYY-MM-DD):")); form.add(startDateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Modify Clinician " + clinicianID,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            controller.updateClinicianDetails(
                    clinicianID,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    titleField.getText(),
                    specialityField.getText(),
                    gmcField.getText(),
                    workplaceIdField.getText(),
                    workplaceTypeField.getText(),
                    employmentField.getText(),
                    LocalDate.parse(startDateField.getText().trim())
            );

            statusLabel.setText("Updated clinician: " + clinicianID);
            refreshCliniciansTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Start Date must be YYYY-MM-DD",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    //------------------------------------ Referrals ----------------------------------

    private void handleMakeReferralFromClinician() {
        int row = cliniciansTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a clinician first.");
            return;
        }

        String fromClinicianID = cliniciansTableModel.getValueAt(row, 0).toString();
        Clinician fromClinician = controller.getClinicianByID(fromClinicianID);

        if (fromClinician == null) {
            JOptionPane.showMessageDialog(this, "Could not load selected clinician: " + fromClinicianID);
            return;
        }

        if (fromClinician.getWorkplaceType() == null ||
                !fromClinician.getWorkplaceType().equalsIgnoreCase("GP Surgery")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only clinicians working at a 'GP Surgery' can make referrals.\n" +
                            "Selected clinician workplace: " + fromClinician.getWorkplaceType(),
                    "Not Allowed",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Targets: only Hospital clinicians
        ArrayList<Clinician> hospitalClinicians = controller.getCliniciansByWorkplaceType("Hospital");
        if (hospitalClinicians.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hospital clinicians available to refer to.");
            return;
        }

        // Patients list
        ArrayList<Patient> patients = controller.getAllPatients();
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients loaded. Load patients first.");
            return;
        }

        // Build dropdowns with readable labels
        JComboBox<String> patientBox = new JComboBox<>();
        for (Patient p : patients) {
            patientBox.addItem(p.getPatientID() + " - " + p.getFirstName() + " " + p.getLastName());
        }

        JComboBox<String> toClinicianBox = new JComboBox<>();
        for (Clinician c : hospitalClinicians) {
            toClinicianBox.addItem(
                    c.getClinicianID() + " - " + c.getTitle() + " " + c.getFirstName() + " " + c.getLastName()
                            + " (" + c.getWorkplaceID() + ")"
            );
        }

        JComboBox<String> urgencyBox = new JComboBox<>(new String[]{"Routine", "Urgent", "Non-Urgent"});
        JTextField referralDateField = new JTextField(LocalDate.now().toString()); // yyyy-MM-dd

        JTextField reasonField = new JTextField();
        JTextArea summaryArea = new JTextArea(4, 20);
        JTextArea investigationsArea = new JTextArea(3, 20);
        JTextArea notesArea = new JTextArea(3, 20);

        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        investigationsArea.setLineWrap(true);
        investigationsArea.setWrapStyleWord(true);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        form.add(new JLabel("Referring Clinician:"), gbc);
        gbc.gridx = 1;
        form.add(new JLabel(fromClinicianID + " (" + fromClinician.getWorkplaceID() + ")"), gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        form.add(patientBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Referred To (Hospital Clinician):"), gbc);
        gbc.gridx = 1;
        form.add(toClinicianBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Referral Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        form.add(referralDateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Urgency Level:"), gbc);
        gbc.gridx = 1;
        form.add(urgencyBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Referral Reason:"), gbc);
        gbc.gridx = 1;
        form.add(reasonField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Clinical Summary:"), gbc);
        gbc.gridx = 1;
        form.add(new JScrollPane(summaryArea), gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Requested Investigations:"), gbc);
        gbc.gridx = 1;
        form.add(new JScrollPane(investigationsArea), gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        form.add(new JScrollPane(notesArea), gbc);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Create Referral",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        // Extract selected patientID from label
        String patientLabel = (String) patientBox.getSelectedItem();
        String patientID = patientLabel.split(" - ")[0].trim();

        // Extract toClinicianID from label
        String toClinicianLabel = (String) toClinicianBox.getSelectedItem();
        String toClinicianID = toClinicianLabel.split(" - ")[0].trim();

        // Find the selected hospital clinician object to get toFacilityID
        Clinician toClinician = null;
        for (Clinician c : hospitalClinicians) {
            if (c.getClinicianID().equals(toClinicianID)) {
                toClinician = c;
                break;
            }
        }
        if (toClinician == null) {
            JOptionPane.showMessageDialog(this, "Could not resolve selected hospital clinician.");
            return;
        }

        // From / To facilities
        String fromFacilityID = fromClinician.getWorkplaceID();
        String toFacilityID = toClinician.getWorkplaceID();

        // Parse date
        LocalDate referralDate;
        try {
            referralDate = LocalDate.parse(referralDateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Referral date must be in YYYY-MM-DD format.");
            return;
        }

        try {
            Referral created = controller.createReferral(
                    patientID,
                    fromClinicianID,
                    toClinicianID,
                    fromFacilityID,
                    toFacilityID,
                    referralDate,
                    (String) urgencyBox.getSelectedItem(),
                    reasonField.getText(),
                    summaryArea.getText(),
                    investigationsArea.getText(),
                    notesArea.getText()
            );

            statusLabel.setText("Created referral " + created.getReferralID() + " (text file generated).");
            JOptionPane.showMessageDialog(this,
                    "Referral created: " + created.getReferralID() + "\nA referral text file has been generated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildReferralsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        String[] cols = {
                "Referral ID", "Patient ID",
                "From Clinician", "To Clinician",
                "From Facility", "To Facility",
                "Referral Date", "Urgency",
                "Reason", "Clinical Summary",
                "Investigations", "Status",
                "Appointment ID", "Notes",
                "Created", "Last Updated"
        };

        referralsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        referralsTable = new JTable(referralsTableModel);
        referralsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(referralsTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton removeBtn = new JButton("Remove Referral");
        JButton modifyStatusBtn = new JButton("Modify Status");

        bottom.add(removeBtn);
        bottom.add(modifyStatusBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        removeBtn.addActionListener(e -> handleRemoveReferral());
        modifyStatusBtn.addActionListener(e -> handleModifyReferralStatus());

        refreshReferralsTable();

        return panel;
    }

    private void refreshReferralsTable() {
        controller.reloadReferrals();
        referralsTableModel.setRowCount(0);

        for (Referral r : controller.getAllReferrals()) {
            referralsTableModel.addRow(referralRow(r));
        }
    }

    private Object[] referralRow(Referral r) {
        return new Object[]{
                r.getReferralID(),
                r.getPatientID(),
                r.getFromClinician(),
                r.getToClinician(),
                r.getFromFacility(),
                r.getToFacility(),
                r.getReferralDate(),
                r.getUrgencyLevel(),
                r.getReasonForReferral(),
                r.getClinicalSummary(),
                r.getRequestedInvestigations(),
                r.getReferralStatus(),
                r.getAppointmentID(),
                r.getReferralNotes(),
                r.getReferralCreated(),
                r.getReferralLastUpdated()
        };
    }


    private void handleRemoveReferral() {
        int row = referralsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a referral first.");
            return;
        }

        String referralID = referralsTableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete referral " + referralID + "?\n(This will remove it from referrals.csv)",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.deleteReferral(referralID);
            statusLabel.setText("Deleted referral: " + referralID);
            refreshReferralsTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifyReferralStatus() {
        int row = referralsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a referral first.");
            return;
        }

        String referralID = referralsTableModel.getValueAt(row, 0).toString();

        JComboBox<ReferralStatus> statusBox = new JComboBox<>(ReferralStatus.values());

        int result = JOptionPane.showConfirmDialog(
                this,
                statusBox,
                "Set status for " + referralID,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        ReferralStatus newStatus = (ReferralStatus) statusBox.getSelectedItem();

        try {
            controller.updateReferralStatus(referralID, newStatus);
            statusLabel.setText("Updated referral " + referralID + " to " + newStatus);
            refreshReferralsTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//------------------------------- Prescriptions ------------------------------------

    private JPanel buildPrescriptionsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        String[] cols = {
                "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                "Prescription Date", "Medication", "Dosage", "Frequency",
                "Duration Days", "Quantity", "Instructions", "Pharmacy",
                "Status", "Issue Date", "Collection Date"
        };

        prescriptionsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        prescriptionsTable = new JTable(prescriptionsTableModel);
        prescriptionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(prescriptionsTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");
        JButton modifyBtn = new JButton("Modify");

        bottom.add(addBtn);
        bottom.add(removeBtn);
        bottom.add(modifyBtn);

        panel.add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAddPrescription());
        removeBtn.addActionListener(e -> handleRemovePrescription());
        modifyBtn.addActionListener(e -> handleModifyPrescription());

        refreshPrescriptionsTable();

        return panel;
    }

    private void refreshPrescriptionsTable() {
        controller.reloadPrescriptions();
        prescriptionsTableModel.setRowCount(0);

        for (Prescription p : controller.getAllPrescriptions()) {
            prescriptionsTableModel.addRow(prescriptionRow(p));
        }
    }

    private Object[] prescriptionRow(Prescription p) {
        return new Object[]{
                p.getPrescriptionID(),
                p.getPatientID(),
                p.getClinicianID(),
                p.getAppointmentID(),
                p.getPrescriptionDate(),
                p.getMedicationName(),
                p.getDosage(),
                p.getFrequency(),
                p.getDurationDays(),
                p.getQuantity(),
                p.getInstructions(),
                p.getPharmacyName(),
                p.getStatus(),
                p.getIssueDate(),
                p.getCollectionDate()
        };
    }

    private void handleRemovePrescription() {
        int row = prescriptionsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }

        String id = prescriptionsTableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete prescription " + id + "?\n(This will remove it from prescriptions.csv)",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.deletePrescription(id);
            statusLabel.setText("Deleted prescription: " + id);
            refreshPrescriptionsTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifyPrescription() {
        int row = prescriptionsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }

        String id = prescriptionsTableModel.getValueAt(row, 0).toString();
        String currentStatus = prescriptionsTableModel.getValueAt(row, 12).toString();

        // Only allow Issued to Collected
        if ("Collected".equalsIgnoreCase(currentStatus.trim())) {
            JOptionPane.showMessageDialog(this, "This prescription is already marked as Collected.");
            return;
        }

        JTextField dateField = new JTextField(LocalDate.now().toString());

        JPanel form = new JPanel(new GridLayout(1, 2, 6, 6));
        form.add(new JLabel("Collection Date (YYYY-MM-DD):"));
        form.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Mark Prescription Collected (" + id + ")",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        LocalDate collectionDate;
        try {
            collectionDate = LocalDate.parse(dateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date. Use YYYY-MM-DD.");
            return;
        }

        try {
            controller.markPrescriptionCollected(id, collectionDate);
            statusLabel.setText("Prescription " + id + " marked Collected.");
            refreshPrescriptionsTable();
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddPrescription() {
        // Load patients + clinicians for dropdowns
        ArrayList<Patient> patients = controller.getAllPatients();
        ArrayList<Clinician> clinicians = controller.getAllClinicians();

        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients loaded.");
            return;
        }
        if (clinicians.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clinicians loaded.");
            return;
        }

        JComboBox<String> patientBox = new JComboBox<>();
        for (Patient p : patients) {
            patientBox.addItem(p.getPatientID() + " - " + p.getFirstName() + " " + p.getLastName());
        }

        JComboBox<String> clinicianBox = new JComboBox<>();
        for (Clinician c : clinicians) {
            clinicianBox.addItem(c.getClinicianID() + " - " + c.getTitle() + " " + c.getFirstName() + " " + c.getLastName());
        }

        JTextField appointmentIdField = new JTextField();
        JTextField prescriptionDateField = new JTextField(LocalDate.now().toString());

        JTextField medicationField = new JTextField();
        JTextField dosageField = new JTextField();
        JTextField frequencyField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextArea instructionsArea = new JTextArea(3, 20);
        JTextField pharmacyField = new JTextField();

        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        form.add(new JLabel("Patient:"), gbc); gbc.gridx = 1; form.add(patientBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Clinician:"), gbc); gbc.gridx = 1; form.add(clinicianBox, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Appointment ID (optional):"), gbc); gbc.gridx = 1; form.add(appointmentIdField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Prescription Date (YYYY-MM-DD):"), gbc); gbc.gridx = 1; form.add(prescriptionDateField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Medication Name:"), gbc); gbc.gridx = 1; form.add(medicationField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Dosage:"), gbc); gbc.gridx = 1; form.add(dosageField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Frequency:"), gbc); gbc.gridx = 1; form.add(frequencyField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Duration Days:"), gbc); gbc.gridx = 1; form.add(durationField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Quantity:"), gbc); gbc.gridx = 1; form.add(quantityField, gbc);
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Instructions:"), gbc); gbc.gridx = 1; form.add(new JScrollPane(instructionsArea), gbc);
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(new JLabel("Pharmacy Name:"), gbc); gbc.gridx = 1; form.add(pharmacyField, gbc);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Add Prescription", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String patientID = ((String) patientBox.getSelectedItem()).split(" - ")[0].trim();
        String clinicianID = ((String) clinicianBox.getSelectedItem()).split(" - ")[0].trim();
        String appointmentID = appointmentIdField.getText().trim();

        // --- Validation ---
        if (medicationField.getText().trim().isEmpty()
                || dosageField.getText().trim().isEmpty()
                || frequencyField.getText().trim().isEmpty()
                || durationField.getText().trim().isEmpty()
                || quantityField.getText().trim().isEmpty()
                || instructionsArea.getText().trim().isEmpty()
                || pharmacyField.getText().trim().isEmpty()
                || prescriptionDateField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields must be filled except Appointment ID.",
                    "Missing Information",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        LocalDate pDate;
        try {
            pDate = LocalDate.parse(prescriptionDateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Prescription date must be YYYY-MM-DD.");
            return;
        }

        int durationDays;
        try {
            durationDays = Integer.parseInt(durationField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Duration Days must be a number.");
            return;
        }

        try {
            Prescription created = controller.createPrescription(
                    patientID,
                    clinicianID,
                    appointmentID,
                    pDate,
                    medicationField.getText(),
                    dosageField.getText(),
                    frequencyField.getText(),
                    durationDays,
                    quantityField.getText(),
                    instructionsArea.getText(),
                    pharmacyField.getText()
            );

            statusLabel.setText("Created prescription " + created.getPrescriptionID() + " (Issued).");
            refreshPrescriptionsTable();

            JOptionPane.showMessageDialog(this,
                    "Prescription created: " + created.getPrescriptionID() + "\nA prescription text file has been generated.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//----------------------------- Electronic Health Record ------------------------------------

    private JPanel buildEHRTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        // Top: patient selector + view button
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.add(new JLabel("Select patient:"));

        ehrPatientBox = new JComboBox<>();
        JButton viewBtn = new JButton("View EHR");

        top.add(ehrPatientBox);
        top.add(viewBtn);

        panel.add(top, BorderLayout.NORTH);

        // Centre: big text area
        ehrTextArea = new JTextArea();
        ehrTextArea.setEditable(false);
        ehrTextArea.setLineWrap(true);
        ehrTextArea.setWrapStyleWord(true);
        ehrTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(new JScrollPane(ehrTextArea), BorderLayout.CENTER);

        // Load patients into dropdown
        refreshEHRPatientDropdown();

        // Button action
        viewBtn.addActionListener(e -> handleViewEHR());

        return panel;
    }


    private void refreshEHRPatientDropdown() {
        ehrPatientBox.removeAllItems();

        // Ensure patients are loaded
        controller.reloadPatients();
        ArrayList<Patient> patients = controller.getAllPatients();

        for (Patient p : patients) {
            ehrPatientBox.addItem(p.getPatientID() + " - " + p.getFirstName() + " " + p.getLastName());
        }

        if (patients.isEmpty()) {
            ehrPatientBox.addItem("(No patients loaded)");
        }
    }


    private void handleViewEHR() {
        String selected = (String) ehrPatientBox.getSelectedItem();
        if (selected == null || selected.startsWith("(No patients")) {
            JOptionPane.showMessageDialog(this, "No patient available.");
            return;
        }

        String patientID = selected.split(" - ")[0].trim();

        try {
            String ehrText = controller.buildEHRForPatient(patientID);
            ehrTextArea.setText(ehrText);
            ehrTextArea.setCaretPosition(0);
            statusLabel.setText("Displayed EHR for " + patientID);
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



}
