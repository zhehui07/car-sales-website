package group2.dao;

import group2.utils.Utils;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group8.Dealer;
import group8.data.NewJDBC;
import group8.Car;

public class VehicleDAO {

    private NewJDBC newJDBC;

    public VehicleDAO() {
        try {
            this.newJDBC = NewJDBC.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getById(int id) throws SQLException {
//        final ResultSet query = newJDBC.query("select * from vehicle_test vt inner join model_test mot on vt.model_id = mot.model_id\n" +
//                "inner join make_test mat on vt.make_id = mat.make_id inner join Dealer d on vt.dealer_id =\n" +
//                "d.DealerId\n" +
//                "where vehicle_id = ?", new String[]{String.valueOf(id)});
        final ResultSet query = newJDBC.query("select * from NewVehicleData nwd inner " +
                "join Dealer d on nwd.DealerId = d.DealerId  where VehicleId = ?", new String[]{String.valueOf(id)});
        List<Map<String, Object>> res = Utils.resultSetToList(query);
//        Car car = Utils.transToCar(res.get(0));
        return res;
    }

    public static void main(String[] args) throws SQLException {
        VehicleDAO vehicleDAO = new VehicleDAO();
        final List<Map<String, Object>> res = vehicleDAO.getById(1);
        Car car = Utils.transToCarV2(res.get(0));
        Dealer dealer = Utils.transToDealer(res.get(0));
        System.out.println(car);
        System.out.println(dealer);
    }
}