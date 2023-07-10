package com.example.securitymanager.securitymanager;

public class stafflatestlist {

    private String Date;
    private String Intime;
    private String Outtime;
    private String Attendance;

    public stafflatestlist(String date, String intime, String outtime, String attendance) {

        Date = date;
        Intime = intime;
        Outtime = outtime;
        Attendance = attendance;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIntime() {
        return Intime;
    }

    public void setIntime(String intime) {
        Intime = intime;
    }

    public String getOuttime() {
        return Outtime;
    }

    public void setOuttime(String outtime) {
        Outtime = outtime;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }
}
