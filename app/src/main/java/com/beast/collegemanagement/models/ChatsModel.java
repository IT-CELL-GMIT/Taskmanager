package com.beast.collegemanagement.models;

public class ChatsModel{

    String dateTime;
    String textMsg;
    String url;
    String type;
    String sender;
    String receiver;
    String name;

    public ChatsModel(String dateTime, String textMsg, String url, String type, String sender, String receiver, String name) {
        this.dateTime = dateTime;
        this.textMsg = textMsg;
        this.url = url;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
