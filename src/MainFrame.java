import javax.swing.*;

public class MainFrame {
    public static void main(String[] args) {
        HealthcareModel model = new HealthcareModel();
        model.loadAll();

        HealthcareController controller = new HealthcareController(model);

        SwingUtilities.invokeLater(() -> {
            HealthcareView view = new HealthcareView(controller);
            view.setVisible(true);
        });
    }
}
