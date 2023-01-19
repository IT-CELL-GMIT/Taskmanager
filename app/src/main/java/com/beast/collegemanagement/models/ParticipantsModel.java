package com.beast.collegemanagement.models;

public class ParticipantsModel {

    String userName, profilePic, position,
    fullName, isSelected;

    public ParticipantsModel(String userName, String profilePic, String position, String fullName, String isSelected) {
        this.userName = userName;
        this.profilePic = profilePic;
        this.position = position;
        this.fullName = fullName;
        this.isSelected = isSelected;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
