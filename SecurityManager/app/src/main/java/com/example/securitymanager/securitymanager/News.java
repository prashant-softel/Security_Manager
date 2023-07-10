package com.example.securitymanager.securitymanager;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {
    Session session;
    ListView mNotice;
    ArrayList<NewsList> mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitle(this.getString(R.string.notice_board));
        /* --    back button code ---- */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* --    back button code ---- */
        session=new Session(getApplicationContext());
        mylist=new ArrayList<>();
        mNotice=(ListView) findViewById(R.id.AllNotices);
        new getNews().execute();
    }
    private class getNews extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray NewsData = jsondata.getJSONArray("news");
                    int j=1;
                    for (int i = 0; i< NewsData.length(); i++) {
                        JSONObject objData = NewsData.getJSONObject(i);
                        mylist.add(new NewsList(
                                objData.getString("title"),
                                objData.getString("description"),
                                j
                        ));


                    }
                    NewViewCustomeAdapter adapter=new NewViewCustomeAdapter(getApplicationContext(),R.layout.newslist,mylist);
                    mNotice.setAdapter(adapter);
                    // System.out.println("News Data 1");

                }
                //Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();


            }
            catch(Exception e)
            {}

        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/getAll";
                String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                //params.add(new BasicNameValuePair("gateNo",gateno));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
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
            //String gateno=(String) params[0];
            String s=postData();
            return s;
        }

    }
}
