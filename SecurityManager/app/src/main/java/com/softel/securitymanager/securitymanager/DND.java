package com.softel.securitymanager.securitymanager;

public class DND {

    private int dnd_id;
    private int unit_id;
    private String dnd_msg;
    private int unit_no;
    private int dnd_type;

    public DND() {
    }

    public DND(int dnd_id, int unit_id, String dnd_msg, int unit_no, int dnd_type) {
        this.dnd_id = dnd_id;
        this.unit_id = unit_id;
        this.dnd_msg = dnd_msg;
        this.unit_no = unit_no;
        this.dnd_type = dnd_type;
    }

    public int getDnd_id() {
        return dnd_id;
    }

    public void setDnd_id(int dnd_id) {
        this.dnd_id = dnd_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public String getDnd_msg() {
        return dnd_msg;
    }

    public void setDnd_msg(String dnd_msg) {
        this.dnd_msg = dnd_msg;
    }

    public int getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(int unit_no) {
        this.unit_no = unit_no;
    }

    public int getDnd_type() {
        return dnd_type;
    }

    public void setDnd_type(int dnd_type) {
        this.dnd_type = dnd_type;
    }
}
