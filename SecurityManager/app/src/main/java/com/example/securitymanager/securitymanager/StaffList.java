package com.example.securitymanager.securitymanager;

public class StaffList {
    private String Name;
    private String JobProfile;
    private String image;
    private String Phoneno;
    private String StaffID;
    public StaffList(String staffID,String name, String jobProfile, String image,String phoneno) {
        StaffID=staffID;
        Name = name;
        JobProfile = jobProfile;
        this.image = image;
        Phoneno=phoneno;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String StaffID) {
        this.StaffID = StaffID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getJobProfile() {
        return JobProfile;
    }

    public void setJobProfile(String jobProfile) {
        JobProfile = jobProfile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String Phoneno) {
        this.Phoneno = Phoneno;
    }
}
