package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
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

public class schedule_list extends AppCompatActivity
{
    Handler handler;
    Session session;
    ListView scheduleList;
    ArrayList<fetchschedule> mylist;
    BottomNavigationView navigationView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_list);
        setTitle("Schedule List");
        session = new Session(getApplicationContext());
        mylist = new ArrayList<>();
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

       scheduleList = (ListView) findViewById(R.id.schedulefetch);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchschedule checklist=mylist.get(position);

                //Intent RoundList=new Intent(CheckPost.this,Round.class);
               // RoundList.putExtra("CheckPostID",checklist.getCheckPost_id());
              // RoundList.putExtra("RoundType",checklist.getRoundType());
               // RoundList.putExtra("ScheduleTime",checklist.getScheduleTime());
                //startActivity(RoundList);
            }
        });

        new schedule_list.getSchedule().execute();
    }
    private class getSchedule extends AsyncTask<String, String, String> {

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

                    JSONArray scheduleData = jsondata.getJSONArray("ScheuleList");

                    //System.out.println("scheduleData "+scheduleData);

                    //int j=1;
                   // System.out.println("scheduleData "+scheduleData.length());
                    for (int i = 0; i< scheduleData.length(); i++) {
                       // System.out.println("A "+i);
                        JSONObject objData = scheduleData.getJSONObject(i);

                        mylist.add(new fetchschedule(
                                objData.getString("desc"),
                                objData.getString("wing"),
                                objData.getString("schedule_name"),
                                objData.getString("post_no"),
                                objData.getString("id")
                               // j

                        ));


                    }
                   // System.out.println("My List  "+mylist.toString());
                    CustomScheduleListAdapter adapter=new CustomScheduleListAdapter(getApplicationContext(),R.layout.activity_schedule_list,mylist);
                    scheduleList.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(scheduleList);
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
                String url_login = getString(R.string.url) + "CheckPost/GetScheduleList";
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
