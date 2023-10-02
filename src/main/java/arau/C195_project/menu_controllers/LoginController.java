package arau.C195_project.menu_controllers;

import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import sql_models.LoginSQL;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/** Controller class for the LoginView.fxml file. */
public class LoginController implements Initializable {

    @FXML
    public Label invalidLabel;
    @FXML
    public Label zoneIdLabel;
    @FXML
    public Label loginLabel;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public Button loginButton;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField userNameTextField;




    /** Method for initiating the login process. First, variables are declared for
     file writing purposes. Next, the login process is attempted by utilizing methods from the
     LoginSQL abstract class. If successful, information reflecting the valid login is printed
     to the specified file, the file is then closed, and the Main Menu is then loaded.
     If the login is unsuccessful, the invalid login information is printed to the specified file, the
     file is then closed, and an error message is displayed either in English or French depending on
     the user's default language.
     @param event The loginButton being fired.
     */
    @FXML
    public void OnActionLogin(ActionEvent event) throws IOException {
        String filename = "src/login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        DateTimeFormatter fileTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");

        try {
            LoginSQL.login(userNameTextField.getText(), passwordTextField.getText());
            LoginSQL.upcomingAppCheck();

            outputFile.println("Username: " + userNameTextField.getText());
            outputFile.println(fileTimeFormatter.format(ZonedDateTime.now(ZoneId.systemDefault())));
            outputFile.println("Login Successful" + "\n");
            outputFile.close();

            SceneSwitcher.sceneSwitch(event, "MainMenuView.fxml");

        } catch (Exception e) {
            if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle rb = ResourceBundle.getBundle("Nat");
                invalidLabel.setText(rb.getString("Invalid Username/Password"));
            } else {
                invalidLabel.setText("Invalid Username/Password");
            }

            outputFile.println("Username: " + userNameTextField.getText());
            outputFile.println(fileTimeFormatter.format(ZonedDateTime.now(ZoneId.systemDefault())));
            outputFile.println("Login Failed" + "\n");
            outputFile.close();

            userNameTextField.setStyle("-fx-border-color: red");
            passwordTextField.setStyle("-fx-border-color: red");
        }
    }


    @FXML
    public void passwordEnter(ActionEvent event) {
        loginButton.fire();
    }

    @FXML
    public void usernameEnter(ActionEvent event) {
        passwordTextField.requestFocus();
    }


    /** Method for initializing the Login page according to the user's Zone ID and default language.
     A resource bundle is utilized to convert all labels and buttons to French. The Login Button is also
     modified to accept an "ENTER" key press as a fire mechanism.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                loginButton.fire();
            }
        });

        if(Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle rb = ResourceBundle.getBundle("Nat");
            loginLabel.setText(rb.getString("Login"));
            usernameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            loginButton.setText(rb.getString("Submit"));
        }
        zoneIdLabel.setText(String.valueOf(ZoneId.systemDefault()));
    }
}
