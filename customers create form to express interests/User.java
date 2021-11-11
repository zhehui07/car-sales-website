package group6;

/**
 * @author Anupama Singh
 * @date: 04/09/2021
 */
public class User {
    private String firstName, lastName, eMail, phoneNumber, zipCode;
    private FormAction form;
    // optional
    private LeadModel optional;
    /*private String useType;
    private String messageText;*/

    public User(String firstName, String lastName, String eMail, String phoneNumber, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setForm(FormAction formAction) {
        this.form = formAction;
    }

    public FormAction getForm() {
        return form;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public LeadModel getOptional() {
        return optional;
    }

    public void setOptional(LeadModel optional) {
        this.optional = optional;
    }
    /*// optional
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    // optional
    public void setUseType(String useType) {
        this.useType = useType;
    }


    public String getUseType() {
        if (this.useType == null) {
            return "";
        }
        return useType == "" ? "Business" : "Personal";
    }*/

    /*@Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", eMail='" + eMail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", form=" + form +
                ", useType='" + useType + '\'' +
                ", messageText='" + messageText + '\'' +
                '}';
    }*/
}
