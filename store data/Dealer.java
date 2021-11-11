package group8;

public class Dealer {
    private String dealerID;
    private String name;
    private String streetAddress;
    private String city;
    private String stateID;
    private String state;
    private String zipcode;
    private String phoneNumber;
    private String country;
    private double latitude;
    private double longitude;

    public Dealer() {
    }

    public Dealer(String dealerID, String name, String streetAddress, String city, String stateID, String state,
                  String zipcode, String phoneNumber, String country, Inventory inventory) {
        this.dealerID = dealerID;
        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.stateID = stateID;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.country = country;
    }

    /*******************  GETTERS AND SETTERS  *******************/
    public String getDealerID() {
        return dealerID;
    }

    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /*******************  METHODS  *******************/

}
