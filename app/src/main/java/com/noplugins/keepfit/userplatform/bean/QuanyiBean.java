package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class QuanyiBean {

    /**
     * code : 0
     * data : [{"gymAreaNum":"GYM19080451439742","updateDate":"2019-08-15 10:08:26","begdate":"2019-08-01 10:08:32","level":1,"custNum":null,"memberName":"鸣人","memberRights":null,"remark":"","memberNum":"GYM1234","pkname":"id","mailbox":"","enddate":"2019-08-16 10:08:37","areaLogo":"","phone":"123456","areaName":"场馆名称","id":1,"cardNumber":"","coachServiceTimes":1,"createDate":"2019-08-15 10:08:26","status":0},{"gymAreaNum":"GYM19080245331937","updateDate":"2019-08-15 10:08:26","begdate":"2019-08-01 10:08:32","level":1,"custNum":null,"memberName":"我爱罗","memberRights":null,"remark":null,"memberNum":"GYM12345","pkname":"id","mailbox":null,"enddate":"2019-08-16 10:08:37","areaLogo":"","phone":"123456","areaName":"场馆名称","id":2,"cardNumber":" ","coachServiceTimes":1,"createDate":"2019-08-15 10:08:26","status":0}]
     * message : success
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gymAreaNum : GYM19080451439742
         * updateDate : 2019-08-15 10:08:26
         * begdate : 2019-08-01 10:08:32
         * level : 1
         * custNum : null
         * memberName : 鸣人
         * memberRights : null
         * remark :
         * memberNum : GYM1234
         * pkname : id
         * mailbox :
         * enddate : 2019-08-16 10:08:37
         * areaLogo :
         * phone : 123456
         * areaName : 场馆名称
         * id : 1
         * cardNumber :
         * coachServiceTimes : 1
         * createDate : 2019-08-15 10:08:26
         * status : 0
         */

        private String gymAreaNum;
        private String updateDate;
        private String begdate;
        private int level;
        private Object custNum;
        private String memberName;
        private Object memberRights;
        private String remark;
        private String memberNum;
        private String pkname;
        private String mailbox;
        private String enddate;
        private String areaLogo;
        private String phone;
        private String areaName;
        private int id;
        private String cardNumber;
        private int coachServiceTimes = 0;
        private String createDate;
        private int status;
        private int memberRight;

        public int getMemberRight() {
            return memberRight;
        }

        public void setMemberRight(int memberRight) {
            this.memberRight = memberRight;
        }

        public String getGymAreaNum() {
            return gymAreaNum;
        }

        public void setGymAreaNum(String gymAreaNum) {
            this.gymAreaNum = gymAreaNum;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getBegdate() {
            return begdate;
        }

        public void setBegdate(String begdate) {
            this.begdate = begdate;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Object getCustNum() {
            return custNum;
        }

        public void setCustNum(Object custNum) {
            this.custNum = custNum;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public Object getMemberRights() {
            return memberRights;
        }

        public void setMemberRights(Object memberRights) {
            this.memberRights = memberRights;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getPkname() {
            return pkname;
        }

        public void setPkname(String pkname) {
            this.pkname = pkname;
        }

        public String getMailbox() {
            return mailbox;
        }

        public void setMailbox(String mailbox) {
            this.mailbox = mailbox;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getAreaLogo() {
            return areaLogo;
        }

        public void setAreaLogo(String areaLogo) {
            this.areaLogo = areaLogo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public int getCoachServiceTimes() {
            return coachServiceTimes;
        }

        public void setCoachServiceTimes(int coachServiceTimes) {
            this.coachServiceTimes = coachServiceTimes;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
