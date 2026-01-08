import java.util.ArrayList;

public class ReferralTextWriter {

    public static void writeReferralEmail(Referral r) {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("=== REFERRAL EMAIL ===");
        lines.add("Referral ID: " + r.getReferralID());
        lines.add("Status: " + r.getReferralStatus());
        lines.add("Created: " + r.getReferralCreated());
        lines.add("Last Updated: " + r.getReferralLastUpdated());
        lines.add("");

        lines.add("From Clinician: " + r.getFromClinician());
        lines.add("To Clinician: " + r.getToClinician());
        lines.add("From Facility: " + r.getFromFacility());
        lines.add("To Facility: " + r.getToFacility());
        lines.add("");

        lines.add("Patient ID: " + r.getPatientID());
        lines.add("Referral Date: " + r.getReferralDate());
        lines.add("Urgency Level: " + r.getUrgencyLevel());
        lines.add("");

        lines.add("Reason for Referral:");
        lines.add(r.getReasonForReferral());
        lines.add("");

        lines.add("Clinical Summary:");
        lines.add(r.getClinicalSummary());
        lines.add("");

        lines.add("Requested Investigations:");
        lines.add(r.getRequestedInvestigations());
        lines.add("");

        lines.add("Referral Notes:");
        lines.add(r.getReferralNotes());
        lines.add("");

        String file = "referral_" + r.getReferralID() + ".txt";
        CSVHandler.writeLines(file, lines);
    }
}
