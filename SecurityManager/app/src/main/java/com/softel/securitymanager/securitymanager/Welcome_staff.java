package com.softel.securitymanager.securitymanager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;
public class Welcome_staff extends AppCompatActivity {
    Session session;
    String token,staffid="";

    String jobprofile,staffname;

    TextView society_name,swelcome_msg,scontact,sentry_date,sentry_time,sjob_profile,sgate_no;
    Button back_to_dashboard;
    ListView staffvisitlistView;
    ArrayList<stafflatestlist> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.welcome_staff));
        setContentView(R.layout.activity_welcome_staff);


        society_name = (TextView) findViewById(R.id.societyname);
        swelcome_msg = (TextView) findViewById(R.id.welcometxt);
        scontact = (TextView) findViewById(R.id.scontact);
        sentry_date = (TextView) findViewById(R.id.sentry_date);
        sentry_time = (TextView) findViewById(R.id.sentry_time);
        sjob_profile = (TextView) findViewById(R.id.sjobprofile);
        sgate_no = (TextView) findViewById(R.id.sentry_gate);
        back_to_dashboard = (Button) findViewById(R.id.back_to_dashboard);
        staffvisitlistView=(ListView) findViewById(R.id.staffvisitlistView);
        Bundle staffdata;
        staffdata = getIntent().getExtras();
        String staffid = staffdata.getString("StaffID");
        String staffentry_id = staffdata.getString("EntryID");
        session=new Session(getApplicationContext());
        new fetchstaffDetails().execute();
        back_to_dashboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(Welcome_staff.this,homepage.class);
                startActivity(dashboard);
            }
        });

    }

    @Override
    public void onBackPressed() {
        System.out.println("BackPressed");
        Intent FingerPrint_page = new Intent(Welcome_staff.this,Staff_Entry.class);

        startActivity(FingerPrint_page);

    }


    public void showstaffreport(View view) {

        new fetchlateststaff().execute();
    }

   /* public void Backtodashboard(View view) {

        Intent dashboard = new Intent(this,homepage.class);
        startActivity(dashboard);


    }*/
   private class fetchlateststaff extends AsyncTask<String, String, String> {

       protected void onPostExecute(String result) {
           Log.d("on post ", "on post execute");
           try {

               JSONObject obj = new JSONObject(result);
               System.out.println("Result : " + obj);
               JSONObject jsondata=obj.getJSONObject("response");
               //tv.setText(result);
               if(obj.getString("success").equals("1"))
               {
                   JSONArray staffdata = jsondata.getJSONArray("LetestFive");
                   System.out.println("Staff Array : " + staffdata);
                   for (int i = 0; i < staffdata.length(); i++) {
                       JSONObject jsonstaff = staffdata.getJSONObject(i);
                       mylist.add(new stafflatestlist(
                               jsonstaff.getString("Date"),
                               jsonstaff.getString("InTime"),
                               jsonstaff.getString("OutTime"),
                               jsonstaff.getString("attendance")
                       ));
                   }

                   CustomAdapterStaffLatest adapter=new CustomAdapterStaffLatest(getApplicationContext(),R.layout.view_lateststaff,mylist);
                   staffvisitlistView.setAdapter(adapter);
               }


           }
           catch (Exception e)
           {
           }
       }

       public String postData() {

           String output="";

           System.out.println("StaffID "+staffid);
           System.out.println("Sname "+staffname);
           jobprofile= sjob_profile.getText().toString();
           token=session.gettoken();
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair("token", token));
           params.add(new BasicNameValuePair("StaffID", staffid));
           String url_login = getString(R.string.url) + "Staff/fetchLatestReport";
           JSONParser jParser=new JSONParser();
           JSONObject  json = jParser.makeHttpRequest(url_login, "POST", params);
           System.out.println(json);
           output = json.toString();
           System.out.println(output);
           return output;
       }

       @Override
       protected String doInBackground(String... params) {
           String s = postData();
           return s;
       }

   }

    private class fetchstaffDetails extends AsyncTask<String, String, String> {
        protected void onPostExecute(String result) {

            Log.d("on post ", "on post execute");

            try {

                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success=obj.getString("success");
                if (success.contains("1"))
                {
                    JSONObject jsondata=obj.getJSONObject("response");
                    JSONArray staffdata = jsondata.getJSONArray("welcome");
                    final JSONObject jsonstaff = staffdata.getJSONObject(0);
                    JSONObject staffdataresult = jsondata.getJSONObject("Result");
                    staffname = jsonstaff.getString("full_name");
                    System.out.println("jsonstaff"+jsonstaff);
                    System.out.println("Session "+session.getSocietyName());
                    society_name.setText(session.getSocietyName());
                    String welcolemsg = "Welcome "+jsonstaff.getString("full_name")+", your attendance is recorded";
                    swelcome_msg.setText(welcolemsg);
                    //scontact.setText(jsonstaff.getString("cur_con_1"));

                    String contactno = "<u>"+jsonstaff.getString("cur_con_1")+"</u>";
                    scontact.setClickable(true);
                    scontact.setTextColor(getResources().getColor(R.color.colorPrimary));
                    scontact.setText(Html.fromHtml(contactno));
                    scontact.setOnClickListener(new View.OnClickListener() {
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

                    sentry_date.setText(staffdataresult.getString("Date"));
                    sentry_time.setText(staffdataresult.getString("InTime"));
                    sgate_no.setText(session.getGateNo());
                    sjob_profile.setText(staffdataresult.getString("Profile"));

                }
                else
                {

                }
            }
            catch (Exception e)
            {
            }
        }

        public String postData() {


            Bundle staffdata;
            String output="";
            staffdata = getIntent().getExtras();
            staffid = staffdata.getString("StaffID");
            String staffentry_id=staffdata.getString("EntryID");
            String url_login = getString(R.string.url) + "Staff/StaffEntryWelcome";
            token=session.gettoken();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("StaffEntryId", staffentry_id));
            params.add(new BasicNameValuePair("StaffId", staffid));
            params.add(new BasicNameValuePair("token", token));
            JSONParser jParser=new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
            System.out.println(json);
            output = json.toString();
            System.out.println(output);
            return output;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = postData();
            return s;
        }

    }


}
