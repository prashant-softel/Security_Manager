package com.softel.securitymanager.securitymanager;

public class stafffetch {

    private String Staff_id;
    private String Full_Name;
    private String Entry_Profile;
    private String Date;
    private String InTime;
    private String Entry_Gate;

    public stafffetch(String staff_id,String full_Name, String entry_Profile, String date, String inTime, String entry_Gate)
    {
        Staff_id=staff_id;
        Full_Name = full_Name;
        Entry_Profile = entry_Profile;
        Date = date;
        InTime = inTime;
        Entry_Gate = entry_Gate;
    }

    public String getStaff_id() {
        return Staff_id;
    }

    public void setStaff_id(String staff_id) {
        Staff_id = staff_id;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getEntry_Profile() {
        return Entry_Profile;
    }

    public void setEntry_Profile(String entry_Profile) {
        Entry_Profile = entry_Profile;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getEntry_Gate() {
        return Entry_Gate;
    }

    public void setEntry_Gate(String entry_Gate) {
        Entry_Gate = entry_Gate;
    }
}
