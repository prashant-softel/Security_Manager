package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UnitCustomAdapter extends ArrayAdapter<UnitCusttom> {
    ArrayList<UnitCusttom> unitCusttoms;
    Context context;
    int resource;
    int textview;
    public UnitCustomAdapter(Context context, int resource,int textviewid, ArrayList<UnitCusttom> unitCusttoms) {
        super(context, resource,textviewid, unitCusttoms);
        this.context=context;
        this.resource=resource;
        this.textview=textviewid;
        this.unitCusttoms=unitCusttoms;

    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
    {
        return getView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.unitlayout, null, true);
        }
        UnitCusttom unit = getItem(position);

        TextView txtunit = (TextView) convertView.findViewById(R.id.UnitList);
        String owner=unit.getOwnerName();
        if(unit.getOwnerName().length()<25) {
            if (unit.getUnitNo().equals("")) {
                txtunit.setText(unit.getOwnerName());
            } else {
                txtunit.setText(unit.getUnitNo() + " [ " + unit.getOwnerName().toString() + " ]");
            }
        }
        else
        {
            owner= owner.substring(0,25);
            txtunit.setText(unit.getUnitNo() + " [ " + owner  + " ...");
        }
        return convertView;
    }
}