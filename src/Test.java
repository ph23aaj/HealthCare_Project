public class Test {
    public static void main(String[] args) {
        ClinicianManager cm = new ClinicianManager("clinicians.csv");
        cm.load();

        System.out.println("Loaded clinicians: " + cm.getAllClinicians().size());

        Clinician c1 = cm.findByClinicianID("C001");
        System.out.println("C001: " + c1);

        System.out.println("GP clinicians count: " + cm.findByTitle("GP").size());
        System.out.println("Clinicians at S001: " + cm.findByWorkplaceID("S001").size());
    }
}
