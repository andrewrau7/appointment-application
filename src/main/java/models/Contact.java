package models;

/** Class for instantiating Contact objects that hold contact information from the database. */
public class Contact {

    private String contact_name;
    private int contact_id;

    public Contact(String contact_name, int contact_id) {
        this.contact_name = contact_name;
        this.contact_id = contact_id;
    }

    /**
     @return the unique Contact ID.
     */
    public int getContact_id() {
        return contact_id;
    }

    /** This method overrides the toString function for ComboBox display purposes.
     @return The name of the Contact as a string.
     */
    @Override
    public String toString(){
        return contact_name;
    }
}
