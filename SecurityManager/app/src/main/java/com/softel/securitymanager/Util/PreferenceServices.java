package com.softel.securitymanager.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.softel.securitymanager.securitymanager.UnitCusttom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreferenceServices {

    private static final String PREFS_NAME = "SecurityManger";
    private static final String xValue = "xValue";
    private static final String WingsId = "WingsId";
    private static final String Flats = "Flats";


    @SuppressLint("StaticFieldLeak")
    private static PreferenceServices mSingleton = new PreferenceServices();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private PreferenceServices() {
    }

    public static PreferenceServices instance() {
        return mSingleton;
    }

    public static PreferenceServices getInstance() {
        return mSingleton;
    }

    public static void init(Context context) {
        mContext = context;
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public String getFlats() {
        return getPrefs().getString(Flats, "");
    }

    public void setFlats(String name) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(Flats, name);
        editor.apply();
    }

    public void saveWing(List<String> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public List<String> getWing(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveWingId(int [] strings, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public int[] getWingId(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<int[]>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveWingName(String [] strings, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public String[] getWingName(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUnitList_1(ArrayList<UnitCusttom> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public ArrayList<UnitCusttom> getUnitList_1(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<ArrayList<UnitCusttom>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUnitList_2(ArrayList<UnitCusttom> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public ArrayList<UnitCusttom> getUnitList_2(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<ArrayList<UnitCusttom>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUnitList_3(ArrayList<UnitCusttom> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public ArrayList<UnitCusttom> getUnitList_3(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<ArrayList<UnitCusttom>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUnitList_4(ArrayList<UnitCusttom> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public ArrayList<UnitCusttom> getUnitList_4(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<ArrayList<UnitCusttom>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveFlats(ArrayList<HashMap<String, String>> list, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public ArrayList<HashMap<String, String>> getFlats(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public String getxValue() {
        return getPrefs().getString(xValue, "");
    }

    public void setxValue(String todayTrendingMovies) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(xValue, todayTrendingMovies);
        editor.apply();
    }

    public void saveStringArrayUnit1(String [] strings, String key){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        editor.putString(key, json);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public String[] getStringArrayUnit1(String key){
        //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = getPrefs().getString(key, null);
        Type type = new TypeToken<String[]>() {}.getType();
        return gson.fromJson(json, type);
    }
}
