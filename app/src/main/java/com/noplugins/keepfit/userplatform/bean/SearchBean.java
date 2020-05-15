package com.noplugins.keepfit.userplatform.bean;

import android.text.TextUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class SearchBean {


    private List<AreaBean> list;
    private int maxPage;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<AreaBean> getList() {
        return list;
    }

    public void setList(List<AreaBean> list) {
        this.list = list;
    }
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public class AreaBean implements MultiItemEntity {
        /**
         * pkname : id
         * address : 上海欢乐谷
         * distance : 14
         * areaName : 欢乐谷
         * areaNum : GYM19082007140341
         */

        public static final int TYPE_CHANG = 1;
        public static final int TYPE_PRIVATE = 2;
        public static final int TYPE_TEAM = 3;
        private String pkname;
        private String address;
        private int distance;
        private String areaName;
        private String areaNum;
        private String teacherName;
        private String teacherNum;
        private int serviceDur;
        private String logoUrl;
        private String courseName;
        private String courseNum;
        private long startTime;
        private long endTime;


        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(String teacherNum) {
            this.teacherNum = teacherNum;
        }

        public int getServiceDur() {
            return serviceDur;
        }

        public void setServiceDur(int serviceDur) {
            this.serviceDur = serviceDur;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getPkname() {
            return pkname;
        }

        public void setPkname(String pkname) {
            this.pkname = pkname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaNum() {
            return areaNum;
        }

        public void setAreaNum(String areaNum) {
            this.areaNum = areaNum;
        }

        @Override
        public int getItemType() {

            if (!TextUtils.isEmpty(courseName)){
                return 3;
            }
            if (!TextUtils.isEmpty(areaNum)){
                return 1;
            }
            if (!TextUtils.isEmpty(teacherNum)){
                return 2;
            }

            return 0;

        }
    }
}
