package group7.dataprovider;

import group8.*;

import java.util.List;

public interface DataProvider {
    List<Car> getAllCarsByDealerId(String dealerId);
    void persistIncentive(CashDiscountIncentive cashDiscountIncentive);
    void persistIncentive(LoanIncentive loanIncentive);
    void persistIncentive(LeasingIncentive leasingIncentive);
    void persistIncentive(RebateIncentive rebateIncentive);
}
