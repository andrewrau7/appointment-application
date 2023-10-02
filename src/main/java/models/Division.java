package models;


/** Class for instantiating Division objects that hold division information from the database. */
public class Division {

    private String division_name;

    private int division_id;

    public Division(String division_name, int division_id) {
        this.division_name = division_name;
        this.division_id = division_id;
    }

    public String getDivision_name() {
        return division_name;
    }

    /**
     @return The unique Division ID.
     */
    public int getDivision_id() {
        return division_id;
    }


    /** This method overrides the toString function for ComboBox display purposes.
     @return The name of the Division as a String.
     */
    @Override
    public String toString(){
        return this.division_name;
    }
}
