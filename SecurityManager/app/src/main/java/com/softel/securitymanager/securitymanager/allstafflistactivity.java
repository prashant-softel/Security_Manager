package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import org.json.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.*;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
public class allstafflistactivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    JSONParser jParser = new JSONParser();
    SearchView searchView;
    ListView list;
    String stoken;
    String SocietyID;
    Session session;
    ProgressDialog dialog;
    ArrayList<StaffList> mylist;
    CustomListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_allstafflist);
        searchView = (SearchView) findViewById(R.id.search_view);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        list=(ListView) findViewById(R.id.listdata);
        session=new Session(getApplicationContext());
        mylist=new ArrayList<>();
       // System.out.println("Session dtaa " + session.getRole() + " "+session.getSocietyid() + " " +  session.gettoken());
        new Staff().execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               System.out.println(position);
               StaffList staff=adapter.getItem(position);
               //System.out.println("<"+staff.getStaffID() + "><"+ staff.getName() + "><" + staff.getJobProfile() + ">");
               Intent stafflatest=new Intent(allstafflistactivity.this,staff_latest.class);
               stafflatest.putExtra("StaffID",staff.getStaffID());
               stafflatest.putExtra("FirstName",staff.getName());
               stafflatest.putExtra("JobProfile",staff.getJobProfile());
               startActivity(stafflatest);
            }
        });
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

    private class Staff extends AsyncTask<String, String, String> {
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
                JSONArray  staffdata = jsondata.getJSONArray("AllStaff");
                System.out.println("Staff Array : " + staffdata);
                String i_url = getString(R.string.imageurl);
                for(int i=0;i<staffdata.length();i++)
                {
                    JSONObject jsonstaff=staffdata.getJSONObject(i);
                    mylist.add(new StaffList(
                            jsonstaff.getString("service_prd_reg_id"),
                            jsonstaff.getString("full_name"),
                            jsonstaff.getString("cat"),
                            i_url + jsonstaff.getString("photo_thumb"),
                            jsonstaff.getString("cur_con_1")
                            ));
                }

              /*  System.out.println("Staff Array : " + staffdata);
                for(int i=0;i < staffdata.length();i++)
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject e = staffdata.getJSONObject(i);
                    map.put("Name", "Name : " +e.getString("full_name"));
                    map.put("JobProfile","Job Profile : " + e.getString("cat"));
                    mylist.add(map);
                    ImageView image = (ImageView) findViewById(R.id.image1);
                    String image_url = "https://way2society.com/"+e.getString("photo_thumb");
                    System.out.println(image_url);

                }*/
                //System.out.println("List" +mylist);

                //final SimpleAdapter adapter=new SimpleAdapter(Welcome.this,mylist,R.layout.view_staff,new String[] {"Name","JobProfile"},new int[]{R.id.text1,R.id.text2});
               // list.setAdapter(adapter);
            }
            catch (Exception e)
            {
            }
           adapter=new CustomListAdapter(getApplicationContext(),R.layout.view_staff,mylist);
            list.setAdapter(adapter);
            setupSearchView();
            dialog.dismiss();
        }

        public String postData() {
            String output = "";
            try {
                stoken = session.gettoken();
                SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
               String url_login = getString(R.string.url) + "Staff/AllStaffList";
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
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
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("Search Here");
    }
}
