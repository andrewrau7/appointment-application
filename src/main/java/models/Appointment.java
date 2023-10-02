package models;

import java.time.ZonedDateTime;

/** Class for instantiating Appointment objects that hold appointment information from the database. */
public class Appointment {
    private int id;
    private String title;
    private String location;
    private String description;
    private String contact_name;

    private int contact_id;
    private String type;

    /** String member variable used to store an appointment's start date.
     Useful for displaying information in a table view. */
    private String  start_date;

    /** String member variable used to store an appointment's end date.
     Useful for displaying information in a table view. */
    private String end_date;
    private int customer_id;
    private int user_id;

    /** ZonedDateTime member variable used to store an appointment's start date.
     Useful for comparing potentially conflicting appointment times. */
    private ZonedDateTime start_date_with_zone;

    /** ZonedDateTime member variable used to store an appointment's end date.
     Useful for comparing potentially conflicting appointment times. */
    private ZonedDateTime end_date_with_zone;

    public Appointment(int id, String title, String location, String description, String contact_name, int contact_id, String type, String start_date, String end_date, int customer_id, int user_id, ZonedDateTime start_date_with_zone, ZonedDateTime end_date_with_zone) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.contact_name = contact_name;
        this.contact_id = contact_id;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.start_date_with_zone = start_date_with_zone;
        this.end_date_with_zone = end_date_with_zone;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }
    public int getCustomer_id() {
        return customer_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public int getContact_id() {
        return contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public ZonedDateTime getStart_date_with_zone() {
        return start_date_with_zone;
    }


    public ZonedDateTime getEnd_date_with_zone() {
        return end_date_with_zone;
    }


    /** Method for displaying key info relating to an appointment.
     Useful for conveying information relating to an upcoming appointment and an
     appointment with a scheduling conflict.
     @return The information String relating to the specific appointment.
     */
    public String displayInfo(){

        String appInfo = "Appointment ID: " + this.id + "\n"
                       + "Customer ID: " + this.customer_id + "\n"
                       + "Type: " + this.type + "\n"
                       + "Start Date/Time: " + this.start_date + "\n"
                       + "End Date/Time: " + this.end_date;
        return appInfo;
    }
}
