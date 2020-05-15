package com.noplugins.keepfit.userplatform.entity;

public class CheckInEntity {

    /**
     * pkname : id
     * id : 11
     * custOrderItemNum : CUS19082772331131
     * custOrderNum : CUS19082764103636
     * areaNum : GYM19081515171364
     * custUserNum : CUS19081905171695
     * courseNum : GYM77902
     * coachUserNum : Gen123
     * areaName : 123
     * address : 课程地址
     * courseType : 1
     * courseName : 课程名称
     * coachUserName : 课程老师
     * checkIn : 0
     * checkOut : 0
     * startTime : 2019-09-04 14:05:43
     * price : 2000
     * status : 0
     * createDate : 2019-08-27 15:35:27
     * updateDate : 2019-08-27 15:35:27
     * beforeFace : 0
     * afterFace : 0
     */

    private String pkname;
    private int id;
    private String custOrderItemNum;
    private String custOrderNum;
    private String areaNum;
    private String custUserNum;
    private String courseNum;
    private String coachUserNum;
    private String areaName;
    private String address;
    private int courseType;
    private String courseName;
    private String coachUserName;
    private int checkIn;
    private int checkOut;
    private String startTime;
    private int price;
    private int status;
    private String createDate;
    private String updateDate;
    private int beforeFace;
    private int afterFace;

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

    public String getCustOrderItemNum() {
        return custOrderItemNum;
    }

    public void setCustOrderItemNum(String custOrderItemNum) {
        this.custOrderItemNum = custOrderItemNum;
    }

    public String getCustOrderNum() {
        return custOrderNum;
    }

    public void setCustOrderNum(String custOrderNum) {
        this.custOrderNum = custOrderNum;
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

    public int getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(int checkIn) {
        this.checkIn = checkIn;
    }

    public int getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(int checkOut) {
        this.checkOut = checkOut;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getBeforeFace() {
        return beforeFace;
    }

    public void setBeforeFace(int beforeFace) {
        this.beforeFace = beforeFace;
    }

    public int getAfterFace() {
        return afterFace;
    }

    public void setAfterFace(int afterFace) {
        this.afterFace = afterFace;
    }
}
