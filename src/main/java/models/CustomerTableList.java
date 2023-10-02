package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class for holding an ObservableLists that contains Customers for TableView purposes. */
public abstract class CustomerTableList {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    private static ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    public static void addCustomer(Customer customer){
        allCustomers.add(customer);
    }

    public static void addFilteredCustomer(Customer customer){filteredCustomers.add(customer);};

    /**
     @return An ObservableList of all Customers.
     */
    public static ObservableList<Customer> getAllCustomers(){
        return allCustomers;
    }

    /**
     @return An ObservableList of filtered Customers.
     */
    public static ObservableList<Customer> getFilteredCustomers(){return filteredCustomers;}

    public static void clearCustomerTableList(){
        allCustomers.clear();
    }

    public static void clearFilteredCustomerList(){filteredCustomers.clear();}

    public static void removeCustomer(Customer customer){
        allCustomers.remove(customer);
    }

}
