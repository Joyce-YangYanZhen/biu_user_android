package com.noplugins.keepfit.userplatform.bean;

import java.io.Serializable;

public class OrderBean implements Serializable {

    private String custUserNum;
    private String areaName;
    private String address;
    private int courseType;
    private String startTime;
    private String endTime;
    private double price;
    private double oldPrice;
    private String areaNum;

    private String courseNum;
    private String coachUserNum;
//    private int teacherTime;
    private String courseName;
    private String coachUserName;
    private String logo;
    private String difficulty;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getCustUserNum() {
        return custUserNum;
    }

    public void setCustUserNum(String custUserNum) {
        this.custUserNum = custUserNum;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

//    public int getTeacherTime() {
//        return teacherTime;
//    }
//
//    public void setTeacherTime(int teacherTime) {
//        this.teacherTime = teacherTime;
//    }

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
}
