package com.noplugins.keepfit.userplatform.entity;

import java.util.List;

public class BodyResourceEntity {

    /**
     * data : [{"pkname":"id","id":7,"assessNum":"CU1111245","sportsNum":null,"userNum":"CUS19081905171695","stature":123,"weight":123,"bodyfat":1234,"bmi":1234,"waistratebuttocks":1234,"kcal":1232,"deleted":0,"remark":null,"createDate":"2019-08-22 15:17:14","updateDate":"2019-08-22 15:16:45","finalBodyfat":12.34,"finalKcal":12.32,"finalBmi":12.34,"finalwaistratebuttocks":12.34,"year":null},{"pkname":"id","id":8,"assessNum":"CU11112456","sportsNum":null,"userNum":"CUS19081905171695","stature":123,"weight":123,"bodyfat":1234,"bmi":1234,"waistratebuttocks":1234,"kcal":1232,"deleted":0,"remark":null,"createDate":"2019-08-21 15:17:14","updateDate":"2019-08-22 15:16:45","finalBodyfat":12.34,"finalKcal":12.32,"finalBmi":12.34,"finalwaistratebuttocks":12.34,"year":null}]
     * code : 0
     * message : success
     */

    private List<DataBean> bodyData;

    public List<DataBean> getData() {
        return bodyData;
    }

    public void setData(List<DataBean> data) {
        this.bodyData = data;
    }

    public static class DataBean {
        /**
         * pkname : id
         * id : 7
         * assessNum : CU1111245
         * sportsNum : null
         * userNum : CUS19081905171695
         * stature : 123
         * weight : 123
         * bodyfat : 1234
         * bmi : 1234
         * waistratebuttocks : 1234
         * kcal : 1232
         * deleted : 0
         * remark : null
         * createDate : 2019-08-22 15:17:14
         * updateDate : 2019-08-22 15:16:45
         * finalBodyfat : 12.34
         * finalKcal : 12.32
         * finalBmi : 12.34
         * finalwaistratebuttocks : 12.34
         * year : null
         */

        private String pkname;
        private int id;
        private String assessNum;
        private Object sportsNum;
        private String userNum;
        private int stature;
        private int weight;
        private int bodyfat;
        private int bmi;
        private int waistratebuttocks;
        private int kcal;
        private int deleted;
        private Object remark;
        private String createDate;
        private String updateDate;
        private double finalBodyfat;
        private double finalKcal;
        private double finalBmi;
        private double finalwaistratebuttocks;
        private Object year;

        public String getDayview() {
            return dayview;
        }

        public void setDayview(String dayview) {
            this.dayview = dayview;
        }

        private String dayview;

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

        public String getAssessNum() {
            return assessNum;
        }

        public void setAssessNum(String assessNum) {
            this.assessNum = assessNum;
        }

        public Object getSportsNum() {
            return sportsNum;
        }

        public void setSportsNum(Object sportsNum) {
            this.sportsNum = sportsNum;
        }

        public String getUserNum() {
            return userNum;
        }

        public void setUserNum(String userNum) {
            this.userNum = userNum;
        }

        public int getStature() {
            return stature;
        }

        public void setStature(int stature) {
            this.stature = stature;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getBodyfat() {
            return bodyfat;
        }

        public void setBodyfat(int bodyfat) {
            this.bodyfat = bodyfat;
        }

        public int getBmi() {
            return bmi;
        }

        public void setBmi(int bmi) {
            this.bmi = bmi;
        }

        public int getWaistratebuttocks() {
            return waistratebuttocks;
        }

        public void setWaistratebuttocks(int waistratebuttocks) {
            this.waistratebuttocks = waistratebuttocks;
        }

        public int getKcal() {
            return kcal;
        }

        public void setKcal(int kcal) {
            this.kcal = kcal;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
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

        public double getFinalBodyfat() {
            return finalBodyfat;
        }

        public void setFinalBodyfat(double finalBodyfat) {
            this.finalBodyfat = finalBodyfat;
        }

        public double getFinalKcal() {
            return finalKcal;
        }

        public void setFinalKcal(double finalKcal) {
            this.finalKcal = finalKcal;
        }

        public double getFinalBmi() {
            return finalBmi;
        }

        public void setFinalBmi(double finalBmi) {
            this.finalBmi = finalBmi;
        }

        public double getFinalwaistratebuttocks() {
            return finalwaistratebuttocks;
        }

        public void setFinalwaistratebuttocks(double finalwaistratebuttocks) {
            this.finalwaistratebuttocks = finalwaistratebuttocks;
        }

        public Object getYear() {
            return year;
        }

        public void setYear(Object year) {
            this.year = year;
        }
    }
}
