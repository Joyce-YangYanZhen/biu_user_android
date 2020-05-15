package com.noplugins.keepfit.userplatform.bean;

import java.util.List;

public class ChangguanDetailBean {

    /**
     * picUrl : ["http://qnimg.ahcomg.com/20190731165812206.png","http://qnimg.ahcomg.com/20190731165812206.png","http://qnimg.ahcomg.com/20190731165812206.png"]
     * areaDetail : {"edxtendVar05":"","lowTimePrice":null,"edxtendVar02":"","edxtendVar01":"","edxtendVar04":"","companyName":"企业名称","edxtendVar03":"","picNum":"","type":1,"areaName":"场馆名称9","lowTimeStart":null,"logo":"","id":106,"taxpayerNum":"","longitude":121.468417,"manageDur":null,"area":900,"companyCode":"xxx223232","pkname":"id","phone":"123456","grade":960,"areaNum":"GYM19081634685143","gymUserNum":"","normalPrice":null,"highTimeEnd":null,"status":1,"tryout":7,"updateDate":"2019-08-16 11:21:45","extendBigint02":null,"extendBigint01":null,"highTimePrice":null,"extendBigint04":null,"distance":null,"extendBigint03":null,"latitude":31.208032,"extendBigint05":null,"remark":"","cardNum":"232323232","legalPerson":"法人姓名","haveSwim":1,"clickNum":0,"email":"232324@qq.com","createDate":"2019-08-16 11:21:45","lowTimeEnd":null,"address":"上海田子坊","charge":0,"businessStart":"12:00:00","finalGradle":9.6,"edxtendDec01":null,"deleted":0,"edxtendDec03":null,"edxtendDec02":null,"edxtendDec05":null,"edxtendDec04":null,"highTimeStart":null,"businessEnd":"20:00:00","facility":"有氧,无氧,团操,游泳"}
     * nowPrice : 20.0
     * teacherList : [{"updateDate":null,"lowestPrice":29,"teacherName":"卡卡西","skillList":null,"inviteStatus":null,"label":null,"logoUrl":null,"tips":null,"teacherType":null,"labelList":null,"password":null,"deleted":null,"phone":null,"skill":null,"gymInviteNum":null,"teacherNum":"Gen123","serviceDur":100,"card":null,"createDate":null},{"updateDate":null,"lowestPrice":0,"teacherName":"自来也","skillList":null,"inviteStatus":null,"label":null,"logoUrl":null,"tips":null,"teacherType":null,"labelList":null,"password":null,"deleted":null,"phone":null,"skill":null,"gymInviteNum":null,"teacherNum":"Gen234","serviceDur":100,"card":null,"createDate":null},{"updateDate":"2019-08-19 10:14:22","lowestPrice":0,"teacherName":"大蛇丸","skillList":null,"inviteStatus":null,"label":null,"logoUrl":null,"tips":null,"teacherType":1,"labelList":null,"password":null,"deleted":0,"phone":"123","skill":null,"gymInviteNum":null,"teacherNum":"Gen2345","serviceDur":100,"card":" ","createDate":"2019-08-19 10:14:22"},{"updateDate":"2019-08-19 10:14:22","lowestPrice":0,"teacherName":"纲手","skillList":null,"inviteStatus":null,"label":null,"logoUrl":null,"tips":null,"teacherType":1,"labelList":null,"password":null,"deleted":0,"phone":"123","skill":null,"gymInviteNum":null,"teacherNum":"Gen23456","serviceDur":100,"card":" ","createDate":"2019-08-19 10:14:22"}]
     * courseList : [{"gymAreaNum":"GYM19081634685143","reason":null,"courseType":1,"extendDec01":null,"extendDec02":null,"extendDec03":null,"type":"1","maxNum":20,"extendDec04":null,"extendVar03":null,"extendDec05":null,"extendVar04":null,"tips":null,"extendVar05":null,"extendVar01":null,"extendVar02":null,"price":3900,"loop":false,"id":4,"courseDes":null,"difficulty":1,"courseName":"卡卡西团课2","placeType":null,"courseNum":"GYM7790","classType":1,"status":1,"updateDate":"2019-08-19 10:48:38","extendBigint02":null,"comeNum":0,"extendBigint01":null,"extendBigint04":null,"extendBigint03":null,"extendBigint05":null,"finalPrice":39,"remark":null,"checkStatus":1,"lable":null,"startTime":1566276503000,"gymPlaceNum":"GYM19081657827460","createDate":"2019-08-19 10:48:38","teacherName":"卡卡西","applyNum":10,"target":1,"imgUrl":null,"deleted":false,"genTeacherNum":"Gen123","suitPerson":null,"endTime":1566280112000,"loopCycle":null,"courseTime":1}]
     */

    private AreaDetailBean areaDetail;
    private double nowPrice;
    private List<String> picUrl;
    private List<TeacherListBean> teacherList;
    private List<CourseListBean> courseList;
    /**
     * areaTime : {"gymArea":{"pkname":"id","lowTimePrice":1,"businessStart":"09:00:00","normalPrice":0,"businessEnd":"23:59:00"},"highTimeList":[{"gymAreaNum":"GYM19120426473653","updateDate":"2019-12-04 14:25:47","highTimePrice":2,"finalNormalPrice":null,"pkname":"id","gymTimeNum":"GYM19120441557634","deleted":0,"finalHighPrice":0.02,"normalPrice":1,"highTimeStart":"18:00:00","id":211,"highTimeEnd":"19:00:00","createDate":"2019-12-04 14:25:47"}]}
     */

    private AreaTimeBean areaTime;


    public AreaDetailBean getAreaDetail() {
        return areaDetail;
    }

    public void setAreaDetail(AreaDetailBean areaDetail) {
        this.areaDetail = areaDetail;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }

    public List<TeacherListBean> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherListBean> teacherList) {
        this.teacherList = teacherList;
    }

    public List<CourseListBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseListBean> courseList) {
        this.courseList = courseList;
    }

    public AreaTimeBean getAreaTime() {
        return areaTime;
    }

    public void setAreaTime(AreaTimeBean areaTime) {
        this.areaTime = areaTime;
    }

    public static class AreaDetailBean {
        /**
         * edxtendVar05 :
         * lowTimePrice : null
         * edxtendVar02 :
         * edxtendVar01 :
         * edxtendVar04 :
         * companyName : 企业名称
         * edxtendVar03 :
         * picNum :
         * type : 1
         * areaName : 场馆名称9
         * lowTimeStart : null
         * logo :
         * id : 106
         * taxpayerNum :
         * longitude : 121.468417
         * manageDur : null
         * area : 900
         * companyCode : xxx223232
         * pkname : id
         * phone : 123456
         * grade : 960
         * areaNum : GYM19081634685143
         * gymUserNum :
         * normalPrice : null
         * highTimeEnd : null
         * status : 1
         * tryout : 7
         * updateDate : 2019-08-16 11:21:45
         * extendBigint02 : null
         * extendBigint01 : null
         * highTimePrice : null
         * extendBigint04 : null
         * distance : null
         * extendBigint03 : null
         * latitude : 31.208032
         * extendBigint05 : null
         * remark :
         * cardNum : 232323232
         * legalPerson : 法人姓名
         * haveSwim : 1
         * clickNum : 0
         * email : 232324@qq.com
         * createDate : 2019-08-16 11:21:45
         * lowTimeEnd : null
         * address : 上海田子坊
         * charge : 0
         * businessStart : 12:00:00
         * finalGradle : 9.6
         * edxtendDec01 : null
         * deleted : 0
         * edxtendDec03 : null
         * edxtendDec02 : null
         * edxtendDec05 : null
         * edxtendDec04 : null
         * highTimeStart : null
         * businessEnd : 20:00:00
         * facility : 有氧,无氧,团操,游泳
         */

        private String edxtendVar05;
        private Object lowTimePrice;
        private String edxtendVar02;
        private String edxtendVar01;
        private String edxtendVar04;
        private String companyName;
        private String edxtendVar03;
        private String picNum;
        private int type;
        private String areaName;
        private Object lowTimeStart;
        private String logo;
        private int id;
        private String taxpayerNum;
        private double longitude;
        private Object manageDur;
        private int area;
        private String companyCode;
        private String pkname;
        private String phone;
        private int grade;
        private String areaNum;
        private String gymUserNum;
        private Object normalPrice;
        private Object highTimeEnd;
        private int status;
        private int tryout;
        private String updateDate;
        private Object extendBigint02;
        private Object extendBigint01;
        private Object highTimePrice;
        private Object extendBigint04;
        private Object distance;
        private Object extendBigint03;
        private double latitude;
        private Object extendBigint05;
        private String remark;
        private String cardNum;
        private String legalPerson;
        private int haveSwim;
        private int clickNum;
        private String email;
        private String createDate;
        private Object lowTimeEnd;
        private String address;
        private int charge;
        private String businessStart;
        private double finalGradle;
        private Object edxtendDec01;
        private int deleted;
        private Object edxtendDec03;
        private Object edxtendDec02;
        private Object edxtendDec05;
        private Object edxtendDec04;
        private Object highTimeStart;
        private String businessEnd;
        private String facility;
        private int collect;

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public String getEdxtendVar05() {
            return edxtendVar05;
        }

        public void setEdxtendVar05(String edxtendVar05) {
            this.edxtendVar05 = edxtendVar05;
        }

        public Object getLowTimePrice() {
            return lowTimePrice;
        }

        public void setLowTimePrice(Object lowTimePrice) {
            this.lowTimePrice = lowTimePrice;
        }

        public String getEdxtendVar02() {
            return edxtendVar02;
        }

        public void setEdxtendVar02(String edxtendVar02) {
            this.edxtendVar02 = edxtendVar02;
        }

        public String getEdxtendVar01() {
            return edxtendVar01;
        }

        public void setEdxtendVar01(String edxtendVar01) {
            this.edxtendVar01 = edxtendVar01;
        }

        public String getEdxtendVar04() {
            return edxtendVar04;
        }

        public void setEdxtendVar04(String edxtendVar04) {
            this.edxtendVar04 = edxtendVar04;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getEdxtendVar03() {
            return edxtendVar03;
        }

        public void setEdxtendVar03(String edxtendVar03) {
            this.edxtendVar03 = edxtendVar03;
        }

        public String getPicNum() {
            return picNum;
        }

        public void setPicNum(String picNum) {
            this.picNum = picNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public Object getLowTimeStart() {
            return lowTimeStart;
        }

        public void setLowTimeStart(Object lowTimeStart) {
            this.lowTimeStart = lowTimeStart;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTaxpayerNum() {
            return taxpayerNum;
        }

        public void setTaxpayerNum(String taxpayerNum) {
            this.taxpayerNum = taxpayerNum;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public Object getManageDur() {
            return manageDur;
        }

        public void setManageDur(Object manageDur) {
            this.manageDur = manageDur;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getPkname() {
            return pkname;
        }

        public void setPkname(String pkname) {
            this.pkname = pkname;
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

        public String getAreaNum() {
            return areaNum;
        }

        public void setAreaNum(String areaNum) {
            this.areaNum = areaNum;
        }

        public String getGymUserNum() {
            return gymUserNum;
        }

        public void setGymUserNum(String gymUserNum) {
            this.gymUserNum = gymUserNum;
        }

        public Object getNormalPrice() {
            return normalPrice;
        }

        public void setNormalPrice(Object normalPrice) {
            this.normalPrice = normalPrice;
        }

        public Object getHighTimeEnd() {
            return highTimeEnd;
        }

        public void setHighTimeEnd(Object highTimeEnd) {
            this.highTimeEnd = highTimeEnd;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTryout() {
            return tryout;
        }

        public void setTryout(int tryout) {
            this.tryout = tryout;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public Object getExtendBigint02() {
            return extendBigint02;
        }

        public void setExtendBigint02(Object extendBigint02) {
            this.extendBigint02 = extendBigint02;
        }

        public Object getExtendBigint01() {
            return extendBigint01;
        }

        public void setExtendBigint01(Object extendBigint01) {
            this.extendBigint01 = extendBigint01;
        }

        public Object getHighTimePrice() {
            return highTimePrice;
        }

        public void setHighTimePrice(Object highTimePrice) {
            this.highTimePrice = highTimePrice;
        }

        public Object getExtendBigint04() {
            return extendBigint04;
        }

        public void setExtendBigint04(Object extendBigint04) {
            this.extendBigint04 = extendBigint04;
        }

        public Object getDistance() {
            return distance;
        }

        public void setDistance(Object distance) {
            this.distance = distance;
        }

        public Object getExtendBigint03() {
            return extendBigint03;
        }

        public void setExtendBigint03(Object extendBigint03) {
            this.extendBigint03 = extendBigint03;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public Object getExtendBigint05() {
            return extendBigint05;
        }

        public void setExtendBigint05(Object extendBigint05) {
            this.extendBigint05 = extendBigint05;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }

        public String getLegalPerson() {
            return legalPerson;
        }

        public void setLegalPerson(String legalPerson) {
            this.legalPerson = legalPerson;
        }

        public int getHaveSwim() {
            return haveSwim;
        }

        public void setHaveSwim(int haveSwim) {
            this.haveSwim = haveSwim;
        }

        public int getClickNum() {
            return clickNum;
        }

        public void setClickNum(int clickNum) {
            this.clickNum = clickNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public Object getLowTimeEnd() {
            return lowTimeEnd;
        }

        public void setLowTimeEnd(Object lowTimeEnd) {
            this.lowTimeEnd = lowTimeEnd;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public String getBusinessStart() {
            return businessStart;
        }

        public void setBusinessStart(String businessStart) {
            this.businessStart = businessStart;
        }

        public double getFinalGradle() {
            return finalGradle;
        }

        public void setFinalGradle(double finalGradle) {
            this.finalGradle = finalGradle;
        }

        public Object getEdxtendDec01() {
            return edxtendDec01;
        }

        public void setEdxtendDec01(Object edxtendDec01) {
            this.edxtendDec01 = edxtendDec01;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public Object getEdxtendDec03() {
            return edxtendDec03;
        }

        public void setEdxtendDec03(Object edxtendDec03) {
            this.edxtendDec03 = edxtendDec03;
        }

        public Object getEdxtendDec02() {
            return edxtendDec02;
        }

        public void setEdxtendDec02(Object edxtendDec02) {
            this.edxtendDec02 = edxtendDec02;
        }

        public Object getEdxtendDec05() {
            return edxtendDec05;
        }

        public void setEdxtendDec05(Object edxtendDec05) {
            this.edxtendDec05 = edxtendDec05;
        }

        public Object getEdxtendDec04() {
            return edxtendDec04;
        }

        public void setEdxtendDec04(Object edxtendDec04) {
            this.edxtendDec04 = edxtendDec04;
        }

        public Object getHighTimeStart() {
            return highTimeStart;
        }

        public void setHighTimeStart(Object highTimeStart) {
            this.highTimeStart = highTimeStart;
        }

        public String getBusinessEnd() {
            return businessEnd;
        }

        public void setBusinessEnd(String businessEnd) {
            this.businessEnd = businessEnd;
        }

        public String getFacility() {
            return facility;
        }

        public void setFacility(String facility) {
            this.facility = facility;
        }
    }

    public static class TeacherListBean {
        /**
         * updateDate : null
         * lowestPrice : 29.0
         * teacherName : 卡卡西
         * skillList : null
         * inviteStatus : null
         * label : null
         * logoUrl : null
         * tips : null
         * teacherType : null
         * labelList : null
         * password : null
         * deleted : null
         * phone : null
         * skill : null
         * gymInviteNum : null
         * teacherNum : Gen123
         * serviceDur : 100
         * card : null
         * createDate : null
         */

        private Object updateDate;
        private double lowestPrice;
        private String teacherName;
        private Object skillList;
        private Object inviteStatus;
        private Object label;
        private Object logoUrl;
        private Object tips;
        private Object teacherType;
        private Object labelList;
        private Object password;
        private Object deleted;
        private Object phone;
        private Object skill;
        private Object gymInviteNum;
        private String teacherNum;
        private int serviceDur;
        private Object card;
        private Object createDate;

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(double lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public Object getSkillList() {
            return skillList;
        }

        public void setSkillList(Object skillList) {
            this.skillList = skillList;
        }

        public Object getInviteStatus() {
            return inviteStatus;
        }

        public void setInviteStatus(Object inviteStatus) {
            this.inviteStatus = inviteStatus;
        }

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }

        public Object getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(Object logoUrl) {
            this.logoUrl = logoUrl;
        }

        public Object getTips() {
            return tips;
        }

        public void setTips(Object tips) {
            this.tips = tips;
        }

        public Object getTeacherType() {
            return teacherType;
        }

        public void setTeacherType(Object teacherType) {
            this.teacherType = teacherType;
        }

        public Object getLabelList() {
            return labelList;
        }

        public void setLabelList(Object labelList) {
            this.labelList = labelList;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getDeleted() {
            return deleted;
        }

        public void setDeleted(Object deleted) {
            this.deleted = deleted;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getSkill() {
            return skill;
        }

        public void setSkill(Object skill) {
            this.skill = skill;
        }

        public Object getGymInviteNum() {
            return gymInviteNum;
        }

        public void setGymInviteNum(Object gymInviteNum) {
            this.gymInviteNum = gymInviteNum;
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

        public Object getCard() {
            return card;
        }

        public void setCard(Object card) {
            this.card = card;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }
    }

    public static class CourseListBean {
        /**
         * gymAreaNum : GYM19081634685143
         * reason : null
         * courseType : 1
         * extendDec01 : null
         * extendDec02 : null
         * extendDec03 : null
         * type : 1
         * maxNum : 20
         * extendDec04 : null
         * extendVar03 : null
         * extendDec05 : null
         * extendVar04 : null
         * tips : null
         * extendVar05 : null
         * extendVar01 : null
         * extendVar02 : null
         * price : 3900
         * loop : false
         * id : 4
         * courseDes : null
         * difficulty : 1
         * courseName : 卡卡西团课2
         * placeType : null
         * courseNum : GYM7790
         * classType : 1
         * status : 1
         * updateDate : 2019-08-19 10:48:38
         * extendBigint02 : null
         * comeNum : 0
         * extendBigint01 : null
         * extendBigint04 : null
         * extendBigint03 : null
         * extendBigint05 : null
         * finalPrice : 39.0
         * remark : null
         * checkStatus : 1
         * lable : null
         * startTime : 1566276503000
         * gymPlaceNum : GYM19081657827460
         * createDate : 2019-08-19 10:48:38
         * teacherName : 卡卡西
         * applyNum : 10
         * target : 1
         * imgUrl : null
         * deleted : false
         * genTeacherNum : Gen123
         * suitPerson : null
         * endTime : 1566280112000
         * loopCycle : null
         * courseTime : 1
         */

        private String gymAreaNum;
        private Object reason;
        private int courseType;
        private Object extendDec01;
        private Object extendDec02;
        private Object extendDec03;
        private String type;
        private int maxNum;
        private Object extendDec04;
        private Object extendVar03;
        private Object extendDec05;
        private Object extendVar04;
        private Object tips;
        private Object extendVar05;
        private Object extendVar01;
        private Object extendVar02;
        private int price;
        private boolean loop;
        private int id;
        private Object courseDes;
        private int difficulty;
        private String courseName;
        private Object placeType;
        private String courseNum;
        private int classType;
        private int status;
        private String updateDate;
        private Object extendBigint02;
        private int comeNum;
        private Object extendBigint01;
        private Object extendBigint04;
        private Object extendBigint03;
        private Object extendBigint05;
        private double finalPrice;
        private Object remark;
        private int checkStatus;
        private Object lable;
        private long startTime;
        private String gymPlaceNum;
        private String createDate;
        private String teacherName;
        private int applyNum;
        private int target;
        private String imgUrl;
        private boolean deleted;
        private String genTeacherNum;
        private Object suitPerson;
        private long endTime;
        private Object loopCycle;
        private int courseTime;

        public String getGymAreaNum() {
            return gymAreaNum;
        }

        public void setGymAreaNum(String gymAreaNum) {
            this.gymAreaNum = gymAreaNum;
        }

        public Object getReason() {
            return reason;
        }

        public void setReason(Object reason) {
            this.reason = reason;
        }

        public int getCourseType() {
            return courseType;
        }

        public void setCourseType(int courseType) {
            this.courseType = courseType;
        }

        public Object getExtendDec01() {
            return extendDec01;
        }

        public void setExtendDec01(Object extendDec01) {
            this.extendDec01 = extendDec01;
        }

        public Object getExtendDec02() {
            return extendDec02;
        }

        public void setExtendDec02(Object extendDec02) {
            this.extendDec02 = extendDec02;
        }

        public Object getExtendDec03() {
            return extendDec03;
        }

        public void setExtendDec03(Object extendDec03) {
            this.extendDec03 = extendDec03;
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

        public Object getExtendDec04() {
            return extendDec04;
        }

        public void setExtendDec04(Object extendDec04) {
            this.extendDec04 = extendDec04;
        }

        public Object getExtendVar03() {
            return extendVar03;
        }

        public void setExtendVar03(Object extendVar03) {
            this.extendVar03 = extendVar03;
        }

        public Object getExtendDec05() {
            return extendDec05;
        }

        public void setExtendDec05(Object extendDec05) {
            this.extendDec05 = extendDec05;
        }

        public Object getExtendVar04() {
            return extendVar04;
        }

        public void setExtendVar04(Object extendVar04) {
            this.extendVar04 = extendVar04;
        }

        public Object getTips() {
            return tips;
        }

        public void setTips(Object tips) {
            this.tips = tips;
        }

        public Object getExtendVar05() {
            return extendVar05;
        }

        public void setExtendVar05(Object extendVar05) {
            this.extendVar05 = extendVar05;
        }

        public Object getExtendVar01() {
            return extendVar01;
        }

        public void setExtendVar01(Object extendVar01) {
            this.extendVar01 = extendVar01;
        }

        public Object getExtendVar02() {
            return extendVar02;
        }

        public void setExtendVar02(Object extendVar02) {
            this.extendVar02 = extendVar02;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getCourseDes() {
            return courseDes;
        }

        public void setCourseDes(Object courseDes) {
            this.courseDes = courseDes;
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

        public Object getPlaceType() {
            return placeType;
        }

        public void setPlaceType(Object placeType) {
            this.placeType = placeType;
        }

        public String getCourseNum() {
            return courseNum;
        }

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
        }

        public int getClassType() {
            return classType;
        }

        public void setClassType(int classType) {
            this.classType = classType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public Object getExtendBigint02() {
            return extendBigint02;
        }

        public void setExtendBigint02(Object extendBigint02) {
            this.extendBigint02 = extendBigint02;
        }

        public int getComeNum() {
            return comeNum;
        }

        public void setComeNum(int comeNum) {
            this.comeNum = comeNum;
        }

        public Object getExtendBigint01() {
            return extendBigint01;
        }

        public void setExtendBigint01(Object extendBigint01) {
            this.extendBigint01 = extendBigint01;
        }

        public Object getExtendBigint04() {
            return extendBigint04;
        }

        public void setExtendBigint04(Object extendBigint04) {
            this.extendBigint04 = extendBigint04;
        }

        public Object getExtendBigint03() {
            return extendBigint03;
        }

        public void setExtendBigint03(Object extendBigint03) {
            this.extendBigint03 = extendBigint03;
        }

        public Object getExtendBigint05() {
            return extendBigint05;
        }

        public void setExtendBigint05(Object extendBigint05) {
            this.extendBigint05 = extendBigint05;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public Object getLable() {
            return lable;
        }

        public void setLable(Object lable) {
            this.lable = lable;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
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

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public String getGenTeacherNum() {
            return genTeacherNum;
        }

        public void setGenTeacherNum(String genTeacherNum) {
            this.genTeacherNum = genTeacherNum;
        }

        public Object getSuitPerson() {
            return suitPerson;
        }

        public void setSuitPerson(Object suitPerson) {
            this.suitPerson = suitPerson;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public Object getLoopCycle() {
            return loopCycle;
        }

        public void setLoopCycle(Object loopCycle) {
            this.loopCycle = loopCycle;
        }

        public int getCourseTime() {
            return courseTime;
        }

        public void setCourseTime(int courseTime) {
            this.courseTime = courseTime;
        }
    }

    public static class AreaTimeBean {
        /**
         * gymArea : {"pkname":"id","lowTimePrice":1,"businessStart":"09:00:00","normalPrice":0,"businessEnd":"23:59:00"}
         * highTimeList : [{"gymAreaNum":"GYM19120426473653","updateDate":"2019-12-04 14:25:47","highTimePrice":2,"finalNormalPrice":null,"pkname":"id","gymTimeNum":"GYM19120441557634","deleted":0,"finalHighPrice":0.02,"normalPrice":1,"highTimeStart":"18:00:00","id":211,"highTimeEnd":"19:00:00","createDate":"2019-12-04 14:25:47"}]
         */

        private GymAreaBean gymArea;
        private List<HighTimeListBean> highTimeList;
        /**
         * lowPrice : {"price":0.02,"startTime":"11:00:00","endTime":"12:00:00"}
         */

        private LowPriceBean lowPrice;


        public GymAreaBean getGymArea() {
            return gymArea;
        }

        public void setGymArea(GymAreaBean gymArea) {
            this.gymArea = gymArea;
        }

        public List<HighTimeListBean> getHighTimeList() {
            return highTimeList;
        }

        public void setHighTimeList(List<HighTimeListBean> highTimeList) {
            this.highTimeList = highTimeList;
        }

        public LowPriceBean getLowPrice() {
            return lowPrice;
        }

        public void setLowPrice(LowPriceBean lowPrice) {
            this.lowPrice = lowPrice;
        }

        public static class GymAreaBean {
            /**
             * pkname : id
             * lowTimePrice : 1
             * businessStart : 09:00:00
             * normalPrice : 0
             * businessEnd : 23:59:00
             */

            private String pkname;
            private double lowTimePrice;
            private String businessStart;
            private double normalPrice;
            private String businessEnd;

            public String getPkname() {
                return pkname;
            }

            public void setPkname(String pkname) {
                this.pkname = pkname;
            }

            public double getLowTimePrice() {
                return lowTimePrice;
            }

            public void setLowTimePrice(double lowTimePrice) {
                this.lowTimePrice = lowTimePrice;
            }

            public String getBusinessStart() {
                return businessStart;
            }

            public void setBusinessStart(String businessStart) {
                this.businessStart = businessStart;
            }

            public double getNormalPrice() {
                return normalPrice;
            }

            public void setNormalPrice(double normalPrice) {
                this.normalPrice = normalPrice;
            }

            public String getBusinessEnd() {
                return businessEnd;
            }

            public void setBusinessEnd(String businessEnd) {
                this.businessEnd = businessEnd;
            }
        }

        public static class HighTimeListBean {
            /**
             * gymAreaNum : GYM19120426473653
             * updateDate : 2019-12-04 14:25:47
             * highTimePrice : 2
             * finalNormalPrice : null
             * pkname : id
             * gymTimeNum : GYM19120441557634
             * deleted : 0
             * finalHighPrice : 0.02
             * normalPrice : 1
             * highTimeStart : 18:00:00
             * id : 211
             * highTimeEnd : 19:00:00
             * createDate : 2019-12-04 14:25:47
             */

            private String gymAreaNum;
            private String updateDate;
            private double highTimePrice;
            private Object finalNormalPrice;
            private String pkname;
            private String gymTimeNum;
            private int deleted;
            private double finalHighPrice;
            private double normalPrice;
            private String highTimeStart;
            private int id;
            private String highTimeEnd;
            private String createDate;

            public String getGymAreaNum() {
                return gymAreaNum;
            }

            public void setGymAreaNum(String gymAreaNum) {
                this.gymAreaNum = gymAreaNum;
            }

            public String getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

            public double getHighTimePrice() {
                return highTimePrice;
            }

            public void setHighTimePrice(double highTimePrice) {
                this.highTimePrice = highTimePrice;
            }

            public Object getFinalNormalPrice() {
                return finalNormalPrice;
            }

            public void setFinalNormalPrice(Object finalNormalPrice) {
                this.finalNormalPrice = finalNormalPrice;
            }

            public String getPkname() {
                return pkname;
            }

            public void setPkname(String pkname) {
                this.pkname = pkname;
            }

            public String getGymTimeNum() {
                return gymTimeNum;
            }

            public void setGymTimeNum(String gymTimeNum) {
                this.gymTimeNum = gymTimeNum;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public double getFinalHighPrice() {
                return finalHighPrice;
            }

            public void setFinalHighPrice(double finalHighPrice) {
                this.finalHighPrice = finalHighPrice;
            }

            public double getNormalPrice() {
                return normalPrice;
            }

            public void setNormalPrice(double normalPrice) {
                this.normalPrice = normalPrice;
            }

            public String getHighTimeStart() {
                return highTimeStart;
            }

            public void setHighTimeStart(String highTimeStart) {
                this.highTimeStart = highTimeStart;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getHighTimeEnd() {
                return highTimeEnd;
            }

            public void setHighTimeEnd(String highTimeEnd) {
                this.highTimeEnd = highTimeEnd;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }
        }

        public static class LowPriceBean {
            /**
             * price : 0.02
             * startTime : 11:00:00
             * endTime : 12:00:00
             */

            private double price;
            private String startTime;
            private String endTime;

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
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
        }
    }
}
