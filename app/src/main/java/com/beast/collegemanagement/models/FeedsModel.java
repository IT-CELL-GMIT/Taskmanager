package com.beast.collegemanagement.models;

public class FeedsModel {

    String
    feedId,
    userName,
    position,
    profilePic,
    feedText,
    timeDate,
    visibility;


    public FeedsModel(String feedId, String userName, String position, String profilePic, String feedText, String timeDate, String visibility) {
        this.feedId = feedId;
        this.userName = userName;
        this.position = position;
        this.profilePic = profilePic;
        this.feedText = feedText;
        this.timeDate = timeDate;
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFeedText() {
        return feedText;
    }

    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }
}
