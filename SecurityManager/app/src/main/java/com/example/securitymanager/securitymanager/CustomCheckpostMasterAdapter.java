package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomCheckpostMasterAdapter extends ArrayAdapter<fetchCheckPostMaster> {
    ArrayList<fetchCheckPostMaster> fetchCheckPostMaster;
    Context context;
    int resource;

    public CustomCheckpostMasterAdapter(Context context, int resource, ArrayList<fetchCheckPostMaster> fetchCheckPostMaster)
    {
        super(context, resource, fetchCheckPostMaster);
        this.context=context;
        this.resource=resource;
        this.fetchCheckPostMaster=fetchCheckPostMaster;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.checkpost_master_fetch,null,true);
        }
        fetchCheckPostMaster PostMasterList=getItem(position);

        TextView stime=(TextView) convertView.findViewById(R.id.roundType);
        stime.setText(PostMasterList.getRoundsType());

        TextView roundtype=(TextView) convertView.findViewById(R.id.sechType);
        roundtype.setText(PostMasterList.getScheduleType());

        TextView scheduletype=(TextView) convertView.findViewById(R.id.wingType);
        scheduletype.setText(PostMasterList.getWingType());


        TextView createdby = (TextView) convertView.findViewById(R.id.no_of_post);
        createdby.setText(PostMasterList.getNo_Of_Post());

        return convertView;
    }
}
