package com.beast.collegemanagement.models;

public class AddStaffModel {

    String id, userName, fullName, profilePic;

    public AddStaffModel(String id, String userName, String fullName, String profilePic) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
