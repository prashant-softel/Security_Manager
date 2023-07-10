package com.example.securitymanager.securitymanager;

public class UnitCusttom {
    private String UnitId ;
    private String OwnerName;
    private String UnitNo;
    private String OwnerContact;
    private String WingId;
    private String Tenant_Name;
    private String Name;

    public UnitCusttom(String unitId, String ownerName, String unitNo,String ownerContact, String wingId) {
        UnitId = unitId;
        OwnerName = ownerName;
        UnitNo = unitNo;
        OwnerContact = ownerContact;
    }

    public UnitCusttom(String unitId, String ownerName, String unitNo,String ownerContact, String wingId,String tenant_name,String name) {
        UnitId = unitId;
        OwnerName = ownerName;
        UnitNo = unitNo;
        OwnerContact = ownerContact;
        Tenant_Name = tenant_name;
        Name = name;

    }


    public UnitCusttom() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTenant_Name() {
        return Tenant_Name;
    }

    public void setTenant_Name(String tenant_Name) {
        Tenant_Name = tenant_Name;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getOwnerContact() {
        return OwnerContact;
    }

    public void setOwnerContact(String ownerContact) {
        OwnerContact = ownerContact;
    }
    public String getWingId() {
        return WingId;
    }

    public void setWingId(String wingId) {
        WingId = wingId;
    }
}
