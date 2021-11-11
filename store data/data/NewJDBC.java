package group8.data;

import group8.Car;
import group8.CarCategory;
import group8.CashDiscountIncentive;
import group8.CashDiscountType;
import group8.IDataProvider;
import group8.Incentive;
import group8.IncentiveType;
import group8.LeasingIncentive;
import group8.LoanIncentive;
import group8.RebateIncentive;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;

/**
 * @author Guiyu Liu
 * Apr 13, 2021.
 */
public class NewJDBC implements IDataProvider{
    private static final String URL
            = "jdbc:sqlserver://guiyu.database.windows.net:1433;databaseName=test";
    private static final String USERNAME = "guiyu"; //For personal test, will replace it later
    private static final String PASSWORD = "2021test**";

    private Connection conn;
    private static NewJDBC _instance;
    private Statement stmt;

    static public NewJDBC getInstance() throws SQLException, ClassNotFoundException {
        if (_instance == null) {
            _instance = new NewJDBC();
            _instance.createRequiredTablesIfDoesNotExists();
        }
        return _instance;
    }

    private NewJDBC() throws SQLException, ClassNotFoundException {
        //Need to download Microsoft JDBC Driver for SQL Server
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        this.stmt = conn.createStatement();
    }
    
    private void createRequiredTablesIfDoesNotExists() throws SQLException {
        this.createIncentiveTableIfDoesnotExisit();
        this.createIncentiveVINsTableIfDoesnotExisit();
        this.createIncentiveRebatesTableIfDoesnotExisit();
    }
    
    private void createIncentiveTableIfDoesnotExisit() throws SQLException {
        this.stmt.execute("if not exists (select * from sysobjects where name='Incentive' and xtype='U')" +
                "CREATE TABLE dbo.Incentive (" +
                "id VARCHAR(40) PRIMARY KEY NOT NULL," +
                "incentiveType VARCHAR(20) NOT NULL," +
                "dealerId VARCHAR(40) NOT NULL," +
                "startDate DATE NOT NULL," +
                "endDate DATE NOT NULL," +
                "title VARCHAR(MAX) NOT NULL," +
                "description VARCHAR(MAX) NOT NULL," +
                "disclaimer VARCHAR(MAX) NOT NULL," +
                "carVinUUID VARCHAR(40) NOT NULL," +
                "cashDiscountType VARCHAR(20) DEFAULT(NULL)," +
                "discountValue DECIMAL DEFAULT(NULL)," +
                "apr DECIMAL DEFAULT(NULL)," +
                "loanmonths INT DEFAULT(NULL)," +
                "leasemonths INT DEFAULT(NULL)," +
                "signingPay DECIMAL DEFAULT(NULL)," +
                "monthlyPay DECIMAL DEFAULT(NULL)," +
                "rebateMapUUID VARCHAR(40) DEFAULT(NULL))");
    }
    
        private void createIncentiveVINsTableIfDoesnotExisit() throws SQLException {
        this.stmt.execute("if not exists (select * from sysobjects where name='IncentiveVINs' and xtype='U')" +
                "CREATE TABLE dbo.IncentiveVINs (" +
                "incentiveVinID VARCHAR(40) NOT NULL," +
                "carVIN VARCHAR(40) NOT NULL)");
    }

    private void createIncentiveRebatesTableIfDoesnotExisit() throws SQLException {
        this.stmt.execute("if not exists (select * from sysobjects where name='IncentiveRebates' and xtype='U')" +
                "CREATE TABLE dbo.IncentiveRebates (" +
                "rebateID VARCHAR(40) NOT NULL," +
                "rebateType VARCHAR(40) NOT NULL," +
                "rebateValue DECIMAL NOT NULL)");
    }
    
   public String applyDiscount(Incentive incentive, Car c){
        String output = "";
        if(incentive instanceof CashDiscountIncentive){
            double newPrice = c.getMSRP() - ((CashDiscountIncentive)incentive).getValue();
            output += "$ " + newPrice;
        }else if(incentive instanceof LeasingIncentive){
            output += "Down Payment: $ " + ((LeasingIncentive)incentive).getSigningPay();
            output += "\nMontly Payment: $ " + ((LeasingIncentive)incentive).getMonthlyPay();
            output += "\nFor " + ((LeasingIncentive)incentive).getMonths() + " months";
        }else if(incentive instanceof LoanIncentive){
            output += "Loan Incentive special APR: " + ((LoanIncentive)incentive).getApr() + "%";
            output += "\nFor " + ((LoanIncentive)incentive).getMonths() + " months";
        }else if(incentive instanceof RebateIncentive){
            HashMap<String, Double> rebateMap = ((RebateIncentive)incentive).getRebateMap();
            for(String key: rebateMap.keySet()){
                double newPrice = c.getMSRP() - rebateMap.get(key);
                output += "Discount Price For " + key + ": $" + newPrice + "\n";
            }
        }
        return output;
    }
    
    public List<Incentive> getAllIncentiveByCarVIN(String carVIN) throws SQLException{
        List<Incentive> res = new ArrayList<>();
        res.addAll(this.getAllCashDiscountIncentivesByCarVIN(carVIN));
        res.addAll(this.getAllLeasingIncentivesByCarVIN(carVIN));
        res.addAll(this.getAllLoanIncentivesByCarVIN(carVIN));
        res.addAll(this.getAllRebateIncentivesByCarVIN(carVIN));
        return res;
    }
    
    public List<CashDiscountIncentive> getAllCashDiscountIncentivesByCarVIN(String carVIN) throws SQLException {
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Discount Incentive')\n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE IncentiveVINs.carVIN = '"+ carVIN +"'";

        List<CashDiscountIncentive> cashDiscountIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            CashDiscountType cashDiscountType = CashDiscountType.fromString(rs.getString("cashDiscountType"));
            double discountValue = rs.getInt("discountValue");
            cashDiscountIncentiveList.add(new CashDiscountIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, discountValue, cashDiscountType));
        }

        return cashDiscountIncentiveList;
    }
    
    public List<LoanIncentive> getAllLoanIncentivesByCarVIN(String carVIN) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Loan Incentive')\n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE IncentiveVINs.carVIN = '"+ carVIN +"'";

        List<LoanIncentive> loanIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            double apr = rs.getDouble("apr");
            int months = rs.getInt("loanmonths");
            loanIncentiveList.add(new LoanIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, apr, months));
        }

        return loanIncentiveList;
    }
    
    public List<RebateIncentive> getAllRebateIncentivesByCarVIN(String carVIN) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Rebate Incentive') \n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "JOIN dbo.IncentiveRebates AS IncentiveRebates\n" +
                "ON Incentive.rebateMapUUID = IncentiveRebates.rebateID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE IncentiveVINs.carVIN = '" + carVIN + "'";

        List<RebateIncentive> rebateIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            HashMap rebateMap = new HashMap<String, Double>() {
                {
                    put(rs.getString("rebateType"), rs.getDouble("rebateValue"));
                }
            };
            rebateIncentiveList.add(new RebateIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, rebateMap));
        }

        return rebateIncentiveList;
    }

    public List<LeasingIncentive> getAllLeasingIncentivesByCarVIN(String carVIN) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Lease Incentive')\n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE IncentiveVINs.carVIN = '"+ carVIN +"'";

        List<LeasingIncentive> leasingIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            int leasemonths = rs.getInt("leasemonths");
            double signingPay = rs.getDouble("signingPay");
            double monthlyPay = rs.getDouble("monthlyPay");
            leasingIncentiveList.add(new LeasingIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, leasemonths, signingPay, monthlyPay));
        }

        return leasingIncentiveList;
    }
    
    @Override
    public HashMap<Car, List<Incentive>> getAllIncentivesByDealerId(String dealerId){
        // get cars and filter res by cars in current dealer
        List<Car> owned = getAllCarsByDealerId(dealerId);
        HashMap<Car, List<Incentive>> carIncentiveMap = new HashMap<>();
        for(Car c: owned){
            try {
                List<Incentive> carIncentive = new ArrayList<>();
                carIncentive.addAll(this.getAllCashDiscountIncentivesByCarVIN(c.getVIN()));
                carIncentive.addAll(this.getAllLeasingIncentivesByCarVIN(c.getVIN()));
                carIncentive.addAll(this.getAllRebateIncentivesByCarVIN(c.getVIN()));
                carIncentive.addAll(this.getAllLoanIncentivesByCarVIN(c.getVIN()));
                carIncentiveMap.put(c, carIncentive);
            } catch (SQLException ex) {
                Logger.getLogger(NewJDBC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return carIncentiveMap;
    }
    
    // get all cars by dealer Id
    // this will be used by the UI team for displaying cars
    // after the customer selected a dealer to view

    /**
     *
     * @param dealerId
     * @return
     * @throws SQLException
     */
    @Override
    public List<Car> getAllCarsByDealerId(String dealerId){
        List<Car> res = new ArrayList<>();
        try {
            String query = "Select * from NewVehicleData where DealerId = " + dealerId;
            ResultSet resultSet = this.stmt.executeQuery(query);
            while(resultSet.next()){
                String stockID = resultSet.getString(1);
                String VIN = resultSet.getString(2);
                String dealerID = resultSet.getString(3);
                String make = resultSet.getString(4);
                String model = resultSet.getString(5);
                int year = resultSet.getInt(6);
                CarCategory category = resultSet.getString(7).equals("New") ? CarCategory.NEW : CarCategory.USED;
                double msrp = resultSet.getDouble(8);
                String color = resultSet.getString(9);
                int miles = resultSet.getInt(10);
                String[] img = resultSet.getString(15).split(",");
                List<String> images = new ArrayList<>();
                for(String i: img){
                    images.add(i);
                }
                String incentiveId = resultSet.getString(12);
                String discountPrice = resultSet.getString(13);
                int rating = resultSet.getInt(14);
                String engine = resultSet.getString(16);
                String description = resultSet.getString(17);
                String transmission = resultSet.getString(18);
                int seat = resultSet.getInt(20);
                String fuel = resultSet.getString(21);

                Car c = new Car(stockID, VIN, dealerID, make, model, year, category, msrp, color, miles, images,
                        incentiveId, discountPrice, rating, engine, description, transmission, null, seat, fuel);
                res.add(c);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NewJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public List<LoanIncentive> getAllLoanIncentivesByDealerId(String dealerID) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Loan Incentive')\n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE Incentive.dealerId = '"+ dealerID +"'";

        List<LoanIncentive> loanIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            double apr = rs.getDouble("apr");
            int months = rs.getInt("loanmonths");
            loanIncentiveList.add(new LoanIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, apr, months));
        }

        return loanIncentiveList;
    }
    
    public List<RebateIncentive> getAllRebateIncentivesByDealerId(String dealerID) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Rebate Incentive') \n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "JOIN dbo.IncentiveRebates AS IncentiveRebates\n" +
                "ON Incentive.rebateMapUUID = IncentiveRebates.rebateID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE Incentive.dealerId = '" + dealerID + "'";

        List<RebateIncentive> rebateIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            HashMap rebateMap = new HashMap<String, Double>() {
                {
                    put(rs.getString("rebateType"), rs.getDouble("rebateValue"));
                }
            };
            rebateIncentiveList.add(new RebateIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, rebateMap));
        }

        return rebateIncentiveList;
    }
    
    public List<LeasingIncentive> getAllLeasingIncentivesByDealerId(String dealerID) throws SQLException{
        String sql = "SELECT * FROM \n" +
                "\t(SELECT * FROM dbo.Incentive\n" +
                "\tWHERE incentiveType = 'Lease Incentive')\n" +
                "AS Incentive\n" +
                "JOIN dbo.IncentiveVINs AS IncentiveVINs\n" +
                "ON Incentive.carVinUUID = IncentiveVINs.incentiveVinID\n" +
                "AND GETDATE() BETWEEN Incentive.startDate AND Incentive.endDate\n" +
                "WHERE Incentive.dealerId = '"+ dealerID +"'";

        List<LeasingIncentive> leasingIncentiveList = new ArrayList<>();

        ResultSet rs = this.stmt.executeQuery(sql);
        while(rs.next()){
            // Retrieve by column name
            String id  = rs.getString("id");
            String incentiveType = rs.getString("incentiveType");
            String dealerId = rs.getString("dealerId");
            Date startDate = rs.getDate("startDate");
            Date endDate = rs.getDate("endDate");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String disclaimer = rs.getString("disclaimer");
            HashSet carVINs = new HashSet<>(Arrays.asList(rs.getString("carVIN")));
            int leasemonths = rs.getInt("leasemonths");
            double signingPay = rs.getDouble("signingPay");
            double monthlyPay = rs.getDouble("monthlyPay");
            leasingIncentiveList.add(new LeasingIncentive(id, dealerId, startDate, endDate, title, description, disclaimer, carVINs, leasemonths, signingPay, monthlyPay));
        }

        return leasingIncentiveList;
    }

    @Override
    public void persistIncentive(LoanIncentive loanDiscountIncentive) {
        String carVinUUID = UUID.randomUUID().toString();
        String sql = "INSERT INTO Incentive(id, incentiveType, dealerId, startDate, endDate, title, description, disclaimer, carVinUUID, " +
                "apr, loanmonths) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, loanDiscountIncentive.getId());
            preparedStatement.setString(2, loanDiscountIncentive.getIncentiveType().toString());
            preparedStatement.setString(3, loanDiscountIncentive.getDealerId());
            preparedStatement.setDate(4, new Date(loanDiscountIncentive.getStartDate().getTime()));
            preparedStatement.setDate(5, new Date(loanDiscountIncentive.getEndDate().getTime()));
            preparedStatement.setString(6, loanDiscountIncentive.getTitle());
            preparedStatement.setString(7, loanDiscountIncentive.getDescription());
            preparedStatement.setString(8, loanDiscountIncentive.getDisclaimer());
            preparedStatement.setString(9, carVinUUID);
            preparedStatement.setDouble(10, loanDiscountIncentive.getApr());
            preparedStatement.setInt(11, loanDiscountIncentive.getMonths());
            preparedStatement.execute();

            sql = "INSERT INTO IncentiveVINs(incentiveVinID, carVIN) VALUES (?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            for (String carVIN : loanDiscountIncentive.getCarVINList()) {
                preparedStatement.setString(1, carVinUUID);
                preparedStatement.setString(2, carVIN);
                preparedStatement.execute();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void persistIncentive(LeasingIncentive leasingIncentive) {
        String carVinUUID = UUID.randomUUID().toString();
        String sql = "INSERT INTO Incentive(id, incentiveType, dealerId, startDate, endDate, title, description, disclaimer, carVinUUID, " +
                "leasemonths, signingPay, monthlyPay) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, leasingIncentive.getId());
            preparedStatement.setString(2, leasingIncentive.getIncentiveType().toString());
            preparedStatement.setString(3, leasingIncentive.getDealerId());
            preparedStatement.setDate(4, new Date(leasingIncentive.getStartDate().getTime()));
            preparedStatement.setDate(5, new Date(leasingIncentive.getEndDate().getTime()));
            preparedStatement.setString(6, leasingIncentive.getTitle());
            preparedStatement.setString(7, leasingIncentive.getDescription());
            preparedStatement.setString(8, leasingIncentive.getDisclaimer());
            preparedStatement.setString(9, carVinUUID);
            preparedStatement.setInt(10, leasingIncentive.getMonths());
            preparedStatement.setDouble(11, leasingIncentive.getSigningPay());
            preparedStatement.setDouble(12, leasingIncentive.getMonthlyPay());
            preparedStatement.execute();

            sql = "INSERT INTO IncentiveVINs(incentiveVinID, carVIN) VALUES (?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            for (String carVIN : leasingIncentive.getCarVINList()) {
                preparedStatement.setString(1, carVinUUID);
                preparedStatement.setString(2, carVIN);
                preparedStatement.execute();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void persistIncentive(RebateIncentive rebateIncentive) {
        String carVinUUID = UUID.randomUUID().toString();
        String rebateMapUUID = UUID.randomUUID().toString();
        String sql = "INSERT INTO Incentive(id, incentiveType, dealerId, startDate, endDate, title, description, disclaimer, carVinUUID, " +
                "rebateMapUUID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, rebateIncentive.getId());
            preparedStatement.setString(2, rebateIncentive.getIncentiveType().toString());
            preparedStatement.setString(3, rebateIncentive.getDealerId());
            preparedStatement.setDate(4, new Date(rebateIncentive.getStartDate().getTime()));
            preparedStatement.setDate(5, new Date(rebateIncentive.getEndDate().getTime()));
            preparedStatement.setString(6, rebateIncentive.getTitle());
            preparedStatement.setString(7, rebateIncentive.getDescription());
            preparedStatement.setString(8, rebateIncentive.getDisclaimer());
            preparedStatement.setString(9, carVinUUID);
            preparedStatement.setString(10, rebateMapUUID);
            preparedStatement.execute();

            sql = "INSERT INTO IncentiveVINs(incentiveVinID, carVIN) VALUES (?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            for (String carVIN : rebateIncentive.getCarVINList()) {
                preparedStatement.setString(1, carVinUUID);
                preparedStatement.setString(2, carVIN);
                preparedStatement.execute();
            }

            sql = "INSERT INTO IncentiveRebates(rebateID, rebateType, rebateValue) VALUES (?, ?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            for (Map.Entry<String, Double> mapElement : rebateIncentive.getRebateMap().entrySet()) {
                preparedStatement.setString(1, rebateMapUUID);
                preparedStatement.setString(2, mapElement.getKey());
                preparedStatement.setDouble(3, mapElement.getValue());
                preparedStatement.execute();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void persistIncentive(CashDiscountIncentive cashDiscountIncentive) {
        String carVinUUID = UUID.randomUUID().toString();
        String sql = "INSERT INTO Incentive(id, incentiveType, dealerId, startDate, endDate, title, description, disclaimer, carVinUUID, " +
                "cashDiscountType, discountValue) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashDiscountIncentive.getId());
            preparedStatement.setString(2, cashDiscountIncentive.getIncentiveType().toString());
            preparedStatement.setString(3, cashDiscountIncentive.getDealerId());
            preparedStatement.setDate(4, new java.sql.Date(cashDiscountIncentive.getStartDate().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(cashDiscountIncentive.getEndDate().getTime()));
            preparedStatement.setString(6, cashDiscountIncentive.getTitle());
            preparedStatement.setString(7, cashDiscountIncentive.getDescription());
            preparedStatement.setString(8, cashDiscountIncentive.getDisclaimer());
            preparedStatement.setString(9, carVinUUID);
            preparedStatement.setString(10, cashDiscountIncentive.getCashDiscountType().toString());
            preparedStatement.setDouble(11, cashDiscountIncentive.getValue());
            preparedStatement.execute();

            sql = "INSERT INTO IncentiveVINs(incentiveVinID, carVIN) VALUES (?, ?)";
            preparedStatement = this.conn.prepareStatement(sql);
            for (String carVIN : cashDiscountIncentive.getCarVINList()) {
                preparedStatement.setString(1, carVinUUID);
                preparedStatement.setString(2, carVIN);
                preparedStatement.execute();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(String sql, String[] params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setString(i + 1 , params[i]);
        }
        return stmt;
    }

    public ResultSet query(String sql, String[] params) throws SQLException {
        return prepareStatement(sql, params).executeQuery();
    }

    public int update(String sql, String[] params) throws SQLException {
        return prepareStatement(sql, params).executeUpdate();
    }
    public ResultSet getResults(String query) {
        ResultSet rs = null;
        try {
            // stmt is the connection statement
            // System.out.println("select sql query: " + query);
            rs = this.stmt.executeQuery(query);
            /**
             *
             * rs = this.stmt.executeQuery("select * from dbo.CustomerRequest"); while
             * (rs.next()) { System.out.println(rs.getString("leadId") +
             * rs.getString("firstName")); }
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
    public int updateOldversion(String sql) {
        try {
            // System.out.println("update sql query: " + sql);
            return this.stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Car> getAllCars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Incentive> getAllIncentives() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}
