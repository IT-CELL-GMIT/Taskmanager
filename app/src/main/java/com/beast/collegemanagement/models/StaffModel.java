package com.beast.collegemanagement.models;

public class StaffModel {

    String userId, username, fullName, profilePic, phoneNumber, eMail, password, position;

    public StaffModel(String userId, String username, String fullName, String profilePic, String phoneNumber, String eMail, String password, String position) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.password = password;
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
