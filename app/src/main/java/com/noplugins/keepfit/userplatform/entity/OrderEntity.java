package com.noplugins.keepfit.userplatform.entity;

import java.util.List;

public class OrderEntity {

    private int maxPage;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    private List<List<OrderListBean>> orderList;

    public List<List<OrderListBean>> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<List<OrderListBean>> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * pkname : id
         * id : 1
         * custOrderItemNum : CUS123
         * custOrderNum : CUS123
         * custUserNum : CUS19081922275624
         * courseNum : GYM77902
         * coachUserNum : Gen234567
         * areaName : 大蛇丸欢乐团课2
         * address : 场馆地址。。
         * courseType : 1
         * url : 111111
         * courseName : 大蛇丸欢乐
         * coachUserName : 大蛇丸
         * checkInTime : 2019-08-26 14:52:47
         * checkIn : 1
         * startTime : 2019-08-26 14:25:37
         * endTime : 2019-08-26 15:25:44
         * price : 2000
         * status : 0
         * createDate : 2019-08-26 14:25:33
         * updateDate : 2019-08-26 14:25:33
         * deleted : 0
         * beforeFace : 0
         * afterFace : 0
         * totalPrice : 120.0
         * finalStatus : 未支付
         */

        

        private String pkname;
        private int id;
        private String custOrderItemNum;
        private String custOrderNum;
        private String custUserNum;
        private String courseNum;
        private String coachUserNum;
        private String areaName;
        private String address;
        private int courseType;
        private String url;
        private String courseName;
        private String coachUserName;
        private String checkInTime;
        private int checkIn;
        private String startTime;
        private String endTime;
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
        private long timeDur;
        private String areaNum;
        private int cancelButton;
        private List<CouponListBean> couponList;


        public int getCancelButton() {
            return cancelButton;
        }

        public void setCancelButton(int cancelButton) {
            this.cancelButton = cancelButton;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getAreaNum() {
            return areaNum;
        }

        public void setAreaNum(String areaNum) {
            this.areaNum = areaNum;
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

        public String getCustUserNum() {
            return custUserNum;
        }

        public void setCustUserNum(String custUserNum) {
            this.custUserNum = custUserNum;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCoachUserName() {
            return coachUserName;
        }

        public void setCoachUserName(String coachUserName) {
            this.coachUserName = coachUserName;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public int getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(int checkIn) {
            this.checkIn = checkIn;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
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

        public List<CouponListBean> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<CouponListBean> couponList) {
            this.couponList = couponList;
        }

        public static class CouponListBean {
            /**
             * priceDiscount : 0.01
             * priceFit : 0.02
             * type : 1
             * couponType : 1
             */

            private String priceDiscount;
            private String priceFit;
            private int type;
            private int couponType;

            public String getPriceDiscount() {
                return priceDiscount;
            }

            public void setPriceDiscount(String priceDiscount) {
                this.priceDiscount = priceDiscount;
            }

            public String getPriceFit() {
                return priceFit;
            }

            public void setPriceFit(String priceFit) {
                this.priceFit = priceFit;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getCouponType() {
                return couponType;
            }

            public void setCouponType(int couponType) {
                this.couponType = couponType;
            }
        }
    }

}
