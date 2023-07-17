package com.softel.securitymanager.securitymanager.database;

/**
 * Created by SW11 on 2/29/2016.
 */
public class DBConst {

    public static final String TABLE_NAME_ENROLL = "UsersEnrollment";

    public static final String ID = "_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String CONTACT_NO = "contact_no";
    public static final String FINGER_ISO = "finger_iso";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_NAME_ENROLL + " ("
            + ID + " INTEGER PRIMARY KEY,"
            + UNIQUE_ID + " TEXT,"
            + NAME + " TEXT,"
            + EMAIL + " TEXT,"
            + CONTACT_NO + " TEXT,"
            + FINGER_ISO + " BLOB);";

    public static final String CREATE_INDEX_USERS = "CREATE INDEX cardid_indx ON " + TABLE_NAME_ENROLL + " ("
            + UNIQUE_ID + ");";
}
