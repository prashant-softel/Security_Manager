package com.example.securitymanager.securitymanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class welcomevisit extends AppCompatActivity {

    Session session;
    TextView txtname, sname, contact, edate, etime, egate, purpose, company, vehicle, note, contacttext;
    ListView list;
    Button dashboard;
    LinearLayout vehicleL, companyL, noteL;
    ArrayList<VisitorDetailsUnit> mylist;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(welcomevisit.this, homepage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomevisit);
        setTitle(R.string.welcome_visitor);
        session = new Session(getApplicationContext());
        txtname = (TextView) findViewById(R.id.txtname);
        sname = (TextView) findViewById(R.id.txtsname);
        contact = (TextView) findViewById(R.id.wcontactno);
        contacttext = (TextView) findViewById(R.id.contacttext);
        edate = (TextView) findViewById(R.id.entrydate);
        etime = (TextView) findViewById(R.id.entrytime);
        egate = (TextView) findViewById(R.id.entrygate);
        purpose = (TextView) findViewById(R.id.purposewelcome);
        company = (TextView) findViewById(R.id.companywelcome);
        vehicle = (TextView) findViewById(R.id.vehiclewelcome);
        note = (TextView) findViewById(R.id.note);
        noteL = (LinearLayout) findViewById(R.id.noteL);
        list = (ListView) findViewById(R.id.list);
        dashboard = (Button) findViewById(R.id.dashboard);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(welcomevisit.this, homepage.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                finish();
            }
        });
        vehicleL = (LinearLayout) findViewById(R.id.vehicleL);
        companyL = (LinearLayout) findViewById(R.id.companyL);
        mylist = new ArrayList<>();
        new welcomevisitor().execute();
    }

    private class welcomevisitor extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    JSONObject objvisit = obj.getJSONObject("response");
                    JSONArray visitarray = objvisit.getJSONArray("visitors");
                    JSONObject visit = visitarray.getJSONObject(0);
                    JSONObject visitdetail = visit.getJSONObject("VisitorDetails");

                    JSONObject visitdetail1 = visitdetail.getJSONObject("0");
                    txtname.setText("Welcome " + visitdetail1.getString("FullName") + " to ");
                    sname.setText(session.getSocietyName());
                    if (visit.getString("Entry_With").equals("2")) {
                        contacttext.setText(getString(R.string.aadhar));
                    }
                    if (visit.getString("Entry_With").equals("3")) {
                        contacttext.setText(getString(R.string.pancard));
                    }
                    if (visit.getString("Entry_With").equals("4")) {
                        contacttext.setText(getString(R.string.driving_license_id));
                    }
                    if (visit.getString("Entry_With").equals("5")) {
                        contacttext.setText(getString(R.string.documnet_id));
                    }

                    contact.setText(visit.getString("visitorMobile"));
                    edate.setText(visit.getString("Date"));
                    etime.setText(visit.getString("InTime"));
                    egate.setText(session.getGateNo());
                    purpose.setText(visit.getString("purpose_name"));

                    if (visit.getString("purpose_name").equals("Guest") || visit.getString("company").equals("Select Company")) {
                        companyL.setVisibility(View.GONE);
                    } else {
                        company.setText(visit.getString("company"));
                    }
                    if (visit.getString("vehicle").equals("")) {
                        vehicleL.setVisibility(View.GONE);
                    } else {
                        vehicleL.setVisibility(View.VISIBLE);
                        vehicle.setText(visit.getString("vehicle"));
                    }
                    if (visit.getString("visitor_note").equals("")) {
                        noteL.setVisibility(View.GONE);
                    } else {
                        noteL.setVisibility(View.VISIBLE);
                        note.setText(visit.getString("visitor_note"));
                    }
                    JSONArray unit_no = visit.getJSONArray("UnitNo");
                    for (int j = 0; j < unit_no.length(); j++) {
                        JSONArray test1 = unit_no.getJSONArray(j);
                        mylist.add(new VisitorDetailsUnit(
                                test1.getString(0) + "(" + test1.getString(7) + ")",
                                test1.getString(1),
                                test1.getString(2),
                                test1.getString(3),
                                test1.getString(4),
                                test1.getString(5),
                                test1.getString(6),
                                "",
                                ""

                        ));

                    }
                    CustomVisitorDetailsUnit adapter = new CustomVisitorDetailsUnit(getApplicationContext(), R.layout.visitor_unit, mylist);
                    list.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(list);


                }

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                Bundle visitdata = getIntent().getExtras();
                String visitorId = visitdata.getString("VisiID");
                System.out.println(visitorId);
                String url_login = getString(R.string.url) + "Visitor/fetchVisitorsDetails";
                String token = session.gettoken();
                //System.out.println("token" +token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("visitorId", visitorId));

                Log.e("ParamsWelcome", String.valueOf(params));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

    public static void justifyListViewHeightBasedOnChildren(ListView listView) {

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
