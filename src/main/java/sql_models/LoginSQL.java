package sql_models;

import helper.JDBC;
import javafx.scene.control.Alert;
import models.ActiveUser;
import models.Appointment;
import models.AppointmentTableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Objects;


/** Abstract Class utilized for sending SQL commands to the database relating to Login functionality */
public abstract class LoginSQL {

    /** This method sends a SELECT SQL command to the database and iterates through the subsequent
     ResultSet to check for a matching username and password combination in the database from user input.
     If a match is found, the username and id corresponding to the valid user is stored in the ActiveUser
     abstract class. If a match is not found, an exception is thrown for error handling.
     @param username The username string from input.
     @param password The password string from input.
     */
    public static void login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        rs.next();
        if (Objects.equals(password, rs.getString("Password"))) {
            String valid_user_name = rs.getString("User_Name");
            int valid_user_id = rs.getInt("User_ID");
            ActiveUser.login(valid_user_id, valid_user_name);
        }else{
            throw new SQLException();
        }

    }

    /** This method checks for upcoming appointments within fifteen minutes corresponding to the user
     that is logged in. First, all appointments are gathered from the database through the AppointmentSQL abstract
     class's "selectAllAppointments" method. The resulting ObservableList "allAppointments" from the AppointmentTableList
     abstract class is then iterated through to check if any appointment starts within fifteen minutes of the user's
     login time. Boolean variables are utilized for increased readability. Two different alerts are shown
     depending on if any appointments are scheduled within fifteen minutes or not.
     */
    public static void upcomingAppCheck(){
        AppointmentSQL.selectAllAppointments();

        boolean noUpcomingApp = true;

        for (Appointment appointment : AppointmentTableList.getAllAppointments()){
            boolean afterNow = appointment.getStart_date_with_zone().isAfter(ZonedDateTime.now());
            boolean before15minFromNow = appointment.getStart_date_with_zone().isBefore(ZonedDateTime.now().plusMinutes(15));
            boolean sameUser = (ActiveUser.getUserId() == appointment.getUser_id());
            if (afterNow && before15minFromNow && sameUser){
                noUpcomingApp = false;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Upcoming appointment within 15 minutes:\n\n" + appointment.displayInfo());
                alert.showAndWait();
            }

        }
        if (noUpcomingApp){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No upcoming appointments within 15 minutes.");
            alert.showAndWait();
        }
    }
}
