package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VisitorExit extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    Session session;
    private ListView list;
    static ArrayList<VisitorExitList> mylist;
    FloatingActionButton fab;
    ProgressDialog dialog;
    VisitorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_exit);
        setTitle(getString(R.string.visitor_exit));
        list = (ListView) findViewById(R.id.vEList);
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        searchView = (SearchView) findViewById(R.id.search_view);
        mylist = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vreport = new Intent(getApplicationContext(), visitorreport.class);
                startActivity(vreport);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VisitorExitList visit = adapter.getItem(position);
                System.out.println(visit.getVid());
                Intent visitordetails = new Intent(VisitorExit.this, visitordetails.class);
                visitordetails.putExtra("visitorId", visit.getVid());
                startActivity(visitordetails);
            }
        });
        session = new Session(getApplicationContext());
        /* --    back button code ---- */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* --    back button code ---- */
        new VisitorData().execute();
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
            System.out.println("Test 1 : " + newText);
            list.setFilterText(newText.toString());
            adapter.getFilter().filter(newText);
        }
        return true;
    }

    private class VisitorData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                Log.e("Result : ","" +obj);
                JSONObject jsondata=obj.getJSONObject("response");
                //tv.setText(result);
                JSONArray VisitorData = jsondata.getJSONArray("visitors");
                System.out.println("Staff Array : " + VisitorData);
                int k=1;
                for(int i=0;i<VisitorData.length();i++)
                {
                    String store="";
                    JSONObject jsonstaff=VisitorData.getJSONObject(i);
                    JSONObject visitordetails=jsonstaff.getJSONObject("VisitorDetails");
                    JSONObject vobjdetail=visitordetails.getJSONObject(String.valueOf(0));
                    System.out.println(  vobjdetail.getString("FullName"));
                    JSONArray unit_no=jsonstaff.getJSONArray("UnitNo");
                    if(!(jsonstaff.getString("unit_id").equals("-1"))) {
                        for (int j = 0; j < unit_no.length(); j++) {
                            JSONArray test1 = unit_no.getJSONArray(j);
                            Log.e("test1", String.valueOf(test1.length()));
                            if (unit_no.length() == k && test1.getString(2).equals("2")) {
                                store = "";
                            } else {
                                store += test1.getString(0)  + "(" + test1.getString(3) + ")";
                                store += "/";
                            }
                        }
                        String newstr;
                        if (store == "") {
                            newstr = "";
                        } else {
                            newstr = store.substring(0, store.length() - 1);
                        }
                        String doc_img = "";
                        if(!vobjdetail.getString("Doc_img").equals(""))
                        {
                            doc_img = vobjdetail.getString("Doc_img");
                        }

                        if(newstr.equals(""))
                        {}
                        else {
                            mylist.add(new VisitorExitList(
                                    String.valueOf(jsonstaff.getInt("id")),
                                    vobjdetail.getString("FullName"),
                                    newstr,
                                    jsonstaff.getString("Date"),
                                    jsonstaff.getString("InTime"),
                                    String.valueOf(jsonstaff.getInt("Entry_Gate")),
                                    String.valueOf(jsonstaff.getInt("time_status")),
                                    jsonstaff.getString("vehicle"),
                                    getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + vobjdetail.getString("img"),
                                    jsonstaff.getString("visitorMobile"),
                                    jsonstaff.getString("Entry_With"),
                                    getString(R.string.imageurl) + "SecuirityApp/VisitorDoc/" + doc_img

                            ));
                        }
                    }
                }

                Log.e("listSize::", String.valueOf(mylist.size()));
            }
            catch (Exception e)
            {
            }
            adapter=new VisitorListAdapter(getApplicationContext(),R.layout.visitorexitlistview,mylist);
            list.setAdapter(adapter);
            setupSearchView();
            dialog.dismiss();

        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "Visitor/fetchVisitors";
                Log.e("FetchVisitor",url_login);
                String token=session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                Log.e("TOKEN::::",token);
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

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("Search Here");
    }
}
