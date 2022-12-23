package com.beast.collegemanagement.models;

public class CommentModel {

    String userName
            , fullName
            , profilePic
            , timeDate
            , content
            , type
            , link
            , uniqueTaskID;

    public CommentModel(String userName, String fullName, String profilePic, String timeDate, String content, String type, String link, String uniqueTaskID) {
        this.userName = userName;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.timeDate = timeDate;
        this.content = content;
        this.type = type;
        this.link = link;
        this.uniqueTaskID = uniqueTaskID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUniqueTaskID() {
        return uniqueTaskID;
    }

    public void setUniqueTaskID(String uniqueTaskID) {
        this.uniqueTaskID = uniqueTaskID;
    }
}
