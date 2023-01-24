package com.beast.collegemanagement.models;

public class TaskChatModel {

    String taskId,
    taskName,
    leaderName;

    public TaskChatModel(String taskId, String taskName, String leaderName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.leaderName = leaderName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
