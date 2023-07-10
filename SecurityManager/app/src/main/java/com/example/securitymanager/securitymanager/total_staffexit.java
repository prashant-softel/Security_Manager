package com.example.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class total_staffexit extends AppCompatActivity implements SearchView.OnQueryTextListener {
    CustomAdapterStaffTotalExit adapter;
Session session;
TextView gateno,changegate;
FloatingActionButton floatingBtnAddStaff;
ArrayList<StaffTotalExit> mylist;
ProgressDialog dialog;
ListView list;
SearchView searchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_staffexit);
        setTitle(R.string.view_all_staff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session=new Session(getApplicationContext());
        searchview = (SearchView) findViewById(R.id.search_view);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        mylist=new ArrayList<>();
        list=(ListView) findViewById(R.id.totalstaffdata);
       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            StaffTotalExit staff=adapter.getItem(position);
                System.out.println("Staff Name : " + staff.getFull_name());
            Intent staffdetail=new Intent(total_staffexit.this,stafftotaldetail.class);
            staffdetail.putExtra("staffID",staff.getService_prd_reg_id());
                staffdetail.putExtra("socstaffID",staff.getSocService_prd_reg_id());

                System.out.println("stafff " + staff.getUnit());
                System.out.println("stafff1 " + staff.getUnit());
                staffdetail.putExtra("image",staff.getPhoto_thumb());
                staffdetail.putExtra("Unit", staff.getUnit());
                staffdetail.putExtra("cat",staff.getCat());
                staffdetail.putExtra("rating",staff.getRating());
                startActivity(staffdetail);
            }
        });
        gateno=(TextView) findViewById(R.id.GateNo);
        gateno.append(session.getGateNo());
        changegate=(TextView) findViewById(R.id.changegate);
        floatingBtnAddStaff= findViewById(R.id.floatingBtnAddStaff);
        changegate.setClickable(true);
        changegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent setting=new Intent(total_staffexit.this,Setting.class);
                startActivity(setting);
            }
        });
        floatingBtnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent setting=new Intent(total_staffexit.this,staff_add.class);
                startActivity(setting);
            }
        });
        new totalstaff().execute();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText.toString())) {
            list.clearTextFilter();
        } else {
            System.out.println("Test 1 : " +newText);
            list.setFilterText(newText.toString());
            adapter.getFilter().filter(newText);
        }
        return true;
    }

    private class totalstaff extends AsyncTask<String, String, String> {
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
                JSONObject jsondata=obj.getJSONObject("response");
                //tv.setText(result);
                JSONArray staffdata = jsondata.getJSONArray("AllStaff");
                System.out.println("Staff Array : " + staffdata);
                String rating="";
                for(int i=0;i<staffdata.length();i++)
                {

                   JSONObject jsonsatff=staffdata.getJSONObject(i);
                   if(jsonsatff.has("Rating"))
                   {
                       rating=jsonsatff.getString("Rating");
                   }
                   else
                   {
                     rating="";
                   }
                   String unitstaff="";
                JSONArray jsonstaffunit=jsonsatff.getJSONArray("UnitDetails");
                System.out.println(jsonstaffunit);
                String i_url = getString(R.string.imageurl);
                   for(int k=0;k<jsonstaffunit.length();k++)
                   {
                       JSONObject jsonunit=jsonstaffunit.getJSONObject(k);
                       unitstaff += jsonunit.getString("unit_no") + " %";
                   }
                   mylist.add(new StaffTotalExit(
                      jsonsatff.getString("security_status"),
                           jsonsatff.getString("full_name"),
                           jsonsatff.getString("IsBlock"),
                           i_url + jsonsatff.getString("photo"),
                           jsonsatff.getString("cur_con_1"),
                           jsonsatff.getString("service_prd_reg_id"),
                           jsonsatff.getString("society_staff_id"),
                           jsonsatff.getString("since"),
                           rating,
                           unitstaff,
                           jsonsatff.getString("cat")
                   ));
                   Log.e("kkkkIMAGE",i_url + jsonsatff.getString("photo"));
                }

            }
            catch (Exception e)
            {
            }

            adapter=new CustomAdapterStaffTotalExit(getApplicationContext(),R.layout.total_staff,mylist);
            list.setAdapter(adapter);
            //list.setTextFilterEnabled(true);
            setupSearchView();
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
                String url_login = getString(R.string.url) + "Staff/AllStaffList";
                JSONParser jParser=new JSONParser();
                JSONObject  json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                output = json.toString();


            } catch (Exception e) {

            }
            return output;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = postData();
            return s;
        }

    }
    private void setupSearchView()
    {
        searchview.setIconifiedByDefault(false);
        searchview.setOnQueryTextListener(this);
        searchview.setSubmitButtonEnabled(true);
        //searchview.setQueryHint("Search Here");
    }
}
