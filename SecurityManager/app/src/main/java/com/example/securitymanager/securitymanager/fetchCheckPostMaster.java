package com.example.securitymanager.securitymanager;

public class fetchCheckPostMaster {
    private String ScheduleID;
    private String RoundsType;
    private String ScheduleType;
    private String WingType;
    private String No_Of_Post;

    public fetchCheckPostMaster(String roundsType,String wingType, String scheduleType, String no_of_CheckPost,String scheduleId,int i) {
        // System.out.println("title in class : "+title+" "+i);
        RoundsType=roundsType;
        ScheduleType=scheduleType;
        WingType=wingType;
        No_Of_Post=no_of_CheckPost;
        ScheduleID=scheduleId;

    }

    public String getRoundsType() {
        return RoundsType;
    }

    public void setRoundsType(String roundsType) {
        RoundsType = roundsType;
    }



    public String getWingType() {
        return WingType;
    }

    public void setWingType(String wingType) {
        WingType = wingType;
    }

    public String getScheduleType() {
        return ScheduleType;
    }

    public void setScheduleType(String scheduleType) {
        ScheduleType = scheduleType;
    }
    public String getNo_Of_Post() {
        return No_Of_Post;
    }

    public void setNo_Of_Post(String no_of_CheckPost) {
        No_Of_Post = no_of_CheckPost;
    }

    public String getScheduleID() {
        return ScheduleID;
    }

    public void setScheduleID(String scheduleId) {
        ScheduleID = scheduleId;
    }
}
