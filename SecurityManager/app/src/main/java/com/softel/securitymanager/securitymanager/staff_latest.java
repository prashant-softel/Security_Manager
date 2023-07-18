package com.softel.securitymanager.securitymanager;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.DatePickerDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.DatePicker;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
import java.util.List;

import android.util.TypedValue;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class staff_latest extends AppCompatActivity {
    String token,staffid="";
    Session session;
    String jobprofile,staffname;
    ListView stafflist;
    TextView staffdata;
    TextView Nstafflastest;
    TextView date,status,in,out;
    TableLayout allreport;
    EditText estartdate,eenddate;
    TextView todata1;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    ArrayList<stafflatestlist> mylist;
    private DatePickerDialog.OnDateSetListener mdateSetListener1;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Staff Report");

        setContentView(R.layout.activity_staff_latest);
        mylist=new ArrayList<>();
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        stafflist=(ListView) findViewById(R.id.stafflatest);
        staffdata=(TextView) findViewById(R.id.staffname);
        Nstafflastest=(TextView) findViewById(R.id.Nstafflastest);
        date=(TextView) findViewById(R.id.textView);
        status=(TextView) findViewById(R.id.textView2);
        in=(TextView) findViewById(R.id.textView3);
        out=(TextView) findViewById(R.id.textView4);
        allreport=(TableLayout) findViewById(R.id.l2);
        session=new Session(getApplicationContext());
        new fetchlateststaff().execute();

    }

    public void stafflastest(View view)
    {
        mylist.clear();
        stafflist.setVisibility(View.VISIBLE);
        Nstafflastest.setVisibility(View.GONE);
        date.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        in.setVisibility(View.VISIBLE);
        out.setVisibility(View.VISIBLE);
            allreport.removeAllViews();
            allreport.setVisibility(View.INVISIBLE);
        new fetchlateststaff().execute();
    }

    public void staffreport(View view)
    {
        allreport.removeAllViews();
        allreport.setVisibility(View.VISIBLE);
        stafflist.setVisibility(View.INVISIBLE);
        Nstafflastest.setVisibility(View.GONE);
        date.setVisibility(View.INVISIBLE);
        status.setVisibility(View.INVISIBLE);
        in.setVisibility(View.INVISIBLE);
        out.setVisibility(View.INVISIBLE);
        //start&endtext
        TableRow rowtxtdate=new TableRow(this);
        rowtxtdate.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        TextView startdate=new TextView(this);

        startdate.setPadding(80,0,150,0);
        int width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,Resources.getSystem().getDisplayMetrics());
        int height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());

        startdate.setWidth(width);
        startdate.setHeight(height);
        startdate.setText(getString(R.string.start_date));
        startdate.setTextColor(Color.GRAY);
        rowtxtdate.addView(startdate);
        final TextView enddate=new TextView(this);
       enddate.setPadding(150,0,0,0);

        int width1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,Resources.getSystem().getDisplayMetrics());
        int height1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());

        enddate.setWidth(width1);
        enddate.setHeight(height1);
        enddate.setText(getString(R.string.end_date));
        enddate.setTextColor(Color.GRAY);


        TextView todata=new TextView(this);
        todata.setPadding(30,0,0,0);

        int twidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,Resources.getSystem().getDisplayMetrics());
        int theight=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());

        todata.setWidth(twidth);
        todata.setHeight(theight);
        todata.setText("");
        rowtxtdate.addView(todata);
        rowtxtdate.addView(enddate);

        TableRow rowtextfield=new TableRow(this);
        rowtextfield.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        estartdate=new EditText(this);

        estartdate.setPadding(60,0,0,0);
        estartdate.setFocusable(false);
        startdate.setClickable(true);
        estartdate.setId(R.id.estartdate);
        estartdate.setBackgroundColor(getResources().getColor(R.color.transparent));
        int widthestartdate=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,Resources.getSystem().getDisplayMetrics());
        int heightestartdate=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,Resources.getSystem().getDisplayMetrics());
       // estartdate.setEms(10);
        estartdate.setWidth(widthestartdate);
        estartdate.setHeight(heightestartdate);
        estartdate.setVisibility(View.INVISIBLE);

        startdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(staff_latest.this,R.style.DialogTheme,mdateSetListener,mYear,mMonth,mDay);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        eenddate=new EditText(this);

        eenddate.setPadding(150,0,0,0);
        eenddate.setFocusable(false);
        enddate.setClickable(true);
        eenddate.setId(R.id.estartdate);
       // eenddate.setEms();
        int widthenddate=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,Resources.getSystem().getDisplayMetrics());
        int heightenddate=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,Resources.getSystem().getDisplayMetrics());

        eenddate.setWidth(widthestartdate);
        eenddate.setHeight(heightestartdate);
        eenddate.setVisibility(View.INVISIBLE);
        eenddate.setBackgroundColor(getResources().getColor(R.color.transparent));
        enddate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(staff_latest.this,R.style.DialogTheme,mdateSetListener1,mYear,mMonth,mDay);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mdateSetListener1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String mon="";
                String day;
                if(dayOfMonth<=9)
                {
                    day="0"+String.valueOf(dayOfMonth);
                }
                else
                    day= String.valueOf(dayOfMonth);
                String months[] = {"Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "Jul", "Aug", "Sept",
                        "Oct", "Nov", "Dec"};
                for(int i=0;i<months.length;i++)
                {
                    if(month==(i+1))
                    {mon=months[i];}
                }

                System.out.println(dayOfMonth + "-"+mon+"-"+year);
                eenddate.setVisibility(View.VISIBLE);
                eenddate.setText(day + "-" +mon+"-"+year );
            }
        };

         todata1=new TextView(this);
        mdateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String mon="";
                String day;
                if(dayOfMonth<=9)
                {
                    day="0"+String.valueOf(dayOfMonth);
                }
                else {
                    day = String.valueOf(dayOfMonth);
                }
                String months[] = {"Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "Jul", "Aug", "Sept",
                        "Oct", "Nov", "Dec"};
                for(int i=0;i<months.length;i++)
                {
                    if(month==(i+1))
                    {mon=months[i];}
                }
                estartdate.setVisibility(View.VISIBLE);

                System.out.println(day + "-"+mon+"-"+year);
                estartdate.setText(day + "-" +mon+"-"+year );
                todata1.setVisibility(View.VISIBLE);
            }
        };

        int twidth1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,Resources.getSystem().getDisplayMetrics());
        int theight1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,Resources.getSystem().getDisplayMetrics());

        todata1.setWidth(twidth1);
        todata1.setHeight(theight1);
        todata1.setText(getString(R.string.to));
        todata1.setVisibility(View.INVISIBLE);
        rowtextfield.addView(estartdate);
        rowtextfield.addView(todata1);
        rowtextfield.addView(eenddate);


        RelativeLayout btnlayout=new RelativeLayout(this);
        btnlayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        btnlayout.setGravity(Gravity.CENTER);
        Button btnfetchreport= new Button(this);
        btnfetchreport.setText(getString(R.string.fetch_report));
        btnfetchreport.setTextColor(Color.WHITE);
        btnfetchreport.setBackgroundColor(getResources().getColor(R.color.header));

        int twidthbtn=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,170,Resources.getSystem().getDisplayMetrics());
        int theightbtn=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,Resources.getSystem().getDisplayMetrics());

        btnfetchreport.setWidth(twidthbtn);
        btnfetchreport.setHeight(theightbtn);
        btnfetchreport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(estartdate.getText().toString().equals("")  || eenddate.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Date",Toast.LENGTH_SHORT).show();
                }
                else {
                    allreport.removeAllViews();
                    String startDate = estartdate.getText().toString();

                    String enddate = eenddate.getText().toString();
                    //System.out.println("<"+startDate+"><"+enddate+"><"+staffid+">");
                    new StaffAllreport().execute(startDate, enddate);
                }

            }
        });
        //btnfetchreport.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        btnlayout.addView(btnfetchreport);
        allreport.addView(rowtxtdate);
        allreport.addView(rowtextfield);
        allreport.addView(btnlayout);

    }


    private class StaffAllreport extends AsyncTask<String, String, String> {
        String output="";
        String cstartdate,cenddate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        protected void onPostExecute(String result)
        {

            ListView allreportlist = new ListView(getApplicationContext());
            ArrayList<allstafflist> allstafflist=new ArrayList<>();
            Log.d("on post ", "on post execute");
            try {


                RelativeLayout modifylayout=new RelativeLayout(getApplicationContext());
                modifylayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
               // modifylayout.setGravity(Gravity.RIGHT);

                TextView modifysearch=new TextView(getApplicationContext());
                modifysearch.setLinksClickable(true);
                String modify="<u>Modify Search</u>";
                //modifysearch.setPadding(80,0,150,0);
                int width1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,500,Resources.getSystem().getDisplayMetrics());
                int height1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());
                modifysearch.setGravity(Gravity.END);
                modifysearch.setWidth(width1);
                modifysearch.setHeight(height1);
                modifysearch.setText(Html.fromHtml(modify));
                modifysearch.setTextColor(getResources().getColor(R.color.header));
                modifysearch.setClickable(true);
                modifysearch.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allreport.removeAllViews();
                        staffreport(v);
                        eenddate.setVisibility(View.VISIBLE);
                        todata1.setVisibility(View.VISIBLE);
                        estartdate.setVisibility(View.VISIBLE);
                        estartdate.setText(cstartdate);
                        eenddate.setText(cenddate);

                    }
                });
                modifylayout.addView(modifysearch);
                allreport.addView(modifylayout);


              LinearLayout allstaffheader=new LinearLayout(getApplicationContext());
              allstaffheader.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                allstaffheader.setPadding(0,20,0,0);
                TextView alldate=new TextView(getApplicationContext());
                int width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,73,Resources.getSystem().getDisplayMetrics());
                int height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,Resources.getSystem().getDisplayMetrics());
                alldate.setBackgroundColor(getResources().getColor(R.color.header));
                alldate.setWidth(width);
                alldate.setHeight(height);
                alldate.setText(getString(R.string.date));
                alldate.setTextSize(16);
                alldate.setTypeface(Typeface.DEFAULT_BOLD);
                alldate.setGravity(Gravity.CENTER);
                alldate.setPadding(0,10,0,0);
                alldate.setTextColor(Color.WHITE);
                allstaffheader.addView(alldate);


                TextView allstatus=new TextView(getApplicationContext());
                allstatus.setBackgroundColor(getResources().getColor(R.color.header));
                allstatus.setWidth(width);
                allstatus.setHeight(height);
                allstatus.setText(getString(R.string.statu));
                allstatus.setTextSize(16);
                allstatus.setTypeface(Typeface.DEFAULT_BOLD);
                allstatus.setGravity(Gravity.CENTER);
                allstatus.setPadding(0,10,0,0);
                allstatus.setTextColor(Color.WHITE);
                allstaffheader.addView(allstatus);


                TextView allin=new TextView(getApplicationContext());
                allin.setBackgroundColor(getResources().getColor(R.color.header));
                allin.setWidth(width);
                allin.setHeight(height);
                allin.setText(getString(R.string.in));
                allin.setTextSize(16);
                allin.setTypeface(Typeface.DEFAULT_BOLD);
                allin.setGravity(Gravity.CENTER);
                allin.setPadding(0,10,0,0);
                allin.setTextColor(Color.WHITE);
                allstaffheader.addView(allin);


                TextView allout=new TextView(getApplicationContext());
                allout.setBackgroundColor(getResources().getColor(R.color.header));
                allout.setWidth(width);
                allout.setHeight(height);
                allout.setTextSize(16);
                allout.setTypeface(Typeface.DEFAULT_BOLD);
                allout.setText(getString(R.string.out));
                allout.setGravity(Gravity.CENTER);
                allout.setPadding(0,10,0,0);
                allout.setTextColor(Color.WHITE);
                allstaffheader.addView(allout);


                TextView allhours=new TextView(getApplicationContext());
                allhours.setBackgroundColor(getResources().getColor(R.color.header));
                allhours.setWidth(width);
                allhours.setHeight(height);
                allhours.setText(getString(R.string.hours));
                allhours.setTextSize(16);
                allhours.setTypeface(Typeface.DEFAULT_BOLD);
                allhours.setGravity(Gravity.CENTER);
                allhours.setPadding(0,10,0,0);
                allhours.setTextColor(Color.WHITE);
                allstaffheader.addView(allhours);



                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);

                JSONObject jsondata = obj.getJSONObject("response");
                //tv.setText(result);
                if (obj.getString("success").equals("1")) {
                    JSONArray staffdata = jsondata.getJSONArray("AttendanceReport");
                    for (int i = 0; i < staffdata.length(); i++) {
                        JSONArray jsonstaff = staffdata.getJSONArray(i);
                        allstafflist.add(new allstafflist(
                               jsonstaff.get(0).toString(),
                                jsonstaff.get(1).toString(),
                                jsonstaff.get(2).toString(),
                                jsonstaff.get(3).toString(),
                                jsonstaff.get(4).toString(),
                                jsonstaff.get(5).toString()

                        ));
                    }

                    CustomAdapterStaffreport adapter=new CustomAdapterStaffreport(getApplicationContext(),R.layout.view_allstaffreport,allstafflist);
                    allreportlist.setAdapter(adapter);
                    allreport.addView(allstaffheader);
                    allreport.addView(allreportlist);
                    dialog.dismiss();
                }
            }
            catch (Exception e)
            {

            }
            }

        public String postData(String startdate,String enddate)
        {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("StaffID", staffid));

            String[] stdate=startdate.split("-");
            String sday=stdate[0];
            String smonth=stdate[1];
            String syear=stdate[2];
            int smon=0;
            String months[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};
            for(int i=0;i<months.length;i++)
            {
                if(smonth.equals(months[i]))
                {smon=(i+1);}
            }
            String jstartdate=syear+"-"+smon+"-"+sday;

            String[] endate=enddate.split("-");
            String eday=endate[0];
            String emonth=endate[1];
            String eyear=endate[2];
            int emon=0;
            String monthse[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};
            for(int i=0;i<monthse.length;i++)
            {
                if(emonth.equals(monthse[i]))
                {emon=(i+1);}
            }
            String jenddate=eyear+"-"+emon+"-"+eday;

            params.add(new BasicNameValuePair("startDate", jstartdate));
            params.add(new BasicNameValuePair("EndtDate", jenddate));

           String url_login = getString(R.string.url) + "Staff/fetchStaffReportDetails";
            JSONParser jParser=new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
            System.out.println(json);
            output = json.toString();
            System.out.println(output);
            return output;

        }
        @Override
        protected String doInBackground(String... params) {
            cstartdate=(String) params[0];
            cenddate=(String)params[1];
            String s = postData(cstartdate,cenddate);
            return s;
        }

    }


    private class fetchlateststaff extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            staffdata.setText(staffname+"("+jobprofile+")");
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
                    stafflist.setAdapter(adapter);
                }
                else
                {
                    Nstafflastest.setText(getString(R.string.no_staff_rec_available));
                    Nstafflastest.setGravity(Gravity.CENTER);
                    Nstafflastest.setTextColor(Color.GRAY);
                    Nstafflastest.setTextColor(View.VISIBLE);

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
            staffname=staffdata.getString("FirstName");
            jobprofile=staffdata.getString("JobProfile");
            token=session.gettoken();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("StaffID", staffid));
            String url_login = getString(R.string.url) + "Staff/fetchLatestReport";
            JSONParser jParser=new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
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
