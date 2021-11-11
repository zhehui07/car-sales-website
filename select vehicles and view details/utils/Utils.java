package group2.utils;


import group8.Car;
import group8.CarCategory;
import group8.Dealer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Utils {

    static public List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    static public Car transToCar(Map<String, Object> map) {
        Car car = new Car();
        car.setID(String.valueOf(map.getOrDefault("vehicle_id", "")));
        car.setMake(String.valueOf(map.getOrDefault("make_name", "")));
        car.setModel(String.valueOf(map.getOrDefault("model_name", "")));
        car.setYear(Integer.parseInt(String.valueOf(map.getOrDefault("year", 0))));
        car.setMSRP(Double.parseDouble(String.valueOf(map.getOrDefault("price", 0))));

        if ("New".equals(String.valueOf(map.get("category")))) {
            car.setCarCategory(CarCategory.NEW);
        } else if ("Used".equals(String.valueOf(map.get("category")))) {
            car.setCarCategory(CarCategory.USED);
        } else if ("Certified Pre-Owned".equals(String.valueOf(map.get("category")))) {
            car.setCarCategory(CarCategory.CERTIFIEDPREOWNED);
        }

        car.setColor(String.valueOf(map.getOrDefault("color", "")));
        car.setEngine(String.valueOf(map.getOrDefault("engine", "")));
        car.setTransmission(String.valueOf(map.getOrDefault("transmission", "")));
        car.setFuel(String.valueOf(map.getOrDefault("fuel", "")));
        car.setStockNum(String.valueOf(map.getOrDefault("stock", 0)));
        car.setSeatCount(Integer.parseInt(String.valueOf(map.getOrDefault("seat_count", 0))));
        car.setMileage(Integer.parseInt(String.valueOf(map.getOrDefault("miles", 0))));
        car.setRating(Integer.parseInt(String.valueOf(map.getOrDefault("rating", 0))));
        car.setDescription(String.valueOf(map.getOrDefault("description", "")));
        String urls = String.valueOf(map.getOrDefault("image_urls", "default_car_260.jpg"));
        final String[] arr = urls.split(",");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = "img/" + arr[i];
        }
        car.setImages(Arrays.asList(arr));
        return car;
    }

    private static String checkNull(String input) {
        if("null".equals(input)) {
            return "";
        }
        return input;
    }

    static public Car transToCarV2(Map<String, Object> map) {
        Car car = new Car();
        car.setID(checkNull(String.valueOf(map.getOrDefault("VehicleId", ""))));
        car.setMake(checkNull(String.valueOf(map.getOrDefault("Make", ""))));
        car.setModel(String.valueOf(map.getOrDefault("Model", "")));
        car.setYear(Integer.parseInt(String.valueOf(map.getOrDefault("Year", "0"))));
        car.setMSRP(Double.parseDouble(String.valueOf(map.getOrDefault("Price", ""))));
        if ("New".equals(String.valueOf(map.get("Category")))) {
            car.setCarCategory(CarCategory.NEW);
        } else if ("Used".equals(String.valueOf(map.get("Category")))) {
            car.setCarCategory(CarCategory.USED);
        } else if ("Certified Pre-Owned".equals(String.valueOf(map.get("Category")))) {
            car.setCarCategory(CarCategory.CERTIFIEDPREOWNED);
        }

        car.setColor(checkNull(String.valueOf(map.getOrDefault("Color", ""))));
        car.setEngine(checkNull(String.valueOf(map.getOrDefault("engine", ""))));
        car.setTransmission(checkNull(String.valueOf(map.getOrDefault("transmission", ""))));
        car.setFuel(checkNull(String.valueOf(map.getOrDefault("fuel", ""))));
        car.setVIN(checkNull(String.valueOf(map.getOrDefault("VIN", ""))));
        car.setStockNum(String.valueOf(map.getOrDefault("stock", 0)));
        car.setSeatCount(Integer.parseInt(String.valueOf(map.getOrDefault("seat_count", 0))));
        car.setMileage(Integer.parseInt(String.valueOf(map.getOrDefault("Miles", 0))));
        car.setRating(Integer.parseInt(String.valueOf(map.getOrDefault("Ratings", 0))));
        car.setDescription(checkNull(String.valueOf(map.getOrDefault("description", ""))));

        String urls = String.valueOf(map.getOrDefault("image_urls", ""));
        if(urls.equals(""))
            car.setImages(null);
        else {
            final String[] arr = urls.split(",");
            for (int i = 0; i < arr.length; i++) {
                arr[i] = "img/" + arr[i];
            }
            car.setImages(Arrays.asList(arr));
        }
        return car;
    }

    static public Dealer transToDealer(Map<String, Object> map) {
        Dealer dealer = new Dealer();
        dealer.setName(checkNull(String.valueOf(map.getOrDefault("DealerName", ""))));
        dealer.setStreetAddress(checkNull(String.valueOf(map.getOrDefault("DealerAddress", ""))));
        dealer.setCity(checkNull(String.valueOf(map.getOrDefault("City", ""))));
        dealer.setState(checkNull(String.valueOf(map.getOrDefault("State", ""))));
        dealer.setCountry(checkNull(String.valueOf(map.getOrDefault("Country", ""))));
        dealer.setPhoneNumber(checkNull(String.valueOf(map.getOrDefault("PhoneNumber", ""))));
        dealer.setZipcode(checkNull(String.valueOf(map.getOrDefault("ZipCode", ""))));
        return dealer;
    }


}
