package com.example.securitymanager.securitymanager;

public class allstafflist {
    private String InDate;
    private String Attendance;
    private String InTime;
    private String TotalHour;
    private String OutTime;
    private String OutDate;

    public allstafflist(String inDate, String attendance, String inTime, String totalHour, String outTime, String outDate)
    {
        InDate = inDate;
        Attendance = attendance;
        InTime = inTime;
        TotalHour = totalHour;
        OutTime = outTime;
        OutDate = outDate;
        }

    public String getInDate() {
        return InDate;
    }

    public void setInDate(String inDate) {
        InDate = inDate;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getTotalHour() {
        return TotalHour;
    }

    public void setTotalHour(String totalHour) {
        TotalHour = totalHour;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getOutDate() {
        return OutDate;
    }

    public void setOutDate(String outDate) {
        OutDate = outDate;
    }
}
