package com.beast.collegemanagement.models;

public class SubTaskModel {

    String
            subTaskId,
            taskId,
    title,
    startDate,
    status,
    completeDate;

    public SubTaskModel(String subTaskId, String taskId, String title, String startDate, String status, String completeDate) {
        this.subTaskId = subTaskId;
        this.taskId = taskId;
        this.title = title;
        this.startDate = startDate;
        this.status = status;
        this.completeDate = completeDate;
    }

    public String getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
}
