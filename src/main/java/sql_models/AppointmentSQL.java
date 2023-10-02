package sql_models;

import helper.JDBC;
import helper.TimeHandler;
import models.ActiveUser;
import models.Appointment;
import models.AppointmentTableList;

import java.sql.*;
import java.time.ZonedDateTime;

/** Abstract Class utilized for sending SQL commands to the database relating to appointments. */
public abstract class AppointmentSQL {

    /** This method sends an INSERT SQL command to the database to add an appointment to the database from the application.
     A ZonedDateTime variable with a "UTC" zone id is utilized in order to properly update the Appointment Table columns
     "Create_Date" and "Last_Update" with the correct time. The getUserName method is called from the ActiveUser abstract
     class to properly update the "Created_By" and "Last_Update_By" columns as well. Appointment ID is omitted from
     the method as it is auto-generated in the database.
     */
    public static void insert(String title, String description, String location, String type, String start_date, String end_date, int custID, int userID, int contactID) throws SQLException {

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End," +
                " Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        String currentTimeUtc = TimeHandler.toUtcDateTimeString(ZonedDateTime.now());

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, start_date);
        ps.setString(6, end_date);
        ps.setString(7, currentTimeUtc);
        ps.setString(8, ActiveUser.getUserName());
        ps.setString(9, currentTimeUtc);
        ps.setString(10, ActiveUser.getUserName());
        ps.setInt(11, custID);
        ps.setInt(12, userID);
        ps.setInt(13, contactID);

        ps.executeUpdate();

    }


    /** This method sends an UPDATE SQL command to the database to update an appointment in the database from the application.
     A ZonedDateTime variable with a "UTC" zone id is utilized in order to properly update the Appointment Table column "Last_Update" with the correct time.
     The getUserName method is called from the ActiveUser abstract class to properly update the "Last_Update_By"
     column as well.
     @param appId Appointment ID for finding the correct appointment to be updated in the database.
     */
    public static void update(int appId, String title, String description, String location, String type, String start_date, String end_date, int custID, int userID, int contactID) throws SQLException {

        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        String currentTimeUtc = TimeHandler.toUtcDateTimeString(ZonedDateTime.now());

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, start_date);
        ps.setString(6, end_date);
        ps.setString(7, currentTimeUtc);
        ps.setString(8, ActiveUser.getUserName());
        ps.setInt(9, custID);
        ps.setInt(10, userID);
        ps.setInt(11, contactID);
        ps.setInt(12, appId);

        ps.executeUpdate();
    }


    /** This method sends a DELETE SQL command to the database to delete an appointment in the database from the application.
     @param appId the ID number of the specified appointment to be deleted in the database. */
    public static void delete(int appId) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setInt(1, appId);

        ps.executeUpdate();

    }

    /** This method sends a SELECT SQL command to read appointments from the database and write them
     to an ObservableList.
     A while loop is utilized to iterate through the ResultSet from the query and add appointment objects with
     the Appointment constructor to the allAppointments ObservableList in the AppointmentTableList abstract class.
     TimeHandler methods are called in order to convert the datetime strings from the database into ZonedDateTimes and local datetime strings
     according to the user computer's local time zone.
     */
    public static void selectAllAppointments() {
        AppointmentTableList.clearAllAppointments();

        String sql = "SELECT appointments.*, contacts.Contact_Name, contacts.Contact_ID\n" +
                "FROM appointments\n" +
                "LEFT JOIN contacts ON contacts.Contact_ID = appointments.Contact_ID;";
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                String contact_name = rs.getString("Contact_Name");
                int contact_id = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                ZonedDateTime zone_start_date_time = TimeHandler.localZdtFromUtcString(rs.getString("Start"));
                ZonedDateTime zone_end_date_time = TimeHandler.localZdtFromUtcString(rs.getString("End"));
                int customer_id = rs.getInt("Customer_ID");
                int user_id = rs.getInt("User_ID");

                String start_date_time_string = TimeHandler.tableViewTimeFormatter.format(zone_start_date_time);
                String end_date_time_string = TimeHandler.tableViewTimeFormatter.format(zone_end_date_time);

                AppointmentTableList.addAppointmentToAll(new Appointment(id, title, location, description, contact_name, contact_id, type, start_date_time_string, end_date_time_string, customer_id, user_id, zone_start_date_time, zone_end_date_time));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


