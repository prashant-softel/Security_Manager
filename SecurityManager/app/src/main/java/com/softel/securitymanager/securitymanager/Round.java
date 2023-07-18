package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round extends AppCompatActivity {
    Session session;
    ListView RoundList;
    static ArrayList<RoundList> mylist;
    BottomNavigationView navigationView;
    ProgressDialog dialog;
    TextView btnScanner,codemsg;
    ImageView imgQRScanner;
    private Button Submitbtn;
    int checkpostID =0;
    String scheduleID,roundID, code,counter="0", total_checkpost,sendCkpId,clickID,scancounter;
    LinearLayout btnLayout;
    String number;
    static boolean found = false;

    static LinearLayout mLayout;
    static LinearLayout layout;
    Map<String, String> map = new HashMap<String, String>();
    static ArrayList<String> arrlist = new ArrayList<String>();
    static ArrayList<String> arrlist1 = new ArrayList<String>();

    static AdapterView<?> PareantList;
    static int positionNo;
    static long id;
    //static ChieldID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_list);
        setTitle("Round List");

        session = new Session(getApplicationContext());
        mylist = new ArrayList<>();
        btnScanner = (Button)findViewById(R.id.btnScanner);
        imgQRScanner = findViewById(R.id.imgQRScanner);
        btnScanner = (Button)findViewById(R.id.btnScanner);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        RoundList = (ListView) findViewById(R.id.roundlist);
        layout= (LinearLayout)findViewById(R.id.qr_list);
        btnLayout = (LinearLayout) findViewById(R.id.btnlayout);
        scheduleID  = session.getScheduleID();
        roundID  = session.getRoundID();
        total_checkpost =session.getNoOfCheckpost();

       // codemsg = (TextView)findViewById(R.id.code) ;

        layout = (LinearLayout) findViewById(R.id.qr_list);
        RoundList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // TextView text = (TextView) findViewById(R.id.arraydata);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                PareantList =parent;
                positionNo=position;
              /* for(int i = 0; i < parent.getChildCount(); i++)
               {
                   if(i== position) {

                       mLayout = (LinearLayout) parent.getChildAt(i).findViewById(R.id.qr_list);
                       mLayout.setBackgroundResource(R.color.GreenText);
                       parent.getChildAt(i).setEnabled(false);

                   }
               }*/
               //System.out.println("Parants : "+number);
                RoundList roundlist = mylist.get(position);
                clickID=roundlist.getCPostID();


            Intent RoundList=new Intent(Round.this,RoundQRScanner.class);
            RoundList.putExtra("CheckpostID", roundlist.getCPostID());
            startActivity(RoundList);



                   //new Round.getQRCode().execute(); // call directorly scan funtion
            }

        });

        new Round.getCheckPostData().execute();
        Bundle QRData;
        String output="";
        QRData = getIntent().getExtras();
        code = QRData.getString("code");
        counter=QRData.getString("counter");
        sendCkpId=QRData.getString("SendCkpId");
        scancounter=QRData.getString("scancounter");

        if (counter != null && counter.equalsIgnoreCase("1")) {

            new Round.getQRCode().execute();
        }
        /*else
        {
            new Round.getCheckPostData().execute();
        }*/

        Submitbtn = (Button) findViewById(R.id.Submitbtn);
        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Round.SubmitData().execute();
            }
        });

    }

    private class SubmitData extends AsyncTask<String, String, String> {
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                String success = obj.getString("success");
                if (obj.getString("success").equals("1")) {
                    JSONObject objsucc = obj.getJSONObject("response");

                    Intent homepage=new Intent(getApplicationContext(),homepage.class);
                    startActivity(homepage);
                   // Intent Checkpost=new Intent(getApplicationContext(),CheckPostView.class);
                   // startActivity(Checkpost);
                }


            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {

                String url_submit = getString(R.string.url) + "CheckPost/AddSecurityRound";
                String token = session.gettoken();
                String username = session.getusername();
                String societyid = session.getSocietyid();
                ArrayList<Object> list = new ArrayList<>();

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("Data", arrlist.toString()));
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("societyid", societyid));
                params.add(new BasicNameValuePair("schedule_id", scheduleID));
                params.add(new BasicNameValuePair("round_id", roundID));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_submit, "GET", params);
                origresponseText = json.toString();

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

    private class getCheckPostData extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                System.out.println("SUccess");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray CheckpostList = jsondata.getJSONArray("CheckPostList");

                    int j=1;
                    for (int i = 0; i< CheckpostList.length(); i++) {
                        JSONObject objData = CheckpostList.getJSONObject(i);
                        mylist.add(new RoundList(
                                objData.getString("checkpost_name"),
                                objData.getString("qrcode"),
                                objData.getString("checkpost_id"),
                                j

                        ));


                    }
                    CustomeAdapterRound adapter=new CustomeAdapterRound(getApplicationContext(),R.layout.fetch_round_list,mylist);
                    RoundList.setAdapter(adapter);
                   // System.out.println("End Of Adapter");
                    //justifyListViewHeightBasedOnChildren(RoundList);
                    dialog.dismiss();


                }
                //Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();


            }
            catch(Exception e)
            {
                System.out.println("Exp"+e.getMessage());
            }

        }
        public String postData()
        {
            //System.out.println("Post Data ");
            String output = "";
            try
            {
                String stoken = session.gettoken();

                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("ScheduleID", scheduleID));
                params.add(new BasicNameValuePair("RoundID", roundID));
                String url_CheckpostDetails = getString(R.string.url) + "CheckPost/GetCheckPostDetails";
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_CheckpostDetails, "GET", params);
                output = json.toString();
            }
            catch (Exception e)
            { }
            return output;
        }
        @Override
        protected String doInBackground(String... params)
        {
            //String gateno=(String) params[0];
            String s=postData();
            return s;
        }

    }

    private class getQRCode extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray MatchResult = jsondata.getJSONArray("MatchResult");

                    //System.out.println("MatchResult"+MatchResult);
                    JSONObject objData = MatchResult.getJSONObject(0);
                    boolean found = false;
                    if (arrlist1.contains(objData.getString("qrcode"))){
                        // true
                        found = true;
                        Toast.makeText(getApplicationContext(),"QR Code Already Scaned. Please Scan Other QR Code ",Toast.LENGTH_SHORT).show();
                    }
                    map.put("Checkpost Name",objData.getString("checkpost_name"));
                    map.put("QRCode", objData.getString("qrcode"));
                    map.put("Status", "Matched");

                   if(found== false)
                   {
                       arrlist.add(map.toString());
                       arrlist1.add(objData.getString("qrcode"));
                       mLayout =  (LinearLayout)findViewById(R.id.qr_list);
                       //mLayout = PareantList.getChildAt(positionNo).findViewById(R.id.qr_list);
                       /*switch (positionNo) {
                           case 0:
                               mLayout.setBackgroundColor(Color.RED);
                               break;
                           case 1:
                               mLayout.setBackgroundColor(Color.GREEN);
                               break;
                           case 2:
                               mLayout.setBackgroundColor(Color.WHITE);
                               break;
                           case 3:
                               mLayout.setBackgroundColor(Color.parseColor("#FFA500"));
                               break;
                       }*/

                      /*for(int i = 0; i < PareantList.getChildCount(); i++)
                      {
                           System.out.println("Cheld Id ! => : "+PareantList.getChildAt(i));
                            if(i== positionNo)
                            {

                              mLayout = PareantList.getChildAt(positionNo).findViewById(R.id.qr_list);
                              mLayout.setBackgroundResource(R.color.GreenText);
                               PareantList.getChildAt(positionNo).setEnabled(false);
                          }

                      }*/

                   }
                   //else
                   //{
                       //Toast.makeText(getApplicationContext(),"QR Code Already Scaned. Please Scan Other QR Code ",Toast.LENGTH_SHORT).show();
                   //}
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"QR Code Not Matched. Please Try again ",Toast.LENGTH_SHORT).show();
                }
              }
            catch(Exception e)
            {
                System.out.println("Exp"+e.getMessage());
            }
            if(arrlist.size() == Integer.parseInt(total_checkpost))
            {
                btnLayout.setVisibility(View.VISIBLE);
            }
        }
        public String postData()
        {


            String output = "";
            try
            {
                // System.out.println("Inside Data ");
                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("QRCode", code));
                //params.add(new BasicNameValuePair("RoundID", roundID));
                params.add(new BasicNameValuePair("CheckpostId", sendCkpId));
                String url_QRScan = getString(R.string.url) + "CheckPost/QRCodeMatch";
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_QRScan, "GET", params);
                // System.out.println(json);
                output = json.toString();
                // System.out.println("output_json:  "+output);
            }
            catch (Exception e)
            { }
            return output;
        }
        @Override
        protected String doInBackground(String... params)
        {
            //String gateno=(String) params[0];
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
