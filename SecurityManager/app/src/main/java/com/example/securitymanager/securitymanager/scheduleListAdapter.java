package com.example.securitymanager.securitymanager;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/*public class scheduleListAdapter extends ArrayAdapter<fetchschedule> {
    ArrayList<fetchschedule> fetchschedule;
    Context context;
    int resource;

    public scheduleListAdapter(Context context, int resource, ArrayList<fetchschedule> fetchschedule) {
        super(context, resource, fetchschedule);
        this.context = context;
        this.resource = resource;
        this.fetchschedule = fetchschedule;
       // System.out.println("Round"+fetchschedule);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println("Round");
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_schedule_list, null, true);
        }

        fetchschedule schedule = getItem(position);

        //System.out.println("Shedule Data  : "+schedule.getSchedule_Type());
       // System.out.println("Round Data : "+schedule.getRound_Type());
       // System.out.println("Wing  Data :"+schedule.getWing_Type());
       // System.out.println("No Of Data :"+schedule.getNo_of_Checkpost());
        TextView txtround = (TextView) convertView.findViewById(R.id.roundType);
        txtround.setText(schedule.getRound_Type());

        TextView txtwing = (TextView) convertView.findViewById(R.id.wingType);
        txtwing.setText(schedule.getWing_Type());

        TextView txtType = (TextView) convertView.findViewById(R.id.sechType);
        txtType.setText(schedule.getRound_Type());

        TextView txtpost = (TextView) convertView.findViewById(R.id.no_of_post);
        txtpost.setText(schedule.getNo_of_Checkpost());
        //System.out.println("No Of Data :"+convertView);
        return convertView;
    }
}*/