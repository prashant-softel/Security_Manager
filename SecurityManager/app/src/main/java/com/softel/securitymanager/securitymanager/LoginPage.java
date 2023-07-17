package com.softel.securitymanager.securitymanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.*;

import com.softel.securitymanager.Util.PreferenceServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class LoginPage extends Activity {

    EditText uname, password;
    ProgressBar pb_loading;
    TextView txt_errormsg;
    Button btn_login;
    Session session;
    String Device_id;
    NetworkInfo netInfo;
    ConnectivityManager cm;
    public boolean checkConnectivity(){
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_LONG).show();
            return false;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        session=new Session(getApplicationContext());
        PreferenceServices.init(this);
        findViewsById();
        checkConnectivity();
        Device_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SimpleDateFormat s=new SimpleDateFormat("dd-MMM-yyyy");
        if(session.getLoginDate().isEmpty() || session.getLoginDate()==null){
            session.setLoginDate(getCurrentDate());
        }

            String formattedDate = s.format(new Date(session.getLoginDate()));
            Date d=new Date(formattedDate);

            if(d.compareTo(new Date(getCurrentDate()))>=0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pb_loading.setVisibility(View.GONE);
                        Log.e("token_",session.gettoken());
                        if(!session.gettoken().equalsIgnoreCase("")) {
                            new tokenauth().execute();
                        }
                    }
                }, 1000);

            }else{
                pb_loading.setVisibility(View.GONE);
                session.setLoginDate("");
                session.settoken("");
                session.setusername("");
                session.setpassword("");
                PreferenceServices.getInstance().setFlats("");
                PreferenceServices.getInstance().saveWing(null,"Wing");

            }

       /* if (System.currentTimeMillis() > Date.parse(session.getLoginDate())) {
            Toast.makeText(this, "YesterDay", Toast.LENGTH_SHORT).show();
        }else{

                Toast.makeText(this, "Today", Toast.LENGTH_SHORT).show();
        }*/

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btn_login.setEnabled(false);

                if(uname.getText().toString().length()==0)
                {
                    uname.requestFocus();
                    uname.setError("Field Cannot be Empty");
                }
                else if(password.getText().toString().length()==0)
                {
                    password.requestFocus();
                    password.setError("Field Cannot be Empty");
                }
                else
                { new Login().execute();

                  //  LoginUser();
                }
                btn_login.setEnabled(true);

            }
        });

    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getYesterdayDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void findViewsById() {

        pb_loading=(ProgressBar) findViewById(R.id.loading);
        uname = (EditText) findViewById(R.id.txtUser);
        password = (EditText) findViewById(R.id.txtPass);
        btn_login = (Button) findViewById(R.id.submit);
        txt_errormsg=(TextView) findViewById(R.id.data);
        txt_errormsg.setText("");
    }

    private class tokenauth extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
               Log.e("Result : ", String.valueOf(obj));
                String success=obj.getString("success");
                if (success.contains("1"))
                {
                    session.setkiosk("1");
                    Intent welcome=new Intent(LoginPage.this,homepage.class);
                    startActivity(welcome);
                    finish();
                }
                else
                {
                    //if(session.gettoken().equals(""))
                    //{ new loggedout().execute();}
                }


            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String token=session.gettoken();
                String url_login = getString(R.string.url) + "Login";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("grant_type","refresh_token"));
                params.add(new BasicNameValuePair("username",session.getusername()));
                params.add(new BasicNameValuePair("pass",session.getpassword()));

                Log.e("LoginParams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                System.out.println(json);
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
            String s=postData();
            return s;
        }
    }

    private class Login extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try
            {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {

                    txt_errormsg.setText("");

                    String warversion = obj.getString("version");
                    session.setWarversion(warversion);
                    String responsedata = obj.getString("response");
                    JSONObject sessiondata = new JSONObject(responsedata);
                    session.setusername(uname.getText().toString());
                    session.setpassword(password.getText().toString());
                    session.setSocietyid(sessiondata.getString("SocietyId"));
                    session.setGateNo(sessiondata.getString("GateNo"));
                    session.setSocietyName(sessiondata.getString("SocietyName"));
                    session.setName(sessiondata.getString("name"));
                    session.settoken(sessiondata.getString("token"));
                    session.setkiosk("1");
                    String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    if (sessiondata.getString("loggedinstatus").equals("1") && (!android_id.equals(sessiondata.getString("device_id")))) {
                        session.settoken("");
                        Toast.makeText(getApplicationContext(), "Already a device is logged in with the following user name", Toast.LENGTH_LONG).show();
                        txt_errormsg.setText("User: "+uname.getText().toString()+" is already logged in.");
                        txt_errormsg.setTextColor(Color.RED);

                    } else {
                        if (sessiondata.getString("role").equals("Security")) {
                            session.setRole(sessiondata.getString("role"));
                            session.setkioskv("0");
                            Toast.makeText(getApplicationContext(), "Logged in Susccessful", Toast.LENGTH_LONG).show();
                            // System.out.println(session.getRole() + " "+session.getSocietyid() + " " +  session.gettoken());
                            if (sessiondata.getString("GateNo").equals("0")) {
                                Toast.makeText(getApplicationContext(), "Kindly Set Gate No.", Toast.LENGTH_LONG).show();
                                Intent setting = new Intent(LoginPage.this, Setting.class);
                                startActivity(setting);
                                finish();
                            } else {
                                Intent welcome = new Intent(LoginPage.this, homepage.class);
                                startActivity(welcome);
                                finish();
                            }
                            new setloggedinstatus().execute();
                        } else if (sessiondata.getString("login_id").equals("265")) {
                            session.setkioskv("1");
                            if (sessiondata.getString("GateNo").equals("0")) {
                                Toast.makeText(getApplicationContext(), "Kindly Set Gate No.", Toast.LENGTH_LONG).show();
                                Intent setting = new Intent(LoginPage.this, Setting.class);
                                startActivity(setting);
                                finish();
                            } else {
                                Intent welcome = new Intent(LoginPage.this, homepage.class);
                                startActivity(welcome);
                                finish();
                            }
                            new setloggedinstatus().execute();
                        } else {
                            session.settoken("");
                            Toast.makeText(getApplicationContext(), "You are not an authorized person", Toast.LENGTH_LONG).show();
                            txt_errormsg.setText("You are not an authorized person");
                            txt_errormsg.setTextColor(Color.RED);
                        }
                    }
                }
                else
                {
                    if(checkConnectivity()){
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                        txt_errormsg.setText("Invalid username or password");
                        txt_errormsg.setTextColor(Color.RED);
                    }
                    else{
                        txt_errormsg.setText("Internet not Connected");
                        txt_errormsg.setTextColor(Color.RED);
                    }

                }

            }
            catch(Exception e)
            {
                Log.e("LoginException",e.toString());
            }
        }

        public String postData() {

            String origresponseText="";
            try {
                String username = uname.getText().toString();
                String pass = password.getText().toString();

                Log.e("username",username);
                Log.e("pass",pass);

                String url_login = getString(R.string.url) + "Login";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Email",username));
                params.add(new BasicNameValuePair("Password",pass));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
                origresponseText=json.toString();
                System.out.println("Connection close");
            }
            catch (Exception e)
            {
                Log.e("LoginException2",e.toString());

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

    private class setloggedinstatus extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                Log.e("News/UpdateLoginDataResp", String.valueOf(obj));
                String success=obj.getString("success");
                if (success.contains("1"))
                {
                   System.out.println("Updated");
                }


            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String username = uname.getText().toString();
                String pass = password.getText().toString();
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                System.out.println("Android Id : " + android_id);

                String token=session.gettoken();
                String url_login = getString(R.string.url) + "News/UpdateLoginData";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("Email",username));
                params.add(new BasicNameValuePair("Password",pass));
                params.add(new BasicNameValuePair("deviceId",android_id));
                Log.e("News/UpdateLoginDataParams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
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
            String s=postData();
            return s;
        }

    }

    private class loggedout extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1"))
                {
                    Intent sLogout = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(sLogout);
                    System.out.println("Updated");
                    session.setusername("");
                    session.setpassword("");
                    session.settoken("");

                }


            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String username = session.getusername();
                String pass =session.getpassword();
                String token=session.gettoken();
                String url_login = getString(R.string.url) + "News/UpdateLoginDatalogout";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",""));
                params.add(new BasicNameValuePair("Email",username));
                params.add(new BasicNameValuePair("Password",pass));

                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                System.out.println(json);
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
            String s=postData();
            return s;
        }

    }

    private void LoginUser() {

        String username = uname.getText().toString();
        String pass = password.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://way2society.com:8080/NewSecurityManagerWeb/Login?Email="+username+"&Password="+pass, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("Response", response.toString());
                try
                {
                    JSONObject obj = new JSONObject(response.toString());
                    System.out.println("Result : " +obj);
                    String success=obj.getString("success");
                    if (success.contains("1")) {

                        txt_errormsg.setText("");

                        String warversion = obj.getString("version");
                        session.setWarversion(warversion);
                        String responsedata = obj.getString("response");
                        JSONObject sessiondata = new JSONObject(responsedata);
                        session.setusername(uname.getText().toString());
                        session.setpassword(password.getText().toString());
                        session.setSocietyid(sessiondata.getString("SocietyId"));
                        session.setGateNo(sessiondata.getString("GateNo"));
                        session.setSocietyName(sessiondata.getString("SocietyName"));
                        session.setName(sessiondata.getString("name"));
                        session.settoken(sessiondata.getString("token"));
                        session.setkiosk("1");
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        if (sessiondata.getString("loggedinstatus").equals("1") && (!android_id.equals(sessiondata.getString("device_id")))) {
                            session.settoken("");
                            Toast.makeText(getApplicationContext(), "Already a device is logged in with the following user name", Toast.LENGTH_LONG).show();
                            txt_errormsg.setText("User: "+uname.getText().toString()+" is already logged in.");
                            txt_errormsg.setTextColor(Color.RED);

                        } else {
                            if (sessiondata.getString("role").equals("Security")) {
                                session.setRole(sessiondata.getString("role"));
                                session.setkioskv("0");
                                Toast.makeText(getApplicationContext(), "Logged in Susccessful", Toast.LENGTH_LONG).show();
                                // System.out.println(session.getRole() + " "+session.getSocietyid() + " " +  session.gettoken());
                                if (sessiondata.getString("GateNo").equals("0")) {
                                    Toast.makeText(getApplicationContext(), "Kindly Set Gate No.", Toast.LENGTH_LONG).show();
                                    Intent setting = new Intent(LoginPage.this, Setting.class);
                                    startActivity(setting);
                                    finish();
                                } else {
                                    Intent welcome = new Intent(LoginPage.this, homepage.class);
                                    startActivity(welcome);
                                    finish();
                                }
                                new setloggedinstatus().execute();
                            } else if (sessiondata.getString("login_id").equals("265")) {
                                session.setkioskv("1");
                                if (sessiondata.getString("GateNo").equals("0")) {
                                    Toast.makeText(getApplicationContext(), "Kindly Set Gate No.", Toast.LENGTH_LONG).show();
                                    Intent setting = new Intent(LoginPage.this, Setting.class);
                                    startActivity(setting);
                                    finish();
                                } else {
                                    Intent welcome = new Intent(LoginPage.this, homepage.class);
                                    startActivity(welcome);
                                    finish();
                                }
                                new setloggedinstatus().execute();
                            } else {
                                session.settoken("");
                                Toast.makeText(getApplicationContext(), "You are not an authorized person", Toast.LENGTH_LONG).show();
                                txt_errormsg.setText("You are not an authorized person");
                                txt_errormsg.setTextColor(Color.RED);
                            }
                        }
                    }
                    else
                    {
                        if(checkConnectivity()){
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                            txt_errormsg.setText("Invalid username or password");
                            txt_errormsg.setTextColor(Color.RED);
                        }
                        else{
                            txt_errormsg.setText("Internet not Connected");
                            txt_errormsg.setTextColor(Color.RED);
                        }

                    }

                }
                catch(Exception e)
                {
                    Log.e("LoginException",e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
