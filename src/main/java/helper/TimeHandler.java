package helper;

import javafx.scene.control.ComboBox;

import java.time.*;
import java.time.format.DateTimeFormatter;

/** Abstract Class for handling operations related to time objects. */
public abstract class TimeHandler {

    /** ZonedDateTime member variable for establishing the opening hour of the business as 8:00am US/Eastern time.
     */
    public static final ZonedDateTime open_hour = ZonedDateTime.of(2023, 1, 1, 8,0,0,0, ZoneId.of("US/Eastern"));

    /** ZonedDateTime member variable for establishing the closing hour of the business as 10:00pm US/Eastern time.
     */
    public static final ZonedDateTime close_hour = ZonedDateTime.of(2023, 1, 1, 22,0,0,0, ZoneId.of("US/Eastern"));

    /** LocalTime member variable for converting the opening hour of the business to the timezone of the user's computer.
     */
    public static final LocalTime local_open_hour = LocalTime.ofInstant(open_hour.toInstant(), ZoneId.systemDefault());

    /** LocalTime member variable for converting the closing hour of the business to the timezone of the user's computer.
     */
    public static final LocalTime local_close_hour = LocalTime.ofInstant(close_hour.toInstant(), ZoneId.systemDefault());

    public static final DateTimeFormatter hourMinAmPmFormatter = DateTimeFormatter.ofPattern("hh:mma");

    /** DateTimeFormatter member variable for converting time objects to and from strings that are consistent with the database.
     */
    public static final DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** DateTimeFormatter member variable for converting time objects to strings for ease-of-reading display purposes in a TableView.
     */
    public static final DateTimeFormatter tableViewTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd     hh:mma");

    /** DateTimeFormatter member variable for converting the string display of DatePickers. Utilized to ensure
     date displays are consistent throughout the application.
     */
    public static final DateTimeFormatter datePickerTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /** Method for converting user input from different class types into a ZonedDateTime.
     @param date LocalDate from user input.
     @param am_pm_time String in "hh:mma" format from user input.
     @return A complete ZonedDateTime with the ZoneId of the user's system.*/
    public static ZonedDateTime localZonedDateTimeConverterFromInput(LocalDate date, String am_pm_time){
        LocalTime hour_min = LocalTime.parse(am_pm_time, hourMinAmPmFormatter);
        LocalDateTime inputStartDateTime = LocalDateTime.of(date, hour_min);
        return ZonedDateTime.of(inputStartDateTime, ZoneId.systemDefault());
    }


    /** Method for converting a ZonedDateTime object into a string that's consistent with the database.
     @param inputLocalZoneTime The ZonedDateTime object to be converted.
     @return A string representing time that's ready for database insertion.
     */
    public static String toUtcDateTimeString(ZonedDateTime inputLocalZoneTime){
        return dbDateTimeFormatter.format(inputLocalZoneTime.withZoneSameInstant(ZoneId.of("UTC")));
    }

    /**
     @return A String displaying the open business hours converted to the user's system time.
     */
    public static String getLocalBusinessHoursString(){
        return "(" + hourMinAmPmFormatter.format(local_open_hour) + " to " +
                hourMinAmPmFormatter.format(local_close_hour) + " Local Time)";
    }


    /** Method for filling ComboBox's with time increments of fifteen minutes. The last time in the
     ComboBox is always consistent with the closing hour of the business according to the user's
     system time.
     @param timeComboBox The ComboBox to be filled.
     @param earliest_time The first time to be inserted into the ComboBox and where the incrementation begins.
     */
    public static void timeComboBoxFiller(ComboBox<String> timeComboBox, LocalTime earliest_time) {

        timeComboBox.getItems().clear();

        while ((earliest_time.isBefore(local_close_hour.plusMinutes(1)))) {
            timeComboBox.getItems().add(hourMinAmPmFormatter.format(earliest_time));
            earliest_time = earliest_time.plusMinutes(15);
        }

        timeComboBox.setVisibleRowCount(Math.min(timeComboBox.getItems().size(), 10));
    }


    /** Method for converting a string from the database into a ZonedDateTime with the user system's Zone ID.
     @param dbUtcString String to be converted.
     @return ZonedDateTime object with the user's Zone ID.
     */
    public static ZonedDateTime localZdtFromUtcString(String dbUtcString){
        LocalDateTime time = LocalDateTime.parse(dbUtcString, dbDateTimeFormatter);
        ZonedDateTime utcZoneTime = ZonedDateTime.of(time, ZoneId.of("UTC"));
        return utcZoneTime.withZoneSameInstant(ZoneId.systemDefault());
    }

    /**
     @return A LocalTime object representing the opening hour of the business according to the user's time.
     */
    public static LocalTime getLocal_open_hour() {
        return LocalTime.ofInstant(open_hour.toInstant(), ZoneId.systemDefault());
    }



}
