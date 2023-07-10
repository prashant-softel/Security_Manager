package com.example.securitymanager.securitymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.securitymanager.securitymanager.model.clsUser;
import com.example.securitymanager.securitymanager.model.clsUser;
import com.mantra.mfs100.MFS100;

public class UserDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MFS100SQLiteDatabaseSample.db";
    private static final int DATABASE_VERSION = 1;

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConst.CREATE_TABLE_USERS);
        db.execSQL(DBConst.CREATE_INDEX_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int insertUser(clsUser user) {
        try {
            int retCode = 0;
            SQLiteDatabase dbWritable = this.getWritableDatabase();
            SQLiteDatabase dbReadable = this.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBConst.UNIQUE_ID, user.uniqueId);
            values.put(DBConst.NAME, user.name);
            values.put(DBConst.EMAIL, user.email);
            values.put(DBConst.CONTACT_NO, user.contactNo);
            values.put(DBConst.FINGER_ISO, user.fingerIso);

            Cursor cursor = dbReadable.query(DBConst.TABLE_NAME_ENROLL, null,
                    DBConst.UNIQUE_ID + "=?", new String[]{user.uniqueId}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                retCode = 1;
            } else {
                dbWritable.insert(DBConst.TABLE_NAME_ENROLL, null, values);
                retCode = 0;
            }
            cursor.close();
            dbReadable.close();
            dbWritable.close();
            return retCode;
        } catch (Exception e){
            Log.e("Error", e.toString());
            return -1;
        }
    }

    public clsUser verifyFinger(byte[] captureFingerIso, MFS100 mfs100) {
        try {
            SQLiteDatabase dbReadable = this.getReadableDatabase();

            Cursor cursor = dbReadable.query(DBConst.TABLE_NAME_ENROLL, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    clsUser user = new clsUser();
                    user.uniqueId = cursor.getString(cursor.getColumnIndexOrThrow(DBConst.UNIQUE_ID));
                    user.name = cursor.getString(cursor.getColumnIndexOrThrow(DBConst.NAME));
                    user.email = cursor.getString(cursor.getColumnIndexOrThrow(DBConst.EMAIL));
                    user.contactNo = cursor.getString(cursor.getColumnIndexOrThrow(DBConst.CONTACT_NO));
                    user.fingerIso = cursor.getBlob(cursor.getColumnIndexOrThrow(DBConst.FINGER_ISO));

                    if (user.fingerIso != null && captureFingerIso != null && mfs100 != null) {
                        int score = mfs100.MatchISO(captureFingerIso, user.fingerIso);
                        if (score >= 1400) {
                            cursor.close();
                            dbReadable.close();
                            return user;
                        }
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
            dbReadable.close();
            return null;
        } catch (Exception e) {
            Log.e("Error", e.toString());
            return null;
        }
    }
}
