package group5.dao;
import java.sql.*;
import java.util.ArrayList;

/*
 * used this class only for Team5 when group8 DAO was not integrated
 */
public class DBConnector {
    public ResultSet data;

    Connection conn = null;

    public Connection getDBConnection() throws SQLException {

        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		
		  String dbURL = "jdbc:sqlserver://boyce.coe.neu.edu:1433; DatabaseName=Jaya";
		  String userid = "INFO6210"; String passwd = "NEUHusky!";

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(dbURL, userid, passwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    public ResultSet queryExecution(String query) throws SQLException {
    	System.out.println("in queryExecution  "+query);
        conn = getDBConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

