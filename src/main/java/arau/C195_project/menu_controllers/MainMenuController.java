package arau.C195_project.menu_controllers;

import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

/** Controller class for the MainMenuView.fxml file. */
public class MainMenuController {

    @FXML
    public void onActionAppointmentsDisplay(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "AppointmentTableView.fxml");
    }

    @FXML
    public void onActionCustomerDisplay(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "CustomerTableView.fxml");
    }

    @FXML
    public void onActionLogout(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "LoginView.fxml");
    }
}