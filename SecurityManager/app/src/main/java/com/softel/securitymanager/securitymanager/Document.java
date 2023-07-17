package com.softel.securitymanager.securitymanager;

public class Document {
    private String DocID ;
    private String DocName;

    public Document(String docID, String docName) {
        this.DocID = docID;
        this.DocName = docName;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        this.DocID = docID;
    }

    public String getDocName() {
        return DocName;
    }

    public void setDocName(String docName) {
        this.DocName = docName;
    }
}

