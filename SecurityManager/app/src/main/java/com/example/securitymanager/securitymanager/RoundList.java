package com.example.securitymanager.securitymanager;


public class RoundList {
    private String ChekpostName;
    private String CheckPostCode;
    private String CPostId;
    private int I;
    public RoundList(String checkpostName, String checkpostCode,String cpostId,int i) {

        this.ChekpostName = checkpostName;
        this.CheckPostCode = checkpostCode;
        this.CPostId = cpostId;
        this.I=i;
    }

    public int getI() {
        return I;
    }

    public void setI(int i) {
        I = i;
    }

    public String getCheckPostName() {
        return ChekpostName;
    }

    public void setCheckPostName(String checkpostName) {
        this.ChekpostName = checkpostName;
    }

    public String getCheckPostCode() {
        return CheckPostCode;
    }

    public void setCheckPostCode(String checkpostCode) {
        this.CheckPostCode = checkpostCode;
    }

    public String getCPostID() {
        return CPostId;
    }

    public void setCPostID(String cpostId) {
        this.CPostId = cpostId;
    }


}
