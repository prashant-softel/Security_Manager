package com.softel.securitymanager.securitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.softel.securitymanager.Util.PreferenceServices;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import info.hoang8f.android.segmented.SegmentedGroup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class visitor_unit extends AppCompatActivity {


    ArrayList<HashMap<String, String>> flats;
    String wingID = "";


    private static final int REQUEST_PICK_PHOTO = 2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;
    boolean isImageAddedexp = false;
    private static final String TAG = visitor_unit.class.getSimpleName();

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    MediaPlayer incomingmsgSoundMediaPlayerapproved = null;
    MediaPlayer incomingmsgSoundMediaPlayerdenied = null;


    public boolean isDataLoaded= false;

    private Uri fileUri;
    private String postPath;
    private String mediaPath;
    Uri outputUri;
    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    Thread t;

    private Spinner vWingSpinner, vWingSpinner1, vWingSpinner2, vWingSpinner3;
    private Spinner vUnitSpinner, vUnitSpinner1, vUnitSpinner2, vUnitSpinner3;
    private Button vSelectButton;

    private LinearLayout Lsendforapproval, Lsendforapproval1, Lsendforapproval2, Lsendforapproval3;
    String sendapprovcount = "0", sendapprovcount1 = "0", sendapprovcount2 = "0";
    private TextView txt_sendforapproval, txt_sendforapproval1, txt_sendforapproval2, txt_sendforapproval3,txt_dndMessage,txt_dndMessage1,txt_dndMessage2,txt_dndMessage3;
    private RadioButton segButtonA1, segButtonA2, segButtonA3, segButtonB1, segButtonB2, segButtonB3, segButtonC1, segButtonC2, segButtonC3, segButtonD1, segButtonD2, segButtonD3;
    String unit1 = "", unit2 = "", unit3 = "", unit4 = "",dndMessage="";
    private TextView vAddMore;
    private TextView vDeleteUnit;
    private LinearLayout lMainLayout, lMainLayout1, lMainLayout2, documentlayout;
    private LinearLayout mContactLayout, mContactLayout1, mContactLayout2, mContactLayout3,mApprovalSection,mApprovalSection1,mApprovalSection2,mApprovalSection3;
    private RelativeLayout mreal, mreal1, mreal2, mreal3;


    private LinearLayout approvemsg, approvemsg1, approvemsg2, approvemsg3;
    private TextView txt_msg, txt_msg1, txt_msg2, txt_msg3, DocumentDetails;


    /* ---- Data Collection Section ------------------ */
    ArrayList<String> UnitSelcetion = new ArrayList<>();
    ArrayList<String> ApprovalFlag = new ArrayList<>();
    String ApprovFlag;
    private SegmentedGroup mAprrovedGroup;
    ArrayAdapter<String> dataAdapter;
    Session session;
    String tunit_id = "";
    SegmentedGroup seg;
    List<String> WingList;
    List<String> arrayListWings;
    ArrayList<DND> dndList = new ArrayList<>();

    ArrayList<UnitCusttom> UnitList = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList1 = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList2 = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList3 = new ArrayList<>();
    String vID = "", docID = "", doc_img = "";
    String unitV, wingV;
    String ownername, unit_id, unit_no, wing, OwnerContact;
    private TextView mContact, mContact1, mContact2, mContact3;
    private String contact = "", contact1 = "", contact2 = "", contact3;

    HashMap unitdataapproval = new HashMap<>();
    HashMap unitdataapproval1 = new HashMap<>();
    HashMap unitdataapproval2 = new HashMap<>();
    HashMap unitdataapproval3 = new HashMap<>();

    HashMap unitdataapprovalarray = new HashMap<>();
    String visitorid;
    int i = 0, count = 0, count1 = 0, count2 = 0, count3 = 0;
    int time = 30, time1 = 30, time2 = 30, time3 = 30;
    LinearLayout UpDocLayout;
    ImageView ImageDoc;
    Button btnUpload;

    LinearLayout Layout1, Layout2; //mainlayout
    String visitor_approval_status = "";
    LinearLayout documentlayout1, UpDocLayout1, lMainLayoutw1, lMainLayoutw2, lMainLayoutw3;
    private Spinner vWingSpinnerw, vWingSpinnerw1, vWingSpinnerw2, vWingSpinnerw3;
    private Spinner vUnitSpinnerw, vUnitSpinnerw1, vUnitSpinnerw2, vUnitSpinnerw3;
    private TextView mContactw, mContactw1, mContactw2, mContactw3;
    private LinearLayout mContactLayoutw, mContactLayoutw1, mContactLayoutw2, mContactLayoutw3;
    private LinearLayout new_button, new_button2;
    private LinearLayout wing_button0, unit_button0, wing_button2, unit_button2, wing_button3, unit_button3;
    private GridLayout grid, grid0, grid2, grid3;
    private NestedScrollView scrol, scrol0, scrol2, scrol3;
    TextView unittext, unittext0, unittext2, unittext3;

    ImageView ImageDoc1;
    Button btnUpload1, btnsubmit;
    TextView DocumentDetails1, vAddMorew, vDeleteUnitw;
    private Button buttons[];
    private LinearLayout layouts[];
    private String[] wing_list, storedWingList, unit_list, unit_list0, unit_list2, unit_list3;
    private int[] wing_id_list, unit_id_list, storedWingIdList;
    private int ct;

    ProgressDialog dialog;
    int a = 0, x = 0, c = 0, b = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_unit);
        setTitle(R.string.visit_in_unit);

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_please_wait));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        PreferenceServices.init(this);
        flats = new ArrayList();

        session = new Session(getApplicationContext());
        incomingmsgSoundMediaPlayerapproved = MediaPlayer.create(this, R.raw.approved);
        incomingmsgSoundMediaPlayerdenied = MediaPlayer.create(this, R.raw.denied);
        Bundle visitdata = getIntent().getExtras();
        visitorid = visitdata.getString("visitorid");
        vID = visitdata.getString("vID");
        doc_img = visitdata.getString("Doc_img");
        System.out.println("Doc IMG : " + visitdata.getString("Doc_img"));
        docID = visitdata.getString("entryWith");
        System.out.println("Visitro ID on unit" + vID);

        new_button = (LinearLayout) findViewById(R.id.wing_button);
        wing_button0 = (LinearLayout) findViewById(R.id.wing_button0);
        wing_button2 = (LinearLayout) findViewById(R.id.wing_button2);
        wing_button3 = (LinearLayout) findViewById(R.id.wing_button3);
        new_button2 = (LinearLayout) findViewById(R.id.unit_button);
        unit_button0 = (LinearLayout) findViewById(R.id.unit_button0);
        unit_button2 = (LinearLayout) findViewById(R.id.unit_button2);
        unit_button3 = (LinearLayout) findViewById(R.id.unit_button3);
        scrol = (NestedScrollView) findViewById(R.id.scrollunit);
        scrol0 = (NestedScrollView) findViewById(R.id.scrollunit0);
        scrol2 = (NestedScrollView) findViewById(R.id.scrollunit2);
        scrol3 = (NestedScrollView) findViewById(R.id.scrollunit3);
        grid = (GridLayout) findViewById(R.id.grid);
        grid0 = (GridLayout) findViewById(R.id.grid0);
        grid2 = (GridLayout) findViewById(R.id.grid2);
        grid3 = (GridLayout) findViewById(R.id.grid3);
        unittext = (TextView) findViewById(R.id.unittext);
        unittext0 = (TextView) findViewById(R.id.unittext0);
        unittext2 = (TextView) findViewById(R.id.unittext2);
        unittext3 = (TextView) findViewById(R.id.unittext3);
        Layout1 = (LinearLayout) findViewById(R.id.Layout1);
        Layout2 = (LinearLayout) findViewById(R.id.Layout2);

        mApprovalSection = (LinearLayout) findViewById(R.id.approvalSection);
        mApprovalSection1 = (LinearLayout) findViewById(R.id.approvalSection1);
        mApprovalSection2 = (LinearLayout) findViewById(R.id.approvalSection2);
        mApprovalSection3 = (LinearLayout) findViewById(R.id.approvalSection3);


        documentlayout = (LinearLayout) findViewById(R.id.DOCUPLOAD);
        documentlayout1 = (LinearLayout) findViewById(R.id.DOCUPLOAD1);

        DocumentDetails = (TextView) findViewById(R.id.DocumentDetails);
        DocumentDetails1 = (TextView) findViewById(R.id.DocumentDetails1);

        UpDocLayout = (LinearLayout) findViewById(R.id.UpDocImageLayout);
        UpDocLayout1 = (LinearLayout) findViewById(R.id.UpDocImageLayout1);
        ImageDoc = (ImageView) findViewById(R.id.ImageDoc);
        ImageDoc1 = (ImageView) findViewById(R.id.ImageDoc1);

        btnUpload = (Button) findViewById(R.id.btndocUpload);
        btnUpload1 = (Button) findViewById(R.id.btndocUpload1);
        btnsubmit = (Button) findViewById(R.id.submitw);

        mreal = (RelativeLayout) findViewById(R.id.real);
        mreal1 = (RelativeLayout) findViewById(R.id.real1);
        mreal2 = (RelativeLayout) findViewById(R.id.real2);
        mreal3 = (RelativeLayout) findViewById(R.id.real3);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
                btnUpload.setText(getString(R.string.uploaded));
                btnUpload.setEnabled(false);
                UpDocLayout.setVisibility(View.GONE);
                btnUpload.setBackgroundColor(Color.argb(255, 133, 189, 199));

            }
        });

        btnUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
                btnUpload1.setText(getString(R.string.uploaded));
                btnUpload1.setEnabled(false);
                UpDocLayout1.setVisibility(View.GONE);
                btnUpload1.setBackgroundColor(Color.argb(255, 133, 189, 199));

            }
        });

        UpDocLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });
        UpDocLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });

        mContact = (TextView) findViewById(R.id.contactNo);
        mContact1 = (TextView) findViewById(R.id.contactNo1);
        mContact2 = (TextView) findViewById(R.id.contactNo2);
        mContact3 = (TextView) findViewById(R.id.contactNo3);

        mContactw = (TextView) findViewById(R.id.contactNow);
        mContactw1 = (TextView) findViewById(R.id.contactNow1);
        mContactw2 = (TextView) findViewById(R.id.contactNow2);
        mContactw3 = (TextView) findViewById(R.id.contactNow3);

        vWingSpinner = (Spinner) findViewById(R.id.wingSpinner);
        vWingSpinner1 = (Spinner) findViewById(R.id.wingSpinner1);
        vWingSpinner2 = (Spinner) findViewById(R.id.wingSpinner2);
        vWingSpinner3 = (Spinner) findViewById(R.id.wingSpinner3);
        vUnitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        vUnitSpinner1 = (Spinner) findViewById(R.id.unitSpinner1);
        vUnitSpinner2 = (Spinner) findViewById(R.id.unitSpinner2);
        vUnitSpinner3 = (Spinner) findViewById(R.id.unitSpinner3);

        vWingSpinnerw = (Spinner) findViewById(R.id.wingSpinnerw);
        vWingSpinnerw1 = (Spinner) findViewById(R.id.wingSpinnerw1);
        vWingSpinnerw2 = (Spinner) findViewById(R.id.wingSpinnerw2);
        vWingSpinnerw3 = (Spinner) findViewById(R.id.wingSpinnerw3);
        vUnitSpinnerw = (Spinner) findViewById(R.id.unitSpinnerw);
        vUnitSpinnerw1 = (Spinner) findViewById(R.id.unitSpinnerw1);
        vUnitSpinnerw2 = (Spinner) findViewById(R.id.unitSpinnerw2);
        vUnitSpinnerw3 = (Spinner) findViewById(R.id.unitSpinnerw3);


        approvemsg = (LinearLayout) findViewById(R.id.approvemsg);
        approvemsg1 = (LinearLayout) findViewById(R.id.approvemsg1);
        approvemsg2 = (LinearLayout) findViewById(R.id.approvemsg2);
        approvemsg3 = (LinearLayout) findViewById(R.id.approvemsg3);

        txt_msg = (TextView) findViewById(R.id.msg);
        txt_msg1 = (TextView) findViewById(R.id.msg1);
        txt_msg2 = (TextView) findViewById(R.id.msg2);
        txt_msg3 = (TextView) findViewById(R.id.msg3);


        seg = (SegmentedGroup) findViewById(R.id.segmented);
        segButtonA1 = (RadioButton) findViewById(R.id.segButtonA1);
        segButtonA2 = (RadioButton) findViewById(R.id.segButtonA2);
        segButtonA3 = (RadioButton) findViewById(R.id.segButtonA3);
        Lsendforapproval = (LinearLayout) findViewById(R.id.Lsendforapproval);
        txt_sendforapproval = (TextView) findViewById(R.id.txtsendforproposal);

        segButtonB1 = (RadioButton) findViewById(R.id.segButtonB1);
        segButtonB2 = (RadioButton) findViewById(R.id.segButtonB2);
        segButtonB3 = (RadioButton) findViewById(R.id.segButtonB3);
        Lsendforapproval1 = (LinearLayout) findViewById(R.id.Lsendforapproval1);
        txt_sendforapproval1 = (TextView) findViewById(R.id.txtsendforproposal1);

        segButtonC1 = (RadioButton) findViewById(R.id.segButtonC1);
        segButtonC2 = (RadioButton) findViewById(R.id.segButtonC2);
        segButtonC3 = (RadioButton) findViewById(R.id.segButtonC3);
        Lsendforapproval2 = (LinearLayout) findViewById(R.id.Lsendforapproval2);
        txt_sendforapproval2 = (TextView) findViewById(R.id.txtsendforproposal2);

        segButtonD1 = (RadioButton) findViewById(R.id.segButtonD1);
        segButtonD2 = (RadioButton) findViewById(R.id.segButtonD2);
        segButtonD3 = (RadioButton) findViewById(R.id.segButtonD3);
        Lsendforapproval3 = (LinearLayout) findViewById(R.id.Lsendforapproval3);
//        Ldnd_msg = (LinearLayout) findViewById(R.id.dnd_msg);

        txt_sendforapproval3 = (TextView) findViewById(R.id.txtsendforproposal3);

        txt_dndMessage = (TextView) findViewById(R.id.txtsdndMessage);
        txt_dndMessage1 = (TextView) findViewById(R.id.txtsdndMessage1);
        txt_dndMessage2 = (TextView) findViewById(R.id.txtsdndMessage2);
        txt_dndMessage3 = (TextView) findViewById(R.id.txtsdndMessage3);

        mContactLayout = (LinearLayout) findViewById(R.id.ContactLayout);
        mContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                System.out.println(mContact.getText().toString());
                callIntent.setData(Uri.parse("tel:" + contact));
                startActivity(callIntent);

            }
        });
        mContactLayout1 = (LinearLayout) findViewById(R.id.ContactLayout1);
        mContactLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact1));
                startActivity(callIntent);

            }
        });
        mContactLayout2 = (LinearLayout) findViewById(R.id.ContactLayout2);
        mContactLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact2));
                startActivity(callIntent);

            }
        });
        mContactLayout3 = (LinearLayout) findViewById(R.id.ContactLayout3);
        mContactLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact3));
                startActivity(callIntent);

            }
        });

        mContactLayoutw = (LinearLayout) findViewById(R.id.ContactLayoutw);
        mContactLayoutw1 = (LinearLayout) findViewById(R.id.ContactLayoutw1);
        mContactLayoutw2 = (LinearLayout) findViewById(R.id.ContactLayoutw2);
        mContactLayoutw3 = (LinearLayout) findViewById(R.id.ContactLayoutw3);
        mContactLayoutw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact));
                startActivity(callIntent);

            }
        });
        mContactLayoutw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact1));
                startActivity(callIntent);

            }
        });
        mContactLayoutw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact2));
                startActivity(callIntent);

            }
        });
        mContactLayoutw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Call");

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact3));
                startActivity(callIntent);

            }
        });
        txt_sendforapproval.setClickable(true);
        txt_sendforapproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_sendforapproval.setEnabled(false);

                //if(txt_sendforapproval.getText().toString().equals("Send For Approval")) {
                new submitunit().execute();
                //}
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txt_sendforapproval.setText(getString(R.string.send_for_approval) + " (0:" + checkDigit(time) + ")");
                        time--;
                    }

                    private String checkDigit(int i) {
                        return i <= 9 ? "0" + i : String.valueOf(i);

                    }

                    public void onFinish() {
                        txt_sendforapproval.setEnabled(true);
                        count++;
                        if (count == 1) {

                            txt_sendforapproval.setText(getString(R.string.send_for_approval));
                            time = 30;
                        } else {

                            txt_sendforapproval.setText(getString(R.string.re_send_for_approval));
                            time = 30;
                        }
                        //txt_sendforapproval.setText("Re-Send For Approval");
                        //txt_sendforapproval.setClickable(false);
                        //txt_sendforapproval.setBackgroundColor(Color.argb(255,224,224,224));
                        //txt_sendforapproval.setTextColor(Color.argb(255 ,72, 138, 255));
                        new checkApproval().execute(unit1, "0");
                    }

                }.start();
            }
        });

        txt_sendforapproval1.setClickable(true);
        txt_sendforapproval1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txt_sendforapproval1.setEnabled(false);
                vDeleteUnit.setVisibility(View.GONE);

                sendapprovcount = "1";
                System.out.println("sendapprovcount " + sendapprovcount);

                //if(txt_sendforapproval1.getText().toString().equals("Send For Approval")) {
                new submitunit().execute();
                //}
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txt_sendforapproval1.setText(getString(R.string.send_for_approval) + " (0:" + checkDigit(time1) + ")");
                        time1--;
                    }

                    private String checkDigit(int i) {
                        return i <= 9 ? "0" + i : String.valueOf(i);

                    }

                    public void onFinish() {
                        txt_sendforapproval1.setEnabled(true);
                        count1++;
                        if (count1 == 1) {

                            txt_sendforapproval1.setText(getString(R.string.send_for_approval));
                            time1 = 30;
                        } else {

                            txt_sendforapproval1.setText(getString(R.string.re_send_for_approval));
                            time1 = 30;
                        }
                        new checkApproval().execute(unit2, "1");
                    }

                }.start();
            }
        });

        txt_sendforapproval2.setClickable(true);
        txt_sendforapproval2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txt_sendforapproval2.setEnabled(false);
                vDeleteUnit.setVisibility(View.GONE);
                sendapprovcount1 = "1";
                System.out.println("sendapprovcount1 " + sendapprovcount1);

                //if(txt_sendforapproval2.getText().toString().equals("Send For Approval")) {
                new submitunit().execute();
                //}
                new CountDownTimer(50000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txt_sendforapproval2.setText(getString(R.string.send_for_approval) + " (0:" + checkDigit(time2) + ")");
                        time2--;
                    }

                    private String checkDigit(int i) {
                        return i <= 9 ? "0" + i : String.valueOf(i);

                    }

                    public void onFinish() {
                        txt_sendforapproval2.setEnabled(true);
                        count2++;
                        if (count2 == 1) {

                            txt_sendforapproval2.setText(getString(R.string.send_for_approval));
                            time2 = 30;
                        } else {

                            txt_sendforapproval2.setText(getString(R.string.re_send_for_approval));
                            time2 = 30;
                        }
                        new checkApproval().execute(unit3, "2");
                    }

                }.start();
            }
        });

        txt_sendforapproval3.setClickable(true);
        txt_sendforapproval3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txt_sendforapproval3.setEnabled(false);
                vDeleteUnit.setVisibility(View.GONE);
                sendapprovcount2 = "1";
                System.out.println("sendapprovcount2 " + sendapprovcount2);
                //if(txt_sendforapproval3.getText().toString().equals("Send For Approval")) {
                new submitunit().execute();
                //}
                new CountDownTimer(50000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        txt_sendforapproval3.setText(getString(R.string.send_for_approval) + " (0:" + checkDigit(time3) + ")");
                        time3--;
                    }

                    private String checkDigit(int i) {
                        return i <= 9 ? "0" + i : String.valueOf(i);

                    }

                    public void onFinish() {
                        txt_sendforapproval3.setEnabled(true);
                        count3++;
                        if (count3 == 1) {

                            txt_sendforapproval3.setText(getString(R.string.send_for_approval));
                            time3 = 30;
                        } else {

                            txt_sendforapproval3.setText(getString(R.string.re_send_for_approval));
                            time3 = 30;
                        }
                        new checkApproval().execute(unit4, "3");
                    }

                }.start();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnsubmit.setEnabled(false);
                //System.out.println("UNITSSSSS;   "+unit1+" "+unit2+" "+unit3+" "+unit4);
                if (!unit1.equals("")) UnitSelcetion.add(unit1);
                if (!unit2.equals("")) UnitSelcetion.add(unit2);
                if (!unit3.equals("")) UnitSelcetion.add(unit3);
                if (!unit4.equals("")) UnitSelcetion.add(unit4);
                new submitdataw().execute();
                btnsubmit.setEnabled(true);

            }
        });

        vSelectButton = (Button) findViewById(R.id.submit);
        vSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.show();
                new submitunitdata().execute();
            }
        });
        // vCancleButton=(Button) findViewById(R.id.cancle);
        vAddMore = (TextView) findViewById(R.id.addUnit);
        vDeleteUnit = (TextView) findViewById(R.id.DeleteUnit);

        vAddMorew = (TextView) findViewById(R.id.addUnitw);
        vDeleteUnitw = (TextView) findViewById(R.id.DeleteUnitw);
        lMainLayoutw1 = (LinearLayout) findViewById(R.id.MainLayoutw1);
        lMainLayoutw2 = (LinearLayout) findViewById(R.id.MainLayoutw2);
        lMainLayoutw3 = (LinearLayout) findViewById(R.id.MainLayoutw3);
        vDeleteUnitw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 1) {
                    lMainLayoutw1.setVisibility(View.GONE);
                    vDeleteUnitw.setVisibility(View.GONE);
                    i--;
                }
                if (i == 2) {
                    lMainLayoutw2.setVisibility(View.GONE);
                    i--;
                }
                if (i == 3) {

                    lMainLayoutw3.setVisibility(View.GONE);
                    i--;
                }
                if (i > 3) {
                    Toast.makeText(getApplicationContext(), "Error while trying to remove element", Toast.LENGTH_SHORT).show();
                }
            }
        });


        vAddMorew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i == 1) {

                    lMainLayoutw1.setVisibility(View.VISIBLE);
                    // vDeleteUnitw.setVisibility(View.VISIBLE);
                    System.out.println("Approval Flag" + ApprovalFlag);
                }
                if (i == 2) {

                    lMainLayoutw2.setVisibility(View.VISIBLE);

                    System.out.println("Approval Flag" + ApprovalFlag);
                }
                if (i == 3) {

                    lMainLayoutw3.setVisibility(View.VISIBLE);
                    System.out.println("Approval Flag" + ApprovalFlag);
                    vAddMorew.setVisibility(View.GONE);
                }
                if (i > 3) {
                    Toast.makeText(getApplicationContext(), "At a time visitor can visit only 4 flat", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lMainLayout = (LinearLayout) findViewById(R.id.MainLayout1);
        lMainLayout1 = (LinearLayout) findViewById(R.id.MainLayout2);
        lMainLayout2 = (LinearLayout) findViewById(R.id.MainLayout3);
        vDeleteUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vAddMore.setVisibility(View.VISIBLE);
                if (i == 1) {
                    lMainLayout.setVisibility(View.GONE);
                    vDeleteUnit.setVisibility(View.GONE);
                    i--;
                }
                if (i == 2) {
                    System.out.println("Testing : " + sendapprovcount + " " + sendapprovcount1 + " " + sendapprovcount2);
                    if (sendapprovcount.equals("1")) {
                        vDeleteUnit.setVisibility(View.GONE);
                    }
                    lMainLayout1.setVisibility(View.GONE);
                    i--;
                }
                if (i == 3) {
                    System.out.println("Testing1 : " + sendapprovcount + " " + sendapprovcount1 + " " + sendapprovcount2);
                    if (sendapprovcount1.equals("1")) {
                        vDeleteUnit.setVisibility(View.GONE);
                    }
                    lMainLayout2.setVisibility(View.GONE);
                    i--;
                }
                if (i > 3) {
                    Toast.makeText(getApplicationContext(), "Error while trying to remove element", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i == 1) {

                    lMainLayout.setVisibility(View.VISIBLE);
                    //new_button.setVisibility(View.VISIBLE);
                    vDeleteUnit.setVisibility(View.VISIBLE);
                    System.out.println("Approval Flag" + ApprovalFlag);
                }
                if (i == 2) {
                    lMainLayout1.setVisibility(View.VISIBLE);
                    vDeleteUnit.setVisibility(View.VISIBLE);
                    System.out.println("Approval Flag" + ApprovalFlag);
                }
                if (i == 3) {
                    lMainLayout2.setVisibility(View.VISIBLE);
                    vDeleteUnit.setVisibility(View.VISIBLE);
                    System.out.println("Approval Flag" + ApprovalFlag);
                    vAddMore.setVisibility(View.GONE);
                }
                if (i > 3) {

                    System.out.println("Approval Flag" + ApprovalFlag);
                    Toast.makeText(getApplicationContext(), "At a time visitor can visit only 4 flat", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* --------------------------------------  WIng Section -------------------  */

        //vWingSpinner.setBackgroundResource(R.drawable.ic_launcher_background);
        vWingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    wingV = parent.getItemAtPosition(position).toString();
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    wingV = parent.getItemAtPosition(position).toString();

                    // System.out.println("item" +wing+ "position"+position);
                    new getUnit().execute();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    wingV = parent.getItemAtPosition(position).toString();

                } else {
                    wingV = parent.getItemAtPosition(position).toString();

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);
                    new getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);

                    new getUnit().execute(String.valueOf(position));

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);
                    new getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /* ------------------------------------  Unit Selection --------------------------- */
        vUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        // UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit1 = unit_id;
                    }

                    contact = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContact.setText(contactno);
                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {

                } else {

                    UnitCusttom unit = UnitList1.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit2 = unit_id;
                    }

                    contact1 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContact1.setText(contactno);

                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList2.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit3 = unit_id;
                    }
                    //System.out.println(UnitSelcetion);

                    contact2 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContact2.setText(contactno);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList3.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit4 = unit_id;
                    }

                    contact3 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContact3.setText(contactno);
                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vWingSpinnerw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    wingV = parent.getItemAtPosition(position).toString();
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    wingV = parent.getItemAtPosition(position).toString();

                    // System.out.println("item" +wing+ "position"+position);
                    new getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinnerw1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    wingV = parent.getItemAtPosition(position).toString();

                } else {
                    wingV = parent.getItemAtPosition(position).toString();

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);
                    new getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinnerw2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);

                    new getUnit().execute(String.valueOf(position));

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinnerw3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_wing))) {
                    // do nothing
                } else {

                    wing = parent.getItemAtPosition(position).toString();
                    System.out.println("item" + wing + "position" + position);
                    new getUnit().execute(wing);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /* ------------------------------------  Unit Selection --------------------------- */
        vUnitSpinnerw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit1 = unit_id;
                    }

                    contact = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContactw.setText(contactno);
                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinnerw1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {

                } else {

                    UnitCusttom unit = UnitList1.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit2 = unit_id;
                    }

                    contact1 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContactw1.setText(contactno);

                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinnerw2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList2.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        // UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit3 = unit_id;
                    }
                    //System.out.println(UnitSelcetion);

                    contact2 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContactw2.setText(contactno);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinnerw3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    // do nothing
                } else {

                    UnitCusttom unit = UnitList3.get(position);
                    ownername = unit.getOwnerName();
                    unit_id = unit.getUnitId();
                    unit_no = unit.getUnitNo();
                    OwnerContact = unit.getOwnerContact();
                    if (unit_id.equals("-1")) {

                    } else {
                        //UnitSelcetion.add(unit_id);
                        tunit_id = unit_id;
                        unit4 = unit_id;
                    }

                    contact3 = OwnerContact;
                    String contactno = "";
                    if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                        System.out.println("Test Contact");
                        String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                        int length = OwnerContact.length() - 4;
                        System.out.println(length);

                        for (int i = 0; i < length; i++) {
                            contactno += "X";
                        }
                        contactno += lastFourDigits;
                        System.out.println("Contact : " + contactno);
                    } else {
                        contactno = OwnerContact;
                    }
                    mContactw3.setText(contactno);
                    // System.out.println(UnitSelcetion);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getOtpFlag().execute();
        if (PreferenceServices.getInstance().getWing("Wing") == null) {
            new getWing().execute();
        } else {
            storedWingList = new String[PreferenceServices.getInstance().getWingName("WingName").length];
            storedWingIdList = new int[PreferenceServices.getInstance().getWingId("WingId").length];
            for (int i = 0; i < storedWingList.length; i++) {
                storedWingList[i] = PreferenceServices.getInstance().getWingName("WingName")[i];
                storedWingIdList[i] = PreferenceServices.getInstance().getWingId("WingId")[i];
            }
            arrayListWings = PreferenceServices.getInstance().getWing("Wing");
            displayWing();
        }
    }

    public void ApprovedVisitor(View view) {
        ApprovFlag = "1";
        ApprovalFlag.add("1");
        unitdataapproval.put("unitdata", unit1 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval);
        unitdataapprovalarray.put("0", unitdataapproval);
    }

    public void WitoutAppVisitor(View view) {
        ApprovFlag = "3";
        ApprovalFlag.add("3");
        unitdataapproval.put("unitdata", unit1 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval);
        unitdataapprovalarray.put("0", unitdataapproval);
    }

    public void DeniedVisitor(View view) {
        ApprovFlag = "2";
        ApprovalFlag.add("2");
        unitdataapproval.put("unitdata", unit1 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval);
        unitdataapprovalarray.put("0", unitdataapproval);
    }

    public void ApprovedVisitor1(View view) {
        ApprovFlag = "1";
        ApprovalFlag.add("1");
        unitdataapproval1.put("unitdata", unit2 + ";" + ApprovFlag);
        unitdataapprovalarray.put("1", unitdataapproval1);
    }

    public void WitoutAppVisitor1(View view) {
        ApprovFlag = "3";
        ApprovalFlag.add("3");
        unitdataapproval1.put("unitdata", unit2 + ";" + ApprovFlag);
        unitdataapprovalarray.put("1", unitdataapproval1);
    }

    public void DeniedVisitor1(View view) {
        ApprovFlag = "2";
        ApprovalFlag.add("2");
        unitdataapproval1.put("unitdata", unit2 + ";" + ApprovFlag);
        unitdataapprovalarray.put("1", unitdataapproval1);
    }

    public void ApprovedVisitor2(View view) {
        ApprovFlag = "1";
        ApprovalFlag.add("1");
        unitdataapproval2.put("unitdata", unit3 + ";" + ApprovFlag);
        unitdataapprovalarray.put("2", unitdataapproval2);
    }

    public void WitoutAppVisitor2(View view) {
        ApprovFlag = "3";
        ApprovalFlag.add("3");
        unitdataapproval2.put("unitdata", unit3 + ";" + ApprovFlag);
        unitdataapprovalarray.put("2", unitdataapproval2);
    }

    public void DeniedVisitor2(View view) {
        ApprovFlag = "2";
        ApprovalFlag.add("2");
        unitdataapproval2.put("unitdata", unit3 + ";" + ApprovFlag);
        unitdataapprovalarray.put("2", unitdataapproval2);
    }

    public void ApprovedVisitor3(View view) {
        ApprovFlag = "1";
        ApprovalFlag.add("1");
        unitdataapproval.put("unitdata", unit4 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval);
        unitdataapprovalarray.put("3", unitdataapproval);
    }

    public void WitoutAppVisitor3(View view) {
        ApprovFlag = "3";
        ApprovalFlag.add("3");
        unitdataapproval3.put("unitdata", unit4 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval3);
        unitdataapprovalarray.put("3", unitdataapproval3);
    }

    public void DeniedVisitor3(View view) {
        ApprovFlag = "2";
        ApprovalFlag.add("2");
        unitdataapproval3.put("unitdata", unit4 + ";" + ApprovFlag);
        System.out.println(" " + unitdataapproval3);
        unitdataapprovalarray.put("3", unitdataapproval3);
    }

    public void unitget(int id, int p) {
        wingID = String.valueOf(id);
        Log.e("PPPPPPPPPPPPPP", String.valueOf(p));
        Log.e("wingID", String.valueOf(wingID));
        i = p;
        x = 0;
        a = 0;
        b = 0;
        c = 0;
        if (PreferenceServices.getInstance().getFlats().isEmpty() || !isDataLoaded ) {
            new getUnit().execute(String.valueOf(id));
        } else {
            displayFlats(wingID);
        }
    }

    public void displayWing() {
        new_button.removeAllViews();
        wing_button0.removeAllViews();
        wing_button2.removeAllViews();
        wing_button3.removeAllViews();
        new_button.setVisibility(View.VISIBLE);
        wing_button0.setVisibility(View.VISIBLE);
        wing_button2.setVisibility(View.VISIBLE);
        wing_button3.setVisibility(View.VISIBLE);
        buttons = new Button[storedWingList.length];
        for (int i = 0; i < storedWingList.length; i++) {
            buttons[i] = new Button(visitor_unit.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.width), (int) getResources().getDimension(R.dimen.height), 1.0f);
            buttons[i].setLayoutParams(lp);
            buttons[i].setText(storedWingList[i]);
            buttons[i].setId(storedWingIdList[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int id = v.getId();
                    //i=2;
                    //Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    unitget(id, 1);

                }
            });
            new_button.addView(buttons[i]);
            Log.e("button[i]", String.valueOf(buttons[i].toString().length()));
        }
        for (int i = 0; i < storedWingList.length; i++) {
            buttons[i] = new Button(visitor_unit.this);

            //final Button button = new Button(visitor_unit.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.width), (int) getResources().getDimension(R.dimen.height), 1.0f);
            buttons[i].setLayoutParams(lp);
            buttons[i].setText(storedWingList[i]);
            buttons[i].setId(storedWingIdList[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int id = v.getId();
                    //i=2;
                    //Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    unitget(id, 0);

                }
            });
            wing_button0.addView(buttons[i]);
        }
        for (int i = 0; i < storedWingList.length; i++) {

            buttons[i] = new Button(visitor_unit.this);

            //final Button button = new Button(visitor_unit.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.width), (int) getResources().getDimension(R.dimen.height), 1.0f);
            buttons[i].setLayoutParams(lp);
            buttons[i].setText(storedWingList[i]);
            buttons[i].setId(storedWingIdList[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int id = v.getId();
                    //i=2;
                    //Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    unitget(id, 2);
                }
            });
            wing_button2.addView(buttons[i]);
        }
        for (int i = 0; i < storedWingList.length; i++) {


            buttons[i] = new Button(visitor_unit.this);

            //final Button button = new Button(visitor_unit.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.width), (int) getResources().getDimension(R.dimen.height), 1.0f);
            buttons[i].setLayoutParams(lp);
            buttons[i].setText(storedWingList[i]);
            buttons[i].setId(storedWingIdList[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int id = v.getId();
                    //i=2;
                    //Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                    unitget(id, 3);

                }
            });
            wing_button3.addView(buttons[i]);
        }
        dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, arrayListWings);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        if (visitor_approval_status.equals("0")) {
            vWingSpinner.setAdapter(dataAdapter);
            vWingSpinner1.setAdapter(dataAdapter);
            vWingSpinner2.setAdapter(dataAdapter);
            vWingSpinner3.setAdapter(dataAdapter);
        }
        if (visitor_approval_status.equals("1")) {
            System.out.println("Wing 1");
            vWingSpinnerw.setAdapter(dataAdapter);
            vWingSpinnerw1.setAdapter(dataAdapter);
            vWingSpinnerw2.setAdapter(dataAdapter);
            vWingSpinnerw3.setAdapter(dataAdapter);
        }

    }

    public void displayFlats(String wingID) {
        ct = PreferenceServices.getInstance().getFlats().length();

        if (i == 1) grid.removeAllViews();
        if (i == 0) grid0.removeAllViews();
        if (i == 2) grid2.removeAllViews();
        if (i == 3) grid3.removeAllViews();
        if (i == 1) unit_list = new String[ct];
        if (i == 0) unit_list0 = new String[ct];
        if (i == 2) unit_list2 = new String[ct];
        if (i == 3) unit_list3 = new String[ct];

        if (i == 1) {
            UnitList1.clear();
            unit_list[a] = "Office";
            a++;
            UnitList1.add(new UnitCusttom("-1", getString(R.string.select_unit), "", "", "","",""));
            UnitList1.add(new UnitCusttom("0", "Society-Office", "", session.getEmegency(), "","",""));
        } else if (i == 2) {
            UnitList2.clear();
            unit_list2[b] = "Office";
            b++;
            UnitList2.add(new UnitCusttom("-1", getString(R.string.select_unit), "", "", "","",""));
            UnitList2.add(new UnitCusttom("0", "Society-Office", "", session.getEmegency(), "","",""));
        } else if (i == 3) {
            UnitList3.clear();
            unit_list3[c] = "Office";
            c++;
            UnitList3.add(new UnitCusttom("-1", getString(R.string.select_unit), "", "", "","",""));
            UnitList3.add(new UnitCusttom("0", "Society-Office", "", session.getEmegency(), "","",""));
        } else {
            UnitList.clear();
            unit_list0[x] = "Office";
            x++;
            UnitList.add(new UnitCusttom("-1", getString(R.string.select_unit), "", "", "","",""));
            UnitList.add(new UnitCusttom("0", "Society-Office", "", session.getEmegency(), "","",""));
        }

        try {
            JSONObject object = new JSONObject(PreferenceServices.getInstance().getFlats());
            if (object.getString("success").equals("1")) {
                JSONObject jsonObject = object.getJSONObject("response");
                JSONArray jArrayValue = new JSONArray(jsonObject.getString("member"));
                for (int k = 0; k < jArrayValue.length(); k++) {
                    JSONObject main = jArrayValue.getJSONObject(k);

                    UnitCusttom unitCusttom = new UnitCusttom();
                    unitCusttom.setWingId(main.getString("wing_id"));
                    unitCusttom.setUnitId(main.getString("unit_id"));
                    unitCusttom.setUnitNo(main.getString("unit_no"));
                    unitCusttom.setOwnerContact(main.getString("mobile_no"));

                    if (i == 1) {
                        if (wingID.equalsIgnoreCase(String.valueOf(main.get("wing_id")))) {
                            String unit = String.valueOf(main.get("unit_no"));
                            unit_list[a] = unit;
                            a++;
                            unitCusttom.setName(main.getString("name").trim());
                            unitCusttom.setTenant_Name(main.getString("tenant_name").trim());
                            unitCusttom.setOwnerName(main.getString("owner_name").trim());

                            UnitList1.add(unitCusttom);
                        }
                    } else if (i == 2) {
                        if (wingID.equalsIgnoreCase(String.valueOf(main.get("wing_id")))) {
                            String unit = String.valueOf(main.get("unit_no"));
                            unit_list2[b] = unit;
                            b++;

                            unitCusttom.setName(main.getString("name").trim());
                            unitCusttom.setTenant_Name(main.getString("tenant_name").trim());
                            unitCusttom.setOwnerName(main.getString("owner_name").trim());

                            UnitList2.add(unitCusttom);

                        }
                    } else if (i == 3) {
                        if (wingID.equalsIgnoreCase(String.valueOf(main.get("wing_id")))) {
                            String unit = String.valueOf(main.get("unit_no"));
                            unit_list3[c] = unit;
                            c++;
                            unitCusttom.setName(main.getString("name").trim());
                            unitCusttom.setTenant_Name(main.getString("tenant_name").trim());
                            unitCusttom.setOwnerName(main.getString("owner_name").trim());
                            UnitList3.add(unitCusttom);
                        }

                    } else {
                        if (wingID.equalsIgnoreCase(main.get("wing_id").toString())) {
                            String unit = String.valueOf(main.get("unit_no"));
                            unit_list0[x] = unit;
                            x++;
                            unitCusttom.setName(main.getString("name").trim());
                            unitCusttom.setTenant_Name(main.getString("tenant_name").trim());
                            unitCusttom.setOwnerName(main.getString("owner_name").trim());
                            UnitList.add(unitCusttom);
                        }
                    }
                }

                new_button2.setVisibility(View.VISIBLE);
                unit_button0.setVisibility(View.VISIBLE);
                unit_button2.setVisibility(View.VISIBLE);
                unit_button3.setVisibility(View.VISIBLE);
                if (i == 1) grid.removeAllViews();
                if (i == 0) grid0.removeAllViews();
                if (i == 2) grid2.removeAllViews();
                if (i == 3) grid3.removeAllViews();
                int col = 4;

                if (i == 1) {
                    buttons = new Button[a];
                    grid.setColumnCount(col);
                    LinearLayout verticalLp = new LinearLayout(this);
                    LinearLayout.LayoutParams verticalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    verticalLp.setOrientation(LinearLayout.VERTICAL);
                    verticalLp.setLayoutParams(verticalParam);

                    LinearLayout lp = new LinearLayout(this);
                    LinearLayout.LayoutParams params;

                    for (int i = 0; i < a; i++) {
                        buttons[i] = new Button(visitor_unit.this);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 150);
                        params1.weight = 1f;
                        buttons[i].setLayoutParams(params1);
                        buttons[i].setText(unit_list[i]);
                        buttons[i].setId(i);

                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final int id = v.getId();
                                UnitCusttom unit = UnitList1.get(id + 1);
                                ownername = unit.getOwnerName();
                                unit_id = unit.getUnitId();
                                unit_no = unit.getUnitNo();
                                OwnerContact = unit.getOwnerContact();
                                tunit_id = unit_id;
                                unit2 = unit_id;
                                dndMessage="";
                                mContactLayoutw1.setVisibility(View.VISIBLE);
                                mApprovalSection1.setVisibility(View.VISIBLE);
                                txt_dndMessage1.setText(dndMessage);
                                for( DND a : dndList) {
                                    String UNIT_NO_AS_STRING = Integer.toString(a.getUnit_no());
                                    String UNIT_ID_AS_STRING = Integer.toString(a.getUnit_id());
                                    if (UNIT_NO_AS_STRING.equals(unit.getUnitNo()) && UNIT_ID_AS_STRING.equals(unit.getUnitId())) {
                                        dndMessage=  a.getDnd_msg();
                                        txt_dndMessage1.setText(a.getDnd_msg());
                                        mContactLayoutw1.setVisibility(View.GONE);
                                        mApprovalSection1.setVisibility(View.GONE);
                                    }
                                }
                                contact1 = OwnerContact;
                                String contactno = "";
                                if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {

                                    String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                                    int length = OwnerContact.length() - 4;

                                    for (int i = 0; i < length; i++) {
                                        contactno += "X";
                                    }
                                    contactno += lastFourDigits;

                                } else {
                                    contactno = OwnerContact;
                                }
                                mContactw1.setText(contactno);

                                if (!unit.getTenant_Name().equalsIgnoreCase("")) {
                                    unittext.setText(unit.getTenant_Name() + " (T)");
                                } else if(!unit.getName().equalsIgnoreCase("")) {
                                    unittext.setText(unit.getName());
                                }else {
                                    unittext.setText(unit.getOwnerName());
                                }

                            }
                        });
                        if (i % 4 == 0 || i == 0) {
                            lp = new LinearLayout(this);
                            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setWeightSum(4);
                            lp.setLayoutParams(params);
                            lp.setOrientation(LinearLayout.HORIZONTAL);
                            verticalLp.addView(lp);
                        }
                        lp.addView(buttons[i]);

                    }
                    grid.addView(verticalLp);

                }
                //first
                if (i == 0) {
                    Log.e("XXXXXXX", String.valueOf(x));
                    buttons = new Button[x];
                    grid0.setColumnCount(col);

                    LinearLayout verticalLp = new LinearLayout(this);
                    LinearLayout.LayoutParams verticalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    verticalLp.setOrientation(LinearLayout.VERTICAL);
                    verticalLp.setLayoutParams(verticalParam);

                    LinearLayout lp = new LinearLayout(this);
                    LinearLayout.LayoutParams params;
                    for (int i = 0; i < x; i++) {
                        buttons[i] = new Button(visitor_unit.this);
                        //final Button button = new Button(visitor_unit.this);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 150);
                        params1.weight = 1f;
                        buttons[i].setLayoutParams(params1);
                        buttons[i].setText(unit_list0[i]);
                        buttons[i].setId(i);
                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final int id = v.getId();
                                //i=2;
                               // Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                                UnitCusttom unit = UnitList.get(id + 1);
                                ownername = unit.getOwnerName();
                                unit_id = unit.getUnitId();
                                unit_no = unit.getUnitNo();
                                OwnerContact = unit.getOwnerContact();

                                //UnitSelcetion.add(unit_id);
                                tunit_id = unit_id;
                                unit1 = unit_id;
                                dndMessage="";
                                mContactLayoutw.setVisibility(View.VISIBLE);
                                mApprovalSection.setVisibility(View.VISIBLE);
                                txt_dndMessage.setText(dndMessage);
                                for( DND a : dndList) {
                                    String UNIT_NO_AS_STRING = Integer.toString(a.getUnit_no());
                                    String UNIT_ID_AS_STRING = Integer.toString(a.getUnit_id());
                                    if (UNIT_NO_AS_STRING.equals(unit.getUnitNo()) && UNIT_ID_AS_STRING.equals(unit.getUnitId())) {
                                        dndMessage=  a.getDnd_msg();
                                        txt_dndMessage.setText(a.getDnd_msg());
                                        mContactLayoutw.setVisibility(View.GONE);
                                        mApprovalSection.setVisibility(View.GONE);
                                    }
                                }
                                contact = OwnerContact;
                                String contactno = "";
                                if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                                    System.out.println("Test Contact");
                                    String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                                    int length = OwnerContact.length() - 4;
                                    System.out.println(length);

                                    for (int i = 0; i < length; i++) {
                                        contactno += "X";
                                    }
                                    contactno += lastFourDigits;
                                    System.out.println("Contact : " + contactno);
                                } else {
                                    contactno = OwnerContact;
                                }
                                mContactw.setText(contactno);

                                if (!unit.getTenant_Name().equalsIgnoreCase("")) {
                                    unittext0.setText(unit.getTenant_Name() + " (T)");
                                } else if(!unit.getName().equalsIgnoreCase("")) {
                                    unittext0.setText(unit.getName());
                                }else {
                                    unittext0.setText(unit.getOwnerName());
                                }
                            }
                        });
                        if (i % 4 == 0 || i == 0) {
                            lp = new LinearLayout(this);
                            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setWeightSum(4);
                            lp.setLayoutParams(params);
                            lp.setOrientation(LinearLayout.HORIZONTAL);
                            verticalLp.addView(lp);
                        }
                        lp.addView(buttons[i]);

                    }
                    grid0.addView(verticalLp);


                }

                if (i == 2) {
                    System.out.println("unit count:    " + b);
                    buttons = new Button[b];
                    grid2.setColumnCount(col);
                    LinearLayout verticalLp = new LinearLayout(this);
                    LinearLayout.LayoutParams verticalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    verticalLp.setOrientation(LinearLayout.VERTICAL);
                    verticalLp.setLayoutParams(verticalParam);

                    LinearLayout lp = new LinearLayout(this);
                    LinearLayout.LayoutParams params;

                    for (int i = 0; i < b; i++) {
                        buttons[i] = new Button(visitor_unit.this);

                        //final Button button = new Button(visitor_unit.this);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 150);
                        params1.weight = 1f;
                        buttons[i].setLayoutParams(params1);
                        buttons[i].setText(unit_list2[i]);
                        buttons[i].setId(i);
                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final int id = v.getId();
                                //i=2;
                              //  Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();

                                UnitCusttom unit = UnitList2.get(id + 1);
                                ownername = unit.getOwnerName();
                                unit_id = unit.getUnitId();
                                unit_no = unit.getUnitNo();
                                OwnerContact = unit.getOwnerContact();

                                //UnitSelcetion.add(unit_id);
                                tunit_id = unit_id;
                                unit2 = unit_id;

                                dndMessage="";
                                mContactLayoutw2.setVisibility(View.VISIBLE);
                                mApprovalSection2.setVisibility(View.VISIBLE);
                                txt_dndMessage2.setText(dndMessage);
                                for( DND a : dndList) {
                                    String UNIT_NO_AS_STRING = Integer.toString(a.getUnit_no());
                                    String UNIT_ID_AS_STRING = Integer.toString(a.getUnit_id());
                                    if (UNIT_NO_AS_STRING.equals(unit.getUnitNo()) && UNIT_ID_AS_STRING.equals(unit.getUnitId())) {
                                        dndMessage=  a.getDnd_msg();
                                        txt_dndMessage2.setText(a.getDnd_msg());
                                        mContactLayoutw2.setVisibility(View.GONE);
                                        mApprovalSection2.setVisibility(View.GONE);
                                    }
                                }


                                contact2 = OwnerContact;
                                String contactno = "";
                                if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                                    System.out.println("Test Contact");
                                    String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                                    int length = OwnerContact.length() - 4;
                                    System.out.println(length);

                                    for (int i = 0; i < length; i++) {
                                        contactno += "X";
                                    }
                                    contactno += lastFourDigits;
                                    System.out.println("Contact : " + contactno);
                                } else {
                                    contactno = OwnerContact;
                                }
                                mContactw2.setText(contactno);

                                if (!unit.getTenant_Name().equalsIgnoreCase("")) {
                                    unittext2.setText(unit.getTenant_Name() + " (T)");
                                } else if(!unit.getName().equalsIgnoreCase("")) {
                                    unittext2.setText(unit.getName());
                                }else {
                                    unittext2.setText(unit.getOwnerName());
                                }

                            }
                        });
                        if (i % 4 == 0 || i == 0) {
                            lp = new LinearLayout(this);
                            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setWeightSum(4);
                            lp.setLayoutParams(params);
                            lp.setOrientation(LinearLayout.HORIZONTAL);
                            verticalLp.addView(lp);
                        }
                        lp.addView(buttons[i]);
                    }
                    grid2.addView(verticalLp);

                }

                if (i == 3) {
                    System.out.println("unit count:    " + c);
                    buttons = new Button[c];
                    grid3.setColumnCount(col);
                    LinearLayout verticalLp = new LinearLayout(this);
                    LinearLayout.LayoutParams verticalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    verticalLp.setOrientation(LinearLayout.VERTICAL);
                    verticalLp.setLayoutParams(verticalParam);

                    LinearLayout lp = new LinearLayout(this);
                    LinearLayout.LayoutParams params;

                    for (int i = 0; i < c; i++) {
                        buttons[i] = new Button(visitor_unit.this);

                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, 150);
                        params1.weight = 1f;
                        buttons[i].setLayoutParams(params1);
                        buttons[i].setText(unit_list3[i]);
                        buttons[i].setId(i);
                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final int id = v.getId();
                                //i=2;
                               // Toast.makeText(visitor_unit.this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                                //unitget(id);

                                UnitCusttom unit = UnitList.get(id + 1);
                                ownername = unit.getOwnerName();
                                unit_id = unit.getUnitId();
                                unit_no = unit.getUnitNo();
                                OwnerContact = unit.getOwnerContact();

                                //UnitSelcetion.add(unit_id);
                                tunit_id = unit_id;
                                unit3 = unit_id;
                                dndMessage="";
                                mContactLayoutw3.setVisibility(View.VISIBLE);
                                mApprovalSection3.setVisibility(View.VISIBLE);
                                txt_dndMessage3.setText(dndMessage);
                                for( DND a : dndList) {
                                    String UNIT_NO_AS_STRING = Integer.toString(a.getUnit_no());
                                    String UNIT_ID_AS_STRING = Integer.toString(a.getUnit_id());
                                    if (UNIT_NO_AS_STRING.equals(unit.getUnitNo()) && UNIT_ID_AS_STRING.equals(unit.getUnitId())) {
                                        dndMessage=  a.getDnd_msg();
                                        txt_dndMessage3.setText(a.getDnd_msg());
                                        mContactLayoutw3.setVisibility(View.GONE);
                                        mApprovalSection3.setVisibility(View.GONE);
                                    }
                                }
                                contact3 = OwnerContact;
                                String contactno = "";
                                if (OwnerContact.length() > 0 && OwnerContact.length() > 4) {
                                    System.out.println("Test Contact");
                                    String lastFourDigits = OwnerContact.substring(OwnerContact.length() - 4);
                                    int length = OwnerContact.length() - 4;
                                    System.out.println(length);

                                    for (int i = 0; i < length; i++) {
                                        contactno += "X";
                                    }
                                    contactno += lastFourDigits;
                                    System.out.println("Contact : " + contactno);
                                } else {
                                    contactno = OwnerContact;
                                }
                                mContactw3.setText(contactno);

                                if (!unit.getTenant_Name().equalsIgnoreCase("")) {
                                    unittext3.setText(unit.getTenant_Name() + " (T)");
                                } else if(!unit.getName().equalsIgnoreCase("")) {
                                    unittext3.setText(unit.getName());
                                }else {
                                    unittext3.setText(unit.getOwnerName());
                                }


                            }
                        });
                        if (i % 4 == 0 || i == 0) {
                            lp = new LinearLayout(this);
                            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setWeightSum(4);
                            lp.setLayoutParams(params);
                            lp.setOrientation(LinearLayout.HORIZONTAL);
                            verticalLp.addView(lp);
                        }
                        lp.addView(buttons[i]);
                    }
                    grid3.addView(verticalLp);

                }

                UnitCustomAdapter adapter;
                if (i == 1) {
                    adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList1);
                } else if (i == 2) {
                    adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList2);
                } else if (i == 3) {
                    adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList3);
                } else {
                    adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList);

                }

                if (i == 1) {
                    if (visitor_approval_status.equals("0")) {
                        vUnitSpinner1.setAdapter(adapter);
                    }
                    if (visitor_approval_status.equals("1")) {
                        vUnitSpinnerw1.setAdapter(adapter);
                    }
                } else if (i == 2) {
                    if (visitor_approval_status.equals("0")) {
                        vUnitSpinner2.setAdapter(adapter);
                    }
                    if (visitor_approval_status.equals("1")) {
                        vUnitSpinnerw2.setAdapter(adapter);
                    }

                } else if (i == 3) {
                    if (visitor_approval_status.equals("0")) {
                        vUnitSpinner3.setAdapter(adapter);
                    }
                    if (visitor_approval_status.equals("1")) {
                        vUnitSpinnerw3.setAdapter(adapter);
                    }

                } else {
                    if (visitor_approval_status.equals("0")) {
                        vUnitSpinner.setAdapter(adapter);
                    }
                    if (visitor_approval_status.equals("1")) {
                        vUnitSpinnerw.setAdapter(adapter);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class getWing extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            WingList = new ArrayList<>();
            arrayListWings = new ArrayList<>();

            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray wingData = jsondata.getJSONArray("Wing");

                    WingList.add(0, getString(R.string.select_wing));
                    ct = wingData.length();
                    System.out.println("wingsize: " + ct);
                    wing_list = new String[ct];
                    wing_id_list = new int[ct];
                    for (int i = 0; i < wingData.length(); i++) {
                        JSONObject objData = wingData.getJSONObject(i);
                        WingList.add(Integer.valueOf(objData.getString("wing_id")), objData.getString("wing"));
                        wing_list[i] = objData.getString("wing");
                        wing_id_list[i] = Integer.valueOf(objData.getString("wing_id"));
                    }
                }

                PreferenceServices.getInstance().saveWingId(wing_id_list, "WingId");
                PreferenceServices.getInstance().saveWingName(wing_list, "WingName");
                PreferenceServices.getInstance().saveWing(WingList, "Wing");
                storedWingList = new String[PreferenceServices.getInstance().getWingName("WingName").length];
                storedWingIdList = new int[PreferenceServices.getInstance().getWingId("WingId").length];
                for (int i = 0; i < storedWingList.length; i++) {
                    storedWingList[i] = PreferenceServices.getInstance().getWingName("WingName")[i];
                    storedWingIdList[i] = PreferenceServices.getInstance().getWingId("WingId")[i];
                }
                arrayListWings = PreferenceServices.getInstance().getWing("Wing");
                displayWing();

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "ServiceProvider/fetchWing";
                String token = session.gettoken();
                System.out.println("token" + token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

    private class getUnit extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                Log.e("FLATSRESP:", result);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    isDataLoaded = true;
                    PreferenceServices.getInstance().setFlats(result);
                    JSONObject jsonObject = obj.getJSONObject("response");
                    JSONArray jArrayValue = new JSONArray(jsonObject.getString("member"));
                    JSONArray dndArrayValue = new JSONArray(jsonObject.getString("DND_record"));
                    for (int d = 0; d < dndArrayValue.length(); d++) {
                        JSONObject main = dndArrayValue.getJSONObject(d);
                        DND dnd = new DND();
                        dnd.setDnd_msg(main.getString("dnd_msg"));
                        dnd.setUnit_id(main.getInt("unit_id"));
                        dnd.setUnit_no(main.getInt("unit_no"));
                        dnd.setDnd_id(main.getInt("dnd_id"));
                        dnd.setDnd_type(main.getInt("dnd_type"));
                        dndList.add(dnd);
                    }

                    displayFlats(wingID);

                }
            } catch (Exception e) {
            }
        }

        public String postData(String wingID) {

            String origresponseText = "";

            try {
                String url_login = getString(R.string.url) + "ServiceProvider/fetchUnits";
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            wingID = (String) params[0];
            String s = postData(wingID);
            return s;
        }
    }

    private class submitunitdata extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                System.out.println("test");
                if (success.contains("1")) {
                    System.out.println("test1");
                    System.out.println("Vid +" + vID);
                    //JSONObject jsondata = obj.getJSONObject("response");
                    //  dialog.dismiss();
                    Intent welcomevisit=new Intent(visitor_unit.this,welcomevisit.class);
                    welcomevisit.putExtra("VisiID",vID);
                    startActivity(welcomevisit);

                }

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");
                //UnitSelcetion
                String url_login = getString(R.string.url) + "OTP/UnitApprovalnew";
                String token = session.gettoken();
                String UnitID = "";
                System.out.println("Unit : " + UnitSelcetion);
                for (String s : UnitSelcetion) {
                    UnitID += s + ",";
                }
                String unitinsubmit = "", approveflaginsubmit = "";

                UnitID = UnitID.substring(0, UnitID.length() - 1);
                System.out.println("UnitList : " + UnitID);
                Gson objGson = new Gson();
                String objStr = objGson.toJson(unitdataapprovalarray);
                System.out.println("unit Approval " + objStr);
                JSONObject udata = new JSONObject(objStr);
                if (!objStr.equals("{}")) {
                    for (int i = 0; i < 4; i++) {
                        if (udata.has(String.valueOf(i))) {
                            JSONObject objunit = udata.getJSONObject(String.valueOf(i));
                            String data = objunit.getString("unitdata");
                            System.out.println("Data : " + data);
                            String[] arraydata = data.split(";");
                            unitinsubmit += arraydata[0] + ",";
                            approveflaginsubmit += arraydata[1] + ",";
                        }
                    }
                    unitinsubmit = unitinsubmit.substring(0, unitinsubmit.length() - 1);
                    approveflaginsubmit = approveflaginsubmit.substring(0, approveflaginsubmit.length() - 1);

                }

                System.out.println("Unit : " + unitinsubmit + " App :  " + approveflaginsubmit);
                String id = session.getid();
                String name = session.getName();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("VisitorEntryID", vID));
                params.add(new BasicNameValuePair("unit_id", UnitID));
                params.add(new BasicNameValuePair("unit", unitinsubmit));
                params.add(new BasicNameValuePair("approve", approveflaginsubmit));

                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("name", name));

                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

    private class submitunit extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);


            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/SubmitUnit";
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("VisitorEntryID", vID));
                params.add(new BasicNameValuePair("visitorid", visitorid));
                params.add(new BasicNameValuePair("unit_id", tunit_id));
                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

    private class checkApproval extends AsyncTask<String, String, String> {
        String statusoflayout = "";

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                JSONObject objres = obj.getJSONObject("response");
                JSONObject objresponse = objres.getJSONObject("0");


                if (statusoflayout.equals("0")) {
                    mreal.setVisibility(View.VISIBLE);
                    mContactLayout.setVisibility(View.VISIBLE);
                    if (txt_sendforapproval.getText().toString().equals(getString(R.string.send_for_approval))) {
                        UnitSelcetion.add(objresponse.getString("unit_id"));
                    }
                    if (objresponse.getString("approvewith").equals("1")) {

                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        String status = objresponse.getString("Entry_flag");
                        if (status.equals("1")) {
                            if (objresponse.has("approvemsg") && (!objresponse.getString("approvemsg").equals("NULL"))) {
                                approvemsg.setVisibility(View.VISIBLE);
                                txt_msg.setText(objresponse.getString("approvemsg"));

                            }
                            incomingmsgSoundMediaPlayerapproved.start();
                            segButtonA1.setChecked(true);
                            Lsendforapproval.setVisibility(View.GONE);

                        }
                        if (status.equals("2")) {

                            segButtonA3.setChecked(true);
                            incomingmsgSoundMediaPlayerdenied.start();
                            Lsendforapproval.setVisibility(View.GONE);
                        }
                        if (status.equals("3")) {
                            segButtonA2.setChecked(true);
                            Lsendforapproval.setVisibility(View.GONE);
                        }
                        segButtonA1.setEnabled(false);
                        segButtonA2.setEnabled(false);
                        segButtonA3.setEnabled(false);

                    } else {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        txt_sendforapproval.setText(getString(R.string.re_send_for_approval));
                        txt_sendforapproval.setTextColor(Color.WHITE);
                        txt_sendforapproval.setBackgroundColor(Color.RED);
                    }
                }
                if (statusoflayout.equals("1")) {
                    mreal1.setVisibility(View.VISIBLE);
                    mContactLayout1.setVisibility(View.VISIBLE);
                    if (txt_sendforapproval1.getText().toString().equals(getString(R.string.send_for_approval))) {
                        UnitSelcetion.add(objresponse.getString("unit_id"));
                    }
                    if (objresponse.getString("approvewith").equals("1")) {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        String status = objresponse.getString("Entry_flag");
                        if (status.equals("1")) {
                            if (objresponse.has("approvemsg") && (!objresponse.getString("approvemsg").equals("NULL"))) {
                                approvemsg1.setVisibility(View.VISIBLE);
                                txt_msg1.setText(objresponse.getString("approvemsg"));

                            }
                            incomingmsgSoundMediaPlayerapproved.start();
                            segButtonB1.setChecked(true);
                            Lsendforapproval1.setVisibility(View.GONE);
                        }
                        if (status.equals("2")) {
                            segButtonB3.setChecked(true);
                            incomingmsgSoundMediaPlayerdenied.start();
                            Lsendforapproval1.setVisibility(View.GONE);
                        }
                        if (status.equals("3")) {
                            segButtonB2.setChecked(true);
                            Lsendforapproval1.setVisibility(View.GONE);
                        }
                        segButtonB1.setEnabled(false);
                        segButtonB2.setEnabled(false);
                        segButtonB3.setEnabled(false);

                    } else {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        txt_sendforapproval1.setText(getString(R.string.re_send_for_approval));
                        txt_sendforapproval1.setTextColor(Color.WHITE);
                        txt_sendforapproval1.setBackgroundColor(Color.RED);
                    }

                }

                if (statusoflayout.equals("2")) {
                    mreal2.setVisibility(View.VISIBLE);
                    mContactLayout2.setVisibility(View.VISIBLE);
                    if (txt_sendforapproval2.getText().toString().equals(getString(R.string.send_for_approval))) {
                        UnitSelcetion.add(objresponse.getString("unit_id"));
                    }
                    if (objresponse.getString("approvewith").equals("1")) {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        String status = objresponse.getString("Entry_flag");
                        if (status.equals("1")) {
                            if (objresponse.has("approvemsg") && (!objresponse.getString("approvemsg").equals("NULL"))) {
                                approvemsg2.setVisibility(View.VISIBLE);
                                txt_msg2.setText(objresponse.getString("approvemsg"));

                            }
                            incomingmsgSoundMediaPlayerapproved.start();
                            segButtonC1.setChecked(true);
                            Lsendforapproval2.setVisibility(View.GONE);
                        }
                        if (status.equals("2")) {
                            segButtonC3.setChecked(true);
                            incomingmsgSoundMediaPlayerdenied.start();
                            Lsendforapproval2.setVisibility(View.GONE);
                        }
                        if (status.equals("3")) {
                            segButtonC2.setChecked(true);
                            Lsendforapproval2.setVisibility(View.GONE);
                        }
                        segButtonC1.setEnabled(false);
                        segButtonC2.setEnabled(false);
                        segButtonC3.setEnabled(false);

                    } else {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        txt_sendforapproval2.setText(getString(R.string.send_for_approval));
                        txt_sendforapproval2.setTextColor(Color.WHITE);
                        txt_sendforapproval2.setBackgroundColor(Color.RED);
                    }
                }
                if (statusoflayout.equals("3")) {
                    mreal3.setVisibility(View.VISIBLE);
                    mContactLayout3.setVisibility(View.VISIBLE);
                    if (txt_sendforapproval3.getText().toString().equals(getString(R.string.send_for_approval))) {
                        UnitSelcetion.add(objresponse.getString("unit_id"));
                    }
                    if (objresponse.getString("approvewith").equals("1")) {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        String status = objresponse.getString("Entry_flag");
                        if (status.equals("1")) {
                            if (objresponse.has("approvemsg") && (!objresponse.getString("approvemsg").equals("NULL"))) {
                                approvemsg3.setVisibility(View.VISIBLE);
                                txt_msg3.setText(objresponse.getString("approvemsg"));

                            }
                            incomingmsgSoundMediaPlayerapproved.start();
                            segButtonD1.setChecked(true);
                            Lsendforapproval3.setVisibility(View.GONE);
                        }
                        if (status.equals("2")) {
                            segButtonD3.setChecked(true);
                            incomingmsgSoundMediaPlayerdenied.start();
                            Lsendforapproval3.setVisibility(View.GONE);
                        }
                        if (status.equals("3")) {
                            segButtonD2.setChecked(true);
                            Lsendforapproval3.setVisibility(View.GONE);
                        }
                        segButtonD1.setEnabled(false);
                        segButtonD2.setEnabled(false);
                        segButtonD3.setEnabled(false);

                    } else {
                        System.out.println("Approve With : " + objresponse.getString("approvewith"));
                        txt_sendforapproval3.setText(getString(R.string.re_send_for_approval));
                        txt_sendforapproval3.setTextColor(Color.WHITE);
                        txt_sendforapproval3.setBackgroundColor(Color.RED);
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        public String postData(String unit) {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/checkStatus";
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("VisitorEntryID", vID));
                params.add(new BasicNameValuePair("unit_id", unit));
                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            String unit = (String) params[0];
            statusoflayout = (String) params[1];
            String s = postData(unit);
            return s;
        }

    }

    private class submitdataw extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                dialog.dismiss();
                String success = obj.getString("success");
                System.out.println("test");
                if (success.contains("1")) {
                    System.out.println("test1");
                    System.out.println("Vid +" + vID);
                    Intent welcomevisit = new Intent(visitor_unit.this, welcomevisit.class);
                    welcomevisit.putExtra("VisiID", vID);
                    startActivity(welcomevisit);
                    finish();

                }

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");
                //UnitSelcetion
                String url_login = getString(R.string.url) + "OTP/UnitApprovalData";
                String token = session.gettoken();
                String UnitID = "";
                System.out.println("Unit : " + UnitSelcetion);
                for (String s : UnitSelcetion) {
                    UnitID += s + ",";
                }
                String unitinsubmit = "", approveflaginsubmit = "";

                UnitID = UnitID.substring(0, UnitID.length() - 1);
                System.out.println("UnitList : " + UnitID);
                Gson objGson = new Gson();
                String objStr = objGson.toJson(unitdataapprovalarray);
                System.out.println("unit Approval " + objStr);
                JSONObject udata = new JSONObject(objStr);
                if (!objStr.equals("{}")) {
                    for (int i = 0; i < 4; i++) {
                        if (udata.has(String.valueOf(i))) {
                            JSONObject objunit = udata.getJSONObject(String.valueOf(i));
                            String data = objunit.getString("unitdata");
                            System.out.println("Data : " + data);
                            String[] arraydata = data.split(";");
                            unitinsubmit += arraydata[0] + ",";
                            approveflaginsubmit += arraydata[1] + ",";
                        }
                    }
                    unitinsubmit = unitinsubmit.substring(0, unitinsubmit.length() - 1);
                    approveflaginsubmit = approveflaginsubmit.substring(0, approveflaginsubmit.length() - 1);

                }

                System.out.println("Unit : " + unitinsubmit + " App :  " + approveflaginsubmit);
                String id = session.getid();
                String name = session.getName();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("VisitorEntryID", vID));
                params.add(new BasicNameValuePair("unit_id", UnitID));
                params.add(new BasicNameValuePair("unit", unitinsubmit));
                params.add(new BasicNameValuePair("approve", approveflaginsubmit));
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("visitorid", visitorid));

                params.add(new BasicNameValuePair("token", token));

                Log.e("params", String.valueOf(params));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                //session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    public void selectImage1() {

        new MaterialDialog.Builder(visitor_unit.this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                                break;
                            case 1:
                                captureImage();

                                break;
                            case 2:
                                ImageDoc.setVisibility(View.GONE);
                                isImageAddedexp = false;
                                break;
                        }
                    }
                })
                .show();

    }

    private void captureImage() {
        System.out.println("1");
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            System.out.println("2");
            // We give some instruction to the intent to save the image
            File photoFile = null;
            System.out.println("3");
            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                System.out.println("4");
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            System.out.println("5");
            outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            System.out.println("6 : " + outputUri);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            System.out.println("7");
            Logger.getAnonymousLogger().info("Calling the camera App by intent");
            System.out.println("8");
            // The following strings calls the camera app and wait for his file in return.
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //          intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            //  callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageFileLocation);
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
            System.out.println("9");
        } else {
            System.out.println("10");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            System.out.println("11");
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            System.out.println("12");
            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
            System.out.println("13");
        }
    }

    File createImageFile() throws IOException {
        mImageFileLocation = "";
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "V_DOC_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory.getPath() + File.separator + imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        System.out.println("Location : " + mImageFileLocation);
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RESULT CODE : " + resultCode);
        System.out.println("RESULT OKK : " + RESULT_OK);
        System.out.println("REQUEST CODE : " + requestCode);
        System.out.println("Data : " + data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST || requestCode == REQUEST_PICK_PHOTO) {

                // Get the Image from data
                if (requestCode == CAMERA_PIC_REQUEST) {

                    System.out.println("Media :  " + mImageFileLocation);
                    //  thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);

                    mediaPath = mImageFileLocation;
                    if (visitor_approval_status.equals("0")) {
                        ImageDoc.setVisibility(View.VISIBLE);
                        ImageDoc.getLayoutParams().height = 350;
                        ImageDoc.getLayoutParams().width = 350;
                        ImageDoc.setScaleType(ImageView.ScaleType.FIT_XY);
                        ImageDoc.setImageURI(Uri.parse(mImageFileLocation));
                        isImageAddedexp = true;
                        btnUpload.setVisibility(View.VISIBLE);
                    }
                    if (visitor_approval_status.equals("1")) {
                        ImageDoc1.setVisibility(View.VISIBLE);
                        ImageDoc1.getLayoutParams().height = 350;
                        ImageDoc1.getLayoutParams().width = 350;
                        ImageDoc1.setScaleType(ImageView.ScaleType.FIT_XY);
                        ImageDoc1.setImageURI(Uri.parse(mImageFileLocation));
                        isImageAddedexp = true;
                        btnUpload1.setVisibility(View.VISIBLE);
                    }

                    System.out.println("media Path : " + mImageFileLocation);
                    postPath = mediaPath;

                } else {
                    Uri selectedImage = data.getData();


                    System.out.println("image : " + selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    System.out.println("mediaPath : " + mediaPath);
                    // Set the Image in ImageView for Previewing the Media
                    //expImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    if (visitor_approval_status.equals("0")) {
                        ImageDoc.setVisibility(View.VISIBLE);
                        ImageDoc.getLayoutParams().height = 350;
                        ImageDoc.getLayoutParams().width = 350;
                        ImageDoc.setScaleType(ImageView.ScaleType.FIT_XY);
                        ImageDoc.setImageURI(selectedImage);
                        btnUpload.setVisibility(View.VISIBLE);
                    }
                    if (visitor_approval_status.equals("1")) {
                        ImageDoc1.setVisibility(View.VISIBLE);
                        ImageDoc1.getLayoutParams().height = 350;
                        ImageDoc1.getLayoutParams().width = 350;
                        ImageDoc1.setScaleType(ImageView.ScaleType.FIT_XY);
                        ImageDoc1.setImageURI(selectedImage);
                        btnUpload1.setVisibility(View.VISIBLE);
                    }
                    isImageAddedexp = true;


                    cursor.close();
                    postPath = mediaPath;
                }


            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {
                    if (visitor_approval_status.equals("0")) {
                        Glide.with(this).load(mImageFileLocation).into(ImageDoc);
                    }
                    if (visitor_approval_status.equals("1")) {
                        Glide.with(this).load(mImageFileLocation).into(ImageDoc1);
                    }
                    postPath = mImageFileLocation;

                } else {
                    if (visitor_approval_status.equals("0")) {
                        Glide.with(this).load(fileUri).into(ImageDoc);
                    }
                    if (visitor_approval_status.equals("1")) {
                        Glide.with(this).load(fileUri).into(ImageDoc1);
                    }

                    postPath = fileUri.getPath();

                }

            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadFile() {
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
            return;
        } else {


            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);
            System.out.println("1 " + file);
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse(""), visitorid);
            RequestBody requestBody2 = RequestBody.create(MediaType.parse(""), "4");

            // RequestBody time = createPartFromString("2016");
            map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            map.put("VisitorID", requestBody1);
            map.put("feature", requestBody2);
            // map.put("VisitorID",VisitorID1);
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            System.out.println("2 " + getResponse.toString());
            Call<ServerResponse> call = getResponse.upload("token", map);
            call.enqueue(new retrofit2.Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    System.out.println("3");
                    if (response.isSuccessful()) {
                        System.out.println("4");
                        if (response.body() != null) {
                            System.out.println("5");
                            ServerResponse serverResponse = response.body();
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "problem uploading image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.v("Response gotten is", t.getMessage());
                }
            });
        }
    }

    private class getOtpFlag extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONObject NewsData = jsondata.getJSONObject("Setting");
                    JSONObject NewsData1 = NewsData.getJSONObject("0");
                    visitor_approval_status = NewsData1.getString("send_approval_tovisitor");
                    System.out.println("send_approval_tovisitor : " + visitor_approval_status);
                    Layout1.setVisibility(View.GONE);
                    Layout2.setVisibility(View.VISIBLE);
                   /* if (visitor_approval_status.equals("1")) {
                        System.out.println("Test Data 1");
                        Layout1.setVisibility(View.GONE);
                        Layout2.setVisibility(View.VISIBLE);
                    }
                    if (visitor_approval_status.equals("0")) {
                        System.out.println("Test Data 0");
                        Layout2.setVisibility(View.GONE);
                        Layout1.setVisibility(View.VISIBLE);
                    }*/
                    System.out.println("Visitor Status : " + visitor_approval_status);
                    if (!docID.equals("1") && doc_img.equals("") && visitor_approval_status.equals("0")) {
                        documentlayout.setVisibility(View.VISIBLE);
                        if (docID.equals("2")) {
                            DocumentDetails.setText(getString(R.string.upload_aadhar_card));
                        }
                        if (docID.equals("3")) {
                            DocumentDetails.setText(getString(R.string.upload_pan_card));
                        }
                        if (docID.equals("4")) {
                            DocumentDetails.setText(getString(R.string.upload_driving_license));
                        }
                        if (docID.equals("5")) {
                            DocumentDetails.setText(getString(R.string.upload_document));
                        }
                    }
                    if (!docID.equals("1") && doc_img.equals("") && visitor_approval_status.equals("1")) {
                        documentlayout1.setVisibility(View.VISIBLE);
                        if (docID.equals("2")) {
                            DocumentDetails1.setText(getString(R.string.upload_aadhar_card));
                        }
                        if (docID.equals("3")) {
                            DocumentDetails1.setText(getString(R.string.upload_pan_card));
                        }
                        if (docID.equals("4")) {
                            DocumentDetails1.setText(getString(R.string.upload_driving_license));
                        }
                        if (docID.equals("5")) {
                            DocumentDetails1.setText(getString(R.string.upload_document));
                        }
                    }

                }


            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "News/OtpFlag";
                String token = session.gettoken();
                String societyId = session.getSocietyid();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyID", societyId));
                System.out.println("societyId:::" + societyId);
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            } catch (Exception e) {
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            //String gateno=(String) params[0];
            String s = postData();
            return s;
        }

    }

}
