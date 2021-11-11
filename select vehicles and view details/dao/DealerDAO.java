package group2.dao;

import group8.data.NewJDBC;

import java.sql.SQLException;

public class DealerDAO {
    private NewJDBC newJDBC;

    public DealerDAO() {
        try {
            this.newJDBC = NewJDBC.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}


