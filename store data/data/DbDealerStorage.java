package group8.data;

import group8.Car;
import group8.CarCategory;
import group8.Dealer;
import group8.IDataProvider;
import group8.Incentive;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import group8.*;
import java.sql.Blob;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import javax.sql.rowset.serial.SerialBlob;
/**
 * @author Guiyu Liu Apr 13, 2021.
 */
public class DbDealerStorage implements IDataProvider {
    private NewJDBC db;
    public DbDealerStorage() throws SQLException, ClassNotFoundException {
        db = NewJDBC.getInstance();
    }
    public Dealer getDealerById(String id) {
        String sql = "select * from Dealer where dealerID = ?";
        try {
            String[] aStrParams = new String[1];
            aStrParams[0] = id;
            ResultSet resultSet = db.query(sql, aStrParams);
            return mapRow(resultSet);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("No result for Dealer with Id : " + id);
            return null;
        }
    }
    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
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
    public Dealer mapRow(ResultSet resultSet) throws SQLException {
        Dealer dealer = new Dealer();
        resultSet.next();
        dealer.setDealerID(resultSet.getString("DealerId"));
        dealer.setName(resultSet.getString("DealerName"));
        dealer.setStreetAddress(resultSet.getString("DealerAddress"));
        dealer.setCity(resultSet.getString("City"));
        dealer.setStateID(resultSet.getString("StateCode"));
        dealer.setState(resultSet.getString("State"));
        dealer.setCountry(resultSet.getString("Country"));
        dealer.setZipcode(resultSet.getString("ZipCode"));
        dealer.setPhoneNumber(resultSet.getString("PhoneNumber"));
        dealer.setLatitude(resultSet.getDouble("Latitude"));
        dealer.setLongitude(resultSet.getDouble("Longitude"));
        return dealer;
    }
    @Override
    public List<Car> getAllCarsByDealerId(String dealerId) {
        
        return db.getAllCarsByDealerId(dealerId);
    }
    @Override
    public List<Car> getAllCars() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
    @Override
    public HashMap<Car, List<Incentive>> getAllIncentivesByDealerId(String dealerId) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
    @Override
    public List<Incentive> getAllIncentives() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
    
    @Override
    public void persistIncentive(CashDiscountIncentive cashDiscountIncentive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persistIncentive(LoanIncentive loanIncentive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persistIncentive(LeasingIncentive leasingIncentive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persistIncentive(RebateIncentive rebateIncentive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
