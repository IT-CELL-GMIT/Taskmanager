package com.beast.collegemanagement.models;

public class ProjectListModel {

    String uniqueID, title,
    dateTime,
    leaderProfileImage,
    leaderUsername,
    projectStatus,
    closedDate;

    public ProjectListModel(String uniqueID, String title, String dateTime, String leaderProfileImage, String leaderUsername, String projectStatus, String closedDate) {
        this.uniqueID = uniqueID;
        this.title = title;
        this.dateTime = dateTime;
        this.leaderProfileImage = leaderProfileImage;
        this.leaderUsername = leaderUsername;
        this.projectStatus = projectStatus;
        this.closedDate = closedDate;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLeaderProfileImage() {
        return leaderProfileImage;
    }

    public void setLeaderProfileImage(String leaderProfileImage) {
        this.leaderProfileImage = leaderProfileImage;
    }

    public String getLeaderUsername() {
        return leaderUsername;
    }

    public void setLeaderUsername(String leaderUsername) {
        this.leaderUsername = leaderUsername;
    }
}
