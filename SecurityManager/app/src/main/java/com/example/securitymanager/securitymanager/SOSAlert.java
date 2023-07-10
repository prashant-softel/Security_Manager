package com.example.securitymanager.securitymanager;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SOSAlert extends AppCompatActivity {

private Button Mbutton,Fbutton,Lbutton,Obutton;
private TextView mOwnerName,fOwnerName,lOwnerName,oOwnerName,mUnitNo,fUnitNo,lUnitNo,oUnitNo,mContactNo,fContactNo,lContactNo,oContactNo,mWing,fWing,lWing,oWing,mFloor,fFloor,lFloor,oFloor;
private LinearLayout mEmergency, lEmergency,fEmergency,oEmergency;
String SOSAlertID;
Session session;
JSONObject json;
TextView txtresolvemsg,txtresolvemsg1,txtresolvemsg2,txtresolvemsg3;
String AlertType="",SubmitType="",issueresolved="0",SubmitInSM ="0",issueresolvedtest="0";
MediaPlayer catSoundMediaPlayer=null;
Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosalert);
        setTitle(R.string.sos);
        catSoundMediaPlayer = MediaPlayer.create(this, R.raw.alert_sound);
        catSoundMediaPlayer.setLooping(true);

        catSoundMediaPlayer.start();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(getApplicationContext());
        mOwnerName = (TextView) findViewById(R.id.txtmName);
        fOwnerName = (TextView) findViewById(R.id.txtFireName);
        lOwnerName = (TextView) findViewById(R.id.txtLfName);
        oOwnerName = (TextView) findViewById(R.id.txtOName);
        mUnitNo = (TextView) findViewById(R.id.mFlatNo);
        fUnitNo = (TextView) findViewById(R.id.fFlateNo);
        lUnitNo = (TextView) findViewById(R.id.LFlatNo);
        oUnitNo = (TextView) findViewById(R.id.oFlatNo);
        mContactNo = (TextView) findViewById(R.id.mContact);
        fContactNo = (TextView) findViewById(R.id.fContact);
        lContactNo = (TextView) findViewById(R.id.lContact);
        oContactNo = (TextView) findViewById(R.id.oContact);
        mWing = (TextView) findViewById(R.id.mWingNo);
        fWing = (TextView) findViewById(R.id.fWing);
        lWing = (TextView) findViewById(R.id.lWingNo);
        oWing = (TextView) findViewById(R.id.oWingNo);
        mFloor = (TextView) findViewById(R.id.mFloorNo);
        fFloor = (TextView) findViewById(R.id.fFloorNo);
        lFloor = (TextView) findViewById(R.id.LFlatNo);
        oFloor = (TextView) findViewById(R.id.oFlatNo);
        txtresolvemsg = (TextView) findViewById(R.id.txtresolvemsg);
        txtresolvemsg1 = (TextView) findViewById(R.id.txtresolvemsg1);
        txtresolvemsg2 = (TextView) findViewById(R.id.txtresolvemsg2);
        txtresolvemsg3 = (TextView) findViewById(R.id.txtresolvemsg3);
        mEmergency = (LinearLayout) findViewById(R.id.medicalEmer);
        fEmergency = (LinearLayout) findViewById(R.id.fireEmer);
        lEmergency = (LinearLayout) findViewById(R.id.liftEmer);
        oEmergency = (LinearLayout) findViewById(R.id.otherEmer);
        mContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mContactNo.getText().toString()));
                startActivity(callIntent);
            }
        });
        fContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + fContactNo.getText().toString()));
                startActivity(callIntent);
            }
        });
        lContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + lContactNo.getText().toString()));
                startActivity(callIntent);
            }
        });
        oContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + oContactNo.getText().toString()));
                startActivity(callIntent);
            }
        });

        Bundle sosdata = getIntent().getExtras();
        SOSAlertID = sosdata.getString("SOSID");
        SubmitType = sosdata.getString("SubmitType");
        new SOSdata().execute(SOSAlertID);
        System.out.println("SubmitType : " + SubmitType);
        if (SubmitType.equals("1")) {
            Mbutton = (Button) findViewById(R.id.medicalButton);
            Mbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitInSM="1";
                    if (Mbutton.getText().toString().equals(getString(R.string.please_acknowledge))) {

                        Mbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Mbutton.setText("Close");

                        //Mbutton.setEnabled(false);
                        new ResolvedIssues().execute(SOSAlertID);
                    } else if (Mbutton.getText().toString().equals(getString(R.string.close))) {
                        Intent homePage = new Intent(SOSAlert.this, homepage.class);
                        startActivity(homePage);
                    }

                }
            });
        } else if (SubmitType.equals("2")) {
            Fbutton = (Button) findViewById(R.id.fireButton);
            Fbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitInSM="1";
                    if (Fbutton.getText().toString().equals(getString(R.string.please_acknowledge))) {

                        Fbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Fbutton.setText("Close");
                        // Fbutton.setEnabled(false);
                        new ResolvedIssues().execute(SOSAlertID);
                    } else if (Fbutton.getText().toString().equals(getString(R.string.close))) {
                        Intent homePage = new Intent(SOSAlert.this, homepage.class);
                        startActivity(homePage);
                    }
                }
            });
        } else if (SubmitType.equals("3")) {
            Lbutton = (Button) findViewById(R.id.liftButton);
            Lbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitInSM="1";
                    if (Lbutton.getText().toString().equals(getString(R.string.please_acknowledge))) {

                        Lbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Lbutton.setText("Close");


                        // Lbutton.setEnabled(false);
                        new ResolvedIssues().execute(SOSAlertID);
                    } else if (Lbutton.getText().toString().equals(getString(R.string.close))) {
                        Intent homePage = new Intent(SOSAlert.this, homepage.class);
                        startActivity(homePage);
                    }


                }
            });
        } else if (SubmitType.equals("4")) {
            Obutton = (Button) findViewById(R.id.OtherButton);
            Obutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubmitInSM="1";
                    if (Obutton.getText().toString().equals(getString(R.string.please_acknowledge))) {

                        Obutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Obutton.setText("Close");

                        // Obutton.setEnabled(false);
                        new ResolvedIssues().execute(SOSAlertID);
                    } else if (Obutton.getText().toString().equals(getString(R.string.close))) {
                        Intent homePage = new Intent(SOSAlert.this, homepage.class);
                        startActivity(homePage);
                    }


                }
            });
        }
     t = new Thread() {
            int count = 0;

            @Override
            public void run() {

                while (!t.isInterrupted()) {

                    try {
                        System.out.println("Interrupted " + t.isInterrupted());
                        t.sleep(5000);  //100000ms = 10 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                SubmitInSM="0";
                                count++;
                                if (issueresolvedtest.equals("1")) {
                                    System.out.println("Test");
                                  // Thread.currentThread().interrupt();
                                    t.interrupt();
                                    System.out.println("Is Interrupted : " + t.isInterrupted());

                                }
                                else {
                                    new CheckedResolvedIssues().execute(SOSAlertID);
                                }


                            }

                        });
                        if (issueresolvedtest.equals("1")) {
                            System.out.println("Test");
                            // Thread.currentThread().interrupt();
                            t.interrupt();
                            System.out.println("Is Interrupted : " + t.isInterrupted());

                        }



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

            t.start();
        
    }

    private class SOSdata extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONObject SOSData = jsondata.getJSONObject("SOSAlert");
                    JSONObject SOSData1 = SOSData.getJSONObject("0");
                    AlertType=SOSData1.getString("AlertType");
                    System.out.println("AlertType - " +AlertType);
                    if(AlertType.equals("1"))
                    {
                        SubmitType = "1";
                        mEmergency.setVisibility(View.VISIBLE);
                        fEmergency.setVisibility(View.GONE);
                        lEmergency.setVisibility(View.GONE);
                        oEmergency.setVisibility(View.GONE);
                        mOwnerName.setText(SOSData1.getString("RaisedBy"));
                        mUnitNo.append(SOSData1.getString("UnitNo"));
                        mContactNo.append(SOSData1.getString("ContactNo"));
                        //mWing.setText(SOSData1.getString("R_Wing"));
                        mWing.append(SOSData1.getString("R_Wing"));
                        mFloor.append(SOSData1.getString("R_FloorNo"));
                    }
                    else if(AlertType.equals("2"))
                    {
                        SubmitType = "2";
                        fEmergency.setVisibility(View.VISIBLE);
                        mEmergency.setVisibility(View.GONE);
                        lEmergency.setVisibility(View.GONE);
                        oEmergency.setVisibility(View.GONE);
                        fOwnerName.setText(SOSData1.getString("RaisedBy"));
                        fUnitNo.append(SOSData1.getString("UnitNo"));
                        fContactNo.append(SOSData1.getString("ContactNo"));
                        fWing.append(SOSData1.getString("R_Wing"));
                        fFloor.append(SOSData1.getString("R_FloorNo"));
                    }
                    else if(AlertType.equals("3"))
                    {
                        SubmitType = "3";
                        lEmergency.setVisibility(View.VISIBLE);
                        fEmergency.setVisibility(View.GONE);
                        mEmergency.setVisibility(View.GONE);
                        oEmergency.setVisibility(View.GONE);
                        lOwnerName.setText(SOSData1.getString("RaisedBy"));
                        lUnitNo.append(SOSData1.getString("UnitNo"));
                        lContactNo.append(SOSData1.getString("ContactNo"));
                        lWing.append(SOSData1.getString("R_Wing"));
                        lFloor.append(SOSData1.getString("R_FloorNo"));
                    }
                    else if(AlertType.equals("4"))
                    {
                        SubmitType = "4";
                       oEmergency.setVisibility(View.VISIBLE);
                       lEmergency.setVisibility(View.GONE);
                       fEmergency.setVisibility(View.GONE);
                       mEmergency.setVisibility(View.GONE);
                       oOwnerName.setText(SOSData1.getString("RaisedBy"));
                       oUnitNo.append(SOSData1.getString("UnitNo"));
                       oContactNo.append(SOSData1.getString("ContactNo"));
                       oWing.append(SOSData1.getString("R_Wing"));
                       oFloor.append(SOSData1.getString("R_FloorNo"));
                    }

                }


            }
            catch(Exception e)
            {}
        }

        public String postData(String SOSID) {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/ShowAlert";
                String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("AlertID",SOSID));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String SOSID=(String) params[0];
            String s=postData(SOSID);
            return s;
        }

    }

    private class ResolvedIssues extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    issueresolved="1";
                    //System.out.println("Test");
                    JSONObject jsondata = obj.getJSONObject("response");
                   catSoundMediaPlayer.stop();
                 /*  if(SubmitType.equals("1"))
                   {
                       Mbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                       Mbutton.setText("Close");
                   }
                    else if(SubmitType.equals("2"))
                    {
                        Fbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Fbutton.setText("Close");
                    }
                    else if(SubmitType.equals("3"))
                    {
                        Lbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Lbutton.setText("Close");
                    }
                    else if(SubmitType.equals("4"))
                    {
                        Obutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Obutton.setText("Close");
                    }*/



                }
                else
                {
                    issueresolved="0";
                }


            }
            catch(Exception e)
            {}
        }

        public String postData(String SOSID) {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/ResolvedAlert";
                String token = session.gettoken();
                String LoginName = session.getName();
                String Role = session.getRole();
                String GateNo = session.getGateNo();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("AlertID", SOSID));
               // if (SubType.equals("1")) {
                    params.add(new BasicNameValuePair("LoginName", LoginName));
                    params.add(new BasicNameValuePair("Role", Role));
                    params.add(new BasicNameValuePair("GateNo", GateNo));
                    //params.add(new BasicNameValuePair("SubType", SubType));
               // }

                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String SOSID=(String) params[0];
           // String SubType=(String) params[1];
            String s=postData(SOSID);
            return s;
        }

    }



    private class CheckedResolvedIssues extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result in CheckedResolvedIssues : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                     issueresolvedtest="1";
                    catSoundMediaPlayer.stop();
                    JSONObject response = obj.getJSONObject("response");
                    JSONObject sosalert = response.getJSONObject("SOSAlert");
                    JSONObject json_sos = sosalert.getJSONObject("0");
                    if(json_sos.getString("AlertType").equals("1"))
                    {
                        txtresolvemsg.setVisibility(View.VISIBLE);
                        txtresolvemsg.setText(getString(R.string.issue_resolved_by_owner));
                        Mbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Mbutton.setText(getString(R.string.close));
                    }
                    if(json_sos.getString("AlertType").equals("2"))
                    {
                        txtresolvemsg1.setVisibility(View.VISIBLE);
                        txtresolvemsg1.setText(getString(R.string.issue_resolved_by_owner));
                        Fbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Fbutton.setText(getString(R.string.close));
                    }
                    if(json_sos.getString("AlertType").equals("3"))
                    {
                        txtresolvemsg2.setVisibility(View.VISIBLE);
                        txtresolvemsg2.setText(getString(R.string.issue_resolved_by_owner));
                        Lbutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Lbutton.setText(getString(R.string.close));
                    }
                    if(json_sos.getString("AlertType").equals("4"))
                    {
                        txtresolvemsg3.setVisibility(View.VISIBLE);
                        txtresolvemsg3.setText(getString(R.string.issue_resolved_by_owner));
                        Obutton.setBackgroundColor(Color.argb(255, 72, 138, 255));
                        Obutton.setText(getString(R.string.close));
                    }



                }
                else
                {
                    issueresolvedtest="0";
                }


            }
            catch(Exception e)
            {}
        }

        public String postData(String SOSID) {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/CheckResolvedStatus";
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("AlertID", SOSID));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String SOSID=(String) params[0];
            // String SubType=(String) params[1];
            String s=postData(SOSID);
            return s;
        }

    }
}
