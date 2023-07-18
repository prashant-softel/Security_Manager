package com.softel.securitymanager.securitymanager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.softel.securitymanager.Util.PreferenceServices;
import com.softel.securitymanager.securitymanager.model.clsUser;
import com.softel.securitymanager.securitymanager.database.UserDatabase;

public class Setting extends AppCompatActivity {

    private UserDatabase database;

    private Spinner spinner;
    private RadioButton lang_hind, lang_eng, yes, no;
    Integer health;
    private static String url_login;
    Session session;
    private Button sButtom;
    private int l = 1;
    TextView tvStatus;
    Button btn_Sync;
    String setGate="" ,Gate;
    String language;
    LinearLayout kiosklayout;
    Button kioskmode;

    //multilanguage addition

    private void setLocale(String lang) {

        Locale locale =new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        language = prefs.getString("My_Lang", "");
        if (language.equals("hi")) lang_hind.setChecked(true);
        if (language.equals("en")) lang_eng.setChecked(true);
        setLocale(language);
        System.out.println("Present language: "+language);
        Log.d("language", language);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        lang_hind = (RadioButton) findViewById(R.id.lang_hind);
        lang_eng = (RadioButton) findViewById(R.id.lang_eng);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton) findViewById(R.id.no);
        health = ((a_variable) this.getApplication()).getHealthcheck();
        if(health == 0) no.setChecked(true);
        else yes.setChecked(true);
        loadLocale();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                health = 1;
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                health = 0;
            }
        });
        lang_hind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(l!=0){
                 //   l=0;
                language="hi";
                    //setLocale("hi");
                    //recreate();
               //}
            }
        });
        lang_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(l!=1){
                  //  l=1;
                language="en";
                    //setLocale("en");
                    //recreate();
                //}
            }
        });
        session=new Session(getApplicationContext());
       database = new UserDatabase(getApplicationContext());
       kiosklayout = (LinearLayout) findViewById(R.id.kioskLayout);
       kioskmode = (Button) findViewById(R.id.kioskmode);
       kioskmode.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
           @Override
           public void onClick(View v) {
               stopLock();
           }
       });
       if(session.getkioskv().equals("1"))
       {
           kiosklayout.setVisibility(View.VISIBLE);
       }
       // String sessiongate = session.getGateNo();
        sButtom =(Button) findViewById(R.id.button);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btn_Sync = (Button) findViewById(R.id.btn_Sync);
        sButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Gate.equals("Select Gate"))
                {
                    setSpinnerError(spinner,"Select Gate");
                }else if(language.equalsIgnoreCase("")){
                    Toast.makeText(Setting.this, "Select language", Toast.LENGTH_SHORT).show();
                }
                else {
                    setLocale(language);
                  //  recreate();
                    a_variable objMyApp = (a_variable)v.getContext().getApplicationContext();
                    objMyApp.setHealthcheck(health);
                    new setGateNo().execute(setGate);
                }
            }
        });
        setTitle("Setting");
        /* --    back button code ---- */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* --    back button code ---- */

        spinner = findViewById(R.id.spinner);
        List<String> gateNumbers = new ArrayList<>();
        gateNumbers.add(0,"Select Gate");
        gateNumbers.add(1,"Gate No - 1");
        gateNumbers.add(2,"Gate No - 2");
        gateNumbers.add(3,"Gate No - 3");
        gateNumbers.add(4,"Gate No - 4");
        gateNumbers.add(5,"Gate No - 5");
        gateNumbers.add(6,"Gate No - 6");

        // style add poputale the spiner.
        ArrayAdapter<String> dataAdapter;

        dataAdapter =new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, gateNumbers);

        //dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        btn_Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceServices.getInstance().saveWing(null,"Wing");
                PreferenceServices.getInstance().setFlats("");
                new SyncFingerData().execute();
                finish();
            }
        });

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(Integer.valueOf(session.getGateNo()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).toString().equals("Select Gate"))
                {
                    Gate=parent.getItemAtPosition(position).toString();
                }
                else
                {
                    Gate=parent.getItemAtPosition(position).toString();
                    // on selecting a spinner item
                   // if(parent.getItemAtPosition(position).toString().equals(session.getGateNo()))
                  //  {
                      //  String item = parent.getItemAtPosition(position).toString();
                   // }
                   // else {
                        String item = parent.getItemAtPosition(position).toString();
                   // }
                    // show selected to spinner
                    //Toast.makeText(parent.getContext(), "Selected" + position,Toast.LENGTH_SHORT).show();
                    setGate=String.valueOf(position);
                   // new setGateNo().execute(String.valueOf(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class SyncFingerData extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            System.out.println("Sync Finger result "+result);
            Log.d("on post ", "on post execute");
            try {

                JSONObject json=new JSONObject(result);
                System.out.println("Result On Sysn "+result);
                JSONObject FingerObject = json.getJSONObject("response");
                // mCheck.setText(FingerObject.toString());
                JSONArray FingerArray = FingerObject.getJSONArray("staffdata");

                System.out.println("Length"+FingerArray.length());
                clsUser user = new clsUser();
                int retCode = 0;
                for(int i = 0 ; i < FingerArray.length(); i++)
                {
                    JSONObject obj=FingerArray.getJSONObject(i);
                    if(obj.has("fingerISO"))
                    {
                        String Staff_id = obj.getString("service_prd_reg_id");
                        String FingerServer=obj.getString("fingerISO");
                        byte[] fingerIso = Base64.decode(FingerServer,Base64.DEFAULT);

                        System.out.println("obj in Staff_id "+Staff_id);
                        // System.out.println("obj in fingerIso "+fingerIso);
                        user.uniqueId = Staff_id;
                        user.fingerIso = fingerIso;
                        retCode = database.insertUser(user);
                    }
                }

                if (retCode == 0 || retCode == 1) {
                    Toast.makeText(getApplicationContext(), "SYNC Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SYNC Failed !!", Toast.LENGTH_LONG).show();
                }

            }
            catch (Exception e){}

        }

        public String postData() {

            String origresponseText="";
            try {
                String society_id = session.getSocietyid();
                String token = session.gettoken();
                System.out.println("token "+token);

                System.out.println("token"+token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("SocietyID",society_id));
                params.add(new BasicNameValuePair("token",token));

                String url_login = getString(R.string.url) + "Staff/fetchStaffSyncronise";
                JSONParser jParser=new JSONParser();
                JSONObject  json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String s=postData();
            return s;
        }

    }


    private class setGateNo extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();
                Intent welcome = new Intent(Setting.this, homepage.class);
                startActivity(welcome);
            }
            catch(Exception e)
            {}
        }

        public String postData(String gateno) {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "OTP/SetGateNo";
               String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("gateNo",gateno));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String gateno=(String) params[0];
            String s=postData(gateno);
            return s;
        }

    }

    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopLock(){

        //Note. Double call for stopLockTask() sometimes causes crash, so you should check if you app is really in lock mode
        if(isAppInLockTaskMode()) {
            stopLockTask();
            showThisViewAndHideOthers (null);
        } else {
            Toast.makeText (this, R.string.not_in_kiosk_mode, Toast.LENGTH_SHORT).show ();
        }
    }
    private boolean isAppInLockTaskMode() {
        ActivityManager activityManager;

        activityManager = (ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For SDK version 23 and above.
            return activityManager.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // When SDK version >= 21. This API is deprecated in 23.
            return activityManager.isInLockTaskMode();
        }

        return false;
    }


    private void showThisViewAndHideOthers (View v){
    }

}
