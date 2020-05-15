package com.noplugins.keepfit.userplatform.entity;

import java.util.List;

public class OrderDetailEntity {

    private List<List<OrderListBean>> orderList;
    private List<CouponBean> coupon;
    private String totalPrice;

    public List<List<OrderListBean>> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<List<OrderListBean>> orderList) {
        this.orderList = orderList;
    }

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

    public static class OrderListBean {
        /**
         * pkname : id
         * id : 5
         * custOrderItemNum : CUS19082741960847
         * custOrderNum : CUS19082750347844
         * areaNum : GYM19081515171364
         * custUserNum : 121
         * payType : 1
         * teacherTime : 5
         * coachUserNum : Gen123
         * areaName : 123
         * address : 222
         * courseType : 2
         * startTime : 2019-08-27 20:59:09
         * price : 2000
         * status : 0
         * createDate : 2019-08-27 15:26:37
         * updateDate : 2019-08-27 15:26:37
         * deleted : 0
         * beforeFace : 0
         * afterFace : 0
         * totalPrice : 40.0
         * finalStatus : 未支付
         * url : http://qnimg.ahcomg.com/teacherDefault
         */

        private String pkname;
        private int id;
        private String custOrderItemNum;
        private String custOrderNum;
        private String areaNum;
        private String custUserNum;
        private int payType;
        private int teacherTime;
        private String coachUserNum;
        private String areaName;
        private String address;
        private int courseType;
        private String startTime;
        private int price;
        private int status;
        private String createDate;
        private String updateDate;
        private int deleted;
        private int beforeFace;
        private int afterFace;
        private double totalPrice;
        private double finalPrice;
        private String finalStatus;
        private String url;
private String endTime;
        private long timeDur;
        private String coachUserName;
        private String courseName;
        private int cancelButton;

        public int getCancelButton() {
            return cancelButton;
        }

        public void setCancelButton(int cancelButton) {
            this.cancelButton = cancelButton;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCoachUserName() {
            return coachUserName;
        }

        public void setCoachUserName(String coachUserName) {
            this.coachUserName = coachUserName;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public long getTimeDur() {
            return timeDur;
        }

        public void setTimeDur(long timeDur) {
            this.timeDur = timeDur;
        }

        public String getPkname() {
            return pkname;
        }

        public void setPkname(String pkname) {
            this.pkname = pkname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCustOrderItemNum() {
            return custOrderItemNum;
        }

        public void setCustOrderItemNum(String custOrderItemNum) {
            this.custOrderItemNum = custOrderItemNum;
        }

        public String getCustOrderNum() {
            return custOrderNum;
        }

        public void setCustOrderNum(String custOrderNum) {
            this.custOrderNum = custOrderNum;
        }

        public String getAreaNum() {
            return areaNum;
        }

        public void setAreaNum(String areaNum) {
            this.areaNum = areaNum;
        }

        public String getCustUserNum() {
            return custUserNum;
        }

        public void setCustUserNum(String custUserNum) {
            this.custUserNum = custUserNum;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getTeacherTime() {
            return teacherTime;
        }

        public void setTeacherTime(int teacherTime) {
            this.teacherTime = teacherTime;
        }

        public String getCoachUserNum() {
            return coachUserNum;
        }

        public void setCoachUserNum(String coachUserNum) {
            this.coachUserNum = coachUserNum;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCourseType() {
            return courseType;
        }

        public void setCourseType(int courseType) {
            this.courseType = courseType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getBeforeFace() {
            return beforeFace;
        }

        public void setBeforeFace(int beforeFace) {
            this.beforeFace = beforeFace;
        }

        public int getAfterFace() {
            return afterFace;
        }

        public void setAfterFace(int afterFace) {
            this.afterFace = afterFace;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getFinalStatus() {
            return finalStatus;
        }

        public void setFinalStatus(String finalStatus) {
            this.finalStatus = finalStatus;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public static class CouponBean {
        /**
         * couponType : 1
         * priceDiscount : 10
         */

        private int couponType;
        private String priceDiscount;

        public int getCouponType() {
            return couponType;
        }

        public void setCouponType(int couponType) {
            this.couponType = couponType;
        }

        public String getPriceDiscount() {
            return priceDiscount;
        }

        public void setPriceDiscount(String priceDiscount) {
            this.priceDiscount = priceDiscount;
        }
    }
}
