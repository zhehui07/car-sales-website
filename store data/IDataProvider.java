package group8;

import java.util.HashMap;
import java.util.List;

public interface IDataProvider
{
    List<Car> getAllCarsByDealerId(String dealerId);
    List<Car> getAllCars();
    HashMap<Car, List<Incentive>> getAllIncentivesByDealerId(String dealerId);
    List<Incentive> getAllIncentives();
    void persistIncentive(CashDiscountIncentive cashDiscountIncentive);
    void persistIncentive(LoanIncentive loanIncentive);
    void persistIncentive(LeasingIncentive leasingIncentive);
    void persistIncentive(RebateIncentive rebateIncentive);
}
