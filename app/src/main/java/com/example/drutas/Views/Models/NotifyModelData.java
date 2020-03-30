package com.example.drutas.Views.Models;

import java.util.ArrayList;

public class NotifyModelData {
    private String LeaveType;
    private String NotifyDate;
    private String Reason;
    private String StartDate;
    private String EndDate;
    private ArrayList<Timermodel> timermodel;

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getNotifyDate() {
        return NotifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        NotifyDate = notifyDate;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public ArrayList<Timermodel> getTimermodel() {
        return timermodel;
    }

    public void setTimermodel(ArrayList<Timermodel> timermodel) {
        this.timermodel = timermodel;
    }


}
