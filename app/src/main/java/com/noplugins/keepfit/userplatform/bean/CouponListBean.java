package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class CouponListBean {

    private List<CouponBean> coupon;

    private String totalPrice;

    public List<CouponBean> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponBean> coupon) {
        this.coupon = coupon;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static class CouponBean {
        /**
         * coupon_num : 123
         * type : 1
         * price_fit : 40
         * price_discount : 10
         * coupon_type : 1
         */

        private String userCouponNum;
        private int type;
        private String priceFit;
        private String priceDiscount;
        private int couponType;

        public String getUserCouponNum() {
            return userCouponNum;
        }

        public void setUserCouponNum(String userCouponNum) {
            this.userCouponNum = userCouponNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPriceFit() {
            return priceFit;
        }

        public void setPriceFit(String priceFit) {
            this.priceFit = priceFit;
        }

        public String getPriceDiscount() {
            return priceDiscount;
        }

        public void setPriceDiscount(String priceDiscount) {
            this.priceDiscount = priceDiscount;
        }

        public int getCouponType() {
            return couponType;
        }

        public void setCouponType(int couponType) {
            this.couponType = couponType;
        }
    }
}
