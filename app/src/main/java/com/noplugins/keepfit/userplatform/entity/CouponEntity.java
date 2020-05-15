package com.noplugins.keepfit.userplatform.entity;

public class CouponEntity {
    public CouponEntity() {
    }

    public CouponEntity(String coupon_num) {
        this.coupon_num = coupon_num;
    }

    private String coupon_num;

    public String getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(String coupon_num) {
        this.coupon_num = coupon_num;
    }
}
