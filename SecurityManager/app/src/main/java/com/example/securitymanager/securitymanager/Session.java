package com.example.securitymanager.securitymanager;

import android.content.SharedPreferences;
import android.content.Context;
import  android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setkiosk(String kiosk) {
        prefs.edit().putString("kiosk", kiosk).commit();
    }

    public String getkiosk() {
        String kiosk = prefs.getString("kiosk","");
        return kiosk;
    }
    public void setkioskv(String kioskv) {
        prefs.edit().putString("kioskv", kioskv).commit();
    }

    public String getkioskv() {
        String kioskv = prefs.getString("kioskv","");
        return kioskv;
    }


    public void setRole(String role) {
        prefs.edit().putString("role", role).commit();
    }

    public String getRole() {
        String role = prefs.getString("role","");
        return role;
    }
    public void setid(String id) {
        prefs.edit().putString("id", id).commit();
    }

    public String getid() {
        String id = prefs.getString("id","");
        return id;
    }
    public String getSocietyid() {
        String Societyid = prefs.getString("Societyid","");
        return Societyid;
    }
    public void setSocietyid(String Societyid) {
        prefs.edit().putString("Societyid", Societyid).commit();
    }
    public String getSocietyName() {
        String SocietyName = prefs.getString("SocietyName","");
        return SocietyName;
    }
    public void setSocietyName(String SocietyName) {
        prefs.edit().putString("SocietyName", SocietyName).commit();
    }
    public String getName() {
        String UserName = prefs.getString("UserName","");
        return UserName;
    }
    public void setName(String UserName) {
        prefs.edit().putString("UserName", UserName).commit();
    }
    public String getGateNo()
    {
        String GateNo = prefs.getString("GateNo","");
        return GateNo;
    }
    public void setGateNo(String GateNo)
    {
        prefs.edit().putString("GateNo", GateNo).commit();
    }
    /* --------------------------  OTP SECTION ----------------------------- */
    public String getExistVisitor()
    {
        String OTPExist = prefs.getString("OTPExistingVisitor","");
        return OTPExist;
    }
    public void setExistVisitor(String OTPExist)
    {
        prefs.edit().putString("OTPExistingVisitor", OTPExist).commit();
    }

    public String getExpVisitor()
    {
        String OTPExp = prefs.getString("OTPExpectedVisitor","");
        return OTPExp;
    }
    public void setExpVisitor(String OTPExp)
    {
        prefs.edit().putString("OTPExpectedVisitor", OTPExp).commit();
    }
    public String getNewVisitor()
    {
        String OTPNew = prefs.getString("OTPNewVisitor","");
        return OTPNew;
    }
    public void setNewVisitor(String OTPNew)
    {
        prefs.edit().putString("OTPNewVisitor", OTPNew).commit();
    }

    /* ----------------------------  set Society Emegency Contacy No -----------------------  */
    public String getEmegency()
    {
        String Emergancy = prefs.getString("SocietyContact","");
        return Emergancy;
    }
    public void setEmegency(String Emergancy)
    {
        prefs.edit().putString("SocietyContact", Emergancy).commit();
    }
    /* -------------------------  War Version set --------------------- */
    public String getWarversion()
    {
        String warVersion = prefs.getString("WarVersion","");
        return warVersion;
    }
    public void setWarversion(String warVersion)
    {
        prefs.edit().putString("WarVersion", warVersion).commit();
    }
/* ------------------------- Set Round Details -------------------*/
    public String getScheduleID()
    {
    String ScheduleID = prefs.getString("ScheduleId","");
    return ScheduleID;
    }
    public void setScheduleID(String ScheduleID)
    {
        prefs.edit().putString("ScheduleId", ScheduleID).commit();
    }
    public String getRoundID()
    {
        String RoundID = prefs.getString("RoundID","");
        return RoundID;
    }
    public void setgetRoundID(String RoundID)
    {
        prefs.edit().putString("RoundID", RoundID).commit();
    }
    public String getNoOfCheckpost()
    {
        String NoOfCheckpost = prefs.getString("NoOfCheckpost","");
        return NoOfCheckpost;
    }
    public void setNoOfCheckpost(String NoOfCheckpost)
    {
        prefs.edit().putString("NoOfCheckpost", NoOfCheckpost).commit();
    }
    /* -------------------------  APK Version set --------------------- */
    public String getAPKVersion_old()
    {
        String APKVersion_old = prefs.getString("APKVersion_old","1.0.20210228");
        return APKVersion_old;

    }
    public String getAPKVersion_New()
    {
        String APKVersion_New = prefs.getString("APKVersion_New","1.0.20210228");
        return APKVersion_New;

    }
    public String gettoken() {
        String token = prefs.getString("token","");
        return token;
    }
    public void settoken(String token) {
        prefs.edit().putString("token", token).commit();
    }


    public String getusername() {
        String username = prefs.getString("username","");
        return username;
    }
    public void setusername(String username) {
        prefs.edit().putString("username", username).commit();
    }


    public String getpassword() {
        String password = prefs.getString("password","");
        return password;
    }
    public void setpassword(String password) {
        prefs.edit().putString("password", password).commit();
    }

    public String getLoginDate() {
        String date = prefs.getString("date","");
        return date;
    }
    public void setLoginDate(String date) {
        prefs.edit().putString("date", date).commit();
    }


}
