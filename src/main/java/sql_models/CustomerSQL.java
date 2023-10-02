package sql_models;

import helper.JDBC;
import helper.TimeHandler;
import models.ActiveUser;
import models.Customer;
import models.CustomerTableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Abstract Class utilized for sending SQL commands to the database relating to customers. */
public abstract class CustomerSQL {


    /** This method sends an INSERT SQL command to the database to add a customer to the database from the application.
     A ZonedDateTime variable with a "UTC" zone id is utilized in order to properly update the Customer Table columns
     "Create_Date" and "Last_Update" with the correct time. The getUserName method is called from the ActiveUser abstract
     class to properly update the "Created_By" and "Last_Update_By" columns as well. Customer ID is omitted from
     the method as it is auto-generated in the database.
     */
    public static int insert(String customer_name, String customer_address, String postal_code, String phone_num, int division_id) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Create_Date, "
                + "Created_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        String currentTimeUtc = TimeHandler.toUtcDateTimeString(ZonedDateTime.now());

        ps.setString(1, customer_name);
        ps.setString(2, customer_address);
        ps.setString(3, postal_code);
        ps.setString(4, phone_num);
        ps.setString(5, currentTimeUtc);
        ps.setString(6, ActiveUser.getUserName());
        ps.setInt(7, division_id);

        return ps.executeUpdate();
    }


    /** This method sends an UPDATE SQL command to the database to update a customer in the database from the application.
     A ZonedDateTime variable with a "UTC" zone id is utilized in order to properly update the Customer Table column "Last_Update" with the correct time.
     The getUserName method is called from the ActiveUser abstract class to properly update the "Last_Update_By"
     column as well.
     @param custId Customer ID for finding the correct customer to be updated in the database.
     */
    public static int update(int custId, String customer_name, String customer_address, String postal_code, String phone_num, int division_id) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "
                     + "Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        String currentTimeUtc = TimeHandler.toUtcDateTimeString(ZonedDateTime.now());

        ps.setString(1, customer_name);
        ps.setString(2, customer_address);
        ps.setString(3, postal_code);
        ps.setString(4, phone_num);
        ps.setString(5, currentTimeUtc);
        ps.setString(6, ActiveUser.getUserName());
        ps.setInt(7, division_id);
        ps.setInt(8, custId);

        return ps.executeUpdate();
    }


    /** This method sends a SELECT SQL command to read customers from the database and write them
     to an ObservableList.
     A while loop is utilized to iterate through the ResultSet from the query and add customer objects with
     the Customer constructor to the allCustomers ObservableList in the CustomerTableList abstract class.
     */
    public static void selectAllCustomers() throws SQLException {
        CustomerTableList.clearCustomerTableList();
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Phone, customers.Postal_Code, first_level_divisions.Division, countries.Country\n" +
                "FROM customers\n" +
                "LEFT JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID\n" +
                "LEFT JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID;";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal_code = rs.getString("Postal_Code");
            String phone_number = rs.getString("Phone");
            String country_name = rs.getString("Country");
            String division_name = rs.getString("Division");

            CustomerTableList.addCustomer(new Customer(id, name, address, postal_code, phone_number, country_name, division_name));
        }
    }

    /** This method sends a DELETE SQL command to the database to delete a customer and all appointments
     related to the customer in the database from the application.
     @param custId the ID number of the specified customer along with their appointments to be deleted in the database. */
    public static void delete(int custId) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Customer_ID = ?";
        String sql_2 = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        PreparedStatement ps_2 = JDBC.getConnection().prepareStatement(sql_2);

        ps.setInt(1, custId);
        ps_2.setInt(1, custId);

        ps.executeUpdate();
        ps_2.executeUpdate();
    }
}


