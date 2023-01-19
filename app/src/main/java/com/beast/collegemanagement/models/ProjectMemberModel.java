package com.beast.collegemanagement.models;

public class ProjectMemberModel {

    String userName,
    profilePic,
    position;

    public ProjectMemberModel(String userName, String profilePic, String position) {
        this.userName = userName;
        this.profilePic = profilePic;
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
