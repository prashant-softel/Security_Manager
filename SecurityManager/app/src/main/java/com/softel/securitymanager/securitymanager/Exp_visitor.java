package com.softel.securitymanager.securitymanager;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Exp_visitor extends AppCompatActivity implements SearchView.OnQueryTextListener {
private TextView GateNo;
private TableLayout ExpCureent,ExpAll;
static ArrayList<allExpectedVisitor> Expectdlist;
private ListView list;
Session session;
SearchView searchView;
CustomAdapterExpectedVisitor adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_visitor);
        setTitle(this.getString(R.string.expected_visitor));
        /* --    back button code ---- */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* --    back button code ---- */

        session=new Session(getApplicationContext());
        GateNo=(TextView)findViewById(R.id.txtGateNo);
        GateNo.setText(this.getString(R.string.gateno)+session.getGateNo());
        Expectdlist=new ArrayList<>();
        list=(ListView) findViewById(R.id.expeVisitor);
        searchView = (SearchView) findViewById(R.id.search_view);
        new getExpected().execute();
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
            System.out.println("Test 1 : " +newText);
            list.setFilterText(newText.toString());
            adapter.getFilter().filter(newText);
        }
        return true;
    }


    private class getExpected extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                Log.e("expectedVisitorResp",result);
                String success=obj.getString("success");
                System.out.println("success : " +success);
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    System.out.println("response : " +jsondata);
                    JSONArray ExpectedData = jsondata.getJSONArray("TotalExpected");
                    System.out.println("TotalExpected : " +ExpectedData);
                    System.out.println("TotalExpected length  : " +ExpectedData.length());

                    for (int i = 0; i< ExpectedData.length(); i++) {
                        String unit="",ownername="",wing="";
                        JSONObject objData = ExpectedData.getJSONObject(i);
                        System.out.println("getJSONObject(i) : " +objData);

                        if(objData.getString("currentIn").equals("0")) {
                            System.out.println("Flat");
                           if(objData.getInt("unit") == 0)
                           {
                               unit = "S-Office";
                               ownername = "S-Office";
                               wing = "-";
                               System.out.println("Flat: " + unit );
                               System.out.println("date:    "+objData.getString("expected_date"));
                            }
                           else
                           {
                               unit = objData.getString("UnitNo");
                               ownername =  objData.getString("OwnerName");
                               wing = objData.getString("Wing");
                               System.out.println("Flat: " + unit );
                           }

                            Expectdlist.add(new allExpectedVisitor(
                                    objData.getString("fname"),
                                    objData.getString("lname"),
                                    objData.getString("mobile"),
                                    unit,
                                    objData.getString("expected_date"),
                                    objData.getString("expected_time"),
                                    objData.getString("purpose_name"),
                                    objData.getString("currentIn"),
                                    wing,
                                   ownername

                            ));

                        }
                    }
                   adapter=new CustomAdapterExpectedVisitor(getApplicationContext(),R.layout.allexpected,Expectdlist);
                    list.setAdapter(adapter);
                    setupSearchView();

                }
                else {
                }

            }
            catch(Exception e)
            {}

        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/fetchExpVisitor";
                String token=session.gettoken();
                String SocietyId=session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("societyID",SocietyId));
                Log.e("expectedVisitorParams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("origresponse:   "+ origresponseText);
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
    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("Search Here");
    }
}
