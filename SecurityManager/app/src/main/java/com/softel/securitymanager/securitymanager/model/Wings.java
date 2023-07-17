package com.softel.securitymanager.securitymanager.model;

import java.util.ArrayList;

public class Wings {

    String society_id,wing,wing_id,timestamp,status;
    ArrayList<Flats> flatsArrayList;

    public Wings() {
    }

    public ArrayList<Flats> getFlatsArrayList() {
        return flatsArrayList;
    }

    public void setFlatsArrayList(ArrayList<Flats> flatsArrayList) {
        this.flatsArrayList = flatsArrayList;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getWing_id() {
        return wing_id;
    }

    public void setWing_id(String wing_id) {
        this.wing_id = wing_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return wing;
    }
}
