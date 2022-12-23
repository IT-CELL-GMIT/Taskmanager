package com.beast.collegemanagement.models;

public class TaskListModel {

    String uniqueID, title,
    leaderName,
    leaderProfileImg,
    coLeaderProfileImg,
    time,
    date,
    status,
    completeDate;

    public TaskListModel(String uniqueID, String title, String leaderName, String leaderProfileImg, String coLeaderProfileImg, String time, String date, String status, String completeDate) {
        this.uniqueID = uniqueID;
        this.title = title;
        this.leaderName = leaderName;
        this.leaderProfileImg = leaderProfileImg;
        this.coLeaderProfileImg = coLeaderProfileImg;
        this.time = time;
        this.date = date;
        this.status = status;
        this.completeDate = completeDate;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderProfileImg() {
        return leaderProfileImg;
    }

    public void setLeaderProfileImg(String leaderProfileImg) {
        this.leaderProfileImg = leaderProfileImg;
    }

    public String getCoLeaderProfileImg() {
        return coLeaderProfileImg;
    }

    public void setCoLeaderProfileImg(String coLeaderProfileImg) {
        this.coLeaderProfileImg = coLeaderProfileImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
