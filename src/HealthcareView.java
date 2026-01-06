import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class HealthcareView extends JFrame {

    private final HealthcareController controller;

    private JTextField nhsField, dateField, timeField;
    private JComboBox<AppointmentType> typeBox;
    private JTextField reasonField, notesField;

    private JTextField apptIdField, newDateField, newTimeField;
    private JLabel statusLabel;
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsTableModel;

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
        tabs.add("Appointments", buildAppointmentsTab());
        add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);
    }

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


}
