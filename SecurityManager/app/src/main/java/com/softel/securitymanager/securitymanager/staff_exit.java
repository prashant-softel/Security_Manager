package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class staff_exit extends AppCompatActivity {
    Session session;
    TextView totalstaff,staffin,staffout;
    ListView stafflist;
    ArrayList<stafffetch> mylist;
    BottomNavigationView navigationView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_exit);
        setTitle("Staff Entry List");
        session=new Session(getApplicationContext());
        mylist=new ArrayList<>();
        totalstaff=(TextView) findViewById(R.id.totalstaff);
        staffin=(TextView) findViewById(R.id.totalin);
        staffout=(TextView) findViewById(R.id.totalout);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        totalstaff.setClickable(true);
        totalstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent totalstaff=new Intent(staff_exit.this,total_staffexit.class);
                startActivity(totalstaff);
            }
        });
       navigationView=(BottomNavigationView) findViewById(R.id.navigation);
       navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
           @Override
           public void onNavigationItemReselected( MenuItem menuItem) {

               switch (menuItem.getItemId()) {
                   case R.id.staffadd:
                       Intent staffadd=new Intent(staff_exit.this,staff_add.class);
                       startActivity(staffadd);
                       break;
                   case R.id.totalstaff:
                       Intent stafftotal=new Intent(staff_exit.this,total_staffexit.class);
                       startActivity(stafftotal);
                       break;
                   case R.id.staffreport:
                       Intent staffreportintent=new Intent(staff_exit.this,allstafflistactivity.class);
                       startActivity(staffreportintent);
                       break;
               }

           }
       });

        stafflist=(ListView) findViewById(R.id.stafffetch);
        stafflist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                stafffetch staff=mylist.get(position);

                //System.out.println("<"+staff.getStaffID() + "><"+ staff.getName() + "><" + staff.getJobProfile() + ">");
                Intent staffview=new Intent(staff_exit.this,staffview.class);
                staffview.putExtra("StaffID",staff.getStaff_id());
                staffview.putExtra("InTime",staff.getInTime());
                staffview.putExtra("GateNo",staff.getEntry_Gate());
                startActivity(staffview);
            }
        });

        new stafffetchasy().execute();

    }


    private class stafffetchasy extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                JSONObject jsondata = obj.getJSONObject("response");
                String staffcount = jsondata.getString("TotalStaffCount");
                System.out.println("jsondata:  "+jsondata);
                System.out.println("staffcount: "+staffcount);

                //tv.setText(result);
                JSONArray staffdata = jsondata.getJSONArray("getStaffDetails");
                System.out.println("allStaffDetails: "+staffdata);
                int currentin=staffdata.length();
                int currentout=Integer.valueOf(staffcount)-currentin;
                String curin,curout;
                totalstaff.setText(staffcount);
                if(currentin<=9)
                {
                    curin="0"+currentin;
                }
                else
                {
                    curin=String.valueOf(currentin);
                }
                if(currentout<=9)
                {
                    curout="0"+currentout;
                }
                else
                {
                    curout=String.valueOf(currentout);
                }
               staffin.setText(String.valueOf(curin));
                staffout.setText(String.valueOf(curout));
                //System.out.println("Staff Array : " + staffdata);
                for (int i = 0; i < staffdata.length(); i++) {
                    JSONObject jsonstaff = staffdata.getJSONObject(i);

                  //  System.out.println("Staff Data : " + jsonstaff);
                    mylist.add(new stafffetch(
                            jsonstaff.getString("staff_id"),
                            jsonstaff.getString("Full_Name"),
                            jsonstaff.getString("entry_profile"),
                            jsonstaff.getString("Date"),
                            jsonstaff.getString("InTime"),
                            jsonstaff.getString("Entry_Gate")
                    ));
                }
            }
            catch (Exception e)
            {
            }
            CustomStaffFetchAdapter adapter=new CustomStaffFetchAdapter(getApplicationContext(),R.layout.staff_entryfetch,mylist);
            stafflist.setAdapter(adapter);

            justifyListViewHeightBasedOnChildren(stafflist);
            dialog.dismiss();
        }

        public String postData() {
            String output = "";
            try {
                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                String url_login = getString(R.string.url) + "Staff/fetch";
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                output = json.toString();
                System.out.println("output_json:  "+output);
                 }
                 catch (Exception e)
                 { }
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
