package com.softel.securitymanager.securitymanager;

public class fetchcheckpost {
    private String CheckPost_Id;
    private String ScheduleType;
    private String RoundType;
    private String ScheduleTime;
    //private String CreatedBy;
    private String Round_Id;
    private String Total_Checkpost;
    // private String Entry_Gate;
    public fetchcheckpost(String roundType,String scheduleType,String scheduleTime, String checkPost_Id,String round_Id,String total_Checkpost,int i) {
        // System.out.println("title in class : "+title+" "+i);
        RoundType=roundType;
        ScheduleType=scheduleType;
        ScheduleTime=scheduleTime;
       // CreatedBy=createdBy;
        CheckPost_Id=checkPost_Id;
        Round_Id=round_Id;
        Total_Checkpost=total_Checkpost;
       // Time_Id=time_Id;

    }
    public String getRoundType() {
        return RoundType;
    }

    public void setRoundType(String roundType) {
        RoundType = roundType;
    }

    public String getScheduleType() {
        return ScheduleType;
    }

    public void setScheduleType(String scheduleType) {
        ScheduleType = scheduleType;
    }

    public String getScheduleTime() {
        return ScheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        ScheduleTime = scheduleTime;
    }

    public String getRound_id() {
        return Round_Id;
    }

    public void setRound_id(String round_Id) {
        Round_Id = round_Id;
    }

    public String getCheckPost_id() {
        return CheckPost_Id;
    }

    public void setCheckPost_id(String checkPost_Id) {
        CheckPost_Id = checkPost_Id;
    }

    public String getTotalCheckPost() {
        return Total_Checkpost;
    }

    public void setTotalCheckPost(String total_Checkpost) {
        Total_Checkpost = total_Checkpost;
    }




}
