package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class TeacherTimeBean {
    private List<MyBean> data;

    public List<MyBean> getData() {
        return data;
    }

    public void setData(List<MyBean> data) {
        this.data = data;
    }

    public class MyBean{

        /**
         * a : a
         * updateDate : 2019-08-27 20:10:13
         * deleted : 1
         * timeType : 1
         * teacherNum : Gen123
         * id : 1
         * timeDec : 7:00-8:00
         * createDate : 2019-08-27 20:10:13
         */

        private String a;
        private String updateDate;
        private int deleted;
        private int timeType;
        private String teacherNum;
        private String timeDec;
        private int id;

        private String createDate;
        private boolean isClicks;

        public boolean isClicks() {
            return isClicks;
        }

        public void setClicks(boolean clicks) {
            isClicks = clicks;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
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

        public int getTimeType() {
            return timeType;
        }

        public void setTimeType(int timeType) {
            this.timeType = timeType;
        }

        public String getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(String teacherNum) {
            this.teacherNum = teacherNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTimeDec() {
            return timeDec;
        }

        public void setTimeDec(String timeDec) {
            this.timeDec = timeDec;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
