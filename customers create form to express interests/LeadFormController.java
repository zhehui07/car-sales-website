package group6;

import group8.Car;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LeadFormController {

    Car car;
    FormActionDirectory formActionDirectory;

    public LeadFormController(Car car, FormActionDirectory formActionDirectory) {
        this.car = car;
        this.formActionDirectory = formActionDirectory;
    }

    public void showLeadForm() {
        LeadFormView formView = new LeadFormView(car, formActionDirectory);
        formView.controller = this;
        formView.setVisible(true);
    }

    /**
     * move the directory to mainTest class
     * @param user
     */
    public void submitLeadForm(User user) {
        //System.out.println(user.toString());

        FormAction formAction =  formActionDirectory.newForm(user);
        formActionDirectory.submit(formAction);
        System.out.println("There are " + formActionDirectory.getForms().size() + " customers");
        for (FormAction form: formActionDirectory.getForms()) {
            System.out.println("FirstName: " + form.getUser().getFirstName());
            System.out.println("LastName:" + form.getUser().getLastName());
            System.out.println("Email: " + form.getUser().geteMail());
            System.out.println("PhoneNum: " + form.getUser().getPhoneNumber());
            System.out.println("Zipcode: " + form.getUser().getZipCode());
            if (form.getUser().getOptional() != null) {
                System.out.println("UseType: " + form.getUser().getOptional().getUserType());
                System.out.println("Text Message: " + form.getUser().getOptional().getMessageText());
            }
        }
        System.out.println("-------------next user--------------");
    }

    /*public static void main(String[] args) {
        Car car = new Car("1",
                "BMW",
                "Q7",
                2022,
                9090.0,
                "Blue",
                "Bell",
                234,
                "12",
                NEW, 5,"3746534857",
                6, "Good",
                " Auto", "Auto", "Petrol", "Type");
        FormActionDirectory formActionDirectory = new FormActionDirectory(car);
        LeadFormController leadFormController = new LeadFormController(car, formActionDirectory);
        leadFormController.showLeadForm();

    }*/

}
