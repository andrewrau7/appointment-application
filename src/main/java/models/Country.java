package models;

import java.sql.SQLData;

/** Class for instantiating Country objects that hold country information from the database. */
public class Country {

    private String countryName;

    private int countryId;

    public Country(String countryName, int countryId) {
        this.countryName = countryName;
        this.countryId = countryId;
    }


    public String getCountryName() {
        return countryName;
    }


    /**
     @return the unique Country ID. */
    public int getCountryId() {
        return countryId;
    }


    /** This method overrides the toString function for ComboBox display purposes.
     @return The name of the Country as a String.
     */
    @Override
    public String toString(){
        return this.countryName;
    }
}
