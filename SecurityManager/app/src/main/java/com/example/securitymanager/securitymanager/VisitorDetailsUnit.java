package com.example.securitymanager.securitymanager;

public class VisitorDetailsUnit {
    private String UnitNo;
    private String OwnerName;
    private String ApprovalStatus;
    private String LoginId;
    private String LoginName;
    private String Approvalwith;
    private String Approvalmsg;
    private String Note;
    private String Visitor_item_image;


    public VisitorDetailsUnit(String unitNo, String ownerName, String approvalStatus,String loginId,String loginName,String approvalwith,String approvalmsg,String note,String visitor_item_image) {
        System.out.println("In class : " +unitNo + " "+ownerName + " "+approvalStatus);
        UnitNo = unitNo;
        OwnerName = ownerName;
        ApprovalStatus = approvalStatus;
        LoginId=loginId;
        LoginName=loginName;
        Approvalwith=approvalwith;
        Approvalmsg=approvalmsg;
        Note = note;
        Visitor_item_image = visitor_item_image;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getVisitor_item_image() {
        return Visitor_item_image;
    }

    public void setVisitor_item_image(String visitor_item_image) {
        Visitor_item_image = visitor_item_image;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getApprovalwith() {
        return Approvalwith;
    }

    public void setApprovalwith(String approvalwith) {
        Approvalwith = approvalwith;
    }

    public String getApprovalmsg() {
        return Approvalmsg;
    }

    public void setApprovalmsg(String approvalmsg) {
        Approvalmsg = approvalmsg;
    }
}
