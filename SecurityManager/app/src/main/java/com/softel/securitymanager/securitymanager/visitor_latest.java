package com.softel.securitymanager.securitymanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class visitor_latest extends AppCompatActivity {

    String token;
    Session session;
    String VisitorVisitID;
    ListView visitorlist;
    TextView Nvisitorlastest;
    TextView date,status,in,out,visitorname;
    TableLayout allreport;
    EditText estartdate,eenddate;
    TextView todata1;
    Button btndashboard;
    private DatePickerDialog.OnDateSetListener mdateSetListener;
    ArrayList<visitorlatestlist> mylist;
    private DatePickerDialog.OnDateSetListener mdateSetListener1;
    private static int resultCode = 40;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorlatest);
        setTitle("Visitor Entry Reports");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mylist=new ArrayList<>();
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.poor));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        visitorlist=(ListView) findViewById(R.id.visitorlatest);
        Nvisitorlastest=(TextView) findViewById(R.id.Nvisitorlastest);
        date=(TextView) findViewById(R.id.textView);
        status=(TextView) findViewById(R.id.textView2);
        in=(TextView) findViewById(R.id.textView3);
        out=(TextView) findViewById(R.id.textView4);
        allreport=(TableLayout) findViewById(R.id.l2);
        visitorname = (TextView) findViewById(R.id.visitorname);
        btndashboard = (Button) findViewById(R.id.dashboard);
        btndashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(visitor_latest.this,homepage.class);
                startActivity(dashboard);
            }
        });
        session=new Session(getApplicationContext());

        new fetchlatestvisitor().execute();
    }

    public void visitorlatest(View view)
    {
        mylist.clear();
        visitorlist.setVisibility(View.VISIBLE);
        Nvisitorlastest.setVisibility(View.GONE);
        date.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        in.setVisibility(View.VISIBLE);
        out.setVisibility(View.VISIBLE);
       // btndashboard.setVisibility(View.VISIBLE);
        allreport.removeAllViews();
        allreport.setVisibility(View.INVISIBLE);
        new fetchlatestvisitor().execute();

    }

    public void visitorreport(View view)
    {
        allreport.removeAllViews();
        allreport.setVisibility(View.VISIBLE);
        visitorlist.setVisibility(View.INVISIBLE);
        Nvisitorlastest.setVisibility(View.GONE);
        date.setVisibility(View.INVISIBLE);
        status.setVisibility(View.INVISIBLE);
        in.setVisibility(View.INVISIBLE);
        out.setVisibility(View.INVISIBLE);
        //btndashboard.setVisibility(View.GONE);
        //start&endtext
        TableRow rowtxtdate=new TableRow(this);
        rowtxtdate.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView startdate=new TextView(this);

        startdate.setPadding(80,0,150,0);
        int width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,Resources.getSystem().getDisplayMetrics());
        int height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());

        startdate.setWidth(width);
        startdate.setHeight(height);
        startdate.setText("Start Date");
        startdate.setTextColor(Color.GRAY);
        rowtxtdate.addView(startdate);
        final TextView enddate=new TextView(this);
      //  enddate.setPadding(150,0,0,0);

        int width1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,Resources.getSystem().getDisplayMetrics());
        int height1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());

        enddate.setWidth(width1);
        enddate.setHeight(height1);
        enddate.setText("End Date");
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
        rowtextfield.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
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

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(visitor_latest.this,R.style.DialogTheme,mdateSetListener,mYear,mMonth,mDay);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        eenddate=new EditText(this);

        //eenddate.setPadding(150,0,0,0);
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
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(visitor_latest.this,R.style.DialogTheme,mdateSetListener1,mYear,mMonth,mDay);
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

        int twidth1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics());
        int theight1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,Resources.getSystem().getDisplayMetrics());

        todata1.setWidth(twidth1);
        todata1.setHeight(theight1);
        todata1.setText("To");
        todata1.setVisibility(View.INVISIBLE);
        rowtextfield.addView(estartdate);
        rowtextfield.addView(todata1);
        rowtextfield.addView(eenddate);


        RelativeLayout btnlayout=new RelativeLayout(this);
        btnlayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        btnlayout.setGravity(Gravity.CENTER);
        btnlayout.setPadding(0,30,0,0);
        Button btnfetchreport= new Button(this);
        btnfetchreport.setText("Fetch Report");
        btnfetchreport.setTextColor(Color.WHITE);
        btnfetchreport.setBackgroundColor(getResources().getColor(R.color.header));

        int twidthbtn=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,170,Resources.getSystem().getDisplayMetrics());
        int theightbtn=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,Resources.getSystem().getDisplayMetrics());

        btnfetchreport.setWidth(twidthbtn);
        btnfetchreport.setHeight(theightbtn);
        btnfetchreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(estartdate.getText().toString().equals("")  || eenddate.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Enter Valid Date",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        allreport.removeAllViews();
                        visitorname.setVisibility(View.GONE);
                        String startDate = estartdate.getText().toString();
                        String enddate = eenddate.getText().toString();
                        //System.out.println("<"+startDate+"><"+enddate+"><"+staffid+">");
                        new visitorAllreport().execute(startDate, enddate);
                    }

            }
        });
        //btnfetchreport.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        btnlayout.addView(btnfetchreport);
        allreport.addView(rowtxtdate);
        allreport.addView(rowtextfield);
        allreport.addView(btnlayout);

    }



    private class visitorAllreport extends AsyncTask<String, String, String> {
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
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            allreportlist.setLayoutParams(params1);
            ArrayList<visitorlatestlist> allvisitlist=new ArrayList<>();
            Log.d("on post ", "on post execute");
            try {


                RelativeLayout modifylayout=new RelativeLayout(getApplicationContext());
                modifylayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
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
                modifysearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allreport.removeAllViews();
                        visitorreport(v);
                        eenddate.setVisibility(View.VISIBLE);
                        todata1.setVisibility(View.VISIBLE);
                        estartdate.setVisibility(View.VISIBLE);
                        estartdate.setText(cstartdate);
                        eenddate.setText(cenddate);

                    }
                });
                modifylayout.addView(modifysearch);
                allreport.addView(modifylayout);



                LinearLayout allvisitorheader=new LinearLayout(getApplicationContext());
                allvisitorheader.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                allvisitorheader.setPadding(0,20,0,0);
                TextView flat=new TextView(getApplicationContext());
                int width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,Resources.getSystem().getDisplayMetrics());
                int height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,Resources.getSystem().getDisplayMetrics());
                flat.setBackgroundColor(getResources().getColor(R.color.header));
                flat.setWidth(width);
                flat.setHeight(height);
                flat.setText("Flat Visited");
                flat.setTextSize(16);
                flat.setTypeface(Typeface.DEFAULT_BOLD);
                flat.setGravity(Gravity.CENTER);
                flat.setPadding(0,10,0,0);
                flat.setTextColor(Color.WHITE);
                allvisitorheader.addView(flat);


                TextView alldate=new TextView(getApplicationContext());
                alldate.setBackgroundColor(getResources().getColor(R.color.header));
                alldate.setWidth(width);
                alldate.setHeight(height);
                alldate.setText("Date");
                alldate.setTextSize(16);
                alldate.setTypeface(Typeface.DEFAULT_BOLD);
                alldate.setGravity(Gravity.CENTER);
                alldate.setPadding(0,10,0,0);
                alldate.setTextColor(Color.WHITE);
                allvisitorheader.addView(alldate);


                TextView allin=new TextView(getApplicationContext());
                allin.setBackgroundColor(getResources().getColor(R.color.header));
                allin.setWidth(width);
                allin.setHeight(height);
                allin.setText("In (Gate)");

                allin.setTextSize(16);
                allin.setTypeface(Typeface.DEFAULT_BOLD);
                allin.setGravity(Gravity.CENTER);
                allin.setPadding(0,10,0,0);
                allin.setTextColor(Color.WHITE);
                allvisitorheader.addView(allin);


                TextView allout=new TextView(getApplicationContext());
                allout.setBackgroundColor(getResources().getColor(R.color.header));
                allout.setWidth(width);
                allout.setHeight(height);
                allout.setText("Out (Gate)");
                allout.setTextSize(16);
                allout.setTypeface(Typeface.DEFAULT_BOLD);
                allout.setGravity(Gravity.CENTER);
                allout.setPadding(0,10,0,0);
                allout.setTextColor(Color.WHITE);
                allvisitorheader.addView(allout);




                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);

                JSONObject jsondata = obj.getJSONObject("response");

                if(obj.getString("success").equals("1")) {
                    JSONArray visitdata = jsondata.getJSONArray("VisitorReport");

                    System.out.println("visitdata : " + visitdata);
                    JSONObject jsoname=visitdata.getJSONObject(0);
                    String name=jsoname.getString("FullName");
                    TextView txname=new TextView(getApplicationContext());
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                   // int widthname=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,500,Resources.getSystem().getDisplayMetrics());
                    //int heightname=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,Resources.getSystem().getDisplayMetrics());
                    txname.setGravity(Gravity.CENTER);
                    txname.setLayoutParams(params);

                    txname.setPadding(0,10,0,20);
                    txname.setText(Html.fromHtml(modify));
                    txname.setTextColor(Color.WHITE);
                    txname.setTypeface(Typeface.DEFAULT_BOLD);
                    txname.setBackgroundColor(getResources().getColor(R.color.header));
                    txname.setText("Report for  "+name+" from "+cstartdate + " to "+cenddate);

                    for (int i = 0; i < visitdata.length(); i++)
                    {
                        String store="";
                        JSONObject jsonvisit = visitdata.getJSONObject(i);
                        System.out.println("Data1" + jsonvisit);
                        JSONObject jsonTime = jsonvisit.getJSONObject("Timelist");

                        JSONArray unit_no = jsonvisit.getJSONArray("UnitNo");
                        System.out.println("Unit Array : " + unit_no);
                        for (int j = 0; j < unit_no.length(); j++)
                        {
                            JSONArray test1 = unit_no.getJSONArray(j);
                            System.out.println("test : " + test1);

                            store += test1.getString(0)  + "("+test1.getString(3) + ")";
                            store += "/";
                        }
                        String newstr;
                        if (store == "")
                        {
                            newstr = "";
                        }
                        else
                        {
                            newstr = store.substring(0, store.length() - 1);
                        }
                        String checkout;
                        if(jsonvisit.has("checkOut")) {
                             checkout = jsonvisit.getString("checkOut");
                        }
                        else
                        {checkout="";}

                        allvisitlist.add(new visitorlatestlist(
                                jsonTime.getString("Date"),
                                jsonTime.getString("InTime"),
                                newstr,
                                jsonTime.getString("OutDate"),
                                jsonTime.getString("OutTime"),
                                checkout,
                                jsonvisit.getString("Entry_Gate"),
                                jsonvisit.getString("Exit_Gate")
                        ));


                    }
                        System.out.println("Data1");
                    CustomAdapterVisitorLatest adapter = new CustomAdapterVisitorLatest(getApplicationContext(), R.layout.view_visitorlatest, allvisitlist);

                    allreportlist.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(allreportlist);
                    dialog.dismiss();
                    System.out.println("Data2");
                    allreport.addView(txname);
                    allreport.addView(allvisitorheader);
                    allreport.addView(allreportlist);
                   /* RelativeLayout dashboardL=new RelativeLayout(getApplicationContext());
                    //dashboardL.setPadding(0,10,0,0);
                    dashboardL.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
                    Button btndash = new Button(getApplicationContext());
                    btndash.setText("Go To DashBoard");
                    btndash.setTextColor(Color.WHITE);
                    btndash.setBackgroundColor(Color.argb(255,72,138,255));
                    dashboardL.setGravity(Gravity.CENTER);
                    int widthname=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,170,Resources.getSystem().getDisplayMetrics());
                    int heightname=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,Resources.getSystem().getDisplayMetrics());

                    btndash.setWidth(widthname);
                    btndash.setHeight(heightname);
                    dashboardL.addView(btndash);
                    allreport.addView(dashboardL);
                    btndash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent dash = new Intent(visitor_latest.this,homepage.class);
                            startActivity(dash);
                        }
                    });*/

                }
               // CustomAdapterVisitorLatest adapter = new CustomAdapterVisitorLatest(getApplicationContext(), R.layout.view_visitorlatest, allvisitlist);


            }
            catch (Exception e)
            {

            }
        }

        public String postData(String startdate,String enddate)
        {
            Bundle visitreport=getIntent().getExtras();
            VisitorVisitID=visitreport.getString(
                    "VisitorVisitID");
            token=session.gettoken();


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("VisitorVisitID", VisitorVisitID));



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

            String url_login = getString(R.string.url) + "Visitor/VisitorVisitReport";
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


    private class fetchlatestvisitor extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            int k=1;
            try {
                visitorname.setVisibility(View.VISIBLE);
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                JSONObject jsondata=obj.getJSONObject("response");

              if(obj.getString("success").equals("1"))
                {
                    JSONArray visitdata = jsondata.getJSONArray("VisitorReport");

                    System.out.println("visitdata : " + visitdata);

                    for (int i = 0; i < visitdata.length(); i++) {
                        String store="";
                        JSONObject jsonvisit = visitdata.getJSONObject(i);
                        JSONObject jsonTime=jsonvisit.getJSONObject("Timelist");

                        JSONArray unit_no=jsonvisit.getJSONArray("UnitNo");
                        System.out.println("Unit Array : "+ unit_no);
                        for(int j=0;j<unit_no.length();j++)
                        {
                            JSONArray test1=unit_no.getJSONArray(j);
                            System.out.println("test : "+test1);

                                store += test1.getString(0)  + "("+test1.getString(3) + ")";
                                store += "/";
                        }
                        String newstr;
                        if(store == "")
                        {
                            newstr ="";
                        }
                        else
                        {
                            newstr = store.substring(0, store.length()-1);
                        }
                        System.out.println("Unit No : " +newstr);
                        String checkout="";
                        if(jsonvisit.has("checkOut"))
                        {
                            checkout=jsonvisit.getString("checkOut");
                        }
                        else
                        {
                            checkout="";
                        }
                        mylist.add(new visitorlatestlist(
                                jsonTime.getString("Date"),
                                jsonTime.getString("InTime"),
                                newstr,
                                jsonTime.getString("OutDate"),
                                jsonTime.getString("OutTime"),
                                checkout,
                                jsonvisit.getString("Entry_Gate"),
                                jsonvisit.getString("Exit_Gate")
                        ));
                    }

                    CustomAdapterVisitorLatest adapter=new CustomAdapterVisitorLatest(getApplicationContext(),R.layout.view_visitorlatest,mylist);
                    visitorlist.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(visitorlist);
                }
                else
                {
                    Nvisitorlastest.setText("No Visitor Record Availbale");
                    Nvisitorlastest.setGravity(Gravity.CENTER);
                    Nvisitorlastest.setTextColor(Color.GRAY);
                    Nvisitorlastest.setVisibility(View.VISIBLE);
                }

            }
            catch (Exception e)
            {
            }
        }

        public String postData()
        {

            String output="";
            Bundle visitreport=getIntent().getExtras();
            VisitorVisitID=visitreport.getString("VisitorVisitID");
            visitorname.setText("Report For " + visitreport.getString("visitname"));
            token=session.gettoken();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("VisitorVisitID", VisitorVisitID));

            String url_login = getString(R.string.url) + "Visitor/LatestVisitReport";
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
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
