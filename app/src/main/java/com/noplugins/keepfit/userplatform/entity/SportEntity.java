package com.noplugins.keepfit.userplatform.entity;

import java.util.List;

public class SportEntity {


    /**
     * moodDetail : {"pkname":"id","id":18,"sportsNum":"CUS12311","ordNum":"CUS12311","sportsBefore":1,"sportsAfter":2,"deleted":0,"checkIn":"2019-08-26 14:52:47","ordItemNum":"CUS1231","inRoom":"14:52"}
     * lable : 1,2
     * testDetail : {"pkname":"id","id":12,"assessNum":"CUS190822043968391","sportsNum":"CUS12311","userNum":"CUS19081685290145","stature":160,"weight":50,"bodyfat":120,"bmi":120,"waistratebuttocks":120,"kcal":120,"deleted":0,"remark":null,"createDate":"2019-08-22 16:18:51","updateDate":"2019-08-22 16:18:51","finalBodyfat":1.2,"finalKcal":null,"finalBmi":null,"finalwaistratebuttocks":1.2,"year":null}
     * courseDetail : {"pkname":"id","id":13,"sportsNum":"CUS12311","userNum":"CUS19081685290145","areaNum":" ","areaName":"火影运动场","courseNum":"GYM999","areaAddress":"钱伟欧巴的闺房","teacherName":"钱伟欧巴","courseType":1,"courseName":"伟哥开车训练营","startTime":"2019-08-01 14:59:37","endTime":"2019-08-01 15:59:45","courseDes":"让你一路畅通无忧～","lable":"1,2","deleted":0,"biuTime":"14:59-15:59","lableList":["增肌减脂","尊巴"]}
     * courseDesDetail : 让你一路畅通无忧～
     */

    private MoodDetailBean moodDetail;
    private String lable;
    private TestDetailBean testDetail;
    private CourseDetailBean courseDetail;
    private String courseDesDetail;

    public MoodDetailBean getMoodDetail() {
        return moodDetail;
    }

    public void setMoodDetail(MoodDetailBean moodDetail) {
        this.moodDetail = moodDetail;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public TestDetailBean getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(TestDetailBean testDetail) {
        this.testDetail = testDetail;
    }

    public CourseDetailBean getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(CourseDetailBean courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getCourseDesDetail() {
        return courseDesDetail;
    }

    public void setCourseDesDetail(String courseDesDetail) {
        this.courseDesDetail = courseDesDetail;
    }

    public static class MoodDetailBean {
        /**
         * pkname : id
         * id : 18
         * sportsNum : CUS12311
         * ordNum : CUS12311
         * sportsBefore : 1
         * sportsAfter : 2
         * deleted : 0
         * checkIn : 2019-08-26 14:52:47
         * ordItemNum : CUS1231
         * inRoom : 14:52
         */

        private String pkname;
        private int id;
        private String sportsNum;
        private String ordNum;
        private int sportsBefore;
        private int sportsAfter;
        private int deleted;
        private String checkIn;
        private String ordItemNum;
        private String inRoom;

        public String getOutRoom() {
            return outRoom;
        }

        public void setOutRoom(String outRoom) {
            this.outRoom = outRoom;
        }

        private String outRoom;
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

        public String getSportsNum() {
            return sportsNum;
        }

        public void setSportsNum(String sportsNum) {
            this.sportsNum = sportsNum;
        }

        public String getOrdNum() {
            return ordNum;
        }

        public void setOrdNum(String ordNum) {
            this.ordNum = ordNum;
        }

        public int getSportsBefore() {
            return sportsBefore;
        }

        public void setSportsBefore(int sportsBefore) {
            this.sportsBefore = sportsBefore;
        }

        public int getSportsAfter() {
            return sportsAfter;
        }

        public void setSportsAfter(int sportsAfter) {
            this.sportsAfter = sportsAfter;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(String checkIn) {
            this.checkIn = checkIn;
        }

        public String getOrdItemNum() {
            return ordItemNum;
        }

        public void setOrdItemNum(String ordItemNum) {
            this.ordItemNum = ordItemNum;
        }

        public String getInRoom() {
            return inRoom;
        }

        public void setInRoom(String inRoom) {
            this.inRoom = inRoom;
        }
    }

    public static class TestDetailBean {
        /**
         * pkname : id
         * id : 12
         * assessNum : CUS190822043968391
         * sportsNum : CUS12311
         * userNum : CUS19081685290145
         * stature : 160
         * weight : 50
         * bodyfat : 120
         * bmi : 120
         * waistratebuttocks : 120
         * kcal : 120
         * deleted : 0
         * remark : null
         * createDate : 2019-08-22 16:18:51
         * updateDate : 2019-08-22 16:18:51
         * finalBodyfat : 1.2
         * finalKcal : null
         * finalBmi : null
         * finalwaistratebuttocks : 1.2
         * year : null
         */

        private String pkname;
        private int id;
        private String assessNum;
        private String sportsNum;
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

        public String getSportsNum() {
            return sportsNum;
        }

        public void setSportsNum(String sportsNum) {
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

    public static class CourseDetailBean {
        /**
         * pkname : id
         * id : 13
         * sportsNum : CUS12311
         * userNum : CUS19081685290145
         * areaNum :
         * areaName : 火影运动场
         * courseNum : GYM999
         * areaAddress : 钱伟欧巴的闺房
         * teacherName : 钱伟欧巴
         * courseType : 1
         * courseName : 伟哥开车训练营
         * startTime : 2019-08-01 14:59:37
         * endTime : 2019-08-01 15:59:45
         * courseDes : 让你一路畅通无忧～
         * lable : 1,2
         * deleted : 0
         * biuTime : 14:59-15:59
         * lableList : ["增肌减脂","尊巴"]
         */

        private String pkname;
        private int id;
        private String sportsNum;
        private String userNum;
        private String areaNum;
        private String areaName;
        private String courseNum;
        private String areaAddress;
        private String teacherName;
        private int courseType;
        private String courseName;
        private String startTime;
        private String endTime;
        private String courseDes;
        private String lable;
        private int deleted;
        private String biuTime;
        private List<String> lableList;

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

        public String getSportsNum() {
            return sportsNum;
        }

        public void setSportsNum(String sportsNum) {
            this.sportsNum = sportsNum;
        }

        public String getUserNum() {
            return userNum;
        }

        public void setUserNum(String userNum) {
            this.userNum = userNum;
        }

        public String getAreaNum() {
            return areaNum;
        }

        public void setAreaNum(String areaNum) {
            this.areaNum = areaNum;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
        }

        public String getAreaAddress() {
            return areaAddress;
        }

        public void setAreaAddress(String areaAddress) {
            this.areaAddress = areaAddress;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getCourseType() {
            return courseType;
        }

        public void setCourseType(int courseType) {
            this.courseType = courseType;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
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

        public String getCourseDes() {
            return courseDes;
        }

        public void setCourseDes(String courseDes) {
            this.courseDes = courseDes;
        }

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getBiuTime() {
            return biuTime;
        }

        public void setBiuTime(String biuTime) {
            this.biuTime = biuTime;
        }

        public List<String> getLableList() {
            return lableList;
        }

        public void setLableList(List<String> lableList) {
            this.lableList = lableList;
        }
    }
}
