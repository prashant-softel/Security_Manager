package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomScheduleListAdapter extends ArrayAdapter<fetchschedule>  {
    ArrayList<fetchschedule> fetchschedule;
    Context context;
    int resource;

    public CustomScheduleListAdapter(Context context, int resource, ArrayList<fetchschedule> fetchschedule) {
        super(context, resource, fetchschedule);
        this.context = context;
        this.resource = resource;
        this.fetchschedule = fetchschedule;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_schedule_list, null, true);
        }

        fetchschedule schedule = getItem(position);


        TextView txtround = (TextView) convertView.findViewById(R.id.roundType);
        txtround.setText(schedule.getRound_Type());

        TextView txtwing = (TextView) convertView.findViewById(R.id.wingType);
        txtwing.setText(schedule.getWing_Type());

        TextView txtType = (TextView) convertView.findViewById(R.id.sechType);
        txtType.setText(schedule.getSchedule_Type());

        TextView txtpost = (TextView) convertView.findViewById(R.id.no_of_post);
        txtpost.setText(schedule.getNo_of_Checkpost());

        return convertView;
    }
}
