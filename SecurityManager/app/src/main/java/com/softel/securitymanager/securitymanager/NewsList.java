package com.softel.securitymanager.securitymanager;

public class NewsList {

    private String Title;
    private String Desc;
    private int I;

    public NewsList(String title, String desc,int i) {
        System.out.println("title in class : "+title+" "+i);

        this.Title = title;
        this.Desc = desc;
        this.I=i;
    }

    public int getI() {
        return I;
    }

    public void setI(int i) {
        I = i;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        this.Desc = desc;
    }
}
