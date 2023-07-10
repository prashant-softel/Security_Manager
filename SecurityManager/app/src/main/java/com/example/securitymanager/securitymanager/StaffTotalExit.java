package com.example.securitymanager.securitymanager;

import org.json.JSONArray;

public class StaffTotalExit {
    private String Security_status;
    private String Full_name;
    private String  IsBlock;
    private String Photo_thumb;
    private String Cur_con_1;
    private String Service_prd_reg_id;
    private String SocService_prd_reg_id;

    private String Since;
    private String Rating;
    private String Cat;
    private String Unit;

    public StaffTotalExit(String security_status, String full_name, String isBlock, String photo_thumb, String cur_con_1, String service_prd_reg_id,String socservice_prd_reg_id, String since, String rating,String unit,String cat)
    {

        Security_status = security_status;
        Full_name = full_name;
        IsBlock = isBlock;
        Photo_thumb = photo_thumb;
        Cur_con_1 = cur_con_1;
        Service_prd_reg_id = service_prd_reg_id;
        SocService_prd_reg_id = socservice_prd_reg_id;
        Since = since;
        Rating = rating;
        Unit=unit;
        Cat=cat;
    }

    public String getSocService_prd_reg_id() {
        return SocService_prd_reg_id;
    }

    public void setSocService_prd_reg_id(String socService_prd_reg_id) {
        SocService_prd_reg_id = socService_prd_reg_id;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    public String getSecurity_status() {
        return Security_status;
    }

    public void setSecurity_status(String security_status) {
        Security_status = security_status;
    }

    public String getFull_name() {
        return Full_name;
    }

    public void setFull_name(String full_name) {
        Full_name = full_name;
    }

    public String getIsBlock() {
        return IsBlock;
    }

    public void setIsBlock(String isBlock) {
        IsBlock = isBlock;
    }

    public String getPhoto_thumb() {
        return Photo_thumb;
    }

    public void setPhoto_thumb(String photo_thumb) {
        Photo_thumb = photo_thumb;
    }

    public String getCur_con_1() {
        return Cur_con_1;
    }

    public void setCur_con_1(String cur_con_1) {
        Cur_con_1 = cur_con_1;
    }

    public String getService_prd_reg_id() {
        return Service_prd_reg_id;
    }

    public void setService_prd_reg_id(String service_prd_reg_id) {
        Service_prd_reg_id = service_prd_reg_id;
    }

    public String getSince() {
        return Since;
    }

    public void setSince(String since) {
        Since = since;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}
