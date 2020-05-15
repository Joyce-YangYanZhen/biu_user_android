package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class PrivateBean {
    private int totalNum;
    private List<GenTeacherListBean> genTeacherList;
    private List<GenTeacherListBean> teacherList;
    private int maxPage;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<GenTeacherListBean> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<GenTeacherListBean> teacherList) {
        this.teacherList = teacherList;
    }

    public List<GenTeacherListBean> getGenTeacherList() {
        return genTeacherList;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setGenTeacherList(List<GenTeacherListBean> genTeacherList) {
        this.genTeacherList = genTeacherList;
    }

    public static class GenTeacherListBean {

        /**
         * updateDate : 2019-08-19 10:14:22
         * teacherName : 卡卡西
         * skillList : ["增肌减脂","尊巴","动感单车","体适能","瑜伽"]
         * year : 1
         * sex : 1
         * label : 1,2,3
         * logoUrl : http://qnimg.ahcomg.com/AllDefaultLogo
         * teacherType : 2
         * password :
         * deleted : 0
         * phone : 123
         * grade : 980
         * skill : 1,2,3,4,5
         * teacherNum : Gen123
         * finalGrade : 9.8
         * serviceDur : 100
         * collect : 1
         * card :
         * createDate : 2019-08-19 10:14:22
         */

        private String updateDate;
        private String teacherName;
        private int year;
        private int sex;
        private String label;
        private String logoUrl;
        private int teacherType;
        private String password;
        private int deleted;
        private String phone;
        private int grade;
        private String skill;
        private String teacherNum;
        private double finalGrade;
        private int serviceDur;
        private int collect;
        private String card;
        private String createDate;
        private List<String> skillList;
        private double lowestPrice;
        private int distance;

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(double lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public int getTeacherType() {
            return teacherType;
        }

        public void setTeacherType(int teacherType) {
            this.teacherType = teacherType;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(String teacherNum) {
            this.teacherNum = teacherNum;
        }

        public double getFinalGrade() {
            return finalGrade;
        }

        public void setFinalGrade(double finalGrade) {
            this.finalGrade = finalGrade;
        }

        public int getServiceDur() {
            return serviceDur;
        }

        public void setServiceDur(int serviceDur) {
            this.serviceDur = serviceDur;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public List<String> getSkillList() {
            return skillList;
        }

        public void setSkillList(List<String> skillList) {
            this.skillList = skillList;
        }
    }
}
