package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Staff_CheckIn extends AppCompatActivity {

    EditText staff_id, mobile_no, snote;
    Session session;
 //   ActionBar mActionBar;
    Button submit,checkInBtn,backButton;
    TextView gateno, sname, sdob, scontactno, sworksince, sjob;
    TextView stdate,sttime;
    CardView cstaffdetails;
    LinearLayout checkLayout;
    EditText temp, oxygen, pulse;
    Integer health;
    String tem="0", puls="0", oxy="0";
    LinearLayout nostaff,aleradyCheckIn,lnoteandimage,lcheckinBtn,lselectimage,lViewlatestHistory;
    ImageView simage;
    ProgressDialog dialog;
    String staffID,mobileno,system_staff_id,counter,URL;

    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff__check_in);
        health = ((a_variable) this.getApplication()).getHealthcheck();

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        gateno = (TextView) findViewById(R.id.gateno);
        checkInBtn = (Button) findViewById(R.id.CheckInBtn);

        //checkLayout
        checkLayout=(LinearLayout) findViewById(R.id.checkLayout);
        temp=(EditText) findViewById(R.id.temp);
        oxygen=(EditText) findViewById(R.id.oxygen);
        pulse=(EditText) findViewById(R.id.pulse);
        if(health==0) checkLayout.setVisibility(GONE);
        aleradyCheckIn = (LinearLayout) findViewById(R.id.lCheckIn);
        lnoteandimage = (LinearLayout) findViewById(R.id.lNotenandImage);
        lcheckinBtn = (LinearLayout) findViewById(R.id.lcheckInBtn);
        lselectimage = (LinearLayout) findViewById(R.id.lselectimage);
        lViewlatestHistory = (LinearLayout) findViewById(R.id.lViewlatestHistory);
        stdate = (TextView) findViewById(R.id.staffIn);
        sttime = (TextView) findViewById(R.id.staffInTime);
        nostaff = (LinearLayout) findViewById(R.id.lnoStaff) ;
        simage = (ImageView) findViewById(R.id.simage);
        snote = (EditText) findViewById(R.id.staff_note);
        sname = (TextView) findViewById(R.id.sname);
        sdob = (TextView) findViewById(R.id.sDob);
        temp.requestFocus();
        simage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL, null);
            }
        });

        scontactno = (TextView) findViewById(R.id.scontactno);
        sworksince = (TextView) findViewById(R.id.sworksince);
        sjob = (TextView) findViewById(R.id.sjob);
        cstaffdetails = (CardView) findViewById(R.id.cstaffdetails);
        spinner=(ProgressBar) findViewById(R.id.progressBar1);
        session = new Session(getApplicationContext());
        setTitle("Staff Check In");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle staffdata;
        String output="";
        staffdata = getIntent().getExtras();
        staffID = staffdata.getString("StaffID");
        mobileno=staffdata.getString("MobileNo");
        counter=staffdata.getString("counter");

        new Staffdetails().execute();

        checkInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(temp.getText().toString().length() == 0 && health ==1)
                {
                    temp.requestFocus();
                    temp.setError(getString(R.string.field_cannot_be_empty));
                }
                else if(oxygen.getText().toString().length() == 0 && health ==1)
                {
                    oxygen.requestFocus();
                    oxygen.setError(getString(R.string.field_cannot_be_empty));
                }
                else if(pulse.getText().toString().length() == 0 && health ==1)
                {
                    pulse.requestFocus();
                    pulse.setError(getString(R.string.field_cannot_be_empty));
                }
                else
                    {
                        if(health ==1)
                        {
                            tem =temp.getText().toString() ;
                            puls = pulse.getText().toString();
                            oxy = oxygen.getText().toString();
                        }

                        new StaffEntry().execute();
                    }


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private class StaffEntry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1"))
                {

                    System.out.println("Staff Present");
                    JSONObject jsondata=obj.getJSONObject("response");
                    System.out.println("Obj");
                    String sid = jsondata.getString("StaffID");
                    String sentry_id = jsondata.getString("EntryID");
                    System.out.println("Staff ID "+sid);
                    System.out.println("Entry ID "+sentry_id);
                    dialog.dismiss();
                    Intent welcome_staff = new Intent(getApplicationContext(),Welcome_staff.class);
                    System.out.print("Test1");
                    welcome_staff.putExtra("StaffID",sid);
                    welcome_staff.putExtra("EntryID",sentry_id);
                    startActivity(welcome_staff);
                }
                else
                {

                }
            }
            catch(Exception e)
            {}
        }

        public String postData() {

            System.out.println("In site Post Data");
            String origresponseText="";
            try {

                String sid = system_staff_id;
                System.out.println("sid"+sid);
                String s_profile = sjob.getText().toString();
                System.out.println("staff Image"+s_profile);
                String staff_note = snote.getText().toString();

                String gateno = session.getGateNo().toString();
                String societyID = session.getSocietyid();
                System.out.println("Gate No "+session.getGateNo());
                System.out.println("societyID"+societyID);
                String token = session.gettoken();
                System.out.println("token"+token);

                String url_login=  getString(R.string.url) + "Staff/StaffEntry";
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("temp",tem));
                params.add(new BasicNameValuePair("oxygen", oxy));
                params.add(new BasicNameValuePair("pulse", puls));

                params.add(new BasicNameValuePair("ProviderID",sid));
                System.out.println("providerid:"+sid);
                params.add(new BasicNameValuePair("Profile",s_profile));
                System.out.println("profile:"+s_profile);
                params.add(new BasicNameValuePair("pNote",staff_note));
                System.out.println("pnote:"+staff_note);
                params.add(new BasicNameValuePair("EntryGate",gateno));
                System.out.println("Entrygate:"+gateno);
                params.add(new BasicNameValuePair("SocietyID",societyID));
                System.out.println("societyid:"+societyID);
                params.add(new BasicNameValuePair("counter",counter));
                System.out.println("counter:"+counter);
                params.add(new BasicNameValuePair("token",token));
                System.out.println("token:"+token);
                Log.e("CheckingParams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            System.out.println("CheckIn Do in Back groud");

            String s=postData();
            return s;
        }
    }

    private class Staffdetails extends AsyncTask<String, String, String> {


        protected void onPostExecute(String result) {

            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1"))
                {
                    System.out.println("Staff Present");
                    JSONObject jsondata=obj.getJSONObject("response");
                    System.out.println("Obj");
                    JSONArray staffdata = jsondata.getJSONArray("visitors");
                    System.out.println("visitor");
                    final JSONObject jsonstaff = staffdata.getJSONObject(0);
                    System.out.println("json Staff");


                    if(jsondata.getString("ChekInExist").equals("Inside"))
                    {
                        JSONObject aletreadyInstaffdata = jsondata.getJSONObject("InTime");
                        System.out.println("aletreadyInstaffdata" + aletreadyInstaffdata);
                        JSONObject jsonin_staff = aletreadyInstaffdata.getJSONObject(String.valueOf(0));
                        System.out.println("Check staff"+jsondata.getString("ChekInExist"));

                        System.out.println("Staff Already Present");
                        lnoteandimage.setVisibility(GONE);
                        lcheckinBtn.setVisibility(GONE);
                        aleradyCheckIn.setVisibility(VISIBLE);

                        System.out.println(" S Date "+jsonin_staff.getString("Date"));
                        System.out.println(" InTime "+jsonin_staff.getString("InTime"));
                        stdate.setText(jsonin_staff.getString("Date"));
                        sttime.setText(jsonin_staff.getString("InTime"));

                    }
                    else
                    {
                        lcheckinBtn.setVisibility(VISIBLE);
                        aleradyCheckIn.setVisibility(GONE);
                        lnoteandimage.setVisibility(VISIBLE);
                    }
                    nostaff.setVisibility(GONE);
                    lselectimage.setVisibility(GONE);
                    cstaffdetails.setVisibility(VISIBLE);
                    URL = getString(R.string.imageurl) + jsonstaff.getString("photo");
                    Picasso.get().load(getString(R.string.imageurl) + jsonstaff.getString("photo")).into(simage);
                    sname.setText(jsonstaff.getString("full_name"));
                    sdob.setText(jsonstaff.getString("dob"));
                   // scontactno.setText(jsonstaff.getString("cur_con_1"));
                    system_staff_id = jsonstaff.getString("service_prd_reg_id");
                    String contactno = "<u>"+jsonstaff.getString("cur_con_1")+"</u>";
                    scontactno.setClickable(true);
                    scontactno.setTextColor(getResources().getColor(R.color.colorPrimary));
                    scontactno.setText(Html.fromHtml(contactno));
                    scontactno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            try {
                                callIntent.setData(Uri.parse("tel:" + jsonstaff.getString("cur_con_1")));
                                startActivity(callIntent);
                            }
                            catch (Exception e)
                            {}
                        }
                    });
                    scontactno.setLinkTextColor(getResources().getColor(R.color.colorPrimary));
                    sworksince.setText(jsonstaff.getString("since"));
                    sjob.setText(jsonstaff.getString("cat"));
                }
                else
                {
                    nostaff.setVisibility(VISIBLE);
                    System.out.println("Staff Not Present");
                    cstaffdetails.setVisibility(GONE);
                    lViewlatestHistory.setVisibility(GONE);

                }
            }
            catch(Exception e)
            {}
        }

        public String postData() {

            System.out.println("In site Post Data");
            String origresponseText="";
            try {
                String url_login;
                if(counter.equals("1")) {
                    url_login = getString(R.string.url) + "Staff/fetchSocietyStaff";
                }
                else
                {
                    url_login = getString(R.string.url) + "Staff/fetchSocietyStaff1";
                }
                System.out.println("sid"+staffID);
                System.out.println("mobileNo"+mobileno);
                String societyID = session.getSocietyid();
                System.out.println("societyID"+societyID);
                String token = session.gettoken();
                System.out.println("token"+token);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("StaffID",staffID));
                params.add(new BasicNameValuePair("contactNo",mobileno));
                params.add(new BasicNameValuePair("SocietyID",societyID));
                params.add(new BasicNameValuePair("token",token));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                Log.e("staffDetailsParam", String.valueOf(params));
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
            System.out.println("In Do in Back groud");
            String s=postData();
            return s;
        }
    }

}

