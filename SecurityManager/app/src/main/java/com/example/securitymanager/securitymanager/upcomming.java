package com.example.securitymanager.securitymanager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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


public class upcomming extends Fragment {

    Session session;
    ListView checkpostlist;
    ArrayList<fetchcheckpost> mylist;
    BottomNavigationView navigationView;
    //ProgressDialog dialog = new ProgressDialog(this);

    public upcomming() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upcoming, container, false);
        session = new Session(getContext());
        System.out.println("Position " + session.gettoken());
        //session = new Session(getApplicationContext());
        mylist = new ArrayList<>();

         checkpostlist = (ListView) rootView.findViewById(R.id.listView2);
      //  checkpostlist = getView().findViewById(R.id.listView2);

        checkpostlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchcheckpost checklist = mylist.get(position);
                System.out.println("ID :" + checklist.getCheckPost_id());
                System.out.println("Position " + id);
                //System.out.println("<"+checklist.getStaffID() + "><"+ staff.getName() + "><" + staff.getJobProfile() + ">");
                //Intent RoundList=new Intent(upcomming.this,Round.class);
                session.setScheduleID(checklist.getCheckPost_id());
                session.setgetRoundID(checklist.getRound_id());
                session.setNoOfCheckpost(checklist.getTotalCheckPost());
                Intent RoundList = new Intent(upcomming.this.getActivity(), Round.class);
                RoundList.putExtra("ScheduleId", checklist.getCheckPost_id());
                RoundList.putExtra("RoundType", checklist.getRoundType());
                RoundList.putExtra("RoundID", checklist.getRound_id());
               // RoundList.putExtra("TimeID", checklist.getTime_id());
                startActivity(RoundList);
            }
        });

        new getCheckPost().execute();

        return rootView;
    }


    private class getCheckPost extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String result) {
           // Log.d("on post ", "on post execute");
            try {
                System.out.println("Upcomming ");
                JSONObject obj = new JSONObject(result);
                //System.out.println("Result : " + obj);
                String success = obj.getString("success");
               // System.out.println("SUccess");
                if (success.contains("1")) {
                    //System.out.println("Inside SUccess");
                    JSONObject jsondata = obj.getJSONObject("response");
                   // System.out.println("checkPOstDate " + jsondata);
                    JSONArray checkPostData = jsondata.getJSONArray("CheckpostList");


                    //System.out.println("checkPOstDate1 " + checkPostData);
                    int j = 1;
                    for (int i = 0; i < checkPostData.length(); i++) {
                        JSONObject objData = checkPostData.getJSONObject(i);

                        mylist.add(new fetchcheckpost(
                                objData.getString("sch_name"),
                                objData.getString("round_Time"),
                                objData.getString("no_of_post"),
                                objData.getString("round_id"),
                                objData.getString("id"),
                                objData.getString("no_of_checkpost"),
                               // objData.getString("timeid"),
                                j

                        ));


                    }
                    //CustomCheckPostAdapter adapter = new CustomCheckPostAdapter(getApplicationContext(), R.layout.checkpost_fetch, mylist);
                    CustomCheckPostAdapter adapter = new CustomCheckPostAdapter(getActivity().getApplicationContext(), R.layout.checkpost_fetch, mylist);
                  //  getActivity().getApplicationContext()
                    checkpostlist.setAdapter(adapter);
                    justifyListViewHeightBasedOnChildren(checkpostlist);
                   // dialog.dismiss();
                    // System.out.println("News Data 1");

                }
                //Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                System.out.println("Expaption " + e.getMessage());
            }

        }

        public String postData() {
            String output = "";
           // System.out.println("Inside Post Data  ");
            try {

                String stoken = session.gettoken();
                String SocietyID = session.getSocietyid();
               // getstaff=getIntent().getExtras();
                String type = "0";
                //get Schedule Id
               // Bundle bundle = getActivity().getIntent().getExtras();
               // String ckpid =  "1";//bundle.getString("ScheduleID"); //hardcoded
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("type", type));
                //params.add(new BasicNameValuePair("checkpostID", ckpid));
                String url_login = getString(R.string.url) + "CheckPost/GetCheckpost";
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                output = json.toString();
                System.out.println("output_json:  " + output);
            } catch (Exception e) {
                System.out.println("Expaption1 " + e.getMessage());
            }
            return output;
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
    //@Override
    /*public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Intent checheckpost = new Intent(getContext(), CheckPost.class);
        //startActivity(intent);
       // startActivity(checheckpost);
       View  view  = inflater.inflate(R.layout.upcoming, container, false);


       return view;
    }*/
//}
