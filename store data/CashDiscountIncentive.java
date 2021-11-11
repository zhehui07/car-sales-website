package group8;

import java.util.Date;
import java.util.HashSet;

public class CashDiscountIncentive extends Incentive {
    private double value;
    private CashDiscountType cashDiscountType;

    public CashDiscountIncentive(String id, String dealerId, Date startDate, Date endDate,
                                 String title, String description, String disclaimer,
                                 HashSet<String> carVINs, double value, CashDiscountType cashDiscountType) {
        super(id, IncentiveType.DISCOUNT, dealerId, startDate, endDate, title, description, disclaimer, carVINs);
        this.value = value;
        this.cashDiscountType = cashDiscountType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public CashDiscountType getCashDiscountType() {
        return cashDiscountType;
    }

    public void setCashDiscountType(CashDiscountType cashDiscountType) {
        this.cashDiscountType = cashDiscountType;
    }
}
