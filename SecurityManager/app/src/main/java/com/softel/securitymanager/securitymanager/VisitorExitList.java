    package com.softel.securitymanager.securitymanager;

    public class VisitorExitList
    {
        private String Vid;
        private String Name;
        private String UnitNo;
        private String Date;
        private String InTime;
        private String EntryGate;
        private String TimeStatus;
        private String Vehicle;
        private String Img;
        private String Contact;
        private String Entrywith;
        private String Doc_Img;



        public VisitorExitList(String vid, String name, String unitNo, String date, String inTime, String entryGate, String timeStatus,String vehicle,String img,String contact,String entrywith,String doc_Img) {
            Vid = vid;
            Name = name;
            UnitNo = unitNo;
            Date = date;
            InTime = inTime;
            EntryGate = entryGate;
            TimeStatus = timeStatus;
            Vehicle = vehicle;
            Img = img;
            Contact = contact;
            Entrywith = entrywith;
            Doc_Img = doc_Img;
            System.out.println(Vid+ " "+Name+ " "+ UnitNo+ " "+Date+ " "+InTime+" "+EntryGate+" "+TimeStatus  + " " + Vehicle);
        }

        public String getDoc_Img() {
            return Doc_Img;
        }

        public void setDoc_Img(String doc_Img) {
            Doc_Img = doc_Img;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String img) {
            Img = img;
        }

        public String getContact() {
            return Contact;
        }

        public void setContact(String contact) {
            Contact = contact;
        }

        public String getEntrywith() {
            return Entrywith;
        }

        public void setEntrywith(String entrywith) {
            Entrywith = entrywith;
        }

        public String getVehicle() {
            return Vehicle;
        }

        public void setVehicle(String vehicle) {
            Vehicle = vehicle;
        }

        public String getVid() {
            return Vid;
        }

        public void setVid(String vid) {
            Vid = vid;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getUnitNo() {
            return UnitNo;
        }

        public void setUnitNo(String unitNo) {
            UnitNo = unitNo;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getInTime() {
            return InTime;
        }

        public void setInTime(String inTime) {
            InTime = inTime;
        }

        public String getEntryGate() {
            return EntryGate;
        }

        public void setEntryGate(String entryGate) {
            EntryGate = entryGate;
        }

        public String getTimeStatus() {
            return TimeStatus;
        }

        public void setTimeStatus(String timeStatus) {
            TimeStatus = timeStatus;
        }
    }
