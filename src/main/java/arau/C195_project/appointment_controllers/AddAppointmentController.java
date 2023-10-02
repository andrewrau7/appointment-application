package arau.C195_project.appointment_controllers;

import helper.InputHandler;
import helper.SceneSwitcher;
import helper.TimeHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.*;
import sql_models.AppointmentSQL;
import sql_models.ComboBoxFillerFromDbSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for the AddAppointmentView.fxml file. */
public class AddAppointmentController implements Initializable {

    @FXML
    public Label appStartDateErrorLabel;
    @FXML
    public Label appEndDateErrorLabel;
    @FXML
    public Label appStartTimeErrorLabel;
    @FXML
    public Label appEndTimeErrorLabel;
    @FXML
    public Label localBusinessHoursLabel;
    @FXML
    private ComboBox<Contact> appContactComboBox;
    @FXML
    private Label appContactErrorLabel;
    @FXML
    private Label appCustErrorLabel;
    @FXML
    private Label appDescriptionErrorLabel;
    @FXML
    private TextField appDescriptionTextField;
    @FXML
    private DatePicker appEndDatePicker;
    @FXML
    private ComboBox<String> appEndTimeComboBox;
    @FXML
    private Label appLocationErrorLabel;
    @FXML
    private TextField appLocationTextField;
    @FXML
    private DatePicker appStartDatePicker;
    @FXML
    private ComboBox<String> appStartTimeComboBox;
    @FXML
    private Label appTitleErrorLabel;
    @FXML
    private TextField appTitleTextField;
    @FXML
    private Label appTypeErrorLabel;
    @FXML
    private TextField appTypeTextField;
    @FXML
    private Label appUserErrorLabel;
    @FXML
    private ComboBox<Integer> appCustIdComboBox;
    @FXML
    private ComboBox<Integer> appUserIdComboBox;

    /** Method for returning to the AppointmentTable page without adding an Appointment to the database.
     An alert is utilized to prevent the user from accidentally deleting information that was meant
     for an insert operation.
     */
    @FXML
    public void onActionCancelAdd(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            appContactComboBox.getItems().clear();

            SceneSwitcher.sceneSwitch(event, "AppointmentTableView.fxml");
        }
    }

    @FXML
    public void onActionDescriptionEnterKey(ActionEvent event) {
        appLocationTextField.requestFocus();
    }

    /** Method for taking user input from the Add Appointment page and using it to add an Appointment record
     to the database. An Exception Counter variable is implemented to check for any input errors that
     could be returned by the InputHandler class. If no errors are detected, an enhanced for loop is utilized
     to check for any conflicting Appointments times that have a matching Customer ID. If no exception is raised
     in this process, then the valid Appointment information is inserted into the database.
     <p></p>
     If errors are detected within the text fields, the offending text fields are updated along with
     their respective labels to reflect an error. If conflicting Appointment times are detected, an Alert is
     displayed to the screen along with a description of the conflicting appointment.
     <p></p>
     String variables are utilized to hold Appointment information for readability purposes. Boolean variables
     are also utilized for readability purposes.
     */
    @FXML
    public void onActionInsertAppointment(ActionEvent event) throws SQLException, IOException {

        InputHandler.resetTextFieldAndLabel(appTitleTextField, appTitleErrorLabel);
        InputHandler.resetTextFieldAndLabel(appDescriptionTextField, appDescriptionErrorLabel);
        InputHandler.resetTextFieldAndLabel(appLocationTextField, appLocationErrorLabel);
        InputHandler.resetTextFieldAndLabel(appTypeTextField, appTypeErrorLabel);
        InputHandler.resetDatePickerAndLabel(appStartDatePicker, appStartDateErrorLabel);
        InputHandler.resetDatePickerAndLabel(appEndDatePicker, appEndDateErrorLabel);
        InputHandler.resetComboBoxAndLabel(appStartTimeComboBox, appStartTimeErrorLabel);
        InputHandler.resetComboBoxAndLabel(appEndTimeComboBox, appEndTimeErrorLabel);
        InputHandler.resetComboBoxAndLabel(appCustIdComboBox, appCustErrorLabel);
        InputHandler.resetComboBoxAndLabel(appUserIdComboBox, appUserErrorLabel);
        InputHandler.resetComboBoxAndLabel(appContactComboBox, appContactErrorLabel);

        int exceptCount = 0;

        exceptCount += InputHandler.checkTextField(appTitleTextField, appTitleErrorLabel);
        exceptCount += InputHandler.checkTextField(appDescriptionTextField, appDescriptionErrorLabel);
        exceptCount += InputHandler.checkTextField(appLocationTextField, appLocationErrorLabel);
        exceptCount += InputHandler.checkTextField(appTypeTextField, appTypeErrorLabel);
        exceptCount += InputHandler.checkDatePicker(appStartDatePicker, appStartDateErrorLabel);
        exceptCount += InputHandler.checkDatePicker(appEndDatePicker, appEndDateErrorLabel);
        exceptCount += InputHandler.checkComboBox(appStartTimeComboBox, appStartTimeErrorLabel);
        exceptCount += InputHandler.checkComboBox(appEndTimeComboBox, appEndTimeErrorLabel);
        exceptCount += InputHandler.checkComboBox(appCustIdComboBox, appCustErrorLabel);
        exceptCount += InputHandler.checkComboBox(appUserIdComboBox, appUserErrorLabel);
        exceptCount += InputHandler.checkComboBox(appContactComboBox, appContactErrorLabel);


        if (exceptCount == 0) {

            ZonedDateTime localZoneStartDateTime = TimeHandler.localZonedDateTimeConverterFromInput(appStartDatePicker.getValue(), appStartTimeComboBox.getValue());

            ZonedDateTime localZoneEndDateTime = TimeHandler.localZonedDateTimeConverterFromInput(appEndDatePicker.getValue(), appEndTimeComboBox.getValue());

            for (Appointment appointment : AppointmentTableList.getAllAppointments()) {

                if (appointment.getCustomer_id() == appCustIdComboBox.getValue()) {

                    boolean inputStartIsSameOrAfterStoredStart = (localZoneStartDateTime.isAfter(appointment.getStart_date_with_zone()) || localZoneStartDateTime.isEqual(appointment.getStart_date_with_zone()));
                    boolean inputStartIsSameOrBeforeStoredEnd = (localZoneStartDateTime.isBefore(appointment.getEnd_date_with_zone())) || localZoneStartDateTime.isEqual(appointment.getEnd_date_with_zone());
                    boolean inputEndIsSameOrAfterStoredStart = (localZoneEndDateTime.isAfter(appointment.getStart_date_with_zone()) || localZoneStartDateTime.isEqual(appointment.getStart_date_with_zone()));
                    boolean inputEndIsSameOrBeforeStoredEnd = (localZoneEndDateTime.isBefore(appointment.getEnd_date_with_zone()) || localZoneEndDateTime.isEqual(appointment.getEnd_date_with_zone()));

                    if ((inputStartIsSameOrAfterStoredStart && inputStartIsSameOrBeforeStoredEnd) || (inputEndIsSameOrAfterStoredStart && inputEndIsSameOrBeforeStoredEnd)){

                        exceptCount++;

                        Alert alert = new Alert(Alert.AlertType.ERROR, "The selected customer currently has an appointment within the desired input time.\n\n" +
                                "Conflicting appointment info:\n\n" + appointment.displayInfo());
                        alert.showAndWait();
                    }
                }
            }

            if (exceptCount == 0) {

                    String inputTitle = appTitleTextField.getText();
                    String inputDescription = appDescriptionTextField.getText();
                    String inputLocation = appLocationTextField.getText();
                    String inputType = appTypeTextField.getText();
                    String inputStartDateTimeString = TimeHandler.toUtcDateTimeString(localZoneStartDateTime);
                    String inputEndDateTimeString = TimeHandler.toUtcDateTimeString(localZoneEndDateTime);
                    int inputCustId = appCustIdComboBox.getValue();
                    int inputUserId = appUserIdComboBox.getValue();
                    int inputContactId = appContactComboBox.getSelectionModel().getSelectedItem().getContact_id();

                    AppointmentSQL.insert(inputTitle, inputDescription, inputLocation, inputType, inputStartDateTimeString, inputEndDateTimeString, inputCustId, inputUserId, inputContactId);

                    SceneSwitcher.sceneSwitch(event, "AppointmentTableView.fxml");
            }
        }
    }


    @FXML
    public void onActionLocationEnterKey(ActionEvent event) {
        appTypeTextField.requestFocus();
    }

    @FXML
    public void onActionTitleEnterKey(ActionEvent event) {
        appDescriptionTextField.requestFocus();
    }

    /** Method to initialize the Update Customer page with necessary data and functionality.
     LocalBusinessHoursLabel is set to display the business hours of operation in hours time consistent
     with the user's system.
     <p></p>
     StartTime and EndTime ComboBoxes are initially populated with time values of fifteen minute increments between
     the local opening hour and the local closing hour.
     <p></p>
     User ID, Customer ID, and Contact ComboBoxes are populated with data directly from the database.
     <p></p>
     String Converter is added to datePickers for consistent display throughout the application.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        localBusinessHoursLabel.setText(TimeHandler.getLocalBusinessHoursString());

        TimeHandler.timeComboBoxFiller(appStartTimeComboBox, TimeHandler.getLocal_open_hour());
        TimeHandler.timeComboBoxFiller(appEndTimeComboBox, TimeHandler.getLocal_open_hour());

        try {
            ComboBoxFillerFromDbSQL.fillUserIdComboBox(appUserIdComboBox);
            ComboBoxFillerFromDbSQL.fillCustIdComboBox(appCustIdComboBox);
            ComboBoxFillerFromDbSQL.fillContactComboBox(appContactComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

        appStartDatePicker.setConverter(datePickerFormat);
        appEndDatePicker.setConverter(datePickerFormat);

    }

    /** Method for ensuring all end time and end date values are after start time and start date values
     when a value is chosen in the appStartDatePicker. Any date prior to the chosen value is subsequently
     disabled in the appEndDatePicker. If the two dates are equal between the datePickers, the endTimeComboBox
     is populated with time increments equal to and greater than the value of the startTimeComboBox.
     */
    public void onActionFromStartDate(ActionEvent event) {

        if( appEndDatePicker.getValue() == null){
            appEndDatePicker.setValue(appStartDatePicker.getValue());

        } else if (appEndDatePicker != null && appStartDatePicker.getValue().compareTo(appEndDatePicker.getValue()) > 0) {
            appEndDatePicker.setValue(appStartDatePicker.getValue());

        }

        appEndDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = appStartDatePicker.getValue();

                setDisable(empty || date.compareTo(startDate) < 0);
            }
        });

        if(appStartTimeComboBox.getValue() == null){
            return;
        }

        if(appEndDatePicker.getValue().compareTo(appStartDatePicker.getValue()) > 0) {

            String keep_end_time = appEndTimeComboBox.getValue();

            TimeHandler.timeComboBoxFiller(appEndTimeComboBox, TimeHandler.getLocal_open_hour());

            if(keep_end_time != null){
                appEndTimeComboBox.setValue(keep_end_time);
            }
        }else if (appStartDatePicker.getValue().equals(appEndDatePicker.getValue()) && appStartTimeComboBox != null) {

            String keep_end_time = appEndTimeComboBox.getValue();

            LocalTime start_time = LocalTime.parse(appStartTimeComboBox.getValue(), TimeHandler.hourMinAmPmFormatter);

            TimeHandler.timeComboBoxFiller(appEndTimeComboBox, start_time);

            if(keep_end_time != null && LocalTime.parse(keep_end_time, TimeHandler.hourMinAmPmFormatter).isAfter(start_time)){
                appEndTimeComboBox.setValue(keep_end_time);
            }
        }
    }

    /** Method for ensuring all end time values are after start time values
     when a value is chosen in the appEndDatePicker. If the two dates are equal between both the
     startDate and endDate datePickers, the endTimeComboBox is populated with time increments
     equal to and greater than the value of the startTimeComboBox.
     */
    public void onActionfromEndDate(ActionEvent event) {
        if(appStartTimeComboBox.getValue() == null){
            return;
        }

        if(appStartDatePicker.getValue() == null){
            return;
        }

        if(appEndDatePicker.getValue().compareTo(appStartDatePicker.getValue()) >0) {

            String keep_end_time = appEndTimeComboBox.getValue();

            TimeHandler.timeComboBoxFiller(appEndTimeComboBox, TimeHandler.getLocal_open_hour());

            if (keep_end_time != null) {
                appEndTimeComboBox.setValue(keep_end_time);
            }
        }else if (appStartDatePicker.getValue().equals(appEndDatePicker.getValue()) && appStartTimeComboBox != null) {
            String keep_end_time = appEndTimeComboBox.getValue();

            LocalTime start_time = LocalTime.parse(appStartTimeComboBox.getValue(), TimeHandler.hourMinAmPmFormatter);
            TimeHandler.timeComboBoxFiller(appEndTimeComboBox, start_time);

            if(keep_end_time != null && LocalTime.parse(keep_end_time, TimeHandler.hourMinAmPmFormatter).isAfter(start_time)) {
                appEndTimeComboBox.setValue(keep_end_time);
            }
        }
    }

    /** Method for ensuring all end time values are after start time values
     when a value is chosen in the appStartTimeComboBox. If the two dates are equal between
     both the startDate and endDate datePickers, the endTimeComboBox is populated with time increments equal to
     and greater than the value of the startTimeComboBox.
     */
    public void onActionFromStartTime(ActionEvent event) {
        if(appStartDatePicker.getValue() == null){
            return;
        }

        if (appStartDatePicker.getValue().equals(appEndDatePicker.getValue())) {

            LocalTime start_time = LocalTime.parse(appStartTimeComboBox.getValue(), TimeHandler.hourMinAmPmFormatter);

            String keep_end_time = appEndTimeComboBox.getValue();

            TimeHandler.timeComboBoxFiller(appEndTimeComboBox, start_time);

            if(keep_end_time != null && LocalTime.parse(keep_end_time, TimeHandler.hourMinAmPmFormatter).isAfter(start_time)){
                appEndTimeComboBox.setValue(keep_end_time);
            }
        }
    }
}
