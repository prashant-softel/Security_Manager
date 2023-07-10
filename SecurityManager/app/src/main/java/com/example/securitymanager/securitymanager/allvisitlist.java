package com.example.securitymanager.securitymanager;

public class allvisitlist
{
    private String Id;
    private String Name;
    private String Indate;
    private String Intime;
    private String Unitno;
    private String Outdate;
    private String Outtime;
    private String Checkout;
    private String Entrygate;
    private String Exitgate;
    private String Check;

    public allvisitlist(String id,String name, String indate, String intime, String unitno, String outdate, String outtime, String checkout, String entrygate, String exitgate, String check) {

        Id=id;

        Indate = indate;
        Intime = intime;
        Unitno = unitno;
        Outdate = outdate;
        Outtime = outtime;
        Checkout = checkout;
        Entrygate = entrygate;
        Exitgate = exitgate;
        Check = check;
        Name = name;

        System.out.println(Id + " "+Name+" " + Indate + " "+ Intime + " " + Unitno + " " + Outdate + " "+ Outtime + " "+ Checkout+ " "+ Entrygate + " "+ Exitgate + " "+ Check);

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getEntrygate() {
        return Entrygate;
    }

    public void setEntrygate(String entrygate) {
        Entrygate = entrygate;
    }

    public String getExitgate() {
        return Exitgate;
    }

    public void setExitgate(String exitgate) {
        Exitgate = exitgate;
    }

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }
}
