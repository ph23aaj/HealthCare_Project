import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String filename = "patients.csv"; // adjust if your file is elsewhere

        ArrayList<String> lines = CSVHandler.readLines(filename);
        if (lines.isEmpty()) {
            System.out.println("No lines read. Check file path.");
            return;
        }

        // assume first line is header
        String firstDataRow = lines.get(1);
        System.out.println("First data row:");
        System.out.println(firstDataRow);

        Patient p = Patient.fromCSV(firstDataRow);
        System.out.println("\nParsed Patient:");
        System.out.println(p);

        System.out.println("\ntoCSV output:");
        System.out.println(p.toCSV());
    }
}
