package com.softel.securitymanager.securitymanager;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomCheckPostAdapter extends ArrayAdapter<fetchcheckpost> {
    ArrayList<fetchcheckpost> fetchcheckpost;
    Context context;
    int resource;

    public CustomCheckPostAdapter(Context context, int resource, ArrayList<fetchcheckpost> fetchcheckpost)
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
            convertView=layoutInflater.inflate(R.layout.checkpost_fetch,null,true);
        }
        fetchcheckpost checkpostList=getItem(position);

        TextView roundtype=(TextView) convertView.findViewById(R.id.rtype);

        roundtype.setText(checkpostList.getRoundType());

        TextView scheduletype=(TextView) convertView.findViewById(R.id.stype);
        scheduletype.setText(checkpostList.getScheduleType());

        TextView stime=(TextView) convertView.findViewById(R.id.stime);
        stime.setText(checkpostList.getScheduleTime());

        //TextView createdby = (TextView) convertView.findViewById(R.id.createdby);
       // createdby.setText(checkpostList.getCreatedBy());

        return convertView;
    }
}
