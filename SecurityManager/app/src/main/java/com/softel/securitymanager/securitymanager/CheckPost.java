package com.softel.securitymanager.securitymanager;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class CheckPost  extends AppCompatActivity
{

    Session session;
    ListView checkpostlist;
    ArrayList<fetchcheckpost> mylist;
    BottomNavigationView navigationView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkpost_list);
        setTitle("Check Post List 123");
        session = new Session(getApplicationContext());
        mylist = new ArrayList<>();

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);


        checkpostlist = (ListView) findViewById(R.id.postlist);
        checkpostlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchcheckpost checklist=mylist.get(position);
                //System.out.println("ID :"+checklist.getCheckPost_id());
               // System.out.println("Position "+id);
                //System.out.println("<"+checklist.getStaffID() + "><"+ staff.getName() + "><" + staff.getJobProfile() + ">");
                Intent RoundList=new Intent(CheckPost.this,Round.class);
                RoundList.putExtra("CheckPostID",checklist.getCheckPost_id());
                RoundList.putExtra("RoundType",checklist.getRoundType());
                RoundList.putExtra("ScheduleTime",checklist.getScheduleTime());
               // RoundList.putExtra("TimeID", checklist.getTime_id());
                startActivity(RoundList);
            }
        });

        new CheckPost.getCheckPost().execute();
    }

    private class getCheckPost extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
           // Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
               // System.out.println("Result : " +obj);
                String success=obj.getString("success");
               // System.out.println("SUccess");
                if (success.contains("1")) {
                   // System.out.println("Inside SUccess");
                    JSONObject jsondata = obj.getJSONObject("response");
                   // System.out.println("checkPOstDate "+jsondata);
                    JSONArray checkPostData = jsondata.getJSONArray("CheckpostList");


                   // System.out.println("checkPOstDate1 "+checkPostData);
                    int j=1;
                    for (int i = 0; i< checkPostData.length(); i++) {
                        JSONObject objData = checkPostData.getJSONObject(i);

                          mylist.add(new fetchcheckpost(
                                  objData.getString("desc"),
                                  objData.getString("schedule_name"),
                                  objData.getString("rtime"),
                                  objData.getString("login_id"),
                                  objData.getString("id"),
                                  objData.getString("no_of_checkpost"),
                                  //objData.getString("timeid"),
                                j

                       ));


                    }
                    CustomCheckPostAdapter adapter=new CustomCheckPostAdapter(getApplicationContext(),R.layout.checkpost_fetch,mylist);
                    checkpostlist.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(checkpostlist);
                    dialog.dismiss();
                    // System.out.println("News Data 1");

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
        String output = "";
        try
        {
                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
               // params.add(new BasicNameValuePair("SocietyID", SocietyID));
                String url_login = getString(R.string.url) + "CheckPost/GetCheckpost";
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                output = json.toString();
                //System.out.println("output_json:  "+output);
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
