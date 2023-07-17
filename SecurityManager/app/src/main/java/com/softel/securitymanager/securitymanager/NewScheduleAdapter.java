package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewScheduleAdapter extends ArrayAdapter<fetchcheckpost> {

    ArrayList<fetchcheckpost> fetchcheckpost;
    Context context;
    int resource;

    public NewScheduleAdapter(Context context, int resource, ArrayList<fetchcheckpost> fetchcheckpost)
    {
        super(context, resource, fetchcheckpost);
        this.context=context;
        this.resource=resource;
        this.fetchcheckpost=fetchcheckpost;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.upcoming_schedule,null,true);
        }
        fetchcheckpost checkpostList=getItem(position);

        TextView txtscheduleName = (TextView) convertView.findViewById(R.id.scheduleTitle);
        txtscheduleName.setText(checkpostList.getRoundType());

        TextView txtscheduleTime = (TextView) convertView.findViewById(R.id.sheduleTime);
        txtscheduleTime.setText(checkpostList.getScheduleTime());

        return convertView;
    }
}
