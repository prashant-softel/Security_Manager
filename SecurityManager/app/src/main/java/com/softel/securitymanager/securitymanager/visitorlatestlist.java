package com.softel.securitymanager.securitymanager;

public class visitorlatestlist {

    private String Indate;
    private String Intime;
    private String Unitno;
    private String Outdate;
    private String Outtime;
    private  String Checkout;
    private  String Entrygate;
    private String Exitgate;

    public visitorlatestlist(String indate, String intime, String unitno, String outdate, String outtime, String checkout, String entrygate,String exitgate) {
        System.out.println("in class : "+unitno);
        Indate = indate;
        Intime = intime;
        Unitno = unitno;
        Outdate = outdate;
        Outtime = outtime;
        Checkout = checkout;
        Entrygate=entrygate;
        Exitgate = exitgate;
    }

    public String getEntrygate() {
        return Entrygate;
    }

    public void setEntrygate(String entrygate) {
        Entrygate = entrygate;
    }

    public String getIndate() {
        return Indate;
    }

    public void setIndate(String indate) {
        Indate = indate;
    }

    public String getIntime() {
        return Intime;
    }

    public void setIntime(String intime) {
        Intime = intime;
    }

    public String getUnitno() {
        return Unitno;
    }

    public void setUnitno(String unitno) {
        Unitno = unitno;
    }

    public String getOutdate() {
        return Outdate;
    }

    public void setOutdate(String outdate) {
        Outdate = outdate;
    }

    public String getOuttime() {
        return Outtime;
    }

    public void setOuttime(String outtime) {
        Outtime = outtime;
    }

    public String getCheckout() {
        return Checkout;
    }

    public void setCheckout(String checkout) {
        Checkout = checkout;
    }

    public String getExitgate() {
        return Exitgate;
    }

    public void setExitgate(String exitgate) {
        Exitgate = exitgate;
    }
}
