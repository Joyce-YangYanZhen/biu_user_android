package com.noplugins.keepfit.userplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MessageDateEntity {

    private List<ListBean> list;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    private int maxPage;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * pkname : id
         * id : 2
         * messageNum : CUS3
         * gymUserNum : CUS19081922275624
         * messageCon : 系统消息测试
         * withdrawTime : 2019-08-2411: 53: 46
         * readStatus : 0
         * type : 1
         * messageTime : 2019-08-24 12: 53: 46
         * createDate : 2019-08-24 12: 53: 46
         * updateDate : 2019-08-24 11: 53: 46
         * deleted : 0
         */

        private String pkname;
        private int id;
        private String messageNum;
        private String gymUserNum;
        private String messageCon;
        private String withdrawTime;
        private int readStatus;
        private int type;
        private String messageTime;
        private String createDate;
        private String updateDate;
        private int deleted;

        public String getCustOrderNum() {
            return custOrderNum;
        }

        public void setCustOrderNum(String custOrderNum) {
            this.custOrderNum = custOrderNum;
        }

        private String custOrderNum;

        public String getCustSportLogNum() {
            return custSportLogNum;
        }

        public void setCustSportLogNum(String custSportLogNum) {
            this.custSportLogNum = custSportLogNum;
        }

        private String custSportLogNum;

        public ListBean(Parcel in) {
            pkname = in.readString();
            id = in.readInt();
            messageNum = in.readString();
            gymUserNum = in.readString();
            messageCon = in.readString();
            withdrawTime = in.readString();
            readStatus = in.readInt();
            type = in.readInt();
            messageTime = in.readString();
            createDate = in.readString();
            updateDate = in.readString();
            deleted = in.readInt();
            custSportLogNum = in.readString();
        }

        public ListBean() {

        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };

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

        public String getMessageNum() {
            return messageNum;
        }

        public void setMessageNum(String messageNum) {
            this.messageNum = messageNum;
        }

        public String getGymUserNum() {
            return gymUserNum;
        }

        public void setGymUserNum(String gymUserNum) {
            this.gymUserNum = gymUserNum;
        }

        public String getMessageCon() {
            return messageCon;
        }

        public void setMessageCon(String messageCon) {
            this.messageCon = messageCon;
        }

        public String getWithdrawTime() {
            return withdrawTime;
        }

        public void setWithdrawTime(String withdrawTime) {
            this.withdrawTime = withdrawTime;
        }

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(String messageTime) {
            this.messageTime = messageTime;
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

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.pkname);
            parcel.writeInt(this.id);
            parcel.writeString(this.messageNum);
            parcel.writeString(this.gymUserNum);
            parcel.writeString(this.messageCon);
            parcel.writeString(this.withdrawTime);
            parcel.writeInt(this.readStatus);
            parcel.writeInt(this.type);
            parcel.writeString(this.messageTime);
            parcel.writeString(this.createDate);
            parcel.writeString(this.updateDate);
            parcel.writeInt(this.deleted);
            parcel.writeString(this.custSportLogNum);

        }
    }
}
