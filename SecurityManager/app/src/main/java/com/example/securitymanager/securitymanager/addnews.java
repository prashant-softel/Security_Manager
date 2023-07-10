package com.example.securitymanager.securitymanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class addnews extends AppCompatActivity {
private EditText mTitle;
private EditText mDesc;
private Button mSubmit;
private Button mCancle;

Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnews);
        setTitle("Add News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session=new Session(getApplicationContext());
        mTitle=(EditText) findViewById(R.id.txtTitle);

        mDesc=(EditText) findViewById(R.id.txtDesc);

        mSubmit =(Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addNews().execute();
            }
        });
    mCancle=(Button) findViewById(R.id.cancle);
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CancleNews = new Intent(addnews.this, homepage.class);
                startActivity(CancleNews);
            }
        });
    }
    private class addNews extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                Toast.makeText(getApplicationContext(),"News Added successfully.",Toast.LENGTH_SHORT).show();
                Intent addHome = new Intent(addnews.this, homepage.class);
                startActivity(addHome);

            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/insert";
                String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("title", mTitle.getText().toString()));
                params.add(new BasicNameValuePair("desc", mDesc.getText().toString()));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText=json.toString();
                System.out.println("Connection close");

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
