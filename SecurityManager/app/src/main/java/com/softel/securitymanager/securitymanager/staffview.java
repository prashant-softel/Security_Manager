package com.softel.securitymanager.securitymanager;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import com.squareup.picasso.Callback;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

public class staffview extends AppCompatActivity {

    ImageView imagestaff;
    Session session;
    String staffid;
    Bundle getstaff;
    ListView list,staffitemdesc;
    View view1;
    ArrayList<stafflatestlist> mylist;
    ArrayList<staffitemlist> staffdesc;
    LinearLayout Lstaffnote;
    TextView tname,tstaffid,tdob,tjobprof,tcontact,tintime,tegateno,gateNo,changegate,txt_staffnote;
    String URL = "";
    TextView headdate,headinitme,headoutime,headstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session=new Session(getApplicationContext());
        setTitle(R.string.staff_view);
        mylist=new ArrayList<>();
        staffdesc = new ArrayList<>();
        tname=(TextView) findViewById(R.id.name);
        view1 = (View) findViewById(R.id.view1);
        tstaffid=(TextView) findViewById(R.id.staffid);
        staffitemdesc = (ListView) findViewById(R.id.staffitemdesc);
        changegate=(TextView) findViewById(R.id.changegate);
        changegate.setClickable(true);
        changegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting=new Intent(staffview.this,Setting.class);
                startActivity(setting);
            }
        });
        tdob=(TextView) findViewById(R.id.dob);
        tjobprof=(TextView) findViewById(R.id.jobprofile);
        tcontact=(TextView) findViewById(R.id.contactno);
        tintime=(TextView) findViewById(R.id.intime);
        tegateno=(TextView) findViewById(R.id.entrygateno);
        imagestaff=(ImageView) findViewById(R.id.image1);
        gateNo=(TextView) findViewById(R.id.GateNo);
        gateNo.append(session.getGateNo());
        Lstaffnote = (LinearLayout) findViewById(R.id.Lnote);
        txt_staffnote = (TextView) findViewById(R.id.note);
        headdate=(TextView) findViewById(R.id.headdate);
        headinitme=(TextView) findViewById(R.id.headintime);
        headoutime=(TextView) findViewById(R.id.headouttime);
        headstatus=(TextView) findViewById(R.id.headstatus);
        headdate.setVisibility(View.INVISIBLE);
        headinitme.setVisibility(View.INVISIBLE);
        headoutime.setVisibility(View.INVISIBLE);
        headstatus.setVisibility(View.INVISIBLE);
        imagestaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL, null);
            }
        });

        list=(ListView) findViewById(R.id.viewentrystaff);
        list.setVisibility(View.INVISIBLE);

        new StaffView().execute();
    }

    public void checkout(View view) {

        new checkout().execute(staffid);
    }

    public void viewlatest(View view) {

        headdate.setVisibility(View.VISIBLE);
        headinitme.setVisibility(View.VISIBLE);
        headoutime.setVisibility(View.VISIBLE);
        headstatus.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        new stafflatest().execute();
    }



    private class stafflatest extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");

            try {

                Log.e("viewLatestResp",result);
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
                    list.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(list);
                }

            }
            catch (Exception e)
            {
            }
        }

        public String postData() {
            String output="";
            String token=session.gettoken();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", token));
            params.add(new BasicNameValuePair("StaffID", staffid));
            Log.e("viewLatestParams", String.valueOf(params));
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

    private class checkout extends AsyncTask<String, String, String> {
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);

                if(obj.getString("success").equals("1"))
                {
                    Toast.makeText(getApplicationContext(),"Checked out Successfully",Toast.LENGTH_SHORT).show();
                    Intent stafffexit=new Intent(getApplicationContext(),staff_exit.class);
                    startActivity(stafffexit);
                }
            }
            catch (Exception e)
            {
            }
        }

        public String postData(String staffid) {
            String output = "";
            try {

                String stoken = session.gettoken();
                String gateno=session.getGateNo();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("ExitGate", gateno));
                params.add(new BasicNameValuePair("ProviderID", staffid));
                String url_login = getString(R.string.url)  + "Staff/StaffExit";
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                System.out.println(json);
                output = json.toString();
            }
            catch (Exception e)
            { }
            return output;
        }

        @Override
        protected String doInBackground(String... params) {
            String staffid=(String) params[0];
            String s = postData(staffid);
            return s;
        }

    }

    private class StaffView extends AsyncTask<String, String, String> {
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                JSONObject jsondata = obj.getJSONObject("response");
                JSONObject jsonIntime = jsondata.getJSONObject("InTime");

                System.out.println("Test : " + jsonIntime.length());

                JSONObject jsonin=jsonIntime.getJSONObject("0");
                for(int i = 0;i< jsonIntime.length(); i++)
                {
                    String unitno = "";

                   JSONObject jsonobj = jsonIntime.getJSONObject(String.valueOf(i));
                   if(jsonobj.has("item_desc"))
                   {

                       System.out.println(getString(R.string.imageurl) + jsonobj.getString("item_image"));
                       System.out.println(jsonobj.getString("item_desc"));
                       System.out.println(jsonobj.getString("member_name"));
                        if(jsonobj.getString("unit_no").equals("0"))
                        {
                           unitno = "S-Office";
                        }
                        else
                        {
                            unitno = jsonobj.getString("unit_no");
                        }
                       System.out.println("Unit No :" +unitno);
                       staffdesc.add(new staffitemlist(
                               getString(R.string.imageurl)+jsonobj.getString("item_image"),
                               unitno,
                               jsonobj.getString("member_name"),
                               jsonobj.getString("item_desc")
                            ));


                   }
                }
                System.out.println("StaffLength : " + staffdesc.size());
                if(staffdesc.size() > 0) {
                    staffitemdesc.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    CustomStaffItemList adapter = new CustomStaffItemList(getApplicationContext(), R.layout.staffdesclist, staffdesc);
                    staffitemdesc.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(staffitemdesc);
                }
                else
                {
                    staffitemdesc.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                }
                JSONArray jsonarr=jsondata.getJSONArray("visitors");

                final JSONObject staffdata=jsonarr.getJSONObject(0);
                tname.setText(staffdata.getString("full_name"));
                if(jsonin.getString("staff_note").equals(""))
                {
                    Lstaffnote.setVisibility(View.GONE);
                }
                else
                {
                    txt_staffnote.setText(jsonin.getString("staff_note"));
                }
                tstaffid.setText(staffdata.getString("society_staff_id"));
                tjobprof.setText(staffdata.getString("cat"));
                String contactno = "<u>"+staffdata.getString("cur_con_1")+"</u>";
                tcontact.setClickable(true);
                tcontact.setText(Html.fromHtml(contactno));
                tcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        try {
                            callIntent.setData(Uri.parse("tel:" + staffdata.getString("cur_con_1")));
                            startActivity(callIntent);
                        }
                        catch (Exception e)
                        {}
                    }
                });


                tdob.setText(jsonin.getString("Date"));

                tintime.setText(getstaff.getString("InTime"));
                tegateno.setText(getstaff.getString("GateNo"));
               // Picasso.get().load("https://way2society.com/" + staffdata.getString("photo_thumb")).into(imagestaff);

                    URL = getString(R.string.imageurl) + staffdata.getString("photo");

                Picasso.get().load(getString(R.string.imageurl) + staffdata.getString("photo_thumb")).into(imagestaff, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        imagestaff.setImageResource(R.drawable.noimage);
                    }
                });
            }
            catch (Exception e)
            {
            }
            }

        public String postData() {
            String output = "";
            try {

                getstaff=getIntent().getExtras();
                staffid=getstaff.getString("StaffID");
                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("staffId", staffid));
                String url_login = getString(R.string.url) + "Staff/fetchStaffCheckIn";
                JSONParser jParser=new JSONParser();
                JSONObject  json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                output = json.toString();
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
