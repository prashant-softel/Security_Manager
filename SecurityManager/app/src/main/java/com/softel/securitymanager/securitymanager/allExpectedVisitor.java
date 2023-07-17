package com.softel.securitymanager.securitymanager;

public class allExpectedVisitor {
    private String Fname;
    private  String Lname;
    private String Mobile;
    private String UnitNo;
    private String ExpectedDate;
    private String ExpectedTime;
    private String PurposeName;
    private String CurrentIn;
    private String Wing;
    private String OwnerName;

    public allExpectedVisitor(String fname, String lname, String mobile, String unitNo, String expectedDate, String expectedTime, String purposeName, String currentIn,String wing, String ownerName) {
        Fname = fname;
        Lname = lname;
        Mobile = mobile;
        UnitNo = unitNo;
        ExpectedDate = expectedDate;
        ExpectedTime = expectedTime;
        PurposeName = purposeName;
        CurrentIn = currentIn;
        Wing = wing;
        OwnerName = ownerName;
    }



    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getExpectedDate() {
        return ExpectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        ExpectedDate = expectedDate;
    }

    public String getExpectedTime() {
        return ExpectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        ExpectedTime = expectedTime;
    }

    public String getPurposeName() {
        return PurposeName;
    }

    public void setPurposeName(String purposeName) {
        PurposeName = purposeName;
    }

    public String getCurrentIn() {
        return CurrentIn;
    }

    public void setCurrentIn(String currentIn) {
        CurrentIn = currentIn;
    }
    public String getWing() {
        return Wing;
    }

    public void setWing(String wing) {
        Wing = wing;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }
}
