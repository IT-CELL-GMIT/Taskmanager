package com.beast.collegemanagement.models;

public class FreeDriveModel {

    String
    link,
    timeDate,
    userName;

    public FreeDriveModel(String link, String timeDate, String userName) {
        this.link = link;
        this.timeDate = timeDate;
        this.userName = userName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
