package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/** Class for holding several ObservableLists that contain Appointments for TableView purposes. */
public abstract class AppointmentTableList {


    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    private static ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

    private static ObservableList<Appointment> dateAppointments = FXCollections.observableArrayList();




    public static void addAppointmentToAll(Appointment appointment){
        allAppointments.add(appointment);
    }

    /**
     @return An ObservableList of all scheduled appointments.
     */
    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }

    public static void removeAppointmentFromAll(Appointment appointment){
        allAppointments.remove(appointment);
    }

    public static void clearAllAppointments(){
        allAppointments.clear();
    }

    /** Method for returning an ObservableList of appointments for a specific contact.
     @param contact_id The ID of the specified contact.
     @return The ObservableList of appointments that are scheduled with the specified contacts.
     */
    public static ObservableList<Appointment> getContactAppointments(int contact_id){
        contactAppointments.clear();

        for(Appointment appointment : allAppointments){
            if (appointment.getContact_id() == contact_id){
                contactAppointments.add(appointment);
            }
        }

        return contactAppointments;
    }

    /** Method for returning an ObservableList of appointments for a specific date.
     Appointments that are selected either start, end, or overlap with the date that
     is chosen. Booleans variables are implemented for ease of readability.
     @param date_picked The specific date for filtering.
     @return The ObservableList of appointments that are scheduled on the specified date.
     */
    public static ObservableList<Appointment> getDateAppointments(LocalDate date_picked){
        dateAppointments.clear();

        for (Appointment appointment : allAppointments){
            boolean sameAsStartDay = date_picked.isEqual((appointment.getStart_date_with_zone().toLocalDate()));
            boolean sameAsEndDay = date_picked.isEqual((appointment.getEnd_date_with_zone().toLocalDate()));
            boolean afterStartDay = date_picked.isAfter((appointment.getStart_date_with_zone().toLocalDate()));
            boolean beforeEndDay = date_picked.isBefore((appointment.getEnd_date_with_zone().toLocalDate()));

            if (sameAsStartDay || sameAsEndDay || (afterStartDay && beforeEndDay)){
                dateAppointments.add(appointment);
            }
        }
        return dateAppointments;
    }
}
