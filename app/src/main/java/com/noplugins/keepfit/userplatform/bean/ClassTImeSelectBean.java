package com.noplugins.keepfit.userplatform.bean;

public class ClassTImeSelectBean {
    private String selectDay;
    private int selectType;
    private int selectItem;
    private String startTime;
    private String endTime;

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSelectDay() {
        return selectDay;
    }

    public void setSelectDay(String selectDay) {
        this.selectDay = selectDay;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }
}
