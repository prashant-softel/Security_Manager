package com.example.securitymanager.securitymanager;

public class fetchschedule {
    private String Schedule_Id;
    private String Round_Type;
    private String Wing_Type;
    private String Schedule_Type;
    private String No_of_Checkpost;

    public fetchschedule(String schedule_id,String round_type, String wing, String schedule_type, String no_of_checkpost)
    {
        Schedule_Id=schedule_id;
        Round_Type = round_type;
        Wing_Type = wing;
        Schedule_Type = schedule_type;
        No_of_Checkpost = no_of_checkpost;
        //System.out.println("Schedule_Id"+Schedule_Id);
    }
    public String getCheckpost_id() {
        return Schedule_Id;
    }

    public void setCheckpost_id(String schedule_id) {
        Schedule_Id = schedule_id;
    }

    public String getRound_Type() {
        return Round_Type;
    }

    public void setRound_Type(String round_type) {
        Round_Type = round_type;
    }

    public String getWing_Type() {
        return Wing_Type;
    }

    public void setWing_Type(String wing) {
        Wing_Type = wing;
    }

    public String getSchedule_Type() {
        return Schedule_Type;
    }

    public void setSchedule_Type(String schedule_type) {
        Schedule_Type = schedule_type;
    }

    public String getNo_of_Checkpost() {
        return No_of_Checkpost;
    }

    public void setNo_of_Checkpost(String no_of_checkpost) {
        No_of_Checkpost = no_of_checkpost;
    }


}
