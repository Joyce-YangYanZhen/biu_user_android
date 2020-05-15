package com.noplugins.keepfit.userplatform.bean;

public class OrderResultBean {

    /**
     * order : {"pkname":"id","id":49,"ordNum":"CUS19090410037503","userNum":null,"payType":null,"deleted":0,"totalPrice":2900,"status":"0","note":null,"createDate":"2019-09-04 10:00:28","updateDate":"2019-09-04 10:00:28"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean {
        /**
         * pkname : id
         * id : 49
         * ordNum : CUS19090410037503
         * userNum : null
         * payType : null
         * deleted : 0
         * totalPrice : 2900
         * status : 0
         * note : null
         * createDate : 2019-09-04 10:00:28
         * updateDate : 2019-09-04 10:00:28
         */

        private String pkname;
        private int id;
        private String ordNum;
        private Object userNum;
        private Object payType;
        private int deleted;
        private int totalPrice;
        private String status;
        private Object note;
        private String createDate;
        private String updateDate;

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

        public String getOrdNum() {
            return ordNum;
        }

        public void setOrdNum(String ordNum) {
            this.ordNum = ordNum;
        }

        public Object getUserNum() {
            return userNum;
        }

        public void setUserNum(Object userNum) {
            this.userNum = userNum;
        }

        public Object getPayType() {
            return payType;
        }

        public void setPayType(Object payType) {
            this.payType = payType;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getNote() {
            return note;
        }

        public void setNote(Object note) {
            this.note = note;
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
    }
}
