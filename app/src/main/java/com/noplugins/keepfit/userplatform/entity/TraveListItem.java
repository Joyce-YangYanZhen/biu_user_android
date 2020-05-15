package com.noplugins.keepfit.userplatform.entity;

import java.util.List;

public class TraveListItem {

    /**
     * sportList : [{"pkname":"id","id":4,"sportsNum":"CUS456","userNum":"CUS19081532455685","areaNum":" ","areaName":"火影运动场","courseNum":"GYM998","courseType":2,"courseName":"火影田径赛","startTime":"2019-07-01 14:59:37","endTime":"2019-07-01 15:59:45","deleted":0,"biuTime":"14:59-15:59"},{"pkname":"id","id":5,"sportsNum":"CUS789","userNum":"CUS19081532455685","areaNum":" ","areaName":"火影运动场","courseNum":"GYM999","courseType":2,"courseName":"火影田径赛","startTime":"2019-10-01 14:59:37","endTime":"2019-10-01 15:59:45","deleted":0,"biuTime":"14:59-15:59"},{"pkname":"id","id":6,"sportsNum":"CUS890","userNum":"CUS19081532455685","areaNum":" ","areaName":"火影运动场","courseNum":"GYM995","courseType":2,"courseName":"火影田径赛","startTime":"2019-11-01 14:59:37","endTime":"2019-12-01 15:59:45","deleted":0,"biuTime":"14:59-15:59"}]
     * maxPage : 1
     */

    private int maxPage;
    private List<SportListBean> sportList;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<SportListBean> getSportList() {
        return sportList;
    }

    public void setSportList(List<SportListBean> sportList) {
        this.sportList = sportList;
    }

    public static class SportListBean {
        /**
         * pkname : id
         * id : 4
         * sportsNum : CUS456
         * userNum : CUS19081532455685
         * areaNum :
         * areaName : 火影运动场
         * courseNum : GYM998
         * courseType : 2
         * courseName : 火影田径赛
         * startTime : 2019-07-01 14:59:37
         * endTime : 2019-07-01 15:59:45
         * deleted : 0
         * biuTime : 14:59-15:59
         */

        private String pkname;
        private int id;
        private String sportsNum;
        private String userNum;
        private String areaNum;
        private String areaName;
        private String courseNum;
        private int courseType;
        private String courseName;
        private String startTime;
        private String endTime;
        private int deleted;
        private String biuTime;


        public String getAreaAddress() {
            return areaAddress;
        }

        public void setAreaAddress(String areaAddress) {
            this.areaAddress = areaAddress;
        }

        private String areaAddress;//地址

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        private String teacherName;

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
    }
}
