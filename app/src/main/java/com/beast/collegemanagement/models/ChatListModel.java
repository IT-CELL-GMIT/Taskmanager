package com.beast.collegemanagement.models;

public class ChatListModel {

    String userId,
    userName,
    fullName,
    profilePic,
    phoneNumber,
    eMail,
    lastOnline,
    position,
    noOfMsgs,
    lastMsg,
    chatType;

    public ChatListModel(String userId, String userName, String fullName, String profilePic, String phoneNumber, String eMail, String lastOnline, String position, String noOfMsgs, String lastMsg, String chatType) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.lastOnline = lastOnline;
        this.position = position;
        this.noOfMsgs = noOfMsgs;
        this.lastMsg = lastMsg;
        this.chatType = chatType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNoOfMsgs() {
        return noOfMsgs;
    }

    public void setNoOfMsgs(String noOfMsgs) {
        this.noOfMsgs = noOfMsgs;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
