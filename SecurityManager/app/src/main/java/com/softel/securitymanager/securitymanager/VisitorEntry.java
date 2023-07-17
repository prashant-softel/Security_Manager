package com.softel.securitymanager.securitymanager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.softel.securitymanager.securitymanager.Permission.ActivityManagePermission;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class VisitorEntry extends ActivityManagePermission {

    String imagepath = "";
    File cover;
    Uri mCropImageUri;

    //imagestart
    String VisitorID1, URL;
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;

    private static final String TAG = VisitorEntry.class.getSimpleName();

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    ProgressDialog pDialog;
    private String postPath;
    private String mediaPath;
    Uri outputUri;
    private Button btnCapturePicture;

    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    //imagened
    private Spinner purposespinner;
    private Spinner vCompanySpinnerVisitor;
    private Spinner CompanySpinner;
    String ExpectedVisitor = "0";
    // private SegmentedGroup mAprrovedGroup;
    Session session;
    JSONObject json;
    String NewVisitorOTP, ExpectedVisitorOTP, RepetedVisitorOTP, SendOtpVia;   // OTP Layout Enable Disable Section on check flag.
    TextView gate;
    LinearLayout companyV, othercompanyV;
    EditText txtothercompanyV;
    //ExpectedVisitor
    boolean isImageAddedexp = false;
    LinearLayout checkLayout;
    LinearLayout vehicleexpL, expectedvisitorL, company, alreadyexist, vOTPLayout, vExistVisitorLayout, vNewVisitorLayout, vSelectionLayout, vButtomLayout, imgexpL;
    TextView expname, expowner, expwing, expnote, exppurpose, existingtxt;
    EditText expvehicle;
    CardView selectimage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    ImageView expImage;
    private Spinner companyNameSpinner;
    EditText temp, oxygen, pulse;
    String tem = "0", oxy = "0", puls = "0";
    Integer health;
    EditText txtotp, othercompany, otherCompanyVisitor;
    String othercompanyexp = "", exp_flag, expcontact, expcompany = "";
    String purpose_name = "";
    Uri selectimageuri;
    private Button vSearch;  // Visitor Next Button
    String VisitorExist = "";  /// set new visitor and existing visitor flag click to next button.
    String CheckInVisitor = ""; // Checking Inside and Outside Section.
    private TextView vContactNo;
    private TextView vFirstName;
    private TextView vLastName;
    private ImageView vImage;
    private EditText vNote;
    private EditText vVehicle;
    private EditText nVFname, nVLname;  //  New Visitor Name
    private TextView ExistCheckingVisitor;
    String CompanyName;
    String purposeID;
    private TextView vOtherCompany;

    LinearLayout imgL, imageviewL;
    ImageView newImage;
    String visitortrace = "0";
    String Unit_id;
    String ApprovalFlag;
    private Button vSendOTP;
    private TextView vOTPText;
    private Button vVeryfyOTP;
    String vID = "";    // meand Send OTP getSesponse on VisitorEntry ID from Sm database
    String visitorID = ""; // means Exisitng Visitor Id In Security Root Database
    private Button vSubmit;
    String EntryGateNo;
    List<String> pusposeList = new ArrayList<>();
    ArrayList<String> CompanyList = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    //insert
    String vFName, vLName, vmobNumber, vNote1, vEnteredOtp, isVerified, vCompany, vVehicle1, purpose_id, VisitorEntryID, Doc_id, Doc_no, Gate_no, vNotev, CompanyOther;

    //Entry With Document
    Spinner EntryDoc;
    String entryDoc_id = "", Doc_img = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_entry);
        setTitle(getString(R.string.visitor_entry));
        /* --    back button code ---- */
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* Call OTP Function */
        new getOtpFlag().execute();
        /* --    back button code ---- */
        session = new Session(getApplicationContext());
        gate = (TextView) findViewById(R.id.gate);
        gate.setText("Gate No : " + session.getGateNo());
        /* ------------------ OTP Layout ENable Disable Section------- */
        vOTPLayout = (LinearLayout) findViewById(R.id.sendOtpLayout);
        vExistVisitorLayout = (LinearLayout) findViewById(R.id.RepetedVisitor); /// Repeted Visitor
        vNewVisitorLayout = (LinearLayout) findViewById(R.id.NewVisitorLayout); // new visitor
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        companyV = (LinearLayout) findViewById(R.id.companyV);
        othercompanyV = (LinearLayout) findViewById(R.id.othercompanyV);
        txtothercompanyV = (EditText) findViewById(R.id.txtothercompanyV);

        vSearch = (Button) findViewById(R.id.Vnext);
        vContactNo = (TextView) findViewById(R.id.txtNum);
        vFirstName = (TextView) findViewById(R.id.vfname);
        vLastName = (TextView) findViewById(R.id.vlname);
        vImage = (ImageView) findViewById(R.id.vImage);
        vImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageVisitor(v);
            }
        });
        ExistCheckingVisitor = (TextView) findViewById(R.id.massage);
        //Expected Visitor
        expectedvisitorL = (LinearLayout) findViewById(R.id.expectedvisitorL);
        imgexpL = (LinearLayout) findViewById(R.id.imgexpL);
        vehicleexpL = (LinearLayout) findViewById(R.id.vehicleexpL);
        //checkLayout
        checkLayout = (LinearLayout) findViewById(R.id.checkLayout);
        temp = (EditText) findViewById(R.id.temp);
        oxygen = (EditText) findViewById(R.id.oxygen);
        pulse = (EditText) findViewById(R.id.pulse);
        health = ((a_variable) this.getApplication()).getHealthcheck();
        alreadyexist = (LinearLayout) findViewById(R.id.alreadyexist);
        // vSelectionLayout,vButtomLayout,VnewImageLayout;

        vSelectionLayout = (LinearLayout) findViewById(R.id.unitselectionLayout);
        vButtomLayout = (LinearLayout) findViewById(R.id.SubmitButtomLayout);
        // VnewImageLayout=(LinearLayout) findViewById(R.id.ImagesLayout);
        expectedvisitorL.setVisibility(View.GONE);
        expname = (TextView) findViewById(R.id.expname);
        expvehicle = (EditText) findViewById(R.id.expvehicle);
        expowner = (TextView) findViewById(R.id.expowner);
        expwing = (TextView) findViewById(R.id.expwing);
        expnote = (TextView) findViewById(R.id.expnote);
        exppurpose = (TextView) findViewById(R.id.exppurpose);
        selectimage = (CardView) findViewById(R.id.selectimage);
        expImage = (ImageView) findViewById(R.id.expImage);
        othercompany = (EditText) findViewById(R.id.othercompany);
        existingtxt = (TextView) findViewById(R.id.existingtxt);
        company = (LinearLayout) findViewById(R.id.company);
        vCompanySpinnerVisitor = (Spinner) findViewById(R.id.companyNameSpinnerV);
        otherCompanyVisitor = (EditText) findViewById(R.id.txtothercompanyV);
        company.setVisibility(View.GONE);
        imageviewL = (LinearLayout) findViewById(R.id.imageviewL);
        imgL = (LinearLayout) findViewById(R.id.imgL);
        newImage = (ImageView) findViewById(R.id.ImageNew);

        imgL.setClickable(true);
        imgL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });

        //Entry With Document
        EntryDoc = (Spinner) findViewById(R.id.EntryDoc);
        List<String> docType = new ArrayList<>();
        docType.add(0, getString(R.string.select_entry_with));
        docType.add(1, getString(R.string.mobile));
        docType.add(2, getString(R.string.aadhar_number));
        docType.add(3, getString(R.string.pan_card_no));
        docType.add(4, getString(R.string.driving_license_number));
        docType.add(5, "other");

        //ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, docType);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        EntryDoc.setAdapter(dataAdapter);
        EntryDoc.setSelection(1);
        EntryDoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_entry_with))) {
                    String item = parent.getItemAtPosition(position).toString();
                    System.out.println("Doc type  : " + item);
                    System.out.println("Doc id : " + String.valueOf(position));
                    entryDoc_id = String.valueOf(position);
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    System.out.println(item);
                    System.out.println("Doc id : " + String.valueOf(position));
                    entryDoc_id = String.valueOf(position);
                    if (entryDoc_id.equals("1") || entryDoc_id.equals("2")) {
                        vContactNo.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    if (entryDoc_id.equals("3") || entryDoc_id.equals("4") || entryDoc_id.equals("6")) {
                        vContactNo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        companyNameSpinner = (Spinner) findViewById(R.id.companyNameSpinner);
        companyNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_company))) {
                    String item = parent.getItemAtPosition(position).toString();
                    expcompany = item;
                } else {

                    String item = parent.getItemAtPosition(position).toString();
                    System.out.println(item);
                    expcompany = item;
                    if (item.equals("Other")) {
                        expcompany = "other";
                        othercompany.setVisibility(View.VISIBLE);
                        othercompanyexp = othercompany.getText().toString();
                    } else {
                        othercompany.setVisibility(View.GONE);
                        othercompanyexp = "";
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtotp = (EditText) findViewById(R.id.txtOtp);
        selectimage.setClickable(true);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });

        vNote = (EditText) findViewById(R.id.note);
        vVehicle = (EditText) findViewById(R.id.txtVehicle);
        nVFname = (EditText) findViewById(R.id.txtFName);
        nVLname = (EditText) findViewById(R.id.txtLName);


        vSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSearch.setEnabled(false);
                visitortrace = "0";
                ExistCheckingVisitor.setVisibility(View.GONE);
                vSendOTP.setVisibility(View.GONE);
                txtotp.setVisibility(View.GONE);
                vVeryfyOTP.setVisibility(View.GONE);
                vExistVisitorLayout.setVisibility(View.GONE);
                vNewVisitorLayout.setVisibility(View.GONE);
                vSelectionLayout.setVisibility(View.GONE);
                imgL.setVisibility(View.GONE);
                checkLayout.setVisibility(View.GONE);
                imageviewL.setVisibility(View.GONE);
                vSubmit.setVisibility(View.GONE);
                if (vContactNo.getText().toString().length() == 0) {
                    vContactNo.requestFocus();
                    vContactNo.setError(getString(R.string.field_cannot_be_empty));
                } else if ((vContactNo.getText().toString().length() > 10 || vContactNo.getText().toString().length() < 10) && entryDoc_id.equals("1")) {
                    vContactNo.requestFocus();
                    vContactNo.setError(getString(R.string.entered_mobile_incorrect));
                    expectedvisitorL.setVisibility(View.GONE);
                    vExistVisitorLayout.setVisibility(View.GONE);
                    vNewVisitorLayout.setVisibility(View.GONE);
                    vSelectionLayout.setVisibility(View.GONE);
                    imgL.setVisibility(View.GONE);
                    imageviewL.setVisibility(View.GONE);
                    vSubmit.setVisibility(View.GONE);

                } else if (entryDoc_id.equals("0")) {
                    setSpinnerError(EntryDoc, getString(R.string.please_select_entry_with));
                } else {
                    vVehicle.setText("");
                    vNote.setText("");
                    purposespinner.setSelection(0);
                    companyNameSpinner.setSelection(0);
                    vCompanySpinnerVisitor.setSelection(0);

                    othercompany.setText("");
                    otherCompanyVisitor.setText("");
                    new SearchVisitor().execute();
                }
                vSearch.setEnabled(true);
            }
        });
        /*-----------------------------  Visitor Submit ------------------------ */
        vSubmit = (Button) findViewById(R.id.visitorSubmit);
        vSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (health == 1) {
                    if (temp.getText().toString().length() > 0) tem = temp.getText().toString();
                    if (oxygen.getText().toString().length() > 0) oxy = oxygen.getText().toString();
                    if (pulse.getText().toString().length() > 0) puls = pulse.getText().toString();
                }

                if (RepetedVisitorOTP.equals("0") && exp_flag.equals("0") && VisitorExist.equals("1") && entryDoc_id.equals("1")) {
                    if (purpose_name.equals(getString(R.string.extract_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.pleas_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                           /* if (vVehicle.getText().toString().length() == 0) {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            } else if (vNote.getText().toString().length() == 0) {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }  else {
                            */
                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitOTPVisitor().execute();
                            }

                            // }
                        } else {
                            if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_company));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            }
                           /* else if (vVehicle.getText().toString().length() == 0)
                            {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            }
                            else if (vNote.getText().toString().length() == 0)
                            {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }*/
                            else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {

                                new SubmitOTPVisitor().execute();
                            }
                        }
                    }

                }
                if (RepetedVisitorOTP.equals("0") && exp_flag.equals("0") && VisitorExist.equals("1") && (!entryDoc_id.equals("1"))) {
                    if (purpose_name.equals(getString(R.string.select_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.please_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                            /*if (vVehicle.getText().toString().length() == 0) {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            } else if (vNote.getText().toString().length() == 0) {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }  else {*/
                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                            // }
                        } else {
                            if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_category));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            }
                            /*else if (vVehicle.getText().toString().length() == 0)
                            {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            }
                            else if (vNote.getText().toString().length() == 0)
                            {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }*/
                            else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                        }
                    }

                }
                if (RepetedVisitorOTP.equals("1") && exp_flag.equals("0") && VisitorExist.equals("1")) {

                    if (purpose_name.equals(getString(R.string.select_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.pleas_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                            /*if (vVehicle.getText().toString().length() == 0) {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            } else if (vNote.getText().toString().length() == 0) {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }  else {*/
                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                            // }
                        } else {
                            if (expcompany.equalsIgnoreCase("")) {
                                Toast.makeText(VisitorEntry.this, "Please Select Company", Toast.LENGTH_SHORT).show();
                            } else if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_company));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            }
                            /*else if (vVehicle.getText().toString().length() == 0)
                            {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            }
                            else if (vNote.getText().toString().length() == 0)
                            {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }*/
                            else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                        }
                    }

                }
                if (NewVisitorOTP.equals("0") && exp_flag.equals("0") && VisitorExist.equals("0") && entryDoc_id.equals("1")) {
                    if (nVFname.getText().toString().length() == 0) {
                        nVFname.requestFocus();
                        nVFname.setError(getString(R.string.field_cannot_be_empty));
                    } else if (nVLname.getText().toString().length() == 0) {
                        nVLname.requestFocus();
                        nVLname.setError(getString(R.string.field_cannot_be_empty));
                    }
                    //else if (!isImageAddedexp) {

                    //    Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                    //}
                    else if (purpose_name.equals(getString(R.string.select_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.pleas_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                           /* if (vVehicle.getText().toString().length() == 0) {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            } else if (vNote.getText().toString().length() == 0) {
                            vNote.requestFocus();
                            vNote.setError("Field Cannot be Empty");
                        }  else {*/

                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitOTPVisitor().execute();
                            }

                            //}
                        } else {
                            if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_company));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            } else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            }

                           /* else if (vVehicle.getText().toString().length() == 0)
                            {
                                vVehicle.requestFocus();
                                vVehicle.setError("Field Cannot be Empty");
                            }
                          else if (vNote.getText().toString().length() == 0)
                            {
                                vNote.requestFocus();
                                vNote.setError("Field Cannot be Empty");
                            }*/

                            else {
                                new SubmitOTPVisitor().execute();
                            }
                        }
                    }

                    // new SubmitwthoutOTPVisitor().execute();
                }
                if (NewVisitorOTP.equals("0") && exp_flag.equals("0") && VisitorExist.equals("0") && (!entryDoc_id.equals("1"))) {
                    if (nVFname.getText().toString().length() == 0) {
                        nVFname.requestFocus();
                        nVFname.setError(getString(R.string.field_cannot_be_empty));
                    } else if (nVLname.getText().toString().length() == 0) {
                        nVLname.requestFocus();
                        nVLname.setError(getString(R.string.field_cannot_be_empty));
                    }
                    //else if (!isImageAddedexp) {

                    // Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                    //}
                    else if (purpose_name.equals(getString(R.string.select_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.pleas_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                              /* if (vVehicle.getText().toString().length() == 0) {
                                   vVehicle.requestFocus();
                                   vVehicle.setError("Field Cannot be Empty");
                               } else if (vNote.getText().toString().length() == 0) {
                                   vNote.requestFocus();
                                   vNote.setError("Field Cannot be Empty");
                               }  else {*/
                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                            // }
                        } else {
                            if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_company));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            }
                               /*else if (vVehicle.getText().toString().length() == 0)
                               {
                                   vVehicle.requestFocus();
                                   vVehicle.setError("Field Cannot be Empty");
                               }
                               else if (vNote.getText().toString().length() == 0)
                               {
                                   vNote.requestFocus();
                                   vNote.setError("Field Cannot be Empty");
                               }*/
                            else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }
                        }
                    }

                    // new SubmitwthoutOTPVisitor().execute();
                }
                if (NewVisitorOTP.equals("1") && exp_flag.equals("0") && VisitorExist.equals("0")) {
                    if (nVFname.getText().toString().length() == 0) {
                        nVFname.requestFocus();
                        nVFname.setError(getString(R.string.field_cannot_be_empty));
                    } else if (nVLname.getText().toString().length() == 0) {
                        nVLname.requestFocus();
                        nVLname.setError(getString(R.string.field_cannot_be_empty));
                    }
                    //else if (!isImageAddedexp) {

                    // Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                    //}
                    else if (purpose_name.equals(getString(R.string.select_purpose))) {
                        setSpinnerError(purposespinner, getString(R.string.pleas_select_purpose));
                    } else {
                        if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                              /* if (vVehicle.getText().toString().length() == 0) {
                                   vVehicle.requestFocus();
                                   vVehicle.setError("Field Cannot be Empty");
                               } else if (vNote.getText().toString().length() == 0) {
                                   vNote.requestFocus();
                                   vNote.setError("Field Cannot be Empty");
                               }  else {*/
                            if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }

                            // }
                        } else {
                            if (expcompany.equals(getString(R.string.select_company))) {
                                setSpinnerError(vCompanySpinnerVisitor, getString(R.string.please_select_company));
                            } else if (expcompany.equals("other") && txtothercompanyV.length() == 0) {
                                txtothercompanyV.requestFocus();
                                txtothercompanyV.setError(getString(R.string.field_cannot_be_empty));
                            }
                               /*else if (vVehicle.getText().toString().length() == 0)
                               {
                                   vVehicle.requestFocus();
                                   vVehicle.setError("Field Cannot be Empty");
                               }
                               else if (vNote.getText().toString().length() == 0)
                               {
                                   vNote.requestFocus();
                                   vNote.setError("Field Cannot be Empty");
                               }*/
                            else if (temp.getText().toString().length() == 0 && health == 1) {
                                temp.requestFocus();
                                temp.setError(getString(R.string.field_cannot_be_empty));
                            } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                                oxygen.requestFocus();
                                oxygen.setError(getString(R.string.field_cannot_be_empty));
                            } else if (pulse.getText().toString().length() == 0 && health == 1) {
                                pulse.requestFocus();
                                pulse.setError(getString(R.string.field_cannot_be_empty));
                            } else {
                                new SubmitwthoutOTPVisitor().execute();
                            }
                        }
                    }

                }
                if (ExpectedVisitorOTP.equals("0") && exp_flag.equals("1")) {
                    if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                       /* if (expvehicle.getText().toString().length() == 0) {
                            expvehicle.requestFocus();
                            expvehicle.setError("Field Cannot be Empty");
                        } else if (!isImageAddedexp) {

                            Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                        } else {*/
                        if (temp.getText().toString().length() == 0 && health == 1) {
                            temp.requestFocus();
                            temp.setError(getString(R.string.field_cannot_be_empty));
                        } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                            oxygen.requestFocus();
                            oxygen.setError(getString(R.string.field_cannot_be_empty));
                        } else if (pulse.getText().toString().length() == 0 && health == 1) {
                            pulse.requestFocus();
                            pulse.setError(getString(R.string.field_cannot_be_empty));
                        } else {
                            new SubmitVisitorExp().execute();
                        }

                        // }


                    } else {
                        if (expcompany.equals(getString(R.string.select_company))) {
                            setSpinnerError(companyNameSpinner, getString(R.string.please_select_company));
                        } else if (expcompany.equals("other") && othercompany.length() == 0) {
                            othercompany.requestFocus();
                            othercompany.setError(getString(R.string.field_cannot_be_empty));
                        }
                        /*else if(expvehicle.getText().toString().length()==0)
                        {
                            expvehicle.requestFocus();
                            expvehicle.setError("Field Cannot be Empty");
                        }*/
                        //else if(!isImageAddedexp)
                        //{
                        // Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                        ///}
                        else if (temp.getText().toString().length() == 0 && health == 1) {
                            temp.requestFocus();
                            temp.setError(getString(R.string.field_cannot_be_empty));
                        } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                            oxygen.requestFocus();
                            oxygen.setError(getString(R.string.field_cannot_be_empty));
                        } else if (pulse.getText().toString().length() == 0 && health == 1) {
                            pulse.requestFocus();
                            pulse.setError(getString(R.string.field_cannot_be_empty));
                        } else {
                            new SubmitVisitorExp().execute();
                        }
                    }


                }
                if (ExpectedVisitorOTP.equals("1") && exp_flag.equals("1")) {
                    if (purpose_name.equals("Guest") || purpose_name.equals("Other")) {
                        if (expvehicle.getText().toString().length() == 0) {
                            expvehicle.requestFocus();
                            expvehicle.setError(getString(R.string.field_cannot_be_empty));
                        } //else if (!isImageAddedexp)
                        //{

                        // Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                        // }
                        else if (temp.getText().toString().length() == 0 && health == 1) {
                            temp.requestFocus();
                            temp.setError(getString(R.string.field_cannot_be_empty));
                        } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                            oxygen.requestFocus();
                            oxygen.setError(getString(R.string.field_cannot_be_empty));
                        } else if (pulse.getText().toString().length() == 0 && health == 1) {
                            pulse.requestFocus();
                            pulse.setError(getString(R.string.field_cannot_be_empty));
                        } else {
                            new SubmitWithoutVisitorExp().execute();
                        }
                    } else {
                        if (expcompany.equals(getString(R.string.select_company))) {
                            setSpinnerError(companyNameSpinner, getString(R.string.please_select_company));
                        } else if (expcompany.equals("other") && othercompany.length() == 0) {
                            othercompany.requestFocus();
                            othercompany.setError(getString(R.string.field_cannot_be_empty));
                        }
                       /* else if(expvehicle.getText().toString().length()==0)
                        {
                            expvehicle.requestFocus();
                            expvehicle.setError("Field Cannot be Empty");
                        }*/
                        ///else if(!isImageAddedexp)
                        //{
                        // Toast.makeText(getApplicationContext(), "Please select an image ", Toast.LENGTH_LONG).show();
                        // }
                        else if (temp.getText().toString().length() == 0 && health == 1) {
                            temp.requestFocus();
                            temp.setError(getString(R.string.field_cannot_be_empty));
                        } else if (oxygen.getText().toString().length() == 0 && health == 1) {
                            oxygen.requestFocus();
                            oxygen.setError(getString(R.string.field_cannot_be_empty));
                        } else if (pulse.getText().toString().length() == 0 && health == 1) {
                            pulse.requestFocus();
                            pulse.setError(getString(R.string.field_cannot_be_empty));
                        } else {
                            new SubmitWithoutVisitorExp().execute();
                        }
                    }

                }

            }
        });
        /* -------------------------  Send OTP Button ----------------------- */
        vSendOTP = (Button) findViewById(R.id.SendOTP);
        vSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtotp.setVisibility(View.VISIBLE);
                vVeryfyOTP.setVisibility(View.VISIBLE);
                System.out.println("Value of Send Otp Via : " + SendOtpVia);
                if (SendOtpVia.equals("1")) {
                    new SendOTPviaAPI().execute();
                } else {
                    new SendOTP().execute();
                }
            }
        });


        /* -------------------------  Verify OTP Button ----------------------- */
        vVeryfyOTP = (Button) findViewById(R.id.verifiyOTP);
        vVeryfyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerifiyOTP().execute();
            }
        });
        /* -----------------------------  Purpose spinner assign to purpose -------------------- */
        purposespinner = (Spinner) findViewById(R.id.purpose);
        new getPurpose().execute();


        purposespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_purpose))) {
                    String item = parent.getItemAtPosition(position).toString();
                    purpose_name = item;
                } else {

                    String item = parent.getItemAtPosition(position).toString();
                    purposeID = String.valueOf(position);
                    purpose_name = item;
                    if (Integer.parseInt(purposeID) < 5) {
                        new getCompany().execute(String.valueOf(position));
                        companyV.setVisibility(View.VISIBLE);
                    } else {
                        companyV.setVisibility(View.GONE);
                    }

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* -----------------------------  Company spinner assign to purpose -------------------- */
        // CompanySpinner =(Spinner) findViewById(R.id.companyNameSpinner);
        vCompanySpinnerVisitor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.BLACK);

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_company))) {
                    // do nothing

                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    expcompany = item;
                    if (item.equals("Other")) {
                        expcompany = "other";
                        othercompanyV.setVisibility(View.VISIBLE);
                        txtothercompanyV.setVisibility(View.VISIBLE);
                        othercompanyexp = txtothercompanyV.getText().toString();
                    } else {
                        txtothercompanyV.setVisibility(View.GONE);
                        othercompanyV.setVisibility(View.GONE);
                        othercompanyexp = "";
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Get the Image from data
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                startCropImageAcitivity(imageUri);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    mCropImageUri = result.getUri();
                    imagepath = getPathMyMethod(this,mCropImageUri);
                    Log.e("imagepath1", imagepath);
                    postPath = imagepath;

                    System.out.println("Media :  " + mImageFileLocation);
                    //  thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);

                    mediaPath = mImageFileLocation;
                    // mediaPath = getRealPathFromURI(outputUri);
                    if (visitortrace.equals("1")) {
                       // vImage.setImageURI(Uri.parse(imagepath));
                        vImage.setVisibility(View.VISIBLE);
                        Glide.with(this).load(mCropImageUri).into(vImage);

                    } else {
                        if (exp_flag.equals("0") && VisitorExist.equals("0")) {
                            newImage.setVisibility(View.VISIBLE);
                            newImage.getLayoutParams().height = 300;
                            newImage.getLayoutParams().width = 350;
                            newImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            //newImage.setRotation(newImage.getRotation() + 90);
                            newImage.setImageURI(Uri.parse(mImageFileLocation));
                            newImage.setVisibility(View.VISIBLE);
                            Glide.with(this).load(mCropImageUri).into(newImage);

                            try {
                                ExifInterface ei = new ExifInterface(mImageFileLocation);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        newImage.setRotation(90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        newImage.setRotation(180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        newImage.setRotation(270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        newImage.setRotation(0);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isImageAddedexp = true;
                        } else {
                            expImage.setVisibility(View.VISIBLE);
                            expImage.getLayoutParams().height = 300;
                            expImage.getLayoutParams().width = 350;
                            expImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            expImage.setImageURI(Uri.parse(mImageFileLocation));
                            isImageAddedexp = true;

                        }
                    }
                    System.out.println("media Path : " + mImageFileLocation);
                    postPath = mediaPath;
                }
            }
            /*if (requestCode == CAMERA_PIC_REQUEST || requestCode == REQUEST_PICK_PHOTO) {

                // Get the Image from data
                if (requestCode == CAMERA_PIC_REQUEST) {

                    System.out.println("Media :  " + mImageFileLocation);
                    //  thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);

                    mediaPath = mImageFileLocation;
                    // mediaPath = getRealPathFromURI(outputUri);
                    if (visitortrace.equals("1")) {
                        vImage.setImageURI(Uri.parse(mImageFileLocation));
                    } else {
                        if (exp_flag.equals("0") && VisitorExist.equals("0")) {
                            newImage.setVisibility(View.VISIBLE);
                            newImage.getLayoutParams().height = 300;
                            newImage.getLayoutParams().width = 350;
                            newImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            //newImage.setRotation(newImage.getRotation() + 90);
                            newImage.setImageURI(Uri.parse(mImageFileLocation));
                            try {
                                ExifInterface ei = new ExifInterface(mImageFileLocation);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        newImage.setRotation(90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        newImage.setRotation(180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        newImage.setRotation(270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        newImage.setRotation(0);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isImageAddedexp = true;
                        } else {
                            expImage.setVisibility(View.VISIBLE);
                            expImage.getLayoutParams().height = 300;
                            expImage.getLayoutParams().width = 350;
                            expImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            expImage.setImageURI(Uri.parse(mImageFileLocation));
                            isImageAddedexp = true;

                        }
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
                    if (visitortrace.equals("1")) {
                        vImage.setImageURI(selectedImage);
                    } else {
                        if (exp_flag.equals("0") && VisitorExist.equals("0")) {
                            newImage.setVisibility(View.VISIBLE);
                            newImage.getLayoutParams().height = 300;
                            newImage.getLayoutParams().width = 350;
                            newImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            newImage.setImageURI(selectedImage);
                            isImageAddedexp = true;
                        } else {
                            expImage.setVisibility(View.VISIBLE);
                            expImage.getLayoutParams().height = 300;
                            expImage.getLayoutParams().width = 350;
                            expImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            expImage.setImageURI(selectedImage);
                            isImageAddedexp = true;
                        }
                    }
                    cursor.close();
                    postPath = mediaPath;
                }


            } */


            else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

                    Glide.with(this).load(mImageFileLocation).into(expImage);
                    postPath = mImageFileLocation;

                } else {
                    Glide.with(this).load(fileUri).into(expImage);
                    postPath = fileUri.getPath();

                }

            }

        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathMyMethod(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /*--------------------via api----------------------------------*/
    private class SendOTPviaAPI extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    // JSONObject responseData = jsondata.getJSONObject("ServiceReqID");
                    vID = jsondata.getString("ServiceReqID");
                    String otp_message = jsondata.getString("msg");

                    String number = vContactNo.getText().toString().trim();


                    SmsManager manager = SmsManager.getDefault();
                    System.out.println("Message : " + otp_message);
                    manager.sendTextMessage(number, null, otp_message, null, null);
                    System.out.println("Number : " + vContactNo.getText().toString());
                    Toast.makeText(getApplicationContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/sendapi";
                String token = session.gettoken();
                String SocietyName = session.getSocietyName();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));

                if (exp_flag.equals("1")) {
                    params.add(new BasicNameValuePair("vFName", vFName));
                    params.add(new BasicNameValuePair("vLName", vLName));
                } else {
                    if (VisitorExist.equals("1")) {
                        params.add(new BasicNameValuePair("vFName", vFirstName.getText().toString()));
                        params.add(new BasicNameValuePair("vLName", vLastName.getText().toString()));

                    } else {
                        params.add(new BasicNameValuePair("vFName", nVFname.getText().toString()));
                        params.add(new BasicNameValuePair("vLName", nVLname.getText().toString()));

                    }
                }
                params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                params.add(new BasicNameValuePair("SocietyName", SocietyName));


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

    /* ------------------------Get OTP Flag in Visitor ------------------------------*/
    private class getOtpFlag extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                Log.e("News/OtpFlag", String.valueOf(obj));
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONObject NewsData = jsondata.getJSONObject("Setting");
                    JSONObject NewsData1 = NewsData.getJSONObject("0");
                    NewVisitorOTP = NewsData1.getString("OTP_Status_New");
                    ExpectedVisitorOTP = NewsData1.getString("OTP_Status_Exp");
                    RepetedVisitorOTP = NewsData1.getString("OTP_Status_Rep");
                    SendOtpVia = NewsData1.getString("sms_send_api");
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
                Log.e("News/OtpFlagParams", String.valueOf(params));
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

    /* -----------------------expectedvisitor Show image--------------------- */
    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(VisitorEntry.this);
        builder.setTitle(getString(R.string.select_image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/*");
                    startActivityForResult(gallery.createChooser(gallery, "Select File"), SELECT_FILE);
                } else if (items[i].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /* ----------------------------  Click Next BUtton ------------------------ */

    private class SearchVisitor extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {


                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {

                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray visitor = jsondata.getJSONArray("visitorDetails");
                    JSONObject Vdata = visitor.getJSONObject(0);
                    //if(jsondata.getString("ex_flag").equals("1")) {
                    if (Vdata.getString("ex_flag").equals("1")) {
                        System.out.println("Inside IF");
                        vSelectionLayout.setVisibility(View.GONE);
                        imgL.setVisibility(View.GONE);
                        imageviewL.setVisibility(View.GONE);
                        exp_flag = "1";
                        expectedvisitorL.setVisibility(View.VISIBLE);
                        vExistVisitorLayout.setVisibility(View.GONE);
                        vNewVisitorLayout.setVisibility(View.GONE);// visible
                        JSONArray visitor1 = jsondata.getJSONArray("visitorDetails");

                        JSONObject Vdata1 = visitor.getJSONObject(0);
                        expname.setText(Vdata1.getString("fname") + " " + Vdata1.getString("lname"));
                        vFName = Vdata1.getString("fname");
                        vLName = Vdata1.getString("lname");
                        VisitorEntryID = Vdata1.getString("visitor_id");
                        vVehicle1 = expvehicle.getText().toString();

                        expowner.setText(Vdata1.getString("OwnerName"));
                        purpose_id = Vdata1.getString("purpose_id");
                        Unit_id = Vdata1.getString("unit");
                        expwing.setText(Vdata1.getString("wing") + " - " + Vdata1.getString("unit_no"));
                        expcontact = Vdata1.getString("mobile");
                        if (Vdata.has("note")) {
                            expnote.setText(Vdata1.getString("note"));
                        }
                        exppurpose.setText(Vdata1.getString("purpose_name"));
                        if (jsondata.getString("ChekInExist").equals("Inside")) {
                            alreadyexist.setVisibility(View.VISIBLE);
                            existingtxt.setText("\nVisitor Already Checked In");
                            company.setVisibility(View.GONE);
                            vehicleexpL.setVisibility(View.GONE);
                            imgexpL.setVisibility(View.GONE);

                        } else {
                            if (!(Vdata1.getString("purpose_id").equals("5"))) {
                                company.setVisibility(View.VISIBLE);
                                new getCompany().execute(Vdata1.getString("purpose_id"));
                            } else {
                                purpose_name = "Guest";
                            }
                            if (ExpectedVisitorOTP.equals("0") && entryDoc_id.equals("1")) {
                                vSubmit.setText(getString(R.string.submit));
                                if (health == 1) checkLayout.setVisibility(View.VISIBLE);
                                vSendOTP.setVisibility(View.VISIBLE);

                            } else {
                                vSubmit.setText(getString(R.string.submit));
                                vSubmit.setVisibility(View.VISIBLE);
                                if (health == 1) checkLayout.setVisibility(View.VISIBLE);
                                System.out.println("test");

                            }

                        }

                    }

                    if (Vdata.getString("ex_flag").equals("0")) {


                        expectedvisitorL.setVisibility(View.GONE);
                        imgL.setVisibility(View.GONE);
                        imageviewL.setVisibility(View.GONE);
                        vExistVisitorLayout.setVisibility(View.VISIBLE);
                        vNewVisitorLayout.setVisibility(View.GONE); // visible
                        exp_flag = "0";
                        if (health == 1) checkLayout.setVisibility(View.VISIBLE);
                        CheckInVisitor = jsondata.getString("ChekInExist");
                        Log.e("CheckInVisitor",CheckInVisitor);
                        vFirstName.setText(Vdata.getString("Fname"));
                        vLastName.setText(Vdata.getString("Lname"));
                        vFName = Vdata.getString("Fname");
                        vLName = Vdata.getString("Lname");
                        visitorID = Vdata.getString("visitor_id");
                        Doc_img = Vdata.getString("Doc_img");
                        URL = getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + Vdata.getString("img");
                        if (Vdata.getString("img").equals("")) {
                            vImage.setImageResource(R.drawable.noimage);
                        } else {
                            Picasso.get().load(getString(R.string.imageurl) + "SecuirityApp/VisitorImage/" + Vdata.getString("img")).into(vImage);
                        }
                        if (jsondata.getString("ChekInExist").equals("Inside")) {
                            ExistCheckingVisitor.setVisibility(View.VISIBLE);
                            vSelectionLayout.setVisibility(View.GONE);
                            checkLayout.setVisibility(View.GONE);
                            expectedvisitorL.setVisibility(View.GONE);
                            vSubmit.setVisibility(View.GONE);
                            //existingtxt.setText("Visitor Already Checked In");
                        } else {
                            if (RepetedVisitorOTP.equals("0") && entryDoc_id.equals("1")) {

                                vSendOTP.setVisibility(View.VISIBLE);

                            } else {
                                //vSubmit.setVisibility(View.VISIBLE);
                                vSelectionLayout.setVisibility(View.VISIBLE);
                                vSubmit.setVisibility(View.VISIBLE);
                                System.out.println("test");

                            }
                            if (health == 1) checkLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    VisitorExist = "1";
                } else {
                    System.out.println("Text");
                    VisitorExist = "0";
                    exp_flag = "0";
                    expectedvisitorL.setVisibility(View.GONE);
                    vExistVisitorLayout.setVisibility(View.GONE);
                    vNewVisitorLayout.setVisibility(View.VISIBLE);


                    if (NewVisitorOTP.equals("0") && entryDoc_id.equals("1")) {

                        vSendOTP.setVisibility(View.VISIBLE);
                        imageviewL.setVisibility(View.VISIBLE);
                        imgL.setVisibility(View.VISIBLE);
                        if (health == 1) checkLayout.setVisibility(View.VISIBLE);

                    } else {
                        //vSubmit.setVisibility(View.VISIBLE);
                        vSelectionLayout.setVisibility(View.VISIBLE);
                        vSubmit.setVisibility(View.VISIBLE);
                        imageviewL.setVisibility(View.VISIBLE);
                        imgL.setVisibility(View.VISIBLE);
                        System.out.println("test");
                        if (health == 1) checkLayout.setVisibility(View.VISIBLE);

                    }
                }


            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "Visitor/FetchVisitorByMobileNo";
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("contactNo", vContactNo.getText().toString()));
                params.add(new BasicNameValuePair("EntryDoc_id", entryDoc_id));

                Log.e("FetchVisitorByMobileNo", String.valueOf(params));

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
            String s = "";
            s = postData();
            return s;
        }

    }

    /* ----------------------------  Click Send OTP ------------------------ */

    private class SendOTP extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on otp post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                Toast.makeText(VisitorEntry.this, "OTP Sent to visitor device", Toast.LENGTH_SHORT).show();
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    // JSONObject responseData = jsondata.getJSONObject("ServiceReqID");
                    vID = jsondata.getString("ServiceReqID");
                    System.out.println("VID " + vID);

                }
            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/send";
                String token = session.gettoken();
                String SocietyName = session.getSocietyName();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));

                if (exp_flag.equals("1")) {
                    params.add(new BasicNameValuePair("vFName", vFName));
                    params.add(new BasicNameValuePair("vLName", vLName));
                } else {
                    if (VisitorExist.equals("1")) {
                        params.add(new BasicNameValuePair("vFName", vFirstName.getText().toString()));
                        params.add(new BasicNameValuePair("vLName", vLastName.getText().toString()));

                    } else {
                        params.add(new BasicNameValuePair("vFName", nVFname.getText().toString()));
                        params.add(new BasicNameValuePair("vLName", nVLname.getText().toString()));

                    }
                }
                params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                params.add(new BasicNameValuePair("SocietyName", SocietyName));


                Log.e("OTPPArams", String.valueOf(params));
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

    /* ----------------------------  Click Verify OTP ------------------------ */

    private class VerifiyOTP extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.e("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.equals("1")) {
                    vVeryfyOTP.setText(getString(R.string.verified));
                    vVeryfyOTP.setBackgroundColor(Color.rgb(0, 120, 26));
                    vVeryfyOTP.setClickable(false);
                    if (exp_flag.equals("0")) {
                        vSelectionLayout.setVisibility(View.VISIBLE);
                    } else {
                        vSelectionLayout.setVisibility(View.GONE);
                    }
                    // vSubmit.setVisibility(View.VISIBLE);
                    vSubmit.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Kindly enter correct OTP or Check your internet connection", Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                Log.e("Exception::0",""+e);
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/verify";
                String token = session.gettoken();
                String SocietyName = session.getSocietyName();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("vEnteredOtp", txtotp.getText().toString()));
                params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                params.add(new BasicNameValuePair("vID", vID));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                Log.e("PARAMS::0", String.valueOf(params));
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

    /* ----------------------------  PurPose Function ----------------------- */
    private class getPurpose extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray purposeData = jsondata.getJSONArray("purpose");

                    // pusposeList.add(0,"Select Purpose");
                    pusposeList.add(0, getString(R.string.select_purpose));
                    for (int i = 0; i < purposeData.length(); i++) {
                        // pusposeList.add(0,"Select Purpose");
                        JSONObject objData = purposeData.getJSONObject(i);
                        pusposeList.add(Integer.valueOf(objData.getString("purpose_id")), objData.getString("purpose_name"));

                    }
                    dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, pusposeList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    purposespinner.setAdapter(dataAdapter);


                }
            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "ServiceProvider/fetchPurpose";
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

    /* ----------------------------  Company call inside GetPurpose Function ----------------------- */
    private class getCompany extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            System.out.println(result);
            CompanyList.add(Integer.valueOf(result) + 1, "Other");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, CompanyList);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            System.out.println("Array2  : " + result);
            companyNameSpinner.setAdapter(adapter);
            System.out.println("Compa after add : " + CompanyList.toString());
            vCompanySpinnerVisitor.setAdapter(adapter);

        }

        public String postData(String purposeId) {
            String length = "";
            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "ServiceProvider/fetchCompany";
                String token = session.gettoken();
                System.out.println("token" + token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("porpose", purposeId));
                params.add(new BasicNameValuePair("token", token));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                JSONObject obj = new JSONObject(origresponseText);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    CompanyList.clear();
                    System.out.println("Compa after clearing : " + CompanyList.toString());
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray companyData = jsondata.getJSONArray("CompanyName");
                    CompanyList.add(0, getString(R.string.select_company));
                    System.out.println(length);
                    length = String.valueOf(companyData.length());
                    for (int i = 0; i < companyData.length(); i++) {
                        JSONObject objData = companyData.getJSONObject(i);
                        CompanyList.add(Integer.valueOf(i + 1), objData.getString("c_name"));

                    }
                }
            } catch (Exception e) {
            }
            return length;
        }

        @Override
        protected String doInBackground(String... params) {
            String purposeId = (String) params[0];
            String s = postData(purposeId);
            return s;
        }

    }

    /*  ----------------------  Expected Visitor with OTP ------------------------  */
    private class SubmitVisitorExp extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (obj.getString("success").equals("1")) {
                    JSONObject objsucc = obj.getJSONObject("response");
                    Toast.makeText(getApplicationContext(), "Visitor Entered Successfully", Toast.LENGTH_SHORT).show();
                    String VisitorID = objsucc.getString("VisitorID");
                    System.out.println("VisitorID : " + VisitorID);
                    VisitorID1 = VisitorEntryID;
                    System.out.println("VisitorID : " + VisitorID);
                    uploadFile();
                    Intent welcomevisit = new Intent(VisitorEntry.this, welcomevisit.class);
                    welcomevisit.putExtra("VisiID", VisitorID);
                    startActivity(welcomevisit);
                }


            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "Visitor/submit";
                String token = session.gettoken();
                EntryGateNo = session.getGateNo();
                ArrayList<Object> list = new ArrayList<>();

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("Gate_no", EntryGateNo));
                params.add(new BasicNameValuePair("EntryDoc_id", entryDoc_id));

                params.add(new BasicNameValuePair("temp", tem));
                params.add(new BasicNameValuePair("oxygen", oxy));
                params.add(new BasicNameValuePair("pulse", puls));

                if (exp_flag.equals("1")) {
                    System.out.println(expcompany + " " + othercompanyexp);
                    params.add(new BasicNameValuePair("VisitorEntryID", VisitorEntryID));
                    params.add(new BasicNameValuePair("vID", vID));
                    params.add(new BasicNameValuePair("vVehicle", expvehicle.getText().toString()));
                    params.add(new BasicNameValuePair("unit_id", Unit_id));
                    params.add(new BasicNameValuePair("ApprovalFlag", "3"));
                    params.add(new BasicNameValuePair("purpose_id", purpose_id));
                    params.add(new BasicNameValuePair("vFName", vFName));
                    params.add(new BasicNameValuePair("vLName", vLName));
                    params.add(new BasicNameValuePair("vNote", expnote.getText().toString()));
                    params.add(new BasicNameValuePair("vmobNumber", expcontact));
                    params.add(new BasicNameValuePair("isVerified", ""));
                    params.add(new BasicNameValuePair("vCompany", expcompany));
                    params.add(new BasicNameValuePair("vCompanyOther", othercompanyexp));

                    params.add(new BasicNameValuePair("Doc_id", "1"));
                    params.add(new BasicNameValuePair("Doc_no", ""));

                } else {
                    params.add(new BasicNameValuePair("vFName", vFirstName.getText().toString()));
                    params.add(new BasicNameValuePair("vLName", vLastName.getText().toString()));
                    params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                    params.add(new BasicNameValuePair("VisitorEntryID", VisitorEntryID));
                    params.add(new BasicNameValuePair("vID", vID));
                    params.add(new BasicNameValuePair("vVehicle", "1"));
                    params.add(new BasicNameValuePair("purpose_id", "1"));
                    params.add(new BasicNameValuePair("vCompany", "1"));
                    params.add(new BasicNameValuePair("vCompanyOther", ""));
                    params.add(new BasicNameValuePair("unit_id", "1"));
                    params.add(new BasicNameValuePair("ApprovalFlag", "1"));
                    //params.add(new BasicNameValuePair("vNote", vNote));

                    params.add(new BasicNameValuePair("Doc_id", "1"));
                    params.add(new BasicNameValuePair("Doc_no", ""));
                }

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

    /*  ----------------------  Expected Visitor withaout OTP ------------------------  */
    private class SubmitWithoutVisitorExp extends AsyncTask<String, String, String> {
        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (obj.getString("success").equals("1")) {
                    JSONObject objsucc = obj.getJSONObject("response");
                    Toast.makeText(getApplicationContext(), "Visitor Entered Successfully", Toast.LENGTH_SHORT).show();
                    String VisitorID = objsucc.getString("VisitorID");
                    VisitorID1 = VisitorEntryID;
                    System.out.println("VisitorID : " + VisitorID);
                    uploadFile();

               /*     //image
                    String correctPath = getRealPathFromURI(selectimageuri).substring(getRealPathFromURI(selectimageuri).lastIndexOf('/') + 1);
                   System.out.println(correctPath);
                    System.out.println("File : "+ selectimageuri);
                   // System.out.println("File Path : "+getPath(selectimageuri));
                    System.out.println("File R : "+ getRealPathFromURI(selectimageuri));

                    try {
                        System.out.println("Executed");

                        String uploadId = UUID.randomUUID().toString();

                      new MultipartUploadRequest(getApplicationContext(),uploadId , "http://13.232.206.120/upload_image.php")
                                .addFileToUpload(getRealPathFromURI(selectimageuri), "file")
                                .setMethod("POST")
                                .addParameter("fileName",correctPath)
                                .addParameter("token", session.gettoken()) //Adding text parameter to the request
                                .addParameter("feature","1")
                                .addParameter("VisitorID",VisitorID)
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload();
                        Toast.makeText(VisitorEntry.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();

                        System.out.println("Executed1");
                    } catch (Exception e) {
                        System.out.println("In url not found " + e);
                        e.printStackTrace();
                    }
*/

                    Intent welcomevisit = new Intent(VisitorEntry.this, welcomevisit.class);
                    welcomevisit.putExtra("VisiID", VisitorID);
                    startActivity(welcomevisit);

                }


            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/submitNewWithoutOTP";
                String token = session.gettoken();
                EntryGateNo = session.getGateNo();
                ArrayList<Object> list = new ArrayList<>();

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("Gate_no", EntryGateNo));
                params.add(new BasicNameValuePair("EntryDoc_id", entryDoc_id));
                params.add(new BasicNameValuePair("temp", tem));
                params.add(new BasicNameValuePair("oxygen", oxy));
                params.add(new BasicNameValuePair("pulse", puls));
                if (exp_flag.equals("1")) {
                    params.add(new BasicNameValuePair("VisitorEntryID", VisitorEntryID));
                    params.add(new BasicNameValuePair("vID", "0"));
                    params.add(new BasicNameValuePair("vVehicle", expvehicle.getText().toString()));
                    params.add(new BasicNameValuePair("unit_id", Unit_id));
                    params.add(new BasicNameValuePair("ApprovalFlag", "3"));
                    params.add(new BasicNameValuePair("purpose_id", purpose_id));
                    params.add(new BasicNameValuePair("vFName", vFName));
                    params.add(new BasicNameValuePair("vLName", vLName));
                    params.add(new BasicNameValuePair("vNote", expnote.getText().toString()));
                    params.add(new BasicNameValuePair("vmobNumber", expcontact));
                    params.add(new BasicNameValuePair("isVerified", ""));
                    params.add(new BasicNameValuePair("vCompany", expcompany));
                    params.add(new BasicNameValuePair("vCompanyOther", othercompanyexp));
                    params.add(new BasicNameValuePair("Doc_id", "1"));
                    params.add(new BasicNameValuePair("Doc_no", ""));
                    JSONParser jParser = new JSONParser();
                    JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                    origresponseText = json.toString();
                } else {
                    params.add(new BasicNameValuePair("vFName", vFirstName.getText().toString()));
                    params.add(new BasicNameValuePair("vLName", vLastName.getText().toString()));
                    params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                    params.add(new BasicNameValuePair("VisitorEntryID", VisitorEntryID));
                    params.add(new BasicNameValuePair("vID", vID));
                    params.add(new BasicNameValuePair("vVehicle", "1"));
                    params.add(new BasicNameValuePair("purpose_id", "1"));
                    params.add(new BasicNameValuePair("vCompany", "1"));
                    params.add(new BasicNameValuePair("vCompanyOther", ""));
                    params.add(new BasicNameValuePair("unit_id", "1"));
                    params.add(new BasicNameValuePair("ApprovalFlag", "1"));
                    // params.add(new BasicNameValuePair("vNote", vNote));

                    params.add(new BasicNameValuePair("Doc_id", "1"));
                    params.add(new BasicNameValuePair("Doc_no", ""));
                    JSONParser jParser = new JSONParser();
                    JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                    origresponseText = json.toString();
                }


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

    /* ----- Submit Visitor  Without Otp (Existing  And New Visitor) ------ */
    private class SubmitwthoutOTPVisitor extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    // JSONObject responseData = jsondata.getJSONObject("ServiceReqID");
                    vID = jsondata.getString("VisitorID");
                    System.out.println("vID" + vID);
                    VisitorID1 = jsondata.getString("VisitorEntryID");
                    if (VisitorExist.equals("0") || visitortrace.equals("1")) {
                        uploadFile();
                    }
                    if (!vID.equals("0")) {
                        System.out.println("vID" + vID);

                        Intent addUnit = new Intent(VisitorEntry.this, visitor_unit.class);
                        addUnit.putExtra("vID", vID);
                        addUnit.putExtra("visitorid", VisitorID1);
                        addUnit.putExtra("entryWith", entryDoc_id);
                        addUnit.putExtra("Doc_img", Doc_img);
                        startActivity(addUnit);

                    }
                }

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "OTP/NewAPPsubmitWithoutOTP";
                String token = session.gettoken();
                EntryGateNo = session.getGateNo();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("EntryDoc_id", entryDoc_id));
                params.add(new BasicNameValuePair("temp", tem));
                params.add(new BasicNameValuePair("oxygen", oxy));
                params.add(new BasicNameValuePair("pulse", puls));

                if (visitorID.equals("")) {
                    visitorID = "0";
                }
                params.add(new BasicNameValuePair("VisitorEntryID", visitorID));
                System.out.println("VisitorEntryID" + visitorID);
                if (VisitorExist.equals("1")) {
                    params.add(new BasicNameValuePair("vFName", vFirstName.getText().toString()));
                    System.out.println("vFName" + vFirstName.getText().toString());
                    params.add(new BasicNameValuePair("vLName", vLastName.getText().toString()));
                    System.out.println("vLName" + vLastName.getText().toString());
                } else {
                    params.add(new BasicNameValuePair("vFName", nVFname.getText().toString()));
                    System.out.println("vFName" + nVFname.getText().toString());
                    params.add(new BasicNameValuePair("vLName", nVLname.getText().toString()));
                    System.out.println("vLName" + nVLname.getText().toString());
                }

                params.add(new BasicNameValuePair("vmobNumber", vContactNo.getText().toString()));
                System.out.println("vmobNumber" + vContactNo.getText().toString());
                params.add(new BasicNameValuePair("vNote", vNote.getText().toString()));
                System.out.println("vNote" + vNote.getText().toString());
                params.add(new BasicNameValuePair("Gate_no", EntryGateNo));
                System.out.println("Gate_no" + EntryGateNo);
                params.add(new BasicNameValuePair("vCompany", expcompany));
                System.out.println("CompanyName" + expcompany);
                params.add(new BasicNameValuePair("vVehicle", vVehicle.getText().toString()));
                System.out.println("vVehicle" + vVehicle.getText().toString());
                params.add(new BasicNameValuePair("purpose_id", purposeID));
                params.add(new BasicNameValuePair("vCompanyOther", otherCompanyVisitor.getText().toString()));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);

                Log.e("PARAMSPARAMS", String.valueOf(url_login) + " " + params);
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

    /* ----- Submit Visitor  With Otp (Existing  And New Visitor) ------ */
    private class SubmitOTPVisitor extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    // JSONObject responseData = jsondata.getJSONObject("ServiceReqID");
                    VisitorID1 = jsondata.getString("VisitorEntryID");
                    if (VisitorExist.equals("0") || visitortrace.equals("1")) {

                        uploadFile();
                    }
                    vID = jsondata.getString("VisitorID");
                    // System.out.println("vID" +vID);
                    if (!vID.equals("0")) {
                        System.out.println("vID" + vID);

                        Intent addUnit = new Intent(VisitorEntry.this, visitor_unit.class);
                        addUnit.putExtra("vID", vID);
                        addUnit.putExtra("visitorid", VisitorID1);
                        addUnit.putExtra("entryWith", entryDoc_id);
                        addUnit.putExtra("Doc_img", Doc_img);
                        startActivity(addUnit);

                    }

                }

            } catch (Exception e) {
            }
        }

        public String postData() {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "Visitor/SubmitwithVisitor";
                String token = session.gettoken();
                EntryGateNo = session.getGateNo();
                System.out.println("visitor Id : " + visitorID);

                if (visitorID.equals("")) {
                    visitorID = "0";
                }
                System.out.println("visitor Id : " + visitorID);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("EntryDoc_id", entryDoc_id));
                params.add(new BasicNameValuePair("temp", tem));
                params.add(new BasicNameValuePair("oxygen", oxy));
                params.add(new BasicNameValuePair("pulse", puls));
                System.out.println("Token : " + token);
                if (VisitorExist.equals("1")) {
                    params.add(new BasicNameValuePair("Fname", vFirstName.getText().toString()));
                    System.out.println("Fname : " + vFirstName.getText().toString());
                    params.add(new BasicNameValuePair("Lname", vLastName.getText().toString()));
                    System.out.println("Lname : " + vLastName.getText().toString());
                } else {
                    params.add(new BasicNameValuePair("Fname", nVFname.getText().toString()));
                    System.out.println("Fname : " + nVFname.getText().toString());
                    params.add(new BasicNameValuePair("Lname", nVLname.getText().toString()));
                    System.out.println("Lname : " + nVLname.getText().toString());
                }

                params.add(new BasicNameValuePair("vMobile", vContactNo.getText().toString()));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("VisitorEntryID", visitorID));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("vID", vID));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("vNote", vNote.getText().toString()));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("Gate_no", EntryGateNo));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("vCompany", CompanyName));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("vVehicle", vVehicle.getText().toString()));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("purpose_id", purposeID));
                System.out.println("Lname : " + nVLname.getText().toString());
                params.add(new BasicNameValuePair("vCompanyOther", otherCompanyVisitor.getText().toString()));
                System.out.println("Lname : " + nVLname.getText().toString());
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                Log.e("Visitor/SubmitwithVisitorParams", String.valueOf(params));
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

    private void startCropImageAcitivity(Uri imageUri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //image
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

            startActivityForResult(intent, CAMERA_PIC_REQUEST);
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
        String imageFileName = "V_" + timeStamp;
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

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
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

    /**
     * Receiving activity result method will be called after closing the camera
     * */

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
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

    public void selectImage1() {

        new MaterialDialog.Builder(VisitorEntry.this)
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
                                if (Build.VERSION.SDK_INT >= 23) {
                                    CropImage.startPickImageActivity(VisitorEntry.this);
                                } else {
                                    captureImage();
                                }



                                break;
                            case 2:
                                if (exp_flag.equals("1")) {
                                    expImage.setVisibility(View.GONE);
                                } else {
                                    newImage.setVisibility(View.GONE);
                                }
                                isImageAddedexp = false;
                                break;
                        }
                    }
                })
                .show();

    }

    private void uploadFile() {
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "Saving without image ", Toast.LENGTH_LONG).show();
            return;
        } else {
        }


        // Map is used to multipart the file using okhttp3.RequestBody
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(postPath);
        System.out.println("1 " + file);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse(""), VisitorID1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse(""), "1");

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

    public void selectImageVisitor(final View v) {

        new MaterialDialog.Builder(VisitorEntry.this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImage)
                .itemsIds(R.array.itemId)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {

                            case 0:
                                new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, URL, null);
                                break;
                            case 1:
                                visitortrace = "1";
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                                break;
                            case 2:
                                visitortrace = "1";
                                if (Build.VERSION.SDK_INT >= 23) {
                                    CropImage.startPickImageActivity(VisitorEntry.this);
                                } else {
                                    captureImage();
                                }

                                break;
                        }

                    }
                })
                .show();

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
}
