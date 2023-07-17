package com.softel.securitymanager.securitymanager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ongoing  extends Fragment  {

    Session session;
    ListView checkpostlist;
    ArrayList<fetchcheckpost> mylist;
    BottomNavigationView navigationView;
    public ongoing() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.ongoing, container, false);
        View rootView = inflater.inflate(R.layout.ongoing, container, false);
        session = new Session(getContext());
        System.out.println("Position " + session.gettoken());
        //session = new Session(getApplicationContext());
        mylist = new ArrayList<>();
        //dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setTitle(getString(R.string.loading));
        //dialog.setMessage(getString(R.string.loading));
        //dialog.setIndeterminate(true);
        //dialog.setCanceledOnTouchOutside(false);

        checkpostlist = (ListView) rootView.findViewById(R.id.listView1);

        /*checkpostlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchcheckpost checklist = mylist.get(position);
                System.out.println("ID :" + checklist.getCheckPost_id());
                System.out.println("Position " + id);
                //System.out.println("<"+checklist.getStaffID() + "><"+ staff.getName() + "><" + staff.getJobProfile() + ">");
                //Intent RoundList=new Intent(upcomming.this,Round.class);
                Intent RoundList = new Intent(ongoing.this.getActivity(), Round.class);
                RoundList.putExtra("ScheduleId", checklist.getCheckPost_id());
                RoundList.putExtra("RoundType", checklist.getRoundType());
               // RoundList.putExtra("ScheduleTime", checklist.getScheduleTime());
                RoundList.putExtra("RoundID", checklist.getRound_id());
                startActivity(RoundList);
            }
        });*/
        new ongoing.getCheckPost().execute();
        //System.out.println("After call ");
        return rootView;
    }
    private class getCheckPost extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String result) {
           // Log.d("on post ", "on post execute");
            try {
               //System.out.println("On Going");
                JSONObject obj = new JSONObject(result);
               // System.out.println("Result : " + obj);
                String success = obj.getString("success");
                //System.out.println("SUccess");
                if (success.contains("1")) {
                   // System.out.println("Inside SUccess");
                    JSONObject jsondata = obj.getJSONObject("response");
                    //System.out.println("checkPOstDate " + jsondata);
                    JSONArray checkPostData = jsondata.getJSONArray("CheckpostList");


                   // System.out.println("checkPOstDate1 " + checkPostData);
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
                String type = "1";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", stoken));
                params.add(new BasicNameValuePair("SocietyID", SocietyID));
                params.add(new BasicNameValuePair("type", type));
               // params.add(new BasicNameValuePair("checkpostID", ckpid));
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
