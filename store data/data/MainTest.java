//package group8.data;
//
//import group8.*;
//
//import java.sql.Date;
//import java.sql.SQLException;
//import java.util.HashSet;
//import java.util.List;
//
//import static group8.IncentiveType.DISCOUNT;
//import static group8.IncentiveType.LOAN;
//
///**
// * @author Guiyu Liu
// * Apr 13, 2021.
// */
//public class MainTest {
//
//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
////        DbDealerStorage dbDealerStorage = new DbDealerStorage();
////        //dbDealerStorage.printAll();
////        //dbDealerStorage.printNumRow();
////        //dbDealerStorage.printNumCol();
////        //dbDealerStorage.printColInfo();
////        List<Car> res = dbDealerStorage.getAllCarsByDealerId("18");
////        for(Car c : res){
////           System.out.println(c.getMake());
////        }
////        DealerDirectory d = dbDealerStorage.loadDealerDirectory();
////        System.out.println(d.toString());
////        //System.out.println(dbDealerStorage.getDealerById("testId").getName());
//
//        HashSet<String> hashSet = new HashSet<>();
//        hashSet.add("testCar1");
//        hashSet.add("testCar2");
//        DbDealerStorage dbDealerStorage = new DbDealerStorage();
//        Incentive incentive = new LoanIncentive("222", "222", new Date(System.currentTimeMillis()),
//                new Date(System.currentTimeMillis()+ 10000), "test title", "description", "ff",
//                hashSet, 0.5, 3);
//        dbDealerStorage.persistIncentive(incentive);
//    }
//}
