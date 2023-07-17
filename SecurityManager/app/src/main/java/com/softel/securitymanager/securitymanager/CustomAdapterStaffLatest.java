package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapterStaffLatest extends ArrayAdapter<stafflatestlist>{

    ArrayList<stafflatestlist> stafflatestlists;
    Context context;
    int resource;
    public CustomAdapterStaffLatest(Context context, int resource, ArrayList<stafflatestlist> stafflatestlists) {
        super(context, resource, stafflatestlists);
        this.context=context;
        this.resource=resource;
        this.stafflatestlists=stafflatestlists;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.view_lateststaff,null,true);
        }
        stafflatestlist stafflatest=getItem(position);
        TextView txtindate=(TextView) convertView.findViewById(R.id.indate);
        txtindate.setText(stafflatest.getDate());

        TextView txtattendance=(TextView) convertView.findViewById(R.id.attendance);
        if(stafflatest.getAttendance().equals("p"))
        {
            String att="<b> P </b>";
            txtattendance.setTextColor(Color.rgb(0,120,26));
             txtattendance.setText(Html.fromHtml(att));

        }
        else
        {
            txtattendance.setText(stafflatest.getAttendance());
        }
        TextView txtintime=(TextView) convertView.findViewById(R.id.intime);
        txtintime.setText(stafflatest.getIntime());

        TextView txtouttime=(TextView) convertView.findViewById(R.id.outime);
        if(stafflatest.getOuttime().equals("00:00"))
        {
            txtouttime.setText(context.getString(R.string.still_inside));
            txtouttime.setTextColor(Color.RED);
        }
        if(!(stafflatest.getOuttime().equals("00:00")) && !(stafflatest.getOuttime().equals("--")))
        {
            txtouttime.setText(stafflatest.getOuttime());
        }


        return convertView;
    }


}
