package sql_models;

import helper.JDBC;
import javafx.scene.control.ComboBox;
import models.Contact;
import models.Country;
import models.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

/** Abstract Class utilized for filling ComboBoxes with relevant data from the database. */
public abstract class ComboBoxFillerFromDbSQL {

    /** This method sends a SELECT SQL command to the database to select every contact and iterates through the
     subsequent ResulSet to fill a chosen ComboBox with Contact objects.
     @param contactComboBox The ComboBox to be filled.
     */
    public static void fillContactComboBox(ComboBox<Contact> contactComboBox) throws SQLException {
        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String contact_name = rs.getString("Contact_Name");
            int contact_id = rs.getInt("Contact_ID");
            contactComboBox.getItems().add(new Contact(contact_name, contact_id));
        }
    }

    /** This method sends a SELECT SQL command to the database to select every available user id and
     iterates through the subsequent ResultSet to fill a chosen ComboBox with all valid user id numbers.
     @param userIdComboBox The ComboBox to be filled. */
    public static void fillUserIdComboBox(ComboBox<Integer> userIdComboBox) throws SQLException {
        userIdComboBox.getItems().clear();
        String user_ID_sql = "SELECT USER_ID FROM USERS";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(user_ID_sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                userIdComboBox.getItems().add(rs.getInt("User_ID"));
        }
        userIdComboBox.getItems().sort(Comparator.naturalOrder());
    }

    /** This method sends a SELECT SQL command to the database to select every available customer id and
     iterates through the subsequent ResultSet to fill a chosen ComboBox with all valid customer id numbers.
     @param custIdComboBox The ComboBox to be filled. */
    public static void fillCustIdComboBox(ComboBox<Integer> custIdComboBox) throws SQLException {
        custIdComboBox.getItems().clear();
        String cust_ID_sql = "SELECT CUSTOMER_ID FROM CUSTOMERS ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(cust_ID_sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
                custIdComboBox.getItems().add(rs.getInt("Customer_ID"));
        }
        custIdComboBox.getItems().sort(Comparator.naturalOrder());
    }

    /** This method sends a SELECT SQL command to the database to select every country and iterates through the
     subsequent ResulSet to fill a chosen ComboBox with Country objects.
     @param countryComboBox The ComboBox to be filled.
     */
    public static void fillCountryComboBox(ComboBox<Country> countryComboBox) throws SQLException {
        countryComboBox.getItems().clear();
        String sql = "SELECT Country, Country_ID FROM COUNTRIES";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String country_name = rs.getString("Country");
            int country_id = rs.getInt("Country_ID");
            countryComboBox.getItems().add(new Country(country_name, country_id));
        }
    }

    /** This method sends a SELECT SQL command to the database to select every division relevant
     to a specific country and iterates through the subsequent ResulSet to fill a chosen
     ComboBox with Division objects.
     @param divisionComboBox The ComboBox to be filled.
     @param country_id The id number of the country to filter divisions with.
     */
    public static void fillDivisionComboBox(ComboBox<Division> divisionComboBox, int country_id) throws SQLException {
        divisionComboBox.getItems().clear();
        String sql = "SELECT Division, Division_ID FROM FIRST_LEVEL_DIVISIONS WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, country_id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String division_name = rs.getString("Division");
            int division_id = rs.getInt("Division_ID");
            divisionComboBox.getItems().add(new Division(division_name, division_id));
        }
    }

}
