package group6;

import group8.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Runyao Xia
 * @date: 2021/4/10
 */
public class FormActionDirectory {
    private Car car;
    private List<FormAction> forms;
    private Integer id = 1;

    public FormActionDirectory(Car car) {
        this.car = car;
    }

    public FormAction newForm(String firstName, String lastName, String eMail, String phoneNumber, String zipCode) {
        User user= new User(firstName, lastName, eMail, phoneNumber, zipCode);
        return newForm(user);
    }

    public FormAction newForm(User user) {
        FormAction form = new FormAction(user);
        form.setFd(this);
        form.setId(id++);
        return form;
    }

    public void submit(FormAction form) {
        if (forms == null) {
            forms = new ArrayList<>();
        }
        forms.add(form);
    } 

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<FormAction> getForms() {
        return forms;
    }

    public void generateForm(Car car) {
        LeadFormController leadFormController = new LeadFormController(car, this);
        leadFormController.showLeadForm();
    }
    /**
     * get user by form
     */


    /**
     * get form by user...
     */

    /**
     * find customer by car id or something
     */

    /**
     * if the form belongs to the user
     */

    /**
     * get user by name? or something
     */
}
