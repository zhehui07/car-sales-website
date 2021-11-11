package group8;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jasmineshin
 */
public class Inventory {
    
    private Dealer dealer;
    private ArrayList<Car> carList;
    
    public Inventory(Dealer d){
        carList = new ArrayList<>();
        dealer = d;
    }
    
    public void addCar(Car c){
        carList.add(c);
    }
    
    public String toString(){
        String res = "Displaying Inventory for " + dealer.getDealerID() + "\n";
        if(carList.isEmpty()){
            return res + "Dealer ID " + dealer.getDealerID() + "'s inventory is empty\n";
        }
        for(Car c: carList){
            res += "Stock ID: " + c.getstockNum() + ", Car Make: " + c.getMake() + "\n";
        }
        return res;
    }
}
