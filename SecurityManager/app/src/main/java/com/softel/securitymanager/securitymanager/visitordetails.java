package com.softel.securitymanager.securitymanager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class visitordetails extends AppCompatActivity {

    String VisitorVisitID;
    Session session;
    String visitorID;
    ImageView imagev;
    TextView vname,txtcon,vcontact,vpurpose,vcompanyname,vindate,vintime,vgate,vouttime,gateno,changegate,outdate,vehicle,exitgate;
    LinearLayout lodate,lvehicle,lcompany,lexitgate;
    Button btnsilent,btncheck;
    private ListView list;
    String URL;
    ArrayList<VisitorDetailsUnit> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitordetails);
        setTitle("Visitor Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session=new Session(getApplicationContext());
        imagev=(ImageView) findViewById(R.id.image1);
        imagev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL, null);
            }
        });
        gateno=(TextView) findViewById(R.id.GateNo);
        gateno.append(session.getGateNo());
        btnsilent=(Button) findViewById(R.id.btnsilentout);
        btncheck=(Button) findViewById(R.id.btncheckout);
        changegate=(TextView) findViewById(R.id.changegate);
        changegate.setClickable(true);
        changegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting=new Intent(visitordetails.this,Setting.class);
                startActivity(setting);
            }
        });
        vname=(TextView) findViewById(R.id.name);
        txtcon = (TextView) findViewById(R.id.txtcon);
        vcontact=(TextView) findViewById(R.id.contact);
        vpurpose=(TextView) findViewById(R.id.purpose);
        vcompanyname=(TextView) findViewById(R.id.company);
        vindate=(TextView) findViewById(R.id.indate);
        vintime=(TextView) findViewById(R.id.intime);
        vgate=(TextView) findViewById(R.id.entrygateno);
        vouttime=(TextView) findViewById(R.id.outtime);
        outdate=(TextView) findViewById(R.id.outdate);
        vehicle=(TextView) findViewById(R.id.vehicle);
        exitgate=(TextView) findViewById(R.id.exitgateno);
        lvehicle=(LinearLayout) findViewById(R.id.Lvehicle);
        lodate=(LinearLayout) findViewById(R.id.Loutdate);
        lcompany=(LinearLayout) findViewById(R.id.Lcompany);
        lexitgate=(LinearLayout) findViewById(R.id.Lexitgate);
        list=(ListView) findViewById(R.id.visitordetail);
        mylist = new ArrayList<>();
        new fetchvisitordetails().execute();
    }

    public void silentout(View view)
    {
       new vexit().execute(visitorID,session.getGateNo(),"1");
    }

    public void checkout(View view)
    {
        new vexit().execute(visitorID,session.getGateNo(),"0");
    }

    public void viewlatest(View view) {

        Intent visitreport=new Intent(visitordetails.this,visitor_latest.class);
        visitreport.putExtra("VisitorVisitID",VisitorVisitID);
        visitreport.putExtra("visitname",vname.getText().toString());
        startActivity(visitreport);

    }

    private class vexit extends AsyncTask<String, String, String> {

        String sameday="0";
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                if(obj.getString("success").equals("1"))
                {
                    Toast.makeText(getApplicationContext(),"Visitor Checked Out",Toast.LENGTH_SHORT).show();
                    Intent vexit=new Intent(getApplicationContext(),VisitorExit.class);
                    startActivity(vexit);
                    finish();
                }

                }
            catch (Exception e)
            {
            }




        }

        public String postData(String v_id,String gate,String unknown) {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) +"Visitor/ExitVisitors";
                String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("VisitorID",v_id));
                params.add(new BasicNameValuePair("ExitGate",gate));
                params.add(new BasicNameValuePair("UnkownOut",unknown));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String v_id=(String)params[0];
            String gate=(String) params[1];
            String uknown=(String) params[2];
            String s=postData(v_id,gate,uknown);
            return s;
        }

    }

    private class fetchvisitordetails extends AsyncTask<String, String, String> {

        String sameday="0";
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                Log.e("visitorDetailsResp",result);
                System.out.println("Result : " +obj);
                JSONObject jsondata=obj.getJSONObject("response");
                JSONArray VisitorData = jsondata.getJSONArray("visitors");
                JSONArray visitoritem=null;
                Object aObj = jsondata.get("visitoritems");
                if(aObj instanceof String)
                {
                    System.out.println(visitoritem);
                    System.out.println("Visitor Item string : " + visitoritem);

                }
                if(aObj instanceof JSONArray)
                {
                    visitoritem = (JSONArray) aObj;
                    System.out.println(visitoritem);
                    System.out.println("Visitor Item array : " + visitoritem);

                }


                int k=1;

                String store="";
                final JSONObject jsonvisitor=VisitorData.getJSONObject(0);
                visitorID=jsonvisitor.getString("id");
                VisitorVisitID=jsonvisitor.getString("visitor_ID");
                System.out.println("Visiotor : " +jsonvisitor);
                JSONObject visitordetails=jsonvisitor.getJSONObject("VisitorDetails");
                final JSONObject vobjdetail=visitordetails.getJSONObject("0");
               // System.out.println(  vobjdetail.getString("FullName"));

                vname.setText(vobjdetail.getString("FullName"));
                vcontact.setText(jsonvisitor.getString("visitorMobile"));

                if(jsonvisitor.getString("Entry_With").equals("1")) {
                    vcontact.setClickable(true);
                    vcontact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            try {
                                callIntent.setData(Uri.parse("tel:" + jsonvisitor.getString("visitorMobile")));
                                startActivity(callIntent);
                            } catch (Exception e) {
                            }
                        }
                    });
                }
                else {
                    final String URL1 =  getString(R.string.imageurl) + "SecuirityApp/VisitorDoc/" + vobjdetail.getString("Doc_img");

                    vcontact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL1, null);
                        }
                    });
                    if (jsonvisitor.getString("Entry_With").equals("2")) {
                        txtcon.setText(getString(R.string.aadhar));
                    }
                    if (jsonvisitor.getString("Entry_With").equals("3")) {
                        txtcon.setText(getString(R.string.pancard));
                    }
                    if (jsonvisitor.getString("Entry_With").equals("4")) {
                        txtcon.setText(getString(R.string.dl));
                    }
                    if (jsonvisitor.getString("Entry_With").equals("5")) {
                        txtcon.setText(getString(R.string.other));
                    }
                }
                    vpurpose.setText(jsonvisitor.getString("purpose_name"));
                    if(jsonvisitor.getString("company").equals(""))
                        {
                        lcompany.setVisibility(View.GONE);
                       // lvehicle.setPadding(0,140,0,0);

                        }
                    else
                        {
                            vcompanyname.setText(jsonvisitor.getString("company"));
                        }
                if(jsonvisitor.getString("Exit_Gate").equals("0"))
                {
                    lexitgate.setVisibility(View.GONE);

                }
                else
                {
                    exitgate.setText(jsonvisitor.getString("Exit_Gate"));
                }
                if(jsonvisitor.getString("vehicle").equals(""))
                {
                   lvehicle.setVisibility(View.GONE);

                   // vehicle.setVisibility(View.GONE);

                }
                else
                {
                    vehicle.setText(jsonvisitor.getString("vehicle"));
                }

                if((jsonvisitor.getString("Date").equals(jsonvisitor.getString("OutDate"))) || (jsonvisitor.getString("OutDate").equals("00")))
                    {
                        sameday="1";
                    }

                else
                    {
                        sameday="0";
                    }
                    vindate.setText(jsonvisitor.getString("Date"));
                    if(!sameday.equals("0"))
                    {
                        lodate.setVisibility(View.GONE);
                    }
                    else
                    {
                        outdate.setText(jsonvisitor.getString("OutDate"));
                    }

                    vintime.setText(jsonvisitor.getString("InTime"));
                    vgate.setText(jsonvisitor.getString("Entry_Gate"));


                    if(jsonvisitor.getString("OutTime").equals("0"))
                    {
                        vouttime.setText(getString(R.string.still_inside));
                    }
                    else {
                        vouttime.setText(jsonvisitor.getString("OutTime"));
                    }
                    URL = getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + vobjdetail.getString("img");
                Picasso.get().load(getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + vobjdetail.getString("img")).into(imagev, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        imagev.setImageResource(R.drawable.noimage);

                    }
                });

                JSONArray unit_no=jsonvisitor.getJSONArray("UnitNo");
                System.out.println("Check : " + unit_no);

                    for(int j=0;j<unit_no.length();j++)
                    {
                        String note="",visitor_item_image="";
                        JSONArray test1=unit_no.getJSONArray(j);
                        if(visitoritem != null)
                        {
                            for (int m = 0; m < visitoritem.length(); m++)
                            {
                                JSONObject visitorI = visitoritem.getJSONObject(m);
                                String unit = test1.getString(0) + "(" + test1.getString(7) + ")";
                                if (unit.equals(visitorI.getString("unit_no")))
                                {
                                    note = visitorI.getString("item_desc");
                                    visitor_item_image = getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + visitorI.getString("item_image");
                                    System.out.println("Testing");
                                    break;
                                }
                            }
                        }

                        mylist.add(new VisitorDetailsUnit(
                                test1.getString(0) + "(" + test1.getString(7) +")",
                                test1.getString(1),
                                test1.getString(2),
                                test1.getString(3),
                                test1.getString(4),
                                test1.getString(5),
                                test1.getString(6),
                                note,
                                visitor_item_image
                                ));

                 }

                CustomVisitorDetailsUnit adapter=new CustomVisitorDetailsUnit(getApplicationContext(),R.layout.visitor_unit,mylist);
                list.setAdapter(adapter);
                justifyListViewHeightBasedOnChildren(list);



            }
            catch (Exception e)
            {
            }




        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "Visitor/fetchVisitorsDetails";
                String token=session.gettoken();
                Bundle visitor=getIntent().getExtras();
                String visitorId=visitor.getString("visitorId");
                System.out.println(visitorId);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("visitorId",visitorId));
                Log.e("paramsparams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText=json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            }
            catch (Exception e)
            {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params)
        {
            String s=postData();
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
