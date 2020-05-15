package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class TeamBean {

    /**
     * courseList : [{"gymAreaNum":"GYM19081634685143","courseType":1,"updateDate":"2019-08-19 10:48:38","comeNum":0,"distance":15,"type":"1","maxNum":20,"checkStatus":1,"areaName":"场馆名称9","price":2900,"finalPrice":29,"loop":false,"startTime":1566182903000,"id":3,"gymPlaceNum":"GYM19081657827460","createDate":"2019-08-19 10:48:38","teacherName":"卡卡西","applyNum":10,"target":1,"imgUrl":"fsdf","difficulty":1,"courseName":"卡卡西团课","deleted":false,"courseNum":"GYM779","genTeacherNum":"Gen123","endTime":1566186512000,"classType":1,"courseTime":1,"status":1}]
     * totalCount : 4
     * maxPage : 1
     */

    private int totalCount;
    private int maxPage;
    private List<CourseListBean> courseList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<CourseListBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseListBean> courseList) {
        this.courseList = courseList;
    }

    public static class CourseListBean {

        /**
         * gymAreaNum : GYM19081634685143
         * courseType : 1
         * updateDate : 2019-08-19 10:48:38
         * comeNum : 0
         * distance : 15
         * finalPrice : 29.0
         * type : 1
         * maxNum : 20
         * checkStatus : 1
         * areaName : 场馆名称9
         * price : 2900
         * loop : false
         * startTime : 1566182903000
         * id : 3
         * gymPlaceNum : GYM19081657827460
         * createDate : 2019-08-19 10:48:38
         * address : 上海田子坊
         * teacherName : 卡卡西
         * applyNum : 10
         * target : 1
         * imgUrl : http://qnimg.ahcomg.com/courseDefault
         * difficulty : 1
         * courseName : 卡卡西团课
         * deleted : false
         * courseNum : GYM779
         * genTeacherNum : Gen123
         * fullPerson : 0
         * endTime : 1566186512000
         * classType : 1
         * courseTime : 1
         * status : 1
         */

        private String gymAreaNum;
        private int courseType;
        private String updateDate;
        private int comeNum;
        private int distance;
        private double finalPrice;
        private String type;
        private int maxNum;
        private int checkStatus;
        private String areaName;
        private int price;
        private boolean loop;
        private long startTime;
        private int id;
        private String gymPlaceNum;
        private String createDate;
        private String address;
        private String teacherName;
        private int applyNum;
        private int target;
        private String imgUrl;
        private int difficulty;
        private String courseName;
        private boolean deleted;
        private String courseNum;
        private String genTeacherNum;
        private int fullPerson;
        private long endTime;
        private int classType;
        private int courseTime;
        private int status;

        public String getGymAreaNum() {
            return gymAreaNum;
        }

        public void setGymAreaNum(String gymAreaNum) {
            this.gymAreaNum = gymAreaNum;
        }

        public int getCourseType() {
            return courseType;
        }

        public void setCourseType(int courseType) {
            this.courseType = courseType;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public int getComeNum() {
            return comeNum;
        }

        public void setComeNum(int comeNum) {
            this.comeNum = comeNum;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public boolean isLoop() {
            return loop;
        }

        public void setLoop(boolean loop) {
            this.loop = loop;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGymPlaceNum() {
            return gymPlaceNum;
        }

        public void setGymPlaceNum(String gymPlaceNum) {
            this.gymPlaceNum = gymPlaceNum;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getApplyNum() {
            return applyNum;
        }

        public void setApplyNum(int applyNum) {
            this.applyNum = applyNum;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
        }

        public String getGenTeacherNum() {
            return genTeacherNum;
        }

        public void setGenTeacherNum(String genTeacherNum) {
            this.genTeacherNum = genTeacherNum;
        }

        public int getFullPerson() {
            return fullPerson;
        }

        public void setFullPerson(int fullPerson) {
            this.fullPerson = fullPerson;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getClassType() {
            return classType;
        }

        public void setClassType(int classType) {
            this.classType = classType;
        }

        public int getCourseTime() {
            return courseTime;
        }

        public void setCourseTime(int courseTime) {
            this.courseTime = courseTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
