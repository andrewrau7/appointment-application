package arau.C195_project.customer_controllers;

import arau.C195_project.DataApplication195;
import helper.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;
import sql_models.AppointmentSQL;
import sql_models.CustomerSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for the CustomerTableView.fxml file. */
public class CustomerTableController implements Initializable {
    public RadioButton allRadioButton;
    public ToggleGroup customerTable;
    public ComboBox<Integer> numAppsComboBox;
    Stage stage;

    @FXML
    private TableColumn<Customer, String> custAddressCol;
    @FXML
    private TableColumn<Customer, String> custCountryCol;
    @FXML
    private TableColumn<Customer, Integer> custIdCol;
    @FXML
    private TableColumn<Customer, String> custNameCol;
    @FXML
    private TableColumn<Customer, String> custPhoneCol;
    @FXML
    private TableColumn<Customer, String> custPostalCodeCol;
    @FXML
    private TableColumn<Customer, String> custStateCol;
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    public void onActionAddCustomer(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "AddCustomerView.fxml");

    }

    /** Method for deleting a Customer from the database as well as the CustomerTableView.
     First, a null value checker is implemented to ensure a selection has been made. Once a Customer
     has been chosen, an Alert is displayed to the screen asking for confirmation. Once the confirmation
     is made, the Customer and all appointments related to the Customer are deleted in the database
     and the Customer is removed from the TableView.
     */
    @FXML
    public void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No customer selected");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleting the selected customer will also"
                + " cancel all appointments associated with them." + "\n\n"
                + customerTableView.getSelectionModel().getSelectedItem().displayInfo() + "\n\n"
                + "Do you wish to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            CustomerSQL.delete(customerTableView.getSelectionModel().getSelectedItem().getId());
            CustomerTableList.removeCustomer(customerTableView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void onActionMainMenu(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "MainMenuView.fxml");

    }

    /** Method for loading the Update Customer page. Null checker is implemented to
     ensure a selection has been made. The UpdateCustomer Controller is then loaded, with the
     "sendCustomer" method being used to send the selected Customer's information to the Update Customer page.
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent event) throws IOException {
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No customer selected");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(DataApplication195.class.getResource("UpdateCustomerView.fxml"));
        loader.load();

        UpdateCustomerController UCController = loader.getController();

        UCController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem(), event);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method for initializing the CustomerTable page with Customer data. First, the
     database is queried, then queried data is displayed in the TableView in order of
     ID number. Customer num_appointment variables are incremented according to how many appointments
     assigned to them in the database. The numAppsComboBox is then populated with numbers ranging from
     0 to the largest number of appointments a customer has.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            CustomerSQL.selectAllCustomers();
            AppointmentSQL.selectAllAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Customer customer: CustomerTableList.getAllCustomers()){
            for(Appointment appointment: AppointmentTableList.getAllAppointments()){
                if (customer.getId() == appointment.getCustomer_id()){
                    customer.incrementNum_appointments();
                }
            }
        }

        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal_code"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country_name"));
        custStateCol.setCellValueFactory(new PropertyValueFactory<>("division_name"));

        customerTableView.getSortOrder().add(custIdCol);

        allRadioButton.fire();

        int max_app_number = 0;

        for (Customer customer : CustomerTableList.getAllCustomers()){
            if (max_app_number < customer.getNum_appointments()){
                max_app_number = customer.getNum_appointments();
            }
        }

        numAppsComboBox.getItems().clear();
        for (int i = 0; i < max_app_number + 1; i++){
            numAppsComboBox.getItems().add(i);
        }
    }

    /** Method for populating the customerTableView with all customers in the database.
     */
    @FXML
    public void onActionShowAllCustomers(ActionEvent event) {
        customerTableView.setItems(CustomerTableList.getAllCustomers());
    }

    /** Method for firing the numAppsComboBox when the filter radio button is selected for ease of reading
     and responsive functionality.
     */
    @FXML
    public void onActionFireFilterComboBox(ActionEvent event) {
        if (numAppsComboBox.getSelectionModel().getSelectedItem() == null){
            numAppsComboBox.getSelectionModel().select(0);
        }
        else{
            numAppsComboBox.fireEvent(event);
        }
    }

    /** ADDITIONAL REPORT: Method for populating the customerTableView with customers
     that have at least the number of appointments selected. */
    @FXML
    public void onActionShowFilteredCustomers(ActionEvent event) {
        CustomerTableList.clearFilteredCustomerList();

        int num_selected = numAppsComboBox.getSelectionModel().getSelectedItem();

        for(Customer customer : CustomerTableList.getAllCustomers()){
            if(customer.getNum_appointments() >= num_selected){
                CustomerTableList.addFilteredCustomer(customer);
            }
        }
        customerTableView.setItems(CustomerTableList.getFilteredCustomers());
    }
}
