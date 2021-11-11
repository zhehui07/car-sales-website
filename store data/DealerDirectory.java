package group8;

import group1.model.State;
import group8.data.NewJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DealerDirectory {

    public static final String STATE = "State";
    public static final String STATE_CODE = "StateCode";
    private NewJDBC jdbcInstance;

    public DealerDirectory() {
        try {
            jdbcInstance = NewJDBC.getInstance();
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        DealerDirectory dealerDirectory = new DealerDirectory();
//        List<Dealer> dealers = dealerDirectory.getDealerByStateOrStateId("WA");
        List<Dealer> dealers = dealerDirectory.getDealerByDealerName("lee");
        System.out.println(dealers.size());
        for (Dealer dealer: dealers) {
            System.out.println(dealer.getName());
        }
        System.out.println(dealerDirectory.getDealerByZipCode("98032"));
        dealerDirectory.getUniqueStates().forEach(item -> {
            System.out.println(String.format("%s, %s", item.getName(), item.getCode()));
        });
    }


    /*******************  METHODS  *******************/

    public List<Dealer> getDealerByStateOrStateId(String stateOrStateId) throws SQLException {
        List<Dealer> dealers = new ArrayList<>();
        String query = "select * from Dealer where State = ? or StateCode = ?";
        String[] params = new String[]{stateOrStateId, stateOrStateId};
        ResultSet resultSet = jdbcInstance.query(query, params);
        while (resultSet.next()) {
            dealers.add(getDealer(resultSet));
        }
        return dealers;
    }

    public List<Dealer> getDealerByZipCode(String zipcode) throws SQLException {
        List<Dealer> dealers = new ArrayList<>();
        String query = "select * from Dealer where ZipCode = ?";
        String[] params = new String[]{zipcode};
        ResultSet resultSet = jdbcInstance.query(query, params);
        while (resultSet.next()) {
            dealers.add(getDealer(resultSet));
        }
        return dealers;
    }

    public List<Dealer> getDealerByDealerName(String dealerName) throws SQLException {
        List<Dealer> dealers = new ArrayList<>();
        String query = "select * from Dealer where DealerName like ?";
        dealerName = "%" + dealerName +"%";
        String[] params = new String[]{dealerName};
        ResultSet resultSet = jdbcInstance.query(query, params);
        while (resultSet.next()) {
            dealers.add(getDealer(resultSet));
        }
        return dealers;
    }

    public List<State> getUniqueStates() throws SQLException {
        List<State> states = new ArrayList<>();
        String query = "SELECT DISTINCT State, StateCode FROM Dealer";
        ResultSet resultSet = jdbcInstance.query(query, new String[]{});
        while (resultSet.next()) {
            String name = resultSet.getString(STATE);
            String code = resultSet.getString(STATE_CODE);
            states.add(new State(name, code));
        }
        return states;
    }

    private Dealer getDealer(ResultSet resultSet) throws SQLException {
        Dealer dealer = new Dealer();
        dealer.setDealerID(resultSet.getString("DealerId"));
        dealer.setName(resultSet.getString("DealerName"));
        dealer.setStreetAddress(resultSet.getString("DealerAddress"));
        dealer.setCity(resultSet.getString("City"));
        dealer.setStateID(resultSet.getString(STATE_CODE));
        dealer.setState(resultSet.getString(STATE));
        dealer.setCountry(resultSet.getString("Country"));
        dealer.setZipcode(resultSet.getString("ZipCode"));
        dealer.setPhoneNumber(resultSet.getString("PhoneNumber"));
        dealer.setLatitude(resultSet.getDouble("Latitude"));
        dealer.setLongitude(resultSet.getDouble("Longitude"));
        return dealer;
    }
}
