package com.softel.securitymanager.securitymanager;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class  CustomeAdapterRound extends ArrayAdapter<RoundList>  {
    ArrayList<RoundList> RoundList;
    Context context;
    int resource;

    public CustomeAdapterRound(Context context, int resource, ArrayList<RoundList> RoundList)
    {
        super(context, resource, RoundList);
        this.context=context;
        this.resource=resource;
        this.RoundList=RoundList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fetch_round_list, null, true);
        }
        RoundList roundList = getItem(position);

        TextView roundtype = (TextView) convertView.findViewById(R.id.checkpostno);
        roundtype.setText(roundList.getCheckPostName());

        TextView scheduletype = (TextView) convertView.findViewById(R.id.code);
        scheduletype.setText(roundList.getCheckPostCode());


        return convertView;
    }

}
