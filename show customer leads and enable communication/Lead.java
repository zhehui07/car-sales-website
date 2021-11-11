package group3;
public class Lead {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String zipCode;
    private String carModel;
    private String carColor;
    private String carStock;
    private String carVin;
    private String message;
    private String replyMessage;
    private boolean contacted;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getCarVin() {
        return carVin;
    }
    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }
    public String getCarStock() {
        return carStock;
    }

    public void setCarStock(String carStock) {
        this.carStock = carStock;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getReplyMessage() {
        return this.replyMessage;
    }

    public void setContacted(boolean contacted) {
        this.contacted = contacted;
    }

    public boolean getContacted() {
        return this.contacted;
    }


}
