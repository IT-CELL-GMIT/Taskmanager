package com.beast.collegemanagement.models;

public class ObserverTaskModel {

    String userName,
    fullName,
    profilePic;

    public ObserverTaskModel(String userName, String fullName, String profilePic) {
        this.userName = userName;
        this.fullName = fullName;
        this.profilePic = profilePic;
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
}

