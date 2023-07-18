package com.softel.securitymanager.securitymanager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.softel.securitymanager.Util.PreferenceServices;
import com.softel.securitymanager.securitymanager.Permission.ActivityManagePermission;
import com.softel.securitymanager.securitymanager.Permission.PermissionResult;
import com.softel.securitymanager.securitymanager.model.Wings;
import com.loopj.android.http.BuildConfig;
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
import java.util.Calendar;
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

public class staff_add extends ActivityManagePermission {
    //new additions
    private EditText txtcontactcheck, txtSelfsocidcheck;
    private LinearLayout prevLinearLayout;
    private Button fetchdetails, cancelfetch;
    //
    private LinearLayout sPersonalLayout, sPersonalDetails, sContactLayout, sContactDetail, sDocumentLayout, sDocumentsdetails, ImageLayout, slayoutCat1, slayoutCat2, slayoutCat3;
    private TextView sGateNo, AddCategory;
    private ListView sCategory;
    private EditText sFullName, sDob, sWorkingSince, currAddress, txtContact1, txtContact2, txtRefName, RefAddress, RefContactNo, txtSelfsocid;
    private Spinner sCatSpr1, sCatSpr2, sCatSpr3;
    private RadioGroup sMaritalStatus, gender;
    private RadioButton yes, no, male, female;
    String ProviderID;
    private Spinner sCategoris;
    private ImageView sImages, staffImage;
    //aadhar
    private EditText aadhaarno;
    private ImageView sDOBbutton, sWOCbutton;
    ArrayList<categorylist> sCatlist;
    int i = 0, doc_count = 0;
    String staffcount;
    TextView txtstaffstatus;

    String Category, CategoryName, catID = "", catID1 = "", catID2 = "", checkimage = "0";
    private Button btn_next1, btn_next2, btn_submit;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    String imagepath = "";
    File cover;
    Uri mCropImageUri;

    private String mImageFileLocation = "";
    boolean isImageAddedexp = false;
    private static final int CROP_PIC = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private static final String TAG = "staff_add";
    private static final int CAMERA_CAPTURE = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    ProgressDialog pDialog;
    private String postPath;
    private String mediaPath;
    Uri outputUri;

    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener1;
    Session session;

    private Spinner vWingSpinner, vWingSpinner1, vWingSpinner2, vWingSpinner3;
    private Spinner vUnitSpinner, vUnitSpinner1, vUnitSpinner2, vUnitSpinner3;

    LinearLayout Wing, Wing1, Wing2, Wing3;
    LinearLayout Unit, Unit1, Unit2, Unit3;

    ArrayList<UnitCusttom> UnitList = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList1 = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList2 = new ArrayList<>();
    ArrayList<UnitCusttom> UnitList3 = new ArrayList<>();

    private TextView vAddMore, DocumentDetails;
    List<String> WingList = new ArrayList<>();
    List<String> docList = new ArrayList<>();
    List<String> unitList = new ArrayList<>();
    List<String> unitList1 = new ArrayList<>();
    List<String> unitList2 = new ArrayList<>();
    List<String> unitList3 = new ArrayList<>();
    ArrayAdapter<String> adapter, unitAdapter;
    String wingV = "", unitV = "", wing, unit_id, docV = "";
    int j = 0;
    ArrayList<String> UNITID = new ArrayList<>();
    ArrayList<String> unit_no = new ArrayList<>();
    ArrayList<String> OWNERNAME = new ArrayList<>();

    LinearLayout documentlayout1, documentlayout2, documentlayout3, documentlayout4, documentlayout5, documentlayout6, dhedLayout;
    LinearLayout UpDocLayout, UpDocLayout1, UpDocLayout2, UpDocLayout3, UpDocLayout4, UpDocLayout5;
    Button btnUpload, btnUpload1, btnUpload2, btnUpload3, btnUpload4, btnUpload5;
    TextView txt_add_document, txt_deleteDocument;
    Spinner docSpinner, docSpinner1, docSpinner2, docSpinner3, docSpinner4, docSpinner5;
    ImageView staffimagedoc, staffimagedoc1, staffimagedoc2, staffimagedoc3, staffimagedoc4, staffimagedoc5;
    String doc_str, docid, docid1, docid2, docid3, docid4, docid5, display_image = "0", DOCIDUPLOAD;
    ArrayList<Document> doclist = new ArrayList<>();
    ArrayList<Wings> wingsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add);
        setTitle(R.string.new_staff_entry);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(getApplicationContext());
        PreferenceServices.init(this);
        String GateNo = session.getGateNo();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        dhedLayout = (LinearLayout) findViewById(R.id.dhedLayout);
        txt_add_document = (TextView) findViewById(R.id.addDocument);
        txt_deleteDocument = (TextView) findViewById(R.id.deleteDocument);
        DocumentDetails = (TextView) findViewById(R.id.DocumentDetails);
        txtSelfsocid = (EditText) findViewById(R.id.txtSelfsocid);
        txtstaffstatus = (TextView) findViewById(R.id.txtstaffstatus);

        txt_deleteDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doc_count == 1) {
                    documentlayout2.setVisibility(View.GONE);
                    txt_add_document.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.GONE);
                    doc_count--;
                } else if (doc_count == 2) {
                    documentlayout3.setVisibility(View.GONE);
                    txt_add_document.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.GONE);
                    doc_count--;
                } else if (doc_count == 3) {
                    documentlayout4.setVisibility(View.GONE);
                    txt_add_document.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.GONE);
                    doc_count--;
                } else if (doc_count == 4) {
                    documentlayout5.setVisibility(View.GONE);
                    txt_add_document.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.GONE);
                    doc_count--;
                } else if (doc_count == 5) {
                    documentlayout6.setVisibility(View.GONE);
                    txt_add_document.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.GONE);
                    doc_count--;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.youhaveaddedallrequirements), Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_add_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_count++;
                if (doc_count == 1) {
                    documentlayout2.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.VISIBLE);
                    txt_add_document.setVisibility(View.GONE);
                } else if (doc_count == 2) {
                    documentlayout3.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.VISIBLE);
                    txt_add_document.setVisibility(View.GONE);
                } else if (doc_count == 3) {
                    documentlayout4.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.VISIBLE);
                    txt_add_document.setVisibility(View.GONE);
                } else if (doc_count == 4) {
                    documentlayout5.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.VISIBLE);
                    txt_add_document.setVisibility(View.GONE);
                } else if (doc_count == 5) {
                    documentlayout6.setVisibility(View.VISIBLE);
                    txt_deleteDocument.setVisibility(View.VISIBLE);
                    txt_add_document.setVisibility(View.GONE);
                } else {

                    txt_add_document.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.youhaveaddedallrequirements), Toast.LENGTH_SHORT).show();
                }

            }
        });


        staffimagedoc = (ImageView) findViewById(R.id.Imagestaffdoc);
        staffimagedoc1 = (ImageView) findViewById(R.id.Imagestaffdoc1);
        staffimagedoc2 = (ImageView) findViewById(R.id.Imagestaffdocb);
        staffimagedoc3 = (ImageView) findViewById(R.id.Imagestaffdocc);
        staffimagedoc4 = (ImageView) findViewById(R.id.Imagestaffdocdd);
        staffimagedoc5 = (ImageView) findViewById(R.id.Imagestaffdocde);

        documentlayout1 = (LinearLayout) findViewById(R.id.document1L);
        documentlayout2 = (LinearLayout) findViewById(R.id.document2L);
        documentlayout3 = (LinearLayout) findViewById(R.id.document3L);
        documentlayout4 = (LinearLayout) findViewById(R.id.document4L);
        documentlayout5 = (LinearLayout) findViewById(R.id.document5L);
        documentlayout6 = (LinearLayout) findViewById(R.id.document6L);

        docSpinner = (Spinner) findViewById(R.id.docSpinner);
        docSpinner1 = (Spinner) findViewById(R.id.docSpinner1);
        docSpinner2 = (Spinner) findViewById(R.id.docSpinner2);
        docSpinner3 = (Spinner) findViewById(R.id.docSpinner3);
        docSpinner4 = (Spinner) findViewById(R.id.docSpinner4);
        docSpinner5 = (Spinner) findViewById(R.id.docSpinner5);

        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload1 = (Button) findViewById(R.id.btnUpload1);
        btnUpload2 = (Button) findViewById(R.id.btnUpload2);
        btnUpload3 = (Button) findViewById(R.id.btnUpload3);
        btnUpload4 = (Button) findViewById(R.id.btnUpload4);
        btnUpload5 = (Button) findViewById(R.id.btnUpload5);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                DOCIDUPLOAD = docid;
                uploadFile();
                btnUpload.setText(getString(R.string.uploaded));
                btnUpload.setEnabled(false);
                btnUpload.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);

            }
        });
        btnUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout1.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                DOCIDUPLOAD = docid1;
                uploadFile();
                btnUpload1.setText(getString(R.string.uploaded));
                btnUpload1.setEnabled(false);
                btnUpload1.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);
            }
        });
        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout2.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                DOCIDUPLOAD = docid2;
                uploadFile();
                btnUpload2.setText(getString(R.string.uploaded));
                btnUpload2.setEnabled(false);
                btnUpload2.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);
            }
        });
        btnUpload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout3.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                DOCIDUPLOAD = docid3;
                uploadFile();
                btnUpload3.setText(getString(R.string.uploaded));
                btnUpload3.setEnabled(false);
                btnUpload3.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);
            }
        });
        btnUpload4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout4.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                DOCIDUPLOAD = docid4;
                uploadFile();
                btnUpload4.setText(getString(R.string.uploaded));
                btnUpload4.setEnabled(false);
                btnUpload4.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);
            }
        });
        btnUpload5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDocLayout5.setVisibility(View.GONE);
                txt_deleteDocument.setVisibility(View.GONE);
                txt_add_document.setVisibility(View.GONE);
                DOCIDUPLOAD = docid5;
                uploadFile();
                btnUpload5.setText(getString(R.string.uploaded));
                btnUpload5.setEnabled(false);
                btnUpload5.setBackgroundColor(Color.argb(255, 133, 189, 199));
                txt_add_document.setVisibility(View.VISIBLE);
            }
        });
        new getDocument().execute();

        docSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                    docV = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid = doc.getDocID();
                    doc_str = doc.getDocName();
                    docV = doc_str;
                    System.out.println("DOC : " + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        docSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid1 = doc.getDocID();
                    doc_str = doc.getDocName();
                    Log.e("DOC : ", "" + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        docSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid2 = doc.getDocID();
                    doc_str = doc.getDocName();
                    System.out.println("DOC : " + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        docSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid3 = doc.getDocID();
                    doc_str = doc.getDocName();
                    System.out.println("DOC : " + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        docSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid4 = doc.getDocID();
                    doc_str = doc.getDocName();
                    System.out.println("DOC : " + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        docSpinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_document_type))) {
                    doc_str = parent.getItemAtPosition(position).toString();
                } else {
                    Document doc = doclist.get(position);
                    docid5 = doc.getDocID();
                    doc_str = doc.getDocName();
                    System.out.println("DOC : " + docid + " " + doc_str);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gender = (RadioGroup) findViewById(R.id.gender);
        aadhaarno = (EditText) findViewById(R.id.txtAadhaar);
        male = (RadioButton) findViewById(R.id.radiomale);
        female = (RadioButton) findViewById(R.id.radiofemale);
        sGateNo = (TextView) findViewById(R.id.gateNo);
        sGateNo.append(GateNo);
        sCatlist = new ArrayList<>();
        sFullName = (EditText) findViewById(R.id.txtName);
        sDob = (EditText) findViewById(R.id.txtDOB);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton) findViewById(R.id.no);
        sWorkingSince = (EditText) findViewById(R.id.txtWorkSince);
        sMaritalStatus = (RadioGroup) findViewById(R.id.StatusMarital);
        sDOBbutton = (ImageView) findViewById(R.id.dobBtn);
        sWOCbutton = (ImageView) findViewById(R.id.wosBtn);
        sPersonalLayout = (LinearLayout) findViewById(R.id.HeadingLayout);
        sPersonalDetails = (LinearLayout) findViewById(R.id.MainLayout);
        btn_next1 = (Button) findViewById(R.id.Snext);
        btn_next2 = (Button) findViewById(R.id.addBtn);
        currAddress = (EditText) findViewById(R.id.curAddress);
        txtContact1 = (EditText) findViewById(R.id.txtContact1);
        txtContact2 = (EditText) findViewById(R.id.txtContact2);
        txtRefName = (EditText) findViewById(R.id.txtRefName);
        RefAddress = (EditText) findViewById(R.id.RefAddress);
        RefContactNo = (EditText) findViewById(R.id.txtRefContact);
        ImageLayout = (LinearLayout) findViewById(R.id.ImageLayout);
        staffImage = (ImageView) findViewById(R.id.Imagestaff);


        //new additions
        prevLinearLayout = (LinearLayout) findViewById(R.id.prevstaffLayout);
        txtcontactcheck = (EditText) findViewById(R.id.txtContactcheck);
        txtSelfsocidcheck = (EditText) findViewById(R.id.txtSelfsocidcheck);
        fetchdetails = (Button) findViewById(R.id.fetchdetails);
        cancelfetch = (Button) findViewById(R.id.cancelfetch);
        cancelfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevLinearLayout.setVisibility(View.GONE);
                sPersonalLayout.setVisibility(View.VISIBLE);
                sPersonalDetails.setVisibility(View.VISIBLE);
            }
        });
        fetchdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcontactcheck.getText().toString().length() == 0 && txtSelfsocidcheck.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_phone_or_id), Toast.LENGTH_LONG).show();
                } else {

                    new fetchstaffdetails().execute();

                    Toast.makeText(getApplicationContext(), getString(R.string.details_fetched), Toast.LENGTH_LONG).show();
                    prevLinearLayout.setVisibility(View.GONE);
                    sPersonalLayout.setVisibility(View.VISIBLE);
                    sPersonalDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        //
        //working since change:


        TextWatcher td = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    sDob.setText(current);
                    sDob.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        sDob.addTextChangedListener(td);
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    sWorkingSince.setText(current);
                    sWorkingSince.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        sWorkingSince.addTextChangedListener(tw);

        sDocumentLayout = (LinearLayout) findViewById(R.id.DocumentLayout);
        sDocumentsdetails = (LinearLayout) findViewById(R.id.DocMainLayout);

        UpDocLayout = (LinearLayout) findViewById(R.id.UpDocLayout);
        UpDocLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "1";
                selectImage1();

            }
        });
        UpDocLayout1 = (LinearLayout) findViewById(R.id.UpDocLayout1);
        UpDocLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "2";
                selectImage1();

            }
        });
        UpDocLayout2 = (LinearLayout) findViewById(R.id.UpDocLayout2);
        UpDocLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "3";
                selectImage1();

            }
        });
        UpDocLayout3 = (LinearLayout) findViewById(R.id.UpDocLayout3);
        UpDocLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "4";
                selectImage1();

            }
        });
        UpDocLayout4 = (LinearLayout) findViewById(R.id.UpDocLayout4);
        UpDocLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "5";
                selectImage1();

            }
        });
        UpDocLayout5 = (LinearLayout) findViewById(R.id.UpDocLayout5);
        UpDocLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display_image = "6";
                selectImage1();

            }
        });
        btn_submit = (Button) findViewById(R.id.uplodbtn);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (staffcount.equals("1")) {
                    if (docV.equals(getString(R.string.select_document_type))) {
                        Toast.makeText(getApplicationContext(), getString(R.string.please_attach_atleast_one_document), Toast.LENGTH_LONG).show();
                    } else if (wingV.equals(getString(R.string.select_wing))) {
                        setSpinnerError(vWingSpinner, getString(R.string.please_select_wing));
                    } else if (unitV.equals(getString(R.string.select_unit))) {
                        setSpinnerError(vUnitSpinner, getString(R.string.please_select_unit));
                    } else {
                        new insertdocument().execute();
                    }
                }
                if (staffcount.equals("0")) {
                    if (wingV.equals(getString(R.string.select_wing))) {
                        setSpinnerError(vWingSpinner, getString(R.string.please_select_wing));
                    } else if (unitV.equals(getString(R.string.select_unit))) {
                        setSpinnerError(vUnitSpinner, getString(R.string.please_select_unit));
                    } else {
                        new insertdocument().execute();
                    }
                }
            }
        });

        ImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
                //captureImage();
            }
        });
        sContactLayout = (LinearLayout) findViewById(R.id.ContactLayout);
        sContactDetail = (LinearLayout) findViewById(R.id.cDetailmLayout);
        btn_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSelfsocid.getText().toString().length() == 0) {
                    txtSelfsocid.requestFocus();
                    txtSelfsocid.setError(getString(R.string.field_cannot_be_empty));
                } else if (sFullName.getText().toString().length() == 0) {
                    sFullName.requestFocus();
                    sFullName.setError(getString(R.string.field_cannot_be_empty));
                } else if (sWorkingSince.getText().toString().length() == 0) {
                    sWorkingSince.requestFocus();
                    sWorkingSince.setError(getString(R.string.field_cannot_be_empty));
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    sWorkingSince.setText(currentDate);
                } else if (txtContact1.getText().toString().length() == 0) {
                    txtContact1.requestFocus();
                    txtContact1.setError(getString(R.string.field_cannot_be_empty));
                } else if (txtContact1.getText().toString().length() > 10 || txtContact1.getText().toString().length() < 10) {
                    txtContact1.requestFocus();
                    txtContact1.setError(getString(R.string.entered_mobile_incorrect));
                } else if (CategoryName.equals(getString(R.string.select_category))) {
                    Toast.makeText(staff_add.this, "Select category", Toast.LENGTH_SHORT).show();
                } else if (!isImageAddedexp) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_select_image), Toast.LENGTH_LONG).show();
                } else {
                    new checkstaffid_status().execute();
                }
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currAddress.getText().toString().length() == 0) {
                    currAddress.requestFocus();
                    currAddress.setError(getString(R.string.field_cannot_be_empty));
                } /*else if (txtRefName.getText().toString().length() == 0) {
                    txtRefName.requestFocus();
                    txtRefName.setError(getString(R.string.field_cannot_be_empty));
                } */ else if (txtContact2.getText().toString().length() > 0 && (txtContact2.getText().toString().length() < 10 || txtContact2.getText().toString().length() > 10)) {
                    txtContact2.requestFocus();
                    txtContact2.setError(getString(R.string.entered_mobile_incorrect));
                } else if (RefContactNo.getText().toString().length() > 0 && (RefContactNo.getText().toString().length() < 10 || RefContactNo.getText().toString().length() > 10)) {
                    RefContactNo.requestFocus();
                    RefContactNo.setError(getString(R.string.entered_mobile_incorrect));
                } else {
                    new insertcontact().execute();
                }
            }
        });
        /* Staff Date Of Birth Sction  */
        sDOBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        staff_add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String mon = Integer.toString(month), dy = Integer.toString(day);

                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                if (month < 10) {
                    mon = "0" + mon;
                }
                if (day < 10) {
                    dy = "0" + dy;
                }

                String date = month + "/" + day + "/" + year;
                sDob.setText(dy + "-" + mon + "-" + year);
            }
        };

        /*  Staff working Since Section */
        sWOCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        staff_add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String mon = Integer.toString(month), dy = Integer.toString(day);

                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                if (month < 10) {
                    mon = "0" + mon;
                }
                if (day < 10) {
                    dy = "0" + dy;
                }

                String date = month + "/" + day + "/" + year;
                sWorkingSince.setText(dy + "-" + mon + "-" + year);
            }
        };

        new getCategory().execute();

        AddCategory = (TextView) findViewById(R.id.addmore);
        slayoutCat1 = (LinearLayout) findViewById(R.id.catLayout);
        slayoutCat2 = (LinearLayout) findViewById(R.id.catLayout2);
        slayoutCat3 = (LinearLayout) findViewById(R.id.catLayout3);
        sCatSpr1 = (Spinner) findViewById(R.id.cat1);
        sCatSpr2 = (Spinner) findViewById(R.id.cat2);
        sCatSpr3 = (Spinner) findViewById(R.id.cat3);
        AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i == 1) {

                    slayoutCat2.setVisibility(View.VISIBLE);

                }
                if (i == 2) {
                    slayoutCat3.setVisibility(View.VISIBLE);
                }
                if (i > 2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.add_only_3_category), Toast.LENGTH_SHORT).show();
                }
            }
        });
        sCatSpr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_category))) {
                    CategoryName = parent.getItemAtPosition(position).toString();
                } else {
                    categorylist cat = sCatlist.get(position);

                    CategoryName = cat.getCategoryName();
                    catID = cat.getCatId();
                    Category = parent.getItemAtPosition(position).toString();
                    // new getUnit().execute(String.valueOf(position));
                    // new visitor_unit.getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sCatSpr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_category))) {
                    // do nothing
                } else {
                    categorylist cat = sCatlist.get(position);

                    CategoryName = cat.getCategoryName();
                    catID1 = cat.getCatId();
                    Category = parent.getItemAtPosition(position).toString();
                    // new getUnit().execute(String.valueOf(position));
                    // new visitor_unit.getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sCatSpr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_category))) {
                    // do nothing
                } else {
                    categorylist cat = sCatlist.get(position);

                    CategoryName = cat.getCategoryName();
                    catID2 = cat.getCatId();
                    Category = parent.getItemAtPosition(position).toString();
                    // new getUnit().execute(String.valueOf(position));
                    // new visitor_unit.getUnit().execute(String.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Wing = (LinearLayout) findViewById(R.id.wingLayout);
        Wing1 = (LinearLayout) findViewById(R.id.wingLayout1);
        Wing2 = (LinearLayout) findViewById(R.id.wingLayout2);
        Wing3 = (LinearLayout) findViewById(R.id.wingLayout3);

        Unit = (LinearLayout) findViewById(R.id.unitLayout);
        Unit1 = (LinearLayout) findViewById(R.id.unitLayout1);
        Unit2 = (LinearLayout) findViewById(R.id.unitLayout2);
        Unit3 = (LinearLayout) findViewById(R.id.unitLayout3);

        vAddMore = (TextView) findViewById(R.id.addUnit);

        vAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j++;
                if (j == 1) {
                    Wing1.setVisibility(View.VISIBLE);
                    Unit1.setVisibility(View.VISIBLE);
                }
                if (j == 2) {

                    Wing2.setVisibility(View.VISIBLE);
                    Unit2.setVisibility(View.VISIBLE);
                }
                if (j == 3) {

                    Wing3.setVisibility(View.VISIBLE);
                    Unit3.setVisibility(View.VISIBLE);
                }
                if (j > 3) {
                    Toast.makeText(getApplicationContext(), getString(R.string.flats_less_than_4), Toast.LENGTH_SHORT).show();
                }
            }
        });

        vWingSpinner = (Spinner) findViewById(R.id.wingSpinner);
        vWingSpinner1 = (Spinner) findViewById(R.id.wingSpinner1);
        vWingSpinner2 = (Spinner) findViewById(R.id.wingSpinner2);
        vWingSpinner3 = (Spinner) findViewById(R.id.wingSpinner3);
        vUnitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        vUnitSpinner1 = (Spinner) findViewById(R.id.unitSpinner1);
        vUnitSpinner2 = (Spinner) findViewById(R.id.unitSpinner2);
        vUnitSpinner3 = (Spinner) findViewById(R.id.unitSpinner3);
        new getWing().execute();

        vWingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wings selection = (Wings) parent.getItemAtPosition(position);

                if (selection.getWing().equalsIgnoreCase(getString(R.string.select_wing))) {
                    wingV = selection.getWing();
                    // do nothing
                } else {

                    wing = selection.getWing();
                    wingV = selection.getWing();

                    Log.e("WINGID::", wing + "  " + wingV + " " + selection.getWing() + " " + selection.getWing_id());
                    if (PreferenceServices.getInstance().getFlats().isEmpty()) {
                        new getUnit().execute(selection.getWing_id());
                    } else {
                        displayFlats(selection.getWing_id());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wings selection = (Wings) parent.getItemAtPosition(position);

                if (selection.getWing().equals(getString(R.string.select_wing))) {
                    wingV = selection.getWing();
                    // do nothing
                } else {

                    wing = selection.getWing();
                    wingV = selection.getWing();

                    Log.e("WINGID::", wing + "  " + wingV + " " + id + " " + selection.getWing_id());
                    if (PreferenceServices.getInstance().getFlats().isEmpty()) {
                        new getUnit().execute(selection.getWing_id());
                    } else {
                        displayFlats(selection.getWing_id());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wings selection = (Wings) parent.getItemAtPosition(position);

                if (selection.getWing().equals(getString(R.string.select_wing))) {
                    wingV = selection.getWing();
                    // do nothing
                } else {

                    wing = selection.getWing();
                    wingV = selection.getWing();

                    Log.e("WINGID::", wing + "  " + wingV + " " + id + " " + selection.getWing_id());
                    if (PreferenceServices.getInstance().getFlats().isEmpty()) {
                        new getUnit().execute(selection.getWing_id());
                    } else {
                        displayFlats(selection.getWing_id());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vWingSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wings selection = (Wings) parent.getItemAtPosition(position);

                if (selection.getWing().equals(getString(R.string.select_wing))) {
                    wingV = selection.getWing();
                    // do nothing
                } else {

                    wing = selection.getWing();
                    wingV = selection.getWing();

                    Log.e("WINGID::", wing + "  " + wingV + " " + id + " " + selection.getWing_id());
                    if (PreferenceServices.getInstance().getFlats().isEmpty()) {
                        new getUnit().execute(selection.getWing_id());
                    } else {
                        displayFlats(selection.getWing_id());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.select_unit))) {
                    unitV = parent.getItemAtPosition(position).toString();
                } else {

                    UnitCusttom unit = UnitList.get(position);
                    unit_id = unit.getUnitId();
                    if (unit_id.equals("-1")) {

                    } else {

                        UNITID.add(unit.getUnitId());
                        unit_no.add(unit.getUnitNo());
                        OWNERNAME.add(unit.getOwnerName());
                        System.out.println(unit.getOwnerName());
                    }
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

                    unit_id = unit.getUnitId();
                    if (unit_id.equals("-1")) {

                    } else {

                        UNITID.add(unit.getUnitId());
                        unit_no.add(unit.getUnitNo());
                        OWNERNAME.add(unit.getOwnerName());
                        System.out.println(unit.getOwnerName());

                    }

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
                    unit_id = unit.getUnitId();
                    if (unit_id.equals("-1")) {

                    } else {

                        UNITID.add(unit.getUnitId());
                        unit_no.add(unit.getUnitNo());
                        OWNERNAME.add(unit.getOwnerName());
                        System.out.println(unit.getOwnerName());

                    }
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
                    unit_id = unit.getUnitId();
                    if (unit_id.equals("-1")) {

                    } else {

                        UNITID.add(unit.getUnitId());
                        unit_no.add(unit.getUnitNo());
                        OWNERNAME.add(unit.getOwnerName());
                    }

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new getLastEnteredStaff().execute();

    }

    private class fetchstaffdetails extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    JSONObject res = obj.getJSONObject("response");
                    JSONArray jsonstaff = res.getJSONArray("StaffDetails");
                    JSONObject staff = jsonstaff.getJSONObject(0);
                    System.out.println("full name : " + staff.getString("full_name"));
                    sFullName.setText(staff.getString("full_name"));
                    aadhaarno.setText(staff.getString("adhar_card_no"));
                    txtSelfsocid.setText(staff.getString("service_prd_reg_id"));
                    sDob.setText(staff.getString("dob"));
                    try {
                        String g = staff.getString("gender");
                        if (g == "male") {
                            male.setChecked(true);
                        } else if (g == "female") {
                            female.setChecked(true);
                        }
                    } catch (Exception e) {
                        Toast.makeText(staff_add.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }


                    //sWorkingSince.setText(staff.getString("2015-03-01"));
                    String[] date1 = staff.getString("since").split("-");
                    String sday = date1[2];
                    String smonth = date1[1];
                    String syear = date1[0];
                    String smon = sday + "" + smonth + "" + syear;
                    sWorkingSince.setText(smon);
                    txtContact1.setText(staff.getString("cur_con_1"));
                    txtContact2.setText(staff.getString("cur_con_2"));
                    try {
                        String g = staff.getString("marry");
                        if (g == "Yes") {
                            yes.setChecked(true);
                        } else if (g == "No") {
                            no.setChecked(true);
                        }
                    } catch (Exception e) {
                        Toast.makeText(staff_add.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                    //categories
                    //staffImage.setimage
                    currAddress.setText(staff.getString("cur_resd_add"));
                    txtRefName.setText(staff.getString("ref_name"));
                    RefAddress.setText(staff.getString("ref_add"));
                    RefContactNo.setText(staff.getString("ref_con_1"));
                    //documents
                    //wings
                    //ownernames


                } else {
                    Toast.makeText(staff_add.this, getString(R.string.staff_not_found), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/GetStaffDetails";
                String contact = txtcontactcheck.getText().toString();
                String soc_staffid = txtSelfsocidcheck.getText().toString();
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyId", session.getSocietyid()));
                params.add(new BasicNameValuePair("contact", contact));
                params.add(new BasicNameValuePair("soc_staffid", soc_staffid));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class getLastEnteredStaff extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    JSONObject res = obj.getJSONObject("response");
                    JSONArray jsonstaff = res.getJSONArray("StaffID");
                    JSONObject staff = jsonstaff.getJSONObject(0);
                    System.out.println("Staff : " + staff.getString("StaffIDL"));
                    txtstaffstatus.setText(getString(R.string.last_added_staff_id) + staff.getString("StaffIDL"));

                } else {
                    txtstaffstatus.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/GetStaffIdStatus";

                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyId", session.getSocietyid()));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class checkstaffid_status extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    txtSelfsocid.requestFocus();
                    txtSelfsocid.setError(getString(R.string.please_enter_unique_socid));

                } else {
                    new insertpersonal().execute();
                }
            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/CheckStaffIdStatus";

                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyId", session.getSocietyid()));
                params.add(new BasicNameValuePair("ProviderId", txtSelfsocid.getText().toString()));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class insertdocument extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {

                    sDocumentLayout.setVisibility(View.GONE);
                    sDocumentsdetails.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.staff_added_successfully), Toast.LENGTH_SHORT).show();
                    Intent welcome = new Intent(staff_add.this, homepage.class);
                    startActivity(welcome);
                }
            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/AddStaffUnit";

                String unitid1 = "";
                for (String s : UNITID) {
                    unitid1 += s + ",";
                }

                String ownername1 = "", unitno1 = "";
                for (String s : OWNERNAME) {
                    ownername1 += s + ",";
                }
                for (String s : unit_no) {
                    unitno1 += s + ",";
                }

                unitid1 = unitid1.substring(0, unitid1.length() - 1);
                ownername1 = ownername1.substring(0, ownername1.length() - 1);
                unitno1 = unitno1.substring(0, unitno1.length() - 1);

                System.out.println("unit id : " + unitid1);
                System.out.println("ownername : " + ownername1);
                System.out.println("unitno : " + unitno1);
                System.out.println("prov : " + ProviderID);
                System.out.println("soci" + session.getSocietyid());
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyId", session.getSocietyid()));
                params.add(new BasicNameValuePair("ProviderId", ProviderID));
                params.add(new BasicNameValuePair("unitid", unitid1));
                params.add(new BasicNameValuePair("unitno", unitno1));
                params.add(new BasicNameValuePair("ownername", ownername1));


                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");

                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                //  Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class getDocument extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("success").equals("1")) {
                    JSONObject response = obj.getJSONObject("response");


                    JSONArray document = response.getJSONArray("DocList");
                    doclist.add(new Document("0", getString(R.string.select_document_type)));
                    docList.add(0, getString(R.string.select_document_type));

                    for (int i = 0; i < document.length(); i++) {
                        JSONObject objData = document.getJSONObject(i);
                        doclist.add(new Document(
                                objData.getString("document_id"),
                                objData.getString("document")
                        ));
                        docList.add(objData.getString("document"));
                    }
                    //DocumentAdapter adapter = new DocumentAdapter(getApplicationContext(), R.layout.doclayout, R.id.docList, doclist);
                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, docList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    docSpinner.setAdapter(adapter);
                    docSpinner1.setAdapter(adapter);
                    docSpinner2.setAdapter(adapter);
                    docSpinner3.setAdapter(adapter);
                    docSpinner4.setAdapter(adapter);
                    docSpinner5.setAdapter(adapter);

                }
            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/fetchDocList";
                System.out.println("Society id : " + session.getSocietyid());
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("SocietyID", session.getSocietyid()));

                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "GET", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                // Toast.makeText(staff_add.this,getString(R.string.kindly_check_internet),Toast.LENGTH_SHORT);
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

    private class insertcontact extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    sContactLayout.setVisibility(View.GONE);
                    sContactDetail.setVisibility(View.GONE);
                    sDocumentLayout.setVisibility(View.VISIBLE);
                    sDocumentsdetails.setVisibility(View.VISIBLE);
                    /*Toast.makeText(getApplicationContext(),"Staff Added Successfully",Toast.LENGTH_SHORT).show();
                    Intent welcome = new Intent(staff_add.this,homepage.class);
                    startActivity(welcome);*/
                }
            } catch (Exception e) {
            }

        }

        public String postData() {
            String mstatus = "No";
            String gstatus = "male";
            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/AddStaffContact";
                System.out.println("Society id : " + session.getSocietyid());
                String token = session.gettoken();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("societyID", session.getSocietyid()));
                params.add(new BasicNameValuePair("ProviderId", ProviderID));
                params.add(new BasicNameValuePair("curAddress", currAddress.getText().toString()));
                params.add(new BasicNameValuePair("curContact2", txtContact2.getText().toString()));
                params.add(new BasicNameValuePair("refName", txtRefName.getText().toString()));
                params.add(new BasicNameValuePair("refAdd", RefAddress.getText().toString()));
                params.add(new BasicNameValuePair("refContact", RefContactNo.getText().toString()));


                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class insertpersonal extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                if (obj.getString("success").equals("1")) {
                    JSONObject jsonres = obj.getJSONObject("response");
                    ProviderID = String.valueOf(jsonres.getInt("StaffID "));
                    staffcount = jsonres.getString("staff_count");
                    System.out.println("Staff Count : " + staffcount);
                    Toast.makeText(staff_add.this, "Successfully added", Toast.LENGTH_LONG).show();
                    uploadFile();
                    if (staffcount.equals("1")) {
                        sPersonalLayout.setVisibility(View.GONE);
                        sPersonalDetails.setVisibility(View.GONE);
                        sContactLayout.setVisibility(View.VISIBLE);
                        sContactDetail.setVisibility(View.VISIBLE);
                    }
                    if (staffcount.equals("0")) {
                        sPersonalLayout.setVisibility(View.GONE);
                        sPersonalDetails.setVisibility(View.GONE);
                        sDocumentLayout.setVisibility(View.VISIBLE);
                        sDocumentsdetails.setVisibility(View.VISIBLE);
                        txt_add_document.setVisibility(View.GONE);
                        DocumentDetails.setText(getString(R.string.wing_selection));
                        dhedLayout.setVisibility(View.GONE);
                        documentlayout1.setVisibility(View.GONE);
                    }
                    checkimage = "1";

                    isImageAddedexp = false;
                }
            } catch (Exception e) {
            }

        }

        public String postData() {
            String mstatus = "No";
            String gstatus = "male";
            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/AddStaffPersonal";

                String category = "";
                if (!catID.equals("")) {
                    category += catID + ",";
                }
                if (!catID1.equals("")) {
                    category += catID1 + ",";
                }
                if (!catID2.equals("")) {
                    category += catID2 + ",";
                }
                category = category.substring(0, category.length() - 1);
                System.out.println(category);
                String token = session.gettoken();
                if (yes.isChecked()) {
                    mstatus = "Yes";
                }
                if (no.isChecked()) {
                    mstatus = "No";
                }
                if (male.isChecked()) {
                    gstatus = "male";
                }
                if (female.isChecked()) {
                    gstatus = "female";
                }
                String[] stdate = sWorkingSince.getText().toString().split("-");
                String sday = stdate[0];
                String smonth = stdate[1];
                String syear = stdate[2];

                String workingsince = syear + "-" + smonth + "-" + sday;

                System.out.println("Mstatus : " + mstatus);
                System.out.println("Gstatus : " + gstatus);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("societyID", session.getSocietyid()));
                params.add(new BasicNameValuePair("soc_staffid", txtSelfsocid.getText().toString()));
                params.add(new BasicNameValuePair("fullname", sFullName.getText().toString()));
                params.add(new BasicNameValuePair("dob", sDob.getText().toString()));
                params.add(new BasicNameValuePair("workingSince", workingsince));
                params.add(new BasicNameValuePair("marry", mstatus));
                params.add(new BasicNameValuePair("gender", gstatus));
                params.add(new BasicNameValuePair("adhar_card_no", aadhaarno.getText().toString()));
                params.add(new BasicNameValuePair("category", category));
                params.add(new BasicNameValuePair("curContact1", txtContact1.getText().toString()));
                System.out.println("parameters sent : " + params);
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("origresponseText:  " + origresponseText);
                System.out.println("Connection close");
                // session.setGateNo(gateno);
                // session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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

    private class getCategory extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");

                if (success.contains("1")) {

                    JSONObject jsondata = obj.getJSONObject("response");

                    JSONArray CatList = jsondata.getJSONArray("categoryList");

                    sCatlist.add(new categorylist("0", getString(R.string.select_category)));

                    for (int i = 0; i < CatList.length(); i++) {
                        JSONObject objData = CatList.getJSONObject(i);

                        sCatlist.add(new categorylist(
                                objData.getString("cat_id"),
                                objData.getString("cat")


                        ));
                    }
                    CustomAdapterCategory adapter = new CustomAdapterCategory(getApplicationContext(), R.layout.categorylist, R.id.categorydata, sCatlist);
                    sCatSpr1.setAdapter(adapter);
                    sCatSpr2.setAdapter(adapter);
                    sCatSpr3.setAdapter(adapter);

                }

            } catch (Exception e) {
            }

        }

        public String postData() {

            String origresponseText = "";
            try {
                String url_login = getString(R.string.url) + "ServiceProvider/fetchCategory";
                String token = session.gettoken();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                //params.add(new BasicNameValuePair("gateNo",gateno));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                // session.setGateNo(gateno);
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),getString(R.string.kindly_check_internet),Toast.LENGTH_SHORT);
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

    public void selectImage1() {
        new MaterialDialog.Builder(staff_add.this)
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
                                askCompactPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionResult() {
                                    @Override
                                    public void permissionGranted() {
                                        onSelectImageClick();
                                    }

                                    @Override
                                    public void permissionDenied() {
                                    }
                                });

                                break;
                            case 2:
                                if (checkimage.equals("0")) {
                                    staffImage.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("1")) {
                                    staffimagedoc.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("2")) {
                                    staffimagedoc1.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("3")) {
                                    staffimagedoc2.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("4")) {
                                    staffimagedoc3.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("5")) {
                                    staffimagedoc4.setVisibility(View.GONE);
                                }
                                if (checkimage.equals("1") && display_image.equals("6")) {
                                    staffimagedoc5.setVisibility(View.GONE);
                                }

                                isImageAddedexp = false;
                                break;
                        }
                    }
                })
                .show();

    }

    private void onSelectImageClick() {
        if (Build.VERSION.SDK_INT >= 23) {
            CropImage.startPickImageActivity(this);
        } else {
            captureImage();
        }
    }

    private void uploadFile() {
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_image), Toast.LENGTH_LONG).show();
            return;
        } else {

            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);
            System.out.println("1 " + file);
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse(""), ProviderID);

            map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
            map.put("ProviderId", requestBody1);
            System.out.println("Test : " + checkimage);
            if (checkimage.equals("0")) {
                System.out.println("0");
                RequestBody requestBody2 = RequestBody.create(MediaType.parse(""), "2");
                map.put("feature", requestBody2);
            } else {

                System.out.println("Else");
                RequestBody requestBody2 = RequestBody.create(MediaType.parse(""), "3");
                map.put("feature", requestBody2);
                System.out.println("DOC ID " + DOCIDUPLOAD);
                RequestBody requestBody3 = RequestBody.create(MediaType.parse(""), DOCIDUPLOAD);
                map.put("doc_id", requestBody3);

            }

            Log.e("PARAM", map.toString());

            // RequestBody time = createPartFromString("2016");

            //RequestBody requestBody2  = RequestBody.create(MediaType.parse(""), "2");
            // map.put("feature",requestBody2);

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

                        Toast.makeText(getApplicationContext(), getString(R.string.problem_loading_image), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.v("Response gotten is", t.getMessage());
                }
            });
        }
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

    private void startCropImageAcitivity(Uri imageUri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

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
                    imagepath = getPath(this, mCropImageUri);
                    Log.e("imagepath1", imagepath);
                    postPath = imagepath;
                    if (checkimage.equals("0")) {
                        staffImage.setVisibility(View.VISIBLE);
                        staffImage.getLayoutParams().height = 350;
                        staffImage.getLayoutParams().width = 350;
                        staffImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffImage.setImageURI(mCropImageUri);
                        Glide.with(this).load(mCropImageUri).into(staffImage);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("1")) {
                        staffimagedoc.setVisibility(View.VISIBLE);
                        staffimagedoc.getLayoutParams().height = 350;
                        staffimagedoc.getLayoutParams().width = 350;
                        staffimagedoc.setScaleType(ImageView.ScaleType.FIT_XY);
                        // staffimagedoc.setImageURI(mCropImageUri);
                        Glide.with(this).load(mCropImageUri).into(staffimagedoc);
                        btnUpload.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("2")) {
                        staffimagedoc1.setVisibility(View.VISIBLE);
                        staffimagedoc1.getLayoutParams().height = 350;
                        staffimagedoc1.getLayoutParams().width = 350;
                        staffimagedoc1.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc1.setImageURI(mCropImageUri);
                        btnUpload1.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("3")) {
                        staffimagedoc2.setVisibility(View.VISIBLE);
                        staffimagedoc2.getLayoutParams().height = 350;
                        staffimagedoc2.getLayoutParams().width = 350;
                        staffimagedoc2.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc2.setImageURI(mCropImageUri);
                        btnUpload2.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("4")) {
                        staffimagedoc3.setVisibility(View.VISIBLE);
                        staffimagedoc3.getLayoutParams().height = 350;
                        staffimagedoc3.getLayoutParams().width = 350;
                        staffimagedoc3.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc3.setImageURI(mCropImageUri);
                        btnUpload3.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("5")) {
                        staffimagedoc4.setVisibility(View.VISIBLE);
                        staffimagedoc4.getLayoutParams().height = 350;
                        staffimagedoc4.getLayoutParams().width = 350;
                        staffimagedoc4.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc4.setImageURI(mCropImageUri);
                        btnUpload4.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("6")) {
                        staffimagedoc5.setVisibility(View.VISIBLE);
                        staffimagedoc5.getLayoutParams().height = 350;
                        staffimagedoc5.getLayoutParams().width = 350;
                        staffimagedoc5.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc5.setImageURI(mCropImageUri);
                        btnUpload5.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    Log.e("media Path : ", "" + mCropImageUri);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.e("ERROR", String.valueOf(error));
                }
                // mediaPath = getRealPathFromURI(outputUri);
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {
                    staffImage.setVisibility(View.VISIBLE);
                    staffImage.getLayoutParams().height = 350;
                    staffImage.getLayoutParams().width = 350;
                    staffImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(this).load(mImageFileLocation).into(staffImage);
                    postPath = mImageFileLocation;
                    isImageAddedexp = true;

                    if (checkimage.equals("0")) {
                        staffImage.setVisibility(View.VISIBLE);
                        staffImage.getLayoutParams().height = 350;
                        staffImage.getLayoutParams().width = 350;
                        staffImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffImage);
                        postPath = mImageFileLocation;
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("1")) {
                        staffimagedoc.setVisibility(View.VISIBLE);
                        staffimagedoc.getLayoutParams().height = 350;
                        staffimagedoc.getLayoutParams().width = 350;
                        staffimagedoc.setScaleType(ImageView.ScaleType.FIT_XY);
                        // staffimagedoc.setImageURI(mCropImageUri);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc);
                        btnUpload.setVisibility(View.VISIBLE);
                        postPath = mImageFileLocation;

                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("2")) {
                        staffimagedoc1.setVisibility(View.VISIBLE);
                        staffimagedoc1.getLayoutParams().height = 350;
                        staffimagedoc1.getLayoutParams().width = 350;
                        staffimagedoc1.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc1);
                        postPath = mImageFileLocation;
                        btnUpload1.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("3")) {
                        staffimagedoc2.setVisibility(View.VISIBLE);
                        staffimagedoc2.getLayoutParams().height = 350;
                        staffimagedoc2.getLayoutParams().width = 350;
                        staffimagedoc2.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc2);
                        postPath = mImageFileLocation;
                        btnUpload2.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("4")) {
                        staffimagedoc3.setVisibility(View.VISIBLE);
                        staffimagedoc3.getLayoutParams().height = 350;
                        staffimagedoc3.getLayoutParams().width = 350;
                        staffimagedoc3.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc3);
                        postPath = mImageFileLocation;
                        btnUpload3.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("5")) {
                        staffimagedoc4.setVisibility(View.VISIBLE);
                        staffimagedoc4.getLayoutParams().height = 350;
                        staffimagedoc4.getLayoutParams().width = 350;
                        staffimagedoc4.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc4);
                        postPath = mImageFileLocation;
                        btnUpload4.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("6")) {
                        staffimagedoc5.setVisibility(View.VISIBLE);
                        staffimagedoc5.getLayoutParams().height = 350;
                        staffimagedoc5.getLayoutParams().width = 350;
                        staffimagedoc5.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(this).load(mImageFileLocation).into(staffimagedoc5);
                        postPath = mImageFileLocation;
                        btnUpload5.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                } else {
                    Glide.with(this).load(fileUri).into(staffImage);
                    postPath = fileUri.getPath();

                }

            } else if (requestCode == REQUEST_PICK_PHOTO) {
                if (data.getData() != null) {
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
                    if (checkimage.equals("0")) {
                        staffImage.setVisibility(View.VISIBLE);
                        staffImage.getLayoutParams().height = 350;
                        staffImage.getLayoutParams().width = 350;
                        staffImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffImage.setImageURI(selectedImage);

                        isImageAddedexp = true;
                    }

                    if (checkimage.equals("1") && display_image.equals("1")) {
                        staffimagedoc.setVisibility(View.VISIBLE);
                        staffimagedoc.getLayoutParams().height = 350;
                        staffimagedoc.getLayoutParams().width = 350;
                        staffimagedoc.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc.setImageURI(selectedImage);
                        btnUpload.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("2")) {
                        staffimagedoc1.setVisibility(View.VISIBLE);
                        staffimagedoc1.getLayoutParams().height = 350;
                        staffimagedoc1.getLayoutParams().width = 350;
                        staffimagedoc1.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc1.setImageURI(selectedImage);
                        btnUpload1.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("3")) {
                        staffimagedoc2.setVisibility(View.VISIBLE);
                        staffimagedoc2.getLayoutParams().height = 350;
                        staffimagedoc2.getLayoutParams().width = 350;
                        staffimagedoc2.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc2.setImageURI(selectedImage);
                        btnUpload2.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("4")) {
                        staffimagedoc3.setVisibility(View.VISIBLE);
                        staffimagedoc3.getLayoutParams().height = 350;
                        staffimagedoc3.getLayoutParams().width = 350;
                        staffimagedoc3.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc3.setImageURI(selectedImage);
                        btnUpload3.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("5")) {
                        staffimagedoc4.setVisibility(View.VISIBLE);
                        staffimagedoc4.getLayoutParams().height = 350;
                        staffimagedoc4.getLayoutParams().width = 350;
                        staffimagedoc4.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc4.setImageURI(selectedImage);
                        btnUpload4.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    if (checkimage.equals("1") && display_image.equals("6")) {
                        staffimagedoc5.setVisibility(View.VISIBLE);
                        staffimagedoc5.getLayoutParams().height = 350;
                        staffimagedoc5.getLayoutParams().width = 350;
                        staffimagedoc5.setScaleType(ImageView.ScaleType.FIT_XY);
                        staffimagedoc5.setImageURI(selectedImage);
                        UpDocLayout.setVisibility(View.GONE);
                        btnUpload5.setVisibility(View.VISIBLE);
                        isImageAddedexp = true;
                    }
                    cursor.close();
                    postPath = mediaPath;
                }
            } else if (resultCode != RESULT_CANCELED) {
                //Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }


        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
        }
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

    File createImageFile() throws IOException {
        mImageFileLocation = "";
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "STAFF_" + timeStamp;
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

    private class getWing extends AsyncTask<String, String, String> {

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                System.out.println("Result : " + obj);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    JSONObject jsondata = obj.getJSONObject("response");
                    JSONArray wingData = jsondata.getJSONArray("Wing");

                    // WingList.add(0, getString(R.string.select_wing));
                    for (int i = 0; i < wingData.length(); i++) {
                        JSONObject objData = wingData.getJSONObject(i);
                        Wings wings = new Wings();
                       /* if(i==0){
                            wings.setWing_id("0");
                            wings.setWing(getString(R.string.select_wing));
                        }else {*/
                        wings.setWing_id(objData.getString("wing_id"));
                        wings.setWing(objData.getString("wing"));
                        // }
                        //WingList.add(Integer.valueOf(objData.getString("wing_id")), objData.getString("wing"));
                        wingsList.add(wings);
                    }
                    ArrayAdapter<Wings> dataAdapter = new ArrayAdapter<Wings>(getApplicationContext(), R.layout.spinner_item, wingsList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    vWingSpinner.setAdapter(dataAdapter);
                    vWingSpinner1.setAdapter(dataAdapter);
                    vWingSpinner2.setAdapter(dataAdapter);
                    vWingSpinner3.setAdapter(dataAdapter);

                }
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
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
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
        String wingID = "";

        protected void onPostExecute(String result) {
            Log.d("on post ", "on post execute");
            try {
                JSONObject obj = new JSONObject(result);
                String success = obj.getString("success");
                if (success.contains("1")) {
                    PreferenceServices.getInstance().setFlats(result);
                    displayFlats(wingID);
                }
            } catch (Exception e) {
            }
        }

        public String postData(String wingID) {

            String origresponseText = "";
            try {
                System.out.println("Connection On");

                String url_login = getString(R.string.url) + "ServiceProvider/fetchUnits";
                String token = session.gettoken();
                //System.out.println("token" +token);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                //  params.add(new BasicNameValuePair("WingID",wingID));
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.makeHttpRequest(url_login, "POST", params);
                origresponseText = json.toString();
                System.out.println("Connection close");
                //session.setGateNo(gateno);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.kindly_check_internet), Toast.LENGTH_SHORT);
            }
            return origresponseText;
        }

        @Override
        protected String doInBackground(String... params) {
            wingID = (String) params[0];
            System.out.println("Wing in Back : " + wingID);
            String s = postData(wingID);
            return s;
        }

    }

    private void displayFlats(String wingID) {

        try {
            JSONObject object = new JSONObject(PreferenceServices.getInstance().getFlats());
            if (object.getString("success").equals("1")) {
                JSONObject jsonObject = object.getJSONObject("response");
                JSONArray jArrayValue = new JSONArray(jsonObject.getString("member"));

                if (j == 1) {
                    UnitList1.clear();
                    unitList1.clear();
                    UnitList1.add(new UnitCusttom("-1", getString(R.string.select_wing), "", "", ""));
                    UnitList1.add(new UnitCusttom("0", getString(R.string.society_office), "", session.getEmegency(), ""));
                    unitList1.add(getString(R.string.select_wing));
                    unitList1.add(getString(R.string.society_office));
                } else if (j == 2) {
                    UnitList2.clear();
                    unitList2.clear();
                    UnitList2.add(new UnitCusttom("-1", getString(R.string.select_wing), "", "", ""));
                    UnitList2.add(new UnitCusttom("0", getString(R.string.society_office), "", session.getEmegency(), ""));
                    unitList2.add(getString(R.string.select_wing));
                    unitList2.add(getString(R.string.society_office));
                } else if (j == 3) {
                    UnitList3.clear();
                    unitList3.clear();
                    UnitList3.add(new UnitCusttom("-1", getString(R.string.select_wing), "", "", ""));
                    UnitList3.add(new UnitCusttom("0", getString(R.string.society_office), "", session.getEmegency(), ""));
                    unitList3.add(getString(R.string.select_wing));
                    unitList3.add(getString(R.string.society_office));
                } else {
                    UnitList.clear();
                    unitList.clear();
                    UnitList.add(new UnitCusttom("-1", getString(R.string.select_wing), "", "", ""));
                    UnitList.add(new UnitCusttom("0", getString(R.string.society_office), "", session.getEmegency(), ""));
                    unitList.add(getString(R.string.select_wing));
                    unitList.add(getString(R.string.society_office));
                }

                for (int k = 0; k < jArrayValue.length(); k++) {
                    JSONObject objData = jArrayValue.getJSONObject(k);

                    if (j == 1) {
                        if (wingID.equals(objData.getString("wing_id"))) {
                            UnitList1.add(new UnitCusttom(
                                    objData.getString("unit_id"),
                                    objData.getString("owner_name"),
                                    objData.getString("unit_no"),
                                    objData.getString("mob"),
                                    objData.getString("wing_id")
                            ));

                            String owner = objData.getString("owner_name");
                            if (objData.getString("owner_name").length() < 25) {
                                if (objData.getString("unit_no").equals("")) {
                                    unitList1.add(objData.getString("owner_name"));
                                } else {
                                    unitList1.add(objData.getString("unit_no") + "[" + objData.getString("owner_name") + "]");
                                }
                            } else {
                                owner = owner.substring(0, 25);
                                unitList1.add(objData.getString("unit_no") + " [ " + owner + " ...");
                            }
                        }

                    } else if (j == 2) {
                        if (wingID.equals(objData.getString("wing_id"))) {
                            System.out.println("Count : ");
                            UnitList2.add(new UnitCusttom(
                                    objData.getString("unit_id"),
                                    objData.getString("owner_name"),
                                    objData.getString("unit_no"),
                                    objData.getString("mob"),
                                    objData.getString("wing_id")
                            ));

                            String owner = objData.getString("owner_name");
                            if (objData.getString("owner_name").length() < 25) {
                                if (objData.getString("unit_no").equals("")) {
                                    unitList2.add(objData.getString("owner_name"));
                                } else {
                                    unitList2.add(objData.getString("unit_no") + "[" + objData.getString("owner_name") + "]");
                                }
                            } else {
                                owner = owner.substring(0, 25);
                                unitList2.add(objData.getString("unit_no") + " [ " + owner + " ...");
                            }
                        }
                    } else if (j == 3) {
                        if (wingID.equals(objData.getString("wing_id"))) {
                            UnitList3.add(new UnitCusttom(
                                    objData.getString("unit_id"),
                                    objData.getString("owner_name"),
                                    objData.getString("unit_no"),
                                    objData.getString("mob"),
                                    objData.getString("wing_id")
                            ));

                            String owner = objData.getString("owner_name");
                            if (objData.getString("owner_name").length() < 25) {
                                if (objData.getString("unit_no").equals("")) {
                                    unitList3.add(objData.getString("owner_name"));
                                } else {
                                    unitList3.add(objData.getString("unit_no") + "[" + objData.getString("owner_name") + "]");
                                }
                            } else {
                                owner = owner.substring(0, 25);
                                unitList3.add(objData.getString("unit_no") + " [ " + owner + " ...");
                            }
                        }

                    } else {
                        if (wingID.equals(objData.getString("wing_id"))) {
                            UnitList.add(new UnitCusttom(
                                    objData.getString("unit_id"),
                                    objData.getString("owner_name"),
                                    objData.getString("unit_no"),
                                    objData.getString("mob"),
                                    objData.getString("wing_id")
                            ));
                            String owner = objData.getString("owner_name");
                            if (objData.getString("owner_name").length() < 25) {
                                if (objData.getString("unit_no").equals("")) {
                                    unitList.add(objData.getString("owner_name"));
                                } else {
                                    unitList.add(objData.getString("unit_no") + "[" + objData.getString("owner_name") + "]");
                                }
                            } else {
                                owner = owner.substring(0, 25);
                                unitList.add(objData.getString("unit_no") + " [ " + owner + " ...");
                            }
                        }
                    }
                }

                //UnitCustomAdapter adapter;
                if (j == 1) {
                    unitAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, unitList1);
                    unitAdapter.setDropDownViewResource(R.layout.spinner_item);

                    //adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList1);
                    vUnitSpinner1.setAdapter(unitAdapter);

                } else if (j == 2) {
                    unitAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, unitList2);
                    unitAdapter.setDropDownViewResource(R.layout.spinner_item);
                    //adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList2);
                    vUnitSpinner2.setAdapter(unitAdapter);
                } else if (j == 3) {
                    unitAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, unitList3);
                    unitAdapter.setDropDownViewResource(R.layout.spinner_item);
                    //adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList3);
                    vUnitSpinner3.setAdapter(unitAdapter);
                } else {
                    unitAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, unitList);
                    unitAdapter.setDropDownViewResource(R.layout.spinner_item);
                    //adapter = new UnitCustomAdapter(getApplicationContext(), R.layout.unitlayout, R.id.UnitList, UnitList);
                    vUnitSpinner.setAdapter(unitAdapter);
                }

            }


        } catch (Exception e) {
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
            spinner.performClick();
            // to open the spinner list if error is found.

        }
    }

}
