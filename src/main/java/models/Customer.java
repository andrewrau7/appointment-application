package models;


/** Class for instantiating Customer objects that hold customer information from the database. */
public class Customer {

    private int id;

    private String name;

    private String address;

    private String postal_code;

    private String phone_number;

    private String country_name;

    private String division_name;

    private int num_appointments;

    public Customer(int id, String name, String address, String postal_code, String phone_number, String country_name, String division_name) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.phone_number = phone_number;
        this.country_name = country_name;
        this.division_name = division_name;
        this.num_appointments = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPostal_code() {
        return postal_code;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public String getCountry_name() {
        return country_name;
    }
    public String getDivision_name() {
        return division_name;
    }

    public int getNum_appointments(){return num_appointments;}


    public void incrementNum_appointments(){this.num_appointments++;}

    /** Method for returning key customer information in a string.
     @return The Customer ID and Name in one string.
     */
    public String displayInfo(){
        return "Customer ID: " + this.id + "\n"
                +"Customer Name: " + this.name;
    }
}
