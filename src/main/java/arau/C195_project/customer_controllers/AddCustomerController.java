package arau.C195_project.customer_controllers;

import helper.InputHandler;
import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.*;
import sql_models.ComboBoxFillerFromDbSQL;
import sql_models.CustomerSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for the AddCustomerView.fxml file. */
public class AddCustomerController implements Initializable {

    @FXML
    private Label custAddressErrorLabel;
    @FXML
    private TextField custAddressTextField;
    @FXML
    private ComboBox<Country> custCountryComboBox;
    @FXML
    private Label custCountryErrorLabel;
    @FXML
    private ComboBox<Division> custDivisionComboBox;
    @FXML
    private Label custDivisionErrorLabel;
    @FXML
    private Label custNameErrorLabel;
    @FXML
    private TextField custNameTextField;
    @FXML
    private Label custPhoneErrorLabel;
    @FXML
    private TextField custPhoneTextField;
    @FXML
    private Label custPostCodeErrorLabel;
    @FXML
    private TextField custPostCodeTextField;
    @FXML
    public void onActionAddressEnter(ActionEvent event) {
        custPostCodeTextField.requestFocus();
    }

    /** Method for returning to the CustomerTable page without adding a Customer to the database.
     An alert is utilized to prevent the user from accidentally deleting information that was meant
     for an insert operation.
     */
    @FXML
    public void onActionCancelAdd(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            SceneSwitcher.sceneSwitch(event, "CustomerTableView.fxml");
        }
    }

    /** Method for taking user input from the Add Customer page and using it to add a Customer record
     to the database. An Exception Counter variable is implemented to check for any input errors that
     could be returned by the InputHandler class. If no errors are detected, the CustomerSQL class is
     utilized to send the valid information to the database. String variables are utilized to hold
     Customer information for readability purposes. If errors are detected, the offending text fields
     are updated along with their respective labels to reflect an error.
     */
    @FXML
    public void onActionInsertCustomer(ActionEvent event) throws SQLException, IOException {

        InputHandler.resetTextFieldAndLabel(custNameTextField, custNameErrorLabel);
        InputHandler.resetTextFieldAndLabel(custAddressTextField, custAddressErrorLabel);
        InputHandler.resetTextFieldAndLabel(custPostCodeTextField, custPostCodeErrorLabel);
        InputHandler.resetTextFieldAndLabel(custPhoneTextField, custPhoneErrorLabel);
        InputHandler.resetComboBoxAndLabel(custCountryComboBox, custCountryErrorLabel);
        InputHandler.resetComboBoxAndLabel(custDivisionComboBox, custDivisionErrorLabel);

        int exceptCount = 0;

        exceptCount += InputHandler.checkTextField(custNameTextField, custNameErrorLabel);
        exceptCount += InputHandler.checkTextField(custAddressTextField, custAddressErrorLabel);
        exceptCount += InputHandler.checkTextField(custPostCodeTextField, custPostCodeErrorLabel);
        exceptCount += InputHandler.checkTextField(custPhoneTextField, custPhoneErrorLabel);
        exceptCount += InputHandler.checkComboBox(custCountryComboBox, custCountryErrorLabel);
        exceptCount += InputHandler.checkComboBox(custDivisionComboBox, custDivisionErrorLabel);



        if (exceptCount == 0){
            String inputCustName = custNameTextField.getText();
            String inputCustAddress = custAddressTextField.getText();
            String inputCustPostCode = custPostCodeTextField.getText();
            String inputCustPhone = custPhoneTextField.getText();
            int inputCustDivisionId = custDivisionComboBox.getValue().getDivision_id();
            CustomerSQL.insert(inputCustName, inputCustAddress, inputCustPostCode, inputCustPhone, inputCustDivisionId);

            SceneSwitcher.sceneSwitch(event, "CustomerTableView.fxml");
        }
    }

    @FXML
    public void onActionNameEnter(ActionEvent event) {
        custAddressTextField.requestFocus();
    }
    @FXML
    public void onActionPostCodeEnter(ActionEvent event) {
        custPhoneTextField.requestFocus();
    }

    /** Method for initializing the AddCustomer page with the Country ComboBox data directly from
     the database.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ComboBoxFillerFromDbSQL.fillCountryComboBox(custCountryComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Method for populating the custDivisionComboBox with data respective of the option
     selected in the custCountryComboBox. Division data is pulled directly from the
     database and the visible row count of the custDivisionComboBox is dynamically
     altered depending on the number of items present.
     */
    public void onActionFillDivisions(ActionEvent event) {
        if (custCountryComboBox.getValue() == null){
            return;
        }
        int country_id = custCountryComboBox.getValue().getCountryId();
        try {
            ComboBoxFillerFromDbSQL.fillDivisionComboBox(custDivisionComboBox, country_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        custDivisionComboBox.setVisibleRowCount(Math.min(custDivisionComboBox.getItems().size(), 10));
    }
}

