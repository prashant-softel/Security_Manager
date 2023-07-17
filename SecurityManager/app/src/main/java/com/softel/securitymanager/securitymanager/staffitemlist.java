package com.softel.securitymanager.securitymanager;

public class staffitemlist {
    private String Imagedesc;
    private String Unit_no;
    private String Owner_name;
    private String Staff_note;

    public staffitemlist(String imagedesc, String unit_no, String owner_name, String staff_note) {
        Imagedesc = imagedesc;
        Unit_no = unit_no;
        Owner_name = owner_name;
        Staff_note = staff_note;
    }

    public String getImagedesc() {
        return Imagedesc;
    }

    public void setImagedesc(String imagedesc) {
        Imagedesc = imagedesc;
    }

    public String getUnit_no() {
        return Unit_no;
    }

    public void setUnit_no(String unit_no) {
        Unit_no = unit_no;
    }

    public String getOwner_name() {
        return Owner_name;
    }

    public void setOwner_name(String owner_name) {
        Owner_name = owner_name;
    }

    public String getStaff_note() {
        return Staff_note;
    }

    public void setStaff_note(String staff_note) {
        Staff_note = staff_note;
    }
}
