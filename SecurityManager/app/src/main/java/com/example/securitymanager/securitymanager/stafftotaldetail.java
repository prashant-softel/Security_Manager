package com.example.securitymanager.securitymanager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class stafftotaldetail extends AppCompatActivity {
    Session session;

    String staffid,soc_staffid;
    Bundle getstaff;
    LinearLayout Lrating;
    String contactno,cat,name;
    TextView sname,scontact,sdob,scat,swsince,smarried,sunit,sid,srating;
    String URL;
    ImageView image1;
    Button viewlatest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafftotaldetail);
        session=new Session(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.staff_details)+" ("+getString(R.string.gateno) +session.getGateNo() + " )" );
        sname=(TextView) findViewById(R.id.name);
        scontact=(TextView) findViewById(R.id.contact);
        viewlatest=(Button) findViewById(R.id.btnstafflatest);
        sid=(TextView) findViewById(R.id.Dstaffid);
        srating=(TextView) findViewById(R.id.rating);
        viewlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffhistory=new Intent(stafftotaldetail.this,staff_latest.class);
                staffhistory.putExtra("StaffID",staffid);
                staffhistory.putExtra("FirstName",name);
                staffhistory.putExtra("JobProfile",cat);
                startActivity(staffhistory);
            }
        });
        scontact.setClickable(true);
        scontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + contactno));
                    startActivity(callIntent);
            }
        });
        sdob=(TextView) findViewById(R.id.dob);
        scat=(TextView) findViewById(R.id.category);
        swsince=(TextView) findViewById(R.id.since);
        smarried=(TextView) findViewById(R.id.mstatus);
        sunit=(TextView) findViewById(R.id.units);
        image1=(ImageView) findViewById(R.id.image1);
        Lrating=(LinearLayout) findViewById(R.id.Lrating);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL, null);
            }
        });
        new StaffView().execute();
    }

    private class StaffView extends AsyncTask<String, String, String> {
        String unit,image,bundle_rating;
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                JSONObject response=obj.getJSONObject("response");
                JSONArray josnstaff=response.getJSONArray("StaffProvider");
                JSONObject staffinfo=josnstaff.getJSONObject(0);
                //sname.setText(staffinfo.getString("full_name"));
                if(staffinfo.getString("full_name").length()<=20) {
                    sname.setText(staffinfo.getString("full_name"));
                }
                else {
                    String name = staffinfo.getString("full_name").substring(0, 20);
                    sname.setText(name);
                }
                name=staffinfo.getString("full_name");
                scontact.setText(staffinfo.getString("cur_con_1"));
                contactno=staffinfo.getString("cur_con_1");
                sdob.setText(staffinfo.getString("dob"));
                scat.setText(cat);
                swsince.setText(staffinfo.getString("since"));
                smarried.setText("Yes");
                unit=unit.replaceAll("%","\n");
                sunit.setText(unit);
                sid.setText(soc_staffid);
                if(bundle_rating.equals(""))
                {
                    Lrating.setVisibility(View.GONE);
                }
                else
                {
                    double rating = Double.valueOf(bundle_rating);

                    if (rating == 1 || rating > 1 && rating < 2) {

                        srating.setText(String.valueOf(rating));
                        srating.append(getString(R.string.poor));
                        srating.setTextColor(Color.argb(255, 255, 97, 97));

                    }
                    if (rating == 2 || rating > 2 && rating < 3) {
                        srating.setText(String.valueOf(rating));
                        srating.append(getString(R.string.average));
                        srating.setTextColor(Color.argb(255, 255, 159, 0));

                    }
                    if (rating == 3 || (rating > 3 && rating < 4)) {

                        srating.setText(rating + "");
                        srating.append(getString(R.string.good));
                        srating.setTextColor(Color.argb(255, 56, 142, 60));

                    }
                    if (rating == 4 || rating > 4 && rating < 5) {
                        srating.setText(String.valueOf(rating));
                        srating.append(getString(R.string.very_good));
                        srating.setTextColor(Color.argb(255, 56, 142, 60));

                    }
                    if (rating == 5) {
                        srating.setText(String.valueOf(rating));
                        srating.append(getString(R.string.excellent));
                        srating.setTextColor(Color.argb(255, 56, 142, 60));

                    }
                }
                Picasso.get().load(image).into(image1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        image1.setImageResource(R.drawable.noimage);
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
                staffid=getstaff.getString("staffID");
                soc_staffid = getstaff.getString("socstaffID");
                cat=getstaff.getString("cat");
                unit= getstaff.getString("Unit");
                image=getstaff.getString("image");
                URL = image;
                bundle_rating=getstaff.getString("rating");
                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("ProviderID", staffid));
                Log.e("paraaaaaaa", String.valueOf(params));
                String url_login = getString(R.string.url) + "Staff/StaffProvider";
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
            String s = postData();
            return s;
        }

    }

}
