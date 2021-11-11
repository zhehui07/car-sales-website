package group6;

import group8.*;

import static group8.CarCategory.NEW;

/**
 * @author Runyao Xia
 * @date: 2021/4/10
 */
public class MainTest {
    static Car car = new Car("1",
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
    static FormActionDirectory formActionDirectory = new FormActionDirectory(car);
    public static void main(String[] args) {
     /*Car car = new Car("1",
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
        FormActionDirectory contactUs = new FormActionDirectory(car);
        FormAction form = contactUs.newForm("JiaQi", "Chen", "dhasjdh@neu.com", "9900", "321");
        User user = form.getUser();
        System.out.println(user.geteMail());
        System.out.println(user.getUseType());
        user.setUseType("1");
        System.out.println(user.getUseType());
        System.out.println(user.getForm().getId());*/

        formActionDirectory.generateForm(car);

    }
}
