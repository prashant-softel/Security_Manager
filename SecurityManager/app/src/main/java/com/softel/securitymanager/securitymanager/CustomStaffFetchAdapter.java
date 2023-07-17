package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomStaffFetchAdapter extends ArrayAdapter<stafffetch> {
    ArrayList<stafffetch> stafffetches;
    Context context;
    int resource;
    public CustomStaffFetchAdapter(Context context, int resource, ArrayList<stafffetch> stafffetches)
    {
        super(context, resource, stafffetches);
        this.context=context;
        this.resource=resource;
        this.stafffetches=stafffetches;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.staff_entryfetch,null,true);
        }
        stafffetch stafff=getItem(position);
        //System.out.println("Round"+stafff.getFull_Name());
        TextView txtname=(TextView) convertView.findViewById(R.id.name);
        txtname.setText(stafff.getFull_Name());

        TextView txtprofile=(TextView) convertView.findViewById(R.id.profile);
        txtprofile.setText(stafff.getEntry_Profile());

        TextView txtdate=(TextView) convertView.findViewById(R.id.date);
        txtdate.setText(stafff.getDate());

        TextView txtin = (TextView) convertView.findViewById(R.id.in);
        txtin.setText(stafff.getInTime());

        TextView txtgate=(TextView) convertView.findViewById(R.id.gate);
        txtgate.setText(stafff.getEntry_Gate());
        return convertView;
    }




}
