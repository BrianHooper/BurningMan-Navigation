import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionHandler implements ActionListener {
    public Controller controller;

    public ActionHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "UpdateLocationButton":
                controller.updateLocation();
                break;
            default: break;
        }
    }
}
