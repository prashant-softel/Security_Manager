package com.softel.securitymanager.securitymanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class visitorreport extends AppCompatActivity {

    TextView startdate, enddate,modifysearch,norecord;
    TextView estartdate, eenddate;
    String token;
    Session session;
    TextView to1;
    TableLayout allreport;
    Button btnfetchreport;
    LinearLayout header;
    ListView list;

    TableRow t2;
    ProgressDialog dialog;
    ArrayList<allvisitlist> mylist;
    private DatePickerDialog.OnDateSetListener mdateSetListener;

    private DatePickerDialog.OnDateSetListener mdateSetListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitorreport);
        setTitle(R.string.visitor_entry_reports);

        session = new Session(getApplicationContext());
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        t2=(TableRow) findViewById(R.id.t2);
        allreport=(TableLayout) findViewById(R.id.allreport);
        startdate=(TextView) findViewById(R.id.startdate);
        enddate=(TextView) findViewById(R.id.enddate);
        estartdate=(TextView) findViewById(R.id.estartdate);
        eenddate=(TextView) findViewById(R.id.eenddate) ;
        to1=(TextView) findViewById(R.id.to);
        btnfetchreport=(Button) findViewById(R.id.btnfetchreport);
        modifysearch=(TextView) findViewById(R.id.modifyserach);
        header=(LinearLayout) findViewById(R.id.header);
        list=(ListView) findViewById(R.id.list);
        norecord=(TextView) findViewById(R.id.norecord);
        mylist=new ArrayList<>();
list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        allvisitlist visit=mylist.get(position);
        System.out.println(visit.getId());
        Intent visitordetails=new Intent(visitorreport.this,visitordetails.class);
        visitordetails.putExtra("visitorId",visit.getId());
        startActivity(visitordetails);
    }
});
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(visitorreport.this, R.style.DialogTheme, mdateSetListener, mYear, mMonth, mDay);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(visitorreport.this, R.style.DialogTheme, mdateSetListener1, mYear, mMonth, mDay);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mdateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mon = "";
                String day;
                if (dayOfMonth <= 9) {
                    day = "0" + String.valueOf(dayOfMonth);
                } else
                    day = String.valueOf(dayOfMonth);
                String months[] = {"Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "Jul", "Aug", "Sept",
                        "Oct", "Nov", "Dec"};
                for (int i = 0; i < months.length; i++) {
                    if (month == (i + 1)) {
                        mon = months[i];
                    }
                }

                System.out.println(dayOfMonth + "-" + mon + "-" + year);
                eenddate.setVisibility(View.VISIBLE);
                eenddate.setText(day + "-" + mon + "-" + year);
            }
        };


        mdateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mon = "";
                String day;
                if (dayOfMonth <= 9) {
                    day = "0" + String.valueOf(dayOfMonth);
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                String months[] = {"Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "Jul", "Aug", "Sept",
                        "Oct", "Nov", "Dec"};
                for (int i = 0; i < months.length; i++) {
                    if (month == (i + 1)) {
                        mon = months[i];
                    }
                }
                t2.setVisibility(View.VISIBLE);
                estartdate.setVisibility(View.VISIBLE);

                System.out.println(day + "-" + mon + "-" + year);
                estartdate.setText(day + "-" + mon + "-" + year);
                to1.setVisibility(View.VISIBLE);
            }
        };
        btnfetchreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estartdate.getText().toString().equals("")  || eenddate.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Date",Toast.LENGTH_SHORT).show();
                }
                else {

                    allreport.setVisibility(View.GONE);
                    btnfetchreport.setVisibility(View.GONE);
                    modifysearch.setVisibility(View.VISIBLE);
                    header.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);

                    String startDate = estartdate.getText().toString();
                    String enddate = eenddate.getText().toString();
                    //System.out.println("<"+startDate+"><"+enddate+"><"+staffid+">");
                    new visitreport().execute(startDate, enddate);
                }
            }
        });


    }




    private class visitreport extends AsyncTask<String, String, String> {
        String output = "";
        String cstartdate, cenddate;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            //Toast.makeText(getApplicationContext(),"Please wait data is laoding..",Toast.LENGTH_SHORT).show();
        }
        protected void onPostExecute(String result) {

            Log.d("on post ", "on post execute");
            try {

                 modifysearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        header.setVisibility(View.GONE);
                        norecord.setVisibility(View.GONE);
                        modifysearch.setVisibility(View.GONE);
                        list.setVisibility(View.INVISIBLE);
                        mylist.clear();
                        allreport.setVisibility(View.VISIBLE);
                        btnfetchreport.setVisibility(View.VISIBLE);

                        startdate.setVisibility(View.VISIBLE);
                        enddate.setVisibility(View.VISIBLE);
                        eenddate.setVisibility(View.VISIBLE);
                        to1.setVisibility(View.VISIBLE);
                        estartdate.setVisibility(View.VISIBLE);
                        estartdate.setText(cstartdate);
                        eenddate.setText(cenddate);

                    }
                });



                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);

                JSONObject jsondata = obj.getJSONObject("response");
                if(obj.getString("success").equals("1")) {

                    JSONArray visitdata = jsondata.getJSONArray("VisitorReport");

                    for (int i = 0; i < visitdata.length(); i++) {
                        String store = "";
                        int k = 1;
                        String check = "";
                        JSONObject jsonvisit = visitdata.getJSONObject(i);
                        if (!(jsonvisit.getString("unit_id").equals("-1"))) {
                            System.out.println("Data1" + jsonvisit);
                            JSONObject jsonTime = jsonvisit.getJSONObject("Timelist");

                            JSONArray unit_no = jsonvisit.getJSONArray("UnitNo");
                            System.out.println("Unit Array : " + unit_no);

                            for (int j = 0; j < unit_no.length(); j++) {

                                JSONArray test1 = unit_no.getJSONArray(j);
                                System.out.println("test : " + test1);
                                if (unit_no.length() == k && test1.getString(2).equals("2")) {
                                    check = "data";
                                } else {
                                    check = "";
                                }
                            }

                            for (int j = 0; j < unit_no.length(); j++) {
                                JSONArray test1 = unit_no.getJSONArray(j);
                                System.out.println("test : " + test1);

                                store += test1.getString(0) + "(" + test1.getString(3) +")";
                                store += "/";
                            }
                            String newstr;
                            if (store == "") {
                                newstr = "";
                            } else {
                                newstr = store.substring(0, store.length() - 1);
                            }
                            String checkout;
                            if (jsonvisit.has("checkOut")) {
                                checkout = jsonvisit.getString("checkOut");
                            } else {
                                checkout = "";
                            }
                           /* System.out.println("id " + jsonvisit.getString("id"));
                            System.out.println("Full Name " + jsonvisit.getString("FullName"));
                            System.out.println(jsonTime.getString("Date"));
                            System.out.println(jsonTime.getString("InTime"));
                            System.out.println(newstr);
                            System.out.println(jsonTime.getString("OutDate"));
                            System.out.println(jsonTime.getString("OutTime"));
                            System.out.println(checkout);
                            System.out.println(jsonvisit.getString("Entry_Gate"));*/
                            String Exit_Gate="";
                            if(jsonvisit.has("Exit_Gate"))
                            {
                               Exit_Gate=jsonvisit.getString("Exit_Gate");
                            }
                           System.out.println(Exit_Gate);
                            System.out.println(check);

                            mylist.add(new allvisitlist(
                                    jsonvisit.getString("id"),
                                    jsonvisit.getString("FullName"),
                                    jsonTime.getString("Date"),
                                    jsonTime.getString("InTime"),
                                    newstr,
                                    jsonTime.getString("OutDate"),
                                    jsonTime.getString("OutTime"),
                                    checkout,
                                    jsonvisit.getString("Entry_Gate"),
                                    Exit_Gate,
                                    check
                            ));


                        }
                    }

                    CustomAdapterVisitReport adapter = new CustomAdapterVisitReport(getApplicationContext(), R.layout.visitlistreport, mylist);
                    list.setAdapter(adapter);
                    dialog.dismiss();
                }
                else
                {
                    dialog.dismiss();
                    norecord.setText(getString(R.string.no_records_available));
                    norecord.setVisibility(View.INVISIBLE);
                }

            } catch (Exception e) {

            }


        }

        public String postData(String startdate, String enddate) {
            Bundle visitreport = getIntent().getExtras();
            token = session.gettoken();


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));


            String[] stdate = startdate.split("-");
            String sday = stdate[0];
            String smonth = stdate[1];
            String syear = stdate[2];
            int smon = 0;
            String months[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};
            for (int i = 0; i < months.length; i++) {
                if (smonth.equals(months[i])) {
                    smon = (i + 1);
                }
            }
            String jstartdate = syear + "-" + smon + "-" + sday;

            String[] endate = enddate.split("-");
            String eday = endate[0];
            String emonth = endate[1];
            String eyear = endate[2];
            int emon = 0;
            String monthse[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};
            for (int i = 0; i < monthse.length; i++) {
                if (emonth.equals(monthse[i])) {
                    emon = (i + 1);
                }
            }
            String jenddate = eyear + "-" + emon + "-" + eday;

            params.add(new BasicNameValuePair("startDate", jstartdate));
            params.add(new BasicNameValuePair("EndDate", jenddate));

            String url_login = getString(R.string.url) + "Visitor/VisitorReport";
            JSONParser jParser=new JSONParser();
            JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
            System.out.println(json);
            output = json.toString();
            System.out.println(output);
            return output;

        }

        @Override
        protected String doInBackground(String... params) {
            cstartdate = (String) params[0];
            cenddate = (String) params[1];
            String s = postData(cstartdate, cenddate);
            return s;
        }

    }

}