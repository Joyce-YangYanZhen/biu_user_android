package com.noplugins.keepfit.userplatform.bean;

public class OrderReult {

    /**
     * totalPrice : 4000
     * ordNum : CUS19082723209071
     */

    private double totalPrice;
    private double discount;//1.1活动新增 折扣价格
    private String ordNum;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(String ordNum) {
        this.ordNum = ordNum;
    }
}
