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
                return false; // read-only table
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
        // Reload from CSV so the table always shows real persisted data
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

        // ---- Table columns (include Address!) ----
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
        addBtn.addActionListener(e -> handleAddPatient());      // we’ll stub this next
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

        statusLabel.setText("Loaded patients: " + controller.getAllPatients().size());
    }

    private Object[] patientRow(Patient p) {
        return new Object[]{
                p.getPatientID(),
                p.getFirstName(),
                p.getLastName(),
                p.getNhsNumber(),
                fmtDate(p.getDateOfBirth()), // if Date, prints; you can format later
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

        // Build one dialog panel (no new class)
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
            JTextField dobField = new JTextField("1985-03-15"); // helpful example format
            JTextField nhsField = new JTextField();
            JComboBox<PatientGender> genderBox = new JComboBox<>(PatientGender.values());
            JTextField phoneField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField postcodeField = new JTextField();
            JTextField emergencyNameField = new JTextField();
            JTextField emergencyPhoneField = new JTextField();
            JTextField gpSurgeryField = new JTextField("S001"); // default example

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
        referralBtn.addActionListener(e -> handleMakeReferralFromClinician()); // we’ll implement after

        refreshCliniciansTable();

        return panel;
    }

    private void refreshCliniciansTable() {
        controller.reloadClinicians();
        cliniciansTableModel.setRowCount(0);

        for (Clinician c : controller.getAllClinicians()) {
            cliniciansTableModel.addRow(clinicianRow(c));
        }

        statusLabel.setText("Loaded clinicians: " + controller.getAllClinicians().size());
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
        JTextField titleField = new JTextField("GP");
        JTextField specialityField = new JTextField("General Practice");
        JTextField gmcField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField workplaceIdField = new JTextField("S001");
        JTextField workplaceTypeField = new JTextField("GP Surgery");
        JTextField employmentField = new JTextField("Full-time");
        JTextField startDateField = new JTextField("2025-01-01");

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
            // Add this controller method if you haven't:
            // controller.addClinicianFromForm(...)
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
        JTextField startDateField = new JTextField(c.getClinicianStartDate().toString()); // keep as text

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


    private void handleMakeReferralFromClinician() {
        JOptionPane.showMessageDialog(this, "Make referral not implemented yet.");
    }


}
