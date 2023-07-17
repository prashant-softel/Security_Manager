package com.softel.securitymanager.securitymanager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softel.securitymanager.Util.PreferenceServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToogle;
    private RelativeLayout vClickvisitorEntry;
    private RelativeLayout vClickvisitorExit;
    private RelativeLayout vClickStaffEntry;
    private RelativeLayout vClickStaffExit;
    private RelativeLayout vClickEmergency;
    private RelativeLayout vClickExpVisitor;

    ListView  mNotice,mSchedule;
    ArrayList<NewsList> mylist;
    ArrayList<fetchcheckpost> myschedulelist;
    Session session;
    String SocietyNumber,startThread="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setTitle(this.getString(R.string.home));
        PreferenceServices.init(this);

        /* ------------- Kiosk ----------------- */
        /*mKiosk=(Button) findViewById(R.id.button_exit_kiosk_mode);

        mKiosk.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                stopLock ();
            }
        });*/

        requestKeepScreenOn();
        /* --------------------------------------*/

        session=new Session(getApplicationContext());
      //  mNewsText= (TextView) findViewById(R.id.txtNews);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.Drawer);
        mNavigationView=(NavigationView) findViewById(R.id.nav_view);

       /*  ----------------------  Header set name and society name ------------------  */
       String GateNo = session.getGateNo();
        View hGateView =  mNavigationView.getHeaderView(0);
        TextView nav_gate = (TextView)hGateView.findViewById(R.id.gateno);
       // nav_gate.setText(GateNo);
        nav_gate.append(GateNo);

        String UserName = session.getName();
        View hUserView =  mNavigationView.getHeaderView(0);
        TextView nav_user = (TextView)hUserView.findViewById(R.id.userName);
        nav_user.setText(UserName);

        String societyName  = session.getSocietyName();
        View hSocView =  mNavigationView.getHeaderView(0);
        TextView nav_soc = (TextView)hSocView.findViewById(R.id.societyname);
        nav_soc.setText(societyName);
        /*  ----------------------  Header set name and society name ------------------  */

        mNavigationView.setNavigationItemSelectedListener(this);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        mNotice=(ListView) findViewById(R.id.societyNotice);
        mNotice.setClickable(true);
        mNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent news=new Intent(homepage.this,News.class);
                startActivity(news);
            }
        });

        mSchedule=(ListView) findViewById(R.id.societySchedule);
        mSchedule.setClickable(true);
        mSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Schedule=new Intent(homepage.this, CheckPostView.class);
                startActivity(Schedule);
            }
        });
        mylist=new ArrayList<>();
        myschedulelist=new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* --    Card view Clikcable Visitor entry  code ---- */
        vClickvisitorEntry = (RelativeLayout) findViewById(R.id.ClickvisitorEntry);
        vClickvisitorEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visitroEntry = new Intent(homepage.this, VisitorEntry.class);
                startActivity(visitroEntry);
            }
        });
        /* --    Card view Clikcable Visitor exit  code ---- */
        vClickvisitorExit = (RelativeLayout) findViewById(R.id.ClickvisitorExit);
        vClickvisitorExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visitorExit = new Intent(homepage.this, VisitorExit.class);
                startActivity(visitorExit);
            }
        });
        /* --    Card view Clikcable Staff Entry code ---- */
        vClickStaffEntry = (RelativeLayout) findViewById(R.id.ClickStaffEntry);
        vClickStaffEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffEntry = new Intent(homepage.this, Staff_Entry.class);
                startActivity(staffEntry);
            }
        });
        /* --    Card view Clikcable Staff Exit code ---- */
        vClickStaffExit = (RelativeLayout) findViewById(R.id.ClickStaffExit);
        vClickStaffExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffExit = new Intent(homepage.this, staff_exit.class);
                startActivity(staffExit);
            }
        });

        /* --    Card view Clikcable Emergency code ---- */
       vClickEmergency =(RelativeLayout) findViewById(R.id.ClickEmergency);
        vClickEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");
                if(SocietyNumber.equals(""))
                {
                    Toast.makeText(getApplicationContext(),getString(R.string.contact_for_emergency),Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + SocietyNumber));
                    startActivity(callIntent);
                }
           }
        });
        /* --    Card view Clikcable Staff Exit code ---- */
        vClickExpVisitor = (RelativeLayout) findViewById(R.id.ClickExpVisitor);
        vClickExpVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ExpectedVisitor = new Intent(homepage.this, Exp_visitor.class);
                startActivity(ExpectedVisitor);
            }
        });
        /* --    Card view Clikcable add News ---- */
       /* fab = (FloatingActionButton) findViewById(R.id.addNews);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNews = new Intent(homepage.this, addnews.class);
                startActivity(addNews);
            }
        });*/

        /*  ----------------------  Fetch News  -------------------------------  */
        new getNews().execute();
        new getContactNo().execute();
        new getSchedule().execute();
      //  new getOtpFlag().execute();


        Thread t=new Thread() {
            int count = 0;

            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(5000);  //100000ms = 10 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                               if(startThread.equals("0")) {
                                    count++;
                                    //new getSOS().execute();
                                }
                                else if(startThread.equals("1"))
                                {
                                    isInterrupted();
                                }

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        if(startThread.equals("1")) {
           t.stop();
        }
        else {
            t.start();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nov_home:
                Intent home = new Intent(getApplicationContext(), homepage.class);
                startActivity(home);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_ventry:
                Intent vEntry = new Intent(getApplicationContext(), VisitorEntry.class);
                startActivity(vEntry);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_vexit:
                Intent vExit = new Intent(getApplicationContext(), VisitorExit.class);
                startActivity(vExit);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_staffentry:
                Intent sEntry = new Intent(getApplicationContext(), Staff_Entry.class);
                startActivity(sEntry);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_staffexit:
                Intent sExit = new Intent(getApplicationContext(), staff_exit.class);
                startActivity(sExit);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nov_addstaff:
                Intent sStaffAdd = new Intent(getApplicationContext(), staff_add.class);
                startActivity(sStaffAdd);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_news:
                Intent sNews = new Intent(getApplicationContext(), News.class);
                startActivity(sNews);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_about:
                Intent sAbout = new Intent(getApplicationContext(), About.class);
                startActivity(sAbout);
                mDrawerLayout.closeDrawers();
                break;

            case R.id.nov_setting:
                Intent sSetting = new Intent(getApplicationContext(), Setting.class);
                startActivity(sSetting);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nov_master:
                Intent sMaster = new Intent(getApplicationContext(), CheckPostView.class);
                startActivity(sMaster);
                mDrawerLayout.closeDrawers();
                break;
            /*case R.id.nov_round:
                Intent sCheckPost = new Intent(getApplicationContext(), CheckPost.class);
                startActivity(sCheckPost);
                mDrawerLayout.closeDrawers();
                break;*/

           /* case R.id.nav_schedule:
                Intent sSchedule = new Intent(getApplicationContext(), schedule_list.class);
                startActivity(sSchedule);
                mDrawerLayout.closeDrawers();
                break;*/
           /* case R.id.nov_master:
                Intent sMaster = new Intent(getApplicationContext(), CheckPostMaster.class);
                startActivity(sMaster);
                mDrawerLayout.closeDrawers();
                break;*/
           /* case R.id.nov_vcheck:
                Intent sCheckMaster = new Intent(getApplicationContext(), CheckPostView.class);
                startActivity(sCheckMaster);
                mDrawerLayout.closeDrawers();
                break;*/

            case R.id.nov_logout:

                new setloggedinstatus().execute();
                mDrawerLayout.closeDrawers();
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;

    }

    /*----------------------------------  Get News   --------------------------- */
    private class getNews extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                //System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    Log.e("getHomeResp", String.valueOf(jsondata));
                    JSONArray NewsData = jsondata.getJSONArray("news");
                    int j=0;
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
                else
                {
                    Toast.makeText(getApplicationContext(),getString(R.string.kindly_check_internet),Toast.LENGTH_SHORT);
                }
                //Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();

            }
            catch(Exception e)
            {}

        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/getHome";
                String token=session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                //params.add(new BasicNameValuePair("gateNo",gateno));
                Log.e("getHomeParams", String.valueOf(params));
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
    /*----------------------------------  Get SOciety Contact   --------------------------- */
    private class getContactNo extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                Log.e("ContactResp", String.valueOf(obj));
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONObject NewsData = jsondata.getJSONObject("news");
                    JSONObject NewsData1 = NewsData.getJSONObject("0");
                    SocietyNumber = NewsData1.getString("phone2");
                    session.setEmegency(NewsData1.getString("phone2"));
                }


            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/GetContactNo";
                String token=session.gettoken();
                String societyId=session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("SocietyID",societyId));
                Log.e("News/GetContactNoParams", String.valueOf(params));
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

    /* ----------------------------------  Get SOS ------------------------------------------------- */
    private class getSOS extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);

                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {

                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONObject SOSData = jsondata.getJSONObject("SOSAlert");
                    JSONObject SOSData1 = SOSData.getJSONObject("0");
                    String SOSID = SOSData1.getString("sosID");
                    String sosType = SOSData1.getString("AlertType");
                    startThread = "1";
                    Intent SOSAlert=new Intent(homepage.this,SOSAlert.class);
                    SOSAlert.putExtra("SOSID",SOSID);
                    SOSAlert.putExtra("SubmitType",sosType);
                    startActivity(SOSAlert);
                   // Thread.interrupted();
                  }
                  else {
                    startThread = "0";
                }
                //  Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();

            }
            catch(Exception e)
            {}
        }

        public String postData() {

            String origresponseText="";
            try {
                String url_login = getString(R.string.url) + "News/getSOS";
                String token=session.gettoken();
                String societyId=session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("SocietyID",societyId));
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
    /* -------------------------- Get Society Schedule -------------------- */

    private class getSchedule extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                //System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    Log.e("getHomeResp", String.valueOf(jsondata));
                    JSONArray NewsData = jsondata.getJSONArray("CheckpostList");
                   int j=0;
                    for (int i = 0; i< NewsData.length(); i++) {
                        JSONObject objData = NewsData.getJSONObject(i);
                        myschedulelist.add(new fetchcheckpost(
                                objData.getString("sch_name"),
                                objData.getString("round_Time"),
                                objData.getString("no_of_post"),
                                objData.getString("round_id"),
                                objData.getString("id"),
                                objData.getString("no_of_checkpost"),
                                j
                        ));


                    }
                    NewScheduleAdapter adapter=new NewScheduleAdapter(getApplicationContext(),R.layout.upcoming_schedule,myschedulelist);
                    mSchedule.setAdapter(adapter);
                    // System.out.println("News Data 1");

                }
                else
                {
                    Toast.makeText(getApplicationContext(),getString(R.string.kindly_check_internet),Toast.LENGTH_SHORT);
                }
                //Toast.makeText(getApplicationContext(),"Gate Number successfully set",Toast.LENGTH_SHORT).show();

            }
            catch(Exception e)
            {}

        }

        public String postData() {

            String origresponseText="";
            try {
                String url_schedule = getString(R.string.url) + "CheckPost/GetCheckpost";
                String token=session.gettoken();
                String societyId=session.getSocietyid();
                String type= "0";
                ArrayList<Object> list = new ArrayList<>();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("SocietyID",societyId));
                params.add(new BasicNameValuePair("type",type));
                //params.add(new BasicNameValuePair("gateNo",gateno));
                Log.e("getHomeParams", String.valueOf(params));
                JSONParser jParser=new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_schedule, "GET", params);
                origresponseText=json.toString();
                System.out.println("output_json:  "+origresponseText);
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

    /*  ---------------------  kiosk function --------------------*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume () {
        super.onResume ();

        if(session.getkiosk().equals("1")) {
            requestTaskLock();    //kiosk enable/disable
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            requestImmersiveMode();
        }
    }
    //****************************************
    //Tools

    private void requestKeepScreenOn() {
        //Keep screen highlight always on. This is not required for kiosk mode, but makes things more nice
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void requestImmersiveMode() {
        //Enable full-screen mode. This is not required for kiosk mode, but makes things more nice
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
               // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              //  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               // | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void requestTaskLock() {
        session.setkiosk("0");
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminReceiver = new ComponentName(this, MyDeviceAdminReceiver.class);

        if (dpm.isDeviceOwnerApp(this.getPackageName()))
        {
            String[] packages = {this.getPackageName()};
            dpm.setLockTaskPackages(deviceAdminReceiver, packages);

            if (dpm.isLockTaskPermitted(this.getPackageName())) {
                startLockTask(); //Working in full mode
                //showThisViewAndHideOthers (textKioskModeWorking);
            } else {
                startLockTask(); //Working in emulation mode
               // showThisViewAndHideOthers (textNoPermission);
            }

        } else {
            startLockTask(); //Working in emulation mode
           // showThisViewAndHideOthers (textNotADeviceAdmin);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopLock(){

        //Note. Double call for stopLockTask() sometimes causes crash, so you should check if you app is really in lock mode
        if(isAppInLockTaskMode()) {
            stopLockTask();
            showThisViewAndHideOthers (null);
        } else {
            Toast.makeText (this, R.string.not_in_kiosk_mode, Toast.LENGTH_SHORT).show ();
        }
    }


    @Override
    public void finish () {
        //Note. When your app is in lock mode, you can just call finish() from code and activity will be closed as usually
        //But I do not recommend to do so - I've got some unstable behaviour in this case. This is my personal experience and can be wrong
        //I recommend always call stopLockTask() explicitly

        if(isAppInLockTaskMode ()) return;
        super.finish ();
    }


    private boolean isAppInLockTaskMode() {
        ActivityManager activityManager;

        activityManager = (ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For SDK version 23 and above.
            return activityManager.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // When SDK version >= 21. This API is deprecated in 23.
            return activityManager.isInLockTaskMode();
        }

        return false;
    }


    private void showThisViewAndHideOthers (View v){
       // if(v == textNotADeviceAdmin) textNotADeviceAdmin.setVisibility (View.VISIBLE);
     //   else textNotADeviceAdmin.setVisibility (View.GONE);

      //  if(v == textNoPermission) textNoPermission.setVisibility (View.VISIBLE);
      //  else textNoPermission.setVisibility (View.GONE);

      //  if(v == textKioskModeWorking) textKioskModeWorking.setVisibility (View.VISIBLE);
       // else textKioskModeWorking.setVisibility (View.GONE);
    }

    private class setloggedinstatus extends AsyncTask<String, String, String>{

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
                    session.settoken("");
                    session.setusername("");
                    session.setpassword("");
                    PreferenceServices.getInstance().setFlats("");
                    PreferenceServices.getInstance().saveWing(null,"Wing");
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
                params.add(new BasicNameValuePair("token",token));
                params.add(new BasicNameValuePair("Email",username));
                params.add(new BasicNameValuePair("Password",pass));

                Log.e("LoginOutParams", String.valueOf(params));
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
}
