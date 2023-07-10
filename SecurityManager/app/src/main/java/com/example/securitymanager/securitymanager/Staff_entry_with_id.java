package com.example.securitymanager.securitymanager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;


public class Staff_entry_with_id extends Fragment {

    EditText staff_id, mobile_no;
    // TextView gateno;
    Session session;
    TextView newstaff, txtStaffList,btnScanner;
    ImageView imgQRScanner;
    Button submit, checkInBtn,searchID_btn;

    //Overriden method onCreateView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.staff_entry_with_id, container, false);
        //  gateno = (TextView) view.findViewById(R.id.gateno);
        newstaff = (TextView) view.findViewById(R.id.newstaff);
        txtStaffList = (TextView) view.findViewById(R.id.txtStaffList);
        staff_id = (EditText) view.findViewById(R.id.staffid);
        mobile_no = (EditText) view.findViewById(R.id.staffmobile);
        submit = (Button) view.findViewById(R.id.search_btn);
        searchID_btn = (Button) view.findViewById(R.id.searchID_btn);
        btnScanner = (Button) view.findViewById(R.id.btnScanner);
        imgQRScanner = view.findViewById(R.id.imgQRScanner);

        session = new Session(getContext());
        System.out.println("Staff ID " + staff_id.getText().toString());
        System.out.println("mobile_no " + submit);

        /* gateno.setText(session.getGateNo());*/

        newstaff.setPaintFlags(newstaff.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtStaffList.setPaintFlags(txtStaffList.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        newstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), staff_add.class);
                startActivity(intent);
            }
        });
        txtStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), total_staffexit.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mobile = "";
                if (mobile_no.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Enter staff contact number", Toast.LENGTH_SHORT).show();
                } else if (mobile_no.getText().toString().length() > 0) {
                    if (mobile_no.getText().toString().length() > 10 || mobile_no.getText().toString().length() < 10) {
                        mobile_no.requestFocus();
                        mobile_no.setError("Enter Valid Mobile Number");
                    } else {
                        mobile = mobile_no.getText().toString();
                        Intent staff_checkin = new Intent(getContext(), Staff_CheckIn.class);
                        System.out.print("Test1");
                        staff_checkin.putExtra("StaffID", "");
                        staff_checkin.putExtra("MobileNo", mobile);
                        staff_checkin.putExtra("counter", "1");
                        startActivity(staff_checkin);

                    }
                } else {
                    mobile = mobile_no.getText().toString();
                    Intent staff_checkin = new Intent(getContext(), Staff_CheckIn.class);
                    System.out.print("Test1");
                    staff_checkin.putExtra("StaffID", "");
                    staff_checkin.putExtra("MobileNo", mobile);
                    staff_checkin.putExtra("counter", "1");
                    startActivity(staff_checkin);
                }
            }
        });

        searchID_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sid = "";
                if (staff_id.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Enter either staff id", Toast.LENGTH_SHORT).show();
                }else{
                    sid = staff_id.getText().toString();
                    Intent staff_checkin = new Intent(getContext(), Staff_CheckIn.class);
                    staff_checkin.putExtra("StaffID", sid);
                    staff_checkin.putExtra("MobileNo", "");
                    staff_checkin.putExtra("counter", "1");
                    startActivity(staff_checkin);
                }
            }
        });


        imgQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),QRScannerActivity.class));
            }
        });
        return view;
    }

}


