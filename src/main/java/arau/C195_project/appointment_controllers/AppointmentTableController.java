package arau.C195_project.appointment_controllers;

import arau.C195_project.DataApplication195;
import helper.ObservableListFilter;
import helper.SceneSwitcher;
import helper.TimeHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import models.*;
import sql_models.AppointmentSQL;
import sql_models.ComboBoxFillerFromDbSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.*;

/** Controller class for the AppointmentTableView.fxml file. */
public class AppointmentTableController implements Initializable {
    @FXML
    public ToggleGroup AppointmentTableFilter;
    @FXML
    public RadioButton allFilterRadioButton;
    @FXML
    public ComboBox<Contact> contactComboBox;
    @FXML
    public DatePicker appDatePicker;
    @FXML
    public ComboBox<String> monthOrTypeFilterComboBox;
    @FXML
    public ComboBox<String> monthOrTypeItemsComboBox;
    @FXML
    public TextField numberResultTextField;
    @FXML
    public Tooltip dateToolTip;
    Stage stage;
    @FXML
    private TableColumn<Appointment, String> appContactCol;
    @FXML
    private TableColumn<Appointment, Integer> appCustCol;
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;
    @FXML
    private TableColumn<Appointment, String> appEndCol;
    @FXML
    private TableColumn<Appointment, Integer> appIdCol;
    @FXML
    private TableColumn<Appointment, String> appLocationCol;
    @FXML
    private TableColumn<Appointment, String> appStartCol;
    @FXML
    private TableColumn<Appointment, String> appTitleCol;
    @FXML
    private TableColumn<Appointment, String> appTypeCol;
    @FXML
    private TableColumn<Appointment, Integer> appUserCol;
    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    public void onActionAddAppointment(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "AddAppointmentView.fxml");
    }

    /** Method for deleting an Appointment from the database as well as the AppointmentTableView.
     First, a null value checker is implemented to ensure a selection has been made. Once an Appointment
     has been chosen, an Alert is displayed to the screen asking for confirmation. Once the confirmation
     is made, the Appointment is deleted in the database and is then removed from the TableView. Functionality is
     also added to the method so that Appointments that are selected from a filtered TableView can be deleted from
     the database and TableView as well.
     */
    @FXML
    public void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        if(appointmentTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No appointment selected");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Selected appointment will be deleted:" + "\n\n"
                                                                + appointmentTableView.getSelectionModel().getSelectedItem().displayInfo() + "\n\n"
                                                                + "Please Confirm");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            AppointmentSQL.delete(appointmentTableView.getSelectionModel().getSelectedItem().getId());
            if(appointmentTableView.getItems() != AppointmentTableList.getAllAppointments()) {
                appointmentTableView.getItems().remove(appointmentTableView.getSelectionModel().getSelectedItem());
            }
            AppointmentTableList.removeAppointmentFromAll(appointmentTableView.getSelectionModel().getSelectedItem());
        }
    }


    @FXML
    public void onActionMainMenu(ActionEvent event) throws IOException {
        SceneSwitcher.sceneSwitch(event, "MainMenuView.fxml");

    }

    /** Method for loading the Update Appointment page. Null checker is implemented to
     ensure a selection has been made. The UpdateAppointment Controller is then loaded, with the
     "sendAppointment" method being used to send the selected Appointment's information to the Update Appointment page.
     */
    @FXML
    public void onActionUpdateAppointment(ActionEvent event) throws IOException {
        if(appointmentTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "No appointment selected");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(DataApplication195.class.getResource("UpdateAppointmentView.fxml"));
        loader.load();

        UpdateAppointmentController UAController = loader.getController();

        UAController.sendAppointment(appointmentTableView.getSelectionModel().getSelectedItem(), event);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method initializing the Appointment Table page with necessary data and functionality. The
     allFilterRadioButton is fired to populate the TableView with appointments.
     <p></p>
     Tool tip is added to the Date RadioButton for added clarity on its functionality.
     <p></p>
     String Converter is added to the appDatePicker to match that of the TableView.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allFilterRadioButton.fire();

        dateToolTip.setShowDelay(Duration.seconds(.5));

        monthOrTypeFilterComboBox.getItems().add("Type");
        monthOrTypeFilterComboBox.getItems().add("Month");


        StringConverter<LocalDate> datePickerFormat = new StringConverter<>() {

            @Override
            public String toString(LocalDate localDate) {
                if(localDate == null){
                    return "";
                }
                return TimeHandler.datePickerTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String enteredDate) {
                return LocalDate.parse(enteredDate);
            }
        };
        appDatePicker.setConverter(datePickerFormat);
    }



    /** Method for filtering the AppointmentTableView by Appointments that are scheduled within
     the current week number of the current year.
     LAMBDA: The ObservableListFilter Functional Interface is implemented with a lambda expression to
     search all scheduled Appointments in the ObservableList "allAppointments" within the AppointmentTableList
     abstract class and return an ObservableList of all Appointments that are scheduled within the current
     week number of the current year. The filtered ObservableList is then passed to the AppointmentTableView
     for display.
     @param event "This Week" RadioButton being selected.
     */
    public void onActionFilterByWeek(ActionEvent event) {

        appUserCol.setPrefWidth(74);
        appCustCol.setPrefWidth(80);

        contactComboBox.setVisible(false);
        appDatePicker.setVisible(false);

        ObservableListFilter getThisWeeksApps = (list) -> {

            ObservableList<Appointment> thisWeekAppointments = FXCollections.observableArrayList();

            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

            for(Appointment appointment : list){
                if (appointment.getStart_date_with_zone().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) ==
                        now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)){
                    thisWeekAppointments.add(appointment);
                }
            }
            return thisWeekAppointments;
        };
        appointmentTableView.setItems(getThisWeeksApps.filter(AppointmentTableList.getAllAppointments()));
    }


    /** Method for filtering the AppointmentTableView by Appointments that are scheduled within
     the current month of the current year.
     LAMBDA: The ObservableListFilter Functional Interface is implemented with a lambda expression to
     search all scheduled Appointments in the ObservableList "allAppointments" within the AppointmentTableList
     abstract class and return an ObservableList of all Appointments that are scheduled within the current
     month of the current year. The filtered ObservableList is then passed to the AppointmentTableView
     for display.
     @param event "This Month" RadioButton being selected.
     */
    public void onActionFilterByMonth(ActionEvent event) {

        appUserCol.setPrefWidth(74);
        appCustCol.setPrefWidth(80);

        contactComboBox.setVisible(false);
        appDatePicker.setVisible(false);

        ObservableListFilter getThisMonthsApps = (list) -> {

            ObservableList<Appointment> thisMonthAppointments = FXCollections.observableArrayList();

            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

            for(Appointment appointment : list){
                if (appointment.getStart_date_with_zone().getMonth() == now.getMonth()){
                    thisMonthAppointments.add(appointment);
                }
            }
            return thisMonthAppointments;
        };
        appointmentTableView.setItems(getThisMonthsApps.filter(AppointmentTableList.getAllAppointments()));
    }


    /** Method for populating the AppointmentTableView with all scheduled appointments. Sort order is
     set to list Appointments in order of their respective start dates.
     @param event "All" RadioButton being selected.
     */
    public void onActionShowAllAppointments(ActionEvent event) {
        contactComboBox.setVisible(false);
        appDatePicker.setVisible(false);

        appUserCol.setPrefWidth(74);
        appCustCol.setPrefWidth(80);

        AppointmentSQL.selectAllAppointments();
        appointmentTableView.setItems(AppointmentTableList.getAllAppointments());


        appIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contact_name"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        appCustCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        appUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        appointmentTableView.getSortOrder().add(appStartCol);
    }

    /** Method for displaying and populating the contactComboBox with Contacts from the database. The first Contact
     retrieved is selected to populate the AppointmentTableView with Appointment Data to make it clear
     that the filter option has been changed.
     @param event "Contact" RadioButton being selected.
     */
    public void onActionShowContactCombo(ActionEvent event) throws SQLException {
        contactComboBox.setVisible(true);
        appDatePicker.setVisible(false);
        contactComboBox.getItems().clear();
        ComboBoxFillerFromDbSQL.fillContactComboBox(contactComboBox);
        appUserCol.setPrefWidth(153);
        appCustCol.setPrefWidth(153);
        contactComboBox.getSelectionModel().select(0);
        contactComboBox.fireEvent(event);
    }

    /** Method for displaying the appDatePicker.
     The current day is selected to populate the AppointmentTableView with Appointment data
     relevant to the current day to make it clear that the filter option has been changed.
     @param event "Date" RadioButton being selected.
     */
    public void onActionShowDatePicker(ActionEvent event) {
        contactComboBox.setVisible(false);
        appDatePicker.setVisible(true);
        appUserCol.setPrefWidth(74);
        appCustCol.setPrefWidth(80);
        appDatePicker.setValue(LocalDate.now());
        appointmentTableView.setItems(AppointmentTableList.getDateAppointments(LocalDate.now()));
    }

    /** Method for populating the TableView with Appointments that start, end, or overlap with the
     date chosen in the appDatePicker.
     */
    public void onActionShowByDate(ActionEvent event) {
        appointmentTableView.setItems(AppointmentTableList.getDateAppointments(appDatePicker.getValue()));
    }

    /** Method for populating the TableView with Appointments that are assigned to the Contact
     chosen in the contactComboBox.
     */
    public void onActionFilterByContact(ActionEvent event) {
        if(contactComboBox.getValue() == null){
            return;
        }

        int contact_id = contactComboBox.getValue().getContact_id();

        appointmentTableView.setItems(AppointmentTableList.getContactAppointments(contact_id));
    }

    /** Method for populating the monthOrTypeItemsComboBox with data respective
     of the choice selected in the monthOrTypeFilterComboBox.
     <p></p>
     If "Type" is selected, a TreeSet is utilized to gather type data from all
     appointments during iteration. The TreeSet itself is then iterated through to populate the
     monthOrTypeItemsComboBox with the name of each different type.
     <p></p>
     If "Month" is selected, a standard for loop is utilized to populate the monthOrTypeItemsComboBox
     with the name of each month in the year.
     */
    public void onActionShowMonthTypesItems(ActionEvent event) {
        if (monthOrTypeFilterComboBox.getValue() == null){
            monthOrTypeItemsComboBox.setVisible(false);
            return;
        }


        if (Objects.equals(monthOrTypeFilterComboBox.getValue(), "Type")){
            monthOrTypeItemsComboBox.setVisible(true);
            numberResultTextField.setVisible(false);

            monthOrTypeItemsComboBox.getItems().clear();

            Set<String> types = new TreeSet<String>();
            for(Appointment appointment : AppointmentTableList.getAllAppointments()){
                types.add(appointment.getType());
            }
            for(String type_name : types){
                monthOrTypeItemsComboBox.getItems().add(type_name);
            }
            monthOrTypeItemsComboBox.setVisibleRowCount(Math.min(monthOrTypeItemsComboBox.getItems().size(), 8));
            monthOrTypeItemsComboBox.getSelectionModel().select(0);
        }


        if (Objects.equals(monthOrTypeFilterComboBox.getValue(), "Month")){
            monthOrTypeItemsComboBox.setVisible(true);
            numberResultTextField.setVisible(false);

            monthOrTypeItemsComboBox.getItems().clear();

            for(int i = 1; i < 13; i++){
                String month = Month.of(i).toString().toLowerCase();
                month = month.substring(0, 1).toUpperCase() + month.substring(1);
                monthOrTypeItemsComboBox.getItems().add(month);
            }
            monthOrTypeItemsComboBox.setVisibleRowCount(8);
            monthOrTypeItemsComboBox.getSelectionModel().select(0);
        }
    }


    /** Method for displaying the number of appointments either by the type selected or
     month selected. Enhanced for loops that iterate through all appointments are utilized for
     either option.
     */
    public void onActionShowNumTypesMonths(ActionEvent event) {
        if(Objects.equals(monthOrTypeFilterComboBox.getValue(), "Type")){
            int typeCounter = 0;
            for(Appointment appointment : AppointmentTableList.getAllAppointments()){
                if(Objects.equals(appointment.getType(), monthOrTypeItemsComboBox.getValue())){
                    typeCounter++;
                }
            }
            numberResultTextField.setVisible(true);
            numberResultTextField.setText(Integer.toString(typeCounter));
        }

        if(Objects.equals(monthOrTypeFilterComboBox.getValue(), "Month")) {
            int monthCounter = 0;
            for (Appointment appointment : AppointmentTableList.getAllAppointments()) {
                int chosenMonthValue = monthOrTypeItemsComboBox.getItems().indexOf(monthOrTypeItemsComboBox.getValue()) + 1;
                int appMonthStartValue = appointment.getStart_date_with_zone().getMonth().getValue();
                int appMonthEndValue = appointment.getEnd_date_with_zone().getMonth().getValue();
                if (chosenMonthValue == appMonthStartValue || chosenMonthValue == appMonthEndValue
                        ||(chosenMonthValue > appMonthStartValue && chosenMonthValue < appMonthEndValue)){
                    monthCounter++;
                }
            }
            numberResultTextField.setVisible(true);
            numberResultTextField.setText(Integer.toString(monthCounter));
        }
    }
}
