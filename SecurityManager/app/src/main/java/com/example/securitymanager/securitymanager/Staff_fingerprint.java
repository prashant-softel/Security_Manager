package com.example.securitymanager.securitymanager;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.widget.*;


import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
import com.example.securitymanager.securitymanager.database.UserDatabase;
import com.example.securitymanager.securitymanager.model.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class Staff_fingerprint extends Fragment implements OnClickListener, MFS100Event {
    private UserDatabase database;
    private FingerData fingerData = null;
    TextView tvStatus,SregisterStaff,st_name,st_profile,alreadyregister,newstaff;
    RelativeLayout staffcontent;
    Button btnVerify,btnClear,btn_Sync,btn_stafffetch;
    EditText edt_Staffid,edt_smobile;
    LinearLayout ls_register,Lfingersection,L_btnfetchstaff,Lerrormsg1;
    ImageView imgFinger,image1;
    private MFS100 mfs100;
    int minQuality = 60;
    int timeout = 10000;
    Session session;
    String system_staff_id="",URL,fingerprint_accessstatus="";

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.staff_fingerprint, container, false);

        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        SregisterStaff = (TextView) view.findViewById(R.id.SregisterStaff);
        newstaff = (TextView) view.findViewById(R.id.newstaff);
        btnVerify = (Button) view.findViewById(R.id.btn_verify);
        btnClear = (Button) view.findViewById(R.id.btn_clear);
        btn_Sync = (Button) view.findViewById(R.id.btn_Sync);
        imgFinger = (ImageView) view.findViewById(R.id.imgFinger);
        edt_Staffid = (EditText) view.findViewById(R.id.edt_Staffid);
        edt_smobile = (EditText) view.findViewById(R.id.edt_smobile);
        ls_register = (LinearLayout) view.findViewById(R.id.lregisterfingerprint);
        Lfingersection = (LinearLayout) view.findViewById(R.id.Lfingersection);
        staffcontent = (RelativeLayout) view.findViewById(R.id.staffcontent);
        L_btnfetchstaff = (LinearLayout) view.findViewById(R.id.btnfetchstaff);
        btn_stafffetch = (Button) view.findViewById(R.id.staff_fetch);
        Lerrormsg1 = (LinearLayout) view.findViewById(R.id.errormsg1);
        image1 = (ImageView) view.findViewById(R.id.image1);
        image1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, v, URL, null);
            }
        });
        st_name = (TextView) view.findViewById(R.id.st_name);
        st_profile = (TextView) view.findViewById(R.id.st_profile);
        alreadyregister = (TextView) view.findViewById(R.id.alreadyregister);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        session = new Session(getContext());
        new getOtpFlag().execute();


        initUI();
        intiScanner();

        //new change:
        newstaff.setPaintFlags(newstaff.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        newstaff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), staff_add.class);
                startActivity(intent);
            }
        });

        btn_stafffetch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new staffdata().execute();

            }
        });
        SregisterStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SregisterStaff.getText().equals(getString(R.string.register_staff_fingerprint)))
                {
                    ls_register.setVisibility(View.VISIBLE);
                    Lfingersection.setVisibility(View.GONE);
                    L_btnfetchstaff.setVisibility(View.VISIBLE);

                    /*
                    ls_register.setVisibility(View.VISIBLE);
                    btnVerify.setText("Register");
                    btnClear.setText("Cancel");
                    SregisterStaff.setText("Already Register? ");
                    btn_Sync.setVisibility(View.GONE);*/
                }
                else {
                    // ls_register.setVisibility(View.GONE);
                    btn_Sync.setVisibility(View.VISIBLE);
                    ls_register.setVisibility(View.INVISIBLE);
                    btnVerify.setText(getString(R.string.verify));
                    btnClear.setText(getString(R.string.clear));
                    L_btnfetchstaff.setVisibility(View.GONE);
                    staffcontent.setVisibility(View.GONE);
                    alreadyregister.setVisibility(View.GONE);
                    SregisterStaff.setText(getString(R.string.register_staff_fingerprint));
                }

             }
        });

       /* btn_Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncFingerData().execute();
            }
            });

        btn_Sync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent Setting = new Intent(getContext(),Setting.class);
                System.out.print("Test1");

                startActivity(Setting);
            }
        });
        */

        return view;
    }


    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret = 0;
        if (!hasPermission) {
            setTextonuiThread("Permission denied", true);
            return;
        }
        if (vid == 1204 || vid == 11279) {

            if (pid == 34323) {
                ret = mfs100.LoadFirmware();
                if (ret != 0) {
                    setTextonuiThread(mfs100.GetErrorMsg(ret), true);
                } else {
                    setTextonuiThread("Load firmware success", false);
                }
            } else if (pid == 4101) {
                ret = mfs100.Init();
                if (ret != 0) {
                    setTextonuiThread(mfs100.GetErrorMsg(ret), true);
                } else {
                    setTextonuiThread("Init success", false);
                }
            }

        }
    }

    @Override
    public void OnDeviceDetached() {
        mfs100.UnInit();
        setTextonuiThread("Device removed", true);
    }

    @Override
    public void OnHostCheckFailed(String arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onDestroy() {
        if (mfs100 != null) {
            mfs100.UnInit();
            mfs100.Dispose();
            mfs100 = null;
        }
        super.onDestroy();
    }

    private void initUI() {
        database = new UserDatabase(getContext());
        imgFinger.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    private void intiScanner() {
        if (mfs100 == null) {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(getContext());
        } else {
            mfs100.Init();
        }
    }

    @Override
    public void onClick(View v) {
        tvStatus.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.imgFinger:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            FingerData fingerData = new FingerData();
                            int ret = mfs100.AutoCapture(fingerData, timeout, false);

                            if (ret != 0) {

                                setTextonuiThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")", true);
                                Staff_fingerprint.this.fingerData = null;
                                displayFiner(null);
                            } else {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0, fingerData.FingerImage().length);
                                displayFiner(bitmap);
                                setTextonuiThread("Scan Success", false);
                                Staff_fingerprint.this.fingerData = fingerData;
                            }
                        } catch (Exception e) {
                            setTextonuiThread("Error", true);
                        }
                    }
                }).start();
                break;

            case R.id.btn_verify:

                if(btnVerify.getText().equals(getString(R.string.verify)))
                {
                    if (fingerData == null) {
                        setTextonuiThread("Please capture finger", true);
                        return;
                    }
                   /* new VerifyFinger().execute();*/
                    clsUser user = database.verifyFinger(fingerData.ISOTemplate(), mfs100);

                    if (user != null) {

                        setTextonuiThread("Match Successfully", false);
                        displayVerify(user);
                        String mobile="";
                        System.out.println("FingerPrint : " + user.uniqueId);
                        Intent staff_checkin = new Intent(getContext(),Staff_CheckIn.class);
                        staff_checkin.putExtra("StaffID",user.uniqueId);
                        staff_checkin.putExtra("MobileNo",mobile);
                        staff_checkin.putExtra("counter","0");

                        startActivity(staff_checkin);
                    } else {
                        setTextonuiThread("Not Match", true);
                    }
                }
                else if(btnVerify.getText().equals("Register"))
                {
                    String uid = system_staff_id;
                    String contactNo = edt_smobile.getText().toString();
                    if (isValidate(uid)) {
                        clsUser user = new clsUser();
                        user.uniqueId = uid;
                        user.contactNo = contactNo;
                        user.fingerIso = fingerData.ISOTemplate();
                        System.out.println("Register Data Get FingerISO" + fingerData.ISOTemplate());
                        clsUser clsUser = database.verifyFinger(user.fingerIso, mfs100);

                        if (clsUser == null) {

                            int retCode = database.insertUser(user);
                            if (retCode == 1) {
                                setTextonuiThread("Please enter another unique id", true);
                            } else if (retCode == -1) {

                                setTextonuiThread("Enrollment error", true);
                            } else {
                                 new FingerTest().execute();
                                 /*System.out.println("InsertResponse"+InsertResponse);
                                 if(InsertResponse.equals("1"))
                                 {
                                     setTextonuiThread("Successfully Enrolled", false);
                                     clear();
                                 }
                                 else
                                 {
                                     setTextonuiThread("Entered ID Not Register", false);
                                 }*/


                            }
	                    } else {
                            setTextonuiThread("Finger already enrolled. Please try again with another finger.", true);
                            fingerData = null;
                            displayFiner(null);
                        }}

                }
                break;
            case R.id.btn_clear:
                clear();
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void setTextonuiThread(final String message, final boolean isError) {
        tvStatus.post(new Runnable() {
            @Override
            public void run() {
                tvStatus.setText(message);
                if (isError) {
                    tvStatus.setTextColor(getResources().getColor(R.color.RedText));
                }
                else {
                    tvStatus.setTextColor(getResources().getColor(R.color.GreenText));
                }
            }
        });
    }

    private boolean isValidate(String uniqueId) {
        try {
            if (uniqueId == null || uniqueId.length() <= 0) {
                setTextonuiThread("Please enter unique id", true);
                edt_Staffid.requestFocus();
                return false;
            }
            else if (fingerData == null) {
                setTextonuiThread("Please capture finger", true);
                return false;
            }
            return  true;
        } catch (Exception e) {
            return false;
        }
    }

    private void displayVerify(clsUser user) {
        String message = "Unique ID: " + user.uniqueId + "\n"
                + "Name: " + user.name + "\n"
                + "Email: " + user.email + "\n"
                + "Number: " + user.contactNo+"\n"
                + "FingerData: " + user.fingerIso;
        tvStatus.setText(message);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                clear();
            }
        }, 5000);
    }

    private void clear() {
        fingerData = null;
        tvStatus.setText("");
        imgFinger.setImageResource(R.drawable.finger);
    }

    private void displayFiner(final Bitmap bitmap) {
        imgFinger.post(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    imgFinger.setImageBitmap(bitmap);

                } else {
                    imgFinger.setImageResource(R.drawable.finger);
                }
            }
        });
    }

    private class FingerTest extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            System.out.println("result "+result);
            Log.d("on post ", "on post execute");
            try {
                String Response = "";
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                if(success.contains("1")) {
                    Response = "Successfully Enrolled";
                }
                else
                {
                    Response = "Entered ID Not Register";

                }
                Toast.makeText(getContext(),Response, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public String postData() {

            String origresponseText="";
            try {
                String uid = system_staff_id;
                String token = session.gettoken();
                System.out.println("Uid "+uid);

                System.out.println(Staff_fingerprint.this.fingerData.ISOTemplate());

                String finger = Base64.encodeToString(Staff_fingerprint.this.fingerData.ISOTemplate(), Base64.DEFAULT);

                String societyID = session.getSocietyid();
                System.out.println("Finger "+finger);
                System.out.println("token"+token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("StaffID",uid));

                // String finger=b.toString();
                //finger.substring(1);
                params.add(new BasicNameValuePair("SocietyID",societyID));
                params.add(new BasicNameValuePair("fingerISO",finger));
                params.add(new BasicNameValuePair("token",token));

                String url_login = getString(R.string.url) + "Staff/insertFingerData";
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
            String s=postData();
            return s;
        }

    }

    private class staffdata extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            System.out.println("result "+result);
            Log.d("on post ", "on post execute");
            try {
                String Response = "";
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " +obj);
                String success=obj.getString("success");
                JSONObject jsondata=obj.getJSONObject("response");
                JSONArray staffdata = jsondata.getJSONArray("visitors");
                System.out.println("visitor");
                JSONObject jsonstaff = staffdata.getJSONObject(0);
                System.out.println("json Staff");
                st_name.setText(jsonstaff.getString("full_name"));
                st_profile.setText(jsonstaff.getString("cat"));

                system_staff_id = jsonstaff.getString("service_prd_reg_id");
                URL = getString(R.string.imageurl) + jsonstaff.getString("photo");
                Picasso.get().load(getString(R.string.imageurl) + jsonstaff.getString("photo")).into(image1,new Callback() {;
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        image1.setImageResource(R.drawable.noimage);
                    }
                });
                if(jsonstaff.has("fingerISO"))
                {
                    staffcontent.setVisibility(View.VISIBLE);
                    alreadyregister.setVisibility(View.VISIBLE);
                }
                else {
                    alreadyregister.setVisibility(View.GONE);
                    staffcontent.setVisibility(View.VISIBLE);
                    Lfingersection.setVisibility(View.VISIBLE);
                    btnVerify.setText(getString(R.string.regiter));
                    btnClear.setText(getString(R.string.cancel));
                    SregisterStaff.setText(getString(R.string.already_registered));
                    btn_Sync.setVisibility(View.GONE);
                }
                       }
        catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public String postData() {

            String origresponseText="";
            try {
                String token = session.gettoken();
                String sid = "", mobile = "";
                String societyID = session.getSocietyid();
                System.out.println("token" + token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                if (edt_Staffid.getText().toString().length() == 0 && edt_smobile.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Enter either staff id or its contact number", Toast.LENGTH_SHORT).show();
                } else if (edt_smobile.getText().toString().length() > 0) {
                    if (edt_smobile.getText().toString().length() > 10 || edt_smobile.getText().toString().length() < 10) {
                        edt_smobile.requestFocus();
                        edt_smobile.setError("Enter Valid Mobile Number");
                    }
                    else
                    {
                        sid = edt_Staffid.getText().toString();
                        mobile = edt_smobile.getText().toString();
                        params.add(new BasicNameValuePair("SocietyID", societyID));
                        params.add(new BasicNameValuePair("token", token));
                        params.add(new BasicNameValuePair("token", token));
                        params.add(new BasicNameValuePair("StaffID", sid));
                        params.add(new BasicNameValuePair("contactNo", mobile));

                        String url_login = getString(R.string.url) + "Staff/fetchSocietyStaff";
                        JSONParser jParser = new JSONParser();
                        JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                        origresponseText = json.toString();

                    }
                } else {
                    sid = edt_Staffid.getText().toString();
                    mobile = edt_smobile.getText().toString();
                    params.add(new BasicNameValuePair("SocietyID", societyID));
                    params.add(new BasicNameValuePair("token", token));
                    params.add(new BasicNameValuePair("token", token));
                    params.add(new BasicNameValuePair("StaffID", sid));
                    params.add(new BasicNameValuePair("contactNo", mobile));

                    String url_login = getString(R.string.url) + "Staff/fetchSocietyStaff";
                    JSONParser jParser = new JSONParser();
                    JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                    origresponseText = json.toString();
                    System.out.println("Connection close");

                }
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

   /* private class SyncFingerData extends AsyncTask<String, String, String>{

        protected void onPostExecute(String result) {
            System.out.println("Sync Finger result "+result);
            Log.d("on post ", "on post execute");
            try {

                JSONObject json=new JSONObject(result);
                System.out.println("Result On Sysn "+result);
                JSONObject FingerObject = json.getJSONObject("response");
                // mCheck.setText(FingerObject.toString());
                JSONArray FingerArray = FingerObject.getJSONArray("staffdata");

                System.out.println("Length"+FingerArray.length());
                clsUser user = new clsUser();
                int retCode = 0;
                for(int i = 0 ; i < FingerArray.length(); i++)
                {
                    JSONObject obj=FingerArray.getJSONObject(i);
                    if(obj.has("fingerISO"))
                    {
                        String Staff_id = obj.getString("service_prd_reg_id");
                        String FingerServer=obj.getString("fingerISO");
                        byte[] fingerIso = Base64.decode(FingerServer,Base64.DEFAULT);

                        System.out.println("obj in Staff_id "+Staff_id);
                        // System.out.println("obj in fingerIso "+fingerIso);
                        user.uniqueId = Staff_id;
                        user.fingerIso = fingerIso;
                        retCode = database.insertUser(user);
                    }
                 }

                if (retCode == 0 || retCode == 1) {
                    System.out.println("Successfully ");
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText("SYNC Successfully");
                    tvStatus.setTextColor(getResources().getColor(R.color.GreenText));
                } else {
                    tvStatus.setVisibility(View.VISIBLE);
                    setTextonuiThread("SYNC Failed", true);
                    tvStatus.setTextColor(getResources().getColor(R.color.RedText));
                }
            }
            catch (Exception e){}

        }

        public String postData() {

            String origresponseText="";
            try {
                String society_id = session.getSocietyid();
                String token = session.gettoken();
                System.out.println("token "+token);

                System.out.println("token"+token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("SocietyID",society_id));
                params.add(new BasicNameValuePair("token",token));

                url_login = "http://13.232.206.120:8080/NewSecurityManagerWeb/Staff/fetchStaffSyncronise";
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
            String s=postData();
            return s;
        }

    }
    */

   private class getOtpFlag extends AsyncTask<String, String, String> {

       protected void onPostExecute(String result) {
           Log.d("on post ", "on post execute");
           try {
               JSONObject obj = new JSONObject(result);
               System.out.println("Result : " +obj);
               String success=obj.getString("success");
               if (success.contains("1")) {
                   JSONObject jsondata = obj.getJSONObject("response");
                   JSONObject NewsData = jsondata.getJSONObject("Setting");
                   JSONObject NewsData1 = NewsData.getJSONObject("0");
                   fingerprint_accessstatus =NewsData1.getString("fingerprint_access_status");
                   System.out.println("fingerprint_access_status : " + fingerprint_accessstatus);
                   if(fingerprint_accessstatus.equals("1"))
                   {
                       System.out.println("Test Data");
                       ls_register.setVisibility(View.INVISIBLE);
                       L_btnfetchstaff.setVisibility(View.GONE);
                       staffcontent.setVisibility(View.GONE);
                       Lfingersection.setVisibility(View.GONE);
                       alreadyregister.setVisibility(View.GONE);
                       Lerrormsg1.setVisibility(View.VISIBLE);
                   }
                   else
                   {
                       Lerrormsg1.setVisibility(View.GONE);
                   }
               }


           }
           catch(Exception e)
           {}
       }

       public String postData() {

           String origresponseText="";
           try {
               String url_login = getString(R.string.url) + "News/OtpFlag";
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

}
