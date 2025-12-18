import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        String filename = "patients.csv";

        ArrayList<String> lines = CSVHandler.readLines(filename);

        if (lines.size() < 2) {
            System.out.println("Not enough lines to test. Check file and header.");
            return;
        }

        int max = Math.min(lines.size() - 1, 10); // test first 10 rows
        int pass = 0;

        for (int i = 1; i <= max; i++) { // skip header
            String originalLine = lines.get(i);

            Patient p1 = Patient.fromCSV(originalLine);
            String writtenLine = p1.toCSV();
            Patient p2 = Patient.fromCSV(writtenLine);

            boolean ok =
                    p1.getPatientID().equals(p2.getPatientID()) &&
                            p1.getFirstName().equals(p2.getFirstName()) &&
                            p1.getLastName().equals(p2.getLastName()) &&
                            p1.getNhsNumber().equals(p2.getNhsNumber()) &&
                            p1.getAddress().equals(p2.getAddress()) &&
                            p1.getPostcode().equals(p2.getPostcode()) &&
                            p1.getGpSurgeryID().equals(p2.getGpSurgeryID()) &&
                            p1.getDateOfBirth().equals(p2.getDateOfBirth()) &&
                            p1.getRegisterDate().equals(p2.getRegisterDate());

            if (!ok) {
                System.out.println("FAILED round-trip at row " + i);
                System.out.println("Original: " + originalLine);
                System.out.println("Written : " + writtenLine);
                System.out.println("Parsed2 : " + p2);
                System.out.println();
            } else {
                pass++;
            }
        }

        System.out.println("Round-trip pass: " + pass + " / " + max);
    }
}
