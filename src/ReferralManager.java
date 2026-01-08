import java.time.LocalDate;
import java.util.ArrayList;

public class ReferralManager {

    private static ReferralManager instance;

    private final ArrayList<Referral> referrals = new ArrayList<>();
    private String filename = "referrals.csv";

    private ReferralManager() {}

    public static ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void load() {
        referrals.clear();

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No data found in: " + filename);
            return;
        }

        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            referrals.add(Referral.fromCSV(line));
        }
    }

    public ArrayList<Referral> getAllReferrals() {
        return new ArrayList<>(referrals);
    }

    public ArrayList<Referral> getReferralsByPatientID(String patientID) {
        ArrayList<Referral> out = new ArrayList<>();
        for (Referral r : referrals) {
            if (r.getPatientID() != null && r.getPatientID().equals(patientID)) {
                out.add(r);
            }
        }
        return out;
    }


    public void saveAll() {
        ArrayList<String> out = new ArrayList<>();

        out.add(
                "referral_id,patient_id,referring_clinician_id,referred_to_clinician_id," +
                        "referring_facility_id,referred_to_facility_id,referral_date,urgency_level," +
                        "referral_reason,clinical_summary,requested_investigations,status," +
                        "appointment_id,notes,created_date,last_updated"

);

        for (Referral r : referrals) {
            out.add(r.toCSV());
        }

        CSVHandler.writeLines(filename, out);
    }

    private String generateNextReferralID() {
        int max = 0;

        for (Referral r : referrals) {
            String id = r.getReferralID();
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }

        return String.format("R%03d", max + 1);
    }

    public Referral createReferral(String patientID, String fromClinician, String toClinician,
                                   String fromFacility, String toFacility,
                                   LocalDate referralDate, String urgencyLevel,
                                   String reasonForReferral, String clinicalSummary,
                                   String requestedInvestigations,  String referralNotes) {

        String newReferralID = generateNextReferralID();
        LocalDate now = LocalDate.now();
        String appointmentID = "";

        Referral r = new Referral(
                newReferralID,
                patientID,
                fromClinician,
                toClinician,
                fromFacility,
                toFacility,
                referralDate,
                urgencyLevel,
                reasonForReferral,
                clinicalSummary,
                requestedInvestigations,
                ReferralStatus.NEW,
                appointmentID,
                referralNotes,
                now,
                now
        );

        referrals.add(r);
        saveAll();

        // create output "email" text file
        ReferralTextWriter.writeReferralEmail(r);

        return r;
    }

    public void deleteReferral(String referralID) {
        for (int i = 0; i < referrals.size(); i++) {
            if (referrals.get(i).getReferralID().equals(referralID)) {
                referrals.remove(i);
                saveAll();
                return;
            }
        }
        throw new IllegalArgumentException("No referral found with ID: " + referralID);
    }

    public void updateReferralStatus(String referralID, ReferralStatus newStatus) {
        for (Referral r : referrals) {
            if (r.getReferralID().equals(referralID)) {
                r.setReferralStatus(newStatus);
                r.setReferralLastUpdated(LocalDate.now());
                saveAll();
                // regenerates the text file to reflect the updated status
                ReferralTextWriter.writeReferralEmail(r);
                return;
            }
        }
        throw new IllegalArgumentException("No referral found with ID: " + referralID);
    }
}
